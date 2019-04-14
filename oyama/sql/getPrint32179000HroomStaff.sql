SELECT
    stf_name_w
   ,stf_kana
FROM
    tbl_hroom
    LEFT JOIN
        mst_staff
    ON  hmr_user = stf_user
    AND hmr_stfcodem = stf_stfcode
WHERE
    hmr_user = ?
AND hmr_year = ?
AND hmr_clsno = ?
