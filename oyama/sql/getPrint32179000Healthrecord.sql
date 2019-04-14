SELECT
   hlr_stucode
  ,hlr_goptcode
  ,hlr_height
  ,hlr_weight
  ,hlr_v_left
  ,hlr_v_right
 FROM tbl_healthrecord

WHERE hlr_user = ?
  AND hlr_year = ?
  AND hlr_timescode = ?
  AND hlr_stucode = ?

ORDER BY hlr_stucode