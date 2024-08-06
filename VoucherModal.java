/*
 * InSilico Solutions LLP
 * www.insilicoss.com
 */
package com.insilicoss.cab.fxn.voucher;

import com.insilicoss.cab.CabUtil;
import com.insilicoss.cab.cache.FinancialYearReadOnly;
import com.insilicoss.cab.cache.LedgerAccountReadOnly;
import com.insilicoss.cab.cache.VoucherTypeReadOnly;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.OperationResponse;
import com.insilicoss.eventManager.cache.ReadOnlyCacheManager;
import com.insilicoss.exception.PresentableException;
import com.insilicoss.exception.SystemException;
import com.insilicoss.util.Util;
import com.insilicoss.validation.AppRule;
import com.insilicoss.validation.CoreValidator;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.TrackChanges;

/** 
 * 
 * @author admin 
 */ 
@TrackChanges
public class VoucherModal { 
  private String    svsVochrTypeIdy; 
  private String    svsVochrIdy; 
  private LocalDate svdVochrDate; 
  private String    svsVochrNotes; 
  
  @Setter(AccessLevel.NONE)
  private int       sviVochrD = -1;
  
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private DBManager cvoDBManager; 

   @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private final String VALIDATION_GROUP_SAVE   = "save"; 
  
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private final String VALIDATION_GROUP_DELETE = "delete"; 

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  FinancialYearReadOnly cvoFinancialYearReadOnly; 

  private List<VoucherLine> svoVoucherLines = new ArrayList<>(); 

  public VoucherModal(DBManager pvoDBManager){ 
    cvoDBManager = pvoDBManager; 
  } 

  public VoucherModal(int pviVoucherD, DBManager pvoDBManager){ 
    sviVochrD    = pviVoucherD; 
    cvoDBManager = pvoDBManager; 
    _loadVoucherWithD(); 
  } 

  public VoucherModal(String pvsVoucherIdy, DBManager pvoDBManager){ 
    this(pvsVoucherIdy, pvoDBManager, false); 
  } 

  public VoucherModal(String pvsVoucherIdy, DBManager pvoDBManager, boolean pvbCreateNewIfVoucherIdyNotFound){ 
    svsVochrIdy  = pvsVoucherIdy; 
    cvoDBManager = pvoDBManager; 

    if(svsVochrIdy == null || svsVochrIdy.isBlank()){ 
      if(pvbCreateNewIfVoucherIdyNotFound){ 
        svsVochrIdy = cvoDBManager.getNextTxnIdy("VoucherId", 0); 
      } 
      else { 
        throw new PresentableException("Invalid voucher Id. "); 
      } 
    } 
    else { 
      _loadVoucherWithIdy(pvbCreateNewIfVoucherIdyNotFound); 
    } 
  } 

  private void _loadVoucherWithIdy(boolean pvbCreateNewIfVoucherIdyNotFound){ 
    if((svsVochrIdy == null || svsVochrIdy.isBlank())){ 
      if(!pvbCreateNewIfVoucherIdyNotFound)
        throw new PresentableException("Invalid voucher Id. "); 
    } 

    try { 
      cvoDBManager.addContextParam("rvsVochrIdy", svsVochrIdy); 
      ResultSet lvoRsVoucher = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherWithId"); 
      if(lvoRsVoucher.next()){ 
        svsVochrTypeIdy = lvoRsVoucher.getString("svsVochrTypeIdy"); 
        svsVochrIdy     = lvoRsVoucher.getString("svsVochrIdy"); 
        svdVochrDate    = lvoRsVoucher.getDate("svdVochrDate").toLocalDate(); 
        svsVochrNotes   = lvoRsVoucher.getString("svsVochrNotes"); 
        sviVochrD       = lvoRsVoucher.getInt("sviVochrD"); 
        _loadVoucherLines(); 
      } 
      else { 
        if(!pvbCreateNewIfVoucherIdyNotFound)
          throw new PresentableException("Invalid voucher Id. "); 
      } 
    } 
    catch(SQLException se){ 
      throw new SystemException(se.getMessage(), se); 
    } 
  } 

  private void _loadVoucherWithD(){ 
    try { 
      if(sviVochrD < 0) { 
        throw new PresentableException("Invalid voucher reference. "); 
      }
      cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
      ResultSet lvoRsVoucher = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherWithD"); 
      if(lvoRsVoucher.next()){ 
        svsVochrTypeIdy = lvoRsVoucher.getString("svsVochrTypeIdy"); 
        svsVochrIdy     = lvoRsVoucher.getString("svsVochrIdy"); 
        svdVochrDate    = lvoRsVoucher.getDate("svdVochrDate").toLocalDate(); 
        svsVochrNotes   = lvoRsVoucher.getString("svsVochrNotes"); 
      } 
      else { 
        throw new PresentableException("Invalid voucher reference. "); 
      } 

      _loadVoucherLines(); 
    } 
    catch(SQLException se){ 
      throw new SystemException(se.getMessage(), se); 
    } 
  } 

  private void _loadVoucherLines() throws SQLException {
    cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
    ResultSet lvoRsVoucherLine = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherLine");
    VoucherLine lvoVoucherLine;
    while(lvoRsVoucherLine.next()){
      lvoVoucherLine = new VoucherLine();
      lvoVoucherLine.sviVochrLineDsplyOrdr = lvoRsVoucherLine.getInt("sviVochrLineDsplyOrdr");
      lvoVoucherLine.svoLedgerAccount      = CabUtil.getLedgerAccountReadOnly(lvoRsVoucherLine.getInt("sviAcntD"));
      lvoVoucherLine.svnAcntVal            = lvoRsVoucherLine.getBigDecimal("svnAcntVal");
      lvoVoucherLine.svsVochrLineNotes     = lvoRsVoucherLine.getString("svsVochrLineNotes");
      lvoVoucherLine.sviVochrLineD         = lvoRsVoucherLine.getInt("sviVochrLineD");
      svoVoucherLines.add(lvoVoucherLine);
    }
  }

  @AppRule(message = "", order = 1, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse initValidate(){ 
    cvoFinancialYearReadOnly =CabUtil.getFinancialYear(svdVochrDate); 
    if(cvoFinancialYearReadOnly == null){ 
      return new OperationResponse(false, "Date '" + Util.formatDate(svdVochrDate, true) + "' does not belongs to configured financial years."); 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 2, group = {VALIDATION_GROUP_SAVE, VALIDATION_GROUP_DELETE}) 
  private OperationResponse validateVoucherDate() throws SQLException{ 
    //ToDo : Validate if books are open as on voucher date 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 3, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse validateVoucherId() throws SQLException{ 
    if(svsVochrIdy == null || svsVochrIdy.isEmpty()){ 
      return new OperationResponse(false, "Invalid Voucher Id."); 
    } 

    if(cvoFinancialYearReadOnly == null){ 
      return new OperationResponse(false, "Invalid Voucher Date. No financial year found as on Voucher date."); 
    } 

    cvoDBManager.addContextParam("rvsVochrIdy", svsVochrIdy); 
    cvoDBManager.addContextParam("rvdFinclYearDateFrom", cvoFinancialYearReadOnly.svdFinclYearDateFrom); 
    cvoDBManager.addContextParam("rvdFinclYearDateTo", cvoFinancialYearReadOnly.svdFinclYearDateTo); 
    cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
    ResultSet lvoRsVoucherIdValidation = cvoDBManager.selectResultSet("cab\\voucher\\sarVoucherIdValidation"); 
    if(lvoRsVoucherIdValidation.next()){ 
      return new OperationResponse(false, "Duplicate Voucher Id " + svsVochrIdy); 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 4, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse validateVoucherLinesCount(){ 
    if(svoVoucherLines.isEmpty()){ 
      return new OperationResponse(false, "Voucher has no lines."); 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 5, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse validateVoucherLineDsplyOrdr(){ 
    Set<Integer> lvoDsplyOrdrs = new HashSet<>(); 
    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.svbToDel){ 
        continue; 
      }
      if(lvoDsplyOrdrs.contains(lvoVoucherLine.sviVochrLineDsplyOrdr)){ 
        return new OperationResponse(false, "Duplicate line number " + lvoVoucherLine.sviVochrLineDsplyOrdr);  
      } 
      lvoDsplyOrdrs.add(lvoVoucherLine.sviVochrLineDsplyOrdr); 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 6, group = {VALIDATION_GROUP_SAVE}, breakOnError = true) 
  private OperationResponse validateVoucherLineAccounts(){ 
    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.svbToDel){ 
        continue; 
      } 
      if(lvoVoucherLine.svoLedgerAccount == null){ 
        return new OperationResponse(false, "Invalid account in line " + lvoVoucherLine.sviVochrLineDsplyOrdr + ". "); 
      } 

      if(!lvoVoucherLine.svoLedgerAccount.isActiveDuring(svdVochrDate)){ 
        return new OperationResponse(false, "Voucher '" + lvoVoucherLine.svoLedgerAccount.svsAcntIdy + "' is not active as on " + svdVochrDate); 
      } 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 7, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse validateVoucherAmount(){ 
    BigDecimal lvoTotalVchrAmount = BigDecimal.ZERO; 
    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.svbToDel){ 
        continue; 
      }
      if(lvoVoucherLine.svnAcntVal == null || BigDecimal.ZERO.compareTo(lvoVoucherLine.svnAcntVal) == 0){ 
        return new OperationResponse(false, "Invalid voucher line amount for account " + lvoVoucherLine.svoLedgerAccount.svsAcntIdy); 
      } 
      lvoTotalVchrAmount = lvoTotalVchrAmount.add(lvoVoucherLine.svnAcntVal); 
    } 

    if(lvoTotalVchrAmount.compareTo(BigDecimal.ZERO) != 0){ 
      return new OperationResponse(false, "Voucher credit debit missmatch.(diff:" + lvoTotalVchrAmount.toPlainString() + ")"); 
    } 
    return OperationResponse.OK; 
  } 

  @AppRule(message = "", order = 8, group = {VALIDATION_GROUP_SAVE}) 
  private OperationResponse validateVoucherTypeRules(){ 
    VoucherTypeReadOnly lvoVoucherTypeReadOnly = ReadOnlyCacheManager.getInstance().get(VoucherTypeReadOnly.class, svsVochrTypeIdy); 
    if(lvoVoucherTypeReadOnly == null){ 
      return new OperationResponse(false, "Invalid voucher type '" + svsVochrTypeIdy + "'; "); 
    } 

    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.svbToDel){ 
        continue; 
      } 
      if(!lvoVoucherTypeReadOnly.acceptAccountTypes(lvoVoucherLine.svoLedgerAccount.svsAcntType)){ 
        return new OperationResponse(false, "Can't account '" + lvoVoucherLine.svoLedgerAccount.svsAcntType + "' as part of voucher type '" + lvoVoucherTypeReadOnly.svsVochrTypeIdy + "'; "); 
      } 
    } 
    return OperationResponse.OK; 
  } 

  public VoucherLine createNewVoucherLine(){ 
    VoucherLine lvoVoucherLine = new VoucherLine(); 
    svoVoucherLines.add(lvoVoucherLine); 
    return lvoVoucherLine; 
  } 

  public VoucherLine getVoucherLineWithD(int pviVochrLineD){ 
    if(pviVochrLineD == -1){ 
      return null; 
    } 
    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.sviVochrLineD == pviVochrLineD){ 
        return lvoVoucherLine; 
      } 
    } 
    return null; 
  } 

  public VoucherLine getVoucherLineWithLineNo(int pviVochrLineDsplyOrdr){ 
    for (VoucherLine lvoVoucherLine : svoVoucherLines) { 
      if(lvoVoucherLine.sviVochrLineDsplyOrdr == pviVochrLineDsplyOrdr){ 
        return lvoVoucherLine; 
      } 
    } 
    return null; 
  } 

  public void save(boolean pvbCommit){ 
    CoreValidator.validateAutoFlag(this, VALIDATION_GROUP_SAVE); 
    if(isChanged){
      cvoDBManager.addContextParam("rvbVochrActv", "1"); 
      cvoDBManager.addContextParam("rvsVochrTypeIdy", svsVochrTypeIdy); 
      cvoDBManager.addContextParam("rvsVochrIdy", svsVochrIdy); 
      cvoDBManager.addContextParam("rvdVochrDate", svdVochrDate); 
      cvoDBManager.addContextParam("rvsVochrNotes", svsVochrNotes); 
      if(sviVochrD == -1){ 
        cvoDBManager.update("cab\\voucher\\i1rVoucher", true); 
        sviVochrD = cvoDBManager.cviRecentAutoGenKey; 
      } 
      else{ 
        cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
        cvoDBManager.update("cab\\voucher\\u1rVoucher"); 
      } 
    }

    for (VoucherLine lvoVoucherLine : svoVoucherLines) {
     
      if(lvoVoucherLine.isChanged){
        cvoDBManager.addContextParam("rvbVochrLineActv", "1"); 
        cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
        cvoDBManager.addContextParam("rviVochrLineDsplyOrdr", lvoVoucherLine.sviVochrLineDsplyOrdr); 
        cvoDBManager.addContextParam("rvsVochrLineNotes", lvoVoucherLine.svsVochrLineNotes); 
        cvoDBManager.addContextParam("rviAcntD", lvoVoucherLine.svoLedgerAccount.sviAcntD); 
        cvoDBManager.addContextParam("rvnAcntVal", lvoVoucherLine.svnAcntVal); 
        if(lvoVoucherLine.sviVochrLineD == -1){ 
          if(!lvoVoucherLine.svbToDel){ 
            cvoDBManager.update("cab\\voucher\\i1rVoucherLine", true); 
            lvoVoucherLine.sviVochrLineD = cvoDBManager.cviRecentAutoGenKey; 
          } 
        } 
        else { 
          if(lvoVoucherLine.svbToDel){ 
            cvoDBManager.addContextParam("rviVochrLineD", lvoVoucherLine.sviVochrLineD); 
            cvoDBManager.update("cab\\voucher\\u1rDActvtVoucherLine"); 
          } 
          else{ 
            cvoDBManager.addContextParam("rviVochrLineD", lvoVoucherLine.sviVochrLineD); 
            cvoDBManager.update("cab\\voucher\\u1rVoucherLine"); 
          } 
        } 
      }
    } 

    if(pvbCommit){ 
      cvoDBManager.commitTrans(); 
    } 
  } 

  public void delete(boolean pvbCommit){ 
    CoreValidator.validateAutoFlag(this, VALIDATION_GROUP_DELETE); 
    cvoDBManager.addContextParam("rviVochrD", sviVochrD); 
    cvoDBManager.update("cab\\voucher\\uarDactvtVoucherLine"); /* Delete lines first */ 
    cvoDBManager.update("cab\\voucher\\u1rDactvtVoucher");     /* Delete Voucher */ 

    if(pvbCommit){ 
      cvoDBManager.commitTrans(); 
    } 
  } 

  @TrackChanges
  public class VoucherLine { 
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private final String           uuid = UUID.randomUUID().toString(); 
    private  int                   sviVochrLineDsplyOrdr; 
    private  LedgerAccountReadOnly svoLedgerAccount; 
    private  BigDecimal            svnAcntVal; 
    private  String                svsVochrLineNotes; 
    private  boolean               svbToDel = false; 
    @Setter(AccessLevel.NONE)
    private int                   sviVochrLineD = -1; 

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      } 

      final VoucherLine other = (VoucherLine) obj; 
      return this.uuid.equalsIgnoreCase(other.uuid); 
    } 
  } 
} 