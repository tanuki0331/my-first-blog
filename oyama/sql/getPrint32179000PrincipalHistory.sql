SELECT TOP 4
     stf_name_w /*Ž–¼(’ÊÌ)*/
	,stf_kana /*Ž–¼(‚Ó‚è‚ª‚È)*/
FROM
    tbl_principal_history
    INNER JOIN
        mst_staff
    ON  prh_user=stf_user
    AND prh_staffcode=stf_stfcode
WHERE
    prh_user = ?
AND prh_end >= ?
AND prh_start <= ?
