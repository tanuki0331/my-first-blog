/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 出欠集計 情報 Entity Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000AttendSummaryEntity  {

	/**
	 * 学籍番号
	 */
	private String cls_stucode;

	/**
	 * 性別
	 */
	private String stu_sex;

	/**
	 * 授業日数
	 */
	private Integer classCount = 0;

	/**
	 * 出停・忌引等日数
	 */
	private Integer schoolkindCount = 0;

	/**
	 * 忌引日数
	 */
	private Integer kibikiCount = 0;

	/**
	 * 出停日数
	 */
	private Integer stopCount = 0;

	/**
	 * 出席しなければならない日数
	 */
	private Integer mustCount = 0;

	/**
	 * 欠席日数
	 */
	private Integer absenceCount = 0;

	/**
	 * 病欠日数
	 */
	private Integer sickCount = 0;

	/**
	 * 事故欠(その他)日数
	 */
	private Integer accidentCount = 0;

	/**
	 * 出席日数
	 */
	private Integer attendCount = 0;

	/**
	 * 遅刻日数
	 */
	private Integer lateCount = 0;

	/**
	 * 早退日数
	 */
	private Integer leaveCount = 0;

	/**
	 * 備考
	 */
	private String atc_comment = "";

	public String getCls_stucode() {
		return cls_stucode;
	}

	public void setCls_stucode(String cls_stucode) {
		this.cls_stucode = cls_stucode;
	}

	public String getStu_sex() {
		return stu_sex;
	}

	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
	}

	public Integer getClassCount() {
		return classCount;
	}

	public void setClassCount(Integer classCount) {
		this.classCount = classCount;
	}

	public Integer getSchoolkindCount() {
		return schoolkindCount;
	}

	public void setSchoolkindCount(Integer schoolkindCount) {
		this.schoolkindCount = schoolkindCount;
	}

	public Integer getKibikiCount() {
		return kibikiCount;
	}

	public void setKibikiCount(Integer kibikiCount) {
		this.kibikiCount = kibikiCount;
	}

	public Integer getStopCount() {
		return stopCount;
	}

	public void setStopCount(Integer stopCount) {
		this.stopCount = stopCount;
	}

	public Integer getMustCount() {
		return mustCount;
	}

	public void setMustCount(Integer mustCount) {
		this.mustCount = mustCount;
	}

	public Integer getAbsenceCount() {
		return absenceCount;
	}

	public void setAbsenceCount(Integer absenceCount) {
		this.absenceCount = absenceCount;
	}

	public Integer getSickCount() {
		return sickCount;
	}

	public void setSickCount(Integer sickCount) {
		this.sickCount = sickCount;
	}

	public Integer getAccidentCount() {
		return accidentCount;
	}

	public void setAccidentCount(Integer accidentCount) {
		this.accidentCount = accidentCount;
	}

	public Integer getAttendCount() {
		return attendCount;
	}

	public void setAttendCount(Integer attendCount) {
		this.attendCount = attendCount;
	}

	public Integer getLateCount() {
		return lateCount;
	}

	public void setLateCount(Integer lateCount) {
		this.lateCount = lateCount;
	}

	public Integer getLeaveCount() {
		return leaveCount;
	}

	public void setLeaveCount(Integer leaveCount) {
		this.leaveCount = leaveCount;
	}

	public String getAtc_comment() {
		return atc_comment;
	}

	public void setAtc_comment(String atc_comment) {
		this.atc_comment = atc_comment;
	}


}
