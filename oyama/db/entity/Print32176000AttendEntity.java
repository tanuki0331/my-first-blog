/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) �o����� Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000AttendEntity  {

	/**
	 * �w�Дԍ�
	 */
	private String cls_stucode;

	/**
	 * ���t
	 */
	private String att_date;

	/**
	 * �\���L��
	 */
	private String display;

	/**
	 * �o��E�������t���O
	 */
	private String stopKibikiFlg;

	public String getCls_stucode() {
		return cls_stucode;
	}

	public void setCls_stucode(String cls_stucode) {
		this.cls_stucode = cls_stucode;
	}

	public String getAtt_date() {
		return att_date;
	}

	public void setAtt_date(String att_date) {
		this.att_date = att_date;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getStopKibikiFlg() {
		return stopKibikiFlg;
	}

	public void setStopKibikiFlg(String stopKibikiFlg) {
		this.stopKibikiFlg = stopKibikiFlg;
	}

}
