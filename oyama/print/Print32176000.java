/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.print;

import java.awt.Color;
import java.util.Calendar;
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
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000DayFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StudentFormBean;


/**
 * �o�ȕ���(�ʏ�w���@���R�s�p) �o�̓N���X.
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32176000 extends AbstractPdfManagerAdditionPagesCR {


	/**log4j*/
	private final Log log = LogFactory.getLog(Print32176000.class);

	/** ���C�A�E�g�I�u�W�F�N�g */
	private static final String HEADER_YEAR_MONTH = "HeaderYearMonth";			// �w�b�_ ��
	private static final String HEADER_TANNIN_NAME = "HeaderTanninName";		// �w�b�_�@�w���S�C����
	private static final String HEADER_GRADE_CLASS = "HeaderGradeClass";		// �w�b�_�@�w�N�g

	private static final String ENFORCE_DAY_SUM = "EnforceDaySum";				// �������v�E���Ǝ���
	private static final String HOLIDAY_SUM = "HolidaySum";						// �������v�E����Ǝ���
	private static final String DAY_SUM = "DaySum";								// �������v�E���Ǝ��� + ����Ǝ���


	private static final String DAY_WEEK_LIST = "DayWeekList";					// ���E�j�����X�g�t�B�[���h
	private static final String STUDENT_LIST = "StudentList";					// ���k�����E�ԃ��X�g�t�B�[���h
	private static final String TOTAL_ATTEND = "TotalAttend";					// �������v���X�g�t�B�[���h
	private static final String ATTEND_LIST = "AttendList";						// �o�������X�g�t�B�[���h
	private static final String ATTEND_SUM_LIST = "AttendSumList";				// �o���W�v���X�g�t�B�[���h


	/** ��ʂőI�������j�������F(��)�̒�` */
	private static final String WEEKDAY_COLOR_RED = "1";

	/** �o�������X�g�t�B�[���h */
	private CrText [][] attendListFielddArray;

	/** ���FormBean */
	private Print32176000FormBean printFormBean;

	protected DateFormatUtility dateFormatUtility = null;


	@Override
	protected void doPrint() throws TnaviPrintException {

		try {

			CrListField attendListField = getCrListField(ATTEND_LIST);
			this.attendListFielddArray = getListFieldGetColumns(attendListField);
			this.dateFormatUtility = new DateFormatUtility(printFormBean.getUserCode());

			// �w�b�_���o��
			outputDetailHeader();

			// �o���f�[�^�o��
			outputAttend();

			// �������v���̏o��
			outputStatistics();

			// ����Ɠ��E�x�Ɠ��E�w�������̎������o��
			outputNotEnforcedayLine();

			form.printOut();
			form.initialize();//�t�H�[��������

		} catch (Exception e) {
			log.error("��O�����@���[�쐬���s",e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * ���׃w�b�_�����̏o��
	 * @throws Exception
	 */
	private void outputDetailHeader() throws Exception{

		// ���E�j�����X�g�t�B�[���h
		CrListField dayWeekListField = getCrListField(DAY_WEEK_LIST);
		CrText [][] dayWeekListFieldArray = getListFieldGetColumns(dayWeekListField);

		// �Ώ۔N�E��
		String targetYearMonth = printFormBean.getTargetYearMonth();

		// �Ώ۔N
		String targetYear = targetYearMonth.substring(0, 4);

		// �Ώی�
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");

		String year_month = dateFormatUtility.formatDate("YYYY�N",1,targetYearMonth) + targetMonth + "��";


		// �N�����o��
		getFieldSetData(HEADER_YEAR_MONTH, year_month);

		// �w�N�Ƒg���o��
		getFieldSetData(HEADER_GRADE_CLASS, "��" + printFormBean.getGrade() + "�w�N" +  printFormBean.getHmrclass() + "�g");

		// �w���S�C����
		getFieldSetData(HEADER_TANNIN_NAME, printFormBean.getTeacherName());

		int colIndex = 0;	// ��C���f�b�N�X
		Calendar cal = Calendar.getInstance();
		for(Print32176000DayFormBean dayFormBean : printFormBean.getDays()){

			// ���ɂ����o��
			setListFieldData(dayWeekListFieldArray, colIndex, 0, dayFormBean.getDay());

			// Calendar�N���X�ɓ��t���Z�b�g
			cal.set(Integer.parseInt(targetYear), Integer.parseInt(targetMonth)-1, dayFormBean.getDay());

			// �j�������
			String week = "";
			switch(cal.get(Calendar.DAY_OF_WEEK)){
			case 1: week = "��"; break;
			case 2: week = "��"; break;
			case 3: week = "��"; break;
			case 4: week = "��"; break;
			case 5: week = "��"; break;
			case 6: week = "��"; break;
			case 7: week = "�y"; break;
			}

			// �j�����o��
			setListFieldData(dayWeekListFieldArray, colIndex, 1, week);
			if(WEEKDAY_COLOR_RED.equals(dayFormBean.getWeekDayColor())){
				// �j�������F���Ԃ̏ꍇ�ɐF��ύX����
				dayWeekListFieldArray[colIndex][1].setTextColor(Color.RED);
			}

			if(dayFormBean.getEnfoce() == 0){
				String text = dayFormBean.getText().toString();
				if(text.length() > 0){
					int rowIndex = 5;
					// 6�s�ڈȍ~�ɋx�Ɠ����̂���ŏc����(�f�[�^�I��45�s�𒴂��邱�Ƃ͖����j
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


		// �s�C���f�b�N�X
		int rowIndex = 0;

		Map<String, Print32176000StudentFormBean> studentMap = printFormBean.getStudentMap();
		for(Map.Entry<String, Print32176000StudentFormBean> entrySet : studentMap.entrySet()){

			Print32176000StudentFormBean studentFormBean = entrySet.getValue();

			// �o�Ȕԍ��E����
			setListFieldData(studentListFieldArray, 0, rowIndex, studentFormBean.getCls_reference_number());
			setListFieldData(studentListFieldArray, 1, rowIndex, studentFormBean.getStu_name());
			//setListFieldData(studentListFieldArray, 2, rowIndex, studentFormBean.getCls_reference_number());

			// ���ɂ��f�[�^�z��
			Print32176000DayFormBean[] days = printFormBean.getDays();
			// �o���f�[�^�z��
			String[] attend = studentFormBean.getAttend();
			// �o��E�������t���O�f�[�^�z��
			String[] stopKibikiFlg = studentFormBean.getStopKibikiFlg();
			for(int i=0; i<printFormBean.getMonthDayCnt(); i++){
				if(!StringUtils.isEmpty(attend[i]) && !days[i].isClosingclass()){
					// �o���L������ł͂Ȃ��ꍇ���A�w�����ł͂Ȃ��ꍇ�ɏo�͂��s��
					setListFieldData(attendListFielddArray, i, rowIndex, attend[i]);
					if(!StringUtils.isEmpty(stopKibikiFlg[i]) && stopKibikiFlg[i].equals("1")){
						// �o��Ɗ������̏ꍇ�́A�Ԏ��ŕ\������
						attendListFielddArray[i][rowIndex].setTextColor(Color.RED);
					}else{
						attendListFielddArray[i][rowIndex].setTextColor(Color.BLACK);
					}
				}
			}

			// �]�ғ��҂̌ʏo��
			outputEntryData(rowIndex, studentFormBean);

			// �]�ފw�҂̌ʏo��
			outputLeaveData(rowIndex, studentFormBean);

			// �o���W�v
			setListFieldData(attendSumListFieldArray, 0, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getSchoolkindCount()));		// �o��E����
			setListFieldData(attendSumListFieldArray, 1, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getSickCount()));		// �a��
			setListFieldData(attendSumListFieldArray, 2, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getAccidentCount()));	// ����
			setListFieldData(attendSumListFieldArray, 3, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getAttendCount()));		// �o�ȓ���
			setListFieldData(attendSumListFieldArray, 4, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getLateCount()));		// �x��
			setListFieldData(attendSumListFieldArray, 5, rowIndex, replaceAttendZero(studentFormBean.getAttendSummary().getLeaveCount()));		// ����

			// ���l
			//setListFieldData(attendBikoListFieldArray, 0, rowIndex, studentFormBean.getAttendSummary().getAtc_comment());

			rowIndex++;
		}
	}


	/**
	 * �]�ғ��҂̌ʏo��
	 * @param rowIndex �s�C���f�b�N�X
	 * @param studentFormBean ���k���
	 * @throws Exception
	 */
	private void outputEntryData(int rowIndex, Print32176000StudentFormBean studentFormBean) throws Exception {

		// �]���N����
		String entry_start = studentFormBean.getEntry_start();
		if(StringUtils.isEmpty(entry_start)){
			// �]���������݂��Ȃ��ꍇ�͏������I������
			return;
		}

		// �]����
		int entry_start_date = Integer.parseInt(entry_start.substring(6, 8));

		// �u���v���L�ڂ���z��̈ʒu�i�z��̂��߁A��̈ʒu�͓��t-1�j
		int entry_start_col = entry_start_date - 1;

		// ���ɕ������ݒ�ς݂̏ꍇ�́A����Ɠ��E�x�Ɠ��E�w�������̓��e���o�͂���Ă��邽�߂������D��
		if(StringUtils.isEmpty(attendListFielddArray[entry_start_col][rowIndex].getText()) || printFormBean.getDays()[entry_start_col].getEnfoce() == 1){
			// ���e����̏ꍇ�̂݁u���v���o�͂���
			replaceListFieldData(rowIndex,entry_start_col, "��");
		}

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
	private void outputLeaveData(int rowIndex, Print32176000StudentFormBean studentFormBean) throws Exception {

		// �ٓ��敪
		String reg_mcode = studentFormBean.getReg_mcode();

		if(StringUtils.isEmpty(reg_mcode)){
			// �ٓ��敪����̏ꍇ�͏������I��
			return;
		}

		// �Ώ۔N�E��
		String targetYearMonth = printFormBean.getTargetYearMonth();

		// �w�Z����������
		String regStart = studentFormBean.getReg_start();

		// �]�o�悪�󂯓��ꂽ���̗�C���f�b�N�X
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
		int lastDayColIndex = printFormBean.getMonthDayCnt() - 1;

		//-----------------------------------------------------
		// �O���ȑO�ɓ]�ފw�����ꍇ
		//-----------------------------------------------------
		if("30".equals(reg_mcode) && compareMonth(targetYearMonth, permitdate) > 0 ||		// �]�o��(reg_mcode='30')�ł���ꍇ�Areg_permitdate�̓��t���O���̌������ȑO�̎�
		   "40".equals(reg_mcode) && compareMonth(targetYearMonth, regStart) > 0){			// �ފw��(reg_mcode='40')�ł���ꍇ�Areg_start�̓��t���O���̌������ȑO�̎�

			outputLineHorizontalInBlankCell(rowIndex, 0, lastDayColIndex, CorLineStyle.SOLID);
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
				replaceListFieldData(rowIndex,  permitdateColIndex, "��");
			}

			// �ΐ���permitdateColIndex�̈�O�̃Z���܂ŏo��
			outputLineObliqueBlankCell(rowIndex, 0, permitdateColIndex, CorLineStyle.SOLID);

			// �u�ށv�ȍ~�̒���
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

				// ���ɕ������ݒ�ς݂̏ꍇ�́A����Ɠ��E�x�Ɠ��E�w�������̓��e���o�͂���Ă��邽�߂������D��
				if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
					// ���e����̏ꍇ�̂݁u�ށv���o�͂���
					replaceListFieldData(rowIndex, permitdateColIndex,"��");
				}

				// reg_start��reg_permitdate�̓��t�̍���2���ȏ�ł���ꍇ�A���̊Ԃ̓��Ɏ�̎ΐ��ň����B�i��ɑO�q�̏����Łu�ށv���o�͂��Ă������Ɓj
				outputLineObliqueBlankCell(rowIndex, regStartColIndex + 1, permitdateColIndex, CorLineStyle.SOLID);

				// �]�o�悪�󂯓��ꂽ���̑O��(reg_permitdate�̓��t)�̗����ȍ~�Ɏ�̉����������ň���
				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);

			}else{

				// ���ɕ������ݒ�ς݂̏ꍇ�́A����Ɠ��E�x�Ɠ��E�w�������̓��e���o�͂���Ă��邽�߂������D��
				if(StringUtils.isEmpty(attendListFielddArray[permitdateColIndex][rowIndex].getText()) || printFormBean.getDays()[permitdateColIndex].getEnfoce() == 1){
					// ���e����̏ꍇ�̂݁u�o�v���o�͂���
					replaceListFieldData(rowIndex,  permitdateColIndex, "��");
				}

				outputLineHorizontalInBlankCell(rowIndex, permitdateColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
			}
		}
		//-----------------------------------------------------
		// �ފw��(reg_mcode='40')
		//-----------------------------------------------------
		else if("40".equals(reg_mcode) &&											// �ފw��(reg_mcode='40')�ł���ꍇ
				compareMonth(targetYearMonth, regStart) == 0 ){						// �w�Z����������(reg_start)���Ώی��ł���

			// ���ɕ������ݒ�ς݂̏ꍇ�́A����Ɠ��E�x�Ɠ��E�w�������̓��e���o�͂���Ă��邽�߂������D��
			if(StringUtils.isEmpty(attendListFielddArray[regStartColIndex][rowIndex].getText()) || printFormBean.getDays()[regStartColIndex].getEnfoce() == 1){
				// ���e����̏ꍇ�̂݁u�o�v���o�͂���
				replaceListFieldData(rowIndex,  regStartColIndex, "��");
			}

			outputLineHorizontalInBlankCell(rowIndex, regStartColIndex + 1, lastDayColIndex, CorLineStyle.SOLID);
		}

	}

	/**
	 * �������v���̏o��
	 * @throws Exception
	 */
	private void outputStatistics() throws Exception{

		Map<String, Print32176000StatisticsFormBean> statisticsMap = printFormBean.getStatisticsMap();

		CrListField studentSumListListField = getCrListField(TOTAL_ATTEND);
		CrText[][] studentSumListListFieldArray = getListFieldGetColumns(studentSumListListField);

		int enforceDaySum = 0;			// ���Ǝ���
		int holidaySum = 0;			// ���Ƃ��s��Ȃ�����
		int zaisekiStuSumPre = 0;			// �ݐЎҐ�
		int entryStuSum = 0;			// �ٓ��E���w
		int leaveStuSum = 0;			// �ٓ��E�ފw

		// �j:1�A��:2
		String sex_male  = "1";
		String sex_female = "2";

		// ���Ǝ���
		if(statisticsMap.get(sex_male).getEnforceNum() != null && 0 < statisticsMap.get(sex_male).getEnforceNum()) {

			enforceDaySum = statisticsMap.get(sex_male).getEnforceNum();
		}
		if(statisticsMap.get(sex_female).getEnforceNum() != null && 0 < statisticsMap.get(sex_female).getEnforceNum()) {
			enforceDaySum = statisticsMap.get(sex_female).getEnforceNum();
		}
		getFieldSetData(ENFORCE_DAY_SUM, enforceDaySum);

		// ���Ƃ��s��Ȃ�����
		if(statisticsMap.get(sex_male).getEnforceNum() != null && 0 < statisticsMap.get(sex_male).getEnforceNum()) {
			holidaySum = statisticsMap.get(sex_male).getHolidayNum();
		}
		if(statisticsMap.get(sex_female).getEnforceNum() != null && 0 < statisticsMap.get(sex_female).getEnforceNum()) {
			holidaySum = statisticsMap.get(sex_female).getHolidayNum();
		}
		getFieldSetData(HOLIDAY_SUM, holidaySum);

		// �v
		getFieldSetData(DAY_SUM, enforceDaySum + holidaySum);

		// �Ώی���4���̏ꍇ�͑O�������ݍݐЂƍ����������������ݍݐЂ�\�����Ȃ�
		String targetYearMonth = printFormBean.getTargetYearMonth();
		String targetMonth = targetYearMonth.substring(4, 6).replaceAll("^0", "");
		if(!targetMonth.equals("4")) {
			// �O�����ݐЎҐ�(�j)
			setListFieldData(studentSumListListFieldArray, 0, 0, statisticsMap.get(sex_male).getZaisekiStuNumPre());
			// �O�����ݐЎҐ�(��)
			setListFieldData(studentSumListListFieldArray, 0, 1, statisticsMap.get(sex_female).getZaisekiStuNumPre());
			// �O�����ݐЎҐ�(���v)
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

		if(startColIndex > endColIndex){
			// �J�n�񂪏I����Ɠ����ꍇ�A������ꍇ�͏������I������
			return;
		}

		if(startColIndex == endColIndex){
			if(StringUtils.isEmpty(attendListFielddArray[startColIndex][rowIndex].getText())){
				outputLineHorizontal(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
			return;
		}

		// ���`��J�n�Z��(CrText)�̈ʒu
		int startCol = startColIndex;

		// colIndex�̈ʒu�ɂ͉��炩�̓��e���o�͂���Ă��邽��col<=colIndex�Ƃ��ă��[�v���Ŕ�����s��
		for(int col=startCol; col<=endColIndex; col++){

			if(!StringUtils.isEmpty(attendListFielddArray[col][rowIndex].getText())){
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

    /**
     * �o�����X�g�t�B�[���h��ɐ�������(���j
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

		if(startColIndex > endColIndex){
			// �J�n�񂪏I����Ɠ����ꍇ�A������ꍇ�͏������I������
			return;
		}

		if(startColIndex == endColIndex){
			if(StringUtils.isEmpty(attendListFielddArray[startColIndex][rowIndex].getText())){
				outputLineOblique(startColIndex, rowIndex, endColIndex, rowIndex, lineStyle);
			}
			return;
		}
		else{
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

	/**
	 * ����Ɠ��E�x�Ɠ��E�w�������̎������o��
	 * @throws Exception
	 */
	private void outputNotEnforcedayLine() throws Exception{

		Print32176000DayFormBean[] days = printFormBean.getDays();

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
					if(!StringUtils.isEmpty(attendListFielddArray[col][row].getText()) && !attendListFielddArray[col][row].getText().contains("��")){
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
     * �����r����
     * @param date1	����1(YYYYMM or YYYYMMDD)
     * @param date2	����2(YYYYMM or YYYYMMDD)
     * @return ����1������2�����O�̌��̏ꍇ��-1�A����1������2������̌��̏ꍇ�� 1�A�����ꍇ��0
     */
    private int compareMonth(String date1, String date2){

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Integer.parseInt(date1.substring(0,4)), Integer.parseInt(date1.substring(4,6))-1, 1);

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Integer.parseInt(date2.substring(0,4)), Integer.parseInt(date2.substring(4,6))-1, 1);


		if(cal1.before(cal2)){
			// ����1������2�����O�̌��̏ꍇ��-1��Ԃ�
			return -1;
		}else if(cal1.after(cal2)){
			// ����1������2������̌��̏ꍇ�� 1��Ԃ�
			return 1;
		}

    	return 0;
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

		if(StringUtils.isEmpty(outputText)){
			// �e�L�X�g�̓��e���󂩁A�o�Ȃ̏ꍇ�͒u���p������Œu��������
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText);
		}else if(printFormBean.getDays()[dayIndex].getEnfoce() == 1){
			// �o�ȃf�[�^������ꍇ�͏o�͓��e���܂Ƃ߂�
			setListFieldData(attendListFielddArray, dayIndex, rowIndex, replaceText + outputText);
		}
	}


	/**
	 * �o���W�v�́u0�v��u��������
	 * @param attendCount
	 * @return
	 */
	private String replaceAttendZero(Integer value) {
		// �D�y�́u0�v�̏ꍇ���̂܂܏o��
//		if(value == 0){
//			return "�E";
//		}
		return value.toString();
	}

	/**
	 * @param printFormBean the printFormBean to set
	 */
	public void setPrintFormBean(Print32176000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

}
