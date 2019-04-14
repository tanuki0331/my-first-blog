SELECT
	ssv.rsav_stucode,	/* 学籍番号 */
	ssv.rsav_term,		/* 出力時期 */
	cod.cod_name1,		/* 特別活動名称 */
	ssv.rsav_record		/* 特別活動の学習の記録 */
FROM
	mst_code cod
LEFT JOIN
	tbl_scorptspeactvalue ssv
ON
	cod.cod_user = ssv.rsav_user
AND cod.cod_code = ssv.rsav_rsatcode
AND cod.cod_value1 = ssv.rsav_grade
AND cod.cod_value2 = ssv.rsav_term
AND cod.cod_kind = 692 --固定値
AND ssv.rsav_year = ? /* 年度 */
AND ssv.rsav_stucode = ? /* 学籍番号 */
LEFT JOIN
    mst_cmlguideoutputterm
ON  rsav_user = gopt_user
AND rsav_year = gopt_year
AND ssv.rsav_term = gopt_goptcode
AND cod_value3 = gopt_goptcode --処理中出力時期コードに関連する任期の特活情報のみ取得
WHERE
	cod.cod_user = ? /* 所属コード */
AND cod.cod_value1 = ? /* 学年 */
AND cod.cod_value2 = ?  /* 出力時期 */