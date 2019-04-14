/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 日付 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000DayFormBean  {

	/**
	 * 日付
	 */
	private Integer day;

	/**
	 * 授業日フラグ(授業日:1, 非授業日:0)
	 */
	private Integer enfoce;

	/**
	 * 曜日文字色
	 */
	private String weekDayColor;

	/**
	 * 休業日名称等の文言
	 */
	private StringBuilder text;

	/**
	 * 学級閉鎖判定
	 */
	private boolean closingclass = false;

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getEnfoce() {
		return enfoce;
	}

	public void setEnfoce(Integer enfoce) {
		this.enfoce = enfoce;
	}

	public String getWeekDayColor() {
		return weekDayColor;
	}

	public void setWeekDayColor(String weekDayColor) {
		this.weekDayColor = weekDayColor;
	}

	public StringBuilder getText() {
		return text;
	}

	public void setText(StringBuilder text) {
		this.text = text;
	}

	public boolean isClosingclass() {
		return closingclass;
	}

	public void setClosingclass(boolean closingclass) {
		this.closingclass = closingclass;
	}

}
