package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 * ���ђʒm�\���(���R�s_���w�Z) ��� �o���̋L�^ Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32179000AttendEntity {

	/** �w�Дԍ� */
	private String stucode;

	/** �o�͎��� */
	private String goptcode;

	/** �ϓ_ */
	private String purpose;

	/** �N */
	private String year;

	/** �� */
	private String month;

	/** �ݐЃt���O 0:���̏o�͎����ɍݐЂ��Ă��Ȃ�, 1:���̏o�͎����ɍݐЂ��Ă��� */
	private String onRegist;

	/** ���Ɠ��� */
	private Integer teachingSum;

	/** �o�ȓ��� */
	private Integer attendSum;

	/** ���ȓ��� */
	private Integer absenceSum;

	/** �o��E�������� */
	private Integer stopSum;

	/** �x������ */
	private Integer lateSum;

	/** ���ޓ��� */
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
