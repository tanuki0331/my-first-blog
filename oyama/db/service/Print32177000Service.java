/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.att.db.entity.AttPrintAttendEntity;
import jp.co.systemd.tnavi.att.db.entity.AttPrintAttendSummaryEntity;
import jp.co.systemd.tnavi.att.db.entity.AttPrintStudentEntity;
import jp.co.systemd.tnavi.att.db.service.AbstractAttPrintService;
import jp.co.systemd.tnavi.att.formbean.AttPrintDayFormBean;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.att.formbean.AttPrintStudentFormBean;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.BeanUtilManager;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32177000EnforceEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32177000StatisticsEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32177000StatisticsFormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32177000FormBean;
import jp.co.systemd.tnavi.db.constants.CommonConstantsUseable;
import jp.co.systemd.tnavi.junior.att.db.entity.MonthDataListEntity;


/**
 * <PRE>
 * �o�ȕ���(���ʎx���w���@���R�s�p) ��� �f�[�^�擾 Service.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Print32177000Service extends AbstractAttPrintService{

	/** log4j */
	private static final Log log = LogFactory.getLog(Print32177000Service.class);

	/** ����FormBean */
	private Search32177000FormBean searchFormBean;

	/** ���FormBean */
	private Print32177000FormBean printFormBean;



	public Print32177000Service(HttpServletRequest request, SystemInfoBean sessionBean, Search32177000FormBean searchFormBean, Print32177000FormBean printFormBean) {
		super(request, sessionBean, searchFormBean, printFormBean);

		searchFormBean.setOutputNameFlg(request.getParameter("outputNameFlg"));		// �����o�͎�� 1:�ʏ́@2:�ː�
		searchFormBean.setOutputKanaFlg(request.getParameter("outputKanaFlg"));		// ���ȏo�͎�� 1:�����@2:����
		this.searchFormBean = searchFormBean;
		this.printFormBean = printFormBean;
		this.printFormBean.setSearchFormBean(searchFormBean);


		// �����Ώی����Z�b�g
		printFormBean.setTargetYearMonth(searchFormBean.getYyyymm());
		printFormBean.setGroupName(this.searchFormBean.getGroupname());


		// ���ʎx���Ȃ̂Ō𗬊w���͊܂܂Ȃ�
		searchFormBean.setExcFlg("2");
		super.exc_flg = "2";
	}

	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}

	@Override
	protected void doQuery() throws TnaviDbException {

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

		// ���̓��t�����擾
		getHeaderDays();

		// �w�����S�C�����擾
		printFormBean.setTeacherNameMain(getTeacherName("1", 3));

		// �w�����S�C�����擾
		printFormBean.setTeacherNameSub(getTeacherName("2", 3));

		// ���ʓ��v���擾
		getMonthEnforce();

		// ���ʓ��v(�O����)���擾
		getMonthStatistics();

		// �o�͊w�N���̌������擾����
		for (Entry<String, AttPrintStudentFormBean> entry : printFormBean.getStudentMap().entrySet()) {
			AttPrintStudentFormBean studentFormBean = entry.getValue();

			String grade = studentFormBean.getCls_glade();
			if(!printFormBean.getDaysMap().containsKey(grade)){

				AttPrintDayFormBean[] days = getDayMap(grade);
				printFormBean.getDaysMap().put(grade, days);

			}
		}

		// �x�Ɠ���w�N�ƁA�e�w�N�̎��Ɠ����e���r���āA���e�̒������s��
		checkDays();
	}

	/**
	 * �ΏۂƂȂ鐶�k��Map���쐬����
	 */
	@Override
	protected void getTargetStudents(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		//����������5:�ʏ́A1:�ː�
		String sth_kind = ("1".equals(searchFormBean.getOutputNameFlg())? "5":"1");
		Object[] param = new Object[]{
				 sessionBean.getUserCode()		//�����R�[�h
				,searchFormBean.getNendo()		//�N�x
				,getClsType()					//�N���X���
				,getGroupCode()					// [�O���[�v�R�[�h]
				,strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1")			//������
				,strLastDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth())	//������
				,sth_kind		//�ːЁE�ʏ̎��
				,searchFormBean.getNendo() + sessionBean.getSystemNendoStartDate().trim()
				,exc_flg
				} ;

		QueryManager qm = new QueryManager("cus/oyama/get32177000AttPrintStudent.sql", param, AttPrintStudentEntity.class);
		StringBuffer plusSQL = new StringBuffer();
		plusSQL.append(" ORDER BY cls_glade, cls_reference_number, cls_stucode ");
		qm.setPlusSQL(plusSQL.toString());

		@SuppressWarnings("unchecked")
		List<AttPrintStudentEntity> entityList = (List<AttPrintStudentEntity>) this.executeQuery(qm);


		try {
			for(AttPrintStudentEntity entity : entityList){

				AttPrintStudentFormBean studentFormBean = new AttPrintStudentFormBean();
				beanUtil.copyProperties(studentFormBean, entity);

				if("2".equals(searchFormBean.getOutputKanaFlg())){
					studentFormBean.setStu_name_first(entity.getStu_kana_first());
					studentFormBean.setStu_name_end(entity.getStu_kana_end());
				}

				studentFormBean.setAttend(new String[super.monthDayCnt]);
				studentFormBean.setStopKibikiFlg(new String[super.monthDayCnt]);
				studentFormBean.setAttendSummary(new AttPrintAttendSummaryEntity());

				printFormBean.getStudentMap().put(entity.getStu_stucode(), studentFormBean);

			}
		} catch (Exception e) {
			//�v���p�e�B�ϊ��G���[
			log.error("BeanUtils�ϊ��G���[:AttPrintStudentEntity To AttPrintStudentFormBean");
			throw new TnaviException(e);
		}

	}

	/**
	 * �o�������擾
	 */
	@Override
	protected void getAttendList(){

		Object[] param = new Object[]{
				 sessionBean.getUserCode()		// [�����R�[�h]
				,searchFormBean.getNendo()		// [�������̔N�x]
				,getClsType()					// ����������
				,getGroupCode()					// ���������ʂɍ��킹���O���[�v�R�[�h
				,searchFormBean.getMonth()		// [�Ώ���]
				,firstDayOfMonth				// [�Ώی��̌�����]
				,lastDayOfMonth					// [�Ώی��̌�����]
				,exc_flg
				,printFormBean.getAttendDisplayValue()
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000AttendWith.sql", param, AttPrintAttendEntity.class);
		qm.setPlusSQLFile("cus/oyama/get32177000AttPrintAttend.sql");

		@SuppressWarnings("unchecked")
		List<AttPrintAttendEntity> entityList = (List<AttPrintAttendEntity>) this.executeQuery(qm);

		// �o�����𐶓k���ɃZ�b�g
		for(AttPrintAttendEntity entity : entityList){

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
	@Override
	protected void getAttendSummary(){

		Object[] param = new Object[]{
				 sessionBean.getUserCode()		// [�����R�[�h]
				,searchFormBean.getNendo()		// [�������̔N�x]
				,getClsType()					// ����������
				,getGroupCode()					// ���������ʂɍ��킹���O���[�v�R�[�h
				,searchFormBean.getMonth()		// [�Ώ���]
				,firstDayOfMonth				// [�Ώی��̌�����]
				,lastDayOfMonth					// [�Ώی��̌�����]
				,exc_flg
				,printFormBean.getAttendDisplayValue()
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000AttendWith.sql", param, AttPrintAttendSummaryEntity.class);
		qm.setPlusSQLFile("cus/oyama/getPrint32177000AttendSummary.sql");

		@SuppressWarnings("unchecked")
		List<AttPrintAttendSummaryEntity> entityList = (List<AttPrintAttendSummaryEntity>) this.executeQuery(qm);

		for(AttPrintAttendSummaryEntity entity : entityList){
			// �ΏۂƂȂ鐶�k�ɏW�v���ʂ��Z�b�g
			printFormBean.getStudentMap().get(entity.getCls_stucode()).setAttendSummary(entity);
		}
	}


	/**
	 * �w��w�N�̌��f�[�^���擾����B
	 * @param grade �w�N
	 * @return ���f�[�^�̔z��
	 */
	private AttPrintDayFormBean[] getDayMap(String grade){

		String classType = "";
		if(StringUtils.equals(searchFormBean.getClassType(), AttPrintSearchFormBeanImpl.CLASS_TYPE_SPCLASS)){
			classType = AttPrintSearchFormBeanImpl.CLASS_TYPE_SPCLASS;
		}else if(StringUtils.equals(searchFormBean.getClassType(), AttPrintSearchFormBeanImpl.CLASS_TYPE_PLCLASS)){
			classType = AttPrintSearchFormBeanImpl.CLASS_TYPE_PLCLASS;
		}

		Object[] param = {sessionBean.getUserCode(), searchFormBean.getNendo(), grade, searchFormBean.getGroupcode(), classType, searchFormBean.getMonth() };
		QueryManager queryManager = new QueryManager("cus/oyama/getPrint32177000MonthDataByGroup.sql", param, MonthDataListEntity.class);

		@SuppressWarnings("unchecked")
		List<MonthDataListEntity> monthDataList = (List<MonthDataListEntity>)this.executeQuery(queryManager);


		AttPrintDayFormBean[] days = new AttPrintDayFormBean[super.monthDayCnt];

		for (MonthDataListEntity monthData : monthDataList) {

			int day = Integer.parseInt(monthData.getEnf_day());
			int dayIndex = day - 1;

			if(days[dayIndex] == null){

				AttPrintDayFormBean dayFormBean = new AttPrintDayFormBean();
				dayFormBean.setDay(day);
				dayFormBean.setEnfoce(Integer.parseInt(monthData.getEnf_enforce()));
				dayFormBean.setWeekday(monthData.getEnf_weekday());

				dayFormBean.setText(new StringBuilder());
				if(!StringUtils.isEmpty(monthData.getHol_name())){
					// �x�Ɠ�����
					dayFormBean.getText().append(monthData.getHol_name());
				}
				if(!StringUtils.isEmpty(monthData.getClo_printword())){
					// �w�����󎚕���
					dayFormBean.getText().append(monthData.getClo_printword());
					dayFormBean.setClo_printword(monthData.getClo_printword());
					// �w�����̏ꍇ�A����Ɠ��ɐݒ肷��B
					dayFormBean.setEnfoce(0);
				}

				days[dayIndex] = dayFormBean;
			}else if(days[dayIndex].getText().toString().length() > 0){
				// ������t�ɕ����̋x�Ɠ����ݒ肳��Ă���ꍇ
				days[dayIndex].getText().append("�@");

				if(!StringUtils.isEmpty(monthData.getHol_name())){
					// �x�Ɠ�����
					days[dayIndex].getText().append(monthData.getHol_name());
				}
				if(!StringUtils.isEmpty(monthData.getClo_printword())){
					// �w�����󎚕���
					days[dayIndex].getText().append(monthData.getClo_printword());
					days[dayIndex].setClo_printword(monthData.getClo_printword());
					// �w�����̏ꍇ�A����Ɠ��ɐݒ肷��B
					days[dayIndex].setEnfoce(0);
				}
			}

		}

		return days;
	}


	/**
	 * ���ʎx���w���S�C�������擾
	 * @param kind 1:���S�C�A2:���S�C
	 * @param numOfStaff �����̐l��
	 * @return �w���S�C����
	 */
	@Override
	protected String getTeacherName(String kind, int numOfStaff){

		Object[] param = new Object[]{
			    sessionBean.getUserCode()		// [�����R�[�h]
			   ,searchFormBean.getNendo()		// [�������̔N�x]
			   ,searchFormBean.getGroupcode()	// [�������N���X�̃O���[�v�R�[�h]
			   ,kind							// 1:���S�C�A2:���S�C
			   ,firstDayOfMonth					// [�Ώی��̌�����]
			   ,lastDayOfMonth					// [�Ώی��̌�����]
			   ,numOfStaff						// [�����̐l��]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/get32177000AttPrintSpGroupStaff.sql", param, StaffEntity.class);

		@SuppressWarnings("unchecked")
		List<StaffEntity> staffList = (List<StaffEntity>)this.executeQuery(qm);

		StringBuffer staffNames = new StringBuffer();
		for (StaffEntity staffEntity : staffList) {

			staffNames.append(staffEntity.getStf_name_w());
			break;
		}

		return staffNames.toString();
	}


	/**
	 * �x�Ɠ���w�N�ƁA�e�w�N�̎��Ɠ����e���r���āA���e�̒������s��
	 */
	private void checkDays() {

		AttPrintDayFormBean[] days = printFormBean.getDays();

		for (int i = 0; i < days.length; i++) {

			int enforce = 0;
			for (Entry<String, AttPrintDayFormBean[]> entry : printFormBean.getDaysMap().entrySet()) {
				AttPrintDayFormBean[] gradeDays = entry.getValue();
				enforce += gradeDays[i].getEnfoce();
			}

			if(enforce > 0){
				// �e�w�N�Ŏ��Ɠ�������i���݂���j�ꍇ�͋x�Ɠ����̂��N���A����
				days[i].setText(new StringBuilder());
			}
		}
	}

	/**
	 * �������v���擾
	 */
	private void getMonthEnforce(){

		// �Ώی��̌�����
		String targetYearMonthFirstDay =strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1");
		// �Ώی��̌�����
		String targetYearMonthLastDay = strLastDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth());

		Object[] param = new Object[]{
				sessionBean.getUserCode()			// [�����R�[�h]
				,searchFormBean.getNendo()			// [�������̔N�x]
				,targetYearMonthFirstDay			// [�Ώی��̌�����]
				,targetYearMonthLastDay				// [�Ώی��̌�����]
				,getClsType()
				,getGroupCode()

		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000EnforceNum.sql", param, Print32177000EnforceEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32177000EnforceEntity> entityList = (List<Print32177000EnforceEntity>) this.executeQuery(qm);

		// ���ʓ��v���Z�b�g
		printFormBean.setEnforceList(entityList);
	}

	/**
	 * �������v���擾
	 */
	private void getMonthStatistics(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// �Ώی��̌�����
		String targetYearMonthFirstDay =strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1");
		// �Ώی��̌�����
		String targetYearMonthLastDay = strLastDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth());

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
				sessionBean.getUserCode()		// [�����R�[�h]
				,searchFormBean.getNendo()		// [�������̔N�x]
				,targetYearMonthFirstDay		// [�Ώی��̌�����]
				,targetYearMonthLastDay			// [�Ώی��̌�����]
				,beforeYearMonthFirstDay		// [�Ώی��̌�����]
				,beforeYearMonthLastDay			// [�Ώی��̌�����]
				,getClsType()
				,getGroupCode()
		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000MonthStatistics.sql", param, Print32177000StatisticsEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32177000StatisticsEntity> entityList = (List<Print32177000StatisticsEntity>) this.executeQuery(qm);


		// �j�����̌������v���i�[����Map
		Map<String, Print32177000StatisticsFormBean> map = new HashMap<String, Print32177000StatisticsFormBean>();
		try{
			for(Print32177000StatisticsEntity entity : entityList){
				Print32177000StatisticsFormBean statisticsFormBean = new Print32177000StatisticsFormBean();
				beanUtil.copyProperties(statisticsFormBean, entity);
				map.put(entity.getStu_sex(), statisticsFormBean);
			}
		}catch(Exception e){
			//�v���p�e�B�ϊ��G���[
			log.error("BeanUtils�ϊ��G���[:Print32177000StatisticsEntity To Print32177000StatisticsFormBean");
			throw new TnaviException(e);
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_MALE)){
			// �j���̓��v�f�[�^���擾�ł��Ȃ������ꍇ�ɋ�f�[�^�i�S�ă[���j���Z�b�g
			map.put(CommonConstantsUseable.COD_CODE_SEX_MALE, new Print32177000StatisticsFormBean());
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_FEMALE)){
			// �����̓��v�f�[�^���擾�ł��Ȃ������ꍇ�ɋ�f�[�^�i�S�ă[���j���Z�b�g
			map.put(CommonConstantsUseable.COD_CODE_SEX_FEMALE, new Print32177000StatisticsFormBean());
		}

		// ���ʓ��v���Z�b�g
		printFormBean.setStatisticsMap(map);
	}

	public Print32177000FormBean getPrintFormBean() {
		return printFormBean;
	}

}
