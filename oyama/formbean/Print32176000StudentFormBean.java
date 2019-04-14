/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000AttendSummaryEntity;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 生徒情報 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000StudentFormBean  {

	/**
	 * 学籍番号
	 */
	private String stu_stucode;

	/**
	 * 整理番号
	 */
	private String cls_reference_number;

	/**
	 * 氏名
	 */
	private String stu_name;

	/**
	 * 出欠情報
	 */
	private String attend[];

	/**
	 * 出欠集計
	 */
	private Print32176000AttendSummaryEntity attendSummary;

	/**
	 * 転編入開始日
	 */
	private String entry_start;

	/**
	 * 異動コード
	 */
	private String reg_mcode;

	/**
	 * 異動開始年月日
	 */
	private String reg_start;

	/**
	 * 許可日
	 */
	private String reg_permitdate;

	/**
	 * 転退学判定基準日
	 */
	private String leave_date;

	/**
	 * 出停・忌引きフラグ
	 */
	private String stopKibikiFlg[];


	public String getStu_stucode() {
		return stu_stucode;
	}

	public void setStu_stucode(String stu_stucode) {
		this.stu_stucode = stu_stucode;
	}

	public String getCls_reference_number() {
		return cls_reference_number;
	}

	public void setCls_reference_number(String cls_reference_number) {
		this.cls_reference_number = cls_reference_number;
	}

	public String getStu_name() {
		return stu_name;
	}

	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}

	public String[] getAttend() {
		return attend;
	}

	public void setAttend(String[] attend) {
		this.attend = attend;
	}

	public Print32176000AttendSummaryEntity getAttendSummary() {
		return attendSummary;
	}

	public void setAttendSummary(Print32176000AttendSummaryEntity attendSummary) {
		this.attendSummary = attendSummary;
	}

	public String getEntry_start() {
		return entry_start;
	}

	public void setEntry_start(String entry_start) {
		this.entry_start = entry_start;
	}

	public String getReg_mcode() {
		return reg_mcode;
	}

	public void setReg_mcode(String reg_mcode) {
		this.reg_mcode = reg_mcode;
	}

	public String getReg_start() {
		return reg_start;
	}

	public void setReg_start(String reg_start) {
		this.reg_start = reg_start;
	}

	public String getReg_permitdate() {
		return reg_permitdate;
	}

	public void setReg_permitdate(String reg_permitdate) {
		this.reg_permitdate = reg_permitdate;
	}

	public String getLeave_date() {
		return leave_date;
	}

	public void setLeave_date(String leave_date) {
		this.leave_date = leave_date;
	}

	public String[] getStopKibikiFlg() {
		return stopKibikiFlg;
	}

	public void setStopKibikiFlg(String[] stopKibikiFlg) {
		this.stopKibikiFlg = stopKibikiFlg;
	}


}
