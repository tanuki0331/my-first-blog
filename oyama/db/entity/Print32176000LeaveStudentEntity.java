/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) �]�ފw��� Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000LeaveStudentEntity  {

	/**
	 * �w�Дԍ�
	 */
	private String reg_stucode;

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
	 * �]�o��̊w�Z��
	 */
	private String st4_anotherschname;

	public String getReg_stucode() {
		return reg_stucode;
	}

	public void setReg_stucode(String reg_stucode) {
		this.reg_stucode = reg_stucode;
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

	public String getSt4_anotherschname() {
		return st4_anotherschname;
	}

	public void setSt4_anotherschname(String st4_anotherschname) {
		this.st4_anotherschname = st4_anotherschname;
	}


}
