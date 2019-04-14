package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 出欠の記録 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000AttendEntity {

	/** 学籍番号 */
	private String stucode;

	/** 出力時期 */
	private String goptcode;

	/** 観点 */
	private String purpose;

	/** 年 */
	private String year;

	/** 月 */
	private String month;

	/** 在籍フラグ 0:この出力時期に在籍していない, 1:この出力時期に在籍している */
	private String onRegist;

	/** 授業日数 */
	private Integer teachingSum;

	/** 出席日数 */
	private Integer attendSum;

	/** 欠席日数 */
	private Integer absenceSum;

	/** 出停・忌引日数 */
	private Integer stopSum;

	/** 遅刻日数 */
	private Integer lateSum;

	/** 早退日数 */
	private Integer leaveSum;

	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	public String getGoptcode() {
		return goptcode;
	}

	public void setGoptcode(String goptcode) {
		this.goptcode = goptcode;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getOnRegist() {
		return onRegist;
	}

	public void setOnRegist(String onRegist) {
		this.onRegist = onRegist;
	}

	public Integer getTeachingSum() {
		return teachingSum;
	}

	public void setTeachingSum(Integer teachingSum) {
		this.teachingSum = teachingSum;
	}

	public Integer getAttendSum() {
		return attendSum;
	}

	public void setAttendSum(Integer attendSum) {
		this.attendSum = attendSum;
	}

	public Integer getAbsenceSum() {
		return absenceSum;
	}

	public void setAbsenceSum(Integer absenceSum) {
		this.absenceSum = absenceSum;
	}

	public Integer getStopSum() {
		return stopSum;
	}

	public void setStopSum(Integer stopSum) {
		this.stopSum = stopSum;
	}

	public Integer getLateSum() {
		return lateSum;
	}

	public void setLateSum(Integer lateSum) {
		this.lateSum = lateSum;
	}

	public Integer getLeaveSum() {
		return leaveSum;
	}

	public void setLeaveSum(Integer leaveSum) {
		this.leaveSum = leaveSum;
	}
}
