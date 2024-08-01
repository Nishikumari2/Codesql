package com.insilicoss.item;

import com.insilicoss.App;
import com.insilicoss.codegen.anotation.TrackChanges;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.OperationResponse;
import com.insilicoss.exception.SystemException;
import com.insilicoss.validation.AppRule;
import com.insilicoss.validation.CoreValidator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
@TrackChanges
public class NtyMstrMpngModel {
   private String cvbNtyMstrActv;
   
   private int cviNtyD;
   
   private String cvMstrOnr = "Item";
   
   private int cviMstrOnrD;
   
   private LocalDate  cvdNtyMstrFrom = App.DEFAULT_MIN_DATE;
  
   private LocalDate  cvdNtyMstrTo  = App.DEFAULT_MAX_DATE; 
   
   private DBManager cvoDBManager;
   
   @AppRule(message ="Duplicate Item Maping") 
  public OperationResponse validateIsBizNtyIdyUnique() throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rviNtyD",cviNtyD);
    cvoDBManager.addContextParam("rviMstrOnrD",cviMstrOnrD);
    ResultSet lvoBizNtyIdy = cvoDBManager.selectResultSet("Item\\sarCountFullMstrMpngDtls"); 
    lvoBizNtyIdy.next(); 
    count = lvoBizNtyIdy.getInt("sviCountFullMstrMpngDtls"); 
    if(count != 0){
      return new OperationResponse(false, "This Item is already Mapped with this company");
    }
    return new OperationResponse(true,"");
  } 
   
   private NtyMstrMpngModel(DBManager pvoDBManager){
     cvoDBManager = pvoDBManager;
   }
   NtyMstrMpngModel ntyMstrMpngModel;
   
   static int count;
    public static NtyMstrMpngModel load(int pviMstrOnrD,int pviBizNtyD,DBManager pvoDBManager) throws SQLException{
     if(pviMstrOnrD == -1)
     {
       throw new SystemException("ItemD can't be -1");
     }
     
     NtyMstrMpngModel ntyMstrMpngModel = new NtyMstrMpngModel(pvoDBManager);
     ntyMstrMpngModel.cviMstrOnrD = pviMstrOnrD;
     
     if(pviMstrOnrD != -1 && pviBizNtyD != 1) {
      
      ntyMstrMpngModel.cvoDBManager.addContextParam("rviMstrOnrD",pviMstrOnrD); 
      ntyMstrMpngModel.cvoDBManager.addContextParam("rviBizNtyD",pviBizNtyD);
      ResultSet lvoNtyMstrMpngCountRs = ntyMstrMpngModel.cvoDBManager.selectResultSet("Item//sarCountFullNtyItemMpng");
      lvoNtyMstrMpngCountRs.next();
      count = lvoNtyMstrMpngCountRs.getInt("svsCountNtyMstrMpng"); 
      if(count != 0){
        ntyMstrMpngModel.cvoDBManager.addContextParam("rviMstrOnrD",pviMstrOnrD); 
        ntyMstrMpngModel.cvoDBManager.addContextParam("rviBizNtyD",pviBizNtyD);
        ResultSet lvoNtyMstrMpngRs = ntyMstrMpngModel.cvoDBManager.selectResultSet("Item//sarFullNtyItemMpng");
        
        if(lvoNtyMstrMpngRs.first()){
        ntyMstrMpngModel.cviNtyD                  =  lvoNtyMstrMpngRs.getInt("sviNtyD");
        ntyMstrMpngModel.cvbNtyMstrActv           =  lvoNtyMstrMpngRs.getString("svbNtyMstrActv");
        ntyMstrMpngModel.cvMstrOnr                =  lvoNtyMstrMpngRs.getString("svsMstrOnr");
        ntyMstrMpngModel.cviMstrOnrD              =  lvoNtyMstrMpngRs.getInt("sviMstrOnrD");
        ntyMstrMpngModel.cvdNtyMstrFrom           =  lvoNtyMstrMpngRs.getDate("svdNtyMstrFrom").toLocalDate();
        ntyMstrMpngModel.cvdNtyMstrTo             =  lvoNtyMstrMpngRs.getDate("svdNtyMstrTo").toLocalDate();
        }
        }
        return ntyMstrMpngModel;
       }
       return ntyMstrMpngModel;
    }
   
    public void save() throws SQLException{
    
    if(!isChanged)
    {
      return;
    }
    
    CoreValidator.validateAutoFlag(this);
    cvoDBManager.addContextParam("rvsMstrOnr",cvMstrOnr);
    cvoDBManager.addContextParam("rvbNtyMstrActv",cvbNtyMstrActv);
    cvoDBManager.addContextParam("rvdNtyMstrFrom",cvdNtyMstrFrom);
    cvoDBManager.addContextParam("rvdNtyMstrTo",cvdNtyMstrTo);

    
    if(count != 0)
    {
      cvoDBManager.addContextParam("rviMstrOnrD",cviMstrOnrD); 
      cvoDBManager.addContextParam("rviBizNtyD",cviNtyD);
      cvoDBManager.update("Item\\uarMstrOnr");
    }
    else
    {
      cvoDBManager.addContextParam("rviMstrOnrD",cviMstrOnrD);
      cvoDBManager.addContextParam("rviNtyD",cviNtyD);
      cvoDBManager.update("Item\\iarMstrOnr");
    }
  }
   
}
