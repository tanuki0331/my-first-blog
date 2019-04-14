package jp.co.systemd.tnavi.cus.oyama.formbean;

import java.util.List;
import java.util.Map;

import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * ���ђʒm�[���(���R�s_���w�Z) ��� FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter
@Getter
public class Print32179000FormBean {

	public final static String DEFALUT_VALUE = "";

	/** ������� */
	public static final String OUTPUT_TERM_1		= "01";	// 1�w��
	public static final String OUTPUT_TERM_2		= "02";	// 2�w��
	public static final String OUTPUT_TERM_3		= "03";	// 3�w��
	public static final String OUTPUT_TERM_GRADE	= "99";	// �w�N��

	/** �w�N��� */
	public final static Integer OUTPUT_GRADE_01 = 1; // 1�N
	public final static Integer OUTPUT_GRADE_02 = 2; // 2�N
	public final static Integer OUTPUT_GRADE_03 = 3; // 3�N
	public final static Integer OUTPUT_GRADE_04 = 4; // 4�N
	public final static Integer OUTPUT_GRADE_05 = 5; // 5�N
	public final static Integer OUTPUT_GRADE_06 = 6; // 6�N

	/** �����R�[�h */
	private String userCode = DEFALUT_VALUE;

	/** �N�x */
	private String nendo = DEFALUT_VALUE;

	/** ���ʎx���w���t���O false:�ʏ�w���Atrue:���ʎx���w�� */
	private boolean spgdFlg;

	// ---------------------------------------------------------
	// ���[�o�͏���(���N�G�X�g�p���[���[�^)
	// ---------------------------------------------------------
	/** �I���o�͎��� */
	private String term = DEFALUT_VALUE;

	/** �I���o�͎����� */
	private String termName = DEFALUT_VALUE;

	/** �o�̓y�[�W �\�� */
	private boolean output_cover;

	/** �o�̓y�[�W �߂���(������) */
	private boolean output_purpose;

	/** �o�̓y�[�W ���e */
	private boolean output_contents;

	/** �o�̓y�[�W �C���� */
	private boolean output_deed;

	/** �o�͓��t */
	private String outputDate = DEFALUT_VALUE;

	/** �C�����t */
	private String deedDate = DEFALUT_VALUE;

	// ---------------------------------------------------------
	// ���[�󎚃f�[�^
	// ---------------------------------------------------------
	/** �\�� �\��C���[�W */
	private byte[] stampImage;

	/** �\�� �Z�̓C���[�W */
	byte[] schoolStampImage;

	/** �\�� �w�Z����ڕW�C���[�W */
	byte[] educationalGoalImage;









	/** ������� */
	public final static String OUTPUT_TERM_PREVIOUS = "01"; // �O��
	public final static String OUTPUT_TERM_LATE = "02"; // ���

	/** �g�E�o�Ȕԍ��̏o�̓��[�h */
	public final static String OUTPUT_HROOMINFO_NORMAL = "0"; // 0:�ݐЊw���̏��ň�, 1:�𗬐�w���̏��ň�, 2:�ݐЊw���y�ь𗬐�w���̏��ň�
	public final static String OUTPUT_HROOMINFO_GROUP = "1"; // �ݐЊw��������������ʎx���w���O���[�v���̂ƁA�ݐЊw���̏o�Ȕԍ����o�͂���(���x�w���������̂ݑI���\)
	public final static String OUTPUT_HROOMINFO_KOURYU = "2"; // �𗬐�̑g�E�o�Ȕԍ����o�͂���(���x�w���������̂ݑI���\)

	/** �o�͂���o�Ȕԍ��̎�� */
	public final static String OUTPUT_NUMBER_KOUBO = "0"; // cls_reference_number���o��
	public final static String OUTPUT_NUMBER_NORMAL = "1"; // cls_number���o��

	/** �g�E�o�Ȕԍ��̏o�̓��[�h */
	private String outputHroomInfoMode = OUTPUT_HROOMINFO_NORMAL;

	/** �o�Ȕԍ��̏o�͎�� */
	private String outputNumberKind = OUTPUT_NUMBER_NORMAL;

	/** ���k���̃f�[�^ */
	private Map<String, Print32179000StudentFormBean> studentMap;

	/** �C���ؐ��k���̃f�[�^ */
	private Map<String, Print32179000StudentFormBean> deedStudentMap;

	/** �w�Дԍ����X�g(�~���j */
	private List<String> studentList;

	/** �Z�����X�g(�~���j */
	private List<StaffEntity> principalList;

	/** �g�̂̂悤�� �� */
	private String healthrecordDate;

	/** �\�� �Z�̓C���[�W */
	private byte[] coverImage;

	/** �Z����C���[�W */
	byte[] principalStampImage;

	private Map<String, Boolean> notHyokaMap;

	private Map<String, Boolean> notHyoteiMap;

	/** ���󎚎�� */
	public final static String OUTPUT_NOMAL_PRINT = "0"; // 0:�ݐЊw���̏��ň�
	public final static String OUTPUT_ALTERNAT_PRINT = "1"; // 1:�𗬐�w���̏��ň�
	public final static String OUTPUT_BOTH_PRINT = "2"; // 2:�ݐЊw���y�ь𗬐�w���̏��ň�

	/** �������� */
	private String schoolName = DEFALUT_VALUE;

	/** �C���؂ɏo�͂���Z���� */
	private String deedPrincipalName;

	/** �o�̓y�[�W�w�K�̋L�^ */
	private boolean output_score;

	/** �𗬊w���t���O 0:�ݐЊw���̏��ň�, 1:�𗬐�w���̏��ň�, 2:�ݐЊw���y�ь𗬐�w���̏��ň� */
	private String alternatFlg = "0";
}
