/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) �]�ғ���� Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000EntryStudentEntity  {

	/**
	 * �w�Дԍ�
	 */
	private String stu_stucode;

	/**
	 * �]�ғ���
	 */
	private String stu_entry;

	/**
	 * �]���O�̊w�Z��
	 */
	private String st4_preschname;

	/**
	 * �]�ғ��҃t���O
	 * �]����:3
	 * �ғ���:4
	 */
	private String stu_qualifi;

	public String getStu_stucode() {
		return stu_stucode;
	}

	public void setStu_stucode(String stu_stucode) {
		this.stu_stucode = stu_stucode;
	}

	public String getStu_entry() {
		return stu_entry;
	}

	public void setStu_entry(String stu_entry) {
		this.stu_entry = stu_entry;
	}

	public String getSt4_preschname() {
		return st4_preschname;
	}

	public void setSt4_preschname(String st4_preschname) {
		this.st4_preschname = st4_preschname;
	}

	public String getStu_qualifi() {
		return stu_qualifi;
	}

	public void setStu_qualifi(String stu_qualifi) {
		this.stu_qualifi = stu_qualifi;
	}

}
