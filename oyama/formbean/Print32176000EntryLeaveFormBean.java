/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 転編入・転退学 情報 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000EntryLeaveFormBean  {

	/**
	 * 学籍番号
	 */
	private String stu_stucode;

	/**
	 * 月日
	 */
	private String date;

	/**
	 * 氏名
	 */
	private String stu_name;


	/**
	 * 学校名
	 */
	private String sch_name;

	/**
	 * 転編入者及び転退学者のフラグ
	 */
	private String stu_flg;
	/**
	 * コンストラクタ
	 * @param stu_stucode
	 * @param date
	 * @param stu_name
	 * @param sch_name
	 */
	public Print32176000EntryLeaveFormBean(String stu_stucode, String date, String stu_name, String sch_name, String stu_flg) {
		this.stu_stucode = stu_stucode;
		this.date = date;
		this.stu_name = stu_name;
		this.sch_name = sch_name;
		this.stu_flg = stu_flg;
	}


	public String getStu_stucode() {
		return stu_stucode;
	}


	public void setStu_stucode(String stu_stucode) {
		this.stu_stucode = stu_stucode;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getStu_name() {
		return stu_name;
	}


	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}


	public String getSch_name() {
		return sch_name;
	}


	public void setSch_name(String sch_name) {
		this.sch_name = sch_name;
	}


	public String getStu_flg() {
		return stu_flg;
	}


	public void setStu_flg(String stu_flg) {
		this.stu_flg = stu_flg;
	}



}
