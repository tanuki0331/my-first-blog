/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) ���t FormBean.
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
	 * ���t
	 */
	private Integer day;

	/**
	 * ���Ɠ��t���O(���Ɠ�:1, ����Ɠ�:0)
	 */
	private Integer enfoce;

	/**
	 * �j�������F
	 */
	private String weekDayColor;

	/**
	 * �x�Ɠ����̓��̕���
	 */
	private StringBuilder text;

	/**
	 * �w��������
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
