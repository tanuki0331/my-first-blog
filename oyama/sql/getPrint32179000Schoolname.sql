SELECT
 useh_name_s
FROM
 tbl_userhistory
WHERE
 useh_kind1 = '2'		-- ����
 AND useh_kind2 = ?		-- �w�Z��� 1:���w�Z,2:���w�Z
 AND useh_user = ?		-- �����R�[�h
 AND useh_year = ?		-- �N�x