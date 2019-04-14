/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  �y�o���Ǘ��z�o�ȕ���(�ʏ�w���@���R�s�p) Form Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32176000FormBean  {

	/** �w�Z�R�[�h */
	private String user = "";

	/** �N�x */
	private String nendo = "";

	/** �\���N�x (�a��A�܂��͐���N�x)*/
	private String dispnendo = "";

	/** �N�� */
	private String yyyymm = "";

	/**�����R�[�h*/
	private String department = "";

	/** �w�N */
	private String grade = "";

	/** �w�N tbl_hroom �̍ŏ��̊w�N)	 * */
	private String gradeAtFirst = "";

	/**�z�[�����[���ԍ�*/
	private String clsno = "";

	/**�g*/
	private String hmrclass = "";

	/**�g(tbl_hroom �̍ŏ��̃N���X)	 * */
	private String hmrclassAtFirst = "";

	/** �N���X�� */
	private String hmrname = "";

	/** ���b�Z�[�W */
	private String message =  "";

	/** ���I��pDorpDown */
	private String dropDownForMonth = "";

	/** ���b�Z�[�W�p */
	private String nodata =  "";

	/** �o�͌��e�[�u��HTML */
	private String monthTableHtml = "";

	/** �o�͑Ώی����� */
	private int monthDayCount = 0;

	/**
	 *  �ːЁE�ʏ̑I���\�t���O(cod_value2)
	 *  0:�I��s��,
	 *  1:�I���\(�ʏ̃f�t�H���g�I��)
	 *  2:�I���\(�ːЃf�t�H���g�I��) �����R�[�h�������A�܂���NULL�ł���ꍇ�́u0:�I��s�v�Ƃ��Ĉ���
	 */
	private String outputNameFlg;

	/**
	 *  �����̊����E�ӂ肪�ȑI���\�t���O(cod_name2)
	 *  0:���W�I�{�^����\��
	 *  1:���W�I�{�^���\���@�I���\
	 */
	private String outputKanaFlg;

	/**
	 *  �����E�S�C�J�ڏ��
	 *  ""   :����
	 *  "CLS":�S�C
	 */
	private String tran_mode;

	// ----------------------------------------------
	// getter & setter
	// ----------------------------------------------
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getNendo() {
		return nendo;
	}

	public void setNendo(String nendo) {
		this.nendo = nendo;
	}

	public String getDispnendo() {
		return dispnendo;
	}

	public void setDispnendo(String dispnendo) {
		this.dispnendo = dispnendo;
	}

	public String getYyyymm() {
		return yyyymm;
	}

	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeAtFirst() {
		return gradeAtFirst;
	}

	public void setGradeAtFirst(String gradeAtFirst) {
		this.gradeAtFirst = gradeAtFirst;
	}

	public String getClsno() {
		return clsno;
	}

	public void setClsno(String clsno) {
		this.clsno = clsno;
	}

	public String getHmrclass() {
		return hmrclass;
	}

	public void setHmrclass(String hmrclass) {
		this.hmrclass = hmrclass;
	}

	public String getHmrclassAtFirst() {
		return hmrclassAtFirst;
	}

	public void setHmrclassAtFirst(String hmrclassAtFirst) {
		this.hmrclassAtFirst = hmrclassAtFirst;
	}

	public String getHmrname() {
		return hmrname;
	}

	public void setHmrname(String hmrname) {
		this.hmrname = hmrname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDropDownForMonth() {
		return dropDownForMonth;
	}

	public void setDropDownForMonth(String dropDownForMonth) {
		this.dropDownForMonth = dropDownForMonth;
	}

	public String getNodata() {
		return nodata;
	}

	public void setNodata(String nodata) {
		this.nodata = nodata;
	}

	public String getMonthTableHtml() {
		return monthTableHtml;
	}

	public void setMonthTableHtml(String monthTableHtml) {
		this.monthTableHtml = monthTableHtml;
	}

	public int getMonthDayCount() {
		return monthDayCount;
	}

	public void setMonthDayCount(int monthDayCount) {
		this.monthDayCount = monthDayCount;
	}


	public String getOutputNameFlg() {
		return outputNameFlg;
	}

	public void setOutputNameFlg(String outputNameFlg) {
		this.outputNameFlg = outputNameFlg;
	}

	public String getOutputKanaFlg() {
		return outputKanaFlg;
	}

	public void setOutputKanaFlg(String outputKanaFlg) {
		this.outputKanaFlg = outputKanaFlg;
	}

	public String getTran_mode() {
		return tran_mode;
	}

	public void setTran_mode(String tran_mode) {
		this.tran_mode = tran_mode;
	}

}
