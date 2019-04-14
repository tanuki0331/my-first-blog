/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  【出欠管理】出席簿印刷(通常学級　小山市用) Form Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32176000FormBean  {

	/** 学校コード */
	private String user = "";

	/** 年度 */
	private String nendo = "";

	/** 表示年度 (和暦、または西暦年度)*/
	private String dispnendo = "";

	/** 年月 */
	private String yyyymm = "";

	/**所属コード*/
	private String department = "";

	/** 学年 */
	private String grade = "";

	/** 学年 tbl_hroom の最初の学年)	 * */
	private String gradeAtFirst = "";

	/**ホームルーム番号*/
	private String clsno = "";

	/**組*/
	private String hmrclass = "";

	/**組(tbl_hroom の最初のクラス)	 * */
	private String hmrclassAtFirst = "";

	/** クラス名 */
	private String hmrname = "";

	/** メッセージ */
	private String message =  "";

	/** 月選択用DorpDown */
	private String dropDownForMonth = "";

	/** メッセージ用 */
	private String nodata =  "";

	/** 出力月テーブルHTML */
	private String monthTableHtml = "";

	/** 出力対象月日数 */
	private int monthDayCount = 0;

	/**
	 *  戸籍・通称選択可能フラグ(cod_value2)
	 *  0:選択不可,
	 *  1:選択可能(通称デフォルト選択)
	 *  2:選択可能(戸籍デフォルト選択) ※レコードが無い、またはNULLである場合は「0:選択不可」として扱う
	 */
	private String outputNameFlg;

	/**
	 *  氏名の漢字・ふりがな選択可能フラグ(cod_name2)
	 *  0:ラジオボタン非表示
	 *  1:ラジオボタン表示　選択可能
	 */
	private String outputKanaFlg;

	/**
	 *  教務・担任遷移状態
	 *  ""   :教務
	 *  "CLS":担任
	 */
	private String tran_mode;

	// ----------------------------------------------
	// getter & setter
	// ----------------------------------------------
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNendo() {
		return nendo;
	}

	public void setNendo(String nendo) {
		this.nendo = nendo;
	}

	public String getDispnendo() {
		return dispnendo;
	}

	public void setDispnendo(String dispnendo) {
		this.dispnendo = dispnendo;
	}

	public String getYyyymm() {
		return yyyymm;
	}

	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeAtFirst() {
		return gradeAtFirst;
	}

	public void setGradeAtFirst(String gradeAtFirst) {
		this.gradeAtFirst = gradeAtFirst;
	}

	public String getClsno() {
		return clsno;
	}

	public void setClsno(String clsno) {
		this.clsno = clsno;
	}

	public String getHmrclass() {
		return hmrclass;
	}

	public void setHmrclass(String hmrclass) {
		this.hmrclass = hmrclass;
	}

	public String getHmrclassAtFirst() {
		return hmrclassAtFirst;
	}

	public void setHmrclassAtFirst(String hmrclassAtFirst) {
		this.hmrclassAtFirst = hmrclassAtFirst;
	}

	public String getHmrname() {
		return hmrname;
	}

	public void setHmrname(String hmrname) {
		this.hmrname = hmrname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDropDownForMonth() {
		return dropDownForMonth;
	}

	public void setDropDownForMonth(String dropDownForMonth) {
		this.dropDownForMonth = dropDownForMonth;
	}

	public String getNodata() {
		return nodata;
	}

	public void setNodata(String nodata) {
		this.nodata = nodata;
	}

	public String getMonthTableHtml() {
		return monthTableHtml;
	}

	public void setMonthTableHtml(String monthTableHtml) {
		this.monthTableHtml = monthTableHtml;
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

	public String getTran_mode() {
		return tran_mode;
	}

	public void setTran_mode(String tran_mode) {
		this.tran_mode = tran_mode;
	}

}
