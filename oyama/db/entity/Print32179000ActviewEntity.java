package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 学校生活のようすEntity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000ActviewEntity {

	/** 学籍番号 */
	private String stucode;

	/** 出力時期コード */
	private String ravtterm;

	/** 指導要録 項目コード */
	private String gavtcode;

	/** 項目コード */
	private String ravtcode;

	/** 観点名称 */
	private String ravtname;

	/** 観点の趣旨・学習のめあて */
	private String purpose;

	/** 評価値コード */
	private String racecode;

	/** 評価値(通知表表示用) */
	private String reportdisplay;


	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	public String getRavtterm() {
		return ravtterm;
	}

	public void setRavtterm(String ravtterm) {
		this.ravtterm = ravtterm;
	}

	public String getGavtcode() {
		return gavtcode;
	}

	public void setGavtcode(String gavtcode) {
		this.gavtcode = gavtcode;
	}

	public String getRavtcode() {
		return ravtcode;
	}

	public void setRavtcode(String ravtcode) {
		this.ravtcode = ravtcode;
	}

	public String getRavtname() {
		return ravtname;
	}

	public void setRavtname(String ravtname) {
		this.ravtname = ravtname;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRacecode() {
		return racecode;
	}

	public void setRacecode(String racecode) {
		this.racecode = racecode;
	}

	public String getReportdisplay() {
		return reportdisplay;
	}

	public void setReportdisplay(String reportdisplay) {
		this.reportdisplay = reportdisplay;
	}





}
