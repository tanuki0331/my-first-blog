-- 外国語評価
SELECT
    cls_stucode
  , cls_glade
  , rivt_term
  , rivt_rivtcode
  , rivt_rivtname2
  , rivt_purpose
  , rivt_order
  , rfla_estimate
  , rfle_reportdisplay
  , rfla_eval
FROM tbl_class

LEFT JOIN mst_scorptitemviewpointtitle
  ON rivt_user = cls_user
 AND rivt_year = cls_year
 AND rivt_grade = cls_glade
 AND rivt_term = ?
 AND rivt_item = '426'	--固定値 426：外国語活動

LEFT JOIN tbl_scorptforeignlangact
  ON  rfla_user = rivt_user
 AND rfla_year = rivt_year
 AND rfla_grade = rivt_grade
 AND rfla_term = rivt_term
 AND rfla_code = rivt_rivtcode
 AND rfla_stucode = cls_stucode

LEFT JOIN mst_scorptforeignlangestimate
  ON rfla_user = rfle_user
 AND rfla_year = rfle_year
 AND rfla_grade = rfle_grade
 AND rfla_estimate = rfle_rflecode

WHERE cls_user = ?
  AND cls_year = ?
  AND cls_glade = ?
  AND cls_stucode = ?


 -- 記述評価
UNION
SELECT
    rfla_stucode AS cls_stucode
  , rfla_grade   AS cls_glade
  , rfla_term    AS rivt_term
  , rfla_code    AS rivt_rivtcode
  , NULL         AS rivt_rivtname2
  , NULL         AS rivt_purpose
  , 99           AS rivt_order
  , NULL         AS rfla_estimate
  , NULL         AS rfle_reportdisplay
  , rfla_eval

FROM tbl_scorptforeignlangact

WHERE rfla_user = ?
  AND rfla_year = ?
  AND rfla_grade = ?
  AND rfla_term = ?
  AND rfla_code = ''	-- 記述評価はコードが空
  AND rfla_stucode = ?

ORDER BY
     cls_stucode
    ,rivt_order