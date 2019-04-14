/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 印刷 FormBean.
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
	 * 学校の所属区分:
	 */
	private String userKind;

	/**
	 * 対象年月（yyyymm）
	 */
	private String targetYearMonth;

	/**
	 * 日数
	 */
	private Integer monthDayCnt;

	/**
	 * 日付情報
	 */
	private Print32176000DayFormBean[] days;

	/**
	 * 生徒情報Map
	 */
	Map<String, Print32176000StudentFormBean> studentMap;

	/**
	 * 月末統計のMap（キーは男:1,女:2）
	 */
	Map<String, Print32176000StatisticsFormBean> statisticsMap;

	/**
	 * 転入者一覧
	 */
	private List<Print32176000EntryLeaveFormBean> entryStuList;

	/**
	 * 転出者一覧
	 */
	private List<Print32176000EntryLeaveFormBean> leaveStuList;

	/**
	 * 学年:
	 */
	private String grade;

	/**
	 * 組
	 */
	private String hmrclass;

	/**
	 * 学級担任氏名
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
	 * @param teacherName teacherNameを設定する
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
