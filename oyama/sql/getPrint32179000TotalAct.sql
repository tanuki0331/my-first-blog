SELECT
  rtav_stucode,  /* �w�Дԍ� */
  ISNULL(rtav_value, '') AS rtav_value,    /* �ϓ_ */
  ISNULL(rtav_contents, '') AS rtav_contents  /* �w�K���� */
FROM tbl_scorpttotalactvalue
WHERE
  rtav_user  = ? AND				/* �����R�[�h */
  rtav_year  = ? AND				/* �����N�x */
  rtav_grade = ? AND				/* �������̃N���X�̊w�N */
  rtav_term  = ? AND				/* �o�͎����R�[�h */
  rtav_rtavcode = '0001' AND 			/* 0001�Œ� */
  rtav_stucode = ? 				/* �o�͑Ώێ����̊w�Дԍ� */
