package com.insilicoss.item;

import com.insilicoss.App;
import com.insilicoss.codegen.anotation.TrackChanges;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.OperationResponse;
import com.insilicoss.exception.PresentableException;
import com.insilicoss.validation.AlphaDigitSpacePunch;
import com.insilicoss.validation.AlphaSpace;
import com.insilicoss.validation.AppRule;
import com.insilicoss.validation.CoreValidator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@TrackChanges
public class ItemModel {
  private String cvsItemActv;
  private Logger cvoLogger = LogManager.getLogger(); 
  
  @AlphaSpace(len=20,name="Id")
  private String cvsItemIdy;
  
  @AlphaSpace(len=250,name="Item Description")
  private String cvsItemDesc;
  
  @AlphaDigitSpacePunch(len = 10, name = "Unit Of Measurement Id")
  private String cvsItemUomIdy;
  
  private LocalDate  cvdItemDateEfctvFrom;
  
  @AppRule(message="")
  public OperationResponse validateItemDateEfctvFrom(){
    if(cvdItemDateEfctvFrom == null)
    {
     return new OperationResponse(false,"Please enter from date"); 
    }
    return new OperationResponse(true,""); 
  }  
  
  
  private LocalDate  cvdItemDateEfctvTo = App.DEFAULT_MAX_DATE;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "")
  private String cvbIsSellItem;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "")
  private String cvbIsPchsItem;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "")
  private String cvbIsItcAplcbl;
  
  @AlphaDigitSpacePunch(len = 8 , name = "Item Hsn Sac")
  private String cvsItemHsnSac;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "")
  private String cvbIsAsetItem;
  
  @AlphaSpace(len=3,name="Asset Book Nature",allowEmpty = true)
  private String  cvsAsetBookNatur;
  
  private int cviAsetTaxCtgryD;
  
  private int cviAsetActCtgryD;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "Inventory Item")
   private String cvbIsInvItem;
  
  //@Constant(constantsClass = CoreValidator.ActiveContant.class, name = "Expenses Item")
  private String cvbIsXpnsItem;
  
  public static int cviItemD;
          
  private int cviItemF;
          
  private boolean cvbItemG;
  
  private DBManager cvoDBManager;
 
  @AppRule(message ="Duplicate Idy") 
  public OperationResponse validateIsBizNtyIdyUnique() throws SQLException{
    int count=0;
    cvoDBManager.addContextParam("rvsItemIdy",cvsItemIdy);
    cvoDBManager.addContextParam("rviItemD",cviItemD);
    ResultSet lvoBizNtyIdy = cvoDBManager.selectResultSet("Item\\sarCountItemIdy"); 
    lvoBizNtyIdy.next(); 
    count = lvoBizNtyIdy.getInt("svsCountItemIdy"); 
    if(count != 0){
      return new OperationResponse(false, "This Item Idy \"" + cvsItemIdy + "\" is already exist. or add new");
    }
    return new OperationResponse(true,"");
  } 
  
  private ItemModel(DBManager pvoDBManager){
    cvoDBManager = pvoDBManager;
  }
  
   
 ItemModel itemModel;
 
  /*public NtyMstrMpngModel getNtyMpngModel(int pviItemD,int pviBizNtyD){ 
    try { 
      return NtyMstrMpngModel.load(cviItemD, pviBizNtyD,cvoDBManager); 
    } 
    catch(SQLException se){ 
      throw new SystemException(se.getMessage(), se); 
    } 
    
  } */
  
  
  public static ItemModel load(String pvsItemIdy,DBManager pvoDBManager)throws SQLException{
   
    ItemModel itemModel = new ItemModel(pvoDBManager);
    itemModel.cviItemD = -1;
    itemModel.cviItemF = 1;
    
    if(pvsItemIdy != null && !pvsItemIdy.isBlank())
    {
      itemModel.cvoDBManager.addContextParam("rvsItemIdy",pvsItemIdy);
      ResultSet lvoItemRs = itemModel.cvoDBManager.selectResultSet("Item//sarLoadItem");
      
      if(lvoItemRs.first()){
        
        itemModel.cvsItemIdy           = lvoItemRs.getString("svsItemIdy");
        itemModel.cvsItemDesc          = lvoItemRs.getString("svsItemDesc");
        itemModel.cvsItemUomIdy        = lvoItemRs.getString("svsItemUomIdy");
        itemModel.cvdItemDateEfctvFrom = lvoItemRs.getDate("svdItemDateEfctvFrom").toLocalDate();
        itemModel.cvdItemDateEfctvTo   = lvoItemRs.getDate("svdItemDateEfctvTo").toLocalDate();
        itemModel.cvbIsSellItem        = lvoItemRs.getString("svbIsSellItem");
        itemModel.cvbIsPchsItem        = lvoItemRs.getString("svbIsPchsItem");
        itemModel.cvbIsItcAplcbl       = lvoItemRs.getString("svbIsItcAplcbl");
        itemModel.cvsItemHsnSac        = lvoItemRs.getString("svsItemHsnSac");
        itemModel.cvbIsAsetItem        = lvoItemRs.getString("svbIsAsetItem");
        itemModel.cvsAsetBookNatur     = lvoItemRs.getString("svsAsetBookNatur"); 
        itemModel.cviAsetTaxCtgryD     = lvoItemRs.getInt("sviAsetTaxCtgryD");
        itemModel.cviAsetActCtgryD     = lvoItemRs.getInt("sviAsetActCtgryD");
        itemModel.cvbIsInvItem         = lvoItemRs.getString("svbIsInvItem");
        itemModel.cvbIsXpnsItem        = lvoItemRs.getString("svbIsXpnsItem");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
        itemModel.cviItemD             = lvoItemRs.getInt("sviItemD");
        itemModel.cviItemF             = lvoItemRs.getInt("sviItemF");
      }
    }
    return itemModel;
  }
  
  public void update() throws SQLException{
    if(cviItemD>0)
    {
      if(!isChanged){
       return;
      }

      CoreValidator.validateAutoFlag(this);

      addReplacementVar();

      cvoDBManager.addContextParam("rviItemF",cviItemF+1);
      cvoDBManager.addContextParam("rviItemD",cviItemD);
      cvoDBManager.update("Item\\uarItemG");
      cvoDBManager.update("Item\\iarItem");
    }
    else{ 
      throw new PresentableException("Can't update as Item with Id '" + cvsItemIdy + "' not found."); 
    } 
  } 

  public void insert() throws SQLException{
    if(cviItemD<=0) 
    {
      if(!isChanged){
       return;
      }

      CoreValidator.validateAutoFlag(this);

      addReplacementVar();

      cviItemF = 1;
      cviItemD = cvoDBManager.getNextTxnD("ItemD"); 
      cvoDBManager.addContextParam("rviItemF",cviItemF);
      cvoDBManager.addContextParam("rviItemD",cviItemD);
      cvoDBManager.update("Item\\iarItem");    
    }
    else{ 
      throw new PresentableException("Can't insert as Item with Id '" + cvsItemIdy + "' already exist."); 
    } 
  }

  public void updateOrInsert() throws SQLException{
    if(!isChanged){
     return;
    }
    
    CoreValidator.validateAutoFlag(this);

    addReplacementVar();
    
    if(cviItemD>0)
    {
      cvoDBManager.addContextParam("rviItemF",cviItemF+1);
      cvoDBManager.addContextParam("rviItemD",cviItemD);
      cvoDBManager.update("Item\\uarItemG");
      cvoDBManager.update("Item\\iarItem");
    }
    else
    {
      cviItemF = 1;
      cviItemD = cvoDBManager.getNextTxnD("ItemD"); 
      cvoDBManager.addContextParam("rviItemF",cviItemF);
      cvoDBManager.addContextParam("rviItemD",cviItemD);
      cvoDBManager.update("Item\\iarItem");    
    }
  }

  private void addReplacementVar() {
    cvoDBManager.addContextParam("rvsItemIdy",cvsItemIdy);
    cvoDBManager.addContextParam("rvsItemDesc",cvsItemDesc);
    cvoDBManager.addContextParam("rvsItemUomIdy",cvsItemUomIdy);
    cvoDBManager.addContextParam("rvdItemDateEfctvFrom",cvdItemDateEfctvFrom);
    cvoDBManager.addContextParam("rvdItemDateEfctvTo",cvdItemDateEfctvTo);
    cvoDBManager.addContextParam("rvbIsSellItem",cvbIsSellItem);
    cvoDBManager.addContextParam("rvbIsPchsItem",cvbIsPchsItem);
    cvoDBManager.addContextParam("rvbIsItcAplcbl",cvbIsItcAplcbl);
    cvoDBManager.addContextParam("rvsItemHsnSac",cvsItemHsnSac);
    cvoDBManager.addContextParam("rvbIsAsetItem",cvbIsAsetItem);
    cvoDBManager.addContextParam("rvsAsetBookNatur",cvsAsetBookNatur);
    cvoDBManager.addContextParam("rviAsetTaxCtgryD",cviAsetTaxCtgryD);
    cvoDBManager.addContextParam("rviAsetActCtgryD",cviAsetActCtgryD);
    cvoDBManager.addContextParam("rvbIsInvItem",cvbIsInvItem);
    cvoDBManager.addContextParam("rvbIsXpnsItem",cvbIsXpnsItem);
  }
}
