/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.print;

import java.awt.Color;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.CrLayer;
import jp.co.hos.coreports.constants.CorLineStyle;
import jp.co.hos.coreports.constants.CorUnit;
import jp.co.hos.coreports.object.CrLine;
import jp.co.hos.coreports.object.CrListField;
import jp.co.hos.coreports.object.CrText;
import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.att.formbean.AttPrintStudentFormBean;
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32177000EnforceEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32177000FormBean;


/**
 * �o�ȕ���(���ʎx���w���@���R�s�p) �o�̓N���X.
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32177000 extends AbstractPdfManagerAdditionPagesCR {

	/**log4j*/
	private final Log log = LogFactory.getLog(Print32177000.class);

	/** �V�X�e�����Bean */
	private SystemInfoBean sessionBean;

	/** ���C���I�u�W�F�N�g */
	private static final String LAYER_ATTENDANCE = "Layer_Attendance";
	private static final String LAYER_SUMMARY = "Layer_Summary";
	private static final String LAYER_FOOTER = "Layer_Footer";
	private static final String LAYER_FOOTER_CHU = "Layer_Footer_chu";

	/** ���C�A�E�g�I�u�W�F�N�g */
	private static final String HEADER_YEAR_MONTH = "HeaderYearMonth";
	private static final String HEADER_TANNIN_NAME = "HeaderTanninName";			// �w�b�_�@�w���S�C����
	private static final String HEADER_GRADE_CLASS = "HeaderGradeClass";			// �w�b�_�@�w�N�g

	private static final String DAY_WEEK_LIST = "DayWeekList";						// ���E�j�����X�g�t�B�[���h
	private static final String STUDENT_LIST = "StudentList";						// ���k�����E�ԃ��X�g�t�B�[���h
	private static final String ATTEND_LIST = "AttendList";							// �o�������X�g�t�B�[���h
	private static final String ATTEND_SUM_LIST = "AttendSumList";					// �o���W�v���X�g�t�B�[���h

	private  String TOTAL_ATTEND = "TotalAttend";									// �������v���X�g�t�B�[���h
	private  String ENFORCE_DAY_SUM_LIST = "EnforceDaySumList";						// �������v�E���Ǝ���


	/** �o�Ȏ��ɒu�������镶���� */
	private static final String ATTEND_KIND_00_DISP = "�@";

	/** �o�������X�g�t�B�[���h */
	private CrText [][] attendListFielddArray;

	/** ���FormBean */
	private Print32177000FormBean printFormBean;

	private int[][] enforceState = null ; //����̈�ł̎��Ɠ��ݒ�`�F�b�N�p

	protected DateFormatUtility dateFormatUtility = null;


	/**
	 * �R���X�g���N�^
	 * @param sessionBean �V�X�e�����Bean
	 * @param printFormBean ���FormBean
	 */
	public Print32177000(SystemInfoBean sessionBean, Print32177000FormBean printFormBean) {
		super();
		this.sessionBean = sessionBean;
		this.printFormBean = printFormBean;
	}

	@Override
	protected void doPrint() throws TnaviPrintException {

		try {

			//---------------------------------------
			// �o�ȕ�y�[�W
			//---------------------------------------
			form.initialize();//�t�H�[��������

			getCrLayerSetVisibleAtPrint(LAYER_ATTENDANCE, true);
			getCrLayerSetVisibleAtPrint(LAYER_SUMMARY   , false);

			// �󎚂��郌�C�������ɂ���
			if(sessionBean.getUseKind2().equals("1")) {
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER, true);
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER_CHU, false);

			}else {
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER, false);
				getCrLayerSetVisibleAtPrint(LAYER_FOOTER_CHU, true);
				TOTAL_ATTEND = TOTAL_ATTEND + "1";
				ENFORCE_DAY_SUM_LIST = ENFORCE_DAY_SUM_LIST + "1";

			}

			CrListField attendListField = getCrListField(ATTEND_LIST);
			this.attendListFielddArray = getListFieldGetColumns(attendListField);
			this.dateFormatUtility = new DateFormatUtility(sessionBean.getUserCode());


			// �w�b�_���o��
			outputDetailHeader(1);

			// �o���f�[�^�o��
			outputAttend();

			// �������v���̏o��
			outputStatistics();

			// ����Ɠ��E�x�Ɠ��E�w�������̎������o��
			outputNotEnforcedayLine();

			form.printOut();


		} catch (Exception e) {
			log.error("��O�����@���[�쐬���s",e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * ���׃w�b�_�����̏o��
	 * @throws Exception
	 */
	private void outputDetailHeader(int page) throws Exception{

		DateFormatUtility dfu = new DateFormatUtility(sessionBean.getUserCode());
		dfu.setZeroToSpace(true);

		if(page == 1){

			// ���E�j�����X�g�t�B�[���h
			CrListField dayWeekListField = getCrListField(DAY_WEEK_LIST);
			CrText [][] dayWeekListFieldArray = getListFieldGetColumns(dayWeekListField);

			// �Ώ۔N�E��
			String targetYearMonth = printFormBean.getTargetYearMonth();

			// �Ώی�
			String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");

			String year_month = dateFormatUtility.formatDate("YYYY�N",1,targetYearMonth) + targetMonth + "��";


			// �N�����o��
			getFieldSetData(HEADER_YEAR_MONTH, year_month);

			// �w�N�Ƒg���o��
			getFieldSetData(HEADER_GRADE_CLASS, printFormBean.getGroupName());


			// �w���S�C����
			getFieldSetData(HEADER_TANNIN_NAME, printFormBean.getTeacherNameMain());

			int colIndex = 0;	// ��C���f�b�N�X

			for(AttPrintDayFormBean dayFormBean : printFormBean.getDays()){

				// ���ɂ����o��
				setListFieldData(dayWeekListFieldArray, colIndex, 0, dayFormBean.getDay());

				// �j�����o��
				setListFieldData(dayWeekListFieldArray, colIndex, 1, dayFormBean.getWeekday());
				if(AttPrintSearchFormBeanImpl.WEEKDAY_RED.equals(dayFormBean.getWeekDayColor())){
					// �j�������F���Ԃ̏ꍇ�ɐF��ύX����
					dayWeekListFieldArray[colIndex][1].setTextColor(Color.RED);
				}

				if(dayFormBean.getEnfoce() == 0){
					String text = dayFormBean.getText().toString();
					if(text.length() > 0){
						// 6�s�ڈȍ~�ɋx�Ɠ����̂���ŏc����(�f�[�^�I��45�s�𒴂��邱�Ƃ͖����j
						int rowIndex = 5;
						for(int i=0; i<text.length(); i++){
							setListFieldData(attendListFielddArray, colIndex, rowIndex, text.substring(i, i + 1));
							attendListFielddArray[colIndex][rowIndex].setTextColor(Color.RED);
							rowIndex++;
						}
					}
				}
				// ��C���f�b�N�X�����Z
				colIndex++;
			}
		}

	}



	/**
	 * �o���f�[�^�o��
	 * @throws Exception
	 */
	private void outputAttend() throws Exception{

		// ���k�����E�ԃ��X�g�t�B�[���h
		CrListField studentListField = getCrListField(STUDENT_LIST);
		CrText [][] studentListFieldArray = getListFieldGetColumns(studentListField);

		// �o���W�v���X�g�t�B�[���h
		CrListField attendSumListField = getCrListField(ATTEND_SUM_LIST);
		CrText [][] attendSumListFieldArray = getListFieldGetColumns(attendSumListField);

		enforceState    = new int[31][45]; //���Ɠ�

		// �s�C���f�b�N�X
		int rowIndex = 0;

		// �O���[�v���̊w�N
		String prevGrade = "";

		Map<String, AttPrintStudentFormBean> studentMap = printFormBean.getStudentMap();
		for(Map.Entry<String, AttPrintStudentFormBean> entrySet : studentMap.entrySet()){

			AttPrintStudentFormBean studentFormBean = entrySet.getValue();

			if(!prevGrade.equals(studentFormBean.getCls_glade())){
				if(rowIndex > 0){
					// �ʂ̊w�N�ɐ؂�ւ��ۂ́A1�s�̋�s��݂���
					checkAndSetBlankAttend(rowIndex, printFormBean.getDaysMap().get(prevGrade));	// �o���̔���ɂ͑O�񃋁[�v���̊w�N���g�p
					rowIndex++;
				}
				// �e�w�N�̋��ڂɂ́A�u1�w�N�v�u3�w�N�v�̌��o���s���o�͂���
				setListFieldData(studentListFieldArray, 1, rowIndex, studentFormBean.getCls_glade() + "�w�N");
				checkAndSetTitleAttend(rowIndex, printFormBean.getDaysMap().get(studentFormBean.getCls_glade()));

				rowIndex++;
			}


			// �o�Ȕԍ��E����
			setListFieldData(studentListFieldArray, 0, rowIndex, studentFormBean.getCls_reference_number());


			String stu_name_first = studentFormBean.getStu_name_first();
			String stu_name_end = studentFormBean.getStu_name_end();
			if(stu_name_first.equals(stu_name_end)){
				setListFieldData(studentListFieldArray, 1, rowIndex, stu_name_first);
			}else{
				setListFieldData(studentListFieldArray, 1, rowIndex, stu_name_end);
			}

			if(!"1".equals(printFormBean.getSearchFormBean().getBlankFlg())){


				// ���ɂ��f�[�^�z��
				AttPrintDayFormBean[] baseDays = printFormBean.getDays();
				AttPrintDayFormBean[] days = printFormBean.getDaysMap().get(studentFormBean.getCls_glade());
				// �o���f�[�^�z��
				String[] attend = studentFormBean.getAttend();
				// �o��E�������t���O�f�[�^�z��
				String[] stopKibikiFlg = studentFormBean.getStopKibikiFlg();
				for(int i=0; i<baseDays.length; i++){

					//20180201 �e�w�N���̎��Ɠ���ݒ肷��B
					enforceState[i][rowIndex] = days[i].getEnfoce() ; //20180201

					if(!attendIsBlank(attend[i]) && days[i].getEnfoce() == 0 && StringUtils.isEmpty(days[i].getClo_printword())){
						// �x�Ɠ��ŁA��ȊO�̓��͂�����ꍇ�ɏo�͂��s��(�w�����łȂ��ꍇ�͏����j
						setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
						if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
							attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
						}else{
							attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
						}
					}else if(!StringUtils.isEmpty(attend[i]) && days[i].getEnfoce() != 0){
						// �o���L������ł͂Ȃ��ꍇ���A�x�Ɠ��ł͂Ȃ��ꍇ�ɏo�͂��s��
						if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
							// �o��Ɗ������̏ꍇ�́A�Ԏ��ŕ\������
							setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
							attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
						}else{
							setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
							attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
						}
					}else if(baseDays[i].getEnfoce() != 0 && days[i].getEnfoce() == 0){
						// �x�Ɠ���w�N�Ɏw�肳�ꂽ�w�N�����Ɠ��ł��邪�A�{���̊w�N�͔���Ɠ��ł���ꍇ�A�Y���ӏ��ɏc��������
						outputLineVirtical(i, rowIndex, i, rowIndex);
					}else if(baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
					}else if(!StringUtils.isEmpty(attend[i]) && baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
						// ��w�N���x�Ɠ������A�Ώې��k�̊w�N�����Ɠ��̏ꍇ�́A�S�p�󔒂��Z�b�g����i��w�N���x�Ɠ��̏ꍇ�ɏc���������Ȃ��悤�ɂ��邽�߁j
						setListFieldData(attendListFielddArray, i, rowIndex, "�@");
					}
				}


				// �]�ғ��҂̌ʏo��
				outputEntryData(rowIndex, studentFormBean);

				// �]�ފw�҂̌ʏo��
				outputLeaveData(rowIndex, studentFormBean);

				// �o���W�v
				int colIndex = 0;
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getSchoolkindCount());	// �o��E������
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getSickCount());			// �a��
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getAccidentCount());		// ��
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getAttendCount());		// �o�ȓ���
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getLateCount());			// �x��
				setListFieldData(attendSumListFieldArray, colIndex++, rowIndex, studentFormBean.getAttendSummary().getLeaveCount());		// ����

			}

			prevGrade = studentFormBean.getCls_glade();

			rowIndex++;

			if(rowIndex >= studentListFieldArray[0].length) break;
		}
	}

	/**
	 * �o�������񂪔�\�����������肷��
	 * @param string
	 * @return
	 */
	private boolean attendIsBlank(String string) {
		return StringUtils.isEmpty(string) || StringUtils.equals(string, printFormBean.getAttendDisplayValue());
	}


	/**
	 * �]�ғ��҂̌ʏo��
	 * @param rowIndex �s�C���f�b�N�X
	 * @param studentFormBean ���k���
	 * @throws Exception
	 */
	private void outputEntryData(int rowIndex, AttPrintStudentFormBean studentFormBean) throws Exception {

		// �]���N����
		String entry_start = studentFormBean.getEntry_start();
		if(StringUtils.isEmpty(entry_start)){
			// �]���������݂��Ȃ��ꍇ�͏������I������
			return;
		}

		if(!StringUtils.equals(printFormBean.getSearchFormBean().getMonth(), entry_start.substring(4, 6))){
			// �����قȂ�ꍇ�͏������I������
			return;
		}

		// �]����
		int entry_start_date = Integer.parseInt(entry_start.substring(6, 8));

		// �u���v���L�ڂ���z��̈ʒu�i�z��̂��߁A��̈ʒu�͓��t-1�j
		int entry_start_col = entry_start_date - 1;

		// �u���v���o��
		replaceListFieldData(rowIndex, entry_start_col,  "��");

		// �]������1���̏ꍇ�͐��������K�v���������ߏ������I������
		if(entry_start_date == 1){
			return;
		}

		// ���w�N�����̑O���ȑO�̃Z���ɁA��̉����������ŏo�͂���B
		outputLineHorizontalInBlankCell(rowIndex, 0, entry_start_col, CorLineStyle.SOLID);
	}

	/**
	 * �]�ފw�҂̌ʏo��
	 * @param rowIndex	�s�C���f�b�N�X
	 * @param studentFormBean	���k���
	 * @throws Exception
	 */
	private void outputLeaveData(int rowIndex, AttPrintStudentFormBean studentFormBean) throws Exception {

		// �ٓ��敪
		String reg_mcode = studentFormBean.getReg_mcode();

		if(StringUtils.isEmpty(reg_mcode)){
			// �ٓ��敪����̏ꍇ�͏������I��
			return;
		}

		// �Ώ۔N�E��
		Search32177000FormBean searchFormBean = printFormBean.getSearchFormBean();
		String targetYearMonth = strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "01").substring(0, 6);

		// �w�Z����������
		String regStart = studentFormBean.getReg_start();

		// �w�Z�����������̗�C���f�b�N�X
		Integer regStartColIndex = Integer.parseInt(regStart.substring(6, 8)) - 1;

		// �]�o�悪�󂯓��ꂽ��
		String permitdate = studentFormBean.getReg_permitdate();
		permitdate = StringUtils.isNumeric(permitdate) ? permitdate : "99999999";

		// �]�o�悪�󂯓��ꂽ���̗�C���f�b�N�X
		Integer permitdateColIndex = 0;
		if("30".equals(reg_mcode)){
			// �]�o��(reg_mcode='30')�ł���ꍇ�̂݁Areg_permitdate���L���Ȃ��߁A��C���f�b�N�X���v�Z����
			permitdateColIndex = Integer.parseInt(permitdate.substring(6, 8)) - 1;
		}

		// �Ώی��̍ŏI���̗�C���f�b�N�X
		int lastDayColIndex = printFormBean.getDays().length - 1;

		//-----------------------------------------------------
		// �O���ȑO�ɓ]�ފw�����ꍇ
		//-----------------------------------------------------
		if("30".equals(reg_mcode) && compareMonth(targetYearMonth, permitdate) > 0 ||		// �]�o��(reg_mcode='30')�ł���ꍇ�Areg_permitdate�̓��t���O���̌������ȑO�̎�
		   "40".equals(reg_mcode) && compareMonth(targetYearMonth, regStart) > 0){			// �ފw��(reg_mcode='40')�ł���ꍇ�Areg_start�̓��t���O���̌������ȑO�̎�

			outputLineHorizontalInBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);

			//���k�����E�ԃ��X�g�t�B�[���h��2�d��������
			//CrListField studentListField = getCrListField(STUDENT_LIST);
			//CrText [][] studentListFieldArray = getListFieldGetColumns(studentListField);
			//outputDoubleLineHorizontal(studentListFieldArray, 1, rowIndex, 2, rowIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// �]�o��(reg_mcode='30')�ŁA�ړ����������ׂ�����(�w�Z����������(reg_start)���O���̌������ȑO)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// �]�o��(reg_mcode='30')�ł���ꍇ
				 compareMonth(targetYearMonth, regStart) > 0 &&						// �w�Z����������(reg_start)���O���̌������ȑO�ł���
				 compareMonth(targetYearMonth, permitdate) == 0){					// �]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��̉��ꂩ�̓��ł���


			// ���ɕ������ݒ�ς݂̏ꍇ�́A����Ɠ��E�x�Ɠ��E�w�������̓��e���o�͂���Ă��邽�߂������D��
			if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
				// ���e����̏ꍇ�̂݁u�o�v���o�͂���
				replaceListFieldData(rowIndex, permitdateColIndex,  "��");
			}

			// �ΐ���permitdateColIndex�̈�O�̃Z���܂ŏo��
			outputLineObliqueBlankCell(rowIndex, 0, permitdateColIndex, CorLineStyle.SOLID);

			// �u�o�v�ȍ~�̒���
			outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// �]�o��(reg_mcode='30')�ŁA�ړ����������ׂ�����(�w�Z����������(reg_start)���O���̌������ȑO�A�]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��ȍ~)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// �]�o��(reg_mcode='30')�ł���ꍇ
				compareMonth(targetYearMonth, regStart) > 0 &&						// �w�Z����������(reg_start)���O���̌������ȑO�ł���
				compareMonth(targetYearMonth, permitdate) < 0){						// �]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��ȍ~�ł���

			outputLineObliqueBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// �]�o��(reg_mcode='30') �w�Z�������������Ώی�(�]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��ȍ~)
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// �]�o��(reg_mcode='30')�ł���ꍇ
				compareMonth(targetYearMonth, regStart) == 0 &&						// �w�Z����������(reg_start)���Ώی��ł���
				compareMonth(targetYearMonth, permitdate) < 0){						// �]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��ȍ~�ł���

			outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}
		//-----------------------------------------------------
		// �]�o��(reg_mcode='30') �w�Z�������������Ώی�
		//-----------------------------------------------------
		else if("30".equals(reg_mcode) &&											// �]�o��(reg_mcode='30')�ł���ꍇ
				compareMonth(targetYearMonth, regStart) == 0 &&						// �w�Z����������(reg_start)���Ώی��ł���
				compareMonth(targetYearMonth, permitdate) == 0){					// �]�o�悪�󂯓��ꂽ���̑O�����A�Ώی��ł���

			// reg_start��reg_permitdate�̓��t�̍���2���ȏ�ł���ꍇ�A���̊Ԃ̓��Ɏ�̉������ΐ��ň���
			if(Integer.parseInt(permitdate.substring(6,8)) - Integer.parseInt(regStart.substring(6,8)) >= 2){

				// �u�o�v���o�͂���
				replaceListFieldData(rowIndex, permitdateColIndex, "��");

				// reg_start��reg_permitdate�̓��t�̍���2���ȏ�ł���ꍇ�A���̊Ԃ̓��Ɏ�̉������ΐ��ň����B�i��ɑO�q�̏����Łu�o�v���o�͂��Ă������Ɓj
				outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, permitdateColIndex, CorLineStyle.SOLID);

				// �]�o�悪�󂯓��ꂽ���̑O��(reg_permitdate�̓��t)�̗����ȍ~�Ɏ�̉����������ň���
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else if(Integer.parseInt(permitdate.substring(6,8)) - Integer.parseInt(regStart.substring(6,8)) == 1){
				// ���e����̏ꍇ�̂݁u�o�v���o�͂���
				replaceListFieldData(rowIndex, permitdateColIndex, "��");

				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else {
				replaceListFieldData(rowIndex, permitdateColIndex, "��");
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
			}
		}
		//-----------------------------------------------------
		// �ފw��(reg_mcode='40')
		//-----------------------------------------------------
		else if("40".equals(reg_mcode) &&											// �ފw��(reg_mcode='40')�ł���ꍇ
				compareMonth(targetYearMonth, regStart) == 0 ){						// �w�Z����������(reg_start)���Ώی��ł���

			// �u�o�v���o�͂���
			replaceListFieldData(rowIndex, regStartColIndex, "��");

			outputLineHorizontalInBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}

	}

	/**
	 * �������v���̏o��
	 * @throws Exception
	 */
	private void outputStatistics() throws Exception{

		CrListField enforceDaySumList = getCrListField(ENFORCE_DAY_SUM_LIST);
		CrText[][] enforceDaySumListFieldArray = getListFieldGetColumns(enforceDaySumList);
		int rowMax;
		//���w�E���w�Ń��[�v����ݒ�
		if(sessionBean.getUseKind2().equals("1")) {
			rowMax = 6;

		}else {
			rowMax = 3;
		}

		//������
		for(int i = 0; i < rowMax; i++) {
			String grade = (i + 1) + "�N";
			setListFieldData(enforceDaySumListFieldArray, 0, i, grade); //�w�N
			setListFieldData(enforceDaySumListFieldArray, 1, i, "0"); //���Ɠ���
			setListFieldData(enforceDaySumListFieldArray, 2, i, "0"); //���Ƃ��s��Ȃ�����
			setListFieldData(enforceDaySumListFieldArray, 3, i, "0"); //�v
		}

		//���X�g�̐���
		List<Print32177000EnforceEntity> enforceList = printFormBean.getEnforceList();
		for(Print32177000EnforceEntity enforce : enforceList) {
			int rowIdx = Integer.parseInt(enforce.getCls_glade()) - 1;

			setListFieldData(enforceDaySumListFieldArray, 1, rowIdx, enforce.getEnforceNum()); //���Ɠ���
			setListFieldData(enforceDaySumListFieldArray, 2, rowIdx, enforce.getHolidayNum()); //���Ƃ��s��Ȃ�����
			int enforceNum = enforce.getEnforceNum() == null ? 0 : enforce.getEnforceNum();
			int holidayNum = enforce.getHolidayNum() == null ? 0 : enforce.getHolidayNum();
			setListFieldData(enforceDaySumListFieldArray, 3, rowIdx, enforceNum + holidayNum); //�v
		}

		Map<String, Print32177000StatisticsFormBean> statisticsMap = printFormBean.getStatisticsMap();

		CrListField studentSumListListField = getCrListField(TOTAL_ATTEND);
		CrText[][] studentSumListListFieldArray = getListFieldGetColumns(studentSumListListField);

		int zaisekiStuSumPre = 0;		// �ݐЎҐ�
		int entryStuSum = 0;			// �ٓ��E���w
		int leaveStuSum = 0;			// �ٓ��E�ފw


		// �j:1�A��:2
		String sex_male  = "1";
		String sex_female = "2";
		// �Ώی���4���̏ꍇ�͑O�������ݍݐЂƍ����������������ݍݐЂ�\�����Ȃ�
		String targetYearMonth = printFormBean.getTargetYearMonth();
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");
		if(!targetMonth.equals("4")) {
			// �ݐЎҐ�(�j)
			setListFieldData(studentSumListListFieldArray, 0, 0, statisticsMap.get(sex_male).getZaisekiStuNumPre());
			// �ݐЎҐ�(��)
			setListFieldData(studentSumListListFieldArray, 0, 1, statisticsMap.get(sex_female).getZaisekiStuNumPre());
			// �ݐЎҐ�(���v)
			zaisekiStuSumPre =  statisticsMap.get(sex_male).getZaisekiStuNumPre() + statisticsMap.get(sex_female).getZaisekiStuNumPre();
			setListFieldData(studentSumListListFieldArray, 0, 2, zaisekiStuSumPre);
		}

		// �������������ݍݐ�(�j)
		setListFieldData(studentSumListListFieldArray, 3, 0, statisticsMap.get(sex_male).getZaisekiStuNum());
		//  �������������ݍݐ�(��)
		setListFieldData(studentSumListListFieldArray, 3, 1,statisticsMap.get(sex_female).getZaisekiStuNum());
		//  �������������ݍݐ�(���v)
		int ZaisekiSumNow = statisticsMap.get(sex_male).getZaisekiStuNum() + statisticsMap.get(sex_female).getZaisekiStuNum();
		setListFieldData(studentSumListListFieldArray, 3, 2, ZaisekiSumNow);


		// �ٓ��E���w�Ґ�(�j)
		setListFieldData(studentSumListListFieldArray, 1, 0, statisticsMap.get(sex_male).getEntryStuNum());
		// �ٓ��E���w�Ґ�(��)
		setListFieldData(studentSumListListFieldArray, 1, 1, statisticsMap.get(sex_female).getEntryStuNum());
		// �ٓ��E���w�Ґ�(���v)
		entryStuSum =  statisticsMap.get(sex_male).getEntryStuNum() + statisticsMap.get(sex_female).getEntryStuNum();
		setListFieldData(studentSumListListFieldArray, 1, 2, entryStuSum);

		// �ٓ��E�ފw�Ґ�(�j)
		setListFieldData(studentSumListListFieldArray, 2, 0, statisticsMap.get(sex_male).getLeaveStuNum());
		// �ٓ��E�ފw�Ґ�(��)
		setListFieldData(studentSumListListFieldArray, 2, 1, statisticsMap.get(sex_female).getLeaveStuNum());
		// �ٓ��E�ފw�Ґ�(���v)
		leaveStuSum =  statisticsMap.get(sex_male).getLeaveStuNum() + statisticsMap.get(sex_female).getLeaveStuNum();
		setListFieldData(studentSumListListFieldArray, 2, 2, leaveStuSum);

	}

	/**
	 * ���݂̏o�����e�̃f�[�^�ɂ��A���e�̏��������A�ǉ����s��
	 * @param rowIndex �s�C���f�b�N�X
	 * @param dayIndex ���̃C���f�b�N�X
	 * @param replaceText �u��������
	 * @throws Exception
	 */
	private void replaceListFieldData(int rowIndex, Integer dayIndex, String replaceText) throws Exception {

		// �Z�b�g����Ă��镶����
		String outputText = attendListFielddArray[dayIndex][rowIndex].getText();

		if(isBlankOrAttendText(outputText)){
			// �e�L�X�g�̓��e���󂩁A�o�Ȃ̏ꍇ�͒u���p������Œu��������
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText);
		}else if(printFormBean.getDays()[dayIndex].getEnfoce() == 1){
			// �o�ȃf�[�^������ꍇ�͏o�͓��e���܂Ƃ߂�
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText + outputText);
		}
	}


	/**
	 * �e�L�X�g�̓��e���󂩁A�o�ȃf�[�^���𔻒肷��
	 * @param text �`�F�b�N�Ώە�����
	 * @return �󂩁A�o�Ȃ̏ꍇ��true
	 */
	private boolean isBlankOrAttendText(String text) {
		return StringUtils.isEmpty(text) || StringUtils.equals(text, ATTEND_KIND_00_DISP);
	}

	/**
	 * �󔒍s�ɑ΂��A���Ɠ��̏ꍇ�͑S�p�󔒂��Z�b�g����
	 * @param rowIndex �s�C���f�b�N�X
	 * @param days ���Ɠ����̔z��
	 * @throws Exception
	 */
	private void checkAndSetBlankAttend(int rowIndex, AttPrintDayFormBean[] days) throws Exception {

		for (int i = 0; i < days.length; i++) {
			AttPrintDayFormBean dayFormBean = days[i];
			if(dayFormBean.getEnfoce() != 0){
				// ���Ɠ��̏ꍇ�́A�S�p�󔒂��Z�b�g����i��w�N���x�Ɠ��̏ꍇ�ɏc���������Ȃ��悤�ɂ��邽�߁j
				setListFieldData(attendListFielddArray, i, rowIndex, "�@");
			}
		}
	}

	/**
	 * �w�N�s�ɑ΂��A���Ɠ��̏ꍇ�͑S�p�󔒂��Z�b�g����
	 * @param rowIndex �s�C���f�b�N�X
	 * @param days ���Ɠ����̔z��
	 * @throws Exception
	 */
	private void checkAndSetTitleAttend(int rowIndex, AttPrintDayFormBean[] days) throws Exception {

		AttPrintDayFormBean[] baseDays = printFormBean.getDays();

		for(int i=0; i<baseDays.length - 1; i++){
			if(baseDays[i].getEnfoce() != 0 && days[i].getEnfoce() == 0){
				// �x�Ɠ���w�N�Ɏw�肳�ꂽ�w�N�����Ɠ��ł��邪�A�{���̊w�N�͔���Ɠ��ł���ꍇ�A�Y���ӏ��ɏc��������
				outputLineVirtical(i, rowIndex, i, rowIndex);
			}else if(baseDays[i].getEnfoce() == 0 && days[i].getEnfoce() != 0){
				// ��w�N���x�Ɠ������A�Ώې��k�̊w�N�����Ɠ��̏ꍇ�́A�S�p�󔒂��Z�b�g����i��w�N���x�Ɠ��̏ꍇ�ɏc���������Ȃ��悤�ɂ��邽�߁j
				setListFieldData(attendListFielddArray, i, rowIndex, "�@");
			}
		}
	}






	/**
     * �n�_���W����I�_���W�܂Ő����o�͂��܂��B
     * @param p1	�n�_���W�̔z��
     * @param p2	�I�_���W�̔z��
     * @param lineWidth	���̑���
	 * @param layer	�o�̓��C��
	 * @param lineColor ���̐F
	 * @param lineStyle ���̃X�^�C��
	 */
    private void printSlash(int[] p1, int[] p2, int lineWidth, CrLayer layer, Color lineColor, CorLineStyle lineStyle){

    	if(p1 == null || p2 == null){
    		return;
    	}

		CrLine crLine = new CrLine(p1[0], p1[1], p2[0], p2[1]);

		CorUnit tempUnit = pdfDocumentBeanCR.getDraw().getUnit();
		// ���W�̒P�ʂ�1mm�P�ʂɐݒ�
		pdfDocumentBeanCR.getDraw().setUnit(CorUnit.MM);
		crLine.setLineWidth(lineWidth);
		crLine.setLineStyle(lineStyle);
		pdfDocumentBeanCR.getDraw().setUnit(tempUnit);

		// ���̐F��ݒ�
		crLine.setLineColor(lineColor);

		if(layer != null){
			layer.draw(crLine);
		}else{
			form.draw(crLine);
		}
	}

    /**
     * �󗓂̃Z����Ώۂɂ��ďo�̓��X�g�t�B�[���h��ɐ��������i���j
     *
     * @param rowIndex		�s�C���f�b�N�X
     * @param endColIndex	���̏I�[�ƂȂ��C���f�b�N�X
     * @param lineStyle		�o�͂�����̃X�^�C��
     * @throws Exception
     */
	private void outputLineHorizontalInBlankCell(int rowIndex, int startColIndex, int endColIndex, CorLineStyle lineStyle) throws Exception {

		// �J�n�񂪒�����ꍇ�͏������I������
		if(startColIndex > endColIndex){
			return;
		}

		// �J�n�񂪏I����Ɠ����ꍇ
		if(startColIndex == endColIndex){
			if(isBlankOrAttendText(attendListFielddArray[startColIndex][rowIndex].getText())){
				// �o���E�u�����N�̏ꍇ�ɐ�������
				outputLineHorizontal(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
		}else{

			// ���`��J�n�Z��(CrText)�̈ʒu
			int startCol = startColIndex;

			// colIndex�̈ʒu�ɂ͉��炩�̓��e���o�͂���Ă��邽��col<=colIndex�Ƃ��ă��[�v���Ŕ�����s��
			for(int col=startCol; col<=endColIndex; col++){

				// �o���E�u�����N�ł͂Ȃ��ꍇ
				if(!isBlankOrAttendText(attendListFielddArray[col][rowIndex].getText())){

					if(startCol < col){
						// ���������I���ʒu�͕������o�͂���Ă���Z���̈�O���Ώۂ̂���-1
						outputLineHorizontal(startCol, rowIndex, col - 1, rowIndex, lineStyle);
					}
					// �`��J�n�Z���̈ʒu���X�V
					startCol = col + 1;
				}
			}

			if(startCol < endColIndex){
				// �ŏI��܂ł����ׂċ�̏ꍇ�͖����܂Ő�������
				outputLineHorizontal(startCol, rowIndex, endColIndex, rowIndex, lineStyle);
			}
		}
	}

    /**
     * �o�����X�g�t�B�[���h��ɐ�������(���j
     * @param startCol	�J�n��
     * @param startRow	�J�n�s
     * @param endCol	�I����
     * @param endRow	�I���s
     * @throws Exception
     */
    private void outputDoubleLineHorizontal(CrText [][] listFieldArray, int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// ���X�g�̐�����(�]��v�Z�p)
		int lineWidth = listFieldArray[0][0].getLineWidth();


		// �J�n���WX
		int startX = listFieldArray[startCol][startRow].getStartX();
		// �]�蕝�����Z
		startX += lineWidth * 4;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~4�E�ɂ��炷

		// �J�n���WY�i�Z����Y���W�{�Z���̍�����2�j
		int cellHeight = listFieldArray[startCol][startRow].getHeight();	// �o�͂���Z���̍���
		int startY = listFieldArray[startCol][startRow].getStartY() + (cellHeight / 2);

		// �I�[���WX
		int cellWidth = listFieldArray[endCol][startRow].getWidth();		// �o�͂���Z���̕�
		int endX = listFieldArray[endCol][endRow].getStartX() + cellWidth;
		// �]�蕝�����Z
		endX -= lineWidth * 4;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~2���ɂ��炷

		// �I�[���WY�i�Z����Y���W�{�Z���̍�����2�j
		int endY = listFieldArray[endCol][endRow].getStartY() + (cellHeight / 2);

		// ������`��
		printSlash(new int[]{startX,  startY - (cellHeight / 10)}, new int[]{endX, endY - (cellHeight / 10)}, 5, null, Color.BLACK, lineStyle);
		printSlash(new int[]{startX,  startY + (cellHeight / 10)}, new int[]{endX, endY + (cellHeight / 10)}, 5, null, Color.BLACK, lineStyle);

    }


	/**
	 * ����Ɠ��E�x�Ɠ��E�w�������̎������o��
	 * @throws Exception
	 */
	private void outputNotEnforcedayLine() throws Exception{

		AttPrintDayFormBean[] days = printFormBean.getDays();

		// ���X�g�̍ŏI�s�C���f�b�N�X
		int maxRowIndex = attendListFielddArray[0].length - 1;

		// 1�����炻�̌��̍ŏI����܂�
		for(int col=0; col<days.length; col++){

			// ���Ǝ��{���ł͂Ȃ��ꍇ
			if(days[col].getEnfoce() == 0){

				// ���`��J�n�Z��(CrText)�̍s�ʒu
				int startRow = 0;

				// 1�s�ڂ���ŏI�s�܂�
				for(int row=0; row<=maxRowIndex; row++){

					// �Z��(CrText)�ɕ������Z�b�g����Ă���ꍇ
					if((!StringUtils.isEmpty(attendListFielddArray[col][row].getText()) && !attendListFielddArray[col][row].getText().contains("��"))
					|| enforceState[col][row]==1  //20180201
					){
						if(startRow < row){
							// ���������I���ʒu�́A�������o�͂���Ă���Z���̈�O���Ώۂ̂���row��-1�����l
							outputLineVirtical(col, startRow, col, row - 1);
						}
						// �`��J�n�Z���̈ʒu���X�V
						startRow = row + 1;
					}
				}

				// �����܂Ő�������
				if(startRow < maxRowIndex){
					outputLineVirtical(col, startRow, col, maxRowIndex);
				}
			}
		}
	}

    /**
     * �o�����X�g�t�B�[���h��ɐ��������i�c�j
     * @param startCol	�J�n��
     * @param startRow	�J�n�s
     * @param endCol	�I����
     * @param endRow	�I���s
     * @throws Exception
     */
    private void outputLineVirtical(int startCol, int startRow, int endCol, int endRow) throws Exception{

		// ���X�g�̐�����
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();
		int cellHeight = attendListFielddArray[0][0].getHeight();


		// �J�n���WX
		int startX = attendListFielddArray[startCol][startRow].getStartX() + (cellWidth / 2);

		// �J�n���WY
		int startY = attendListFielddArray[startCol][startRow].getStartY();
		// �]�蕝�����Z
		if(startRow == 0){
			startY += lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��Đ��̑����~2�����l��������
		}else{
			startY += lineWidth;
		}

		// �I�[���WX
		int endX = attendListFielddArray[endCol][endRow].getStartX() + (cellWidth / 2);

		// �I�[���WY
		int endY = attendListFielddArray[endCol][endRow].getStartY() + cellHeight;
		// �]�蕝�����Z
		if(endRow == attendListFielddArray[endCol].length - 1){
			endY -= lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��Đ��̑����~2�����l���グ��
		}else{
			endY -= lineWidth;
		}

		// ������`��
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.RED, CorLineStyle.SOLID);

    }

    /**
     * �o�����X�g�t�B�[���h��ɐ��������i���j
     * @param startCol	�J�n��
     * @param startRow	�J�n�s
     * @param endCol	�I����
     * @param endRow	�I���s
     * @throws Exception
     */
    private void outputLineHorizontal(int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// ���X�g�̐�����
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();		// �o�͂���Z���̕�
		int cellHeight = attendListFielddArray[0][0].getHeight();	// �o�͂���Z���̍���

		// �J�n���WX
		int startX = attendListFielddArray[startCol][startRow].getStartX();
		// �]�蕝�����Z
		if(startCol == 0){
			startX += lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~2�E�ɂ��炷
		}else{
			startX += lineWidth;
		}

		// �J�n���WY�i�Z����Y���W�{�Z���̍�����2�j
		int startY = attendListFielddArray[startCol][startRow].getStartY() + (cellHeight / 2);

		// �I�[���WX
		int endX = attendListFielddArray[endCol][endRow].getStartX() + cellWidth;
		// �]�蕝�����Z
		if(endCol == attendListFielddArray.length - 1){
			endX -= lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~2���ɂ��炷
		}else{
			endX -= lineWidth;
		}

		// �I�[���WY�i�Z����Y���W�{�Z���̍�����2�j
		int endY = attendListFielddArray[endCol][endRow].getStartY() + (cellHeight / 2);

		// ������`��
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.BLACK, lineStyle);


		for(int col=startCol; col<=endCol; col++){
			// �u�E�v�̉\�������邽�߁A�󔒂����߂ăZ�b�g
			if(isBlankOrAttendText(attendListFielddArray[col][startRow].getText())){
				setListFieldData(attendListFielddArray, col, startRow, "");
			}
		}

    }

    /**
     * �����r����
     * @param date1	����1(YYYYMM or YYYYMMDD)
     * @param date2	����2(YYYYMM or YYYYMMDD)
     * @return ����1������2�����O�̌��̏ꍇ��-1�A����1������2������̌��̏ꍇ�� 1�A�����ꍇ��0
     */
    private int compareMonth(String date1, String date2){

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Integer.parseInt(date1.substring(0,4)), Integer.parseInt(date1.substring(4,6))-1, 1);
		cal1.set(Calendar.MILLISECOND, 0);


		Calendar cal2 = Calendar.getInstance();
		cal2.set(Integer.parseInt(date2.substring(0,4)), Integer.parseInt(date2.substring(4,6))-1, 1);
		cal1.set(Calendar.MILLISECOND, 0);


		if(cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)){
			// �N1������2�����O�̔N�̏ꍇ��-1��Ԃ�
			return -1;
		}else if(cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)){
			// �N1������2������̔N�̏ꍇ�� 1��Ԃ�
			return 1;
		}

		if(cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH)){
			// ��1����2�����O�̌��̏ꍇ��-1��Ԃ�
			return -1;
		}else if(cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH)){
			// ��1����2������̌��̏ꍇ�� 1��Ԃ�
			return 1;
		}

    	return 0;
    }


	/**
	 * �N�x�E������N������������擾����
	 * @param sessionBean �V�X�e�����
	 * @param nendo �N�x
	 * @param month ��
	 * @param day ��
	 * @return �N����YYYYMMDD
	 */
	protected String strDayOfMonth(SystemInfoBean sessionBean, String nendo, String month, String day){
		Calendar calendar = getCalendar(sessionBean, nendo, month, day);

		return String.format("%04d%02d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
	}

	private Calendar getCalendar(SystemInfoBean sessionBean, String nendo, String month, String day){

		Calendar nendoStartDate = Calendar.getInstance();
		nendoStartDate.set(Integer.parseInt(nendo),
				           Integer.parseInt(sessionBean.getSystemNendoStartDate().trim().substring(0, 2)) - 1,
				           Integer.parseInt(sessionBean.getSystemNendoStartDate().trim().substring(2, 4)),
				           0,
				           0,
				           0);
		nendoStartDate.set(Calendar.MILLISECOND, 0);

		Calendar targetDate = Calendar.getInstance();
		targetDate.set(Integer.parseInt(nendo),
				           Integer.parseInt(month) - 1,
				           Integer.parseInt(day),
				           0,
				           0,
				           0);
		targetDate.set(Calendar.MILLISECOND, 0);

		if(nendoStartDate.compareTo(targetDate) > 0 ){
			targetDate.add(Calendar.YEAR, 1);
		}

		return targetDate;
	}

	 /**
     * �󗓂̃Z����Ώۂɂ��ďo�̓��X�g�t�B�[���h��ɐ��������i�ΐ��j
     *
     * @param rowIndex		�s�C���f�b�N�X
     * @param endColIndex	���̏I�[�ƂȂ��C���f�b�N�X
     * @param lineStyle		�o�͂�����̃X�^�C��
     * @throws Exception
     */
	private void outputLineObliqueBlankCell(int rowIndex, int startColIndex, int endColIndex, CorLineStyle lineStyle) throws Exception {

		// �J�n�񂪒�����ꍇ�͏������I������
		if(startColIndex > endColIndex){
			return;
		}

		// �J�n�񂪏I����Ɠ����ꍇ
		if(startColIndex == endColIndex){
			if(isBlankOrAttendText(attendListFielddArray[startColIndex][rowIndex].getText())){
				// �o���E�u�����N�̏ꍇ�ɐ�������
				outputLineOblique(startColIndex, rowIndex, startColIndex, rowIndex, lineStyle);
			}
		}else{

			// ���`��J�n�Z��(CrText)�̈ʒu
			int startCol = startColIndex;

			// �e�Z���Ɏΐ�������
			for(int col=startCol; col<=endColIndex; col++){

				if(StringUtils.isEmpty(attendListFielddArray[col][rowIndex].getText())){
					outputLineOblique(col, rowIndex, col, rowIndex, lineStyle);
				}

			}
		}

	}


    /**
     * �o�����X�g�t�B�[���h��ɐ�������(�ΐ��j
     * @param startCol	�J�n��
     * @param startRow	�J�n�s
     * @param endCol	�I����
     * @param endRow	�I���s
     * @throws Exception
     */
    private void outputLineOblique(int startCol, int startRow, int endCol, int endRow, CorLineStyle lineStyle) throws Exception{

		// ���X�g�̐�����
		int lineWidth = attendListFielddArray[0][0].getLineWidth();

		int cellWidth = attendListFielddArray[0][0].getWidth();		// �o�͂���Z���̕�
		int cellHeight = attendListFielddArray[0][0].getHeight();	// �o�͂���Z���̍���

		// �J�n���WX
		int startX = attendListFielddArray[startCol][startRow].getStartX();
		// �]�蕝�����Z
		if(startCol == 0){
			startX += lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~2�E�ɂ��炷
		}else{
			startX += lineWidth;
		}

		// �J�n���WY�i�Z����Y���W�{�Z���̍�����2�j
		int startY = attendListFielddArray[startCol][startRow].getStartY() + cellHeight;

		// �I�[���WX
		int endX = attendListFielddArray[endCol][endRow].getStartX() + cellWidth;
		// �]�蕝�����Z
		if(endCol == attendListFielddArray.length - 1){
			endX -= lineWidth * 2;		// ���X�g���[�̐��͑������߁A�]�蕪�Ƃ��đ����~2���ɂ��炷
		}else{
			endX -= lineWidth;
		}

		// �I�[���WY�i�Z����Y���W�{�Z���̍�����2�j
		int endY = attendListFielddArray[endCol][endRow].getStartY();

		// ������`��
		printSlash(new int[]{startX,  startY}, new int[]{endX, endY}, 10, null, Color.RED, lineStyle);

    }
}
