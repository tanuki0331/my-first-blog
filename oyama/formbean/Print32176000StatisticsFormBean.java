/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 月末統計情報 FormBean.
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
	 * 性別
	 */
	private String stu_sex;

	/**
	 * 授業日数
	 */
	private Integer enforceNum;

	/**
	 * 非授業日数
	 */
	private Integer holidayNum;


	/**
	 * 前月末時点の在籍者数
	 */
	private Integer zaisekiStuNumPre;

	/**
	 * 今月末時点の在籍者数
	 */
	private Integer zaisekiStuNum;

	/**
	 * 出席児童数
	 */
	private Integer attendStuNum;

	/**
	 * 転編入者数
	 */
	private Integer entryStuNum;

	/**
	 * 転退学者数
	 */
	private Integer leaveStuNum;

	/**
	 * 出席児童数を算出する際の「転退学児童数」
	 */
	private Integer leaveStuNumForAttend;

	/**
	 * 全欠者数
	 */
	private Integer allAbsenceStuNum;

	/**
	 * 出席停止忌引数
	 */
	private Integer stopSum;

	/**
	 * 病欠総数
	 */
	private Integer sickSum;

	/**
	 * その他欠席(事故欠)総数
	 */
	private Integer accidentSum;

	/**
	 * 欠席者数計（病欠・その他の合算値）
	 */
	private Integer absenceSum;

	/**
	 * 仮退者数
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
	 * 全欠児童数を加算する
	 * @param addValue
	 */
	public void addAllAbsenceStuNum(int addValue){
		this.allAbsenceStuNum += addValue;
	}

	/**
	 * 出席停止忌引き数を加算する
	 * @param addValue
	 */
	public void addStopSum(int addValue){
		this.stopSum += addValue;
	}

	/**
	 * 病欠数を加算する
	 * @param addValue
	 */
	public void addSickSum(int addValue){
		this.sickSum += addValue;
	}

	/**
	 * その他を加算する
	 * @param addValue
	 */
	public void addAccidentSum(int addValue){
		this.accidentSum += addValue;
	}

	/**
	 * 出席児童数を加算する
	 * @param addValue
	 */
	public void addAttendStuNum(int addValue){
		this.attendStuNum += addValue;
	}

	/**
	 * 出席児童数を取得する(「在籍児童数から全欠児童数を差し引き、転退学児童数を加えた値」)
	 * @return 出席児童数
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
