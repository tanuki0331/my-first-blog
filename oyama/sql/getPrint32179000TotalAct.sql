SELECT
  rtav_stucode,  /* 学籍番号 */
  ISNULL(rtav_value, '') AS rtav_value,    /* 観点 */
  ISNULL(rtav_contents, '') AS rtav_contents  /* 学習活動 */
FROM tbl_scorpttotalactvalue
WHERE
  rtav_user  = ? AND				/* 所属コード */
  rtav_year  = ? AND				/* 処理年度 */
  rtav_grade = ? AND				/* 処理中のクラスの学年 */
  rtav_term  = ? AND				/* 出力時期コード */
  rtav_rtavcode = '0001' AND 			/* 0001固定 */
  rtav_stucode = ? 				/* 出力対象児童の学籍番号 */
