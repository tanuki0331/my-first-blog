package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.ArrayList;
import java.util.List;

import jp.co.systemd.tnavi.common.db.entity.SemesterEntity;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_TargetMonthEntity;

/**
 * <PRE>
 * 	�o���Ȉꗗ�o�� ����FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Search32172000FormBean {

	/** �J�ڃ��[�h **/
	private String transitionMode = "";

	/** �����[�h **/
	private String reload = "";

	/** �N�x **/
	private String nendo = "";

	/** �w�� */
	private String semCode = "";

	/** �w���J�n�� */
	private String semStart = "";

	/** �w���I���� */
	private String semEnd = "";

	/** �w���� */
	private String semName = "";

	/** �o�͌��̑I����� */
	private String[] checkedTargetMonth = new String[12];

	/** �w�N **/
	private String grade = "";

	/** �N���X�ԍ� **/
	private String hmrClsno = "";

	/** �g **/
	private String hmrClass = "";

	/** �g�� **/
	private String hmrName = "";


	/**
     * �N���X�v���_�E�����X�g
     */
    private List<SimpleTagFormBean> classList = new ArrayList<SimpleTagFormBean>();

    /** �w�����X�g **/
	private List<SemesterEntity> semesterEntityList;

	/**
     * �w���v���_�E�����X�g
     */
    private List<SimpleTagFormBean> semesterList = new ArrayList<SimpleTagFormBean>();

	/** �w�����X�g�� **/
	private int semesterEntityListSize = 0;

	/** target month ���X�g */
	private List<Search32172000_TargetMonthEntity> targetMonthList;

	/** �����[�h���ɕύX���� **/
	private String reload_changed = "";

	/**
	 * �o�͓�
	 */
	private String outputYear  = "";
	private String outputMonth = "";
	private String outputDay   = "";

    /**
     * ���v���_�E�����X�g
     */
    private List<SimpleTagFormBean> monthList = new ArrayList<SimpleTagFormBean>();
    /**
     * ���v���_�E�����X�g
     */
    private List<SimpleTagFormBean> dayList = new ArrayList<SimpleTagFormBean>();



	public String getTransitionMode() {
		return transitionMode;
	}
	public void setTransitionMode(String transitionMode) {
		this.transitionMode = transitionMode;
	}
	public String getReload() {
		return reload;
	}
	public void setReload(String reload) {
		this.reload = reload;
	}
	public String getNendo() {
		return nendo;
	}
	public void setNendo(String nendo) {
		this.nendo = nendo;
	}
	public String getSemCode() {
		return semCode;
	}
	public void setSemCode(String semCode) {
		this.semCode = semCode;
	}
	public String getSemStart() {
		return semStart;
	}
	public void setSemStart(String semStart) {
		this.semStart = semStart;
	}
	public String getSemEnd() {
		return semEnd;
	}
	public void setSemEnd(String semEnd) {
		this.semEnd = semEnd;
	}
	public String getSemName() {
		return semName;
	}
	public void setSemName(String semName) {
		this.semName = semName;
	}
	public String[] getCheckedTargetMonth() {
		return checkedTargetMonth;
	}
	public void setCheckedTargetMonth(String[] checkedTargetMonth) {
		this.checkedTargetMonth = checkedTargetMonth;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getHmrClsno() {
		return hmrClsno;
	}
	public void setHmrClsno(String hmrClsno) {
		this.hmrClsno = hmrClsno;
	}
    public String getHmrClass() {
		return hmrClass;
	}
	public void setHmrClass(String hmrClass) {
		this.hmrClass = hmrClass;
	}
	public String getHmrName() {
		return hmrName;
	}
	public void setHmrName(String hmrName) {
		this.hmrName = hmrName;
	}
	public List<SimpleTagFormBean> getClassList() {
		return classList;
	}
	public void setClassList(List<SimpleTagFormBean> classList) {
		this.classList = classList;
	}
	public List<SemesterEntity> getSemesterEntityList() {
		return semesterEntityList;
	}
	public void setSemesterEntityList(List<SemesterEntity> semesterEntityList) {
		this.semesterEntityList = semesterEntityList;
	}
	public int getSemesterEntityListSize() {
		return semesterEntityListSize;
	}
	public void setSemesterEntityListSize(int semesterEntityListSize) {
		this.semesterEntityListSize = semesterEntityListSize;
	}
	public List<SimpleTagFormBean> getSemesterList() {
		return semesterList;
	}
	public void setSemesterList(List<SimpleTagFormBean> semesterList) {
		this.semesterList = semesterList;
	}
	public List<Search32172000_TargetMonthEntity> getTargetMonthList() {
		return targetMonthList;
	}
	public void setTargetMonthList(List<Search32172000_TargetMonthEntity> targetMonthList) {
		this.targetMonthList = targetMonthList;
	}
	public String getOutputYear() {
		return outputYear;
	}
	public void setOutputYear(String outputYear) {
		this.outputYear = outputYear;
	}
	public String getOutputMonth() {
		return outputMonth;
	}
	public void setOutputMonth(String outputMonth) {
		this.outputMonth = outputMonth;
	}
	public String getOutputDay() {
		return outputDay;
	}
	public void setOutputDay(String outputDay) {
		this.outputDay = outputDay;
	}
	public List<SimpleTagFormBean> getMonthList() {
		return monthList;
	}
	public void setMonthList(List<SimpleTagFormBean> monthList) {
		this.monthList = monthList;
	}
	public List<SimpleTagFormBean> getDayList() {
		return dayList;
	}
	public void setDayList(List<SimpleTagFormBean> dayList) {
		this.dayList = dayList;
	}
	public String getReload_changed() {
		return reload_changed;
	}
	public void setReload_changed(String reload_changed) {
		this.reload_changed = reload_changed;
	}


}