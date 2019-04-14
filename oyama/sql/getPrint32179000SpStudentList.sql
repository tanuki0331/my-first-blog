/* ���ђʒm�\��� ���k�ꗗ */

SELECT
  cls_user,
  cls_stucode AS stuCode, /* �w�Дԍ� */
  cls_glade AS stuGrade, /* �w�N */
  hmr_class AS stuClass, /* �g */
  cls_clsno AS stuClsno, /* �N���XNo */
  cls_number AS stuNumber,/* �� */
  (CASE WHEN isKoryu = '1' THEN '(��)' ELSE '' END) + st4_name   AS stuName,	/*��������(�ʏ�)*/
  isKoryu
FROM
(
	SELECT
		cls_user, cls_year, cls_glade, cls_clsno, cls_stucode, cls_number, '0' AS isKoryu
	FROM tbl_class
	UNION
	SELECT
		exc_user, exc_year, hmr_glade, exc_clsno, exc_stucode, exc_number , '1'
	FROM tbl_exchange_class
		INNER JOIN tbl_hroom ON exc_user = hmr_user AND exc_year = hmr_year AND exc_clsno = hmr_clsno
		INNER JOIN tbl_student ON exc_user = stu_user AND exc_stucode = stu_stucode AND stu_audit = '1'
) CLS
LEFT JOIN tbl_hroom
ON(
  CLS.cls_user = hmr_user AND
  CLS.cls_year = hmr_year AND
  CLS.cls_clsno = hmr_clsno
)
INNER JOIN tbl_spclass_group
ON(
  hmr_user = spgd_user AND
  hmr_year = spgd_year AND
  spgd_code = ? AND
  hmr_clsno = spgd_clsno
)
LEFT JOIN tbl_student4
ON(
  CLS.cls_user = st4_user AND
  CLS.cls_stucode = st4_stucode
)
WHERE
  CLS.cls_user = ? AND
  CLS.cls_year = ?
ORDER BY
  CLS.cls_user,hmr_glade, hmr_order, CLS.cls_number
