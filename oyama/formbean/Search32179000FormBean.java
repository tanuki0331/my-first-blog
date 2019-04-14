package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000StudentEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 検索 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter
@Getter
public class Search32179000FormBean {

	public final static String DEFALUT_VALUE = "";

	/** 年度 */
	private String nendo = DEFALUT_VALUE;

	/** クラス名 */
	private String hmrName = DEFALUT_VALUE;

	/** 出力時期リスト */
	private List<SimpleTagFormBean> outputTermList;

	/** 出力時期 */
	private String outputTerm;

	/** 出力ページ 表紙 */
	private String output_cover;

	/** 出力ページ めあて(すがた) */
	private String output_purpose;

	/** 出力ページ 内容 */
	private String output_contents;

	/** 出力ページ 修了証 */
	private String output_deed;

	/** 出力日付 年 */
	private String output_year;

	/** 出力日付 月 */
	private String output_month;

	/** 出力日付 日 */
	private String output_day;

	/** 修了日付 年 */
	private String deed_year;

	/** 修了日付 月 */
	private String deed_month;

	/** 修了日付 日 */
	private String deed_day;

	/** 月リスト */
	private List<SimpleTagFormBean> monthList;

	/** 日リスト */
	private List<SimpleTagFormBean> dayList;

	/** 生徒一覧 */
	private List<Print32179000StudentEntity> studentList;

	/** 生徒一覧数 */
	private int cntStu = 0;

	/** エラーメッセージ */
	private Map<String, String> errorMessage;

	/** 特別支援学級フラグ */
	private boolean isSpSupportCls = false;

	/** 出力ページ 学習のようす */
	private String output_score;

	/** 出力ページ 活動の記録・所見 */
	private String output_other;
}