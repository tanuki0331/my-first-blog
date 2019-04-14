SELECT
 useh_name_s
FROM
 tbl_userhistory
WHERE
 useh_kind1 = '2'		-- 公立
 AND useh_kind2 = ?		-- 学校種別 1:小学校,2:中学校
 AND useh_user = ?		-- 所属コード
 AND useh_year = ?		-- 年度