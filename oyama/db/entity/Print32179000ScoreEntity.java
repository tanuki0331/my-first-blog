package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * ���ђʒm�\���(���R�s_���w�Z) �w�K�̋L�^ Entity.
 * </PRE>
 *
 * <B>Create</B> 2017.07.12 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000ScoreEntity {
	/** �w�Дԍ� */
	private String clsStucode;

	/** ���ȃR�[�h */
	private String itemCode;

	/** �ʒm�\�p���Ȗ��� */
	private String itemName;

	/** �]��R�[�h */
	private String eval;

	/** �ϓ_�R�[�h */
	private String rivtcode;

	/** �ϓ_ */
	private String purpose;

	/** �ϓ_�]���l */
	private String viewpointvalue;

	/** �]���s�v�t���O�P */
	private String rvpe_cannot_flg;

	/** �]���s�v�t���O�Q */
	private String rvpe_none_flg;

	/**
	 * clsStucode ���擾����B
	 * @return clsStucode
	 */
	public final String getClsStucode() {
		return clsStucode;
	}

	/**
	 * clsStucode ��ݒ肷��B
	 * @param clsStucode �Z�b�g���� clsStucode
	 */
	public final void setClsStucode(String clsStucode) {
		this.clsStucode = clsStucode;
	}

	/**
	 * itemCode ���擾����B
	 * @return itemCode
	 */
	public final String getItemCode() {
		return itemCode;
	}

	/**
	 * itemCode ��ݒ肷��B
	 * @param itemCode �Z�b�g���� itemCode
	 */
	public final void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * itemName ���擾����B
	 * @return itemName
	 */
	public final String getItemName() {
		return itemName;
	}

	/**
	 * itemName ��ݒ肷��B
	 * @param itemName �Z�b�g���� itemName
	 */
	public final void setItemName(String itemName) {
		this.itemName = itemName;
	}


	/**
	 * eval ���擾����B
	 * @return eval
	 */
	public final String getEval() {
		return eval;
	}

	/**
	 * eval ��ݒ肷��B
	 * @param eval �Z�b�g���� eval
	 */
	public final void setEval(String eval) {
		this.eval = eval;
	}

	/**
	 * rivtcode ���擾����B
	 * @return rivtcode
	 */
	public final String getRivtcode() {
		return rivtcode;
	}

	/**
	 * rivtcode ��ݒ肷��B
	 * @param rivtcode �Z�b�g���� rivtcode
	 */
	public final void setRivtcode(String rivtcode) {
		this.rivtcode = rivtcode;
	}

	/**
	 * purpose ���擾����B
	 * @return purpose
	 */
	public final String getPurpose() {
		return purpose;
	}

	/**
	 * purpose ��ݒ肷��B
	 * @param purpose �Z�b�g���� purpose
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
	 * @param viewpointvalue �Z�b�g���� viewpointvalue
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
