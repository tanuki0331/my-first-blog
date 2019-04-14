/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  出席簿印刷(特別支援学級　小山市用) 月末統計情報(授業日数) Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32177000EnforceEntity  {

	/**
	 * 学年
	 */
	private String cls_glade;


	/**
	 * 授業日数
	 */
	private Integer enforceNum ;

	/**
	 * 非授業日数
	 */
	private Integer holidayNum;

	public String getCls_glade() {
		return cls_glade;
	}

	public void setCls_glade(String cls_glade) {
		this.cls_glade = cls_glade;
	}

	public Integer getEnforceNum() {
		return enforceNum;
	}

	public void setEnforceNum(Integer enforceNum) {
		this.enforceNum = enforceNum;
	}

	public Integer getHolidayNum() {
		return holidayNum;
	}

	public void setHolidayNum(Integer holidayNum) {
		this.holidayNum = holidayNum;
	}



}
