package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.SchoolStampEntity;
import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.formbean.SpClassKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.BeanUtilManager;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ActviewEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000CommentEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ForeignLangActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000HealthrecordEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ScoreEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000SpActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000StudentEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000TotalActEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000StudentFormBean;

/**
 * <PRE>
 * ���ђʒm�\���(���R�s_���w�Z) ���  Service.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000Service extends AbstractExecuteQuery {

	/** Log4j */
	private static final Log log = LogFactory.getLog(Print32179000Service.class);

	/** ���sSQL */
	private static final String EXEC_SQL_SCHOOL_NAME = "cus/oyama/getPrint32179000Schoolname.sql";					// �w�Z��
	private static final String EXEC_SQL_PRINCIPAL_HISTORY = "cus/oyama/getPrint32179000PrincipalHistory.sql";		// �Z����
	private static final String EXEC_SQL_TEACHER_NAME = "cus/oyama/getPrint32179000StaffHistory.sql";				// �S�C��
	private static final String EXEC_SQL_TEACHER_NAME2 = "cus/oyama/getPrint32179000HroomStaff.sql";				// �S�C��
	private static final String EXEC_SQL_SP_TEACHER_NAME = "cus/oyama/getPrint32179000SpStaffHistory.sql";			// ���ʎx���w���S�C��
	private static final String EXEC_SQL_SP_TEACHER_NAME2 = "cus/oyama/getPrint32179000SpHroomStaff.sql";			// ���ʎx���w���S�C��
	private static final String EXEC_SQL_SCHOOLSTAMP = "sys/getSchoolStamp.sql";									// �Z��
	private static final String EXEC_SQL_STUDENT = "cus/oyama/getPrint32179000Student.sql";							// �Ώێ���
	private static final String EXEC_SQL_ATTEND = "cus/oyama/getPrint32179000Attend.sql";							// �o���̋L�^
	private static final String EXEC_SQL_ACT = "cus/oyama/getPrint32179000Act.sql";									// �s���̋L�^
	private static final String EXEC_SQL_SCORE = "cus/oyama/getPrint32179000Score.sql";								// �w�K�̋L�^
	private static final String EXEC_SQL_FOREIGN_LANGUAGE = "cus/oyama/getPrint32179000ForeignLangAct.sql";			// �O���ꊈ��
	private static final String EXEC_SQL_COMMENT = "cus/oyama/getPrint32179000Comment.sql";							// ��������
	private static final String EXEC_SQL_TOTAL_ACT = "cus/oyama/getPrint32179000TotalAct.sql";						// �����I�Ȋw�K�̋L�^
	private static final String EXEC_SQL_SEPCIAL_ACTIVITY = "cus/oyama/getPrint32179000SpAct.sql";					// ���ʊ����̋L�^
	private static final String EXEC_SQL_HEALTHRECORD_INFO = "cus/oyama/getPrint32179000Healthrecord.sql";			// ���N�̋L�^
	private static final String EXEC_SQL_SPGROUP_NAME = "cus/oyama/getPrint32179000SpGroupName.sql";				// ���ʎx���w���O���[�v����

	private static final String CHECK_ON = "1"; // �`�F�b�N�{�b�N�X:�`�F�b�N�L��

	/** �擾�C���[�W */
	private static final String STM_KIND_SCHOOL = "01";				// �Z��
	private static final String STM_KIND_TITLE = "02";				// �ʒm�[�\��
	private static final String STM_KIND_EDUCATIONAL_GOAL = "05";	// �w�Z����ڕW

	/** ����SessionBean */
	private SystemInfoBean sessionBean;

	/** �z�[�����[�����FormBean. */
	private HroomKeyFormBean hroomKeyFormBean;

	/** ���ʎx���w���N���X���FormBean. */
	private SpClassKeyFormBean spClassKeyFormBean;

	/** �I�����ꂽ���� */
	private String term;

	/** �I�𐶓k�̊w�Дԍ� */
	private List<String> selectedStucodeList;

	/** �I�����ꂽ���k��� */
	Map<String, Print32179000StudentFormBean> selectStudentMap;
	Map<String, Print32179000StudentFormBean> studentMap;
	Map<String, Print32179000StudentFormBean> deedStudentMap;

	/** ���FormBean */
	private Print32179000FormBean printFormBean;




	/** �擾�C���[�W ���s�v */
	private static final String STAMP_IMAGE_TITLE = "02"; // �ʒm�[�\��
	private static final String STAMP_PRINCIPAL = "03"; // �Z����
	private static final String STAMP_COVER_IMAGE = "04"; // �\���摜

	/**
	 * <pre>
	 * Request�̓��e��FormBean���쐬����
	 * ���̏�����Action.doAction���\�b�h���ŌĂяo�����
	 * </pre>
	 *
	 * @param request HTTP���N�G�X�g
	 * @param sessionBean �V�X�e�����Bean(�Z�b�V�������)
	 * @throws TnaviDbException DB��O�������ɃX���[�����
	 */
	public void execute(HttpServletRequest request, SystemInfoBean sessionBean) throws TnaviDbException {

		// ����Session��ݒ�
		this.sessionBean = sessionBean;

		// -----------------------------------------------
		// ���FormBean
		// -----------------------------------------------
		printFormBean = new Print32179000FormBean();

		// �����R�[�h
		printFormBean.setUserCode(sessionBean.getUserCode());
		// �N�x
		printFormBean.setNendo(sessionBean.getSystemNendoSeireki());

		// �z�[�����[�����FormBean��ݒ�
		hroomKeyFormBean = sessionBean.getSelectHroomKey();
		// ���ʎx���w�����FormBean��ݒ�
		spClassKeyFormBean = sessionBean.getSelectSpClassKey();

		// ���N�G�X�g�p�����[�^�̎擾
		// ���ԏ����擾�A�ݒ�
		term = request.getParameter("goptCode");
		printFormBean.setTerm(term);
		printFormBean.setTermName(request.getParameter("termName"));

		// �o�̓y�[�W���擾�A�ݒ�
		printFormBean.setOutput_cover(CHECK_ON.equals(request.getParameter("output_cover")));		// �\��
		printFormBean.setOutput_purpose(CHECK_ON.equals(request.getParameter("output_purpose")));	// �߂���(������)
		printFormBean.setOutput_contents(CHECK_ON.equals(request.getParameter("output_contents")));	// ���e
		printFormBean.setOutput_deed(CHECK_ON.equals(request.getParameter("output_deed")));			// �C����

		// �o�͓��t���擾�A�ݒ�
		printFormBean.setOutputDate(request.getParameter("output_year") + request.getParameter("output_month") + request.getParameter("output_day"));

		// �C�����t���擾�A�ݒ�
		printFormBean.setDeedDate(request.getParameter("deed_year") + request.getParameter("deed_month") + request.getParameter("deed_day"));

		// ���k�����擾�A�ݒ�
		this.selectedStucodeList = new ArrayList<String>();
		if (request.getParameter("stucode") != null) {
			String[] selectedStucodes = request.getParameter("stucode").split(",");
			getParamStucode(selectedStucodes, request);
		}

		// ���ʊw�����t���O
		if (spClassKeyFormBean != null) {
			printFormBean.setSpgdFlg(true);
		}






		// �o�̓y�[�W���擾�A�ݒ� ���s�v
		printFormBean.setOutput_cover(CHECK_ON.equals(request.getParameter("output_cover"))); // �\��
		printFormBean.setOutput_score(CHECK_ON.equals(request.getParameter("output_score"))); // �w�K�̋L�^
		printFormBean.setOutput_deed(CHECK_ON.equals(request.getParameter("output_deed"))); // �C����

		// �C�����t���擾�A�ݒ� ���s�v
		printFormBean.setDeedDate(sessionBean.getEntryNendoSeireki() + "0331");

		// �g�E�o�Ȕԍ��̏o�̓��[�h�擾
		String spHroomOutput = request.getParameter("spHroomOutput");
		if (spHroomOutput == null) {
			// �ʏ�ʂ�A�ݐЊw���̑g�E�o�Ȕԍ����o��
			spHroomOutput = Print32179000FormBean.OUTPUT_ALTERNAT_PRINT;
		}
		printFormBean.setAlternatFlg(spHroomOutput);


		// �N�G�������s����
		super.execute();
	}

	/**
	 * ���k����ݒ肷��
	 * 
	 * @param selectedStucodes �I�����ꂽ�w�Дԍ�
	 * @param request HTTP���N�G�X�g
	 */
	private void getParamStucode(String[] selectedStucodes, HttpServletRequest request) {
		// ���k��
		String stuCnt = request.getParameter("stuCnt");

		this.selectStudentMap = new HashMap<String, Print32179000StudentFormBean>();

		selectedStucodeList = new ArrayList<String>();
		// �I�����ꂽ���k���
		for (int i = 0; i < Integer.parseInt(stuCnt); i++) {
			for (String stucode : selectedStucodes) {
				// ���k���FormBean
				Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
				// �w�Дԍ��̎擾
				String stu = request.getParameter("stucode_" + i);
				// �I���w�Дԍ��Ɠ����w�Дԍ������`�F�b�N
				if (stucode.equals(stu)) {
					// �w�Дԍ�
					studentFormBean.setStuCode(stucode);
					// �w�N
					studentFormBean.setStuGrade(request.getParameter("grade_" + i));
					// �N���X�ԍ�
					studentFormBean.setStuClass(request.getParameter("class_" + i));
					// �w���ԍ�
					studentFormBean.setStuClano(request.getParameter("clsno_" + i));
					// �o�͊w�����
					this.selectStudentMap.put(stucode, studentFormBean);
					// ����Ώۊw�Дԍ��̎擾
					selectedStucodeList.add(stucode);
				}
			}
		}
		printFormBean.setStudentList(selectedStucodeList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doQuery() throws TnaviDbException {

		// �����R�[�h
		String user = sessionBean.getUserCode();
		// �N�x
		String nendoSeireki = sessionBean.getSystemNendoSeireki();
		// �z�[�����[���ԍ�
		String clsno = null;
		// �w�N
		String grade = null;
		try {

			// �w�Z�����擾
			getSchoolName(user, nendoSeireki);

			// �Z�������X�g���擾
			printFormBean.setPrincipalList(getPrincipalStaff(user, nendoSeireki, printFormBean.getOutputDate(), false));

			// �\�������
			if (printFormBean.isOutput_cover()) {

				// �ʒm�[�\��
				byte[] stampImage = getStampImage(user, STAMP_IMAGE_TITLE);
				printFormBean.setStampImage(stampImage);

				// �\���摜
				byte[] coverImage = getStampImage(user, STAMP_COVER_IMAGE);
				printFormBean.setCoverImage(coverImage);

			}

			studentMap = new HashMap<String, Print32179000StudentFormBean>();
			deedStudentMap = new HashMap<String, Print32179000StudentFormBean>();

			for (String selectStucode : selectedStucodeList) {

				// �z�[�����[���ԍ�
				clsno = this.selectStudentMap.get(selectStucode).getStuClano();
				// �w�N
				grade = this.selectStudentMap.get(selectStucode).getStuGrade();

				// �S�C�w�����FormBean��ݒ�
				if (hroomKeyFormBean != null) {

					// ���k�����擾
					getStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// �S�C�����擾
					getTeacherName(user, nendoSeireki, clsno, selectStucode);

					// �w�K�̋L�^�����
					if (printFormBean.isOutput_score()) {
						// �w�K�̋L�^
						getScoreList(user, nendoSeireki, grade, selectStucode);

						// �]���E�]�肵�Ȃ��R�[�h��Map
//						getNotHyokaMap(user, nendoSeireki, grade);
//						getNotHyoteiMap(user, nendoSeireki, grade);

						// �O���ꊈ���̋L�^
						getForeignlangActList(user, nendoSeireki, grade, selectStucode);

						// �����I�Ȋw�K�̎���
						getTotalAct(user, nendoSeireki, grade, selectStucode);

						// ���ʊ����̋L�^
						getSpecialActivity(user, nendoSeireki, grade, selectStucode);

						// �s���̋L�^
						getActviewList(user, nendoSeireki, grade, selectStucode);

						// ��������
						getComment(user, nendoSeireki, selectStucode);

						// �o���̋L�^
						getAttendList(user, nendoSeireki, grade, selectStucode);

						// ����̋L�^
						getHealthrecordList(user, nendoSeireki, grade, selectStucode);
					}
				}
				// ���ʎx���w�����FormBean��ݒ�
				if (spClassKeyFormBean != null) {

					// ���k�����擾
					getStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// �S�C�����擾
					getSpTeacherName(user, nendoSeireki, spClassKeyFormBean.getSpgcode(), selectStucode);

					// �𗬐�N���XNo
					String excClsNo = "";
					if (printFormBean != null && printFormBean.getStudentMap() != null
							&& printFormBean.getStudentMap().get(selectStucode) != null) {
						excClsNo = printFormBean.getStudentMap().get(selectStucode).getExcClsno();
					}
					if (excClsNo == null || excClsNo.isEmpty()) {
						excClsNo = clsno;
					}
					// �𗬐�S�C�����擾
					getTeacherName(user, nendoSeireki, excClsNo, selectStucode);
				}

				// �C���؏o�͎�
				if (printFormBean.isOutput_deed()) {

					// ���k�����擾
					getDeedStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// �Z�������擾
					List<StaffEntity> principalStaffList = getPrincipalStaff(user, nendoSeireki,
							printFormBean.getDeedDate(), true);
					if (principalStaffList.size() > 0) {
						printFormBean.setDeedPrincipalName(principalStaffList.get(0).getStf_name_w());
					}

					// �Z����
					if (printFormBean.getTerm().equals("02")) {
						// ����̏ꍇ�̂݁A�o��
						byte[] principalStampImage = getStampImage(user, STAMP_PRINCIPAL);
						printFormBean.setPrincipalStampImage(principalStampImage);
					}
				}
			}
		} catch (Exception e) {
			log.error("�ʒm�\����(�ʒm�\) DB�擾�����Ɏ��s���܂����B", e);
			throw new TnaviException(e);
		}

	}

	/**
	 * ���k�����擾����.
	 * 
	 * @param clsno
	 * @param nendoSeireki
	 * @param user
	 * @return ���k���̃��X�g
	 */
	private void getStudentInfoEntityList(String user, String nendo, String clsno, String stucode) {

		// �g�ɓ��ʎx���w���O���[�v���̂��o�͂���ꍇ�̓f�[�^���擾���Ă���
		String spGroupName = "";
		if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
			spGroupName = getSpGroupName(user, nendo, clsno);
		}

		// ���k�����擾
		Print32179000StudentEntity entity = getStudentList(user, nendo, clsno, stucode, printFormBean.getOutputDate());

		// ���kFormBean�ɃZ�b�g
		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();
		try {

			Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
			beanUtil.copyProperties(studentFormBean, entity);

			// �����R�[�h
			studentFormBean.setUsercode(user);
			// �N�x
			studentFormBean.setNendo(nendo);

			// �z�[�����[�����̂�ݒ�
			studentFormBean.setStuClass(this.selectStudentMap.get(stucode).getStuClass());

			if (printFormBean.isSpgdFlg()) {
				if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
					// ���ʎx���w���O���[�v���̂��o�͂���ꍇ�́A�g�������ւ�
					studentFormBean.setStuClass(spGroupName.replace("�g", ""));

				} else if (printFormBean.getOutputHroomInfoMode()
						.equals(Print32179000FormBean.OUTPUT_HROOMINFO_KOURYU)) {
					// �𗬐�̑g�E�o�Ȕԍ����o�͂���ꍇ�͑g�A�o�Ȕԍ��������ւ�
					studentFormBean.setStuClass(entity.getExcClass());
					studentFormBean.setStuNumber(entity.getExcNumber());
				}

				if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_KOUBO)) {
					// �o�Ȕԍ��i����j�ŏo��
					studentFormBean.setStuNumber(entity.getRefNumber());
				} else if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_NORMAL)) {
					// �o�Ȕԍ��i�𗬁j�ŏo�͂̏ꍇ�́A�ύX�Ȃ�
				}
			} else {
				studentFormBean.setStuClass(entity.getStuClass());
				studentFormBean.setStuNumber(entity.getStuNumber());
			}
			this.studentMap.put(entity.getStuCode(), studentFormBean);
		} catch (Exception e) {
			log.error("BeanUtils�ϊ��G���[:Print32179000StudentEntity To Print32179000StudentFormBean");
			throw new TnaviException(e);
		}
		printFormBean.setStudentMap(studentMap);
	}

	/**
	 * ���k�����擾����.
	 * 
	 * @param clsno
	 * @param nendoSeireki
	 * @param user
	 * @return ���k���̃��X�g
	 */
	private void getDeedStudentInfoEntityList(String user, String nendo, String clsno, String stucode) {

		// �g�ɓ��ʎx���w���O���[�v���̂��o�͂���ꍇ�̓f�[�^���擾���Ă���
		String spGroupName = "";
		if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
			spGroupName = getSpGroupName(user, nendo, clsno);
		}

		// ���k�����擾
		Print32179000StudentEntity entity = getStudentList(user, nendo, clsno, stucode, printFormBean.getDeedDate());

		// ���kFormBean�ɃZ�b�g
		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();
		try {

			Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
			beanUtil.copyProperties(studentFormBean, entity);

			// �����R�[�h
			studentFormBean.setUsercode(user);
			// �N�x
			studentFormBean.setNendo(nendo);

			// �z�[�����[�����̂�ݒ�
			studentFormBean.setStuClass(this.selectStudentMap.get(stucode).getStuClass());

			if (printFormBean.isSpgdFlg()) {
				if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
					// ���ʎx���w���O���[�v���̂��o�͂���ꍇ�́A�g�������ւ�
					studentFormBean.setStuClass(spGroupName.replace("�g", ""));

				} else if (printFormBean.getOutputHroomInfoMode()
						.equals(Print32179000FormBean.OUTPUT_HROOMINFO_KOURYU)) {
					// �𗬐�̑g�E�o�Ȕԍ����o�͂���ꍇ�͑g�A�o�Ȕԍ��������ւ�
					studentFormBean.setStuClass(entity.getExcClass());
					studentFormBean.setStuNumber(entity.getExcNumber());
				}

				if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_KOUBO)) {
					// �o�Ȕԍ��i����j�ŏo��
					studentFormBean.setStuNumber(entity.getRefNumber());
				} else if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_NORMAL)) {
					// �o�Ȕԍ��i�𗬁j�ŏo�͂̏ꍇ�́A�ύX�Ȃ�
				}
			} else {
				studentFormBean.setStuClass(entity.getStuClass());
				studentFormBean.setStuNumber(entity.getStuNumber());
			}
			this.deedStudentMap.put(entity.getStuCode(), studentFormBean);
		} catch (Exception e) {
			log.error("BeanUtils�ϊ��G���[:Print32179000StudentEntity To Print32179000StudentFormBean");
			throw new TnaviException(e);
		}
		printFormBean.setDeedStudentMap(deedStudentMap);
	}

	/**
	 * ���ʎx���w���O���[�v���̂��擾����
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param clsno �z�[�����[���ԍ�
	 * @return ���ʎx���w���O���[�v����
	 */
	private String getSpGroupName(String user, String nendo, String clsno) {
		Object[] param = { user, nendo, clsno };
		QueryManager qm = new QueryManager(EXEC_SQL_SPGROUP_NAME, param, String.class);
		return (String) this.executeQuery(qm);
	}

	/**
	 * �����ꗗ���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param clsno �N���X�ԍ�
	 * @return �����ꗗ���X�g
	 */
	private Print32179000StudentEntity getStudentList(String user, String nendo, String clsno, String stucode,
			String date) {
		// �p�����[�^�̐���
		Object[] param = { user, nendo, clsno, user, nendo, clsno, user, date, user, date };

		// QueryManager�̍쐬
		QueryManager queryManager = new QueryManager(EXEC_SQL_STUDENT, param, Print32179000StudentEntity.class);
		StringBuffer plusSQL = new StringBuffer();
		plusSQL.append(" WHERE cls_stucode = '" + stucode + "'");
		plusSQL.append(" ORDER BY cls_number");
		queryManager.setPlusSQL(plusSQL.toString());

		// �N�G���̎��s(List�`���Ńf�[�^�i�[)
		@SuppressWarnings("unchecked")
		List<Print32179000StudentEntity> studentList = (List<Print32179000StudentEntity>) this
				.executeQuery(queryManager);

		if (studentList != null) {
			return studentList.get(0);
		}

		return null;
	}

	/**
	 * �w�Z������������w�Z�������擾����.<br/>
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param date ���
	 * @return �Z�����̃��X�g�i�~���j
	 */
	private List<StaffEntity> getPrincipalStaff(String user, String nendo, String date, boolean deedFlg) {

		StringBuffer plusSql = new StringBuffer();
		StringBuffer startDate = new StringBuffer();
		startDate.append(sessionBean.getSystemNendoSeireki());
		startDate.append(sessionBean.getSystemNendoStartDate());
		Object[] param = { user, startDate.toString(), date };
		QueryManager queryManager = new QueryManager(EXEC_SQL_PRINCIPAL_HISTORY, param, StaffEntity.class);
		plusSql.append(" ORDER BY prh_start DESC");
		queryManager.setPlusSQL(plusSql.toString());

		@SuppressWarnings("unchecked")
		List<StaffEntity> principalList = (List<StaffEntity>) this.executeQuery(queryManager);

		return principalList;
	}

	/**
	 * �N���X�S�C��������S�C�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param clsno �N���X�ԍ�
	 */
	private void getTeacherName(String user, String nendo, String clsno, String stucode) {

		StringBuffer startDate = new StringBuffer();
		startDate.append(sessionBean.getSystemNendoSeireki());
		startDate.append(sessionBean.getSystemNendoStartDate());
		Object[] param = { user, nendo, clsno, startDate.toString(), printFormBean.getOutputDate() };
		QueryManager queryManager = new QueryManager(EXEC_SQL_TEACHER_NAME, param, StaffEntity.class);

		@SuppressWarnings("unchecked")
		List<StaffEntity> staffList = (List<StaffEntity>) this.executeQuery(queryManager);

		// �S�C�������Ȃ������ꍇ
		if (staffList.size() == 0) {
			param = new Object[] { user, nendo, clsno };
			queryManager = new QueryManager(EXEC_SQL_TEACHER_NAME2, param, StaffEntity.class);

			staffList = (List<StaffEntity>) this.executeQuery(queryManager);
		}

		printFormBean.getStudentMap().get(stucode).setStaffList(staffList);
	}

	/**
	 * �N���X�S�C��������S�C�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param clsno �N���X�ԍ�
	 */
	private void getSpTeacherName(String user, String nendo, String spgdCode, String stucode) {

		StringBuffer startDate = new StringBuffer();
		startDate.append(sessionBean.getSystemNendoSeireki());
		startDate.append(sessionBean.getSystemNendoStartDate());
		Object[] param = { user, nendo, startDate.toString(), printFormBean.getOutputDate(), spgdCode, user };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SP_TEACHER_NAME, param, StaffEntity.class);

		@SuppressWarnings("unchecked")
		List<StaffEntity> staffList = (List<StaffEntity>) this.executeQuery(queryManager);

		// �S�C�������Ȃ������ꍇ
		if (staffList.size() == 0) {
			param = new Object[] { spgdCode, user, nendo, user };
			queryManager = new QueryManager(EXEC_SQL_SP_TEACHER_NAME2, param, StaffEntity.class);

			staffList = (List<StaffEntity>) this.executeQuery(queryManager);
		}

		printFormBean.getStudentMap().get(stucode).setSpStaffList(staffList);
	}

	/**
	 * �w�Z�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 */
	private void getSchoolName(String user, String nendo) {

		Object[] param = { sessionBean.getUseKind2(), user, nendo };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SCHOOL_NAME, param, String.class);

		String schoolName = (String) this.executeQuery(queryManager);

		printFormBean.setSchoolName(schoolName);
	}

	/**
	 * �w�Z��C���[�W���擾����
	 * 
	 * @param user �����R�[�h
	 * @param kind ��� "01":�Z�́A"02":�ʒm�[�\��A"03":�Z����
	 * @return �Z�̓C���[�W
	 */
	private byte[] getStampImage(String user, String kind) {
		Object[] param = { user, kind };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SCHOOLSTAMP, param, SchoolStampEntity.class);

		@SuppressWarnings("unchecked")
		List<SchoolStampEntity> schoolStampEntityList = (List<SchoolStampEntity>) this.executeQuery(queryManager);

		if (schoolStampEntityList.size() > 0) {
			return schoolStampEntityList.get(0).getStm_image();
		}

		return null;
	}

	/**
	 * �w�K�̋L�^���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getScoreList(String user, String nendoSeireki, String grade, String stucode) {

		Object[] param = { user, nendoSeireki, grade, term, stucode };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SCORE, param, Print32179000ScoreEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000ScoreEntity> scoreList = (List<Print32179000ScoreEntity>) this.executeQuery(queryManager);

		printFormBean.getStudentMap().get(stucode).setScoreList(scoreList);
	}

	/**
	 * �O���ꊈ�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getForeignlangActList(String user, String nendoSeireki, String grade, String stucode) {
		Object[] paramArray = { term, user, nendoSeireki, grade, stucode, user, nendoSeireki, grade, term, stucode };

		QueryManager queryManager = new QueryManager(EXEC_SQL_FOREIGN_LANGUAGE, paramArray,
				Print32179000ForeignLangActEntity.class);
		@SuppressWarnings("unchecked")
		List<Print32179000ForeignLangActEntity> foreignLangActList = (List<Print32179000ForeignLangActEntity>) this
				.executeQuery(queryManager);

		for (Print32179000ForeignLangActEntity entity : foreignLangActList) {
			printFormBean.getStudentMap().get(entity.getCls_stucode()).getForeignLangActList().add(entity);
		}
	}

	/**
	 * �����I�Ȋw�K�̋L�^���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getTotalAct(String user, String nendoSeireki, String grade, String stucode) {

		Object[] paramArray = { user, nendoSeireki, grade, term, stucode };
		QueryManager queryManager = new QueryManager(EXEC_SQL_TOTAL_ACT, paramArray, Print32179000TotalActEntity.class);
		@SuppressWarnings("unchecked")
		List<Print32179000TotalActEntity> totalActList = (List<Print32179000TotalActEntity>) executeQuery(queryManager);

		for (Print32179000TotalActEntity entity : totalActList) {
			printFormBean.getStudentMap().get(entity.getRtav_stucode()).setTotalAct(entity);
		}
	}

	/**
	 * ���ʊ����̋L�^���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getSpecialActivity(String user, String nendoSeireki, String grade, String stucode) {

		Object[] paramArray = { nendoSeireki, stucode, user, grade, printFormBean.getTerm() };

		QueryManager queryManager = new QueryManager(EXEC_SQL_SEPCIAL_ACTIVITY, paramArray,
				Print32179000SpActEntity.class);
		@SuppressWarnings("unchecked")
		List<Print32179000SpActEntity> spActList = (List<Print32179000SpActEntity>) executeQuery(queryManager);

		for (Print32179000SpActEntity entity : spActList) {
			printFormBean.getStudentMap().get(stucode).getSpActList().add(entity);
		}
	}

	/**
	 * �w�Z�����̂悤�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getActviewList(String user, String nendoSeireki, String grade, String stucode) {
		Object[] paramArray = { term, user, nendoSeireki, grade, stucode };

		QueryManager queryManager = new QueryManager(EXEC_SQL_ACT, paramArray, Print32179000ActviewEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000ActviewEntity> actList = (List<Print32179000ActviewEntity>) this.executeQuery(queryManager);

		for (Print32179000ActviewEntity entity : actList) {
			printFormBean.getStudentMap().get(stucode).getActviewList().add(entity);
		}
	}

	/**
	 * �����������擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getComment(String user, String nendoSeireki, String stucode) {

		Object[] paramArray = { user, nendoSeireki, term, stucode };

		QueryManager queryManager = new QueryManager(EXEC_SQL_COMMENT, paramArray, Print32179000CommentEntity.class);
		queryManager.setPlusSQL(" AND rcom_stucode IN(" + createIN(selectedStucodeList) + ") ");
		queryManager.setPlusSQL(" ORDER BY rcom_stucode ");

		@SuppressWarnings("unchecked")
		List<Print32179000CommentEntity> commentList = (List<Print32179000CommentEntity>) executeQuery(queryManager);

		for (Print32179000CommentEntity entity : commentList) {
			printFormBean.getStudentMap().get(entity.getRcom_stucode()).setComment(entity);
		}

	}

	/**
	 * �o���̋L�^�̏W�v���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 * @return �o���̋L�^�̃��X�g
	 */
	private void getAttendList(String user, String nendoSeireki, String grade, String stucode) {

		Object[] paramArray = { grade, stucode, user, nendoSeireki, user, nendoSeireki, stucode };

		QueryManager queryManager = new QueryManager(EXEC_SQL_ATTEND, paramArray, Print32179000AttendEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000AttendEntity> attendList = (List<Print32179000AttendEntity>) this.executeQuery(queryManager);

		// �����f���v�pEntity
		Print32179000AttendEntity totalAttendEntity = new Print32179000AttendEntity();
		// �w�Дԍ�
		totalAttendEntity.setStucode(stucode);
		// �����R�[�h
		totalAttendEntity.setGoptcode("02");
		// ��
		totalAttendEntity.setMonth("99");

		// ���Ɠ����i���v�j
		int teachingSum = 0;
		// �o�ȓ����i���v�j
		int attendSum = 0;
		// ���ȓ����i���v�j
		int absenceSum = 0;
		// �o����������i���v�j
		int stopSum = 0;
		// �x�������i���v�j
		int lateSum = 0;
		// ���ޓ����i���v�j
		int leaveSum = 0;
		// �ݐД���
		String onRegist = null;

		for (Print32179000AttendEntity entity : attendList) {
			printFormBean.getStudentMap().get(stucode).getAttendList().add(entity);

			// ���Ɠ����i���v�j
			teachingSum += entity.getTeachingSum();
			// �o�ȓ����i���v�j
			attendSum += entity.getAttendSum();
			// ���ȓ����i���v�j
			absenceSum += entity.getAbsenceSum();
			// �o����������i���v�j
			stopSum += entity.getStopSum();
			// �x�������i���v�j
			lateSum += entity.getLateSum();
			// ���ޓ����i���v�j
			leaveSum += entity.getLeaveSum();
			// �ݐД���
			onRegist = entity.getOnRegist();
		}
		// ���Ɠ����i���v�j
		totalAttendEntity.setTeachingSum(teachingSum);
		// �o�ȓ����i���v�j
		totalAttendEntity.setAttendSum(attendSum);
		// ���ȓ����i���v�j
		totalAttendEntity.setAbsenceSum(absenceSum);
		// �o����������i���v�j
		totalAttendEntity.setStopSum(stopSum);
		// �x�������i���v�j
		totalAttendEntity.setLateSum(lateSum);
		// ���ޓ����i���v�j
		totalAttendEntity.setLeaveSum(leaveSum);
		// �ݐД���
		totalAttendEntity.setOnRegist(onRegist);
		// ���v�s�̒ǉ�
		printFormBean.getStudentMap().get(stucode).getAttendList().add(totalAttendEntity);

	}

	/**
	 * �g�̂̂悤�����擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendoSeireki �N�x
	 * @param grade �w�N
	 */
	private void getHealthrecordList(String user, String nendoSeireki, String grade, String stucode) {

		Object[] paramArray = { user, nendoSeireki, term, stucode };
		QueryManager queryManager = new QueryManager(EXEC_SQL_HEALTHRECORD_INFO, paramArray,
				Print32179000HealthrecordEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000HealthrecordEntity> entityList = (List<Print32179000HealthrecordEntity>) executeQuery(
				queryManager);

		for (Print32179000HealthrecordEntity entity : entityList) {
			printFormBean.getStudentMap().get(entity.getHlr_stucode()).setHealthrecord(entity);
		}

	}

	/**
	 * ���FormBean���擾����B
	 * 
	 * @return printFormBean
	 */
	public final Print32179000FormBean getPrintFormBean() {
		return printFormBean;
	}
}
