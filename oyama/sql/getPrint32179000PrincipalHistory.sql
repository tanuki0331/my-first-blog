SELECT TOP 4
     stf_name_w /*����(�ʏ�)*/
	,stf_kana /*����(�ӂ肪��)*/
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
