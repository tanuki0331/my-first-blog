package jp.co.systemd.tnavi.cus.oyama.print;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.CrLayer;
import jp.co.hos.coreports.CrLayers;
import jp.co.hos.coreports.CrObject;
import jp.co.hos.coreports.object.CrImageField;
import jp.co.hos.coreports.object.CrListField;
import jp.co.hos.coreports.object.CrText;
import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ActviewEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ForeignLangActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ScoreEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000SpActEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000StudentFormBean;

/**
 * 成績通知表印刷(小山市_小学校) 印刷 出力クラス.
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000 extends AbstractPdfManagerAdditionPagesCR {

	/** log4j */
	private final Log log = LogFactory.getLog(Print32179000.class);

	/** レイアウトオブジェクト */
	/* 表紙 */
	private static final String LAYER_COVER = "Layer_Cover";
	private static final String LAYER_DETAIL_STANDARD = "_Detail_Standard";
	private static final String LAYER_DETAIL_SPGD = "_Detail_Spgd";
	private static final String LAYER_SPGD_1 = "_1"; // 特別支援学級
	private static final String LAYER_SPGD_2 = "_2"; // 交流学級
	private static final String LAYER_SPGD_3 = "_3"; // 交流と特別支援学級

	/* 修了証 */
	private static final String LAYER_DEED = "Layer_Deed";
	private static final String LAYER_SPGD = "_Spgd";
	private static final String LAYER_STANDARD = "_Standard";
	private static final String LAYER_MIKATA = "_Mikata";

	/* 学習の記録 */
	private static final String LAYER_SCORE = "Layer_Score";
	private static final String LAYER_HEADER = "_Header";

	/* 学年別レイヤー用 */
	private static final String LAYER_GRADE_12 = "_12";
	private static final String LAYER_GRADE_34 = "_34";
	private static final String LAYER_GRADE_56 = "_56";

	/** 出力オブジェクト */
	// 表紙
	private static final String ITEM_NENDO_COVER = "Nendo_Cover"; // 年度
	private static final String ITEM_SCHOOL_NAME_COVER = "SchoolName_Cover"; // 学校名
	private static final String ITEM_SCHOOL_STAMP02_COVER = "SchoolStamp02_Cover"; // 通知票表題
	private static final String ITEM_SCHOOL_STAMP04_COVER = "SchoolStamp04_Cover"; // 通知票画像

	private static final String ITEM_STUDENT_CLASS_COVER = "StudentGradeClass_Cover"; // 年・組
	private static final String ITEM_STUDENT_NAME_COVER = "StudentName_Cover"; // 児童名
	private static final String ITEM_PRINCIPAL_NAME_COVER = "PrincipalName_Cover"; // 校長名
	private static final String ITEM_TANNIN_NAME_COVER = "TanninName_Cover"; // 担任名
	private static final String ITEM_SPGD = "_Spgd"; // 特別支援学級
	private static final String ITEM_ALTERNAT = "_Alternat"; // 交流学級
	private static final String ITEM_STANDARD = "_Standard"; // 通常学級
	private static final String ITEM_DISP_NO1 = "_1"; // 年・組、担任名の表示レイアウト
	private static final String ITEM_DISP_NO2 = "_2"; // 年・組、担任名の表示レイアウト

	// 修了証
	private static final String ITEM_STUDENT_NAME_DEED = "StudentName_Deed"; // 児童名
	private static final String ITEM_GRAD_GRADE_DEED = "GradGrade_Deed"; // 学年
	private static final String ITEM_GRAD_DATE_DEED = "GradDate_Deed"; // 修了日
	private static final String ITEM_SCHOOL_NAME_DEED = "SchoolName_Deed"; // 学校名
	private static final String ITEM_PRINCIPAL_NAME_DEED = "PrincipalName_Deed"; // 校長名
	private static final String ITEM_PRINCIPAL_STAMP_DEED = "PrincipalStamp_Deed"; // 校長印

	// 学習の記録
	private static final String ITEM_TERM_SCORE = "Term_Score"; // 出力時期
	private static final String ITEM_STUDENT_NUMBER_SCORE = "StudentNumber_Score"; // 番
	private static final String ITEM_STUDENT_NAME_SCORE = "StudentName_Score"; // 児童名
	private static final String ITEM_TANNIN_NAME_SCORE = "TanninName_Score"; // 担任名

	private static final String ITEM_SCORE_LIST_SCORE = "ScoreList_Score"; // 学習の記録リスト
	private static final String ITEM_SCORE_LINE_SCORE = "ScoreList_Line"; // 学習の記録リスト枠
	private static final Integer ITEM_SCORE_MAX_LINE = 45; // 学習の記録リストの最大行数
	private static final int SCORE_COL_CELL0 = 0; // 教科
	private static final int SCORE_COL_CELL1 = 1; // 学習の目標
	private static final int SCORE_COL_CELL2 = 2; // 評価

	private static final String ITEM_TOTAL_LIST_SCORE = "TotalList_Score"; // 行動の記録リスト
	private static final String ITEM_TOTAL_LINE_SCORE = "TotalList_Line"; // 行動の記録リスト枠
	private static final Integer ITEM_TOTAL_MAX_LINE = 15; // 学習の記録リストの最大行数
	private static final int TOTAL_COL_CELL0 = 0; // 行動の観点
	private static final int TOTAL_COL_CELL1 = 1; // 行動の評価

	private static final String ITEM_ATTEND_LINE_SCORE = "Attend_Score"; // 出欠の記録リスト
	private static final Integer ITEM_ATTEND_MAX_LINE = 7; // 出欠の記録リストの最大行数
	private static final int ATTEND_COL_CELL0 = 0; // 月
	private static final int ATTEND_COL_CELL1 = 1; // 授業日数
	private static final int ATTEND_COL_CELL2 = 2; // 出停忌引
	private static final int ATTEND_COL_CELL3 = 3; // 欠席
	private static final int ATTEND_COL_CELL4 = 4; // 出席日数
	private static final int ATTEND_COL_CELL5 = 5; // 遅刻
	private static final int ATTEND_COL_CELL6 = 6; // 早退

	private static final String ITEM_TOTAL_OPINION = "Total_Opinion"; // 総合所見
	private static final String ITEM_HR_ACTIONTITLE = "HomeRoom_ActionTitle"; // 特別活動のタイトル
	private static final String ITEM_HR_ACTION = "HomeRoom_Action"; // 特別活動の記録
	private static final String ITEM_STUDENT_HEIGHT = "Student_Height"; // 身長
	private static final String ITEM_STUDENT_WEIGHT = "Student_Weight"; // 体重
	private static final String ITEM_TOTAL_STUDY = "Total_Stady"; // 総合的な学習の記録
	private static final String ITEM_FOREIGN_LANG = "Foreign_Lang"; // 外国語活動の記録

	// 時期
	private static final String ITEM_PREVIOUS = "_Previous"; // 前期
	private static final String ITEM_LATE = "_Late"; // 後期

	/** 日付書式設定 */
	private DateFormatUtility dfu = null;

	/** 印刷FormBean */
	private Print32179000FormBean printFormBean;

	/** 動的に幅を変更するリストフィールドの幅、高さの初期値 */
	private Integer scoreListFieldDefaultRow;
	private Integer scoreListFieldDefaultHeight;

	private Integer actListFieldDefaultHeight;
	private Integer actListFieldDefaultRow;

	private Integer attendListFieldDefaultHeight;
	private Integer attendListFieldDefaultRow;

	/** レイヤー表紙詳細種別格納用 */
	private String layerDetail = "";
	/** レイヤー印字種別格納用 */
	private String layerPrint = "";
	/** レイヤー修了証種別格納用 */
	private String layerDeed = "";
	/** レイヤー記録種別格納用 */
	private String layerScore = "";
	/** レイヤー学年格納用 */
	private String layerGrade = "";

	@Override
	protected void doPrint() throws TnaviPrintException {
		try {

			Map<String, Print32179000StudentFormBean> studentMap = printFormBean.getStudentMap();
			Map<String, Print32179000StudentFormBean> deedStudentMap = printFormBean.getDeedStudentMap();

			dfu = new DateFormatUtility(printFormBean.getUserCode());
			dfu.setZeroToSpace(true);

			/**
			 * レイヤー詳細種別
			 */
			if (printFormBean.isSpgdFlg()) {
				// 特別支援学級
				this.layerDetail = LAYER_DETAIL_SPGD;
				this.layerDeed = LAYER_SPGD;

				/**
				 * レイヤー交流種別
				 */
				if (Print32179000FormBean.OUTPUT_ALTERNAT_PRINT.equals(printFormBean.getAlternatFlg())) {
					// 交流学級情報の印字
					this.layerPrint = LAYER_SPGD_2;
				} else if (Print32179000FormBean.OUTPUT_NOMAL_PRINT.equals(printFormBean.getAlternatFlg())) {
					// 通常学級情報の印字
					this.layerPrint = LAYER_SPGD_1;
				} else if (Print32179000FormBean.OUTPUT_BOTH_PRINT.equals(printFormBean.getAlternatFlg())) {
					// 交流学級及び通常学級情報の印字
					this.layerPrint = LAYER_SPGD_3;
				}
			} else {
				// 通常学級
				this.layerDetail = LAYER_DETAIL_STANDARD;
				this.layerDeed = LAYER_STANDARD;
				// 交流種別なし
				this.layerPrint = "";
			}

			for (String entrySet : printFormBean.getStudentList()) {

				// 生徒情報
				Print32179000StudentFormBean student = studentMap.get(entrySet);

				// 表紙
				if (printFormBean.isOutput_cover()) {
					printOutputCover(student);
				}

				// 学習の記録
				if (printFormBean.isOutput_score()) {
					printOutputScore(student);
				}

				// 修了証出力
				if (printFormBean.isOutput_deed()) {
					outputDeed(deedStudentMap.get(entrySet));
				}
			}

		} catch (Exception e) {
			log.error("例外発生　帳票作成失敗", e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * 印刷FormBeanを設定する.
	 * 
	 * @param printFormBean the printFormBean to set
	 */
	public void setPrintFormBean(Print32179000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

	/**
	 * 表紙の出力を行う.
	 * 
	 * @param student 生徒情報
	 */
	private void printOutputCover(Print32179000StudentFormBean student) throws Exception {

		// 全レイヤー非表示
		setVisibleAtPrintForAllLayout(false);
		// 表紙レイヤー表示
		getCrLayerSetVisibleAtPrint(LAYER_COVER, true);
		getCrLayerSetVisibleAtPrint(LAYER_COVER + layerDetail, true);
		getCrLayerSetVisibleAtPrint(LAYER_COVER + layerDetail + this.layerPrint, true);

		// 年度
		String nendo = dfu.formatDate("YYYY", 1, printFormBean.getNendo());
		StringBuffer outputNendo = new StringBuffer(nendo).insert(2, " ").append(" 年度"); // 元号と年度の間、年度の後ろに半角スペースを挿入して配置を調整
		getExistsFieldSetData(ITEM_NENDO_COVER, outputNendo.toString());

		// 通知票表題画像
		if (objectExists(ITEM_SCHOOL_STAMP02_COVER)) {
			if (printFormBean.getStampImage() != null) {
				setImageFieldDataCenterling(ITEM_SCHOOL_STAMP02_COVER, printFormBean.getStampImage());
			}
		}

		// 表紙画像
		if (objectExists(ITEM_SCHOOL_STAMP04_COVER)) {
			if (printFormBean.getCoverImage() != null) {
				setImageFieldDataCenterling(ITEM_SCHOOL_STAMP04_COVER, printFormBean.getCoverImage());
			}
		}

		// 学校名
		getExistsFieldSetData(ITEM_SCHOOL_NAME_COVER, printFormBean.getSchoolName());

		// 児童氏名
		getExistsFieldSetData(ITEM_STUDENT_NAME_COVER + this.layerDeed, getOutputStuName(student));

		// 校長名
		getExistsFieldSetData(ITEM_PRINCIPAL_NAME_COVER + this.layerDeed,
				getStaffNames(printFormBean.getPrincipalList()));

		// 年・組
		String stuGrade = "第 " + student.getStuGrade() + " 学年 ";
		String stuClass = student.getStuClass() + " 組";

		// 学級、担任氏名
		if (printFormBean.isSpgdFlg()) {

			stuGrade = "第 " + student.getStuGrade() + " 学年 ";
			stuClass = student.getExcClass() + " 組";

			/** レイヤー交流種別 */
			if (Print32179000FormBean.OUTPUT_ALTERNAT_PRINT.equals(printFormBean.getAlternatFlg())) {
				// 交流学級情報の印字

				// 学年・学級
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_ALTERNAT + ITEM_DISP_NO1, stuGrade + stuClass);
				// 担任名
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_ALTERNAT + ITEM_DISP_NO1,
						getStaffNames(student.getStaffList()));
			} else if (Print32179000FormBean.OUTPUT_NOMAL_PRINT.equals(printFormBean.getAlternatFlg())) {
				// 通常学級情報の印字
				// 学年・学級
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_SPGD + ITEM_DISP_NO1, student.getSpGroupname());
				// 担任名
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_SPGD + ITEM_DISP_NO1,
						getStaffNames(student.getSpStaffList()));
			} else if (Print32179000FormBean.OUTPUT_BOTH_PRINT.equals(printFormBean.getAlternatFlg())) {
				// 交流学級及び通常学級情報の印字
				// 学年・学級
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_SPGD + ITEM_DISP_NO2, student.getSpGroupname());
				// 担任名
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_SPGD + ITEM_DISP_NO2,
						getStaffNames(student.getSpStaffList()));
				// 担任名
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_ALTERNAT + ITEM_DISP_NO2,
						getStaffNames(student.getStaffList()));
				// 学年・学級
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_ALTERNAT + ITEM_DISP_NO2, stuGrade + stuClass);
			}
		} else {
			// 通常学級情報の印字
			// 学年・学級
			getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_STANDARD, stuGrade + stuClass);
			// 担任名
			getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_STANDARD, getStaffNames(student.getStaffList()));
		}

		// 出力
		form.printOut();
		// フォーム初期化
		form.initialize();
	}

	/**
	 * 修了証の出力を行う.
	 * 
	 * @param student 生徒情報
	 * @throws Exception 出力時例外
	 */
	private void outputDeed(Print32179000StudentFormBean student) throws Exception {

		// 全レイヤー非表示
		setVisibleAtPrintForAllLayout(false);
		// 修了証レイヤー表示
		getCrLayerSetVisibleAtPrint(LAYER_DEED + this.layerDeed, true);
		// 通常学級の場合
		if (!printFormBean.isSpgdFlg()) {
			// 修了証レイヤー表示
			getCrLayerSetVisibleAtPrint(LAYER_DEED + LAYER_MIKATA + "_" + printFormBean.getUserCode(), true);
		}

		// 児童名
		getExistsFieldSetData(ITEM_STUDENT_NAME_DEED + this.layerDeed, getOutputStuName(student));

		// 修了日
		getExistsFieldSetData(ITEM_GRAD_DATE_DEED + this.layerDeed,
				dfu.formatDate("YYYY年MM月DD日", 1, printFormBean.getDeedDate()));

		// 学年
		getExistsFieldSetData(ITEM_GRAD_GRADE_DEED + this.layerDeed, student.getStuGrade());

		// 校長名
		getExistsFieldSetData(ITEM_PRINCIPAL_NAME_DEED + this.layerDeed, printFormBean.getDeedPrincipalName());

		// 校長印(後期のみ表示)
		if (printFormBean.getTerm().equals("02") && objectExists(ITEM_PRINCIPAL_STAMP_DEED + this.layerDeed)) {
			CrImageField imageField = (CrImageField) form.getCrObject(ITEM_PRINCIPAL_STAMP_DEED + this.layerDeed);
			if (printFormBean.getPrincipalStampImage() != null) {
				imageField.setData(printFormBean.getPrincipalStampImage());
			}
		}

		// 学校名
		StringBuffer buffer = new StringBuffer();
		buffer.append(printFormBean.getSchoolName());
		buffer.append("長");
		getExistsFieldSetData(ITEM_SCHOOL_NAME_DEED + this.layerDeed, buffer.toString());

		// 出力
		form.printOut();
		// フォーム初期化
		form.initialize();
	}

	/**
	 * 学習の記録の出力を行う.
	 * 
	 * @param student 生徒情報
	 * @throws 出力時例外
	 */
	private void printOutputScore(Print32179000StudentFormBean student) throws Exception {

		// 出力時期
		if (Print32179000FormBean.OUTPUT_TERM_PREVIOUS.equals(printFormBean.getTerm())) {
			this.layerScore = ITEM_PREVIOUS;
		} else {
			this.layerScore = ITEM_LATE;
		}
		// 出力学年
		if (Print32179000FormBean.OUTPUT_GRADE_01.equals(student.getGrade())
				|| Print32179000FormBean.OUTPUT_GRADE_02.equals(student.getGrade())) {
			this.layerGrade = LAYER_GRADE_12;
		} else if (Print32179000FormBean.OUTPUT_GRADE_03.equals(student.getGrade())
				|| Print32179000FormBean.OUTPUT_GRADE_04.equals(student.getGrade())) {
			this.layerGrade = LAYER_GRADE_34;
		} else {
			this.layerGrade = LAYER_GRADE_56;
		}
		// 全レイヤー非表示
		setVisibleAtPrintForAllLayout(false);
		// 学習の記録レイヤー表示
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + LAYER_HEADER, true);
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + this.layerGrade, true);
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + this.layerScore, true);

		// 出力時期
		getExistsFieldSetData(ITEM_TERM_SCORE, printFormBean.getTermName());
		// 学年
		if ("1".equals(student.getIsKoryu())) {
			getExistsFieldSetData(ITEM_STUDENT_NUMBER_SCORE, student.getExcNumber());
		} else {
			getExistsFieldSetData(ITEM_STUDENT_NUMBER_SCORE, student.getStuNumber());
		}
		// 児童名
		getExistsFieldSetData(ITEM_STUDENT_NAME_SCORE, getOutputStuName(student));

		// 担任名
		getExistsFieldSetData(ITEM_TANNIN_NAME_SCORE, getStaffNamesAlline(student.getStaffList()));

		// 教科の学習の記録
		printScoreList(student);

		// 行動の記録
		printActiveList(student);

		// 出欠の記録
		printAttendList(student);

		// 特別活動の記録
		StringBuffer spcialAction = new StringBuffer(); // 記録
		StringBuffer spcialTitle = new StringBuffer(); // 特別活動名称
		for (Print32179000SpActEntity entity : student.getSpActList()) {
			// 空でなければ"/"追加
			if (spcialTitle != null && !spcialTitle.toString().equals("")) {
				spcialTitle.append("/");
			}
			if (spcialAction != null && !spcialAction.toString().equals("")) {
				spcialAction.append("/");
			}
			// タイトル格納
			if (entity.getCod_name1() != null) {
				spcialTitle.append(entity.getCod_name1());
			} else {
				spcialTitle.append("");
			}
			if (entity.getRsav_record() != null) {
				spcialAction.append(entity.getRsav_record());
			} else {
				spcialAction.append("");
			}
		}
		// テンプレートにセット
		if (spcialTitle != null && !spcialTitle.toString().equals("")) {
			getExistsFieldSetData(ITEM_HR_ACTIONTITLE + this.layerGrade, spcialTitle.toString());
		}
		if (spcialAction != null && !spcialAction.toString().equals("")) {
			getExistsFieldSetData(ITEM_HR_ACTION + this.layerGrade, spcialAction.toString());
		}

		// 総合所見
		if (student.getComment() != null && student.getComment().getRcom_comment() != null) {
			getExistsFieldSetData(ITEM_TOTAL_OPINION + this.layerGrade, student.getComment().getRcom_comment());
		}

		// 発育の記録
		if (student.getHealthrecord() != null) {
			// 身長
			if (student.getHealthrecord().getHlr_height() != null) {
				getExistsFieldSetData(ITEM_STUDENT_HEIGHT + this.layerGrade, student.getHealthrecord().getHlr_height());
			} else {
				getExistsFieldSetData(ITEM_STUDENT_HEIGHT + this.layerGrade, "");
			}

			// 体重
			if (student.getHealthrecord().getHlr_weight() != null) {
				getExistsFieldSetData(ITEM_STUDENT_WEIGHT + this.layerGrade, student.getHealthrecord().getHlr_weight());
			} else {
				getExistsFieldSetData(ITEM_STUDENT_WEIGHT + this.layerGrade, "");
			}
		}

		// 総合的な活動の記録
		if (student.getTotalAct() != null && student.getTotalAct().getRtav_value() != null) {
			getExistsFieldSetData(ITEM_TOTAL_STUDY + this.layerGrade, student.getTotalAct().getRtav_value());
		}

		// 外国語活動の記録
		StringBuffer foreignLang = new StringBuffer(); // 特別活動名称
		for (Print32179000ForeignLangActEntity entity : student.getForeignLangActList()) {
			// 空でなければ"/"追加
			if (foreignLang != null && !foreignLang.toString().equals("")) {
				foreignLang.append("/");
			}
			// タイトル格納
			if (entity.getRfla_eval() != null) {
				foreignLang.append(entity.getRfla_eval());
			} else {
				foreignLang.append("");
			}
		}
		// テンプレートにセット
		if (spcialTitle != null && !spcialTitle.toString().equals("")) {
			getExistsFieldSetData(ITEM_FOREIGN_LANG + this.layerGrade, foreignLang.toString());
		}

		// 出力
		form.printOut();
		// フォーム初期化
		form.initialize();
	}

	/**
	 * オブジェクトの存在チェック後に値をセットする.
	 * 
	 * @param objectName オブジェクト名
	 * @param value      出力する値
	 * @throws Exception
	 */
	private void getExistsFieldSetData(String objectName, Object value) throws Exception {
		if (objectExists(objectName)) {
			getFieldSetData(objectName, value);
		}
	}

	/**
	 * 全レイヤを表示・非表示にする.
	 * 
	 * @param flag 表示・非表示フラグ
	 */
	private void setVisibleAtPrintForAllLayout(boolean flag) {
		CrLayers crLayers = form.getCrLayers();
		for (Iterator<CrLayer> i = crLayers.iterator(); i.hasNext();) {
			CrLayer layer = i.next();
			layer.setVisibleAtPrint(flag);
		}
	}

	/**
	 * 生徒氏名の戸籍・通称判定
	 * 
	 * @param student
	 * @return
	 */
	private String getOutputStuName(Print32179000StudentFormBean student) {
		return student.getStuName();
	}

	/**
	 * 教員のリストを古い順に並べた文字列を取得する
	 * 
	 * @param staffList 教員リスト
	 * @return 変換後教員名
	 */
	private String getStaffNames(List<StaffEntity> staffList) {
		StringBuffer sb = new StringBuffer();
		// 新しい順に並んでいるため、逆から
		if (staffList != null) {
			for (int i = staffList.size() - 1; i >= 0; i--) {
				if (sb.length() > 0) {
					sb.append("\r\n");
				}
				// 氏名は漢字を使用
				sb.append(staffList.get(i).getStf_name_w());
			}
		}

		return sb.toString();
	}

	/**
	 * 教員のリストを古い順に並べた文字列を取得する
	 * 
	 * @param staffList 教員リスト
	 * @return 変換後教員名
	 */
	private String getStaffNamesAlline(List<StaffEntity> staffList) {
		StringBuffer sb = new StringBuffer();
		// 新しい順に並んでいるため、逆から
		for (int i = staffList.size() - 1; i >= 0; i--) {
			if (sb.length() > 0) {
				sb.append(" ・ ");
			}
			// 氏名は漢字を使用
			sb.append(staffList.get(i).getStf_name_w());
		}

		return sb.toString();
	}

	/**
	 * 縦書きのセル用に、1文字毎に改行を挿入して体裁を整える
	 * 
	 * @param itemName 整形対象の文字列
	 * @param rowSize  出力行サイズ
	 * @return 整形後の文字列
	 */
	private String arrangeVertical(String itemName, int rowSize) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < itemName.length(); i++) {
			sb.append(itemName.charAt(i));
			if (i < itemName.length() - 1) {
				// 1文字ごとに改行コードを挿入する
				sb.append("\r\n");
				if (itemName.length() == 2) {
					for (int j = 0; j < (rowSize - 3); j++) {
						sb.append("\r\n");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 教科の学習の記録
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printScoreList(Print32179000StudentFormBean student) throws Exception {

		// 教科、観点の出力
		CrListField scoreGridField = getCrListField(ITEM_SCORE_LIST_SCORE + this.layerGrade);
		CrText[][] scoreGrid = getListFieldGetColumns(scoreGridField);
		// --罫線グリッド
		CrListField lineGridField = getCrListField(ITEM_SCORE_LINE_SCORE + this.layerGrade);

		// 行のカウント
		int rowCount = 0;
		// 行数取得
		rowCount = student.getScoreList().size();

		if (rowCount > ITEM_SCORE_MAX_LINE)
			rowCount = ITEM_SCORE_MAX_LINE;

		// レイアウトの行数および高さ保持
		if (scoreListFieldDefaultRow == null) {
			scoreListFieldDefaultRow = scoreGrid[0].length;
			scoreListFieldDefaultHeight = scoreGridField.getHeight();
		} else {

			// 複数人処理する場合は初期化が必要
			scoreGridField.setHeight(scoreListFieldDefaultHeight);
			scoreGridField.setVisibleRows(scoreListFieldDefaultRow);

			lineGridField.setHeight(scoreListFieldDefaultHeight);
			lineGridField.setVisibleRows(scoreListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// レイアウト行数と高さから、実際の出力行数にあった高さを算出(非表示行考慮した高さ)
		BigDecimal bdMaxrow = new BigDecimal(scoreListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(scoreListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// レイアウト行数を実際の出力行で割ると必要な倍率が出るので、レイアウト行数と掛け合わせて設定する高さを算出
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// 複数人処理する場合は初期化が必要
		scoreGridField.setHeight(culcHeight);
		lineGridField.setHeight(culcHeight);
		// 表示行数セット
		scoreGridField.setVisibleRows(rowCount);
		lineGridField.setVisibleRows(rowCount);

		// 教科ごとのスタート行を保持する。セルマージ用
		int itemStartRow = 0;

		Map<String, Integer> itemRows = new HashMap<String, Integer>();
		int itemRowCount = 0;
		int i = 0;
		for (Print32179000ScoreEntity bean : student.getScoreList()) {
			if (itemRowCount != 0 && !oldItem.equals(bean.getItemCode())) {
				if (!oldItem.isEmpty()) {
					itemRows.put(oldItem, itemRowCount);
				}
				itemRowCount = 0;
			}
			itemRowCount++;
			oldItem = bean.getItemCode();
			i++;
			if (i == student.getScoreList().size()) {
				itemRows.put(bean.getItemCode(), itemRowCount);
			}
		}
		oldItem = "";

		// 観点リスト
		for (Print32179000ScoreEntity bean : student.getScoreList()) {
			// 観点出力
			if (bean != null) {
				// 最大行数は11
				if (rowIndex > ITEM_SCORE_MAX_LINE - 1) {
					break;
				}

				int viewPointSize = 0;
				// 観点出力
				if (!oldItem.equals(bean.getItemCode())) {

					viewPointSize = itemRows.get(bean.getItemCode()) - 1;// 教科の観点数-1取得

					// 教科名を出力
					if (rowIndex + viewPointSize > ITEM_SCORE_MAX_LINE - 1) {
						setListFieldData(scoreGrid, SCORE_COL_CELL0, rowIndex,
								arrangeVertical(bean.getItemName(), ITEM_SCORE_MAX_LINE - 1 - (rowIndex + 1)));
					} else {
						setListFieldData(scoreGrid, SCORE_COL_CELL0, rowIndex,
								arrangeVertical(bean.getItemName(), viewPointSize + 1));
					}
				}
				// データをセットする。
				// 観点
				setListFieldData(scoreGrid, SCORE_COL_CELL1, rowIndex, bean.getPurpose());
				// 評価
				if (bean.getViewpointvalue() == null || bean.getViewpointvalue().isEmpty()) {
					setListFieldData(scoreGrid, SCORE_COL_CELL2, rowIndex, "");
				} else if (bean.getViewpointvalue().equals("*")) {
					CrObject crObject = scoreGrid[SCORE_COL_CELL2][rowIndex];
					printSlash(crObject, LINE_SLASH, 1, form.getCrLayer(LAYER_SCORE + this.layerGrade));
				} else if ("1".equals(bean.getRvpe_cannot_flg()) || "1".equals(bean.getRvpe_none_flg())) {
					CrObject crObject = scoreGrid[SCORE_COL_CELL2][rowIndex];
					printSlash(crObject, LINE_SLASH, 1, form.getCrLayer(LAYER_SCORE + this.layerGrade));
				} else {
					setListFieldData(scoreGrid, SCORE_COL_CELL2, rowIndex, bean.getViewpointvalue());
				}

				int itemEndRow = 0;
				itemEndRow = itemStartRow + viewPointSize;

				if (!oldItem.equals(bean.getItemCode())) {
					// 太線専用リストに対してセルのマージ処理をする。
					// 教科
					listFieldMergeCell(scoreGridField, SCORE_COL_CELL0, itemStartRow, SCORE_COL_CELL0, itemEndRow);

					// 太線専用リストに対しての処理
					// 教科毎
					listFieldMergeCell(lineGridField, SCORE_COL_CELL0, itemStartRow, SCORE_COL_CELL0, itemEndRow);
					listFieldMergeCell(lineGridField, SCORE_COL_CELL1, itemStartRow, SCORE_COL_CELL1, itemEndRow);
					listFieldMergeCell(lineGridField, SCORE_COL_CELL2, itemStartRow, SCORE_COL_CELL2, itemEndRow);
				}

				// 現在の教科を取得して、直前の強化とする
				oldItem = bean.getItemCode();
				rowIndex++;
				itemStartRow++;
			}
		}
	}

	/**
	 * 出欠の記録
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printAttendList(Print32179000StudentFormBean student) throws Exception {

		// 行動の記録の出力
		CrListField activeGridField = getCrListField(ITEM_ATTEND_LINE_SCORE + this.layerGrade);
		CrText[][] activeGrid = getListFieldGetColumns(activeGridField);

		// 行のカウント
		int rowCount = 0;

		if ("01".equals(printFormBean.getTerm())) {
			// 行数取得
			rowCount = 6;
		} else {
			rowCount = 7;
		}

		if (rowCount > ITEM_ATTEND_MAX_LINE)
			rowCount = ITEM_ATTEND_MAX_LINE;

		// レイアウトの行数および高さ保持
		if (attendListFieldDefaultRow == null) {
			attendListFieldDefaultRow = activeGrid[0].length;
			attendListFieldDefaultHeight = activeGridField.getHeight();
		} else {

			// 複数人処理する場合は初期化が必要
			activeGridField.setHeight(attendListFieldDefaultHeight);
			activeGridField.setVisibleRows(attendListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// レイアウト行数と高さから、実際の出力行数にあった高さを算出(非表示行考慮した高さ)
		BigDecimal bdMaxrow = new BigDecimal(attendListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(attendListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// レイアウト行数を実際の出力行で割ると必要な倍率が出るので、レイアウト行数と掛け合わせて設定する高さを算出
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// 複数人処理する場合は初期化が必要
		activeGridField.setHeight(culcHeight);
		// 表示行数セット
		activeGridField.setVisibleRows(rowCount);

		// 教科ごとのスタート行を保持する。セルマージ用
		int itemStartRow = 0;

		// 観点リスト
		for (Print32179000AttendEntity bean : student.getAttendList()) {
			// 観点出力
			if (bean != null) {

				if (printFormBean.getTerm().equals(bean.getGoptcode())) {

					// 最大行数は11
					if (rowIndex > ITEM_ATTEND_MAX_LINE - 1) {
						break;
					}

					int viewPointSize = 0;
					// データをセットする。
					// 月
					if (!bean.getMonth().equals("99")) {
						if (bean.getMonth().substring(0, 1).equals("0")) {
							setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex,
									(bean.getMonth().replace("0", "") + "月"));
						} else {
							setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex, (bean.getMonth() + "月"));
						}
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex, "年間計");
					}
					// 授業日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL1, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL1, rowIndex, bean.getTeachingSum());
					}
					// 出停忌引日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL2, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL2, rowIndex, bean.getStopSum());
					}
					// 欠席日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL3, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL3, rowIndex, bean.getAbsenceSum());
					}
					// 出席日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL4, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL4, rowIndex, bean.getAttendSum());
					}
					// 遅刻日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL5, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL5, rowIndex, bean.getLateSum());
					}
					// 早退日数
					if (bean.getOnRegist().equals("0")) {// 在籍していない生徒はハイフンを印字
						setListFieldData(activeGrid, ATTEND_COL_CELL6, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL6, rowIndex, bean.getLeaveSum());
					}

					int itemEndRow = 0;
					itemEndRow = itemStartRow + viewPointSize;

					if (!oldItem.equals(bean.getMonth())) {
						// 太線専用リストに対してセルのマージ処理をする。
						// 教科
						listFieldMergeCell(activeGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);
					}

					// 現在の教科を取得して、直前の強化とする
					oldItem = bean.getMonth();
					rowIndex++;
					itemStartRow++;
				}
			}
		}
	}

	/**
	 * 行動の記録
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printActiveList(Print32179000StudentFormBean student) throws Exception {
		// 行動の記録の出力
		CrListField activeGridField = getCrListField(ITEM_TOTAL_LIST_SCORE + this.layerGrade);
		CrText[][] activeGrid = getListFieldGetColumns(activeGridField);
		// --罫線グリッド
		CrListField lineGridField = getCrListField(ITEM_TOTAL_LINE_SCORE + this.layerGrade);

		// 行のカウント
		int rowCount = 0;
		// 行数取得
		rowCount = student.getActviewList().size();

		if (rowCount > ITEM_TOTAL_MAX_LINE)
			rowCount = ITEM_TOTAL_MAX_LINE;

		if (rowCount == 0)
			rowCount = ITEM_TOTAL_MAX_LINE;

		// レイアウトの行数および高さ保持
		if (actListFieldDefaultRow == null) {
			actListFieldDefaultRow = activeGrid[0].length;
			actListFieldDefaultHeight = activeGridField.getHeight();
		} else {

			// 複数人処理する場合は初期化が必要
			activeGridField.setHeight(actListFieldDefaultHeight);
			activeGridField.setVisibleRows(actListFieldDefaultRow);

			lineGridField.setHeight(actListFieldDefaultHeight);
			lineGridField.setVisibleRows(actListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// レイアウト行数と高さから、実際の出力行数にあった高さを算出(非表示行考慮した高さ)
		BigDecimal bdMaxrow = new BigDecimal(actListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(actListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// レイアウト行数を実際の出力行で割ると必要な倍率が出るので、レイアウト行数と掛け合わせて設定する高さを算出
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// 複数人処理する場合は初期化が必要
		activeGridField.setHeight(culcHeight);
		lineGridField.setHeight(culcHeight);
		// 表示行数セット
		activeGridField.setVisibleRows(rowCount);
		lineGridField.setVisibleRows(rowCount);

		// 教科ごとのスタート行を保持する。セルマージ用
		int itemStartRow = 0;

		// 観点リスト
		for (Print32179000ActviewEntity bean : student.getActviewList()) {
			// 観点出力
			if (bean != null) {
				// 最大行数は11
				if (rowIndex > ITEM_SCORE_MAX_LINE - 1) {
					break;
				}

				int viewPointSize = 0;
				// データをセットする。
				// 観点
				setListFieldData(activeGrid, TOTAL_COL_CELL0, rowIndex, bean.getRavtname());
				// 評価
				setListFieldData(activeGrid, TOTAL_COL_CELL1, rowIndex, bean.getReportdisplay());

				int itemEndRow = 0;
				itemEndRow = itemStartRow + viewPointSize;

				if (!oldItem.equals(bean.getGavtcode())) {
					// 太線専用リストに対してセルのマージ処理をする。
					// 教科
					listFieldMergeCell(activeGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);

					// 太線専用リストに対しての処理
					// 教科毎
					listFieldMergeCell(lineGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);
					listFieldMergeCell(lineGridField, TOTAL_COL_CELL1, itemStartRow, TOTAL_COL_CELL1, itemEndRow);
				}

				// 現在の教科を取得して、直前の強化とする
				oldItem = bean.getGavtcode();
				rowIndex++;
				itemStartRow++;
			}
		}
	}
}
