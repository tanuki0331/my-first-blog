--�s���̋L�^�̕]���擾
SELECT
	DISTINCT
    cls_stucode AS stucode
   ,ravt_term   AS ravtterm    --�o�͎����R�[�h
   ,gavt_gavtcode AS gavtcode   --�w���v�^ ���ڃR�[�h
   ,ravt_ravtname AS ravtname   --�ϓ_����
   ,ravv_racecode AS racecode  --�]���l�R�[�h
   ,race_reportdisplay AS reportdisplay   --�]���l(�ʒm�\�\���p)
   ,cls_stucode
   ,ravt_term
   ,gavt_order
FROM
    tbl_class
    LEFT JOIN
        mst_scorptactviewpointtitle
    ON  cls_user = ravt_user
    AND ravt_year = cls_year
    AND ravt_grade = cls_glade
    AND ravt_term = ?
    LEFT JOIN
        tbl_scorptactviewpointvalue
    ON  ravt_user = ravv_user
    AND ravt_year = ravv_year
    AND ravt_grade = ravv_grade
    AND ravt_term = ravv_term
    AND ravt_ravtcode = ravv_ravtcode
    AND cls_stucode = ravv_stucode
    LEFT JOIN
        mst_scorptactestimate
    ON  ravv_user = race_user
    AND ravv_year = race_year
    AND ravv_grade = race_grade
    AND ravv_racecode = race_racecode
    LEFT JOIN
        mst_cmlguidesetting
    ON  ravt_user = gst_user
    AND ravt_year = gst_year
    AND ravt_grade = gst_grade
    LEFT JOIN
        mst_cmlguideactviewpointtitle
    ON  ravt_user = gavt_user
    AND gst_curcode = gavt_curcode
    AND ravt_gavtcode = gavt_gavtcode
WHERE
    cls_user = ?
AND cls_year = ?
AND cls_glade = ?
AND cls_stucode = ?
ORDER BY
    cls_stucode
   ,ravt_term
   ,gavt_order