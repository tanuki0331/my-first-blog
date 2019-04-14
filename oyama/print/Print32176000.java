/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.print;

import java.awt.Color;
import java.util.Calendar;
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
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000DayFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StudentFormBean;


/**
 * 出席簿印刷(通常学級　小山市用) 出力クラス.
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32176000 extends AbstractPdfManagerAdditionPagesCR {


	/**log4j*/
	private final Log log = LogFactory.getLog(Print32176000.class);

	/** レイアウトオブジェクト */
	private static final String HEADER_YEAR_MONTH = "HeaderYearMonth";			// ヘッダ 月
	private static final String HEADER_TANNIN_NAME = "HeaderTanninName";		// ヘッダ　学級担任氏名
	private static final String HEADER_GRADE_CLASS = "HeaderGradeClass";		// ヘッダ　学年組

	private static final String ENFORCE_DAY_SUM = "EnforceDaySum";				// 月末統計・授業時数
	private static final String HOLIDAY_SUM = "HolidaySum";						// 月末統計・非授業時数
	private static final String DAY_SUM = "DaySum";								// 月末統計・授業時数 + 非授業時数


	private static final String DAY_WEEK_LIST = "DayWeekList";					// 日・曜日リストフィールド
	private static final String STUDENT_LIST = "StudentList";					// 生徒氏名・番リストフィールド
	private static final String TOTAL_ATTEND = "TotalAttend";					// 月末統計リストフィールド
	private static final String ATTEND_LIST = "AttendList";						// 出欠欄リストフィールド
	private static final String ATTEND_SUM_LIST = "AttendSumList";				// 出欠集計リストフィールド


	/** 画面で選択した曜日文字色(赤)の定義 */
	private static final String WEEKDAY_COLOR_RED = "1";

	/** 出欠欄リストフィールド */
	private CrText [][] attendListFielddArray;

	/** 印刷FormBean */
	private Print32176000FormBean printFormBean;

	protected DateFormatUtility dateFormatUtility = null;


	@Override
	protected void doPrint() throws TnaviPrintException {

		try {

			CrListField attendListField = getCrListField(ATTEND_LIST);
			this.attendListFielddArray = getListFieldGetColumns(attendListField);
			this.dateFormatUtility = new DateFormatUtility(printFormBean.getUserCode());

			// ヘッダを出力
			outputDetailHeader();

			// 出欠データ出力
			outputAttend();

			// 月末統計欄の出力
			outputStatistics();

			// 非授業日・休業日・学級閉鎖日の実線を出力
			outputNotEnforcedayLine();

			form.printOut();
			form.initialize();//フォーム初期化

		} catch (Exception e) {
			log.error("例外発生　帳票作成失敗",e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * 明細ヘッダ部分の出力
	 * @throws Exception
	 */
	private void outputDetailHeader() throws Exception{

		// 日・曜日リストフィールド
		CrListField dayWeekListField = getCrListField(DAY_WEEK_LIST);
		CrText [][] dayWeekListFieldArray = getListFieldGetColumns(dayWeekListField);

		// 対象年・月
		String targetYearMonth = printFormBean.getTargetYearMonth();

		// 対象年
		String targetYear = targetYearMonth.substring(0, 4);

		// 対象月
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");

		String year_month = dateFormatUtility.formatDate("YYYY年",1,targetYearMonth) + targetMonth + "月";


		// 年月を出力
		getFieldSetData(HEADER_YEAR_MONTH, year_month);

		// 学年と組を出力
		getFieldSetData(HEADER_GRADE_CLASS, "第" + printFormBean.getGrade() + "学年" +  printFormBean.getHmrclass() + "組");

		// 学級担任氏名
		getFieldSetData(HEADER_TANNIN_NAME, printFormBean.getTeacherName());

		int colIndex = 0;	// 列インデックス
		Calendar cal = Calendar.getInstance();
		for(Print32176000DayFormBean dayFormBean : printFormBean.getDays()){

			// 日にちを出力
			setListFieldData(dayWeekListFieldArray, colIndex, 0, dayFormBean.getDay());

			// Calendarクラスに日付をセット
			cal.set(Integer.parseInt(targetYear), Integer.parseInt(targetMonth)-1, dayFormBean.getDay());

			// 曜日を特定
			String week = "";
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case 1: week = "日"; break;
			case 2: week = "月"; break;
			case 3: week = "火"; break;
			case 4: week = "水"; break;
			case 5: week = "木"; break;
			case 6: week = "金"; break;
			case 7: week = "土"; break;
			}

			// 曜日を出力
			setListFieldData(dayWeekListFieldArray, colIndex, 1, week);
			if(WEEKDAY_COLOR_RED.equals(dayFormBean.getWeekDayColor())){
				// 曜日文字色が赤の場合に色を変更する
				dayWeekListFieldArray[colIndex][1].setTextColor(Color.RED);
			}

			if(dayFormBean.getEnfoce() == 0){
				String text = dayFormBean.getText().toString();
				if(text.length() > 0){
					int rowIndex = 5;
					// 6行目以降に休業日名称を朱で縦書き(データ的に45行を超えることは無い）
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


		// 行インデックス
		int rowIndex = 0;

		Map<String, Print32176000StudentFormBean> studentMap = printFormBean.getStudentMap();
		for(Map.Entry<String, Print32176000StudentFormBean> entrySet : studentMap.entrySet()){

			Print32176000StudentFormBean studentFormBean = entrySet.getValue();

			// 出席番号・氏名
			setListFieldData(studentListFieldArray, 0, rowIndex, studentFormBean.getCls_reference_number());
			setListFieldData(studentListFieldArray, 1, rowIndex, studentFormBean.getStu_name());
			//setListFieldData(studentListFieldArray, 2, rowIndex, studentFormBean.getCls_reference_number());

			// 日にちデータ配列
			Print32176000DayFormBean[] days = printFormBean.getDays();
			// 出欠データ配列
			String[] attend = studentFormBean.getAttend();
			// 出停・忌引きフラグデータ配列
			String[] stopKibikiFlg = studentFormBean.getStopKibikiFlg();
			for(int i=0; i<printFormBean.getMonthDayCnt(); i++){
				if(!StringUtils.isEmpty(attend[i]) && !days[i].isClosingclass()){
					// 出欠記号が空ではない場合かつ、学級閉鎖ではない場合に出力を行う
					setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
					if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
						// 出停と忌引きの場合は、赤字で表示する
						attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
					}else{
						attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
					}
				}
			}

			// 転編入者の個別出力
			outputEntryData(rowIndex, studentFormBean);

			// 転退学者の個別出力
			outputLeaveData(rowIndex, studentFormBean);

			// 出欠集計
			setListFieldData(attendSumListFieldArray, 0, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getSchoolkindCount()));		// 出停・忌引
			setListFieldData(attendSumListFieldArray, 1, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getSickCount()));		// 病欠
			setListFieldData(attendSumListFieldArray, 2, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getAccidentCount()));	// 事故
			setListFieldData(attendSumListFieldArray, 3, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getAttendCount()));		// 出席日数
			setListFieldData(attendSumListFieldArray, 4, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getLateCount()));		// 遅刻
			setListFieldData(attendSumListFieldArray, 5, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getLeaveCount()));		// 早退

			// 備考
			//setListFieldData(attendBikoListFieldArray, 0, rowIndex, studentFormBean.getAttendSummary().getAtc_comment());

			rowIndex++;
		}
	}


	/**
	 * 転編入者の個別出力
	 * @param rowIndex 行インデックス
	 * @param studentFormBean 生徒情報
	 * @throws Exception
	 */
	private void outputEntryData(int rowIndex, Print32176000StudentFormBean studentFormBean) throws Exception {

		// 転入年月日
		String entry_start = studentFormBean.getEntry_start();
		if(StringUtils.isEmpty(entry_start)){
			// 転入日が存在しない場合は処理を終了する
			return;
		}

		// 転入日
		int entry_start_date = Integer.parseInt(entry_start.substring(6, 8));

		// 「入」を記載する配列の位置（配列のため、列の位置は日付-1）
		int entry_start_col = entry_start_date - 1;

		// 既に文字が設定済みの場合は、非授業日・休業日・学級閉鎖日の内容が出力されているためそちらを優先
		if(StringUtils.isEmpty(attendListFielddArray[entry_start_col][rowIndex].getText()) || printFormBean.getDays()[entry_start_col].getEnfoce() == 1){
			// 内容が空の場合のみ「入」を出力する
			replaceListFieldData(rowIndex,entry_start_col, "入");
		}

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
	private void outputLeaveData(int rowIndex, Print32176000StudentFormBean studentFormBean) throws Exception {

		// 異動区分
		String reg_mcode = studentFormBean.getReg_mcode();

		if(StringUtils.isEmpty(reg_mcode)){
			// 異動区分が空の場合は処理を終了
			return;
		}

		// 対象年・月
		String targetYearMonth = printFormBean.getTargetYearMonth();

		// 学校を去った日
		String regStart = studentFormBean.getReg_start();

		// 転出先が受け入れた日の列インデックス
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
		int lastDayColIndex = printFormBean.getMonthDayCnt() - 1;

		//-----------------------------------------------------
		// 前月以前に転退学した場合
		//-----------------------------------------------------
		if("30".equals(reg_mcode) && compareMonth(targetYearMonth, permitdate) > 0 ||		// 転出者(reg_mcode='30')である場合、reg_permitdateの日付が前月の月末日以前の者
		   "40".equals(reg_mcode) && compareMonth(targetYearMonth, regStart) > 0){			// 退学者(reg_mcode='40')である場合、reg_startの日付が前月の月末日以前の者

			outputLineHorizontalInBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);
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
				replaceListFieldData(rowIndex,  permitdateColIndex, "退");
			}

			// 斜線はpermitdateColIndexの一つ前のセルまで出力
			outputLineObliqueBlankCell(rowIndex, 0, permitdateColIndex, CorLineStyle.SOLID);

			// 「退」以降の直線
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

				// 既に文字が設定済みの場合は、非授業日・休業日・学級閉鎖日の内容が出力されているためそちらを優先
				if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
					// 内容が空の場合のみ「退」を出力する
					replaceListFieldData(rowIndex, permitdateColIndex,"退");
				}

				// reg_startとreg_permitdateの日付の差が2日以上である場合、その間の日に朱の斜線で引く。（先に前述の処理で「退」を出力しておくこと）
				outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, permitdateColIndex, CorLineStyle.SOLID);

				// 転出先が受け入れた日の前日(reg_permitdateの日付)の翌日以降に朱の横線を実線で引く
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else{

				// 既に文字が設定済みの場合は、非授業日・休業日・学級閉鎖日の内容が出力されているためそちらを優先
				if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
					// 内容が空の場合のみ「出」を出力する
					replaceListFieldData(rowIndex,  permitdateColIndex, "退");
				}

				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
			}
		}
		//-----------------------------------------------------
		// 退学者(reg_mcode='40')
		//-----------------------------------------------------
		else if("40".equals(reg_mcode) &&											// 退学者(reg_mcode='40')である場合
				compareMonth(targetYearMonth, regStart) == 0 ){						// 学校を去った日(reg_start)が対象月である

			// 既に文字が設定済みの場合は、非授業日・休業日・学級閉鎖日の内容が出力されているためそちらを優先
			if(StringUtils.isEmpty(attendListFielddArray[regStartColIndex][rowIndex].getText()) || printFormBean.getDays()[regStartColIndex].getEnfoce() == 1){
				// 内容が空の場合のみ「出」を出力する
				replaceListFieldData(rowIndex,  regStartColIndex, "退");
			}

			outputLineHorizontalInBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}

	}

	/**
	 * 月末統計欄の出力
	 * @throws Exception
	 */
	private void outputStatistics() throws Exception{

		Map<String, Print32176000StatisticsFormBean> statisticsMap = printFormBean.getStatisticsMap();

		CrListField studentSumListListField = getCrListField(TOTAL_ATTEND);
		CrText[][] studentSumListListFieldArray = getListFieldGetColumns(studentSumListListField);

		int enforceDaySum = 0;			// 授業時数
		int holidaySum = 0;			// 授業を行わない日数
		int zaisekiStuSumPre = 0;			// 在籍者数
		int entryStuSum = 0;			// 異動・入学
		int leaveStuSum = 0;			// 異動・退学

		// 男:1、女:2
		String sex_male  = "1";
		String sex_female = "2";

		// 授業時数
		if(statisticsMap.get(sex_male).getEnforceNum() != null && 0 < statisticsMap.get(sex_male).getEnforceNum()) {

			enforceDaySum = statisticsMap.get(sex_male).getEnforceNum();
		}
		if(statisticsMap.get(sex_female).getEnforceNum() != null && 0 < statisticsMap.get(sex_female).getEnforceNum()) {
			enforceDaySum = statisticsMap.get(sex_female).getEnforceNum();
		}
		getFieldSetData(ENFORCE_DAY_SUM, enforceDaySum);

		// 授業を行わない日数
		if(statisticsMap.get(sex_male).getEnforceNum() != null && 0 < statisticsMap.get(sex_male).getEnforceNum()) {
			holidaySum = statisticsMap.get(sex_male).getHolidayNum();
		}
		if(statisticsMap.get(sex_female).getEnforceNum() != null && 0 < statisticsMap.get(sex_female).getEnforceNum()) {
			holidaySum = statisticsMap.get(sex_female).getHolidayNum();
		}
		getFieldSetData(HOLIDAY_SUM, holidaySum);

		// 計
		getFieldSetData(DAY_SUM, enforceDaySum + holidaySum);

		// 対象月が4月の場合は前月末現在在籍と差し引き今月末現在在籍を表示しない
		String targetYearMonth = printFormBean.getTargetYearMonth();
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");
		if(!targetMonth.equals("4")) {
			// 前月末在籍者数(男)
			setListFieldData(studentSumListListFieldArray, 0, 0, statisticsMap.get(sex_male).getZaisekiStuNumPre());
			// 前月末在籍者数(女)
			setListFieldData(studentSumListListFieldArray, 0, 1, statisticsMap.get(sex_female).getZaisekiStuNumPre());
			// 前月末在籍者数(合計)
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

		if(startColIndex > endColIndex){
			// 開始列が終了列と同じ場合、超える場合は処理を終了する
			return;
		}

		if(startColIndex == endColIndex){
			if(StringUtils.isEmpty(attendListFielddArray[startColIndex][rowIndex].getText())){
				outputLineHorizontal(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
			return;
		}

		// 線描画開始セル(CrText)の位置
		int startCol = startColIndex;

		// colIndexの位置には何らかの内容が出力されているためcol<=colIndexとしてループ内で判定を行う
		for(int col=startCol; col<=endColIndex; col++){

			if(!StringUtils.isEmpty(attendListFielddArray[col][rowIndex].getText())){
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

    /**
     * 出欠リストフィールド上に線を引く(横）
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

		if(startColIndex > endColIndex){
			// 開始列が終了列と同じ場合、超える場合は処理を終了する
			return;
		}

		if(startColIndex == endColIndex){
			if(StringUtils.isEmpty(attendListFielddArray[startColIndex][rowIndex].getText())){
				outputLineOblique(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
			return;
		}
		else{
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

	/**
	 * 非授業日・休業日・学級閉鎖日の実線を出力
	 * @throws Exception
	 */
	private void outputNotEnforcedayLine() throws Exception{

		Print32176000DayFormBean[] days = printFormBean.getDays();

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
					if(!StringUtils.isEmpty(attendListFielddArray[col][row].getText()) && !attendListFielddArray[col][row].getText().contains("退")){
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
     * 月を比較する
     * @param date1	月日1(YYYYMM or YYYYMMDD)
     * @param date2	月日2(YYYYMM or YYYYMMDD)
     * @return 月日1が月日2よりも前の月の場合は-1、月日1が月日2よりも後の月の場合は 1、同じ場合は0
     */
    private int compareMonth(String date1, String date2){

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Integer.parseInt(date1.substring(0,4)), Integer.parseInt(date1.substring(4,6))-1, 1);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Integer.parseInt(date2.substring(0,4)), Integer.parseInt(date2.substring(4,6))-1, 1);


		if(cal1.before(cal2)){
			// 月日1が月日2よりも前の月の場合は-1を返す
			return -1;
		}else if(cal1.after(cal2)){
			// 月日1が月日2よりも後の月の場合は 1を返す
			return 1;
		}

    	return 0;
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

		if(StringUtils.isEmpty(outputText)){
			// テキストの内容が空か、出席の場合は置換用文字列で置き換える
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText);
		}else if(printFormBean.getDays()[dayIndex].getEnfoce() == 1){
			// 出席データがある場合は出力内容をまとめる
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText + outputText);
		}
	}


	/**
	 * 出欠集計の「0」を置き換える
	 * @param attendCount
	 * @return
	 */
	private String replaceAttendZero(Integer value) {
		// 札幌は「0」の場合そのまま出力
//		if(value == 0){
//			return "・";
//		}
		return value.toString();
	}

	/**
	 * @param printFormBean the printFormBean to set
	 */
	public void setPrintFormBean(Print32176000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

}
