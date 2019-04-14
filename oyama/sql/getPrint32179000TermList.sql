SELECT
	gopt_goptcode,		-- 出力時期ＩＤ
	gopt_name,			-- 出力時期名称
	gopt_attend_start,	-- 出欠対象開始ＤＴ
	gopt_attend_end		-- 出欠対象終了ＤＴ
FROM
	mst_cmlguideoutputterm
WHERE
	gopt_user = ?
	AND gopt_year = ?
	AND gopt_score_flg = '0'
	AND gopt_tempeval_flg = '0'
ORDER BY
	gopt_order
