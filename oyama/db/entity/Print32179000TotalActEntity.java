package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 総合的な学習の時間 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000TotalActEntity {

	/**
	 * 学籍番号
	 */
	private String rtav_stucode;

	/**
	 * 連番
	 */
	private String rtav_rtavcode;

	/**
	 * 観点
	 */
	private String rtav_value;

	/**
	 * 学習活動
	 */
	private String rtav_contents;


	public String getRtav_stucode() {
		return rtav_stucode;
	}

	public void setRtav_stucode(String rtav_stucode) {
		this.rtav_stucode = rtav_stucode;
	}

	public String getRtav_rtavcode() {
		return rtav_rtavcode;
	}

	public void setRtav_rtavcode(String rtav_rtavcode) {
		this.rtav_rtavcode = rtav_rtavcode;
	}

	public String getRtav_value() {
		return rtav_value;
	}

	public void setRtav_value(String rtav_value) {
		this.rtav_value = rtav_value;
	}

	public String getRtav_contents() {
		return rtav_contents;
	}

	public void setRtav_contents(String rtav_contents) {
		this.rtav_contents = rtav_contents;
	}

}
