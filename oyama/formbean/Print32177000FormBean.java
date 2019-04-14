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
 *  �o�ȕ���(���ʎx���w���@���R�s�p) ��� FormBean.
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
	 * �Ώ۔N���iyyyymm�j
	 */
	private String targetYearMonth;

	/**
	 * ����FormBean
	 */
	private Search32177000FormBean searchFormBean;

	/**
	 * �u�o�ȁv���ɏo�͂��镶����
	 */
	private String attendDisplayValue = "�@";

	/**
	 * ���t���
	 */
	private AttPrintDayFormBean[] days;

	private Map<String, AttPrintDayFormBean[]> daysMap = new HashMap<String, AttPrintDayFormBean[]>();

	/**
	 * ���k���Map
	 */
	Map<String, AttPrintStudentFormBean> studentMap = new LinkedHashMap<String, AttPrintStudentFormBean>();

	/**
	 * �]���҈ꗗ
	 */
	private List<AttPrintEntryLeaveStudentFormBean> entryStuList;

	/**
	 * �]�o�҈ꗗ
	 */
	private List<AttPrintEntryLeaveStudentFormBean> leaveStuList;

	/**
	 * ���ʓ��v���X�g
	 */
	private List<AttPrintStatisticsEntity> monthStatisticsList;

	/**
	 * ���ʒ������Ȏ�Map(�L�[�͌��j
	 */
	private Map<String, Integer> longAbsenceNumMap = new HashMap<String, Integer>();

	/**
	 * �w���S�C����(��)
	 */
	private String teacherNameMain;

	/**
	 * �w���S�C����(��)
	 */
	private String teacherNameSub;

	/**
	 * �������Ɠ������
	 */
	private List<Print32177000EnforceEntity> enforceList;

	/**
	 * �������v��Map�i�L�[�͒j:1,��:2�j
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
