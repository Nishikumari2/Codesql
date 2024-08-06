/*
 * InSilico Solutions LLP
 * www.insilicoss.com
 */
package com.insilicoss.cab.fxn.voucher;

import com.insilicoss.cab.CabCnstntDbVar;
import com.insilicoss.cab.CabUtil;
import com.insilicoss.cab.cache.VoucherTypeReadOnly;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.CoreRequest;
import com.insilicoss.eventManager.CoreResponse;
import com.insilicoss.eventManager.Row;
import com.insilicoss.eventManager.RowSet;
import com.insilicoss.eventManager.cache.ReadOnlyCacheManager;
import com.insilicoss.exception.PresentableException;
import com.insilicoss.messaging.CoreMessage;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class VoucherController { 
  CoreRequest cvoCoreReq; 
  CoreResponse cvoCoreRes; 
  Logger cvoLogger; 
  DBManager cvoDBManager; 

  public VoucherController(CoreRequest coreReq, CoreResponse coreRes) { 
    cvoCoreReq   = coreReq; 
    cvoCoreRes   = coreRes; 
    cvoLogger    = LogManager.getLogger(); 
    cvoDBManager = cvoCoreReq.getDBManager(); 
    cvoDBManager.addContextParamFromClass(CabCnstntDbVar.class); 
  } 

  public void loadNewVchr(){ 
    String lvsVochrTypeIdyToCreate = cvoCoreReq.getVal("svsVochrTypeIdyToCreate"); 
    if(lvsVochrTypeIdyToCreate == null){ 
      lvsVochrTypeIdyToCreate = cvoCoreReq.getParameter("svsVochrTypeIdyToCreate"); 
    } 

    if(lvsVochrTypeIdyToCreate == null){ 
      throw new PresentableException("Invalid voucher type."); 
    } 

    loadAccountsForVoucher(lvsVochrTypeIdyToCreate); 
    cvoCoreRes.setVal("svsVochrTypeIdy", lvsVochrTypeIdyToCreate); 
    cvoCoreRes.setViewId("add");
  } 

  private void loadAccountsForVoucher(String pvsVochrTypeIdy){ 
    VoucherTypeReadOnly lvoVoucherTypeReadOnly = ReadOnlyCacheManager.getInstance().get(VoucherTypeReadOnly.class, pvsVochrTypeIdy); 
    if(lvoVoucherTypeReadOnly == null){ 
      throw new PresentableException("Invalid voucher type '" + pvsVochrTypeIdy + "'"); 
    } 

    cvoDBManager.addContextParam("rvbShowAllAcntType", lvoVoucherTypeReadOnly.acceptAllAccountTypes()?"1":"0"); 
    String lvsAccTypes = cvoDBManager.convertToDBString(lvoVoucherTypeReadOnly.getAcceptAccountTypes()); 
    if(lvsAccTypes == null || lvsAccTypes.isBlank()){
      lvsAccTypes = "''"; 
    }
    cvoDBManager.addContextParam("_rvsAcntType", lvsAccTypes); 
    cvoCoreRes.setVal("svoAcntDs", cvoDBManager.selectResultSet("cab\\voucher\\sarAcnts")); 
  } 

  public void loadVchrToEdit() throws SQLException{ 
    loadRevision(); 
    loadAccountsForVoucher(""+cvoCoreRes.getVal("svsVochrTypeIdy")); 
  } 

  public void deleteVoucher(){ 
    String lvsVochrIdy = cvoCoreReq.getVal("svsVochrIdy"); 
    VoucherModal lvoVoucher = new VoucherModal(lvsVochrIdy, cvoDBManager, false); 
    lvoVoucher.delete(true); 
    cvoCoreRes.addMessage(new CoreMessage("Voucher deleted successfully.", CoreMessage.SUCCESS_TYPE, CoreMessage.STATUS_OK)); 
  } 

  public void saveVchr() throws SQLException { 
    String    lvsVochrTypeIdy = cvoCoreReq.getVal("svsVochrTypeIdy"); 
    String    lvsVochrIdy     = cvoCoreReq.getVal("svsVochrIdy"); 
    LocalDate lvdVochrDate    = cvoCoreReq.getDateVal("svdVochrDate"); 
    String    lvsVochrNotes   = cvoCoreReq.getVal("svsVochrNotes"); 
    RowSet    lvoVochrLines   = cvoCoreReq.getRowSet("svoVochrLines"); 

    VoucherModal lvoVoucher = new VoucherModal(lvsVochrIdy, cvoDBManager, true); 
    lvoVoucher.setVochrTypeIdy(lvsVochrTypeIdy);
    lvoVoucher.setVochrDate(lvdVochrDate);
    lvoVoucher.setVochrNotes(lvsVochrNotes); 

    VoucherModal.VoucherLine lvoVoucherLine; 
    while(lvoVochrLines.hasNext()) { 
      Row lvoRowVocherLine = lvoVochrLines.moveNext(); 
      int lviVochrLineD = lvoRowVocherLine.getIntVal("sviVochrLineD", -1); 
      if(lvoRowVocherLine.isDeletedRow()){ 
        if(lviVochrLineD == -1){ 
          continue; 
        } 
        lvoVoucherLine = lvoVoucher.getVoucherLineWithD(lviVochrLineD); 
        lvoVoucherLine.setToDel(true);
      } 
      else { 
        lvoVoucherLine = lviVochrLineD == -1? lvoVoucher.createNewVoucherLine() : lvoVoucher.getVoucherLineWithD(lviVochrLineD); 
        lvoVoucherLine.setVochrLineDsplyOrdr(lvoRowVocherLine.getIntVal("sviVochrLineDsplyOrdr")); 
        lvoVoucherLine.setLedgerAccount(CabUtil.getLedgerAccountReadOnly(lvoRowVocherLine.getVal("svsAcntId"))); 
        lvoVoucherLine.setVochrLineNotes(lvoRowVocherLine.getVal("svsVochrLineNotes")); 
        BigDecimal lvnAcntValDr = lvoRowVocherLine.getNumberVal("svnAcntValDr", BigDecimal.ZERO); 
        BigDecimal lvnAcntValCr = lvoRowVocherLine.getNumberVal("svnAcntValCr", BigDecimal.ZERO); 
        lvoVoucherLine.setAcntVal((lvnAcntValDr.compareTo(BigDecimal.ZERO) != 0)? lvnAcntValDr : lvnAcntValCr.negate()); 
      } 
    } 
    lvoVoucher.save(true); 
    cvoCoreRes.addMessage(new CoreMessage("Voucher saved successfully.", CoreMessage.SUCCESS_TYPE, CoreMessage.STATUS_OK)); 
    cvoCoreReq.setVal("svsVochrIdy", lvoVoucher.getVochrIdy()); 
    loadRevision(); 
  } 
  
  public void loadVchr() throws SQLException{ 
    String lvsVochrIdy   = cvoCoreReq.getVal("svsVochrIdy"); 
    String svtRevisionAt = cvoCoreReq.getVal("svtRevisionAt"); 
    
    if(lvsVochrIdy == null){ 
      lvsVochrIdy = cvoCoreReq.getParameter("svsVochrIdy"); 
    } 
    
    if(lvsVochrIdy == null){ 
      throw new PresentableException("Invalid voucher Id."); 
    } 
    
    if(svtRevisionAt == null){ 
      throw new PresentableException("Invalid voucher revision date "); 
    }
    
    VoucherModal lvoVoucher = new VoucherModal(lvsVochrIdy, cvoDBManager, true);
    cvoDBManager.addContextParam("rviVochrD",     lvoVoucher.getVochrD()); 
    cvoDBManager.addContextParam("rvtVochrLineA", svtRevisionAt);
    ResultSet lvoVoucherRevisionView = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherRevisionView"); 
    if(lvoVoucherRevisionView.next()){
      cvoCoreRes.setVal("svsVochrTypeIdy",       lvoVoucherRevisionView.getString("svsVochrTypeIdy")); 
      cvoCoreRes.setVal("svsVochrIdy",           lvoVoucherRevisionView.getString("svsVochrIdy")); 
      cvoCoreRes.setVal("svsVochrNotes",         lvoVoucherRevisionView.getString("svsVochrNotes")); 
      cvoCoreRes.setVal("svdVochrDate",          lvoVoucherRevisionView.getDate("svdVochrDate").toLocalDate()); 
      
      ResultSet lvoVoucherLineRevisionView = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherLineRevisionView"); 
      cvoCoreRes.setVal("svoVochrLines",lvoVoucherLineRevisionView); 
      cvoCoreRes.setViewId("view"); 
    }
    else{ 
      throw new PresentableException("Invalid Voucher Id"); 
    } 
  } 

  public void loadRevision() throws SQLException{
    String lvsVochrIdy = cvoCoreReq.getVal("svsVochrIdy"); 
    
    if(lvsVochrIdy == null){ 
      lvsVochrIdy = cvoCoreReq.getParameter("svsVochrIdy"); 
    } 
    
    if(lvsVochrIdy == null){ 
      throw new PresentableException("Invalid voucher Id."); 
    } 
    cvoDBManager.addContextParam("rvsVochrIdy", lvsVochrIdy); 
    ResultSet svoVoucherRevision = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherRevision");
    if(svoVoucherRevision.first()){
      cvoCoreReq.setVal("svtRevisionAt",         svoVoucherRevision.getString("svtRevisionAt"));
      cvoCoreRes.setVal("svsVochrRevisionDesc", svoVoucherRevision.getString("svsVochrRevisionDesc"));
    }
    svoVoucherRevision.beforeFirst();
    cvoCoreRes.setVal("svoVochrRevisionDesc",svoVoucherRevision); 
    loadVchr();
  }
  
} 