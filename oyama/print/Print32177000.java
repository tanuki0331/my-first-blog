/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.print;

import java.awt.Color;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.CrLayer;
import jp.co.hos.coreports.constants.CorLineStyle;
import jp.co.hos.coreports.constants.CorUnit;
import jp.co.hos.coreports.object.CrLine;
import jp.co.hos.coreports.object.CrListField;
import jp.co.hos.coreports.object.CrText;
import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.att.formbean.AttPrintStudentFormBean;
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32177000EnforceEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32177000FormBean;


/**
 * 出席簿印刷(特別支援学級　小山市用) 出力クラス.
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32177000 extends AbstractPdfManagerAdditionPagesCR {

	/**log4j*/
	private final Log log = LogFactory.getLog(Print32177000.class);

	/** システム情報Bean */
	private SystemInfoBean sessionBean;

	/** レイヤオブジェクト */
	private static final String LAYER_ATTENDANCE = "Layer_Attendance";
	private static final String LAYER_SUMMARY = "Layer_Summary";
	private static final String LAYER_FOOTER = "Layer_Footer";
	private static final String LAYER_FOOTER_CHU = "Layer_Footer_chu";

	/** レイアウトオブジェクト */
	private static final String HEADER_YEAR_MONTH = "HeaderYearMonth";
	private static final String HEADER_TANNIN_NAME = "HeaderTanninName";			// ヘッダ　学級担任氏名
	private static final String HEADER_GRADE_CLASS = "HeaderGradeClass";			// ヘッダ　学年組

	private static final String DAY_WEEK_LIST = "DayWeekList";						// 日・曜日リストフィールド
	private static final String STUDENT_LIST = "StudentList";						// 生徒氏名・番リストフィールド
	private static final String ATTEND_LIST = "AttendList";							// 出欠欄リストフィールド
	private static final String ATTEND_SUM_LIST = "AttendSumList";					// 出欠集計リストフィールド

	private  String TOTAL_ATTEND = "TotalAttend";									// 月末統計リストフィールド
	private  String ENFORCE_DAY_SUM_LIST = "EnforceDaySumList";						// 月末統計・授業時数


	/** 出席時に置き換える文字列 */
	private static final String ATTEND_KIND_00_DISP = "　";

	/** 出欠欄リストフィールド */
	private CrText [][] attendListFielddArray;

	/** 印刷FormBean */
	private Print32177000FormBean printFormBean;

	private int[][] enforceState = null ; //印刷領域での授業日設定チェック用

	protected DateFormatUtility dateFormatUtility = null;


	/**
	 * コンストラクタ
	 * @param sessionBean システム情報Bean
	 * @param printFormBean 印刷FormBean
	 */
	public Print32177000(SystemInfoBean sessionBean, Print32177000FormBean printFormBean) {
		super();
		this.sessionBean = sessionBean;
		this.printFormBean = printFormBean;
	}

	@Override
	protected void doPrint() throws TnaviPrintException {

		try {

			//---------------------------------------
			// 出席簿ページ
			//---------------------------------------
			form.initialize();//フォーム初期化

			getCrLayerSetVisibleAtPrint(LAYER_ATTENDANCE, true);
			getCrLayerSetVisibleAtPrint(LAYER_SUMMARY   , false);

			// 印字するレイヤを可視にする
			if(sessionBean.getUseKind2().equals("1")) {
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER, true);
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER_CHU, false);

			}else {
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER, false);
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER_CHU, true);
				TOTAL_ATTEND = TOTAL_ATTEND + "1";
				ENFORCE_DAY_SUM_LIST = ENFORCE_DAY_SUM_LIST + "1";

			}

			CrListField attendListField = getCrListField(ATTEND_LIST);
			this.attendListFielddArray = getListFieldGetColumns(attendListField);
			this.dateFormatUtility = new DateFormatUtility(sessionBean.getUserCode());


			// ヘッダを出力
			outputDetailHeader(1);

			// 出欠データ出力
			outputAttend();

			// 月末統計欄の出力
			outputStatistics();

			// 非授業日・休業日・学級閉鎖日の実線を出力
			outputNotEnforcedayLine();

			form.printOut();


		} catch (Exception e) {
			log.error("例外発生　帳票作成失敗",e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * 明細ヘッダ部分の出力
	 * @throws Exception
	 */
	private void outputDetailHeader(int page) throws Exception{

		DateFormatUtility dfu = new DateFormatUtility(sessionBean.getUserCode());
		dfu.setZeroToSpace(true);

		if(page == 1){

			// 日・曜日リストフィールド
			CrListField dayWeekListField = getCrListField(DAY_WEEK_LIST);
			CrText [][] dayWeekListFieldArray = getListFieldGetColumns(dayWeekListField);

			// 対象年・月
			String targetYearMonth = printFormBean.getTargetYearMonth();

			// 対象月
			String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");

			String year_month = dateFormatUtility.formatDate("YYYY年",1,targetYearMonth) + targetMonth + "月";


			// 年月を出力
			getFieldSetData(HEADER_YEAR_MONTH, year_month);

			// 学年と組を出力
			getFieldSetData(HEADER_GRADE_CLASS, printFormBean.getGroupName());


			// 学級担任氏名
			getFieldSetData(HEADER_TANNIN_NAME, printFormBean.getTeacherNameMain());

			int colIndex = 0;	// 列インデックス

			for(AttPrintDayFormBean dayFormBean : printFormBean.getDays()){

				// 日にちを出力
				setListFieldData(dayWeekListFieldArray, colIndex, 0, dayFormBean.getDay());

				// 曜日を出力
				setListFieldData(dayWeekListFieldArray, colIndex, 1, dayFormBean.getWeekday());
				if(AttPrintSearchFormBeanImpl.WEEKDAY_RED.equals(dayFormBean.getWeekDayColor())){
					// 曜日文字色が赤の場合に色を変更する
					dayWeekListFieldArray[colIndex][1].setTextColor(Color.RED);
				}

				if(dayFormBean.getEnfoce() == 0){
					String text = dayFormBean.getText().toString();
					if(text.length() > 0){
						// 6行目以降に休業日名称を朱で縦書き(データ的に45行を超えることは無い）
						int rowIndex = 5;
						for(int i=0; i<text.length(); i++){
							setListFieldData(attendListFielddArray, colIndex, rowIndex, text.substring(i, i + 1));
							attendListFielddArray[colIndex][rowIndex].setTextColor(Color.RED);
							rowIndex++;
						}
					}
				}
				// 列インデックスを加算
				colIndex++;
			}
		}

	}



	/**
	 * 出欠データ出力
	 * @throws Exception
	 */
	private void outputAttend() throws Exception{

		// 生徒氏名・番リストフィールド
		CrListField studentListField = getCrListField(STUDENT_LIST);
		CrText [][] studentListFieldArray = getListFieldGetColumns(studentListField);

		// 出欠集計リストフィールド
		CrListField attendSumListField = getCrListField(ATTEND_SUM_LIST);
		CrText [][] attendSumListFieldArray = getListFieldGetColumns(attendSumListField);

		enforceState    = new int[31][45]; //授業日

		// 行インデックス
		int rowIndex = 0;

		// 前ループ時の学年
		String prevGrade = "";

		Map<String, AttPrintStudentFormBean> studentMap = printFormBean.getStudentMap();
		for(Map.Entry<String, AttPrintStudentFormBean> entrySet : studentMap.entrySet()){

			AttPrintStudentFormBean studentFormBean = entrySet.getValue();

			if(!prevGrade.equals(studentFormBean.getCls_glade())){
				if(rowIndex > 0){
					// 別の学年に切り替わる際は、1行の空行を設ける
					checkAndSetBlankAttend(rowIndex, printFormBean.getDaysMap().get(prevGrade));	// 出欠の判定には前回ループ時の学年を使用
					rowIndex++;
				}
				// 各学年の境目には、「1学年」「3学年」の見出し行を出力する
				setListFieldData(studentListFieldArray, 1, rowIndex, studentFormBean.getCls_glade() + "学年");
				checkAndSetTitleAttend(rowIndex, printFormBean.getDaysMap().get(studentFormBean.getCls_glade()));

				rowIndex++;
			}


			// 出席番号・氏名
			setListFieldData(studentListFieldArray, 0, rowIndex, studentFormBean.getCls_reference_number());


			String stu_name_first = studentFormBean.getStu_name_first();
			String stu_name_end = studentFormBean.getStu_name_end();
			if(stu_name_first.equals(stu_name_end)){
				setListFieldData(studentListFieldArray, 1, rowIndex, stu_name_first);
			}else{
				setListFieldData(studentListFieldArray, 1, rowIndex, stu_name_end);
			}

			if(!"1".equals(printFormBean.getSearchFormBean().getBlankFlg())){


				// 日にちデータ配列
				AttPrintDayFormBean[] baseDays = printFormBean.getDays();
				AttPrintDayFormBean[] days = printFormBean.getDaysMap().get(studentFormBean.getCls_glade());
				// 出欠データ配列
				String[] attend = studentFormBean.getAttend();
				// 出停・忌引きフラグデータ配列
				String[] stopKibikiFlg = studentFormBean.getStopKibikiFlg();
				for(int i=0; i<baseDays.length; i++){

					//20180201 各学年毎の授業日を設定する。
					enforceState[i][rowIndex] = days[i].getEnfoce() ; //20180201

					if(!attendIsBlank(attend[i]) && days[i].getEnfoce() == 0 && StringUtils.isEmpty(days[i].getClo_printword())){
						// 休業日で、空以外の入力がある場合に出力を行う(学級閉鎖でない場合は除く）
						setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
						if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
							attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
						}else{
							attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
						}
					}else if(!StringUtils.isEmpty(attend[i]) && days[i].getEnfoce() != 0){
						// 出欠記号が空ではない場合かつ、休業日ではない場合に出力を行う
						if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
							// 出停と忌引きの場合は、赤字で表示する
							setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
							attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
						}else{
							setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
							attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
						}
					}else if(baseDays[i].getEnfoce() != 0 && days[i].getEnfoce() == 0){
						// 休業日基準学年に指定された学年が授業日であるが、本来の学年は非授業日である場合、該当箇所に縦線を引く
						outputLineVirtical(i, rowIndex, i, rowIndex);
					}else if(baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
					}else if(!StringUtils.isEmpty(attend[i]) && baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
						// 基準学年が休業日だが、対象生徒の学年が授業日の場合は、全角空白をセットする（基準学年が休業日の場合に縦線を引かないようにするため）
						setListFieldData(attendListFielddArray, i, rowIndex, "　");
					}
				}


				// 転編入者の個別出力
				outputEntryData(rowIndex, studentFormBean);

				// 転退学者の個別出力
				outputLeaveData(rowIndex, studentFormBean);

				// 出欠集計
				int colIndex = 0;
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getSchoolkindCount());	// 出停・忌引き
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getSickCount());			// 病欠
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getAccidentCount());		// 他
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getAttendCount());		// 出席日数
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getLateCount());			// 遅刻
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getLeaveCount());		// 早退

			}

			prevGrade = studentFormBean.getCls_glade();

			rowIndex++;

			if(rowIndex >= studentListFieldArray[0].length) break;
		}
	}

	/**
	 * 出欠文字列が非表示文言か判定する
	 * @param string
	 * @return
	 */
	private boolean attendIsBlank(String string) {
		return StringUtils.isEmpty(string) || StringUtils.equals(string, printFormBean.getAttendDisplayValue());
	}


	/**
	 * 転編入者の個別出力
	 * @param rowIndex 行インデックス
	 * @param studentFormBean 生徒情報
	 * @throws Exception
	 */
	private void outputEntryData(int rowIndex, AttPrintStudentFormBean studentFormBean) throws Exception {

		// 転入年月日
		String entry_start = studentFormBean.getEntry_start();
		if(StringUtils.isEmpty(entry_start)){
			// 転入日が存在しない場合は処理を終了する
			return;
		}

		if(!StringUtils.equals(printFormBean.getSearchFormBean().getMonth(), entry_start.substring(4, 6))){
			// 月が異なる場合は処理を終了する
			return;
		}

		// 転入日
		int entry_start_date = Integer.parseInt(entry_start.substring(6, 8));

		// 「入」を記載する配列の位置（配列のため、列の位置は日付-1）
		int entry_start_col = entry_start_date - 1;

		// 「入」を出力
		replaceListFieldData(rowIndex, entry_start_col,  "入");

		// 転入日が1日の場合は線を引く必要が無いため処理を終了する
		if(entry_start_date == 1){
			return;
		}

		// 入学年月日の前日以前のセルに、朱の横線を実線で出力する。
		outputLineHorizontalInBlankCell(rowIndex, 0, entry_start_col, CorLineStyle.SOLID);
	}

	/**
	 * 転退学者の個別出力
	 * @param rowIndex	行インデックス
	 * @param studentFormBean	生徒情報
	 * @throws Exception
	 */
	private void outputLeaveData(int rowIndex, AttPrintStudentFormBean studentFormBean) throws Exception {

		// 異動区分
		String reg_mcode = studentFormBean.getReg_mcode();

		if(StringUtils.isEmpty(reg_mcode)){
			// 異動区分が空の場合は処理を終了
			return;
		}

		// 対象年・月
		Search32177000FormBean searchFormBean = printFormBean.getSearchFormBean();
		String targetYearMonth = strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "01").substring(0, 6);

		// 学校を去った日
		String regStart = studentFormBean.getReg_start();

		// 学校を去った日の列インデックス
		Integer regStartColIndex = Integer.parseInt(regStart.substring(6, 8)) - 1;

		// 転出先が受け入れた日
		String permitdate = studentFormBean.getReg_permitdate();
		permitdate = StringUtils.isNumeric(permitdate) ? permitdate : "99999999";

		// 転出先が受け入れた日の列インデックス
		Integer permitdateColIndex = 0;
		if("30".equals(reg_mcode)){
			// 転出者(reg_mcode='30')である場合のみ、reg_permitdateが有効なため、列インデックスを計算する
			permitdateColIndex = Integer.parseInt(permitdate.substring(6, 8)) - 1;
		}

		// 対象月の最終日の列インデックス
		int lastDayColIndex = printFormBean.getDays().length - 1;

		//-----------------------------------------------------
		// 前月以前に転退学した場合
		//-----------------------------------------------------
		if("30".equals(reg_mcode) && compareMonth(targetYearMonth, permitdate) > 0 ||		// 転出者(reg_mcode='30')である場合、reg_permitdateの日付が前月の月末日以前の者
		   "40".equals(reg_mcode) && compareMonth(targetYearMonth, regStart) > 0){			// 退学者(reg_mcode='40')である場合、reg_startの日付が前月の月末日以前の者

			outputLineHorizontalInBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);

			//生徒氏名・番リストフィールドに2重線を引く
			//CrListField studentListField = getCrListField(STUDENT_LIST);
			//CrText [][] studentListFieldArray = getListFieldGetColumns(studentListField);
			//outputDoubleLineHorizontal(studentListFieldArray, 1, rowIndex, 2, rowIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// 転出者(reg_mcode='30')で、移動日が月を跨ぐもの(学校を去った日(reg_start)が前月の月末日以前)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// 転出者(reg_mcode='30')である場合
				 compareMonth(targetYearMonth, regStart) > 0 &&						// 学校を去った日(reg_start)が前月の月末日以前である
				 compareMonth(targetYearMonth, permitdate) == 0){					// 転出先が受け入れた日の前日が、対象月の何れかの日である


			// 既に文字が設定済みの場合は、非授業日・休業日・学級閉鎖日の内容が出力されているためそちらを優先
			if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
				// 内容が空の場合のみ「出」を出力する
				replaceListFieldData(rowIndex, permitdateColIndex,  "退");
			}

			// 斜線はpermitdateColIndexの一つ前のセルまで出力
			outputLineObliqueBlankCell(rowIndex, 0, permitdateColIndex, CorLineStyle.SOLID);

			// 「出」以降の直線
			outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// 転出者(reg_mcode='30')で、移動日が月を跨ぐもの(学校を去った日(reg_start)が前月の月末日以前、転出先が受け入れた日の前日が、対象月以降)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// 転出者(reg_mcode='30')である場合
				compareMonth(targetYearMonth, regStart) > 0 &&						// 学校を去った日(reg_start)が前月の月末日以前である
				compareMonth(targetYearMonth, permitdate) < 0){						// 転出先が受け入れた日の前日が、対象月以降である

			outputLineObliqueBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// 転出者(reg_mcode='30') 学校を去った日が対象月(転出先が受け入れた日の前日が、対象月以降)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// 転出者(reg_mcode='30')である場合
				compareMonth(targetYearMonth, regStart) == 0 &&						// 学校を去った日(reg_start)が対象月である
				compareMonth(targetYearMonth, permitdate) < 0){						// 転出先が受け入れた日の前日が、対象月以降である

			outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// 転出者(reg_mcode='30') 学校を去った日が対象月
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// 転出者(reg_mcode='30')である場合
				compareMonth(targetYearMonth, regStart) == 0 &&						// 学校を去った日(reg_start)が対象月である
				compareMonth(targetYearMonth, permitdate) == 0){					// 転出先が受け入れた日の前日が、対象月である

			// reg_startとreg_permitdateの日付の差が2日以上である場合、その間の日に朱の横線を斜線で引く
			if(Integer.parseInt(permitdate.substring(6,8)) - Integer.parseInt(regStart.substring(6,8)) >= 2){

				// 「出」を出力する
				replaceListFieldData(rowIndex, permitdateColIndex, "退");

				// reg_startとreg_permitdateの日付の差が2日以上である場合、その間の日に朱の横線を斜線で引く。（先に前述の処理で「出」を出力しておくこと）
				outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, permitdateColIndex, CorLineStyle.SOLID);

				// 転出先が受け入れた日の前日(reg_permitdateの日付)の翌日以降に朱の横線を実線で引く
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else if(Integer.parseInt(permitdate.substring(6,8)) - Integer.parseInt(regStart.substring(6,8)) == 1){
				// 内容が空の場合のみ「出」を出力する
				replaceListFieldData(rowIndex, permitdateColIndex, "退");

				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else {
				replaceListFieldData(rowIndex, permitdateColIndex, "退");
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
			}
		}
		//-----------------------------------------------------
		// 退学者(reg_mcode='40')
		//-----------------------------------------------------
		else if("40".equals(reg_mcode) &&											// 退学者(reg_mcode='40')である場合
				compareMonth(targetYearMonth, regStart) == 0 ){						// 学校を去った日(reg_start)が対象月である

			// 「出」を出力する
			replaceListFieldData(rowIndex, regStartColIndex, "退");

			outputLineHorizontalInBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}

	}

	/**
	 * 月末統計欄の出力
	 * @throws Exception
	 */
	private void outputStatistics() throws Exception{

		CrListField enforceDaySumList = getCrListField(ENFORCE_DAY_SUM_LIST);
		CrText[][] enforceDaySumListFieldArray = getListFieldGetColumns(enforceDaySumList);
		int rowMax;
		//小学・中学でループ数を設定
		if(sessionBean.getUseKind2().equals("1")) {
			rowMax = 6;

		}else {
			rowMax = 3;
		}

		//初期化
		for(int i = 0; i < rowMax; i++) {
			String grade = (i + 1) + "年";
			setListFieldData(enforceDaySumListFieldArray, 0, i, grade); //学年
			setListFieldData(enforceDaySumListFieldArray, 1, i, "0"); //授業日数
			setListFieldData(enforceDaySumListFieldArray, 2, i, "0"); //授業を行わない日数
			setListFieldData(enforceDaySumListFieldArray, 3, i, "0"); //計
		}

		//リストの生成
		List<Print32177000EnforceEntity> enforceList = printFormBean.getEnforceList();
		for(Print32177000EnforceEntity enforce : enforceList) {
			int rowIdx = Integer.parseInt(enforce.getCls_glade()) - 1;

			setListFieldData(enforceDaySumListFieldArray, 1, rowIdx, enforce.getEnforceNum()); //授業日数
			setListFieldData(enforceDaySumListFieldArray, 2, rowIdx, enforce.getHolidayNum()); //授業を行わない日数
			int enforceNum = enforce.getEnforceNum() == null ? 0 : enforce.getEnforceNum();
			int holidayNum = enforce.getHolidayNum() == null ? 0 : enforce.getHolidayNum();
			setListFieldData(enforceDaySumListFieldArray, 3, rowIdx, enforceNum + holidayNum); //計
		}

		Map<String, Print32177000StatisticsFormBean> statisticsMap = printFormBean.getStatisticsMap();

		CrListField studentSumListListField = getCrListField(TOTAL_ATTEND);
		CrText[][] studentSumListListFieldArray = getListFieldGetColumns(studentSumListListField);

		int zaisekiStuSumPre = 0;		// 在籍者数
		int entryStuSum = 0;			// 異動・入学
		int leaveStuSum = 0;			// 異動・退学


		// 男:1、女:2
		String sex_male  = "1";
		String sex_female = "2";
		// 対象月が4月の場合は前月末現在在籍と差し引き今月末現在在籍を表示しない
		String targetYearMonth = printFormBean.getTargetYearMonth();
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");
		if(!targetMonth.equals("4")) {
			// 在籍者数(男)
			setListFieldData(studentSumListListFieldArray, 0, 0, statisticsMap.get(sex_male).getZaisekiStuNumPre());
			// 在籍者数(女)
			setListFieldData(studentSumListListFieldArray, 0, 1, statisticsMap.get(sex_female).getZaisekiStuNumPre());
			// 在籍者数(合計)
			zaisekiStuSumPre =  statisticsMap.get(sex_male).getZaisekiStuNumPre() + statisticsMap.get(sex_female).getZaisekiStuNumPre();
			setListFieldData(studentSumListListFieldArray, 0, 2, zaisekiStuSumPre);
		}

		// 差引今月末現在在籍(男)
		setListFieldData(studentSumListListFieldArray, 3, 0, statisticsMap.get(sex_male).getZaisekiStuNum());
		//  差引今月末現在在籍(女)
		setListFieldData(studentSumListListFieldArray, 3, 1,statisticsMap.get(sex_female).getZaisekiStuNum());
		//  差引今月末現在在籍(合計)
		int ZaisekiSumNow = statisticsMap.get(sex_male).getZaisekiStuNum() + statisticsMap.get(sex_female).getZaisekiStuNum();
		setListFieldData(studentSumListListFieldArray, 3, 2, ZaisekiSumNow);


		// 異動・入学者数(男)
		setListFieldData(studentSumListListFieldArray, 1, 0, statisticsMap.get(sex_male).getEntryStuNum());
		// 異動・入学者数(女)
		setListFieldData(studentSumListListFieldArray, 1, 1, statisticsMap.get(sex_female).getEntryStuNum());
		// 異動・入学者数(合計)
		entryStuSum =  statisticsMap.get(sex_male).getEntryStuNum() + statisticsMap.get(sex_female).getEntryStuNum();
		setListFieldData(studentSumListListFieldArray, 1, 2, entryStuSum);

		// 異動・退学者数(男)
		setListFieldData(studentSumListListFieldArray, 2, 0, statisticsMap.get(sex_male).getLeaveStuNum());
		// 異動・退学者数(女)
		setListFieldData(studentSumListListFieldArray, 2, 1, statisticsMap.get(sex_female).getLeaveStuNum());
		// 異動・退学者数(合計)
		leaveStuSum =  statisticsMap.get(sex_male).getLeaveStuNum() + statisticsMap.get(sex_female).getLeaveStuNum();
		setListFieldData(studentSumListListFieldArray, 2, 2, leaveStuSum);

	}

	/**
	 * 現在の出欠内容のデータにより、内容の書き換え、追加を行う
	 * @param rowIndex 行インデックス
	 * @param dayIndex 日のインデックス
	 * @param replaceText 置換文字列
	 * @throws Exception
	 */
	private void replaceListFieldData(int rowIndex, Integer dayIndex, String replaceText) throws Exception {

		// セットされている文字列
		String outputText = attendListFielddArray[dayIndex][rowIndex].getText();

		if(isBlankOrAttendText(outputText)){
			// テキストの内容が空か、出席の場合は置換用文字列で置き換える
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText);
		}else if(printFormBean.getDays()[dayIndex].getEnfoce() == 1){
			// 出席データがある場合は出力内容をまとめる
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText + outputText);
		}
	}


	/**
	 * テキストの内容が空か、出席データかを判定する
	 * @param text チェック対象文字列
	 * @return 空か、出席の場合はtrue
	 */
	private boolean isBlankOrAttendText(String text) {
		return StringUtils.isEmpty(text) || StringUtils.equals(text, ATTEND_KIND_00_DISP);
	}

	/**
	 * 空白行に対し、授業日の場合は全角空白をセットする
	 * @param rowIndex 行インデックス
	 * @param days 授業日情報の配列
	 * @throws Exception
	 */
	private void checkAndSetBlankAttend(int rowIndex, AttPrintDayFormBean[] days) throws Exception {

		for (int i = 0; i < days.length; i++) {
			AttPrintDayFormBean dayFormBean = days[i];
			if(dayFormBean.getEnfoce() != 0){
				// 授業日の場合は、全角空白をセットする（基準学年が休業日の場合に縦線を引かないようにするため）
				setListFieldData(attendListFielddArray, i, rowIndex, "　");
			}
		}
	}

	/**
	 * 学年行に対し、授業日の場合は全角空白をセットする
	 * @param rowIndex 行インデックス
	 * @param days 授業日情報の配列
	 * @throws Exception
	 */
	private void checkAndSetTitleAttend(int rowIndex, AttPrintDayFormBean[] days) throws Exception {

		AttPrintDayFormBean[] baseDays = printFormBean.getDays();

		for(int i=0; i<baseDays.length - 1; i++){
			if(baseDays[i].getEnfoce() != 0 && days[i].getEnfoce() == 0){
				// 休業日基準学年に指定された学年が授業日であるが、本来の学年は非授業日である場合、該当箇所に縦線を引く
				outputLineVirtical(i, rowIndex, i, rowIndex);
			}else if(baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
				// 基準学年が休業日だが、対象生徒の学年が授業日の場合は、全角空白をセットする（基準学年が休業日の場合に縦線を引かないようにするため）
				setListFieldData(attendListFielddArray, i, rowIndex, "　");
			}
		}
	}






	/**
     * 始点座標から終点座標まで線を出力します。
     * @param p1	始点座標の配列
     * @param p2	終点座標の配列
     * @param lineWidth	線の太さ
	 * @param layer	出力レイヤ
	 * @param lineColor 線の色
	 * @param lineStyle 線のスタイル
	 */
    private void printSlash(int[] p1, int[] p2, int lineWidth, CrLayer layer, Color lineColor, CorLineStyle lineStyle){

    	if(p1 == null || p2 == null){
    		return;
    	}

		CrLine crLine = new CrLine(p1[0], p1[1], p2[0], p2[1]);

		CorUnit tempUnit = pdfDocumentBeanCR.getDraw().getUnit();
		// 座標の単位を1mm単位に設定
		pdfDocumentBeanCR.getDraw().setUnit(CorUnit.MM);
		crLine.setLineWidth(lineWidth);
		crLine.setLineStyle(lineStyle);
		pdfDocumentBeanCR.getDraw().setUnit(tempUnit);

		// 線の色を設定
		crLine.setLineColor(lineColor);

		if(layer != null){
			layer.draw(crLine);
		}else{
			form.draw(crLine);
		}
	}

    /**
     * 空欄のセルを対象にして出力リストフィールド上に線を引く（横）
     *
     * @param rowIndex		行インデックス
     * @param endColIndex	線の終端となる列インデックス
     * @param lineStyle		出力する線のスタイル
     * @throws Exception
     */
	private void outputLineHorizontalInBlankCell(int rowIndex, int startColIndex, int endColIndex, CorLineStyle lineStyle) throws Exception {

		// 開始列が超える場合は処理を終了する
		if(startColIndex > endColIndex){
			return;
		}

		// 開始列が終了列と同じ場合
		if(startColIndex == endColIndex){
			if(isBlankOrAttendText(attendListFielddArray[startColIndex][rowIndex].getText())){
				// 出欠・ブランクの場合に線を引く
				outputLineHorizontal(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
		}else{

			// 線描画開始セル(CrText)の位置
			int startCol = startColIndex;

			// colIndexの位置には何らかの内容が出力されているためcol<=colIndexとしてループ内で判定を行う
			for(int col=startCol; col<=endColIndex; col++){

				// 出欠・ブランクではない場合
				if(!isBlankOrAttendText(attendListFielddArray[col][rowIndex].getText())){

					if(startCol < col){
						// 線を引く終了位置は文字が出力されているセルの一つ前が対象のため-1
						outputLineHorizontal(startCol, rowIndex, col - 1, rowIndex, lineStyle);
					}
					// 描画開始セルの位置を更新
					startCol = col + 1;
				}
			}

			if(startCol < endColIndex){
				// 最終列までがすべて空の場合は末尾まで線を引く
				outputLineHorizontal(startCol, rowIndex, endColIndex, rowIndex, lineStyle);
			}
		}
	}

    /**
     * 出欠リストフィールド上に線を引く(横）
     * @param startCol	開始列
     * @param startRow	開始行
     * @param endCol	終了列
     * @param endRow	終了行
     * @throws Exception
     */
    private void outputDoubleLineHorizontal(CrText [][] listFieldArray, int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// リストの線太さ(余剰計算用)
		int lineWidth = listFieldArray[0][0].getLineWidth();


		// 開始座標X
		int startX = listFieldArray[startCol][startRow].getStartX();
		// 余剰幅を加算
		startX += lineWidth * 4;		// リスト左端の線は太いため、余剰分として太さ×4右にずらす

		// 開始座標Y（セルのY座標＋セルの高さ÷2）
		int cellHeight = listFieldArray[startCol][startRow].getHeight();	// 出力するセルの高さ
		int startY = listFieldArray[startCol][startRow].getStartY() + (cellHeight / 2);

		// 終端座標X
		int cellWidth = listFieldArray[endCol][startRow].getWidth();		// 出力するセルの幅
		int endX = listFieldArray[endCol][endRow].getStartX() + cellWidth;
		// 余剰幅を減算
		endX -= lineWidth * 4;		// リスト左端の線は太いため、余剰分として太さ×2左にずらす

		// 終端座標Y（セルのY座標＋セルの高さ÷2）
		int endY = listFieldArray[endCol][endRow].getStartY() + (cellHeight / 2);

		// 直線を描画
		printSlash(new int[]{startX,  startY - (cellHeight / 10)}, new int[]{endX, endY - (cellHeight / 10)}, 5, null, Color.BLACK, lineStyle);
		printSlash(new int[]{startX,  startY + (cellHeight / 10)}, new int[]{endX, endY + (cellHeight / 10)}, 5, null, Color.BLACK, lineStyle);

    }


	/**
	 * 非授業日・休業日・学級閉鎖日の実線を出力
	 * @throws Exception
	 */
	private void outputNotEnforcedayLine() throws Exception{

		AttPrintDayFormBean[] days = printFormBean.getDays();

		// リストの最終行インデックス
		int maxRowIndex = attendListFielddArray[0].length - 1;

		// 1日からその月の最終日列まで
		for(int col=0; col<days.length; col++){

			// 授業実施日ではない場合
			if(days[col].getEnfoce() == 0){

				// 線描画開始セル(CrText)の行位置
				int startRow = 0;

				// 1行目から最終行まで
				for(int row=0; row<=maxRowIndex; row++){

					// セル(CrText)に文字がセットされている場合
					if((!StringUtils.isEmpty(attendListFielddArray[col][row].getText()) && !attendListFielddArray[col][row].getText().contains("退"))
					|| enforceState[col][row]==1  //20180201
					){
						if(startRow < row){
							// 線を引く終了位置は、文字が出力されているセルの一つ前が対象のためrowは-1した値
							outputLineVirtical(col, startRow, col, row - 1);
						}
						// 描画開始セルの位置を更新
						startRow = row + 1;
					}
				}

				// 末尾まで線を引く
				if(startRow < maxRowIndex){
					outputLineVirtical(col, startRow, col, maxRowIndex);
				}
			}
		}
	}

    /**
     * 出欠リストフィールド上に線を引く（縦）
     * @param startCol	開始列
     * @param startRow	開始行
     * @param endCol	終了列
     * @param endRow	終了行
     * @throws Exception
     */
    private void outputLineVirtical(int startCol, int startRow, int endCol, int endRow) throws Exception{

		// リストの線太さ
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();
		int cellHeight = attendListFielddArray[0][0].getHeight();


		// 開始座標X
		int startX = attendListFielddArray[startCol][startRow].getStartX() + (cellWidth / 2);

		// 開始座標Y
		int startY = attendListFielddArray[startCol][startRow].getStartY();
		// 余剰幅を加算
		if(startRow == 0){
			startY += lineWidth * 2;		// リスト左端の線は太いため、余剰分として線の太さ×2した値分下げる
		}else{
			startY += lineWidth;
		}

		// 終端座標X
		int endX = attendListFielddArray[endCol][endRow].getStartX() + (cellWidth / 2);

		// 終端座標Y
		int endY = attendListFielddArray[endCol][endRow].getStartY() + cellHeight;
		// 余剰幅を減算
		if(endRow == attendListFielddArray[endCol].length - 1){
			endY -= lineWidth * 2;		// リスト左端の線は太いため、余剰分として線の太さ×2した値分上げる
		}else{
			endY -= lineWidth;
		}

		// 直線を描画
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.RED, CorLineStyle.SOLID);

    }

    /**
     * 出欠リストフィールド上に線を引く（横）
     * @param startCol	開始列
     * @param startRow	開始行
     * @param endCol	終了列
     * @param endRow	終了行
     * @throws Exception
     */
    private void outputLineHorizontal(int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// リストの線太さ
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();		// 出力するセルの幅
		int cellHeight = attendListFielddArray[0][0].getHeight();	// 出力するセルの高さ

		// 開始座標X
		int startX = attendListFielddArray[startCol][startRow].getStartX();
		// 余剰幅を加算
		if(startCol == 0){
			startX += lineWidth * 2;		// リスト左端の線は太いため、余剰分として太さ×2右にずらす
		}else{
			startX += lineWidth;
		}

		// 開始座標Y（セルのY座標＋セルの高さ÷2）
		int startY = attendListFielddArray[startCol][startRow].getStartY() + (cellHeight / 2);

		// 終端座標X
		int endX = attendListFielddArray[endCol][endRow].getStartX() + cellWidth;
		// 余剰幅を減算
		if(endCol == attendListFielddArray.length - 1){
			endX -= lineWidth * 2;		// リスト左端の線は太いため、余剰分として太さ×2左にずらす
		}else{
			endX -= lineWidth;
		}

		// 終端座標Y（セルのY座標＋セルの高さ÷2）
		int endY = attendListFielddArray[endCol][endRow].getStartY() + (cellHeight / 2);

		// 直線を描画
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.BLACK, lineStyle);


		for(int col=startCol; col<=endCol; col++){
			// 「・」の可能性があるため、空白を改めてセット
			if(isBlankOrAttendText(attendListFielddArray[col][startRow].getText())){
				setListFieldData(attendListFielddArray, col, startRow, "");
			}
		}

    }

    /**
     * 月を比較する
     * @param date1	月日1(YYYYMM or YYYYMMDD)
     * @param date2	月日2(YYYYMM or YYYYMMDD)
     * @return 月日1が月日2よりも前の月の場合は-1、月日1が月日2よりも後の月の場合は 1、同じ場合は0
     */
    private int compareMonth(String date1, String date2){

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Integer.parseInt(date1.substring(0,4)), Integer.parseInt(date1.substring(4,6))-1, 1);
		cal1.set(Calendar.MILLISECOND, 0);


		Calendar cal2 = Calendar.getInstance();
		cal2.set(Integer.parseInt(date2.substring(0,4)), Integer.parseInt(date2.substring(4,6))-1, 1);
		cal1.set(Calendar.MILLISECOND, 0);


		if(cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)){
			// 年1が月日2よりも前の年の場合は-1を返す
			return -1;
		}else if(cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)){
			// 年1が月日2よりも後の年の場合は 1を返す
			return 1;
		}

		if(cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH)){
			// 月1が月2よりも前の月の場合は-1を返す
			return -1;
		}else if(cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH)){
			// 月1が月2よりも後の月の場合は 1を返す
			return 1;
		}

    	return 0;
    }


	/**
	 * 年度・月から年月日文字列を取得する
	 * @param sessionBean システム情報
	 * @param nendo 年度
	 * @param month 月
	 * @param day 日
	 * @return 年月日YYYYMMDD
	 */
	protected String strDayOfMonth(SystemInfoBean sessionBean, String nendo, String month, String day){
		Calendar calendar = getCalendar(sessionBean, nendo, month, day);

		return String.format("%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
	}

	private Calendar getCalendar(SystemInfoBean sessionBean, String nendo, String month, String day){

		Calendar nendoStartDate = Calendar.getInstance();
		nendoStartDate.set(Integer.parseInt(nendo),
				           Integer.parseInt(sessionBean.getSystemNendoStartDate().trim().substring(0, 2)) - 1,
				           Integer.parseInt(sessionBean.getSystemNendoStartDate().trim().substring(2, 4)),
				           0,
				           0,
				           0);
		nendoStartDate.set(Calendar.MILLISECOND, 0);

		Calendar targetDate = Calendar.getInstance();
		targetDate.set(Integer.parseInt(nendo),
				           Integer.parseInt(month) - 1,
				           Integer.parseInt(day),
				           0,
				           0,
				           0);
		targetDate.set(Calendar.MILLISECOND, 0);

		if(nendoStartDate.compareTo(targetDate) > 0 ){
			targetDate.add(Calendar.YEAR, 1);
		}

		return targetDate;
	}

	 /**
     * 空欄のセルを対象にして出力リストフィールド上に線を引く（斜線）
     *
     * @param rowIndex		行インデックス
     * @param endColIndex	線の終端となる列インデックス
     * @param lineStyle		出力する線のスタイル
     * @throws Exception
     */
	private void outputLineObliqueBlankCell(int rowIndex, int startColIndex, int endColIndex, CorLineStyle lineStyle) throws Exception {

		// 開始列が超える場合は処理を終了する
		if(startColIndex > endColIndex){
			return;
		}

		// 開始列が終了列と同じ場合
		if(startColIndex == endColIndex){
			if(isBlankOrAttendText(attendListFielddArray[startColIndex][rowIndex].getText())){
				// 出欠・ブランクの場合に線を引く
				outputLineOblique(startColIndex, rowIndex, startColIndex, rowIndex, lineStyle);
			}
		}else{

			// 線描画開始セル(CrText)の位置
			int startCol = startColIndex;

			// 各セルに斜線を引く
			for(int col=startCol; col<=endColIndex; col++){

				if(StringUtils.isEmpty(attendListFielddArray[col][rowIndex].getText())){
					outputLineOblique(col, rowIndex, col, rowIndex, lineStyle);
				}

			}
		}

	}


    /**
     * 出欠リストフィールド上に線を引く(斜線）
     * @param startCol	開始列
     * @param startRow	開始行
     * @param endCol	終了列
     * @param endRow	終了行
     * @throws Exception
     */
    private void outputLineOblique(int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// リストの線太さ
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();		// 出力するセルの幅
		int cellHeight = attendListFielddArray[0][0].getHeight();	// 出力するセルの高さ

		// 開始座標X
		int startX = attendListFielddArray[startCol][startRow].getStartX();
		// 余剰幅を加算
		if(startCol == 0){
			startX += lineWidth * 2;		// リスト左端の線は太いため、余剰分として太さ×2右にずらす
		}else{
			startX += lineWidth;
		}

		// 開始座標Y（セルのY座標＋セルの高さ÷2）
		int startY = attendListFielddArray[startCol][startRow].getStartY() + cellHeight;

		// 終端座標X
		int endX = attendListFielddArray[endCol][endRow].getStartX() + cellWidth;
		// 余剰幅を減算
		if(endCol == attendListFielddArray.length - 1){
			endX -= lineWidth * 2;		// リスト左端の線は太いため、余剰分として太さ×2左にずらす
		}else{
			endX -= lineWidth;
		}

		// 終端座標Y（セルのY座標＋セルの高さ÷2）
		int endY = attendListFielddArray[endCol][endRow].getStartY();

		// 直線を描画
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.RED, lineStyle);

    }
}
