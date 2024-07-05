SELECT 
  tabBizNty.tcsBizNtyName AS  svsBizNtyName, 
  tabNgmt.tcsNgmtIdy      AS  svsNgmtIdy,
  tabNgmt.tciNgmtD        AS  sviNgmtD,
  '0'                     AS  svbSel
FROM 
  tabNgmt 
    INNER JOIN tabLnk ON 
        tabNgmt.tciSysHostD = tabLnk.tciSysHostD 
    AND tabNgmt.tciNgmtD    = CAST(tabLnk.tcsLnkFrom AS INTEGER) 
      INNER JOIN tabBizNty ON 
          tabLnk.tciSysHostD                = tabBizNty.tciSysHostD 
      AND CAST(tabLnk.tcsLnkTo AS INTEGER)  = tabBizNty.tciBizNtyD 
WHERE 
      (     tabNgmt.tciSysHostD   = 1 
        AND tabNgmt.tcbNgmtActv   = 1 
        AND tabNgmt.tciNgmtBilerD = 'rviXfrBilerFromD'
      ) 
  AND (     tabLnk.tcbLnkActv = 1 
        AND tabLnk.tcsLnkIdx  = 'NgmtCstmrLoc'
      ) 
  AND tabBizNty.tcsBizNtySts  = 'O' 
ORDER BY 
  svsBizNtyName ASC, 
  svsNgmtIdy    ASC ;
