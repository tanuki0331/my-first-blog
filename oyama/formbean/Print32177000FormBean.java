/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.att.db.entity.AttPrintStatisticsEntity;
import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintEntryLeaveStudentFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintFormBeanImpl;
import jp.co.systemd.tnavi.att.formbean.AttPrintStudentFormBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32177000EnforceEntity;


/**
 * <PRE>
 *  出席簿印刷(特別支援学級　小山市用) 印刷 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32177000FormBean implements AttPrintFormBeanImpl {

	/**
	 * 対象年月（yyyymm）
	 */
	private String targetYearMonth;

	/**
	 * 検索FormBean
	 */
	private Search32177000FormBean searchFormBean;

	/**
	 * 「出席」時に出力する文字列
	 */
	private String attendDisplayValue = "　";

	/**
	 * 日付情報
	 */
	private AttPrintDayFormBean[] days;

	private Map<String, AttPrintDayFormBean[]> daysMap = new HashMap<String, AttPrintDayFormBean[]>();

	/**
	 * 生徒情報Map
	 */
	Map<String, AttPrintStudentFormBean> studentMap = new LinkedHashMap<String, AttPrintStudentFormBean>();

	/**
	 * 転入者一覧
	 */
	private List<AttPrintEntryLeaveStudentFormBean> entryStuList;

	/**
	 * 転出者一覧
	 */
	private List<AttPrintEntryLeaveStudentFormBean> leaveStuList;

	/**
	 * 月別統計リスト
	 */
	private List<AttPrintStatisticsEntity> monthStatisticsList;

	/**
	 * 月別長期欠席者Map(キーは月）
	 */
	private Map<String, Integer> longAbsenceNumMap = new HashMap<String, Integer>();

	/**
	 * 学級担任氏名(正)
	 */
	private String teacherNameMain;

	/**
	 * 学級担任氏名(副)
	 */
	private String teacherNameSub;

	/**
	 * 月末授業日数情報
	 */
	private List<Print32177000EnforceEntity> enforceList;

	/**
	 * 月末統計のMap（キーは男:1,女:2）
	 */
	private Map<String, Print32177000StatisticsFormBean> statisticsMap;

	private String groupName;



	public Search32177000FormBean getSearchFormBean() {
		return searchFormBean;
	}

	public void setSearchFormBean(Search32177000FormBean searchFormBean) {
		this.searchFormBean = searchFormBean;
	}

	public AttPrintDayFormBean[] getDays() {
		return days;
	}

	public void setDays(AttPrintDayFormBean[] days) {
		this.days = days;
	}

	public Map<String, AttPrintDayFormBean[]> getDaysMap() {
		return daysMap;
	}

	public void setDaysMap(Map<String, AttPrintDayFormBean[]> daysMap) {
		this.daysMap = daysMap;
	}

	public Map<String, AttPrintStudentFormBean> getStudentMap() {
		return studentMap;
	}

	public void setStudentMap(Map<String, AttPrintStudentFormBean> studentMap) {
		this.studentMap = studentMap;
	}

	public List<AttPrintEntryLeaveStudentFormBean> getEntryStuList() {
		return entryStuList;
	}

	public void setEntryStuList(List<AttPrintEntryLeaveStudentFormBean> entryStuList) {
		this.entryStuList = entryStuList;
	}

	public List<AttPrintEntryLeaveStudentFormBean> getLeaveStuList() {
		return leaveStuList;
	}

	public void setLeaveStuList(List<AttPrintEntryLeaveStudentFormBean> leaveStuList) {
		this.leaveStuList = leaveStuList;
	}

	public List<AttPrintStatisticsEntity> getMonthStatisticsList() {
		return monthStatisticsList;
	}

	public void setMonthStatisticsList(List<AttPrintStatisticsEntity> monthStatisticsList) {
		this.monthStatisticsList = monthStatisticsList;
	}

	public Map<String, Integer> getLongAbsenceNumMap() {
		return longAbsenceNumMap;
	}

	public void setLongAbsenceNumMap(Map<String, Integer> longAbsenceNumMap) {
		this.longAbsenceNumMap = longAbsenceNumMap;
	}

	public String getTeacherNameMain() {
		return teacherNameMain;
	}

	public void setTeacherNameMain(String teacherNameMain) {
		this.teacherNameMain = teacherNameMain;
	}

	public String getTeacherNameSub() {
		return teacherNameSub;
	}

	public void setTeacherNameSub(String teacherNameSub) {
		this.teacherNameSub = teacherNameSub;
	}

	public String getAttendDisplayValue() {
		return attendDisplayValue;
	}

	public void setAttendDisplayValue(String attendDisplayValue) {
		this.attendDisplayValue = attendDisplayValue;
	}

	public String getTargetYearMonth() {
		return targetYearMonth;
	}

	public void setTargetYearMonth(String targetYearMonth) {
		this.targetYearMonth = targetYearMonth;
	}

	public List<Print32177000EnforceEntity> getEnforceList() {
		return enforceList;
	}

	public void setEnforceList(List<Print32177000EnforceEntity> enforceList) {
		this.enforceList = enforceList;
	}

	public Map<String, Print32177000StatisticsFormBean> getStatisticsMap() {
		return statisticsMap;
	}

	public void setStatisticsMap(Map<String, Print32177000StatisticsFormBean> statisticsMap) {
		this.statisticsMap = statisticsMap;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
