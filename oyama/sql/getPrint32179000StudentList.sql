SELECT
   cls_stucode AS stuCode	--学籍番号
  ,cls_glade  AS stuGrade	--学年
  ,cls_number AS stuNumber	--出席番号
  ,cls_clsno AS stuClsno
  ,(CASE WHEN isKoryu = '1' THEN '(交)' ELSE '' END) + st4_name   AS stuName	--児童氏名(通称)
  ,isKoryu
FROM
   (SELECT
        cls_user, cls_year, cls_glade, cls_clsno, cls_stucode, cls_number, '0' AS isKoryu
    FROM tbl_class
    UNION
    SELECT
        exc_user, exc_year, hmr_glade, exc_clsno, exc_stucode, exc_number , '1'
    FROM tbl_exchange_class
        INNER JOIN tbl_hroom ON exc_user = hmr_user AND exc_year = hmr_year AND exc_clsno = hmr_clsno
        INNER JOIN tbl_student ON exc_user = stu_user AND exc_stucode = stu_stucode AND stu_audit = '1'
    ) CLS
LEFT JOIN tbl_student4
ON(
  cls_user = st4_user AND
  cls_stucode = st4_stucode
)

WHERE cls_user = ?
  AND cls_year = ?
  AND cls_clsno = ?

ORDER BY cls_number