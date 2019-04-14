/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.BeanUtilManager;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000AttendSummaryEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000EntryStudentEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000LeaveStudentEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000NotEnfocedayEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000StatisticsEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32176000StudentEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000DayFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000EntryLeaveFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32176000StudentFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32176000FormBean;
import jp.co.systemd.tnavi.db.constants.CommonConstantsUseable;
import jp.co.systemd.tnavi.junior.common.db.entity.EnforcedayEntity;


/**
 * <PRE>
 * �o�ȕ���(�ʏ�w���@���R�s�p) ��� �f�[�^�擾 Service.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Print32176000Service extends AbstractExecuteQuery{

	/** log4j */
	private static final Log log = LogFactory.getLog(Print32176000Service.class);

	/** �����R�[�h */
	private String user = "";

	/** �N�x */
	private String nendo = "";

	/** �N�x�J�n�� */
	private String nendoStartDate = "";

	/** �N�x�I���� */
	private String nendoEndDate = "";

	/** ����FormBean */
	private Search32176000FormBean searchFormBean;

	/** ���FormBean */
	private Print32176000FormBean printFormBean;


	public void execute(HttpServletRequest request, SystemInfoBean sessionBean) {

		this.user   = sessionBean.getUserCode();
		this.nendo	= request.getParameter("nendo");
		this.nendoStartDate = this.nendo + sessionBean.getSystemNendoStartDate();

		if(sessionBean.getSystemNendoStartDate().compareTo(sessionBean.getSystemNendoEndDate()) > 0){
			// �N�x�I�������J�n�����Ⴂ�ꍇ�N�x��+1
			this.nendoEndDate = (Integer.parseInt(this.nendo) + 1) + sessionBean.getSystemNendoEndDate();
		}else{
			this.nendoEndDate = this.nendo + sessionBean.getSystemNendoEndDate();
		}

		searchFormBean = new Search32176000FormBean();
		printFormBean = new Print32176000FormBean();

		// �w�Z�����̋敪���Z�b�g
		printFormBean.setUserKind(sessionBean.getUseKind2());

		// �����Ώی����Z�b�g
		printFormBean.setTargetYearMonth(request.getParameter("yyyymm"));

		// �o�͑Ώی��̓������Z�b�g
		int monthDayCnt = Integer.parseInt(request.getParameter("monthDayCount"));
		printFormBean.setMonthDayCnt(monthDayCnt);

		// �o�͑Ώۓ��̏����Z�b�g
		Print32176000DayFormBean[] days = new Print32176000DayFormBean[printFormBean.getMonthDayCnt()];
		for(int i=0; i<days.length; i++){
			days[i] = new Print32176000DayFormBean();
			days[i].setDay(i + 1);
			days[i].setEnfoce(1);	// �����l�͎��Ɠ��Ƃ���
			days[i].setWeekDayColor(request.getParameter("weekday_color_" +  Integer.toString(i)));	// �j���̐F
			days[i].setText(new StringBuilder());
		}
		printFormBean.setDays(days);
		//���[�U�[�R�[�h���Z�b�g
		printFormBean.setUserCode(this.user);

		// �����������i�[
		searchFormBean.setGrade(request.getParameter("grade"));						// �w�N
		searchFormBean.setHmrclass(request.getParameter("hmrclass"));				// �z�[�����[���N���X�R�[�h
		searchFormBean.setOutputNameFlg(request.getParameter("outputNameFlg"));		// �����o�͎�� 1:�ʏ́@2:�ː�
		searchFormBean.setOutputKanaFlg(request.getParameter("outputKanaFlg"));		// ���ȏo�͎�� 1:�����@2:����
		printFormBean.setGrade(request.getParameter("grade"));						// �w�N
		printFormBean.setHmrclass(request.getParameter("hmrclass"));				// �z�[�����[���N���X�R�[�h


		super.execute();
	}


	@Override
	protected void doQuery() throws TnaviDbException {

		// �z�[�����[���ԍ����擾
		getClsno();

		// �Ώې��k�����擾
		getTargetStudents();

		// �o�������擾
		getAttendList();

		// �o���W�v���擾
		getAttendSummary();

		// �]�ғ������擾
		getEntryStuList();

		// �]�ފw�����擾
		getLeaveStuList();

		// �o�͗D�揇�ʂ� ����Ɠ� < �x�Ɠ� < �w������ �̂��߁A�����f�[�^���擾���āA�擾�ł����ꍇ�͏㏑�����s��
		// ����Ɠ����擾
		getNotEnforceday();

		// �x�Ɠ����擾
		getHoliday();

		// �w���������擾
		getClosingclass();

		// �w���S�C�����擾
		getTeacherName();
	}

	/**
	 * �z�[�����[������N���X�ԍ����擾����
	 */
	private void getClsno(){

		Object[] param = new Object[]{
				 this.user						// [�����R�[�h]
				,this.nendo						// [�������̔N�x]
				,searchFormBean.getGrade()		// [�������N���X�̊w�N]
				,searchFormBean.getHmrclass()	// [�������z�[�����[��]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Hroom.sql", param, HRoomEntity.class);

		@SuppressWarnings("unchecked")
		List<HRoomEntity> entityList = (List<HRoomEntity>) this.executeQuery(qm);

		String clsno = "";
		if(entityList.size() > 0){
			clsno = entityList.get(0).getHmr_clsno();
		}
		searchFormBean.setClsno(clsno);
	}


	/**
	 * �ΏۂƂȂ鐶�k��Map���쐬����
	 */
	private void getTargetStudents(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// �Ώی��̍ŏI��
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		//����������5:�ʏ́A1:�ː�
		String outputHisNameFlg = ("1".equals(searchFormBean.getOutputNameFlg())? "5":"1");
		Object[] param = new Object[]{
				searchFormBean.getOutputNameFlg()
				,searchFormBean.getOutputNameFlg()
				,this.user
				,targetYearMonthLastDay
				,outputHisNameFlg
				,outputHisNameFlg
				,this.user
				,this.nendo
				,searchFormBean.getGrade()
				,searchFormBean.getClsno()
				,targetYearMonthLastDay					// �������_�œ��w���Ă��鎙�����k�̂�
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Student.sql", param, Print32176000StudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000StudentEntity> entityList = (List<Print32176000StudentEntity>) this.executeQuery(qm);


		// ������ɏ����o�͂��邽��LinkedHashMap
		Map<String, Print32176000StudentFormBean> map = new LinkedHashMap<String, Print32176000StudentFormBean>();
		try {
			for(Print32176000StudentEntity entity : entityList){

				Print32176000StudentFormBean studentFormBean = new Print32176000StudentFormBean();
				beanUtil.copyProperties(studentFormBean, entity);

				String stu_name = "";
				// �ʏ̂��ːЂ���DB����擾���ɔ��肵�Ă��邽�߁A�����ł͊����E���Ȃ̔���̂�
				if("0".equals(searchFormBean.getOutputKanaFlg())){
					// ����
					stu_name = entity.getStu_name();
				}else if("1".equals(searchFormBean.getOutputKanaFlg())){
					// ����
					stu_name = entity.getStu_kana();
				}
				studentFormBean.setStu_name(stu_name);
				studentFormBean.setAttend(new String[printFormBean.getMonthDayCnt()]);
				studentFormBean.setStopKibikiFlg(new String[printFormBean.getMonthDayCnt()]);
				studentFormBean.setAttendSummary(new Print32176000AttendSummaryEntity());

				map.put(entity.getStu_stucode(), studentFormBean);
			}
		} catch (Exception e) {
			//�v���p�e�B�ϊ��G���[
			log.error("BeanUtils�ϊ��G���[:Print32176000StudentEntity To Print32176000StudentFormBean");
			throw new TnaviException(e);
		}

		printFormBean.setStudentMap(map);

	}


	/**
	 * �o�������擾
	 */
	private void getAttendList(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,this.user						// [�����R�[�h]
				,this.nendo						// [�������̔N�x]
				,searchFormBean.getGrade()		// [�������N���X�̊w�N]
				,searchFormBean.getClsno()		// [�������N���X�̃N���X�ԍ�]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Attend.sql", param, Print32176000AttendEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000AttendEntity> entityList = (List<Print32176000AttendEntity>) this.executeQuery(qm);

		// �o�����𐶓k���ɃZ�b�g
		for(Print32176000AttendEntity entity : entityList){

			if(!StringUtils.isEmpty(entity.getAtt_date())){
				Integer day = Integer.parseInt(entity.getAtt_date().substring(6, 8));
				printFormBean.getStudentMap().get(entity.getCls_stucode()).getAttend()[day - 1] = entity.getDisplay();	// �o���L��
				printFormBean.getStudentMap().get(entity.getCls_stucode()).getStopKibikiFlg()[day - 1] = entity.getStopKibikiFlg();	// �o������������̃t���O
			}
		}
	}


	/**
	 * �o���W�v���擾
	 */
	private void getAttendSummary(){

		// �Ώی�
		String targetMonth = printFormBean.getTargetYearMonth().substring(4, 6);
		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						//[�����R�[�h]
				,this.nendo						//[�������̔N�x]
				,targetYearMonthFirstDay		//[�Ώی��̌�����]
				,targetYearMonthLastDay			//[�Ώی��̌�����]
				,targetYearMonthLastDay			//[�Ώی��̌�����]
				,searchFormBean.getGrade()		//[�������N���X�̊w�N]
				,searchFormBean.getClsno()		//[�������N���X�̃N���X�ԍ�]
				,this.user						//[�����R�[�h]
				,this.nendo						//[�������̔N�x]
				,targetMonth					//[�Ώی�]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000AttendSummary.sql", param, Print32176000AttendSummaryEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000AttendSummaryEntity> entityList = (List<Print32176000AttendSummaryEntity>) this.executeQuery(qm);


		// ���ʓ��v���擾
		Map<String, Print32176000StatisticsFormBean> statisticsMap = getMonthStatistics();

		for(Print32176000AttendSummaryEntity entity : entityList){
			// �ΏۂƂȂ鐶�k�ɏW�v���ʂ��Z�b�g
			printFormBean.getStudentMap().get(entity.getCls_stucode()).setAttendSummary(entity);
		}

		// ���ʓ��v���Z�b�g
		printFormBean.setStatisticsMap(statisticsMap);
	}

	/**
	 * �]�ғ��ҏ����擾
	 */
	private void getEntryStuList(){

		Map<String, Print32176000StudentFormBean> studentsMap = printFormBean.getStudentMap();

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
			     this.user						//[�����R�[�h]
			    ,this.nendo						//[�������̔N�x]
			    ,searchFormBean.getGrade()		//[�������N���X�̊w�N]
			    ,searchFormBean.getClsno()		//[�������N���X�̃N���X�ԍ�]
			    ,targetYearMonthFirstDay		//[�Ώی��̌�����]
			    ,targetYearMonthLastDay			//[�Ώی��̌�����]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000EntryStuList.sql", param, Print32176000EntryStudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000EntryStudentEntity> entityList = (List<Print32176000EntryStudentEntity>) this.executeQuery(qm);

		List<Print32176000EntryLeaveFormBean> entryStuList = new ArrayList<Print32176000EntryLeaveFormBean>();
		for(Print32176000EntryStudentEntity entity : entityList){

			String stu_name = studentsMap.get(entity.getStu_stucode()).getStu_name();
			entryStuList.add(new Print32176000EntryLeaveFormBean(entity.getStu_stucode(), entity.getStu_entry(), stu_name, entity.getSt4_preschname(),entity.getStu_qualifi()));

			// �Ώې��k�̓]�������Z�b�g
			studentsMap.get(entity.getStu_stucode()).setEntry_start(entity.getStu_entry());
		}

		// �]�ғ��ҏ����Z�b�g
		printFormBean.setEntryStuList(entryStuList);
	}

	/**
	 * �]�ފw�ҏ����擾
	 */
	private void getLeaveStuList(){

		Map<String, Print32176000StudentFormBean> studentsMap = printFormBean.getStudentMap();

		Object[] param = new Object[]{
				 this.user						//[�����R�[�h]
				,this.nendo						//[�������̔N�x]
				,searchFormBean.getGrade()		//[�������N���X�̊w�N]
				,searchFormBean.getClsno()		//[�������N���X�̃N���X�ԍ�]
				,this.nendoStartDate			//[�N�x�J�n��]	�O���ȑO�ꍇ�̏��������邽�߁A�͈͔͂N�x�J�n������Ƃ���
				,this.nendoEndDate				//[�N�x�I����]	�����ȑO�ꍇ�̏��������邽�߁A�͈͔͂N�x�I�����܂łƂ���
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000LeaveStuList.sql", param, Print32176000LeaveStudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000LeaveStudentEntity> entityList = (List<Print32176000LeaveStudentEntity>) this.executeQuery(qm);


		List<Print32176000EntryLeaveFormBean> leaveStuList = new ArrayList<Print32176000EntryLeaveFormBean>();
		for(Print32176000LeaveStudentEntity entity : entityList){
			String stucode = entity.getReg_stucode();
			// �o�͑Ώێ҂Ɋ܂܂�Ă��Ȃ��ꍇ�͖���
			if (!studentsMap.containsKey(stucode)) {
				continue;
			}

			String stu_name = studentsMap.get(stucode).getStu_name();

			if(printFormBean.getTargetYearMonth().equals(entity.getLeave_date().substring(0, 6))){
				// �Ώی��̏ꍇ�A���X�g�ɒǉ�
				leaveStuList.add(new Print32176000EntryLeaveFormBean(stucode, entity.getLeave_date(), stu_name, entity.getSt4_anotherschname(), entity.getReg_mcode()));
			}

			studentsMap.get(stucode).setReg_mcode(entity.getReg_mcode());
			studentsMap.get(stucode).setReg_start(entity.getReg_start());
			studentsMap.get(stucode).setReg_permitdate(entity.getReg_permitdate());
			studentsMap.get(stucode).setLeave_date(entity.getLeave_date());
		}

		printFormBean.setLeaveStuList(leaveStuList);
	}


	/**
	 * ����Ɠ����擾
	 */
	private void getNotEnforceday(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [�����R�[�h]
				,searchFormBean.getGrade()		// [�������N���X�̊w�N]
				,this.nendo						// [�������̔N�x]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000NotEnfoceday.sql", param, EnforcedayEntity.class);

		@SuppressWarnings("unchecked")
		List<EnforcedayEntity> entityList = (List<EnforcedayEntity>) this.executeQuery(qm);

		for(EnforcedayEntity entity : entityList){
			int index = Integer.parseInt(entity.getEnf_day()) - 1;
			printFormBean.getDays()[index].setEnfoce(entity.getEnf_enforce());
		}

	}


	/**
	 * �x�Ɠ����擾
	 */
	private void getHoliday(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [�����R�[�h]
				,this.nendo						// [�������̔N�x]
				,searchFormBean.getGrade()		// [�������N���X�̊w�N]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Holiday.sql", param, Print32176000NotEnfocedayEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000NotEnfocedayEntity> entityList = (List<Print32176000NotEnfocedayEntity>) this.executeQuery(qm);

		for(Print32176000NotEnfocedayEntity entity : entityList){

			int start_day = Integer.parseInt(entity.getStart_date().substring(6, 8));
			int end_day   = Integer.parseInt(entity.getEnd_date().substring(6, 8));

			String start_year_month = entity.getStart_date().substring(0, 6);
			String end_year_month = entity.getEnd_date().substring(0, 6);

			// �J�n���ƏI�������قȂ�ꍇ�̏���
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(start_year_month)){
				// �J�n�����o�͌��Ɠ����ꍇ�́A�I���������̖����ōX�V
				end_day = printFormBean.getMonthDayCnt();
			}
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(end_year_month)){
				// �I�������o�͌��Ɠ����ꍇ�́A�J�n��������ōX�V
				start_day = 1;
			}

			for(int i=start_day; i<=end_day; i++){
				int arrayIndex = i - 1;
				// ���Ɠ��t���O��0:����Ɠ��ɃZ�b�g
				printFormBean.getDays()[arrayIndex].setEnfoce(0);

				if(printFormBean.getDays()[arrayIndex].getText().length() > 0){
					// ���ɋx�Ɠ�����������ꍇ�͋󔒂�ǉ�
					printFormBean.getDays()[arrayIndex].getText().append("�@");
				}
				printFormBean.getDays()[arrayIndex].getText().append(entity.getText());
			}
		}
	}


	/**
	 * �w���������擾
	 */
	private void getClosingclass(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [�����R�[�h]
				,this.nendo						// [�������̔N�x]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,searchFormBean.getGrade()		// [�������N���X�̊w�N]
				,searchFormBean.getClsno()		// [�������N���X�̃N���X�ԍ�]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Closingclass.sql", param, Print32176000NotEnfocedayEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000NotEnfocedayEntity> entityList = (List<Print32176000NotEnfocedayEntity>) this.executeQuery(qm);

		for(Print32176000NotEnfocedayEntity entity : entityList){

			int start_day = Integer.parseInt(entity.getStart_date().substring(6, 8));
			int end_day   = Integer.parseInt(entity.getEnd_date().substring(6, 8));

			String start_year_month = entity.getStart_date().substring(0, 6);
			String end_year_month = entity.getEnd_date().substring(0, 6);

			// �J�n���ƏI�������قȂ�ꍇ�̏���
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(start_year_month)){
				// �J�n�����o�͌��Ɠ����ꍇ�́A�I���������̖����ōX�V
				end_day = printFormBean.getMonthDayCnt();
			}
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(end_year_month)){
				// �I�������o�͌��Ɠ����ꍇ�́A�J�n��������ōX�V
				start_day = 1;
			}

			for(int i=start_day; i<=end_day; i++){
				int arrayIndex = i - 1;
				// ���Ɠ��t���O��0:����Ɠ��ɃZ�b�g
				printFormBean.getDays()[arrayIndex].setEnfoce(0);

				if(printFormBean.getDays()[arrayIndex].getText().length() > 0){
					// ���ɋx�Ɠ�����������ꍇ�͋󔒂�ǉ�
					printFormBean.getDays()[arrayIndex].getText().append("�@");
				}
				printFormBean.getDays()[arrayIndex].getText().append(entity.getText());
				printFormBean.getDays()[arrayIndex].setClosingclass(true);
			}

		}
	}

	/**
	 * �w���S�C�������擾
	 */
	private void getTeacherName(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";

		Object[] param = new Object[]{
			    this.user						// [�����R�[�h]
			   ,this.nendo						// [�������̔N�x]
			   ,searchFormBean.getClsno()		// [�������N���X�̃N���X�ԍ�]
			   ,targetYearMonthFirstDay			// [�Ώی��̌�����]
			   ,this.user						// [�����R�[�h]
			   ,this.nendo						// [�������̔N�x]
			   ,searchFormBean.getClsno()		// [�������N���X�̃N���X�ԍ�]
			   ,this.user						// [�����R�[�h]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000TeacherName.sql", param, String.class);
		String teacherName = (String) this.executeQuery(qm);
		printFormBean.setTeacherName(teacherName);
	}

	/**
	 * �������v���擾
	 */
	private Map<String, Print32176000StatisticsFormBean> getMonthStatistics(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// �Ώی��̌�����
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// �Ώی��̌�����
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Calendar calFirstDay = Calendar.getInstance();
		calFirstDay.set(Integer.parseInt(printFormBean.getTargetYearMonth().substring(0, 4)),
	            Integer.parseInt(printFormBean.getTargetYearMonth().substring(4, 6)) - 1,
	            1);
		calFirstDay.add(Calendar.MONTH, -1);

	    String beforeYearMonthFirstDay = new SimpleDateFormat("yyyyMMdd").format(calFirstDay.getTime());

	    Calendar calLastDay = Calendar.getInstance();
	    calLastDay.set(Integer.parseInt(printFormBean.getTargetYearMonth().substring(0, 4)),
	            Integer.parseInt(printFormBean.getTargetYearMonth().substring(4, 6)) - 1,
	            1);
	    calLastDay.add(Calendar.DATE, -1);
	    String beforeYearMonthLastDay = new SimpleDateFormat("yyyyMMdd").format(calLastDay.getTime());


		Object[] param = new Object[]{
				this.user						// [�����R�[�h]
				,this.nendo						// [�������̔N�x]
				,searchFormBean.getClsno()		// [�������N���X�̃N���X�ԍ�]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,beforeYearMonthFirstDay		// [�Ώی��̌�����]
				,beforeYearMonthLastDay			// [�Ώی��̌�����]

		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000MonthStatistics.sql", param, Print32176000StatisticsEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000StatisticsEntity> entityList = (List<Print32176000StatisticsEntity>) this.executeQuery(qm);


		// �j�����̌������v���i�[����Map
		Map<String, Print32176000StatisticsFormBean> map = new HashMap<String, Print32176000StatisticsFormBean>();
		try{
			for(Print32176000StatisticsEntity entity : entityList){
				Print32176000StatisticsFormBean statisticsFormBean = new Print32176000StatisticsFormBean();
				beanUtil.copyProperties(statisticsFormBean, entity);
				map.put(entity.getStu_sex(), statisticsFormBean);
			}
		}catch(Exception e){
			//�v���p�e�B�ϊ��G���[
			log.error("BeanUtils�ϊ��G���[:Print32176000StatisticsEntity To Print32176000StatisticsFormBean");
			throw new TnaviException(e);
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_MALE)){
			// �j���̓��v�f�[�^���擾�ł��Ȃ������ꍇ�ɋ�f�[�^�i�S�ă[���j���Z�b�g
			map.put(CommonConstantsUseable.COD_CODE_SEX_MALE, new Print32176000StatisticsFormBean());
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_FEMALE)){
			// �����̓��v�f�[�^���擾�ł��Ȃ������ꍇ�ɋ�f�[�^�i�S�ă[���j���Z�b�g
			map.put(CommonConstantsUseable.COD_CODE_SEX_FEMALE, new Print32176000StatisticsFormBean());
		}

		return map;
	}

	public void setParameters(String user,String nendo ) {
		this.user = user;
		this.nendo = nendo;
	}

	public Search32176000FormBean getSearch30029000FormBean() {
		return searchFormBean;
	}

	public void setSearch32176000FormBean(Search32176000FormBean search32176000FormBean) {
		this.searchFormBean = search32176000FormBean;
	}

	public Print32176000FormBean getPrintFormBean() {
		return printFormBean;
	}

}
