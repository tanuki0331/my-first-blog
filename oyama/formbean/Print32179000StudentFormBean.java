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
 * ���ђʒm�\���(���R�s_���w�Z) ��� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000StudentFormBean {

	public final static String DEFALUT_VALUE = "";

	/** �����R�[�h **/
	private String usercode = DEFALUT_VALUE;

	/** �N�x **/
	private String nendo = DEFALUT_VALUE;

	/** �w�N **/
	private String stuGrade = DEFALUT_VALUE;

	/** �g **/
	private String stuClass = DEFALUT_VALUE;

	/** �w���R�[�h **/
	private String stuClano = DEFALUT_VALUE;

	/** �o�Ȕԍ� **/
	private String stuNumber = DEFALUT_VALUE;

	/** �o�Ȕԍ��i�𗬁j */
	private String refNumber = DEFALUT_VALUE;

	/** �w���R�[�h */
	private String stuClsno = DEFALUT_VALUE;

	/** �w�Дԍ� **/
	private String stuCode = DEFALUT_VALUE;

	/** ���k�E�ʏ̎������� **/
	private String stuName = DEFALUT_VALUE;
	/** ���k�E�ːЎ������� **/
	private String stuKosekiName = DEFALUT_VALUE;

	/** ���k�E������������ **/
	private String stuKana = DEFALUT_VALUE;
	/** ���k�E�ːЎ����������� **/
	private String stuKosekiKana = DEFALUT_VALUE;

	/** ���N���� */
	private String stuBirth = DEFALUT_VALUE;

	/** ���ʎx���w���O���[�v���� */
	private String spGroupname = DEFALUT_VALUE;

	/** ���ʎx���𗬐�w���� */
	private String excClass = DEFALUT_VALUE;

	/** ���ʎx���𗬐�o�Ȕԍ� */
	private String excNumber = DEFALUT_VALUE;

	/** ���ʎx���𗬐�N���XNo */
	private String excClsno = DEFALUT_VALUE;

	/** �𗬊w���Q���҃t���O */
	private String isKoryu;

	/** �w�K�̋L�^Map */
	private List<Print32179000ScoreEntity> scoreList = new ArrayList<Print32179000ScoreEntity>();

	/** �����I�Ȋw�K�̎��� */
	private Print32179000TotalActEntity totalAct;

	/** �O���ꊈ���̋L�^���X�g */
	private List<Print32179000ForeignLangActEntity> foreignLangActList = new ArrayList<Print32179000ForeignLangActEntity>();

	/** ���ʊ����̋L�^ ���X�g */
	private List<Print32179000SpActEntity> spActList = new ArrayList<Print32179000SpActEntity>();

	/** �s���̋L�^ ���X�g */
	private List<Print32179000ActviewEntity> actviewList = new ArrayList<Print32179000ActviewEntity>();

	/** �������� */
	private Print32179000CommentEntity comment = new Print32179000CommentEntity();

	/** �o���̋L�^ */
	private List<Print32179000AttendEntity> attendList = new ArrayList<Print32179000AttendEntity>();

	/** ���N�f�f�̋L�^ */
	private Print32179000HealthrecordEntity healthrecord;

	/** �����x�ƒ��̏o�Z�� */
	private String schooldaysOnLongHolidays;

	/** �S�C���X�g(�~���j */
	private List<StaffEntity> staffList;
	/** �S�C���X�g(�~���j */
	private List<StaffEntity> spStaffList;

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	/** �w�N�𐔒l�ϊ������l���擾���� */
	public Integer getGrade(){
		return Integer.parseInt(stuGrade);
	}

	/** �o�͗p�̖��O���擾���� */
	public String getOutputStuName() {
		// 2�N���ȉ��͂��ȕ\�L
		String stuName = this.stuName;
		if(this.getGrade() <= 2){
			stuName = this.stuKana;
		}

		// @@TODO 2016�N�x�b��Ή� 2016�N�x�́A�D�z���w�Z�̂݁A�K�������������o�͂���
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
