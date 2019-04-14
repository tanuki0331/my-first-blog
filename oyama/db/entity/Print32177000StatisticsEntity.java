/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  出席簿印刷(特別支援学級　小山市用) 月末統計情報 Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32177000StatisticsEntity  {

	/**
	 * 性別
	 */
	private String stu_sex;

	/**
	 * 授業日数
	 */
	private Integer enforceNum;

	/**
	 * 非授業日数
	 */
	private Integer holidayNum;

	/**
	 * 前月末時点の在籍者数
	 */
	private Integer zaisekiStuNumPre;

	/**
	 * 今月末時点の在籍者数
	 */
	private Integer zaisekiStuNum;

	/**
	 * 転編入者数
	 */
	private Integer entryStuNum;

	/**
	 * 転退学者数
	 */
	private Integer leaveStuNum;

	/**
	 * 仮退者数
	 */
	private Integer karitaiStuNum;

	/**
	 * 出席児童数を算出する際の「転退学児童数」
	 */
	private Integer leaveStuNumForAttend;

	public String getStu_sex() {
		return stu_sex;
	}

	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
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

	public Integer getZaisekiStuNumPre() {
		return zaisekiStuNumPre;
	}

	public void setZaisekiStuNumPre(Integer zaisekiStuNumPre) {
		this.zaisekiStuNumPre = zaisekiStuNumPre;
	}

	public Integer getZaisekiStuNum() {
		return zaisekiStuNum;
	}

	public void setZaisekiStuNum(Integer zaisekiStuNum) {
		this.zaisekiStuNum = zaisekiStuNum;
	}

	public Integer getEntryStuNum() {
		return entryStuNum;
	}

	public void setEntryStuNum(Integer entryStuNum) {
		this.entryStuNum = entryStuNum;
	}

	public Integer getLeaveStuNum() {
		return leaveStuNum;
	}

	public void setLeaveStuNum(Integer leaveStuNum) {
		this.leaveStuNum = leaveStuNum;
	}

	public Integer getLeaveStuNumForAttend() {
		return leaveStuNumForAttend;
	}

	public void setLeaveStuNumForAttend(Integer leaveStuNumForAttend) {
		this.leaveStuNumForAttend = leaveStuNumForAttend;
	}

	public Integer getKaritaiStuNum() {
		return karitaiStuNum;
	}

	public void setKaritaiStuNum(Integer karitaiStuNum) {
		this.karitaiStuNum = karitaiStuNum;
	}

}
