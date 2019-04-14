/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;


/**
 * <PRE>
 *  �y�o���Ǘ��z�o�ȕ���(���ʎx���w���@���R�s�p) Form Bean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32177000FormBean implements AttPrintSearchFormBeanImpl {

	/** �w���^�C�v */
	private String classType = "";

	/** �N�x */
	private String nendo = "";

	/** �w�N */
	private String grade = "";

	/** �g */
	private String hmrclass = "";

	/** �N���X�ԍ� */
	private String clsno = "";

	/** �O���[�v�R�[�h */
	private String groupcode = "";

	/** �O���[�v���� */
	private String groupname = "";

	/** �g���X�g */
	private List<SimpleTagFormBean> hroomSelectboxList;

	/** �O���[�v�R�[�h���X�g */
	private List<SimpleTagFormBean> groupSelectboxList;

	/** �� */
	private String month = "";

	/** �����X�g(�Z���N�g�{�b�N�X�p) */
	private List<SimpleTagFormBean> monthSelectboxList;

	/** ��g����`�F�b�N */
	private String blankFlg = "";

	/** �𗬊w�����܂ށE�܂܂Ȃ�
	 * 1:�܂�
	 * 2:�܂܂Ȃ�
	 */
	private String excFlg = "";

	/** �o���敪�}�X�^�ݒ萔 */
	private Integer mstAttendkindJrCount = 0;

	/** �o�͑Ώی����� */
	private int monthDayCount = 0;

	/**
	 *  �ːЁE�ʏ̑I���\�t���O(cod_value2)
	 *  0:�I��s��(���W�I�{�^����\��)
	 *  1:�I���\(�ʏ̃f�t�H���g�I��)
	 *  2:�I���\(�ːЃf�t�H���g�I��)
	 *  �����R�[�h�������A�܂���NULL�ł���ꍇ�́u0:�I��s�v�Ƃ��Ĉ���
	 */
	private String outputNameFlg;

	/**
	 *  �����̊����E�ӂ肪�ȑI���\�t���O(cod_name2)
	 *  0:�I��s��(���W�I�{�^����\��)
	 *  1:�I���\(�����f�t�H���g�I��)
	 *  2:�I���\(���ȃf�t�H���g�I��)
	 */
	private String outputKanaFlg;

	/**
	 * ���t���Map
	 */
	private Map<Integer, AttPrintDayFormBean> dayMap;

	/**
	 *  �����E�S�C�J�ڏ��
	 *  "KYOMU":����
	 */
	private String tranMode;

	/**
	 * �x�Ɠ���w�N
	 */
	private String holidayGrade;

	/**
	 * �x�Ɠ���w�N���X�g
	 */
	private List<SimpleTagFormBean> holidayGradeSelectboxList;

	/**
	 * �Ώ۔N���iyyyymm�j
	 */
	private String yyyymm;

	/**
	 * ��ʕ\�����b�Z�[�W
	 */
	private String message = "";

	public String getNendo() {
		return nendo;
	}

	public void setNendo(String nendo) {
		this.nendo = nendo;
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

	public String getClsno() {
		return clsno;
	}

	public void setClsno(String clsno) {
		this.clsno = clsno;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public List<SimpleTagFormBean> getHroomSelectboxList() {
		return hroomSelectboxList;
	}

	public void setHroomSelectboxList(List<SimpleTagFormBean> hroomSelectboxList) {
		this.hroomSelectboxList = hroomSelectboxList;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<SimpleTagFormBean> getMonthSelectboxList() {
		return monthSelectboxList;
	}

	public void setMonthSelectboxList(List<SimpleTagFormBean> monthSelectboxList) {
		this.monthSelectboxList = monthSelectboxList;
	}

	public String getBlankFlg() {
		return blankFlg;
	}

	public void setBlankFlg(String blankFlg) {
		this.blankFlg = blankFlg;
	}

	public String getExcFlg() {
		return excFlg;
	}

	public void setExcFlg(String excFlg) {
		this.excFlg = excFlg;
	}

	public Integer getMstAttendkindJrCount() {
		return mstAttendkindJrCount;
	}

	public void setMstAttendkindJrCount(Integer mstAttendkindJrCount) {
		this.mstAttendkindJrCount = mstAttendkindJrCount;
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




	public String getTranMode() {
		return tranMode;
	}

	public void setTranMode(String tranMode) {
		this.tranMode = tranMode;
	}

	public String getHolidayGrade() {
		return holidayGrade;
	}

	public void setHolidayGrade(String holidayGrade) {
		this.holidayGrade = holidayGrade;
	}

	public Map<Integer, AttPrintDayFormBean> getDayMap() {
		return dayMap;
	}

	public void setDayMap(Map<Integer, AttPrintDayFormBean> dayMap) {
		this.dayMap = dayMap;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public List<SimpleTagFormBean> getGroupSelectboxList() {
		return groupSelectboxList;
	}

	public void setGroupSelectboxList(List<SimpleTagFormBean> groupSelectboxList) {
		this.groupSelectboxList = groupSelectboxList;
	}

	public List<SimpleTagFormBean> getHolidayGradeSelectboxList() {
		return holidayGradeSelectboxList;
	}

	public void setHolidayGradeSelectboxList(List<SimpleTagFormBean> holidayGradeSelectboxList) {
		this.holidayGradeSelectboxList = holidayGradeSelectboxList;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getYyyymm() {
		return yyyymm;
	}

	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
