SELECT
  rcom_stucode,  --�w�Дԍ�
  rcom_comment  --����
FROM tbl_scorptcomment
WHERE
  rcom_user = ? AND
  rcom_year = ? AND
  rcom_term = ? AND
  rcom_stucode = ?
