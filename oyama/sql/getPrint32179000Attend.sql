SELECT
   cls_stucode AS stucode
  ,GOPT.gopt_goptcode AS goptcode
  ,enf_date_year AS year
  ,enf_month AS month
  ,CASE
		WHEN
			stu_entry <= enf_date_year + enf_month + '31'
			AND ISNULL(reg_start,'99999999') >= enf_date_year + enf_month + '01' THEN '1' --在籍判定
		ELSE '0'
    END AS onRegist
  ,ATT.授業日数 AS teachingSum
  ,ATT.出席日数 AS attendSum
  ,ATT.[欠席(病欠)] + ATT.[欠席(その他)] AS absenceSum
  ,ATT.出停・忌引 AS stopSum
  ,ATT.遅刻 AS lateSum
  ,ATT.早退 AS leaveSum
FROM tbl_class
LEFT JOIN tbl_student AS STU
ON(
  cls_user = stu_user AND
  cls_stucode = stu_stucode
)
INNER JOIN mst_cmlguideoutputterm AS GOPT
ON(
  cls_user = GOPT.gopt_user AND
  cls_year = GOPT.gopt_year AND
  ISNULL(GOPT.gopt_score_flg, '') <> '1' AND
  ISNULL(GOPT.gopt_tempeval_flg, '') <> '1'
)
LEFT JOIN tbl_register
ON(
  cls_user = reg_user AND
  cls_stucode = reg_stucode
)
--出欠集計サブクエリ(ここから)
LEFT JOIN
(
SELECT
   gopt_goptcode
  ,enf_month
  ,SUBSTRING(enf_date, 0, 5) AS enf_date_year
  ,stu_stucode
  ,SUM(CASE WHEN stu_entry <= enf_date AND enf_date <= ISNULL(reg_start, '99999999') AND enf_enforce = 1 THEN 1 ELSE 0 END) AS '授業日数'
  ,SUM(CASE WHEN akj_schoolkind = 1 THEN 1 ELSE 0 END) AS '出停・忌引'
  ,SUM(CASE WHEN stu_entry <= enf_date AND enf_date <= ISNULL(reg_start, '99999999') AND enf_enforce = 1 THEN 1 ELSE 0 END)
    - SUM(CASE WHEN akj_schoolkind = 1 THEN 1 ELSE 0 END) AS '要出席日数'
  ,SUM(CASE WHEN akj_code = COD606101.cod_value4 THEN 1 ELSE 0 END)
   + SUM(CASE WHEN akj_special <> '0' AND sas_replaceattkind = COD606101.cod_value4 AND sas_scorptabsent = '1' THEN 1 ELSE 0 END) AS '欠席(病欠)'
  ,SUM(CASE WHEN akj_code = COD606101.cod_value5 THEN 1 ELSE 0 END)
   + SUM(CASE WHEN akj_special <> '0' AND sas_replaceattkind = COD606101.cod_value5 AND sas_scorptabsent = '1' THEN 1 ELSE 0 END) AS '欠席(その他)'
  ,SUM(CASE WHEN stu_entry <= enf_date AND enf_date <= ISNULL(reg_start, '99999999') AND enf_enforce = 1 THEN 1 ELSE 0 END)
   - SUM(CASE WHEN akj_schoolkind = 1 THEN 1 ELSE 0 END)
   - SUM(CASE WHEN akj_absenceflg = 1 THEN 1 ELSE 0 END)
   - SUM(CASE WHEN akj_special = 1 AND sas_rbabsent = 1 THEN 1 ELSE 0 END) AS '出席日数'
  ,SUM(CASE WHEN akj_lateflg  = 1 THEN 1 ELSE 0 END) AS '遅刻'
  ,SUM(CASE WHEN akj_leaveflg = 1 THEN 1 ELSE 0 END) AS '早退'
FROM mst_cmlguideoutputterm
LEFT JOIN tbl_enforceday
ON(
  gopt_user = enf_user AND
  enf_dcode = '001' AND  --001固定
  enf_glade = ? AND			-- '処理中のクラスの学年'
  gopt_year = enf_year AND
  enf_date BETWEEN gopt_attend_start AND gopt_attend_end
)
LEFT JOIN tbl_student
ON(
  enf_user = stu_user AND
  stu_stucode = ?
)
LEFT JOIN tbl_register
ON(
  stu_user = reg_user AND
  stu_stucode = reg_stucode
)
LEFT JOIN tbl_attend
ON(
  enf_user = att_user AND
  enf_year = att_year AND
  att_ccode = '99999999' AND  --99999999固定
  stu_stucode = att_stucode AND
  att_kind = 'D' AND  --D固定
  enf_date = att_date AND
  att_hour = '99'  --99固定
)
LEFT JOIN mst_attendkind_jr
ON(
  enf_user = akj_user AND
  enf_year = akj_year AND
  att_attendkind = akj_code
)
LEFT JOIN mst_specialattendsetting
ON(
  enf_user = sas_user AND
  enf_year = sas_year AND
  akj_special = sas_attspecial
)
LEFT JOIN mst_code AS COD606101
ON(
  akj_user = COD606101.cod_user AND
  COD606101.cod_kind = 606 AND
  COD606101.cod_code = '101'
)
WHERE
  gopt_user = ? AND		-- '所属コード'
  gopt_year = ? AND		-- '処理年度'
  ISNULL(gopt_score_flg, '') <> '1' AND
  ISNULL(gopt_tempeval_flg, '') <> '1'
GROUP BY
  gopt_goptcode,
  enf_month,
  SUBSTRING(enf_date, 0, 5),
  stu_stucode
) ATT
ON(
  STU.stu_stucode = ATT.stu_stucode AND
  GOPT.gopt_goptcode = ATT.gopt_goptcode
)
--出欠集計サブクエリ(ここまで)
WHERE
  cls_user = ? AND		-- '所属コード'
  cls_year = ? AND		-- '処理年度'
  cls_stucode = ? AND
  enf_month IS NOT NULL
ORDER BY
  stu_user,
  stu_year,
  STU.stu_stucode,
  enf_date_year,
  enf_month
