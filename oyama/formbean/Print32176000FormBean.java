/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *  oÈëóü(Êíw@¬Rsp) óü FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000FormBean  {

	/**
	 * wZÌ®æª:
	 */
	private String userKind;

	/**
	 * ÎÛNiyyyymmj
	 */
	private String targetYearMonth;

	/**
	 * ú
	 */
	private Integer monthDayCnt;

	/**
	 * útîñ
	 */
	private Print32176000DayFormBean[] days;

	/**
	 * ¶kîñMap
	 */
	Map<String, Print32176000StudentFormBean> studentMap;

	/**
	 * vÌMapiL[Íj:1,:2j
	 */
	Map<String, Print32176000StatisticsFormBean> statisticsMap;

	/**
	 * ]üÒê
	 */
	private List<Print32176000EntryLeaveFormBean> entryStuList;

	/**
	 * ]oÒê
	 */
	private List<Print32176000EntryLeaveFormBean> leaveStuList;

	/**
	 * wN:
	 */
	private String grade;

	/**
	 * g
	 */
	private String hmrclass;

	/**
	 * wSC¼
	 */
	private String teacherName;

	private String userCode;

	public String getUserKind() {
		return userKind;
	}

	public void setUserKind(String userKind) {
		this.userKind = userKind;
	}

	public String getTargetYearMonth() {
		return targetYearMonth;
	}

	public void setTargetYearMonth(String targetYearMonth) {
		this.targetYearMonth = targetYearMonth;
	}

	public Integer getMonthDayCnt() {
		return monthDayCnt;
	}

	public void setMonthDayCnt(Integer monthDayCnt) {
		this.monthDayCnt = monthDayCnt;
	}

	public Print32176000DayFormBean[] getDays() {
		return days;
	}

	public void setDays(Print32176000DayFormBean[] days) {
		this.days = days;
	}

	public Map<String, Print32176000StudentFormBean> getStudentMap() {
		return studentMap;
	}

	public void setStudentMap(Map<String, Print32176000StudentFormBean> studentMap) {
		this.studentMap = studentMap;
	}

	public Map<String, Print32176000StatisticsFormBean> getStatisticsMap() {
		return statisticsMap;
	}

	public void setStatisticsMap(Map<String, Print32176000StatisticsFormBean> statisticsMap) {
		this.statisticsMap = statisticsMap;
	}

	public List<Print32176000EntryLeaveFormBean> getEntryStuList() {
		return entryStuList;
	}

	public void setEntryStuList(List<Print32176000EntryLeaveFormBean> entryStuList) {
		this.entryStuList = entryStuList;
	}

	public List<Print32176000EntryLeaveFormBean> getLeaveStuList() {
		return leaveStuList;
	}

	public void setLeaveStuList(List<Print32176000EntryLeaveFormBean> leaveStuList) {
		this.leaveStuList = leaveStuList;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHmrclass() {
		return hmrclass;
	}

	public void setHmrclass(String hmrclass) {
		this.hmrclass = hmrclass;
	}

	/**
	 * @return teacherName
	 */
	public String getTeacherName() {
		return teacherName;
	}

	/**
	 * @param teacherName teacherNameðÝè·é
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
