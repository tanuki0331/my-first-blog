SELECT TOP 4
    stf_name_w
   ,stf_kana
FROM
    tbl_staffhistory
    LEFT JOIN
        mst_staff
    ON  stfh_user = stf_user
    AND stfh_stfcode = stf_stfcode
WHERE
    stfh_user = ?
AND stfh_year = ?
AND stfh_clsno = ?
AND stfh_kind = '1' --ŒÅ’è’l(1F³’S”C)
AND stfh_end >= ?
AND stfh_start <= ?
ORDER BY
    stfh_start DESC
