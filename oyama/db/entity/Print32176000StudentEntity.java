/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) ���k��� Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000StudentEntity  {

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
	 * ����
	 */
	private String stu_kana;

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

	public String getStu_kana() {
		return stu_kana;
	}

	public void setStu_kana(String stu_kana) {
		this.stu_kana = stu_kana;
	}


}
