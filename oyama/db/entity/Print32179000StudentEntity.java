package jp.co.systemd.tnavi.cus.oyama.db.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * ���ђʒm�[���(���R�s_���w�Z) ��� ���k��� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter @Getter
public class Print32179000StudentEntity {

	public final static String DEFALUT_VALUE = "";

	/** �N�x **/
	private String nendo = DEFALUT_VALUE;

	/** �w�N **/
	private String stuGrade = DEFALUT_VALUE;

	/** �g **/
	private String stuClass = DEFALUT_VALUE;

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
}
