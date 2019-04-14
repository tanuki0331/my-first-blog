/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.formbean;

/**
 * <PRE>
 *  �o�ȕ���(���ʎx���w���@���R�s�p) �������v��� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32177000StatisticsFormBean  {

	/**
	 * ����
	 */
	private String stu_sex;

	/**
	 * �O�������_�̍ݐЎҐ�
	 */
	private Integer zaisekiStuNumPre;

	/**
	 * ���������_�̍ݐЎҐ�
	 */
	private Integer zaisekiStuNum;

	/**
	 * �]�ғ��Ґ�
	 */
	private Integer entryStuNum;

	/**
	 * �]�ފw�Ґ�
	 */
	private Integer leaveStuNum;

	/**
	 * ���ގҐ�
	 */
	private Integer karitaiStuNum;


	public Print32177000StatisticsFormBean() {

		this.zaisekiStuNumPre = 0;
		this.zaisekiStuNum = 0;
		this.entryStuNum = 0;
		this.leaveStuNum = 0;
		this.karitaiStuNum = 0;
	}

	public String getStu_sex() {
		return stu_sex;
	}

	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
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


	public Integer getKaritaiStuNum() {
		return karitaiStuNum;
	}

	public void setKaritaiStuNum(Integer karitaiStuNum) {
		this.karitaiStuNum = karitaiStuNum;
	}
}
