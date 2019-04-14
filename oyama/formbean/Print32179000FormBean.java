package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * 成績通知票印刷(小山市_小学校) 印刷 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter
@Getter
public class Print32179000FormBean {

	public final static String DEFALUT_VALUE = "";

	/** 時期種別 */
	public static final String OUTPUT_TERM_1		= "01";	// 1学期
	public static final String OUTPUT_TERM_2		= "02";	// 2学期
	public static final String OUTPUT_TERM_3		= "03";	// 3学期
	public static final String OUTPUT_TERM_GRADE	= "99";	// 学年末

	/** 学年種別 */
	public final static Integer OUTPUT_GRADE_01 = 1; // 1年
	public final static Integer OUTPUT_GRADE_02 = 2; // 2年
	public final static Integer OUTPUT_GRADE_03 = 3; // 3年
	public final static Integer OUTPUT_GRADE_04 = 4; // 4年
	public final static Integer OUTPUT_GRADE_05 = 5; // 5年
	public final static Integer OUTPUT_GRADE_06 = 6; // 6年

	/** 所属コード */
	private String userCode = DEFALUT_VALUE;

	/** 年度 */
	private String nendo = DEFALUT_VALUE;

	/** 特別支援学級フラグ false:通常学級、true:特別支援学級 */
	private boolean spgdFlg;

	// ---------------------------------------------------------
	// 帳票出力条件(リクエストパラーメータ)
	// ---------------------------------------------------------
	/** 選択出力時期 */
	private String term = DEFALUT_VALUE;

	/** 選択出力時期名 */
	private String termName = DEFALUT_VALUE;

	/** 出力ページ 表紙 */
	private boolean output_cover;

	/** 出力ページ めあて(すがた) */
	private boolean output_purpose;

	/** 出力ページ 内容 */
	private boolean output_contents;

	/** 出力ページ 修了証 */
	private boolean output_deed;

	/** 出力日付 */
	private String outputDate = DEFALUT_VALUE;

	/** 修了日付 */
	private String deedDate = DEFALUT_VALUE;

	// ---------------------------------------------------------
	// 帳票印字データ
	// ---------------------------------------------------------
	/** 表紙 表題イメージ */
	private byte[] stampImage;

	/** 表紙 校章イメージ */
	byte[] schoolStampImage;

	/** 表紙 学校教育目標イメージ */
	byte[] educationalGoalImage;









	/** 時期種別 */
	public final static String OUTPUT_TERM_PREVIOUS = "01"; // 前期
	public final static String OUTPUT_TERM_LATE = "02"; // 後期

	/** 組・出席番号の出力モード */
	public final static String OUTPUT_HROOMINFO_NORMAL = "0"; // 0:在籍学級の情報で印字, 1:交流先学級の情報で印字, 2:在籍学級及び交流先学級の情報で印字
	public final static String OUTPUT_HROOMINFO_GROUP = "1"; // 在籍学級が所属する特別支援学級グループ名称と、在籍学級の出席番号を出力する(特支学級処理時のみ選択可能)
	public final static String OUTPUT_HROOMINFO_KOURYU = "2"; // 交流先の組・出席番号を出力する(特支学級処理時のみ選択可能)

	/** 出力する出席番号の種別 */
	public final static String OUTPUT_NUMBER_KOUBO = "0"; // cls_reference_numberを出力
	public final static String OUTPUT_NUMBER_NORMAL = "1"; // cls_numberを出力

	/** 組・出席番号の出力モード */
	private String outputHroomInfoMode = OUTPUT_HROOMINFO_NORMAL;

	/** 出席番号の出力種別 */
	private String outputNumberKind = OUTPUT_NUMBER_NORMAL;

	/** 生徒毎のデータ */
	private Map<String, Print32179000StudentFormBean> studentMap;

	/** 修了証生徒毎のデータ */
	private Map<String, Print32179000StudentFormBean> deedStudentMap;

	/** 学籍番号リスト(降順） */
	private List<String> studentList;

	/** 校長リスト(降順） */
	private List<StaffEntity> principalList;

	/** 身体のようす 月 */
	private String healthrecordDate;

	/** 表紙 校章イメージ */
	private byte[] coverImage;

	/** 校長印イメージ */
	byte[] principalStampImage;

	private Map<String, Boolean> notHyokaMap;

	private Map<String, Boolean> notHyoteiMap;

	/** 情報印字種別 */
	public final static String OUTPUT_NOMAL_PRINT = "0"; // 0:在籍学級の情報で印字
	public final static String OUTPUT_ALTERNAT_PRINT = "1"; // 1:交流先学級の情報で印字
	public final static String OUTPUT_BOTH_PRINT = "2"; // 2:在籍学級及び交流先学級の情報で印字

	/** 公式名称 */
	private String schoolName = DEFALUT_VALUE;

	/** 修了証に出力する校長名 */
	private String deedPrincipalName;

	/** 出力ページ学習の記録 */
	private boolean output_score;

	/** 交流学級フラグ 0:在籍学級の情報で印字, 1:交流先学級の情報で印字, 2:在籍学級及び交流先学級の情報で印字 */
	private String alternatFlg = "0";
}
