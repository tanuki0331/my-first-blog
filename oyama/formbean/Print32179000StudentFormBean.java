package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.ArrayList;
import java.util.List;

import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ActviewEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000CommentEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ForeignLangActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000HealthrecordEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ScoreEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000SpActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000TotalActEntity;

/**
 * <PRE>
 * 成績通知表印刷(小山市_小学校) 印刷 FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000StudentFormBean {

	public final static String DEFALUT_VALUE = "";

	/** 所属コード **/
	private String usercode = DEFALUT_VALUE;

	/** 年度 **/
	private String nendo = DEFALUT_VALUE;

	/** 学年 **/
	private String stuGrade = DEFALUT_VALUE;

	/** 組 **/
	private String stuClass = DEFALUT_VALUE;

	/** 学級コード **/
	private String stuClano = DEFALUT_VALUE;

	/** 出席番号 **/
	private String stuNumber = DEFALUT_VALUE;

	/** 出席番号（交流） */
	private String refNumber = DEFALUT_VALUE;

	/** 学級コード */
	private String stuClsno = DEFALUT_VALUE;

	/** 学籍番号 **/
	private String stuCode = DEFALUT_VALUE;

	/** 生徒・通称児童氏名 **/
	private String stuName = DEFALUT_VALUE;
	/** 生徒・戸籍児童氏名 **/
	private String stuKosekiName = DEFALUT_VALUE;

	/** 生徒・児童氏名かな **/
	private String stuKana = DEFALUT_VALUE;
	/** 生徒・戸籍児童氏名かな **/
	private String stuKosekiKana = DEFALUT_VALUE;

	/** 生年月日 */
	private String stuBirth = DEFALUT_VALUE;

	/** 特別支援学級グループ名称 */
	private String spGroupname = DEFALUT_VALUE;

	/** 特別支援交流先学級名 */
	private String excClass = DEFALUT_VALUE;

	/** 特別支援交流先出席番号 */
	private String excNumber = DEFALUT_VALUE;

	/** 特別支援交流先クラスNo */
	private String excClsno = DEFALUT_VALUE;

	/** 交流学級参加者フラグ */
	private String isKoryu;

	/** 学習の記録Map */
	private List<Print32179000ScoreEntity> scoreList = new ArrayList<Print32179000ScoreEntity>();

	/** 総合的な学習の時間 */
	private Print32179000TotalActEntity totalAct;

	/** 外国語活動の記録リスト */
	private List<Print32179000ForeignLangActEntity> foreignLangActList = new ArrayList<Print32179000ForeignLangActEntity>();

	/** 特別活動の記録 リスト */
	private List<Print32179000SpActEntity> spActList = new ArrayList<Print32179000SpActEntity>();

	/** 行動の記録 リスト */
	private List<Print32179000ActviewEntity> actviewList = new ArrayList<Print32179000ActviewEntity>();

	/** 総合所見 */
	private Print32179000CommentEntity comment = new Print32179000CommentEntity();

	/** 出欠の記録 */
	private List<Print32179000AttendEntity> attendList = new ArrayList<Print32179000AttendEntity>();

	/** 健康診断の記録 */
	private Print32179000HealthrecordEntity healthrecord;

	/** 長期休業中の出校日 */
	private String schooldaysOnLongHolidays;

	/** 担任リスト(降順） */
	private List<StaffEntity> staffList;
	/** 担任リスト(降順） */
	private List<StaffEntity> spStaffList;

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	/** 学年を数値変換した値を取得する */
	public Integer getGrade(){
		return Integer.parseInt(stuGrade);
	}

	/** 出力用の名前を取得する */
	public String getOutputStuName() {
		// 2年生以下はかな表記
		String stuName = this.stuName;
		if(this.getGrade() <= 2){
			stuName = this.stuKana;
		}

		// @@TODO 2016年度暫定対応 2016年度は、船越小学校のみ、必ず漢字氏名を出力する
		if ( "1028".equals(this.usercode) && "2016".equals(this.nendo) ){
			stuName = this.stuName;
		}

		return stuName;
	}

	public String getNendo() {
		return nendo;
	}

	public void setNendo(String nendo) {
		this.nendo = nendo;
	}

	public String getStuGrade() {
		return stuGrade;
	}

	public void setStuGrade(String stuGrade) {
		this.stuGrade = stuGrade;
	}

	public String getStuClass() {
		return stuClass;
	}

	public void setStuClass(String stuClass) {
		this.stuClass = stuClass;
	}

	public String getStuNumber() {
		return stuNumber;
	}

	public void setStuNumber(String stuNumber) {
		this.stuNumber = stuNumber;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStuKana() {
		return stuKana;
	}

	public void setStuKana(String stuKana) {
		this.stuKana = stuKana;
	}

	public String getStuBirth() {
		return stuBirth;
	}

	public void setStuBirth(String stuBirth) {
		this.stuBirth = stuBirth;
	}

	public List<Print32179000ScoreEntity> getScoreList() {
		return scoreList;
	}

	public void setScoreList(List<Print32179000ScoreEntity> scoreList) {
		this.scoreList = scoreList;
	}

	public Print32179000TotalActEntity getTotalAct() {
		return totalAct;
	}

	public void setTotalAct(Print32179000TotalActEntity totalAct) {
		this.totalAct = totalAct;
	}

	public List<Print32179000ForeignLangActEntity> getForeignLangActList() {
		return foreignLangActList;
	}

	public void setForeignLangActList(List<Print32179000ForeignLangActEntity> foreignLangActList) {
		this.foreignLangActList = foreignLangActList;
	}

	public List<Print32179000SpActEntity> getSpActList() {
		return spActList;
	}

	public void setSpActList(List<Print32179000SpActEntity> spActList) {
		this.spActList = spActList;
	}

	public List<Print32179000ActviewEntity> getActviewList() {
		return actviewList;
	}

	public void setActviewList(List<Print32179000ActviewEntity> actviewList) {
		this.actviewList = actviewList;
	}

	public Print32179000CommentEntity getComment() {
		return comment;
	}

	public void setComment(Print32179000CommentEntity comment) {
		this.comment = comment;
	}

	public List<Print32179000AttendEntity> getAttendList() {
		return attendList;
	}

	public void setAttendList(List<Print32179000AttendEntity> attendList) {
		this.attendList = attendList;
	}

	public Print32179000HealthrecordEntity getHealthrecord() {
		return healthrecord;
	}

	public void setHealthrecord(Print32179000HealthrecordEntity healthrecord) {
		this.healthrecord = healthrecord;
	}

	public String getSchooldaysOnLongHolidays() {
		return schooldaysOnLongHolidays;
	}

	public void setSchooldaysOnLongHolidays(String schooldaysOnLongHolidays) {
		this.schooldaysOnLongHolidays = schooldaysOnLongHolidays;
	}

	public String getStuClano() {
		return stuClano;
	}

	public void setStuClano(String stuClano) {
		this.stuClano = stuClano;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getStuClsno() {
		return stuClsno;
	}

	public void setStuClsno(String stuClsno) {
		this.stuClsno = stuClsno;
	}

	public String getSpGroupname() {
		return spGroupname;
	}

	public void setSpGroupname(String spGroupname) {
		this.spGroupname = spGroupname;
	}

	public String getExcClass() {
		return excClass;
	}

	public void setExcClass(String excClass) {
		this.excClass = excClass;
	}

	public String getExcNumber() {
		return excNumber;
	}

	public void setExcNumber(String excNumber) {
		this.excNumber = excNumber;
	}

	public String getIsKoryu() {
		return isKoryu;
	}

	public void setIsKoryu(String isKoryu) {
		this.isKoryu = isKoryu;
	}

	public String getStuKosekiName() {
		return stuKosekiName;
	}

	public void setStuKosekiName(String stuKosekiName) {
		this.stuKosekiName = stuKosekiName;
	}

	public String getStuKosekiKana() {
		return stuKosekiKana;
	}

	public void setStuKosekiKana(String stuKosekiKana) {
		this.stuKosekiKana = stuKosekiKana;
	}

	public String getExcClsno() {
		return excClsno;
	}

	public void setExcClsno(String excClsno) {
		this.excClsno = excClsno;
	}

	public List<StaffEntity> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<StaffEntity> staffList) {
		this.staffList = staffList;
	}

	public List<StaffEntity> getSpStaffList() {
		return spStaffList;
	}

	public void setSpStaffList(List<StaffEntity> spStaffList) {
		this.spStaffList = spStaffList;
	}
}
