package com.insilicoss.bizNty;

import com.insilicoss.App;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.CoreRequest;
import com.insilicoss.eventManager.CoreResponse;
import com.insilicoss.eventManager.Row;
import com.insilicoss.eventManager.RowSet;
import com.insilicoss.eventManager.cache.ReadOnlyCacheManager;
import com.insilicoss.exception.PresentableException;
import com.insilicoss.messaging.CoreMessage;
import com.insilicoss.sparc.SparcCnstntDbVar;
import com.insilicoss.sparc.cache.HldyGrpReadOnly;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
// *
 * author Alaknanda
 */
public class BizNtyWebControler {
  private CoreRequest cvoCoreRequest;
  private CoreResponse cvoCoreResponse;
  private  DBManager cvoDbManager;
  private Logger cvoLogger; 

  public BizNtyWebControler(CoreRequest pvoCoreRequest, CoreResponse pvoCoreResponse) {
    cvoCoreRequest = pvoCoreRequest;
    cvoCoreResponse = pvoCoreResponse;
    cvoDbManager = cvoCoreRequest.getDBManager();
    cvoLogger = LogManager.getLogger(); 
    cvoDbManager.addContextParamFromClass(SparcCnstntDbVar.class);
  }

  public void loadBizNtyNdLocSmry() {
    ResultSet lvoBizNty = cvoDbManager.selectResultSet("BizNtty\\sarBizNtyNdBizNtyLocSmry");
    cvoCoreResponse.setVal("svoBizNtySmry", lvoBizNty);
  } 
   
  public void getBizNtyName() throws SQLException{
   DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviBizNtyD = cvoCoreRequest.getIntVal("sviSmryBizNtyD");
    lvoDbManager.addContextParam("rviBizNtyD",lviBizNtyD);
    ResultSet lvoRsBizNtyName = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyName");
    cvoCoreResponse.setVal("svoDsBizNtyName", lvoRsBizNtyName);
  }
  
   public void getBizNtyRepName() throws SQLException{
   DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviBizNtyD = cvoCoreRequest.getIntVal("sviSmryBizNtyD");
    lvoDbManager.addContextParam("rviBizNtyD",lviBizNtyD);
    ResultSet lvoRsBizNtyName = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyRepName");
    cvoCoreResponse.setVal("svoDsBizNtyRepName", lvoRsBizNtyName);
  }
   
  public void loadBizNtyInAddNewMode() { 
    cvoCoreResponse.setJsonVal("svoBizNty", "[{}]");
    cvoCoreResponse.setJsonVal("svoBizNtyLoc", "[{}]");
    cvoCoreResponse.setVal("svbIsFromAddNew", "1");
    cvoCoreResponse.setViewId("editBizNty"); 
    
  } 
  
  public void loadNdvdlAddScreen() { 
    cvoCoreResponse.setVal("svbIsFromAddNew", "1");
    cvoCoreResponse.setJsonVal("svoNdvdl","[{}]");
    cvoCoreResponse.setViewId("editAddNdvdl"); 
  } 
  
  public void getFullNtyDts() throws SQLException {
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviBizNtyD      =  cvoCoreRequest.getIntVal("sviSmryBizNtyD");
    lvoDbManager.addContextParam("rviBizNtyD",lviBizNtyD);
    ResultSet lvoRsBizNtyDtls = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyRevs");
    cvoCoreResponse.setVal("svoBizNty",lvoRsBizNtyDtls);
  }
 
  public void getFullNtyLocDtls() throws SQLException{
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviBizNtyLocD      = cvoCoreRequest.getIntVal("sviSmryBizNtyLocD");
    lvoDbManager.addContextParam("rviBizNtyLocD",lviBizNtyLocD);
    ResultSet lvoRsBizNtyLocDtls = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyLocRevs");
    cvoCoreResponse.setVal("svoBizNtyLoc",lvoRsBizNtyLocDtls);
  }
  
 
  
  public void getNdvdlDtls() throws SQLException{
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviBizNtyD = cvoCoreRequest.getIntVal("sviSmryBizNtyD",-1);
    if(lviBizNtyD != -1){
    lvoDbManager.addContextParam("rviBizNtyD",lviBizNtyD);
    ResultSet lvoRsNdvdlDtls = lvoDbManager.selectResultSet("BizNtty\\sarNdvdlCmpnyDtls");
    cvoCoreResponse.setVal("svoNdvdlDtls",lvoRsNdvdlDtls);
    }
    else
    {
     throw new PresentableException("Invalid Member Id");
    }
  }
  
  public void settingNdvdlType(){
    cvoCoreResponse.setVal("svsMembrTypeNdvdlDesc","Individual");
  } 

  public void getNtyNdLocDtls() throws SQLException { 
    DBManager lvoDbManager = cvoCoreRequest.getDBManager(); 
    int lviBizNtyD = cvoCoreRequest.getIntVal("sviSmryBizNtyD", -1); 

    if (lviBizNtyD != -1){ 
      lvoDbManager.addContextParam("rviBizNtyD", lviBizNtyD); 
      ResultSet lvoRsBizNtyDtls    = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyDtls");
      ResultSet lvoRsBizNtyLocDtls = lvoDbManager.selectResultSet("BizNtty\\sarBizNtyLocDtls");
      if(lvoRsBizNtyLocDtls.next()){
        String lvsStateIdy = lvoRsBizNtyLocDtls.getString("svsStateIdy");
        cvoCoreResponse.setVal("svsStateIdy",lvsStateIdy);
        String lvsCntryIdy = lvoRsBizNtyLocDtls.getString("svsCntryIdy");
        cvoCoreResponse.setVal("svsCntryIdy",lvsCntryIdy);
        String lvsCntryDesc = lvoRsBizNtyLocDtls.getString("svsCntryDesc");
        cvoCoreResponse.setVal("svsCntryDesc",lvsCntryDesc);
        String lvsStateDesc = lvoRsBizNtyLocDtls.getString("svsStateDesc");
        cvoCoreResponse.setVal("svsStateDesc",lvsStateDesc);
        String lvsPtAuthorityIdy = lvoRsBizNtyLocDtls.getString("svsPtAuthorityIdy");
        cvoCoreResponse.setVal("svsPtAuthorityIdy",lvsPtAuthorityIdy);
        /*String lvsPtAuthorityDesc = lvoRsBizNtyLocDtls.getString("svsPtAuthorityDesc");
        cvoCoreResponse.setVal("svsPtAuthorityDesc",lvsPtAuthorityDesc);*/
      }
      cvoCoreResponse.setVal("svoBizNty", lvoRsBizNtyDtls); 
      cvoCoreResponse.setVal("svoBizNtyLoc", lvoRsBizNtyLocDtls); 
    }
    else { 
      throw new PresentableException("Invalid Business Id"); 
    } 
  } 

  public void saveBizNtyAndLocs()throws SQLException{
    RowSet lvoBizNty = cvoCoreRequest.getRowSet("svoBizNty");
    BizNtyModel lvoBizNtyModel = null; 
    Row lvoRowBizNty = lvoBizNty.moveToRow(0); 
    lvoBizNtyModel = BizNtyModel.load(lvoRowBizNty.getIntVal("sviBizNtyD",-1), cvoDbManager); 
    lvoBizNtyModel.setBizNtyIdy(lvoRowBizNty.getVal("svsBizNtyIdy"));
    lvoBizNtyModel.setBizNtyName(lvoRowBizNty.getVal("svsBizNtyName"));
    /*lvoBizNtyModel.setBizNtyFrom(lvoRowBizNty.getDateVal("svdBizNtyFrom", App.DEFAULT_MIN_DATE));
    lvoBizNtyModel.setBizNtyTo(lvoRowBizNty.getDateVal("svdBizNtyTo", App.DEFAULT_MAX_DATE));*/
    lvoBizNtyModel.setBizNtyNcomTaxIdy(lvoRowBizNty.getVal("svsBizNtyNcomTaxIdy"));
    lvoBizNtyModel.setBizNtyNcomTaxWthdgIdy(lvoRowBizNty.getVal("svsBizNtyNcomTaxWthdgIdy"));
    lvoBizNtyModel.setBizNtyMSMEIdy(lvoRowBizNty.getVal("svsBizNtyMSMEIdy"));
    lvoBizNtyModel.setBizNtyLegalIdy(lvoRowBizNty.getVal("svsBizNtyLegalIdy"));
    String lvsIsBizOnr = lvoRowBizNty.getVal("svsBizNtyIsBizOnr"); 
    if(lvsIsBizOnr == null || lvsIsBizOnr.isBlank()){ 
      lvsIsBizOnr = "0"; 
    } 
    lvoBizNtyModel.setBizNtyIsBizOnr(lvsIsBizOnr); 
    String lvsIsCstmr = lvoRowBizNty.getVal("svsBizNtyIsCstmr");
     if(lvsIsCstmr == null || lvsIsCstmr.isBlank()){ 
      lvsIsCstmr = "0"; 
    } 
    lvoBizNtyModel.setBizNtyIsCstmr(lvsIsCstmr);

    String lvsIsVndr = lvoRowBizNty.getVal("svsBizNtyIsVndr");
     if(lvsIsVndr == null || lvsIsVndr.isBlank()){ 
      lvsIsVndr = "0"; 
    } 
    lvoBizNtyModel.setBizNtyIsVndr(lvsIsVndr); 
    
    RowSet lvoBizNtyLoc = cvoCoreRequest.getRowSet("svoBizNtyLoc");
    while(lvoBizNtyLoc.hasNext()) { 
      Row lvoRowBizNtyLoc = lvoBizNtyLoc.moveNext(); 
      BizNtyLocModel lvoBizNtyLocModel = lvoBizNtyModel.getBizNtyLocModel(lvoRowBizNtyLoc.getIntVal("sviBizNtyLocD",-1)); 

      if(lvoBizNtyLocModel == null) { 
        lvoBizNtyLocModel = lvoBizNtyModel.createBizNtyLocModel(); 
      } 

      lvoBizNtyLocModel.setBizNtyLocIdy(lvoRowBizNtyLoc.getVal("svsBizNtyLocIdy"));
      lvoBizNtyLocModel.setBizNtyLocName(lvoRowBizNtyLoc.getVal("svsBizNtyLocName"));
      /*lvoBizNtyLocModel.setBizNtyLocFrom(lvoRowBizNtyLoc.getDateVal("svdBizNtyLocFrom", App.DEFAULT_MIN_DATE));
      lvoBizNtyLocModel.setBizNtyLocTo(lvoRowBizNtyLoc.getDateVal("svdBizNtyLocTo", App.DEFAULT_MAX_DATE));*/
      lvoBizNtyLocModel.setBizNtyLocAdrsLine1(lvoRowBizNtyLoc.getVal("svsBizNtyLocAdrsLine1"));
      lvoBizNtyLocModel.setBizNtyLocAdrsLine2(lvoRowBizNtyLoc.getVal("svsBizNtyLocAdrsLine2"));
      lvoBizNtyLocModel.setBizNtyLocPostCode(lvoRowBizNtyLoc.getVal("svsBizNtyLocPostCode"));
      lvoBizNtyLocModel.setBizNtyLocState(cvoCoreRequest.getVal("svsStateIdy"));
      lvoBizNtyLocModel.setBizNtyLocCntry(cvoCoreRequest.getVal("svsCntryIdy"));
      lvoBizNtyLocModel.setBizNtyLocSalesTaxIdy(lvoRowBizNtyLoc.getVal("svsBizNtyLocSalesTaxIdy"));
      lvoBizNtyLocModel.setBizNtyLocCrdtPrid(lvoRowBizNtyLoc.getIntVal("sviBizNtyLocCrdtPrid", 0));
      lvoBizNtyLocModel.setBizNtyLocSalesTaxType(lvoRowBizNtyLoc.getVal("svsBizNtyLocSalesTaxType"));
      String lvsIsIsRegOff = lvoRowBizNtyLoc.getVal("svsBizNtyLocIsRegOff"); 
      if(lvsIsIsRegOff == null || lvsIsIsRegOff.isBlank()){ 
        lvsIsIsRegOff = "0"; 
      } 
      lvoBizNtyLocModel.setBizNtyLocIsRegOff(lvsIsIsRegOff);
      String lvsBizNtyLocPtAthry = cvoCoreRequest.getVal("svsPtAuthorityIdy");
      if(lvsBizNtyLocPtAthry == null || lvsBizNtyLocPtAthry.isBlank())
      {
        lvsBizNtyLocPtAthry = "0";
      }
      lvoBizNtyLocModel.setBizNtyLocPtAthry(lvsBizNtyLocPtAthry);
      String lvsIsMtrReg = lvoRowBizNtyLoc.getVal("svsBizNtyLocIsMtrReg"); 
      if(lvsIsMtrReg == null || lvsIsMtrReg.isBlank()){ 
        lvsIsMtrReg = "0"; 
      } 
      lvoBizNtyLocModel.setBizNtyLocIsMtrReg(lvsIsMtrReg);

      String lvsSysHostLocCanSell = lvoRowBizNtyLoc.getVal("svsBizNtySysHostLocCanSell"); 
      if(lvsSysHostLocCanSell == null || lvsSysHostLocCanSell.isBlank()){ 
        lvsSysHostLocCanSell = "0"; 
      } 
      lvoBizNtyLocModel.setBizNtySysHostLocCanSell(lvsSysHostLocCanSell);
      String lvsSysHostLocCanPchs = lvoRowBizNtyLoc.getVal("svsBizNtySysHostLocCanPchs"); 
      if(lvsSysHostLocCanPchs == null || lvsSysHostLocCanPchs.isBlank()){ 
        lvsSysHostLocCanPchs = "0"; 
      } 
      lvoBizNtyLocModel.setBizNtySysHostLocCanPchs(lvsSysHostLocCanPchs);
      String lvsHldyGrp = lvoRowBizNtyLoc.getVal("svsHldyGrpDesc"); 
      cvoLogger.debug("svsHldyGrpDesc :: " + lvsHldyGrp); 
      HldyGrpReadOnly lvoHldyGrpReadOnly = ReadOnlyCacheManager.getInstance().getFirst(HldyGrpReadOnly.class, (t) -> { return t.svsHldyGrpName.equalsIgnoreCase(lvsHldyGrp); }); 
      if(lvoHldyGrpReadOnly == null){ 
        lvoBizNtyLocModel.setBizNtyHldyGrpD(-1); 
      }  
      else { 
        lvoBizNtyLocModel.setBizNtyHldyGrpD(lvoHldyGrpReadOnly.sviHldyGrpD); 
      } 
    } 

    lvoBizNtyModel.save(); 
    if( cvoDbManager.canCommit()){ 
      cvoDbManager.commitTrans(); 
      cvoCoreResponse.addMessage(CoreMessage.RECORDS_SUBMITTED_SUCCESSFULLY); 
    } 
    loadBizNtyNdLocSmry(); 
  } 
  
  public void getFullMembrDtls() throws SQLException{
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
     int lviBizNtyMembrD = cvoCoreRequest.getIntVal("sviBizNtyMembrD");
     lvoDbManager.addContextParam("rviBizNtyMembrD",lviBizNtyMembrD);
     ResultSet lvoBizNtyMembrRw = lvoDbManager.selectResultSet("BizNtty\\sarLoadMemCmpnyDtls");
     
     if(lvoBizNtyMembrRw.next()){
      String lvsMembrType = lvoBizNtyMembrRw.getString("svsMembrType");
      cvoCoreResponse.setVal("svsMembrType",lvsMembrType);
      if(lvsMembrType.equals("I"))
      {
        cvoCoreResponse.setVal("svsNdvdlName", lvoBizNtyMembrRw.getString("svsNdvdlName"));
        cvoCoreResponse.setVal("sviNdvdlD", lvoBizNtyMembrRw.getInt("sviNdvdlD"));
      }
      else
      {
        cvoCoreResponse.setVal("svsMembrName",lvoBizNtyMembrRw.getString("svsMembrName"));
        cvoCoreResponse.setVal("sviBizNtyMembrNameD",lvoBizNtyMembrRw.getInt("sviBizNtyMembrNameD"));
      }
      
      String lvsMembrSts = lvoBizNtyMembrRw.getString("svsMembrSts");
      cvoCoreResponse.setVal("svsMembrSts",lvsMembrSts);
      if (("C".equals(lvsMembrType) && "S".equals(lvsMembrSts))) 
      {
        cvoCoreResponse.setVal("svsNdvdlRep",lvoBizNtyMembrRw.getString("svsNdvdlRep"));
        cvoCoreResponse.setVal("sviNdvdlRepD",lvoBizNtyMembrRw.getInt("sviBizNtyMembrRepD"));
      }
      if(("I".equals(lvsMembrType) && "D".equals(lvsMembrSts)))
      {
        cvoCoreResponse.setVal("svsBizNtyRep",lvoBizNtyMembrRw.getString("svsBizNtyRep"));
        cvoCoreResponse.setVal("sviBizNtyRepD",lvoBizNtyMembrRw.getInt("sviBizNtyMembrRepD"));
      }
      cvoCoreResponse.setVal("svsMembrStsDescOther",lvoBizNtyMembrRw.getString("svsMembrStsDescOther"));
      cvoCoreResponse.setVal("sviNmbrOfShares",lvoBizNtyMembrRw.getString("sviNmbrOfShares"));
      cvoCoreResponse.setVal("svnShareFaceVal",lvoBizNtyMembrRw.getString("svnShareFaceVal"));
      cvoCoreResponse.setVal("svdEfctvDateFrom",lvoBizNtyMembrRw.getDate("svdEfctvDateFrom"));
      cvoCoreResponse.setVal("svdFfctvDateTo",lvoBizNtyMembrRw.getDate("svdEfctvDateTo"));
     }
  }
  
  /*public void getDINandDigSgntr()throws SQLException{
    cvoLogger.debug("===========");
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    String lviNdvdlD    = cvoCoreRequest.getVal("sviNdvdlD");
    lvoDbManager.addContextParam("rviNdvdlD",lviNdvdlD);
    ResultSet lvoDinNdDscDate = lvoDbManager.selectResultSet("BizNtty\\sarDinNdDscDate");
    lvoDinNdDscDate.next();
    String lvsNdvdlLegalIdy =  lvoDinNdDscDate.getString("svsNdvdlLegalIdy");
    String lvdNdvdlDigSigXpiryDate = lvoDinNdDscDate.getString("svdNdvdlDigSigXpiryDate");
    cvoCoreResponse.setVal("svsNdvdlLegalIdy",lvsNdvdlLegalIdy); 
    cvoCoreResponse.setVal("svdNdvdlDigSigXpiryDate",lvdNdvdlDigSigXpiryDate);
  }*/
  
  public void saveBizNtyMembrDtls() throws SQLException{
    
    BizNtyMembrModel bizNtyMembrModel  = BizNtyMembrModel.load(cvoCoreRequest.getIntVal("sviBizNtyMembrD",-1), cvoDbManager);
    
    bizNtyMembrModel.setBizNtyD(cvoCoreRequest.getIntVal("sviSmryBizNtyD",-1));

    String svsMembrType = cvoCoreRequest.getVal("svsMembrType");
    bizNtyMembrModel.setMembrType(svsMembrType);
   
    if( svsMembrType.equals("I"))
    {
      /*String lvsNdvdlName = cvoCoreRequest.getVal("svsNdvdlName");
      cvoDbManager.addContextParam("rvsNdvdlName",lvsNdvdlName);*/
      //String lvsNdvdlLegalIdy = cvoCoreRequest.getVal("svsNdvdlLegalIdy");
      //cvoDbManager.addContextParam("rvsNdvdlLegalIdy",lvsNdvdlLegalIdy);
      //ResultSet lviMembrD = cvoDbManager.selectResultSet("BizNtty\\s1rNdvdlD");
      //lviMembrD.next();
      //int membrD = lviMembrD.getInt("sviMembrD");
      //bizNtyMembrModel.setMembrD(membrD);

      bizNtyMembrModel.setMembrD(cvoCoreRequest.getIntVal("sviNdvdlD"));
    }
    else
    {
     //String lvsMembrName = cvoCoreRequest.getVal("svsMembrName");
     //cvoDbManager.addContextParam("rvsBizNtyName",lvsMembrName);
     //ResultSet lviMembrD  = cvoDbManager.selectResultSet("BizNtty\\s1rBizNtyNameD");
     //lviMembrD.next(); 
     //int membrD = lviMembrD.getInt("sviMembrD");
     //bizNtyMembrModel.setMembrD(membrD);
      bizNtyMembrModel.setMembrD(cvoCoreRequest.getIntVal("sviBizNtyMembrNameD"));

    }
    
    String lvsMembrSts = cvoCoreRequest.getVal("svsMembrSts");
    bizNtyMembrModel.setMembrSts(lvsMembrSts);
    
     /*String lvsMembrStsDescOther = cvoCoreRequest.getVal("svsMembrStsDescOther");
      if(lvsMembrStsDescOther == null || lvsMembrStsDescOther.isBlank())
      {
        lvsMembrStsDescOther = " ";
      }*/
    if(lvsMembrSts.equals('I'))
      bizNtyMembrModel.setMembrStsDesc("Individual");
    else if(lvsMembrSts.equals('D'))
       bizNtyMembrModel.setMembrStsDesc("Director");
    else
      bizNtyMembrModel.setMembrStsDesc("Others");

    if ( "C".equals(svsMembrType) && "S".equals(lvsMembrSts)) 
    {
    /*String lvsRepName = cvoCoreRequest.getVal("svsRep");
    cvoDbManager.addContextParam("rvsNdvdlName",lvsRepName);
    ResultSet lviMembrD = cvoDbManager.selectResultSet("BizNtty\\s1rRepD");
    lviMembrD.next();
    int membrD = lviMembrD.getInt("sviRepD");*/
    bizNtyMembrModel.setBizNtyRepD(cvoCoreRequest.getIntVal("sviNdvdlRepD",0));
    }
    //else
    //{
      // bizNtyMembrModel.setBizNtyRepD(0);
    //}
    
    else if(("I".equals(svsMembrType) && "D".equals(lvsMembrSts)))
    {
      bizNtyMembrModel.setBizNtyRepD(cvoCoreRequest.getIntVal("sviBizNtyRepD",0));

    }
    else
    {
      bizNtyMembrModel.setBizNtyRepD(0);

    }
    
    bizNtyMembrModel.setNmbrOfShares(cvoCoreRequest.getIntVal("sviNumbrOfShares",0));
    bizNtyMembrModel.setShareFaceVal(cvoCoreRequest.getIntVal("svnShareFaceVal",0));
    bizNtyMembrModel.setEfctvDateFrom(cvoCoreRequest.getDateVal("svdEfctvDateFrom"));
    bizNtyMembrModel.setEfctvDateTo(cvoCoreRequest.getDateVal("svdEfctvDateTo",App.DEFAULT_MAX_DATE));

    

     bizNtyMembrModel.save();
     
    
     if( cvoDbManager.canCommit()){ 
      cvoDbManager.commitTrans(); 
      cvoCoreResponse.addMessage(CoreMessage.RECORDS_SUBMITTED_SUCCESSFULLY); 
    } 
     getNdvdlDtls();
    } 
} 