SELECT
	gopt_goptcode,		-- �o�͎����h�c
	gopt_name,			-- �o�͎�������
	gopt_attend_start,	-- �o���ΏۊJ�n�c�s
	gopt_attend_end		-- �o���ΏۏI���c�s
FROM
	mst_cmlguideoutputterm
WHERE
	gopt_user = ?
	AND gopt_year = ?
	AND gopt_score_flg = '0'
	AND gopt_tempeval_flg = '0'
ORDER BY
	gopt_order
