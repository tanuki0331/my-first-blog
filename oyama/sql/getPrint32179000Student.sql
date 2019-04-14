SELECT
  cls_stucode AS stuCode	--wÐÔ
  ,cls_glade  AS stuGrade	--wN
  ,cls_class AS stuClass	/* w */
  ,cls_number AS stuNumber	--oÈÔ
  ,cls_clsno AS stuClsno
  ,ISNULL(CONVERT(varchar(3), cls_reference_number),'') AS refNumber -- oÈÔiöëj
  -- ¶¼ ðÊÌÊÌðËÐËÐ
  ,CASE WHEN history.sth_items IS NOT NULL AND history.sth_items <> '' THEN history.sth_items								-- ð¼
        WHEN st4_name IS NOT NULL AND st4_name <> '' THEN st4_name														-- ¼
        END AS stuName
  ,CASE WHEN history_koseki.sth_items IS NOT NULL AND history_koseki.sth_items <> '' THEN history_koseki.sth_items		-- ðËÐ¼
        ELSE st4_name_r																										-- ËÐ¼
        END AS stuKosekiName
  -- ¶¼¼ ðÊÌÊÌðËÐËÐ
  ,CASE WHEN history.sth_items2 IS NOT NULL AND history.sth_items2 <> '' THEN history.sth_items2							-- ðÊÌ©È
        WHEN st4_hkana IS NOT NULL AND st4_hkana <> '' THEN st4_hkana														-- ÊÌ©È
        END AS stuKana
  ,CASE WHEN history_koseki.sth_items2 IS NOT NULL AND history_koseki.sth_items2 <> '' THEN history_koseki.sth_items2		-- ðËÐ©È
        ELSE st4_kana_r																										-- ËÐ©È
        END AS stuKosekiKana
  ,stu_birth  AS stuBirth	--¶Nú
  ,ISNULL(spg_name, '') AS spGroupname -- ÁÊxwO[v¼Ì
  ,ISNULL(EX.hmr_class, '') AS excClass  -- ÁÊxð¬æw¼
  ,ISNULL(CONVERT(varchar(3), exc_number),'') AS excNumber	-- ÁÊxð¬æoÈÔ
  ,ISNULL(CONVERT(varchar(3), exc_clsno),'') AS excClsno	-- ÁÊxð¬æNXNo
FROM
   (SELECT
        cls_user, cls_year, cls_glade,hmr_class as cls_class, cls_clsno, cls_stucode, cls_number, cls_reference_number
    FROM tbl_class
    LEFT JOIN
	tbl_hroom
	ON
		cls_user = hmr_user
	AND cls_year = hmr_year
	AND cls_clsno = hmr_clsno
    WHERE cls_user = ?
      AND cls_year = ?
      AND cls_clsno = ?


    	UNION

    SELECT
        exc_user, exc_year, hmr_glade,hmr_class as cls_class, exc_clsno, exc_stucode, exc_number, cls_reference_number
    FROM tbl_exchange_class
        INNER JOIN tbl_hroom ON exc_user = hmr_user AND exc_year = hmr_year AND exc_clsno = hmr_clsno
        INNER JOIN tbl_student ON exc_user = stu_user AND exc_stucode = stu_stucode AND stu_audit = '1'
        INNER JOIN tbl_class ON exc_user = cls_user AND exc_year = cls_year AND exc_stucode = cls_stucode
    WHERE exc_user = ?
      AND exc_year = ?
      AND exc_clsno = ?
    ) CLS

LEFT JOIN tbl_student
  ON stu_user = cls_user
 AND stu_stucode = cls_stucode

LEFT JOIN tbl_student4
  ON st4_user = cls_user
 AND st4_stucode = cls_stucode

-- ÁÊxwO[v
LEFT JOIN tbl_spclass_group
  ON spgd_user = cls_user
 AND spgd_year = cls_year
 AND spgd_clsno = cls_clsno

-- ÁÊxwO[v¼Ì
LEFT JOIN mst_spclass_group
ON  spgd_user = spg_user
AND spgd_year = spg_year
AND spgd_code = spg_code

-- ÁÊxwð¬æÌg
LEFT JOIN tbl_exchange_class
  ON exc_user = cls_user
 AND exc_year = cls_year
 AND exc_stucode = cls_stucode

-- ÁÊxwð¬æÌz[[
LEFT JOIN tbl_hroom AS EX
  ON EX.hmr_user = exc_user
 AND EX.hmr_year = exc_year
 AND EX.hmr_clsno = exc_clsno

LEFT JOIN
   (SELECT
         sth_user
        ,sth_stucode
        ,sth_items
        ,sth_items2
    FROM
       (SELECT
            RANK() OVER(PARTITION BY sth_user, sth_stucode ORDER BY sth_changedate ASC) AS rk
           ,sth_user
           ,sth_stucode
           ,sth_items
           ,sth_items2
		   ,sth_history
        FROM
            tbl_stu_history
        WHERE
            sth_user = ?
        AND sth_changedate > ?
        AND sth_kind = '5' --Åèl(5:¶¶k¼(ÊÌ))
       ) AS history
    WHERE
        rk = 1
    ) AS history
ON  history.sth_user = st4_user
AND history.sth_stucode = st4_stucode

LEFT JOIN
   (SELECT
         sth_user
        ,sth_stucode
        ,sth_items
        ,sth_items2
    FROM
       (SELECT
            RANK() OVER(PARTITION BY sth_user, sth_stucode ORDER BY sth_changedate ASC) AS rk
           ,sth_user
           ,sth_stucode
           ,sth_items
           ,sth_items2
        FROM
            tbl_stu_history
        WHERE
            sth_user = ?
        AND sth_changedate > ?
        AND sth_kind = '1' --Åèl(1:¶¶k¼(ËÐ))
       ) AS history
    WHERE
        rk = 1
    ) AS history_koseki
ON  history_koseki.sth_user = st4_user
AND history_koseki.sth_stucode = st4_stucode