package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 特別活動の記録Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000SpActEntity {

	/** 学籍番号 */
	private String rsav_stucode;
	/** 出力時期 */
	private String rsav_term;
	/** 特別活動名称 */
	private String cod_name1;
	/** 特別活動の学習の記録 */
	private String rsav_record;

	public String getRsav_stucode() {
		return rsav_stucode;
	}
	public void setRsav_stucode(String rsav_stucode) {
		this.rsav_stucode = rsav_stucode;
	}
	public String getRsav_term() {
		return rsav_term;
	}
	public void setRsav_term(String rsav_term) {
		this.rsav_term = rsav_term;
	}
	public String getCod_name1() {
		return cod_name1;
	}
	public void setCod_name1(String cod_name1) {
		this.cod_name1 = cod_name1;
	}
	public String getRsav_record() {
		return rsav_record;
	}
	public void setRsav_record(String rsav_record) {
		this.rsav_record = rsav_record;
	}
}
