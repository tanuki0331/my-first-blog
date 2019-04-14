SELECT
	ssv.rsav_stucode,	/* �w�Дԍ� */
	ssv.rsav_term,		/* �o�͎��� */
	cod.cod_name1,		/* ���ʊ������� */
	ssv.rsav_record		/* ���ʊ����̊w�K�̋L�^ */
FROM
	mst_code cod
LEFT JOIN
	tbl_scorptspeactvalue ssv
ON
	cod.cod_user = ssv.rsav_user
AND cod.cod_code = ssv.rsav_rsatcode
AND cod.cod_value1 = ssv.rsav_grade
AND cod.cod_value2 = ssv.rsav_term
AND cod.cod_kind = 692 --�Œ�l
AND ssv.rsav_year = ? /* �N�x */
AND ssv.rsav_stucode = ? /* �w�Дԍ� */
LEFT JOIN
    mst_cmlguideoutputterm
ON  rsav_user = gopt_user
AND rsav_year = gopt_year
AND ssv.rsav_term = gopt_goptcode
AND cod_value3 = gopt_goptcode --�������o�͎����R�[�h�Ɋ֘A����C���̓������̂ݎ擾
WHERE
	cod.cod_user = ? /* �����R�[�h */
AND cod.cod_value1 = ? /* �w�N */
AND cod.cod_value2 = ?  /* �o�͎��� */