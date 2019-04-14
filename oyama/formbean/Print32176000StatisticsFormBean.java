/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) �������v��� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000StatisticsFormBean  {

	/**
	 * ����
	 */
	private String stu_sex;

	/**
	 * ���Ɠ���
	 */
	private Integer enforceNum;

	/**
	 * ����Ɠ���
	 */
	private Integer holidayNum;


	/**
	 * �O�������_�̍ݐЎҐ�
	 */
	private Integer zaisekiStuNumPre;

	/**
	 * ���������_�̍ݐЎҐ�
	 */
	private Integer zaisekiStuNum;

	/**
	 * �o�Ȏ�����
	 */
	private Integer attendStuNum;

	/**
	 * �]�ғ��Ґ�
	 */
	private Integer entryStuNum;

	/**
	 * �]�ފw�Ґ�
	 */
	private Integer leaveStuNum;

	/**
	 * �o�Ȏ��������Z�o����ۂ́u�]�ފw�������v
	 */
	private Integer leaveStuNumForAttend;

	/**
	 * �S���Ґ�
	 */
	private Integer allAbsenceStuNum;

	/**
	 * �o�Ȓ�~������
	 */
	private Integer stopSum;

	/**
	 * �a������
	 */
	private Integer sickSum;

	/**
	 * ���̑�����(���̌�)����
	 */
	private Integer accidentSum;

	/**
	 * ���ȎҐ��v�i�a���E���̑��̍��Z�l�j
	 */
	private Integer absenceSum;

	/**
	 * ���ގҐ�
	 */
	private Integer karitaiStuNum;


	public Print32176000StatisticsFormBean() {

		this.enforceNum = 0;
		this.zaisekiStuNumPre = 0;
		this.zaisekiStuNum = 0;
		this.holidayNum = 0;
		this.attendStuNum = 0;
		this.entryStuNum = 0;
		this.leaveStuNum = 0;
		this.leaveStuNumForAttend = 0;
		this.allAbsenceStuNum = 0;
		this.stopSum = 0;
		this.sickSum = 0;
		this.accidentSum = 0;
		this.absenceSum = 0;
		this.karitaiStuNum = 0;
	}

	public String getStu_sex() {
		return stu_sex;
	}

	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
	}

	public Integer getEnforceNum() {
		return enforceNum;
	}

	public void setEnforceNum(Integer enforceNum) {
		this.enforceNum = enforceNum;
	}

	public Integer getHolidayNum() {
		return holidayNum;
	}

	public void setHolidayNum(Integer holidayNum) {
		this.holidayNum = holidayNum;
	}

	public Integer getZaisekiStuNumPre() {
		return zaisekiStuNumPre;
	}

	public void setZaisekiStuNumPre(Integer zaisekiStuNumPre) {
		this.zaisekiStuNumPre = zaisekiStuNumPre;
	}

	public Integer getZaisekiStuNum() {
		return zaisekiStuNum;
	}

	public void setZaisekiStuNum(Integer zaisekiStuNum) {
		this.zaisekiStuNum = zaisekiStuNum;
	}

	public Integer getEntryStuNum() {
		return entryStuNum;
	}

	public void setEntryStuNum(Integer entryStuNum) {
		this.entryStuNum = entryStuNum;
	}

	public Integer getLeaveStuNum() {
		return leaveStuNum;
	}

	public void setLeaveStuNum(Integer leaveStuNum) {
		this.leaveStuNum = leaveStuNum;
	}

	public Integer getLeaveStuNumForAttend() {
		return leaveStuNumForAttend;
	}

	public void setLeaveStuNumForAttend(Integer leaveStuNumForAttend) {
		this.leaveStuNumForAttend = leaveStuNumForAttend;
	}

	public Integer getAllAbsenceStuNum() {
		return allAbsenceStuNum;
	}

	public void setAllAbsenceStuNum(Integer allAbsenceStuNum) {
		this.allAbsenceStuNum = allAbsenceStuNum;
	}

	public Integer getStopSum() {
		return stopSum;
	}

	public void setStopSum(Integer stopSum) {
		this.stopSum = stopSum;
	}

	public Integer getSickSum() {
		return sickSum;
	}

	public void setSickSum(Integer sickSum) {
		this.sickSum = sickSum;
	}

	public Integer getAccidentSum() {
		return accidentSum;
	}

	public void setAccidentSum(Integer accidentSum) {
		this.accidentSum = accidentSum;
	}

	public Integer getAbsenceSum() {
		return absenceSum;
	}

	public void setAbsenceSum(Integer absenceSum) {
		this.absenceSum = absenceSum;
	}

	/**
	 * �S�������������Z����
	 * @param addValue
	 */
	public void addAllAbsenceStuNum(int addValue){
		this.allAbsenceStuNum += addValue;
	}

	/**
	 * �o�Ȓ�~�������������Z����
	 * @param addValue
	 */
	public void addStopSum(int addValue){
		this.stopSum += addValue;
	}

	/**
	 * �a���������Z����
	 * @param addValue
	 */
	public void addSickSum(int addValue){
		this.sickSum += addValue;
	}

	/**
	 * ���̑������Z����
	 * @param addValue
	 */
	public void addAccidentSum(int addValue){
		this.accidentSum += addValue;
	}

	/**
	 * �o�Ȏ����������Z����
	 * @param addValue
	 */
	public void addAttendStuNum(int addValue){
		this.attendStuNum += addValue;
	}

	/**
	 * �o�Ȏ��������擾����(�u�ݐЎ���������S�������������������A�]�ފw���������������l�v)
	 * @return �o�Ȏ�����
	 */
	public Integer getAttendStuNum() {
		return attendStuNum;
	}

	public Integer getKaritaiStuNum() {
		return karitaiStuNum;
	}

	public void setKaritaiStuNum(Integer karitaiStuNum) {
		this.karitaiStuNum = karitaiStuNum;
	}
}
