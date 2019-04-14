package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 外国語活動Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000ForeignLangActEntity {

	/**
	 * 学籍番号
	 */
	private String cls_stucode;

	/**
	 * 学年
	 */
	private String cls_glade;

	/**
	 * 出力時期
	 */
	private String rivt_term;

	/**
	 * 観点ID
	 */
	private String rivt_rivtcode;

	/**
	 * 観点名称
	 */
	private String rivt_rivtname2;

	/**
	 * 観点の趣旨・学習のめあて
	 */
	private String rivt_purpose;

	/**
	 * 表示順
	 */
	private String rivt_order;

	/**
	 * 評価値ID
	 */
	private String rfla_estimate;

	/**
	 * 各観点評価
	 */
	private String rfle_reportdisplay;

	/**
	 * 記述評価
	 */
	private String rfla_eval;

	public String getCls_stucode() {
		return cls_stucode;
	}

	public void setCls_stucode(String cls_stucode) {
		this.cls_stucode = cls_stucode;
	}

	public String getCls_glade() {
		return cls_glade;
	}

	public void setCls_glade(String cls_glade) {
		this.cls_glade = cls_glade;
	}

	public String getRivt_term() {
		return rivt_term;
	}

	public void setRivt_term(String rivt_term) {
		this.rivt_term = rivt_term;
	}

	public String getRivt_rivtcode() {
		return rivt_rivtcode;
	}

	public void setRivt_rivtcode(String rivt_rivtcode) {
		this.rivt_rivtcode = rivt_rivtcode;
	}

	public String getRivt_rivtname2() {
		return rivt_rivtname2;
	}

	public void setRivt_rivtname2(String rivt_rivtname2) {
		this.rivt_rivtname2 = rivt_rivtname2;
	}

	public String getRivt_purpose() {
		return rivt_purpose;
	}

	public void setRivt_purpose(String rivt_purpose) {
		this.rivt_purpose = rivt_purpose;
	}

	public String getRivt_order() {
		return rivt_order;
	}

	public void setRivt_order(String rivt_order) {
		this.rivt_order = rivt_order;
	}

	public String getRfla_estimate() {
		return rfla_estimate;
	}

	public void setRfla_estimate(String rfla_estimate) {
		this.rfla_estimate = rfla_estimate;
	}

	public String getRfle_reportdisplay() {
		return rfle_reportdisplay;
	}

	public void setRfle_reportdisplay(String rfle_reportdisplay) {
		this.rfle_reportdisplay = rfle_reportdisplay;
	}

	public String getRfla_eval() {
		return rfla_eval;
	}

	public void setRfla_eval(String rfla_eval) {
		this.rfla_eval = rfla_eval;
	}



}
