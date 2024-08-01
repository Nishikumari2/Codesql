/*
 * InSilico Solutions And Services 
 * www.insilicoss.com
 */
package com.insilicoss.bizNty;

import com.insilicoss.App;
import com.insilicoss.codegen.anotation.TrackChanges;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.OperationResponse;
import com.insilicoss.exception.PresentableException;
import com.insilicoss.util.Util;
import com.insilicoss.validation.AlphaDigitSpacePunch;
import com.insilicoss.validation.AppRule;
import com.insilicoss.validation.Constant;
import com.insilicoss.validation.CoreValidator;
import com.insilicoss.validation.Expression;
import org.apache.logging.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
/**
 *
 * @author Alaknanda
 */
@TrackChanges 
public class BizNtyModel {

  private String cvsBizNtySts;
  
  @AlphaDigitSpacePunch(len = 50, name = "Idy", order = 1)
  private String cvsBizNtyIdy;
  
  //private LocalDate cvdBizNtyFrom;

  //private LocalDate cvdBizNtyTo = App.DEFAULT_MAX_DATE; 
  
  @AlphaDigitSpacePunch(len = 150, name = "Name", order = 2)
  private String cvsBizNtyName;

  //@AlphaDigitSpacePunch(len = 20, name = "PAN", order = 3)
  @Expression(regExp = "[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}", message ="INVALID PAN", len = 10, allowEmpty = true) 
  private String cvsBizNtyNcomTaxIdy;

  @AlphaDigitSpacePunch(len = 20, name = "TAN",allowEmpty = true)
  private String cvsBizNtyNcomTaxWthdgIdy;

  @AlphaDigitSpacePunch(len = 20, name = "MSME",allowEmpty = true, group = "step-2")
  private String cvsBizNtyMSMEIdy;

 @Expression(regExp = "^([LUu]{1})([0-9]{5})([A-Za-z]{2})([0-9]{4})([A-Za-z]{3})([0-9]{6})$", message ="INVALID CIN NUMBER", len = 21, allowEmpty = true) 
 private String cvsBizNtyLegalIdy;

  @Constant(constantsClass = CoreValidator.ActiveContant.class, name = "Business Owner?")
  private String cvbBizNtyIsBizOnr;

  @Constant(constantsClass = CoreValidator.ActiveContant.class, name = "Customer?")
  private String  cvbBizNtyIsCstmr;
  
  @Constant(constantsClass = CoreValidator.ActiveContant.class, name = "Vendor?")
  private String  cvbBizNtyIsVndr;

  private int cviBizNtyD;
  private int cviBizNtyF;
  //-------------------------------------------------------------------------------------------------------------------------------
  public List<BizNtyLocModel> cvoBizNtyLocModelList;
  //-------------------------------------------------------------------------------------------------------------------------------
 
  private DBManager cvoDBManager;
  private final Logger cvoLogger = LogManager.getLogger();
  
   private BizNtyModel(DBManager pvoDBManager){
    cvoDBManager = pvoDBManager;
    cvoBizNtyLocModelList = new ArrayList<>();
  }
  
  @AppRule(message = "",order=1)
  public boolean validateOnr(){
    if(cvbBizNtyIsBizOnr.equals("1") && (cvbBizNtyIsCstmr.equals("1")  || cvbBizNtyIsVndr.equals("1"))){
       cvbBizNtyIsBizOnr = "0";
      throw new PresentableException("Business Entity and Owner can't be at a same time");
    }
    return true;
  }
  
  @AppRule(message = "",order=2)
  public boolean validateCstmr(){
    if(cvbBizNtyIsBizOnr.equals("1") && cvbBizNtyIsCstmr.equals("1")){
      cvbBizNtyIsCstmr = "0";
      throw new PresentableException("Business Entity and Owner can't be at a same time");
      
  }
   return true; 
  }
 
  
  @AppRule(message = "",order=3)
  public boolean validateVndr(){
    if(cvbBizNtyIsBizOnr.equals("1") && cvbBizNtyIsVndr.equals("1")){
      cvbBizNtyIsVndr = "0";
      throw new PresentableException("You can't be both i.e. onwer and customer at same time");
  }
   return true;
  }
   
  
  /*@AppRule(message="")
  public OperationResponse validateFromToDate(){
    if (Util.isBefore(cvdBizNtyFrom, cvdBizNtyTo)) {
      return new OperationResponse(true,"");
    } 
    return new OperationResponse(false,"Deactive/to date should be after creation/from date."); 
} */

  @AppRule(message ="Duplicate Idy") 
  public OperationResponse validateIsBizNtyIdyUnique() throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rvsBizNtyIdy", cvsBizNtyIdy);
    cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
    ResultSet lvoBizNtyIdy = cvoDBManager.selectResultSet("BizNtty\\sarCountBizNty"); 
    lvoBizNtyIdy.next(); 
    count = lvoBizNtyIdy.getInt("svsCountBizNty"); 
    if(count != 0){
      return new OperationResponse(false, "This business Idy \"" + cvsBizNtyIdy + "\" is already exist. or add new");
    }
    return new OperationResponse(true,"");
  } 

  @AppRule(message ="Duplicate CIN") 
  public OperationResponse validateIsBizNtyCin() throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rvsBizNtyLegalIdy",cvsBizNtyLegalIdy);
    cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
    ResultSet lvoBizNtyLegalIdy = cvoDBManager.selectResultSet("BizNtty\\sarCountBizNtyLegalIdy");
    lvoBizNtyLegalIdy.next(); 
    count = lvoBizNtyLegalIdy.getInt("svsCountLegalIdy"); 
    if(count != 0){
      return new OperationResponse(false, "This CIN  \"" + cvsBizNtyLegalIdy + "\" is already exist. or add new");
    }
    return new OperationResponse(true,"");
  } 
  
  @AppRule(message = "DUPLICATE PAN")
  public OperationResponse validateIsBizNtyPan() throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rvsBizNtyNcomTaxIdy",cvsBizNtyNcomTaxIdy);
    cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
    ResultSet lvoBizNtyNcomTaxIdy = cvoDBManager.selectResultSet("BizNtty\\sarCountBizNtyNcomTaxIdy");
    lvoBizNtyNcomTaxIdy.next();
    count = lvoBizNtyNcomTaxIdy.getInt("svsCountNcomIdy");
    if(count!=0){
      return new OperationResponse(false,"This PAN \"" + cvsBizNtyNcomTaxIdy + "\" is already exist");
    }
    return new OperationResponse(true,"");
  }
  
  @AppRule(message ="") 
  public OperationResponse hasRgrdOfc() { 
    int regOfficeCount = 0; 
    for (BizNtyLocModel bizNtyLocModel : cvoBizNtyLocModelList) {
      if("1".equalsIgnoreCase(bizNtyLocModel.cvbBizNtyLocIsRegOff)){ 
        regOfficeCount++; 
      } 
    } 
    if(regOfficeCount > 1){ 
      return new OperationResponse(false, "Already existed Registered Office"); 
    }
    if(regOfficeCount == 0){ 
      return new OperationResponse(true, "Successfully Inserted"); 
    }
    return OperationResponse.OK; 
  } 

  public static BizNtyModel load(int pviBizNtyD, DBManager pvoDBManager) throws SQLException { 
    BizNtyModel lvoBizNtyModel = new BizNtyModel(pvoDBManager); 

    if(pviBizNtyD!=-1){ 
      lvoBizNtyModel.cvoDBManager.addContextParam("rviBizNtyD", pviBizNtyD); 
      ResultSet lvoBizNtyRs = lvoBizNtyModel.cvoDBManager.selectResultSet("BizNty/sarLoadBizNty"); 
      ResultSet lvoBizNtyLocRs; 
      BizNtyLocModel lvoBizNtyLocModel; 

      if(lvoBizNtyRs.first()){
        lvoBizNtyModel.cvsBizNtyIdy             = lvoBizNtyRs.getString("svsBizNtyIdy");
        //lvoBizNtyModel.cvdBizNtyFrom            = lvoBizNtyRs.getDate("svdBizNtyFrom").toLocalDate();
        //lvoBizNtyModel.cvdBizNtyTo              = lvoBizNtyRs.getDate("svdBizNtyTo").toLocalDate();
        lvoBizNtyModel.cvsBizNtyName            = lvoBizNtyRs.getString("svsBizNtyName");
        lvoBizNtyModel.cvsBizNtyNcomTaxIdy      = lvoBizNtyRs.getString("svsNcomTaxIdy");
        lvoBizNtyModel.cvsBizNtyNcomTaxWthdgIdy = lvoBizNtyRs.getString("svsBizNtyNcomTaxWthdgIdy");
        lvoBizNtyModel.cvsBizNtyMSMEIdy         = lvoBizNtyRs.getString("svsBizNtyMSMEIdy");
        lvoBizNtyModel.cvsBizNtyLegalIdy        = lvoBizNtyRs.getString("svsLegalIdy");
        lvoBizNtyModel.cvbBizNtyIsBizOnr        = lvoBizNtyRs.getString("svsIsBizOnr");
        lvoBizNtyModel.cvbBizNtyIsCstmr         = lvoBizNtyRs.getString("svsIsCstmr");
        lvoBizNtyModel.cvbBizNtyIsVndr          = lvoBizNtyRs.getString("svsIsVndr");
        lvoBizNtyModel.cviBizNtyD               = lvoBizNtyRs.getInt("sviBizNtyD");
        lvoBizNtyModel.cviBizNtyF               = lvoBizNtyRs.getInt("sviBizNtyF");

        lvoBizNtyModel.cvoDBManager.addContextParam("rviBizNtyD", pviBizNtyD); 
        lvoBizNtyModel.cvoDBManager.addContextParam("rviBizNtyF", lvoBizNtyModel.cviBizNtyF); 
        lvoBizNtyLocRs = lvoBizNtyModel.cvoDBManager.selectResultSet("BizNty/sarLoadAllBizNtyLoc"); 
        while(lvoBizNtyLocRs.next()){ 
          //create bizntyLoc obj
          String lvsBizNtyLocSts                 = lvoBizNtyLocRs.getString("svsBizNtyLocSts");
          int    lviBizNtyD                      = lvoBizNtyLocRs.getInt("sviBizNtyD");
          int    lviBizNtyF                      = lvoBizNtyLocRs.getInt("sviBizNtyF");
          String lvsBizNtyLocIdy                 = lvoBizNtyLocRs.getString("svsBizNtyLocIdy");   
          String lvsBizNtyLocName                = lvoBizNtyLocRs.getString("svsBizNtyLocName");
          //LocalDate lvdBizNtyLocFrom             = lvoBizNtyLocRs.getDate("svdBizNtyLocFrom").toLocalDate();
          //LocalDate lvsBizNtyLocTo               = lvoBizNtyLocRs.getDate("svdBizNtyLocTo").toLocalDate();
          String lvsBizNtyLocAdrsLine1           = lvoBizNtyLocRs.getString("svsBizNtyLocAdrsLine1");
          String lvsBizNtyLocAdrsLine2           = lvoBizNtyLocRs.getString("svsBizNtyLocAdrsLine2");
          String lvsBizNtyLocPostCode            = lvoBizNtyLocRs.getString("svsBizNtyLocPostCode");
          String lvsBizNtyLocState               = lvoBizNtyLocRs.getString("svsLocState");
          String lvsBizNtyLocCntry               = lvoBizNtyLocRs.getString("svsLocCntry");
          String lvsBizNtyLocSalesTaxIdy         = lvoBizNtyLocRs.getString("svsLocSalesTaxIdy");
          int lviBizNtyLocCrdtPrid               = lvoBizNtyLocRs.getInt("sviBizNtyLocCrdtPrid"); 
          String lvsBizNtyLocIsRegOff            = lvoBizNtyLocRs.getString("svsLocIsRegOff");
          String lvsBizNtyLocPtAthry             = lvoBizNtyLocRs.getString("svsBizNtyLocPtAthry");
          String lvsBizNtyLocIsMtrReg            = lvoBizNtyLocRs.getString("svsBizNtyLocIsMtrReg");
          String lvsBizNtyLocCanSell             = lvoBizNtyLocRs.getString("svsBizNtySysHostLocCanSell");
          String lvsBizNtyLocCanPchs             = lvoBizNtyLocRs.getString("svsBizNtySysHostLocCanPchs");
          String lvsBizNtyLocSalesTaxType        = lvoBizNtyLocRs.getString("svsBizNtyLocSalesTaxType");
          int lviBizNtyHldyGrpD                  = lvoBizNtyLocRs.getInt("sviBizNtyHldyGrpD");
          int lviBizNtyLocD                      = lvoBizNtyLocRs.getInt("sviBizNtyLocD");
          int lviBizNtyLocF                      = lvoBizNtyLocRs.getInt("sviBizNtyLocF");
          int lviBizNtyLocG                      = lvoBizNtyLocRs.getInt("sviBizNtyLocG"); 
          lvoBizNtyLocModel = BizNtyLocModel.load(lvsBizNtyLocSts,lviBizNtyD,lviBizNtyF,lvsBizNtyLocIdy,lvsBizNtyLocName
                  ,lvsBizNtyLocAdrsLine1,lvsBizNtyLocAdrsLine2,lvsBizNtyLocPostCode,
                  lvsBizNtyLocState,lvsBizNtyLocCntry,lvsBizNtyLocSalesTaxIdy,lviBizNtyLocCrdtPrid,
                  lvsBizNtyLocIsRegOff, lvsBizNtyLocPtAthry, lvsBizNtyLocIsMtrReg, lvsBizNtyLocCanSell, lvsBizNtyLocCanPchs, lviBizNtyHldyGrpD, 
                  lvsBizNtyLocSalesTaxType,lviBizNtyLocD,lviBizNtyLocF,lviBizNtyLocG,lvoBizNtyModel.cvoDBManager);
          // add in to list
          lvoBizNtyModel.cvoBizNtyLocModelList.add(lvoBizNtyLocModel);
        }
      } 
    } else {
//        lvoBizNtyModel.cvdBizNtyFrom            = App.DEFAULT_MIN_DATE; 
//        lvoBizNtyModel.cvdBizNtyTo              = App.DEFAULT_MAX_DATE; 
        lvoBizNtyModel.cviBizNtyD = -1; 
        lvoBizNtyModel.cviBizNtyF =  1; 
      } 
    return  lvoBizNtyModel; 
  } 

  public BizNtyLocModel getBizNtyLocModel(int pviBizNtyLocD){ 
    for (BizNtyLocModel bizNtyLocModel : cvoBizNtyLocModelList) { 
      if(bizNtyLocModel.cviBizNtyLocD == pviBizNtyLocD){ 
        return bizNtyLocModel; 
      } 
    } 
    return null; 
  } 

  public BizNtyLocModel createBizNtyLocModel(){ 
    BizNtyLocModel lvoBizNtyLocModel = BizNtyLocModel.createNew(cviBizNtyD, cviBizNtyF, cvoDBManager); 
    cvoBizNtyLocModelList.add(lvoBizNtyLocModel); 
    return lvoBizNtyLocModel; 
  } 

  public void save() throws SQLException { 
    CoreValidator.validateAutoFlag(this); 
    if(isChanged){ 
      cvoDBManager.addContextParam("rvsBizNtyIdy",cvsBizNtyIdy);
      //cvoDBManager.addContextParam("rvdBizNtyFrom",cvdBizNtyFrom);
      //cvoDBManager.addContextParam("rvdBizNtyTo",cvdBizNtyTo);
      cvoDBManager.addContextParam("rvsBizNtyName",cvsBizNtyName);
      cvoDBManager.addContextParam("rvsBizNtyNcomTaxIdy", cvsBizNtyNcomTaxIdy.toUpperCase());
      cvoDBManager.addContextParam("rvsBizNtyNcomTaxWthdgIdy",cvsBizNtyNcomTaxWthdgIdy);
      cvoDBManager.addContextParam("rvsBizNtyMSMEIdy",cvsBizNtyMSMEIdy);
      cvoDBManager.addContextParam("rvsBizNtyLegalIdy",cvsBizNtyLegalIdy);
      cvoDBManager.addContextParam("rvsBizNtyIsBizOnr",cvbBizNtyIsBizOnr);
      cvoDBManager.addContextParam("rvsIsCstmr",cvbBizNtyIsCstmr);
      cvoDBManager.addContextParam("rvsIsVndr",cvbBizNtyIsVndr);
      cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);

      if(cviBizNtyD > 0){ 
        cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
//        ResultSet lvoRs = cvoDBManager.selectResultSet("BizNtty/sarLatestBizNtyF");
//        while(lvoRs.next())
//        {
//          int cviBizNtyF = lvoRs.getInt("sviBizNtyF");
//        }
        cviBizNtyF++; 
        cvoDBManager.addContextParam("rviBizNtyF", cviBizNtyF);
        cvoDBManager.update("BizNtty\\uarBizNtyG");
        cvoDBManager.update("BizNtty\\iarBizNty"); 
      }  
      else { 
        cviBizNtyD = cvoDBManager.getNextTxnD("BizNtyD"); 
        cvoDBManager.addContextParam("rviBizNtyD", cviBizNtyD); 
        cvoDBManager.addContextParam("rviBizNtyF", cviBizNtyF); 
        cvoDBManager.update("BizNtty/iarBizNty");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
      } 
    } 

    for (BizNtyLocModel bizNtyLocModel : cvoBizNtyLocModelList) { 
      bizNtyLocModel.setBizNtyD(cviBizNtyD); // Update D for Add New Case; 
      bizNtyLocModel.setBizNtyF(cviBizNtyF); // Update F for Update New Case; 
      bizNtyLocModel.save(); 
    } 
  } 
}
