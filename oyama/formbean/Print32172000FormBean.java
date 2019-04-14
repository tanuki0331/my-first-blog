package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_EnforcedayEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_StaffEntity;

/**
 * <PRE>
 * 	èoåáê»àÍóóèoóÕ àÛç¸ FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32172000FormBean extends Search32172000FormBean {


	protected int checkedCount ;

	protected boolean yearTotalFlag;

	protected List<HRoomEntity> hRoomEntityList ;

	protected HashMap<String, Search32172000_StaffEntity> staffEntityMap;

	protected List<Print32172000_AttendEntity> semesterAttendEntityList;

	protected LinkedHashMap<String, Print32172000_AttendEntity> semesterAttendEntityMap;

	protected List<Print32172000_AttendEntity> attendEntityList;

	protected LinkedHashMap<String, Print32172000_AttendEntity> attendEntityMap;

	protected List<Print32172000_AttendEntity> yearAttendEntityList;

	protected LinkedHashMap<String, Print32172000_AttendEntity> yearAttendEntityMap;

	protected LinkedHashMap<String, Print32172000_EnforcedayEntity> enforceDayTotalMap;

	protected String output_title_gengo;

	protected String output_title_nendo;


	public int getCheckedCount() {
		return checkedCount;
	}

	public void setCheckedCount(int checkedCount) {
		this.checkedCount = checkedCount;
	}

	public boolean isYearTotalFlag() {
		return yearTotalFlag;
	}

	public void setYearTotalFlag(boolean yearTotalFlag) {
		this.yearTotalFlag = yearTotalFlag;
	}

	public List<HRoomEntity> getHRoomEntityList() {
		return hRoomEntityList;
	}

	public void setHRoomEntityList(List<HRoomEntity> hRoomEntityList) {
		this.hRoomEntityList = hRoomEntityList;
	}

	public HashMap<String, Search32172000_StaffEntity> getStaffEntityMap() {
		return staffEntityMap;
	}

	public void setStaffEntityMap(HashMap<String, Search32172000_StaffEntity> staffEntityMap) {
		this.staffEntityMap = staffEntityMap;
	}

	public List<Print32172000_AttendEntity> getSemesterAttendEntityList() {
		return semesterAttendEntityList;
	}

	public void setSemesterAttendEntityList(List<Print32172000_AttendEntity> semesterAttendEntityList) {
		this.semesterAttendEntityList = semesterAttendEntityList;
	}

	public LinkedHashMap<String, Print32172000_AttendEntity> getSemesterAttendEntityMap() {
		return semesterAttendEntityMap;
	}

	public void setSemesterAttendEntityMap(LinkedHashMap<String, Print32172000_AttendEntity> semesterAttendEntityMap) {
		this.semesterAttendEntityMap = semesterAttendEntityMap;
	}

	public List<Print32172000_AttendEntity> getAttendEntityList() {
		return attendEntityList;
	}

	public void setAttendEntityList(List<Print32172000_AttendEntity> attendEntityList) {
		this.attendEntityList = attendEntityList;
	}

	public LinkedHashMap<String, Print32172000_AttendEntity> getAttendEntityMap() {
		return attendEntityMap;
	}

	public void setAttendEntityMap(LinkedHashMap<String, Print32172000_AttendEntity> attendEntityMap) {
		this.attendEntityMap = attendEntityMap;
	}

	public List<Print32172000_AttendEntity> getYearAttendEntityList() {
		return yearAttendEntityList;
	}

	public void setYearAttendEntityList(List<Print32172000_AttendEntity> yearAttendEntityList) {
		this.yearAttendEntityList = yearAttendEntityList;
	}

	public LinkedHashMap<String, Print32172000_AttendEntity> getYearAttendEntityMap() {
		return yearAttendEntityMap;
	}

	public void setYearAttendEntityMap(LinkedHashMap<String, Print32172000_AttendEntity> yearAttendEntityMap) {
		this.yearAttendEntityMap = yearAttendEntityMap;
	}

	public LinkedHashMap<String, Print32172000_EnforcedayEntity> getEnforceDayTotalMap() {
		return enforceDayTotalMap;
	}

	public void setEnforceDayTotalMap(LinkedHashMap<String, Print32172000_EnforcedayEntity> enforceDayTotalMap) {
		this.enforceDayTotalMap = enforceDayTotalMap;
	}

	public String getOutput_title_gengo() {
		return output_title_gengo;
	}

	public void setOutput_title_gengo(String output_title_gengo) {
		this.output_title_gengo = output_title_gengo;
	}

	public String getOutput_title_nendo() {
		return output_title_nendo;
	}

	public void setOutput_title_nendo(String output_title_nendo) {
		this.output_title_nendo = output_title_nendo;
	}


}
