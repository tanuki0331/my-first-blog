package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000StudentEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * ���ђʒm�\���(���R�s_���w�Z) ���� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter
@Getter
public class Search32179000FormBean {

	public final static String DEFALUT_VALUE = "";

	/** �N�x */
	private String nendo = DEFALUT_VALUE;

	/** �N���X�� */
	private String hmrName = DEFALUT_VALUE;

	/** �o�͎������X�g */
	private List<SimpleTagFormBean> outputTermList;

	/** �o�͎��� */
	private String outputTerm;

	/** �o�̓y�[�W �\�� */
	private String output_cover;

	/** �o�̓y�[�W �߂���(������) */
	private String output_purpose;

	/** �o�̓y�[�W ���e */
	private String output_contents;

	/** �o�̓y�[�W �C���� */
	private String output_deed;

	/** �o�͓��t �N */
	private String output_year;

	/** �o�͓��t �� */
	private String output_month;

	/** �o�͓��t �� */
	private String output_day;

	/** �C�����t �N */
	private String deed_year;

	/** �C�����t �� */
	private String deed_month;

	/** �C�����t �� */
	private String deed_day;

	/** �����X�g */
	private List<SimpleTagFormBean> monthList;

	/** �����X�g */
	private List<SimpleTagFormBean> dayList;

	/** ���k�ꗗ */
	private List<Print32179000StudentEntity> studentList;

	/** ���k�ꗗ�� */
	private int cntStu = 0;

	/** �G���[���b�Z�[�W */
	private Map<String, String> errorMessage;

	/** ���ʎx���w���t���O */
	private boolean isSpSupportCls = false;

	/** �o�̓y�[�W �w�K�̂悤�� */
	private String output_score;

	/** �o�̓y�[�W �����̋L�^�E���� */
	private String output_other;
}