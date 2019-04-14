SELECT
  rcom_stucode,  --äwê–î‘çÜ
  rcom_comment  --èäå©
FROM tbl_scorptcomment
WHERE
  rcom_user = ? AND
  rcom_year = ? AND
  rcom_term = ? AND
  rcom_stucode = ?
