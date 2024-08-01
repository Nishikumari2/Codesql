package com.insilicoss.bizNty;

import com.insilicoss.codegen.anotation.TrackChanges;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.OperationResponse;
import com.insilicoss.validation.AlphaDigitSpacePunch;
import com.insilicoss.validation.Constant;
import com.insilicoss.validation.CoreValidator;
import com.insilicoss.validation.AppRule;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.insilicoss.validation.Expression;
/**
 *
 * @author Alaknanda
 */
@TrackChanges
public class BizNtyLocModel {
  
  public String cvsBizNtyLocSts;
  private Logger cvoLogger = LogManager.getLogger(); 
  
  @AlphaDigitSpacePunch(len = 50, name = "Idy", order = 1)
  public String  cvsBizNtyLocIdy;
  
  @AlphaDigitSpacePunch(len = 50, name = "Name", order = 2)
  public String cvsBizNtyLocName;
  
  //public LocalDate cvdBizNtyLocFrom;
  
  //public LocalDate cvdBizNtyLocTo = App.DEFAULT_MAX_DATE; 
  
  /*@AppRule(message="",order=3)
  public OperationResponse validateFromToDate(){
    if (Util.isBefore(cvdBizNtyLocFrom, cvdBizNtyLocTo)) {
      return new OperationResponse(true,"");
    } 
    return new OperationResponse(false,"Deactive/to date should be after creation/from date."); 
}*/
  
  @AlphaDigitSpacePunch(len = 150, name = "Adrs Line 1", order = 4)
  public String cvsBizNtyLocAdrsLine1;
  
  @AlphaDigitSpacePunch(len = 150, name = "Adrs Line 2", allowEmpty = true,order = 5)
  public String cvsBizNtyLocAdrsLine2;
      
  //@AlphaDigitSpacePunch(len = 10, name = "Post Code", order = 6)
  public String cvsBizNtyLocPostCode;
      
 
  
  @AppRule(message = "Post Code Validation")
  public OperationResponse validatePostCode() throws SQLException {

    if (cvsBizNtyLocCntry.equals("IN")) {
        if (cvsBizNtyLocPostCode == null || cvsBizNtyLocPostCode.length() != 6) {
            return new OperationResponse(false, "Postcode must be exactly 6 digits for INDIA");
        } else {
            for (int i = 0; i < cvsBizNtyLocPostCode.length(); i++) {
                if (!Character.isDigit(cvsBizNtyLocPostCode.charAt(i))) {
                    return new OperationResponse(false, "Postcode must be digit");
                }
            }
            return new OperationResponse(true, "");
        }
    } else {
        if (cvsBizNtyLocPostCode == null || cvsBizNtyLocPostCode.length() != 10) {
            return new OperationResponse(false, "Postcode must be exactly 10 characters for NON-INDIA countries");
        } else {
            String pattern = "^[a-zA-Z0-9]{10}$";
            boolean isAlphaNumeric = cvsBizNtyLocPostCode.matches(pattern);
            if (!isAlphaNumeric) {
                return new OperationResponse(false, "Postcode must be alphanumeric for NON-INDIA countries");
            }
            return new OperationResponse(true, "");
        }
    }
}

  
  
  @AlphaDigitSpacePunch(len = 2, name = "State", order = 7,allowEmpty = true)
  public String cvsBizNtyLocState;
  
  @AlphaDigitSpacePunch(len = 2, name = "Country", order = 8)
  public String cvsBizNtyLocCntry;
  
  @AlphaDigitSpacePunch(len = 20, name = "Sales Tax Type",allowEmpty = true)
  public String cvsBizNtyLocSalesTaxType;
  
  @Expression(regExp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message ="" ,len = 20, name = "GST IN",allowEmpty = true,order = 9)
  public String cvsBizNtyLocSalesTaxIdy;
 
  @com.insilicoss.validation.Number(name="Credit Period(Days)", min = "0", max = "999")
  public int cviBizNtyLocCrdtPrid;
  
  @Constant(constantsClass = CoreValidator.ActiveContant.class, name="Registered Office")
  public String cvbBizNtyLocIsRegOff;
  
  @AlphaDigitSpacePunch(len = 15, name = "PT Authority", order = 10)
  public String cvbBizNtyLocPtAthry;
  
  @Constant(constantsClass = CoreValidator.ActiveContant.class, name="Metropolitan Region")
  public String cvbBizNtyLocIsMtrReg;
  
  @Constant(constantsClass = CoreValidator.ActiveContant.class, name="Location Sell")
  public String cvbBizNtySysHostLocCanSell;
  
  @Constant(constantsClass = CoreValidator.ActiveContant.class, name="Location Purchase")
  public String cvbBizNtySysHostLocCanPchs;
  

  public int cviBizNtyD, cviBizNtyLocD;
  
  public int cviBizNtyF;
  public int cviBizNtyLocF,cviBizNtyLocG;
  public int cviBizNtyHldyGrpD;
 
  private final DBManager cvoDBManager;
   
  private BizNtyLocModel(int pviBizNtyD, int pviBizNtyF, DBManager cvoDBManager){ 
     this.cviBizNtyD    = pviBizNtyD; 
     this.cviBizNtyF    = pviBizNtyF; 
     this.cviBizNtyLocD = -1; 
     this.cviBizNtyLocF = 1; 
     this.cviBizNtyLocG = 1; 
     this.cvoDBManager = cvoDBManager;
   }
           
   private BizNtyLocModel(String pvsBizNtyLocSts, int pviBizNtyD, int pviBizNtyF,String pvsBizNtyLocIdy,String pvsBizNtyLocName,
           String pvsBizNtyLocAdrsLine1,String pvsBizNtyLocAdrsLine2,
           String pvsBizNtyLocPostCode,String pvsBizNtyLocState,String pvsBizNtyLocCntry,String pvsBizNtyLocSalesTaxIdy,
           int pviBizNtyLocCrdtPrid,String pvbBizNtyLocIsRegOff,String pvsBizNtyLocPtAthry,String pvbBizNtyLocIsMtrReg,
           String pvsBizNtyLocCanSell, String pvsBizNtyLocCanPchs, int pviBizNtyHldyGrpD, String pvsBizNtyLocSalesTaxType,int pviBizNtyLocD, int pviBizNtyLocF,
           int pviBizNtyLocG, DBManager pvodBManager){
    this.cvsBizNtyLocSts           = pvsBizNtyLocSts;
    this.cviBizNtyD                = pviBizNtyD;         
    this.cviBizNtyF                = pviBizNtyF;                
    this.cvsBizNtyLocIdy           = pvsBizNtyLocIdy;           
    this.cvsBizNtyLocName          = pvsBizNtyLocName;         
    //this.cvdBizNtyLocFrom          = pvdBizNtyLocFrom;          
    //this.cvdBizNtyLocTo            = pvdBizNtyLocTo;            
    this.cvsBizNtyLocAdrsLine1     = pvsBizNtyLocAdrsLine1;     
    this.cvsBizNtyLocAdrsLine2     = pvsBizNtyLocAdrsLine2;     
    this.cvsBizNtyLocPostCode      = pvsBizNtyLocPostCode;      
    this.cvsBizNtyLocState         = pvsBizNtyLocState;         
    this.cvsBizNtyLocCntry         = pvsBizNtyLocCntry;        
    this.cvsBizNtyLocSalesTaxIdy   = pvsBizNtyLocSalesTaxIdy;           
    this.cviBizNtyLocCrdtPrid      = pviBizNtyLocCrdtPrid;      
    this.cvbBizNtyLocIsRegOff      = pvbBizNtyLocIsRegOff;      
    this.cvbBizNtyLocPtAthry       = pvsBizNtyLocPtAthry;      
    this.cvbBizNtyLocIsMtrReg      = pvbBizNtyLocIsMtrReg;      
    this.cvbBizNtySysHostLocCanSell = pvsBizNtyLocCanSell;
    this.cvbBizNtySysHostLocCanPchs= pvsBizNtyLocCanPchs;
    this.cviBizNtyHldyGrpD         = pviBizNtyHldyGrpD;
    this.cvsBizNtyLocSalesTaxType  = pvsBizNtyLocSalesTaxType;
    this.cviBizNtyLocD             = pviBizNtyLocD; 
    this.cviBizNtyLocF             = pviBizNtyLocF;             
    this.cviBizNtyLocG             = pviBizNtyLocG;            
    this.cvoDBManager              = pvodBManager; 
  } 
   
  @AppRule(message = "duplicate location Idy")
  public OperationResponse validateIsBizNtyLocIdyUnique()throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
    cvoDBManager.addContextParam("rvsBizNtyLocIdy",cvsBizNtyLocIdy);
    cvoDBManager.addContextParam("rviBizNtyLocD",cviBizNtyLocD);
    ResultSet lvoBizNtyLocIdy =  cvoDBManager.selectResultSet("BizNtty/sarCountBizNtyLocIdy");
    lvoBizNtyLocIdy.next();
    count = lvoBizNtyLocIdy.getInt("svsBizNtyLocIdy");
    if(count!=0){
     return new OperationResponse(false, "This business Idy \"" + cvsBizNtyLocIdy + "\" is already exist. or add new");
    }
      return new OperationResponse(true,"");
  } 

@AppRule(message = "DUPLICATE GST")
  public OperationResponse validateIsBizNtyLocSalesTaxIdy()throws SQLException{
    if((cvsBizNtyLocSalesTaxType.equals("Composite") || cvsBizNtyLocSalesTaxType.equals("Regular")|| cvsBizNtyLocSalesTaxType.equals("SEZ")) && cvsBizNtyLocCntry.equals("IN")){
    String statecode;
    cvoDBManager.addContextParam("rvsBizNtyLocState",cvsBizNtyLocState);
    ResultSet lvoBizNtyLocStCode = cvoDBManager.selectResultSet("BizNtty\\sarBizNtyLocStateCode");
    lvoBizNtyLocStCode.next();
    statecode = lvoBizNtyLocStCode.getString("svsStateCode");
    String statecodemodel = String.valueOf(cvsBizNtyLocSalesTaxIdy.charAt(0)) + cvsBizNtyLocSalesTaxIdy.charAt(1);

    if(statecode.equals(statecodemodel))
    {
     cvoDBManager.addContextParam("rviBizNtyD",cviBizNtyD);
     cvoDBManager.addContextParam("rviBizNtyF",cviBizNtyF);
     ResultSet lvoBizNtyNcomTaxIdy = cvoDBManager.selectResultSet("BizNtty\\sarPanNo");
     lvoBizNtyNcomTaxIdy.next();
     String pan = lvoBizNtyNcomTaxIdy.getString("svsBizNtyNComTaxIdy");
     if(pan == null)
     {
        return new OperationResponse(false,"Please enter pan no first then enter GST No");
     }
     String pannomodel = cvsBizNtyLocSalesTaxIdy.substring(2,12);
      if(pan.equals(pannomodel))
      {
        if(((cvsBizNtyLocSalesTaxIdy.charAt(12) >= '1' && cvsBizNtyLocSalesTaxIdy.charAt(12) <= '9')
                || (cvsBizNtyLocSalesTaxIdy.charAt(12) >= 'A' && cvsBizNtyLocSalesTaxIdy.charAt(12) <= 'Z') 
                || (cvsBizNtyLocSalesTaxIdy.charAt(12) >= 'a' && cvsBizNtyLocSalesTaxIdy.charAt(12) <= 'z')) 
                && (cvsBizNtyLocSalesTaxIdy.charAt(13) == 'Z' || cvsBizNtyLocSalesTaxIdy.charAt(13) == 'z')
                && ((cvsBizNtyLocSalesTaxIdy.charAt(14) >= '1' && cvsBizNtyLocSalesTaxIdy.charAt(14) <= '9')
                || (cvsBizNtyLocSalesTaxIdy.charAt(14) >= 'A' && cvsBizNtyLocSalesTaxIdy.charAt(14) <= 'Z') 
                || (cvsBizNtyLocSalesTaxIdy.charAt(14) >= 'a' && cvsBizNtyLocSalesTaxIdy.charAt(14) <= 'z')))
         {
            return new OperationResponse(true,"");
         }
        else
        {
            return new OperationResponse(false,"Error can be at any position from 13 to 15.At position 13 no special character is allowed and at position 14 only z character is allowed and at position 15 no special character is allowed");
        }
     }
     else
     {
       return new OperationResponse(false,"Error can be at any position from 3 to 12.It should be same as pan number");
     }
    }
    else
    {
         return new OperationResponse(false,"Error at position either at 1 or 2.It is not matching with the state code. At position 1, number should be from 1 to 3 and at position 2 number should be from 1 to 9.");
    }
   }
   return new OperationResponse(true,"");
 }
  
  public static BizNtyLocModel createNew(int pviBizNtyD, int pviBizNtyF, DBManager cvoDBManager){ 
    BizNtyLocModel bizNtyModel = new BizNtyLocModel(pviBizNtyD, pviBizNtyF, cvoDBManager);
    return bizNtyModel; 
  } 

  public static BizNtyLocModel load(String pvsBizNtyLocSts, int pviBizNtyD, int pviBizNtyF,String pvsBizNtyLocIdy,String pvsBizNtyLocName,
           String pvsBizNtyLocAdrsLine1,String pvsBizNtyLocAdrsLine2,
           String pvsBizNtyLocPostCode,String pvsBizNtyLocState,String pvsBizNtyLocCntry,String pvsBizNtyLocSalesTaxIdy,
           int pviBizNtyLocCrdtPrid,String pvbBizNtyLocIsRegOff,String pvsBizNtyLocPtAthry,String pvbBizNtyLocIsMtrReg,
           String pvsBizNtyLocCanSell,String pvsBizNtyLocCanPchs, int pviBizNtyHldyGrpD, String pvsBizNtyLocSalesTaxType,int pviBizNtyLocD,int pviBizNtyLocF,
           int pviBizNtyLocG,DBManager dBManager)throws SQLException{ 
    
    BizNtyLocModel bizNtyLocModel = new BizNtyLocModel(pvsBizNtyLocSts,pviBizNtyD, pviBizNtyF, pvsBizNtyLocIdy, pvsBizNtyLocName,  
             pvsBizNtyLocAdrsLine1, pvsBizNtyLocAdrsLine2,pvsBizNtyLocPostCode,pvsBizNtyLocState, 
            pvsBizNtyLocCntry,pvsBizNtyLocSalesTaxIdy,pviBizNtyLocCrdtPrid,pvbBizNtyLocIsRegOff,
            pvsBizNtyLocPtAthry, pvbBizNtyLocIsMtrReg, pvsBizNtyLocCanSell, pvsBizNtyLocCanPchs, pviBizNtyHldyGrpD, 
            pvsBizNtyLocSalesTaxType,pviBizNtyLocD,pviBizNtyLocF,pviBizNtyLocG,dBManager); 
    return bizNtyLocModel; 
  }
  
  public void save()throws SQLException{
    
    if(!isChanged){
     return ;
   }
    
    CoreValidator.validateAutoFlag(this); 
    cvoDBManager.addContextParam("rvsBizNtyLocIdy",cvsBizNtyLocIdy);
    cvoDBManager.addContextParam("rviBizNtyD", cviBizNtyD);
    cvoDBManager.addContextParam("rviBizNtyF", cviBizNtyF);
    cvoDBManager.addContextParam("rvsBizNtyLocName",cvsBizNtyLocName);
    cvoDBManager.addContextParam("rvsBizNtyLocLocAdrsLine1",cvsBizNtyLocAdrsLine1);
    cvoDBManager.addContextParam("rvsBizNtyLocLocAdrsLine2",cvsBizNtyLocAdrsLine2);
    cvoDBManager.addContextParam("rvsBizNtyLocPostCode",cvsBizNtyLocPostCode);
    cvoDBManager.addContextParam("rvsBizNtyLocState",cvsBizNtyLocState);
    cvoDBManager.addContextParam("rvsBizNtyLocCntry",cvsBizNtyLocCntry);
    cvoDBManager.addContextParam("rvsBizNtyLocSalesTaxIdy",cvsBizNtyLocSalesTaxIdy.toUpperCase());
    cvoDBManager.addContextParam("rviBizNtyLocCrdtPrid",cviBizNtyLocCrdtPrid);
    cvoDBManager.addContextParam("rvsBizNtyLocIsRegOff",cvbBizNtyLocIsRegOff);
    cvoDBManager.addContextParam("rvsBizNtyLocPtAthry",cvbBizNtyLocPtAthry );
    cvoDBManager.addContextParam("rvsBizNtyLocIsMtrReg",cvbBizNtyLocIsMtrReg);
    cvoDBManager.addContextParam("rvsBizNtySysHostLocCanSell",cvbBizNtySysHostLocCanSell);
    cvoDBManager.addContextParam("rvsBizNtySysHostLocCanPchs",cvbBizNtySysHostLocCanPchs);
    cvoDBManager.addContextParam("rviBizNtyHldyGrpD",cviBizNtyHldyGrpD);
    cvoDBManager.addContextParam("rvsBizNtyLocSalesTaxType",cvsBizNtyLocSalesTaxType);
    if(cviBizNtyLocD > 0){ 
      cvoDBManager.addContextParam("rviBizNtyLocD",cviBizNtyLocD);
      cviBizNtyLocF++; 
      cvoDBManager.addContextParam("rviBizNtyLocF", cviBizNtyLocF); 
      cvoDBManager.update("BizNtty\\uarBizNtyLocG"); 
      cvoDBManager.update("BizNtty\\iarBizNtyLoc"); 
    } 
    else{ 
      cviBizNtyLocD = cvoDBManager.getNextTxnD("BizNtyLocD"); 
      cvoDBManager.addContextParam("rviBizNtyLocD", cviBizNtyLocD); 
      cvoDBManager.addContextParam("rviBizNtyLocF", cviBizNtyLocF); 
      cvoDBManager.update("BizNtty\\iarBizNtyLoc"); 
    } 
  } 
} 