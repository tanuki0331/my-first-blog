package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.ArrayList;
import java.util.List;

import jp.co.systemd.tnavi.common.db.entity.SemesterEntity;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_TargetMonthEntity;

/**
 * <PRE>
 * 	出欠席一覧出力 検索FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Search32172000FormBean {

	/** 遷移モード **/
	private String transitionMode = "";

	/** リロード **/
	private String reload = "";

	/** 年度 **/
	private String nendo = "";

	/** 学期 */
	private String semCode = "";

	/** 学期開始日 */
	private String semStart = "";

	/** 学期終了日 */
	private String semEnd = "";

	/** 学期名 */
	private String semName = "";

	/** 出力月の選択状態 */
	private String[] checkedTargetMonth = new String[12];

	/** 学年 **/
	private String grade = "";

	/** クラス番号 **/
	private String hmrClsno = "";

	/** 組 **/
	private String hmrClass = "";

	/** 組名 **/
	private String hmrName = "";


	/**
     * クラスプルダウンリスト
     */
    private List<SimpleTagFormBean> classList = new ArrayList<SimpleTagFormBean>();

    /** 学期リスト **/
	private List<SemesterEntity> semesterEntityList;

	/**
     * 学期プルダウンリスト
     */
    private List<SimpleTagFormBean> semesterList = new ArrayList<SimpleTagFormBean>();

	/** 学期リスト数 **/
	private int semesterEntityListSize = 0;

	/** target month リスト */
	private List<Search32172000_TargetMonthEntity> targetMonthList;

	/** リロード時に変更あり **/
	private String reload_changed = "";

	/**
	 * 出力日
	 */
	private String outputYear  = "";
	private String outputMonth = "";
	private String outputDay   = "";

    /**
     * 月プルダウンリスト
     */
    private List<SimpleTagFormBean> monthList = new ArrayList<SimpleTagFormBean>();
    /**
     * 日プルダウンリスト
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