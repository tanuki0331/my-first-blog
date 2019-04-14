package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷  健康の記録 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000HealthrecordEntity {

	/**
	 * 学籍番号
	 */
	private String hlr_stucode;

	/**
	 * 出力時期コード
	 */
	private String hlr_goptcode;

	/**
	 * 身長
	 */
	private String hlr_height;

	/**
	 * 体重
	 */
	private String hlr_weight;

	/**
	 * 視力-左
	 */
	private String hlr_v_left;

	/**
	 * 視力-右
	 */
	private String hlr_v_right;


	public String getHlr_stucode() {
		return hlr_stucode;
	}

	public void setHlr_stucode(String hlr_stucode) {
		this.hlr_stucode = hlr_stucode;
	}

	public String getHlr_goptcode() {
		return hlr_goptcode;
	}

	public void setHlr_goptcode(String hlr_goptcode) {
		this.hlr_goptcode = hlr_goptcode;
	}

	public String getHlr_height() {
		return hlr_height;
	}

	public void setHlr_height(String hlr_height) {
		this.hlr_height = hlr_height;
	}

	public String getHlr_weight() {
		return hlr_weight;
	}

	public void setHlr_weight(String hlr_weight) {
		this.hlr_weight = hlr_weight;
	}

	public String getHlr_v_left() {
		return hlr_v_left;
	}

	public void setHlr_v_left(String hlr_v_left) {
		this.hlr_v_left = hlr_v_left;
	}

	public String getHlr_v_right() {
		return hlr_v_right;
	}

	public void setHlr_v_right(String hlr_v_right) {
		this.hlr_v_right = hlr_v_right;
	}



}
