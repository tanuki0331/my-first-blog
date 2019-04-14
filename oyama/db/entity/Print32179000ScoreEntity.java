package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 学習の記録 Entity.
 * </PRE>
 *
 * <B>Create</B> 2017.07.12 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000ScoreEntity {
	/** 学籍番号 */
	private String clsStucode;

	/** 教科コード */
	private String itemCode;

	/** 通知表用教科名称 */
	private String itemName;

	/** 評定コード */
	private String eval;

	/** 観点コード */
	private String rivtcode;

	/** 観点 */
	private String purpose;

	/** 観点評価値 */
	private String viewpointvalue;

	/** 評価不要フラグ１ */
	private String rvpe_cannot_flg;

	/** 評価不要フラグ２ */
	private String rvpe_none_flg;

	/**
	 * clsStucode を取得する。
	 * @return clsStucode
	 */
	public final String getClsStucode() {
		return clsStucode;
	}

	/**
	 * clsStucode を設定する。
	 * @param clsStucode セットする clsStucode
	 */
	public final void setClsStucode(String clsStucode) {
		this.clsStucode = clsStucode;
	}

	/**
	 * itemCode を取得する。
	 * @return itemCode
	 */
	public final String getItemCode() {
		return itemCode;
	}

	/**
	 * itemCode を設定する。
	 * @param itemCode セットする itemCode
	 */
	public final void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * itemName を取得する。
	 * @return itemName
	 */
	public final String getItemName() {
		return itemName;
	}

	/**
	 * itemName を設定する。
	 * @param itemName セットする itemName
	 */
	public final void setItemName(String itemName) {
		this.itemName = itemName;
	}


	/**
	 * eval を取得する。
	 * @return eval
	 */
	public final String getEval() {
		return eval;
	}

	/**
	 * eval を設定する。
	 * @param eval セットする eval
	 */
	public final void setEval(String eval) {
		this.eval = eval;
	}

	/**
	 * rivtcode を取得する。
	 * @return rivtcode
	 */
	public final String getRivtcode() {
		return rivtcode;
	}

	/**
	 * rivtcode を設定する。
	 * @param rivtcode セットする rivtcode
	 */
	public final void setRivtcode(String rivtcode) {
		this.rivtcode = rivtcode;
	}

	/**
	 * purpose を取得する。
	 * @return purpose
	 */
	public final String getPurpose() {
		return purpose;
	}

	/**
	 * purpose を設定する。
	 * @param purpose セットする purpose
	 */
	public final void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	/**
	 * @return viewpointvalue
	 */
	public String getViewpointvalue() {
		return viewpointvalue;
	}

	/**
	 * @param viewpointvalue セットする viewpointvalue
	 */
	public void setViewpointvalue(String viewpointvalue) {
		this.viewpointvalue = viewpointvalue;
	}

	public String getRvpe_cannot_flg() {
		return rvpe_cannot_flg;
	}

	public void setRvpe_cannot_flg(String rvpe_cannot_flg) {
		this.rvpe_cannot_flg = rvpe_cannot_flg;
	}

	public String getRvpe_none_flg() {
		return rvpe_none_flg;
	}

	public void setRvpe_none_flg(String rvpe_none_flg) {
		this.rvpe_none_flg = rvpe_none_flg;
	}
}
