/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000AttendSummaryEntity;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) ���k��� FormBean.
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
	 * �w�Дԍ�
	 */
	private String stu_stucode;

	/**
	 * �����ԍ�
	 */
	private String cls_reference_number;

	/**
	 * ����
	 */
	private String stu_name;

	/**
	 * �o�����
	 */
	private String attend[];

	/**
	 * �o���W�v
	 */
	private Print32176000AttendSummaryEntity attendSummary;

	/**
	 * �]�ғ��J�n��
	 */
	private String entry_start;

	/**
	 * �ٓ��R�[�h
	 */
	private String reg_mcode;

	/**
	 * �ٓ��J�n�N����
	 */
	private String reg_start;

	/**
	 * ����
	 */
	private String reg_permitdate;

	/**
	 * �]�ފw������
	 */
	private String leave_date;

	/**
	 * �o��E�������t���O
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
