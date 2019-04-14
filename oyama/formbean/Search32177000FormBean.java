/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;


/**
 * <PRE>
 *  【出欠管理】出席簿印刷(特別支援学級　小山市用) Form Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32177000FormBean implements AttPrintSearchFormBeanImpl {

	/** 学級タイプ */
	private String classType = "";

	/** 年度 */
	private String nendo = "";

	/** 学年 */
	private String grade = "";

	/** 組 */
	private String hmrclass = "";

	/** クラス番号 */
	private String clsno = "";

	/** グループコード */
	private String groupcode = "";

	/** グループ名称 */
	private String groupname = "";

	/** 組リスト */
	private List<SimpleTagFormBean> hroomSelectboxList;

	/** グループコードリスト */
	private List<SimpleTagFormBean> groupSelectboxList;

	/** 月 */
	private String month = "";

	/** 月リスト(セレクトボックス用) */
	private List<SimpleTagFormBean> monthSelectboxList;

	/** 空枠印刷チェック */
	private String blankFlg = "";

	/** 交流学級を含む・含まない
	 * 1:含む
	 * 2:含まない
	 */
	private String excFlg = "";

	/** 出欠区分マスタ設定数 */
	private Integer mstAttendkindJrCount = 0;

	/** 出力対象月日数 */
	private int monthDayCount = 0;

	/**
	 *  戸籍・通称選択可能フラグ(cod_value2)
	 *  0:選択不可(ラジオボタン非表示)
	 *  1:選択可能(通称デフォルト選択)
	 *  2:選択可能(戸籍デフォルト選択)
	 *  ※レコードが無い、またはNULLである場合は「0:選択不可」として扱う
	 */
	private String outputNameFlg;

	/**
	 *  氏名の漢字・ふりがな選択可能フラグ(cod_name2)
	 *  0:選択不可(ラジオボタン非表示)
	 *  1:選択可能(漢字デフォルト選択)
	 *  2:選択可能(かなデフォルト選択)
	 */
	private String outputKanaFlg;

	/**
	 * 日付情報Map
	 */
	private Map<Integer, AttPrintDayFormBean> dayMap;

	/**
	 *  教務・担任遷移状態
	 *  "KYOMU":教務
	 */
	private String tranMode;

	/**
	 * 休業日基準学年
	 */
	private String holidayGrade;

	/**
	 * 休業日基準学年リスト
	 */
	private List<SimpleTagFormBean> holidayGradeSelectboxList;

	/**
	 * 対象年月（yyyymm）
	 */
	private String yyyymm;

	/**
	 * 画面表示メッセージ
	 */
	private String message = "";

	public String getNendo() {
		return nendo;
	}

	public void setNendo(String nendo) {
		this.nendo = nendo;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHmrclass() {
		return hmrclass;
	}

	public void setHmrclass(String hmrclass) {
		this.hmrclass = hmrclass;
	}

	public String getClsno() {
		return clsno;
	}

	public void setClsno(String clsno) {
		this.clsno = clsno;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public List<SimpleTagFormBean> getHroomSelectboxList() {
		return hroomSelectboxList;
	}

	public void setHroomSelectboxList(List<SimpleTagFormBean> hroomSelectboxList) {
		this.hroomSelectboxList = hroomSelectboxList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<SimpleTagFormBean> getMonthSelectboxList() {
		return monthSelectboxList;
	}

	public void setMonthSelectboxList(List<SimpleTagFormBean> monthSelectboxList) {
		this.monthSelectboxList = monthSelectboxList;
	}

	public String getBlankFlg() {
		return blankFlg;
	}

	public void setBlankFlg(String blankFlg) {
		this.blankFlg = blankFlg;
	}

	public String getExcFlg() {
		return excFlg;
	}

	public void setExcFlg(String excFlg) {
		this.excFlg = excFlg;
	}

	public Integer getMstAttendkindJrCount() {
		return mstAttendkindJrCount;
	}

	public void setMstAttendkindJrCount(Integer mstAttendkindJrCount) {
		this.mstAttendkindJrCount = mstAttendkindJrCount;
	}

	public int getMonthDayCount() {
		return monthDayCount;
	}

	public void setMonthDayCount(int monthDayCount) {
		this.monthDayCount = monthDayCount;
	}

	public String getOutputNameFlg() {
		return outputNameFlg;
	}

	public void setOutputNameFlg(String outputNameFlg) {
		this.outputNameFlg = outputNameFlg;
	}

	public String getOutputKanaFlg() {
		return outputKanaFlg;
	}

	public void setOutputKanaFlg(String outputKanaFlg) {
		this.outputKanaFlg = outputKanaFlg;
	}




	public String getTranMode() {
		return tranMode;
	}

	public void setTranMode(String tranMode) {
		this.tranMode = tranMode;
	}

	public String getHolidayGrade() {
		return holidayGrade;
	}

	public void setHolidayGrade(String holidayGrade) {
		this.holidayGrade = holidayGrade;
	}

	public Map<Integer, AttPrintDayFormBean> getDayMap() {
		return dayMap;
	}

	public void setDayMap(Map<Integer, AttPrintDayFormBean> dayMap) {
		this.dayMap = dayMap;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public List<SimpleTagFormBean> getGroupSelectboxList() {
		return groupSelectboxList;
	}

	public void setGroupSelectboxList(List<SimpleTagFormBean> groupSelectboxList) {
		this.groupSelectboxList = groupSelectboxList;
	}

	public List<SimpleTagFormBean> getHolidayGradeSelectboxList() {
		return holidayGradeSelectboxList;
	}

	public void setHolidayGradeSelectboxList(List<SimpleTagFormBean> holidayGradeSelectboxList) {
		this.holidayGradeSelectboxList = holidayGradeSelectboxList;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getYyyymm() {
		return yyyymm;
	}

	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
