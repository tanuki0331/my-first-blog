SELECT DISTINCT
    stf_name_w
   ,stf_kana
FROM
   (
	SELECT
		hmr_stfcodem AS stfcode
	FROM
		tbl_spclass_group
	LEFT JOIN
		tbl_hroom
	ON
		spgd_user = hmr_user
	AND spgd_year = hmr_year
	AND spgd_clsno = hmr_clsno
    WHERE
        spgd_code = ?
	AND spgd_user = ?
	AND spgd_year = ?
	) STF
    INNER JOIN
        mst_staff
    ON stf_stfcode = STF.stfcode
WHERE
    stf_user = ?
ORDER BY
    stf_kana
