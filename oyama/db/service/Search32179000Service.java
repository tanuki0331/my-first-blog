package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.CmlguideoutputtermEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.common.formbean.SpClassKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000StudentEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32179000FormBean;

/**
 * <PRE>
 * ���ђʒm�\���(���R�s_���w�Z) ���� Service.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Search32179000Service extends AbstractExecuteQuery {

	/** Log4j */
	private static final Log log = LogFactory.getLog(Search32179000Service.class);

	/** ���sSQL */
	private static final String EXEC_SQL_TERM = "cus/oyama/getPrint32179000TermList.sql";				// �o�͎���
	private static final String EXEC_SQL_STUDENT = "cus/oyama/getPrint32179000StudentList.sql";			// �Ώێ���
	private static final String EXEC_SQL_SP_STUDENT = "cus/oyama/getPrint32179000SpStudentList.sql";	// �Ώێ���(���ʎx���w��)

	/** ����SessionBean */
	private SystemInfoBean sessionBean;

	/** �ꗗFormBean. */
	private Search32179000FormBean searchFormBean;

	/** �z�[�����[�����FormBean. */
	private HroomKeyFormBean hroomKeyFormBean;

	/** ���ʎx���w�����FormBean */
	SpClassKeyFormBean spClassKeyFormBean;

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

		// �z�[�����[�����FormBean��ݒ�
		hroomKeyFormBean = sessionBean.getSelectHroomKey();

		// ���ʎx���w�����擾
		spClassKeyFormBean = sessionBean.getSelectSpClassKey();

		// �N�G�������s����
		super.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doQuery() throws TnaviDbException {

		// �N�x
		String nendo = sessionBean.getSystemNendoSeireki();
		// �����R�[�h
		String user = sessionBean.getUserCode();
		// �N���X��
		String hmrName = null;
		// �N���X�ԍ�
		String clsno = null;
		// ���ʎx���w�����ʗp�t���O
		boolean isSpSupportCls = false;

		// �����ꗗ
		List<Print32179000StudentEntity> studentList = new ArrayList<>();
		try {
			// ------------------------------------------------------------------------------------------
			// �ꗗFormBean�̍쐬
			// ------------------------------------------------------------------------------------------
			searchFormBean = new Search32179000FormBean();

			// ���ʎx���w���̏ꍇ
			if (spClassKeyFormBean != null) {
				// ���ʎx���w���R�[�h
				String spgcode = spClassKeyFormBean.getSpgcode();
				// �N���X��
				hmrName = spClassKeyFormBean.getSpgname();
				// ���ʎx���w���������k���擾
				studentList = getSpSupportList(user, nendo, spgcode);
				// ���ʎx���w�����ʗp�t���O �擾
				isSpSupportCls = true;
			}
			// �S�C�w���̏ꍇ
			else if (hroomKeyFormBean != null) {
				// ------------------------------------------------------------------------------------------
				// �z�[�����[����� �擾
				// ------------------------------------------------------------------------------------------
				// �N���X��
				hmrName = hroomKeyFormBean.getHmrname();
				// �N���X�ԍ�
				clsno = hroomKeyFormBean.getClsno();
				// �w�����擾
				studentList = getStudentList(user, nendo, clsno);
			}

			// ���ݓ��t�Z�b�g
			String today = DateUtility.getSystemDate();
			// �o�͓��t
			searchFormBean.setOutput_year(today.substring(0, 4));
			searchFormBean.setOutput_month(today.substring(4, 6));
			searchFormBean.setOutput_day(today.substring(6));
			// �C�����t
			searchFormBean.setDeed_year(today.substring(0, 4));
			searchFormBean.setDeed_month(today.substring(4, 6));
			searchFormBean.setDeed_day(today.substring(6));

			// ------------------------------------------------------------------------------------------
			// �o�͎��� �擾
			// ------------------------------------------------------------------------------------------
			List<CmlguideoutputtermEntity> termList = getTermList(user, nendo);
			List<SimpleTagFormBean> outputTermList = getOutputTermList(termList);

			// ------------------------------------------------------------------------------------------
			// �o�͓��t �擾
			// ------------------------------------------------------------------------------------------
			List<SimpleTagFormBean> monthList = getOutputDateMonth();
			List<SimpleTagFormBean> dayList = getOutputDateDay();

			// �o�͎����Əo���W�v���Ԃ̃`�F�b�N���ʎ擾
			Map<String, String> errorMessage = getErrorMessage(termList);

			// ------------------------------------------------------------------------------------------
			// �擾�����f�[�^��FormBean�֐ݒ�
			// ------------------------------------------------------------------------------------------
			// �N�x
			searchFormBean.setNendo(nendo);
			// �N���X��
			searchFormBean.setHmrName(hmrName);
			// �o�͎���
			searchFormBean.setOutputTermList(outputTermList);
			// �o�͓��t ��
			searchFormBean.setMonthList(monthList);
			// �o�͓��t ��
			searchFormBean.setDayList(dayList);
			// �o�͎����Əo���W�v���Ԃ̃`�F�b�N����
			searchFormBean.setErrorMessage(errorMessage);
			// ���k�ʒʒm�\���
			searchFormBean.setStudentList(studentList);
			// ���ʎx���w���t���O
			searchFormBean.setSpSupportCls(isSpSupportCls);
			// ���k�̈ꗗ��
			searchFormBean.setCntStu(studentList.size());

		} catch (Exception e) {
			log.error("���ђʒm�[���(���R�s_���w�Z) DB�擾�����Ɏ��s���܂����B", e);
			throw new TnaviException(e);
		}
	}

	/**
	 * �ꗗFormBean���擾����.
	 * 
	 * @return
	 */
	public Search32179000FormBean getListFormBean() {
		return searchFormBean;
	}

	/**
	 * �w�����X�g���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @return �w�����X�g
	 */
	private List<CmlguideoutputtermEntity> getTermList(String user, String nendo) {
		// �p�����[�^�̐���
		Object[] paramTag = { user, nendo };

		// QueryManager�̍쐬
		QueryManager queryManager = new QueryManager(EXEC_SQL_TERM, paramTag, CmlguideoutputtermEntity.class);

		// �N�G���̎��s(List�`���Ńf�[�^�i�[)
		@SuppressWarnings("unchecked")
		List<CmlguideoutputtermEntity> entityTermList = (List<CmlguideoutputtermEntity>) this.executeQuery(queryManager);

		return entityTermList;
	}

	/**
	 * �o�͎������擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @return �o�͎������X�g
	 */
	private List<SimpleTagFormBean> getOutputTermList(List<CmlguideoutputtermEntity> entityTermList) {

		List<SimpleTagFormBean> termList = new ArrayList<SimpleTagFormBean>();

		for (CmlguideoutputtermEntity entityTerm : entityTermList) {
			termList.add(new SimpleTagFormBean(entityTerm.getGopt_goptcode(), entityTerm.getGopt_name()));
		}

		return termList;
	}

	/**
	 * �o�͓��t���̃��X�g�𐶐�����.
	 * 
	 * @return �o�͓��t���̃��X�g
	 */
	private List<SimpleTagFormBean> getOutputDateMonth() {
		Format decFmt = new DecimalFormat("00");
		List<SimpleTagFormBean> monthList = new ArrayList<SimpleTagFormBean>();
		for (int i = 1; i < 12 + 1; i++) {
			monthList.add(new SimpleTagFormBean(decFmt.format(i), String.valueOf(i)));
		}
		return monthList;
	}

	/**
	 * �o�͓��t���̃��X�g�𐶐�����.
	 * 
	 * @return �o�͓��t���̃��X�g
	 */
	private List<SimpleTagFormBean> getOutputDateDay() {
		Format decFmt = new DecimalFormat("00");
		List<SimpleTagFormBean> dayList = new ArrayList<SimpleTagFormBean>();
		for (int i = 1; i < 31 + 1; i++) {
			dayList.add(new SimpleTagFormBean(decFmt.format(i), String.valueOf(i)));
		}
		return dayList;
	}

	/**
	 * �����ꗗ���擾����.
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param clsno �N���X�ԍ�
	 * @return �����ꗗ���X�g
	 */
	private List<Print32179000StudentEntity> getStudentList(String user, String nendo, String clsno) {
		// �p�����[�^�̐���
		Object[] param = { user, nendo, clsno };

		// QueryManager�̍쐬
		QueryManager queryManager = new QueryManager(EXEC_SQL_STUDENT, param, Print32179000StudentEntity.class);

		// �N�G���̎��s(List�`���Ńf�[�^�i�[)
		@SuppressWarnings("unchecked")
		List<Print32179000StudentEntity> tempStudentList = (List<Print32179000StudentEntity>) this.executeQuery(queryManager);

		String oldStucode = "";

		List<Print32179000StudentEntity> studentList = new ArrayList<Print32179000StudentEntity>();
		for (int i = 0; i < tempStudentList.size(); i++) {
			if (oldStucode.equals(tempStudentList.get(i).getStuCode())) {
				if (tempStudentList.get(i).getIsKoryu().equals("1")) {
					if (studentList != null && studentList.size() != 0) {
						studentList.remove(i - 1);
						studentList.add(tempStudentList.get(i));
					}
				}
			} else {
				studentList.add(tempStudentList.get(i));
			}

			oldStucode = tempStudentList.get(i).getStuCode();
		}

		// ���ʎx���w���������k���̎擾
		return studentList;
	}

	/**
	 * ���ʎx���w���������k�����擾����
	 * 
	 * @param user �����R�[�h
	 * @param nendo �N�x
	 * @param spgdcode ���ʎx���w���R�[�h
	 * @return ���ʎx���w���������k�ꗗ���X�g
	 */
	private List<Print32179000StudentEntity> getSpSupportList(String user, String nendo, String spgdcode) {

		// �p�����[�^�̐���
		Object[] param = { spgdcode, user, nendo };

		// QueryManager�̍쐬
		QueryManager queryManager = new QueryManager(EXEC_SQL_SP_STUDENT, param, Print32179000StudentEntity.class);

		// �N�G���̎��s(List�`���Ńf�[�^�i�[)
		@SuppressWarnings("unchecked")
		List<Print32179000StudentEntity> tempStudentList = (List<Print32179000StudentEntity>) this.executeQuery(queryManager);

		String oldStucode = "";

		List<Print32179000StudentEntity> studentList = new ArrayList<>();
		for (int i = 0; i < tempStudentList.size(); i++) {
			if (oldStucode.equals(tempStudentList.get(i).getStuCode())) {
				if (tempStudentList.get(i).getIsKoryu().equals("1")) {
					if (studentList != null && studentList.size() != 0) {
						studentList.remove(i - 1);
						studentList.add(tempStudentList.get(i));
					}
				}
			} else {
				studentList.add(tempStudentList.get(i));
			}
			oldStucode = tempStudentList.get(i).getStuCode();
		}
		// ���ʎx���w���������k���̎擾
		return studentList;
	}

	/**
	 * �o�͎����Əo���W�v���Ԃ̃`�F�b�N����
	 * 
	 * @param termList ���ђʒm�\�o�͎������X�g
	 * @return key:�o�͎����R�[�h�Avalue:�o�̓��b�Z�[�W
	 */
	private Map<String, String> getErrorMessage(List<CmlguideoutputtermEntity> termList) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (CmlguideoutputtermEntity term : termList) {
			StringBuilder errMsg = new StringBuilder();
			String currentTermId = term.getGopt_goptcode();
			for (CmlguideoutputtermEntity tempTerm : termList) {
				String tempTermId = tempTerm.getGopt_goptcode();
				String tempTermName = tempTerm.getGopt_name();
				boolean errorFlag = tempTermId.compareTo(currentTermId) <= 0 &&
						(StringUtils.isBlank(tempTerm.getGopt_attend_start()) || StringUtils.isBlank(tempTerm.getGopt_attend_end()));
				if (errorFlag) {
					if (errMsg.length() > 0) {
						errMsg.append("�A");
					}
					errMsg.append(tempTermName);
				}
			}
			if (errMsg.length() > 0) {
				errMsg.append("�̏o���W�v���Ԃ��ݒ肳��Ă��܂���B");
				map.put(currentTermId, errMsg.toString());
			}
		}

		return map;
	}
}
