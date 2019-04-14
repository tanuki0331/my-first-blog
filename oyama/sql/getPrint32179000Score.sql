SELECT
   rvpv_stucode AS clsStucode					--�w�Дԍ�
  ,item_code AS itemCode					--���ȃR�[�h
  ,item_name AS itemName					--�ʒm�\�p���Ȗ���
  ,rivt_rivtcode AS rivtcode					--�ϓ_�R�[�h
  ,rivt_purpose AS purpose						--�ϓ_
  ,ISNULL(rvpe_reportdisplay, '') AS viewpointvalue	--�]���R�[�h
  ,rvpe_cannot_flg
  ,rvpe_none_flg
FROM
(
  SELECT
    DISTINCT
    sth_user AS item_user,
    sth_year AS item_year,
    sth_grade AS item_grade,
    sth_item AS item_code,  --���ȃR�[�h
    COD95.cod_name1 AS item_name,  --�ʒm�\�p���Ȗ���
    COD59.cod_value2 AS item_order
  FROM mst_standardteachinghour

  INNER JOIN mst_code AS COD59
  ON  sth_user = COD59.cod_user
  AND sth_item = COD59.cod_code
  AND COD59.cod_value1 = '0'  --0:�K�C����
  AND COD59.cod_kind = 59

  LEFT JOIN mst_code AS COD95
  ON  COD59.cod_user = COD95.cod_user
  AND COD59.cod_code = COD95.cod_code
  AND COD95.cod_kind = 95
  WHERE sth_user = ? --'�����R�[�h'
  AND   sth_year = ? --'�����N�x'
  AND   sth_grade = ? --'�o�͑Ώې��k�̊w�N'
) ITEM

LEFT JOIN mst_scorptitemviewpointtitle
ON  ITEM.item_user = rivt_user
AND ITEM.item_year = rivt_year
AND ITEM.item_grade = rivt_grade
AND ITEM.item_code = rivt_item
AND rivt_term = ?
AND ISNULL(rivt_notuse, '') <> '1'

LEFT JOIN tbl_scorptviewpointvalue
ON  rivt_user = rvpv_user
AND rivt_year = rvpv_year
AND rvpv_stucode = ? --'�o�͑Ώې��k�̊w�Дԍ�'
AND rivt_grade =rvpv_grade
AND rivt_term = rvpv_goptcode
AND rivt_item = rvpv_item
AND rivt_rivtcode = rvpv_rivtcode

LEFT JOIN mst_scorptviewpointestimate
ON  rvpv_user = rvpe_user
AND rvpv_year = rvpe_year
AND rvpv_grade = rvpe_grade
AND rvpv_manualrevpecode = rvpe_rvpecode

ORDER BY
  item_order, rivt_order
