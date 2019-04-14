/**------------------------------------------------------------**
 * Te@cherNavi
 *  Copyright(C) 2019 SystemD inc., All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

import jp.co.systemd.tnavi.common.db.constants.CommonConstantsUseable;

/**
 * <PRE>
 * 	�o���Ȉꗗ�o�́@�o���ȏ��擾 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY aivick<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Print32172000_AttendEntity implements CommonConstantsUseable {


	/**
     * �w�N
     */
    private String grade;

    /**
     * �g
     */
    private String hmr_class;

    /**
     * �N���X�A�O���[�v
     */
    private String cls_group;

    /**
     * �o�Ȕԍ�(��)
     */
    private String cls_reference_number;

    /**
     * ���k�R�[�h
     */
    private String stucode;

    /**
     * ���k��
     */
    private String student_name;

    /**
     * �N
     */
    private String enf_year;

    /**
     * ��
     */
    private String enf_month;

    /**
     * ���Ɠ���(����Ɠ��敪�̍��������A�ǉ����Ɠ��敪�����Z��View���ŏ�����)
     */
    private String classCount;

    /**
     * �o��E����������
     */
    private String schoolkindCount;

    /**
     * ���ȓ���
     */
    private String absenceCount;

    /**
     * �a������
     */
    private String sickCount;

    /**
     * ���̌�����
     */
    private String accidentCount;

    /**
     * �o�ȓ��� (���Ɠ��� - �o��E���������� - ���ȓ���)
     */
    private String attendCount;

    /**
     * �x������
     */
    private String lateCount;

    /**
     * ���ޓ���
     */
    private String leaveCount;



	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHmr_class() {
		return hmr_class;
	}

	public void setHmr_class(String hmr_class) {
		this.hmr_class = hmr_class;
	}

	public String getCls_group() {
		return cls_group;
	}

	public void setCls_group(String cls_group) {
		this.cls_group = cls_group;
	}

	public String getCls_reference_number() {
		return cls_reference_number;
	}

	public void setCls_reference_number(String cls_reference_number) {
		this.cls_reference_number = cls_reference_number;
	}

	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	public String getStudent_name() {
		return student_name;
	}

	public void setStudent_name(String student_name) {
		this.student_name = student_name;
	}

	public String getEnf_year() {
		return enf_year;
	}

	public void setEnf_year(String enf_year) {
		this.enf_year = enf_year;
	}

	public String getEnf_month() {
		return enf_month;
	}

	public void setEnf_month(String enf_month) {
		this.enf_month = enf_month;
	}

	public String getClassCount() {
		return classCount;
	}

	public void setClassCount(String classCount) {
		this.classCount = classCount;
	}

	public String getSchoolkindCount() {
		return schoolkindCount;
	}

	public void setSchoolkindCount(String schoolkindCount) {
		this.schoolkindCount = schoolkindCount;
	}

	public String getAbsenceCount() {
		return absenceCount;
	}

	public void setAbsenceCount(String absenceCount) {
		this.absenceCount = absenceCount;
	}

	public String getSickCount() {
		return sickCount;
	}

	public void setSickCount(String sickCount) {
		this.sickCount = sickCount;
	}

	public String getAccidentCount() {
		return accidentCount;
	}

	public void setAccidentCount(String accidentCount) {
		this.accidentCount = accidentCount;
	}

	public String getAttendCount() {
		return attendCount;
	}

	public void setAttendCount(String attendCount) {
		this.attendCount = attendCount;
	}

	public String getLateCount() {
		return lateCount;
	}

	public void setLateCount(String lateCount) {
		this.lateCount = lateCount;
	}

	public String getLeaveCount() {
		return leaveCount;
	}

	public void setLeaveCount(String leaveCount) {
		this.leaveCount = leaveCount;
	}




}