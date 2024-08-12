SELECT 
    atbLoc_BizNtyOnr.tcsBizNtyLocName              AS svsBizNtyOnrLocDesc,
    atbLoc_BizNtyOnr.tcsBizNtyLocAdrsLine1         AS svsBizNtyOnrLocAdrsLine1,
    atbLoc_BizNtyOnr.tcsBizNtyLocAdrsLine2         AS svsBizNtyOnrLocAdrsLine2,
    atbLoc_BizNtyOnr.tcsBizNtyLocPostCode          AS svsBizNtyOnrLocPinCode, 
    atbDir_BizNtyOnrState.tcsDirDesc             AS svsBizNtyOnrLocState,
    atbDir_BizNtyOnrCntry.tcsDirDesc             AS svsBizNtyOnrLocCntry,
    atbLoc_BizNtyOnr.tcsBizNtyLocSalesTaxIdy       AS svsBizNtyOnrLocGst,
	atbLoc_TxctgParty.tcsBizNtyLocName              AS svsTxctgPartyLocDesc,
    atbLoc_TxctgParty.tcsBizNtyLocAdrsLine1         AS svsTxctgPartyLocAdrsLine1,
    atbLoc_TxctgParty.tcsBizNtyLocAdrsLine2         AS svsTxctgPartyLocAdrsLine2,
    atbLoc_TxctgParty.tcsBizNtyLocPostCode          AS sviTxctgPartyLocPinCode,
    atbDir_TxctgPartyState.tcsDirDesc             AS svsTxctgPartyLocState,
    atbDir_TxctgPartyCntry.tcsDirDesc             AS svsTxctgPartyLocCntry,
    atbLoc_TxctgParty.tcsBizNtyLocSalesTaxIdy       AS svsTxctgPartyLocGst,
      tabBil.tcsBilIdy                     AS svsBilIdy,
      tabBil.tcdBilDate                    AS svdBilDate,
      tabBil.tcsPchsOrdrIdy                AS svsPchsOrdrIdy,
      tabBil.tcdPchsOrdrDate               AS svdPchsOrdrDate,
      tabBil.tcdBilDueDate                 AS svdBilDueDate,
      tabBil.tcsBilCrncIdy                 AS svsBilCrncIdy,
      tabBil.tcnBilXchngRate               AS svnBilXchngRate,
      tabBil.tciBilPrprdUsrD               AS svsBilPrprdUsrDesc,
      tabBil.tciBilAprvdUsrD               AS svsBilAprvdUsrDesc,
      tabBil.tciTaxTmpltD                  AS svsTaxTmpltDesc
  FROM 
   tabBil 
   LEFT JOIN
     tabBizNtyLoc AS atbLoc_BizNtyOnr ON
     atbLoc_BizNtyOnr.tciBizNtyLocD = tabBil.tciBizNtyOnrLocD  AND
     atbLoc_BizNtyOnr.tcbBizNtyLocG = '1'
   LEFT JOIN
     tabBizNtyLoc AS atbLoc_TxctgParty ON
     atbLoc_TxctgParty.tciBizNtyLocD = tabBil.tciTxctgPartyLocD  AND
     atbLoc_TxctgParty.tcbBizNtyLocG = '1' 
   LEFT JOIN  tabDir AS atbDir_BizNtyOnrCntry ON 
	    atbDir_BizNtyOnrCntry.tcsDirActv             = '1'                    AND 
	    atbDir_BizNtyOnrCntry.tcsDirIdx              = 'Cntry'                AND 
	    atbLoc_BizNtyOnr.tcsBizNtyLocCntry = atbDir_BizNtyOnrCntry.tcsDirIdy  AND 
		atbLoc_BizNtyOnr.tcbBizNtyLocG              = '1'
    LEFT JOIN tabDir AS atbDir_BizNtyOnrState ON 
	  atbDir_BizNtyOnrState.tcsDirActv        = '1'                    AND 
	  atbDir_BizNtyOnrState.tcsDirIdx         = 'State'                AND 
	  atbLoc_BizNtyOnr.tcsBizNtyLocState = atbDir_BizNtyOnrState.tcsDirIdy AND
      atbLoc_BizNtyOnr.tcbBizNtyLocG              = '1' 
	  LEFT JOIN  tabDir AS atbDir_TxctgPartyCntry ON 
	    atbDir_TxctgPartyCntry.tcsDirActv             = '1'                    AND 
	    atbDir_TxctgPartyCntry.tcsDirIdx              = 'Cntry'                AND 
	    atbLoc_TxctgParty.tcsBizNtyLocCntry = atbDir_TxctgPartyCntry.tcsDirIdy  AND 
		atbLoc_TxctgParty.tcbBizNtyLocG              = '1'
    LEFT JOIN tabDir AS atbDir_TxctgPartyState ON 
	  atbDir_TxctgPartyState.tcsDirActv        = '1'                    AND 
	  atbDir_TxctgPartyState.tcsDirIdx         = 'State'                AND 
	  atbLoc_TxctgParty.tcsBizNtyLocState = atbDir_TxctgPartyState.tcsDirIdy AND
      atbLoc_TxctgParty.tcbBizNtyLocG              = '1';