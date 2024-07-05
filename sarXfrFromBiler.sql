SELECT DISTINCT 
  (tabEmp.tcsEmpName + ' | ' + atbDir_Dptmt.tcsDirIdy + ' | ' + atbDir_Loc.tcsDirIdy) AS svsBiler, 
  tabEmp.tciEmpD                                                                      AS  sviXfrBilerFromD 
FROM 
  tabNgmt 
    INNER JOIN tabEmp ON 
        tabNgmt.tciSysHostD   = tabEmp.tciSysHostD 
    AND tabNgmt.tciNgmtBilerD = tabEmp.tciEmpD 
      INNER JOIN tabDir AS atbDir_Dptmt ON 
          tabEmp.tciSysHostD  = atbDir_Dptmt.tciSysHostD 
      AND tabEmp.tciEmpDptmtD = atbDir_Dptmt.tciDirD 
        INNER JOIN tabDir AS atbDir_Loc ON 
            tabEmp.tciSysHostD  = atbDir_Loc.tciSysHostD 
        AND tabEmp.tciEmpLocD   = atbDir_Loc.tciDirD 
          INNER JOIN tabFxnRit ON 
          tabEmp.tciUsrD  = tabFxnRit.tciUsrD 
WHERE 
      (     tabNgmt.tciSysHostD = 1 
        AND tabNgmt.tcbNgmtActv = 1
      ) 
  AND (     atbDir_Dptmt.tcsDirActv = '1' 
        AND atbDir_Dptmt.tcsDirIdx  = 'Dptmt'
      ) 
  AND (     atbDir_Loc.tcsDirActv = '1' 
        AND atbDir_Loc.tcsDirIdx  = 'Loc'
      ) 
  AND tabEmp.tcbEmpActv = 1 
  AND (     tabFxnRit.tcbFxnRitActv = 1
        AND tabFxnRit.tciRoleD      = -5 /* Refers to Role = 'Billing' */
      ) 
ORDER BY 
  svsBiler  ASC ;