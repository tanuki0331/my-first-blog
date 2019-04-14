package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 総合所見 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000CommentEntity {

	/**
	 * 学籍番号
	 */
	private String rcom_stucode;

	/**
	 * 所見
	 */
	private String rcom_comment;

	public String getRcom_stucode() {
		return rcom_stucode;
	}

	public void setRcom_stucode(String rcom_stucode) {
		this.rcom_stucode = rcom_stucode;
	}

	public String getRcom_comment() {
		return rcom_comment;
	}

	public void setRcom_comment(String rcom_comment) {
		this.rcom_comment = rcom_comment;
	}


}
