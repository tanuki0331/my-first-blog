SELECT
  rcom_stucode,  --学籍番号
  rcom_comment  --所見
FROM tbl_scorptcomment
WHERE
  rcom_user = ? AND
  rcom_year = ? AND
  rcom_term = ? AND
  rcom_stucode = ?
