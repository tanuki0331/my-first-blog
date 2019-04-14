SELECT top 4
    ord
   ,stf_name_w
   ,stf_kana
FROM
   (/* �N���X�S�C�����e�[�u��(�D��)*/
    SELECT DISTINCT
        0 AS ord
       ,stfh_stfcode AS stfcode
    FROM
        tbl_staffhistory
    WHERE
        stfh_user = ?
    AND stfh_year = ?
    AND stfh_end >= ?
    AND stfh_start <= ?
    AND stfh_kind   = '1' /* �Œ�l(1:���S�C) */
    AND EXISTS
       (SELECT * FROM tbl_spclass_group
        WHERE
            stfh_user = spgd_user
        AND stfh_year = spgd_year
        AND stfh_clsno = spgd_clsno
        AND spgd_code = ?
       )
    ) STF

    INNER JOIN
        mst_staff
    ON stf_stfcode = stfcode
WHERE
    stf_user = ?
ORDER BY
    ord, stf_kana
