package com.insilicoss.item;

import com.insilicoss.App;
import com.insilicoss.database.DBManager;
import com.insilicoss.eventManager.CoreRequest;
import com.insilicoss.eventManager.CoreResponse;
import com.insilicoss.eventManager.Row;
import com.insilicoss.eventManager.RowSet;
import com.insilicoss.membr.NdvdlModel;
import com.insilicoss.messaging.CoreMessage;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemWebControler {
  private CoreRequest cvoCoreRequest;
  private CoreResponse cvoCoreResponse;
  private  DBManager cvoDbManager;
  private Logger cvoLogger; 
  
  public ItemWebControler(CoreRequest pvoCoreRequest, CoreResponse pvoCoreResponse) {
    cvoCoreRequest = pvoCoreRequest;
    cvoCoreResponse = pvoCoreResponse;
    cvoDbManager = cvoCoreRequest.getDBManager();
    cvoLogger = LogManager.getLogger(); 
  }
  
  public void loadItemDtls() {
    ResultSet lvoItem = cvoDbManager.selectResultSet("Item/sarFullItemDtls");
    cvoCoreResponse.setVal("svoItem", lvoItem);
  } 
  
  public void getBizNtyNameForItem(){
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    int lviItemD = cvoCoreRequest.getIntVal("sviItemD",-1);
    lvoDbManager.addContextParam("rviItemD",lviItemD);
    ResultSet lvoBizNtyName = cvoDbManager.selectResultSet("Item/sarFullBizNtyName");
    cvoCoreResponse.setVal("svoNtyMstrMpng",lvoBizNtyName);
  }
  
  public void getItemDtls() throws SQLException{
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    String lvsItemIdy = cvoCoreRequest.getVal("svsItemIdy","");
    
    lvoDbManager.addContextParam("rvsItemIdy",lvsItemIdy);
    ResultSet lvoItemRw = lvoDbManager.selectResultSet("Item\\sarLoadGetItemDtls");
    if(lvoItemRw.next()){
      int lviAsetTaxCtgryD   = lvoItemRw.getInt("sviAsetTaxCtgryD");
      cvoCoreResponse.setVal("sviAsetTaxCtgryD",lviAsetTaxCtgryD);
      int lviAsetActCtgryD  = lvoItemRw.getInt("sviAsetActCtgryD");
      cvoCoreResponse.setVal("sviAsetActCtgryD",lviAsetActCtgryD); 
      String lvsAsetTaxCtgryId  = lvoItemRw.getString("svsAsetTaxCtgryId");
      cvoCoreResponse.setVal("svsAsetTaxCtgryId",lvsAsetTaxCtgryId);
      String lvsAsetActCtgryId  = lvoItemRw.getString("svsAsetActCtgryId");
      cvoCoreResponse.setVal("svsAsetActCtgryId",lvsAsetActCtgryId);
      String lvsItemUomId   = lvoItemRw.getString("svsItemUomId");
      cvoCoreResponse.setVal("svsItemUomId",lvsItemUomId);
      String lvsItemUomDesc  = lvoItemRw.getString("svsItemUomDesc");
      cvoCoreResponse.setVal("svsItemUomDesc",lvsItemUomDesc); 
      String lvsAsetBookNaturDesc  = lvoItemRw.getString("svsAsetBookNaturDesc");
      cvoCoreResponse.setVal("svsAsetBookNaturDesc",lvsAsetBookNaturDesc);
      String lvsAsetBookNaturId  = lvoItemRw.getString("svsAsetBookNaturId");
      cvoCoreResponse.setVal("svsAsetBookNaturId",lvsAsetBookNaturId);
    }
    cvoCoreResponse.setVal("svoItem", lvoItemRw); 
  }
  
  public void getItemDtlsRevs() throws SQLException{
    DBManager lvoDbManager = cvoCoreRequest.getDBManager();
    String lvsItemIdy = cvoCoreRequest.getVal("svsItemIdy");
    lvoDbManager.addContextParam("rvsItemIdy",lvsItemIdy);
    ResultSet lvoItemRw = lvoDbManager.selectResultSet("Item\\sarLoadGetItemDtlsRevs");
    if(lvoItemRw.next()){
      int lviAsetTaxCtgryD   = lvoItemRw.getInt("sviAsetTaxCtgryD");
      cvoCoreResponse.setVal("sviAsetTaxCtgryD",lviAsetTaxCtgryD);
      int lviAsetActCtgryD  = lvoItemRw.getInt("sviAsetActCtgryD");
      cvoCoreResponse.setVal("sviAsetActCtgryD",lviAsetActCtgryD); 
      String lvsAsetTaxCtgryId  = lvoItemRw.getString("svsAsetTaxCtgryId");
      cvoCoreResponse.setVal("svsAsetTaxCtgryId",lvsAsetTaxCtgryId);
      String lvsAsetActCtgryId  = lvoItemRw.getString("svsAsetActCtgryId");
      cvoCoreResponse.setVal("svsAsetActCtgryId",lvsAsetActCtgryId);
      String lvsItemUomId   = lvoItemRw.getString("svsItemUomId");
      cvoCoreResponse.setVal("svsItemUomId",lvsItemUomId);
      String lvsItemUomDesc  = lvoItemRw.getString("svsItemUomDesc");
      cvoCoreResponse.setVal("svsItemUomDesc",lvsItemUomDesc); 
      String lvsAsetBookNaturDesc  = lvoItemRw.getString("svsAsetBookNaturDesc");
      cvoCoreResponse.setVal("svsAsetBookNaturDesc",lvsAsetBookNaturDesc);
      String lvsAsetBookNaturId  = lvoItemRw.getString("svsAsetBookNaturId");
      cvoCoreResponse.setVal("svsAsetBookNaturId",lvsAsetBookNaturId);
    }
    cvoCoreResponse.setVal("svoItem", lvoItemRw); 
  }
  
  public void saveItemDtls() throws SQLException{
    RowSet lvoItemDtls = cvoCoreRequest.getRowSet("svoItem");
    Row lvoRwItem =  lvoItemDtls.moveToRow(0);
    ItemModel itemModel = ItemModel.load(lvoRwItem.getVal("svsItemIdy", ""), cvoDbManager); 
    itemModel.setItemIdy(lvoRwItem.getVal("svsItemIdyRvsed"));
    itemModel.setItemDesc(lvoRwItem.getVal("svsItemDesc"));
    itemModel.setItemUomIdy(cvoCoreRequest.getVal("svsItemUomId"));
    itemModel.setItemDateEfctvFrom(lvoRwItem.getDateVal("svdItemDateEfctvFrom"));
    itemModel.setItemDateEfctvTo(lvoRwItem.getDateVal("svdItemDateEfctvTo",App.DEFAULT_MAX_DATE));
    String lvsIsSellItem = lvoRwItem.getVal("svbIsSellItem");
      if(lvsIsSellItem == null || lvsIsSellItem.isBlank()){ 
        lvsIsSellItem = "0"; 
      } 
      itemModel.setIsSellItem(lvsIsSellItem);

    String lvsIsPchsItem = lvoRwItem.getVal("svbIsPchsItem");
      if( lvsIsPchsItem == null || lvsIsPchsItem.isBlank()){ 
         lvsIsPchsItem = "0"; 
      } 
      itemModel.setIsPchsItem(lvsIsPchsItem);

    String lvsIsAsetItem = lvoRwItem.getVal("svbIsAsetItem");
      if( lvsIsAsetItem == null || lvsIsAsetItem.isBlank()){ 
        lvsIsAsetItem = "0"; 
      } 
      itemModel.setIsAsetItem(lvsIsAsetItem);

    String lvsIsItcAplcbl = lvoRwItem.getVal("svbIsItcAplcbl");
      if(lvsIsItcAplcbl == null || lvsIsItcAplcbl.isBlank()){ 
        lvsIsItcAplcbl = "0"; 
      } 
      itemModel.setIsItcAplcbl(lvsIsItcAplcbl);

    itemModel.setItemHsnSac(lvoRwItem.getVal("svsItemHsnSac"));
    String lvsAsetBookNaturId = cvoCoreRequest.getVal("svsAsetBookNaturId"); 
    
    if(lvsAsetBookNaturId == null || lvsAsetBookNaturId.isBlank())
    {
      lvsAsetBookNaturId=" ";
    }
    itemModel.setAsetBookNatur(lvsAsetBookNaturId);
    itemModel.setAsetTaxCtgryD(cvoCoreRequest.getIntVal("sviAsetTaxCtgryD",0));
    itemModel.setAsetActCtgryD(cvoCoreRequest.getIntVal("sviAsetActCtgryD",0));
    String lvsIsInvItem = lvoRwItem.getVal("svbIsInvItem");
      if( lvsIsInvItem == null || lvsIsInvItem.isBlank()){ 
         lvsIsInvItem = "0"; 
      } 
      itemModel.setIsInvItem(lvsIsInvItem);

    String lvsIsXpnsItem = lvoRwItem.getVal("svbIsXpnsItem");
      if(lvsIsXpnsItem == null || lvsIsXpnsItem.isBlank()){ 
        lvsIsXpnsItem = "0"; 
      } 
      itemModel.setIsXpnsItem(lvsIsXpnsItem);

   itemModel.updateOrInsert();
   
   RowSet lvoMstrMpngDtls = cvoCoreRequest.getRowSet("svoNtyMstrMpng"); 
    while(lvoMstrMpngDtls.hasNext()){
      Row lvoRowMstrMpngDtls = lvoMstrMpngDtls.moveNext(); 
//      cvoLogger.debug("item D before load::::::::::::::::::::::::::" +itemModel.cviItemD);
//        itemModel = ItemModel.load(lvoRwItem.getVal("svsItemIdy", " "), cvoDbManager); 
//      cvoLogger.debug("item D after load::::::::::::::::::::::::::" +itemModel.cviItemD);
        NtyMstrMpngModel ntyMstrMpngModel = NtyMstrMpngModel.load(itemModel.cviItemD,lvoRowMstrMpngDtls.getIntVal("sviBizNtyD",-1),cvoDbManager);
        
        String lvsBizNtyIdy = lvoRowMstrMpngDtls.getVal("svsBizNtyIdy");
        cvoDbManager.addContextParam("rvsBizNtyIdy",lvsBizNtyIdy);
        ResultSet lviBizNtyIdyD = cvoDbManager.selectResultSet("Item/s1rBizNtyIdyD");
        lviBizNtyIdyD.next();
        int BizNtyIdyD = lviBizNtyIdyD.getInt("sviBizNtyIdyD");
        ntyMstrMpngModel.setNtyD(BizNtyIdyD);
        String lvsBizNtyItemAcess = lvoRowMstrMpngDtls.getVal("svsBizNtyItemAcess");
        if(lvsBizNtyItemAcess == null || lvsBizNtyItemAcess.isBlank())
        {
          lvsBizNtyItemAcess = "0";
        }
        ntyMstrMpngModel.setNtyMstrActv(lvsBizNtyItemAcess);
        ntyMstrMpngModel.setNtyMstrFrom(lvoRowMstrMpngDtls.getDateVal("svdBizNtyItemAcessFrom"));
        ntyMstrMpngModel.setNtyMstrTo(lvoRowMstrMpngDtls.getDateVal("svdBizNtyItemAcessTo",App.DEFAULT_MAX_DATE));
        ntyMstrMpngModel.save();
    }

   if( cvoDbManager.canCommit()){ 
      cvoDbManager.commitTrans(); 
      cvoCoreResponse.addMessage(CoreMessage.RECORDS_SUBMITTED_SUCCESSFULLY); 
    } 
    
    loadItemDtls();
  }
}


