SELECT
  cls_stucode AS stuCode	--学籍番号
  ,cls_glade  AS stuGrade	--学年
  ,cls_class AS stuClass	/* 学級 */
  ,cls_number AS stuNumber	--出席番号
  ,cls_clsno AS stuClsno
  ,ISNULL(CONVERT(varchar(3), cls_reference_number),'') AS refNumber -- 出席番号（公簿）
  -- 児童氏名 履歴通称＞通称＞履歴戸籍＞戸籍
  ,CASE WHEN history.sth_items IS NOT NULL AND history.sth_items <> '' THEN history.sth_items								-- 履歴氏名
        WHEN st4_name IS NOT NULL AND st4_name <> '' THEN st4_name														-- 氏名
        END AS stuName
  ,CASE WHEN history_koseki.sth_items IS NOT NULL AND history_koseki.sth_items <> '' THEN history_koseki.sth_items		-- 履歴戸籍氏名
        ELSE st4_name_r																										-- 戸籍氏名
        END AS stuKosekiName
  -- 児童仮名 履歴通称＞通称＞履歴戸籍＞戸籍
  ,CASE WHEN history.sth_items2 IS NOT NULL AND history.sth_items2 <> '' THEN history.sth_items2							-- 履歴通称かな
        WHEN st4_hkana IS NOT NULL AND st4_hkana <> '' THEN st4_hkana														-- 通称かな
        END AS stuKana
  ,CASE WHEN history_koseki.sth_items2 IS NOT NULL AND history_koseki.sth_items2 <> '' THEN history_koseki.sth_items2		-- 履歴戸籍かな
        ELSE st4_kana_r																										-- 戸籍かな
        END AS stuKosekiKana
  ,stu_birth  AS stuBirth	--生年月日
  ,ISNULL(spg_name, '') AS spGroupname -- 特別支援学級グループ名称
  ,ISNULL(EX.hmr_class, '') AS excClass  -- 特別支援交流先学級名
  ,ISNULL(CONVERT(varchar(3), exc_number),'') AS excNumber	-- 特別支援交流先出席番号
  ,ISNULL(CONVERT(varchar(3), exc_clsno),'') AS excClsno	-- 特別支援交流先クラスNo
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

-- 特別支援学級グループ
LEFT JOIN tbl_spclass_group
  ON spgd_user = cls_user
 AND spgd_year = cls_year
 AND spgd_clsno = cls_clsno

-- 特別支援学級グループ名称
LEFT JOIN mst_spclass_group
ON  spgd_user = spg_user
AND spgd_year = spg_year
AND spgd_code = spg_code

-- 特別支援学級交流先の組
LEFT JOIN tbl_exchange_class
  ON exc_user = cls_user
 AND exc_year = cls_year
 AND exc_stucode = cls_stucode

-- 特別支援学級交流先のホームルーム
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
        AND sth_kind = '5' --固定値(5:児童生徒氏名(通称))
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
        AND sth_kind = '1' --固定値(1:児童生徒氏名(戸籍))
       ) AS history
    WHERE
        rk = 1
    ) AS history_koseki
ON  history_koseki.sth_user = st4_user
AND history_koseki.sth_stucode = st4_stucode