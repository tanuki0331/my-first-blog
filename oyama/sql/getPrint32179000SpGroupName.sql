SELECT
    ISNULL(spg_name, '') AS spg_name
FROM
    tbl_spclass_group
    LEFT JOIN
        mst_spclass_group
    ON  spgd_user = spg_user
    AND spgd_year = spg_year
    AND spgd_code = spg_code
WHERE
    spgd_user = ?
AND spgd_year = ?
AND spgd_clsno = ?
