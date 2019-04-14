package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.CodeEntity;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_EnforcedayEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_StaffEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32172000FormBean;

/**
 * <PRE>
 * �o���Ȉꗗ�o�� ��� Service.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY aivick <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32172000Service  extends AbstractExecuteQuery {

	/** Log4j */
	private static final Log log = LogFactory.getLog(Print32172000Service.class);

	/** ���FormBean�@*/
	private Print32172000FormBean printFormBean;

	private SystemInfoBean sessionBean;

	/**
	 * <pre>
	 * Request�̓��e��FormBean���쐬����
	 * ���̏�����Action.doAction���\�b�h���ŌĂяo�����
	 * </pre>
	 *
	 * @param request
	 *            HTTP���N�G�X�g
	 * @param sessionBean
	 *            �V�X�e�����Bean(�Z�b�V�������)
	 * @throws TnaviDbException
	 *             DB��O�������ɃX���[�����
	 */
	public void execute( Print32172000FormBean printFormBean, SystemInfoBean sessionBean)
			throws TnaviDbException {

		// FormBean��ݒ�
		this.printFormBean = printFormBean;

		this.sessionBean = sessionBean;
		// �N�G�������s����
		super.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doQuery() throws TnaviDbException {

		try {

			String user       = sessionBean.getUserCode();
			String nendo      = printFormBean.getNendo();
			String grade      = printFormBean.getGrade();
			String hmrclsno   = printFormBean.getHmrClsno();


			Object[] param = null;
			QueryManager queryManager = null;

			// ------------------------------------------------------------------------------------------
			// �ΏۃN���X�擾
			// ------------------------------------------------------------------------------------------
			List<HRoomEntity>  hRoomEntityList = new ArrayList<HRoomEntity>();
			if( hmrclsno == null || hmrclsno.equals("")){

				param = new Object[]{ user, nendo, grade };
				queryManager = new QueryManager("cus/oyama/getData32172000_classList.sql", param, HRoomEntity.class);
				String plussql = " order by hmr_order, hmr_class ";
				queryManager.setPlusSQL(plussql);

				hRoomEntityList = (List<HRoomEntity>) this.executeQuery(queryManager);
			}else{
				param = new Object[]{ user, nendo, grade };
				queryManager = new QueryManager("cus/oyama/getData32172000_classList.sql", param, HRoomEntity.class);
				String plussql = " and hmr_clsno ='"+ hmrclsno +"'" ;
				plussql += " order by hmr_order, hmr_class ";
				queryManager.setPlusSQL(plussql);

				hRoomEntityList = (List<HRoomEntity>) this.executeQuery(queryManager);
			}

			String clsno_list ="";
			for ( HRoomEntity entity :hRoomEntityList ){
				if(clsno_list.equals("")){
					clsno_list = "'"+ entity.getHmr_clsno()+ "'";
				}else{
					clsno_list += ",'"+ entity.getHmr_clsno()+ "'";
				}
			}
			printFormBean.setHRoomEntityList(hRoomEntityList);

			// ------------------------------------------------------------------------------------------
			// �o�͑ΏۃN���X�̒S�C�����擾����@�@
			// ------------------------------------------------------------------------------------------

			// �w���I�����̒S�C���o�͂���
			String output_date   = printFormBean.getSemEnd();

			param = new Object[]{user ,nendo , output_date,  printFormBean.getHmrClsno()};
			queryManager = new QueryManager("cus/oyama/getData32172000_staff.sql", param, Search32172000_StaffEntity.class);

			String plussql = " AND hmr_clsno IN ("+ clsno_list + ") ";
			queryManager.setPlusSQL(plussql);

			List<Search32172000_StaffEntity> staffEntityList = (List<Search32172000_StaffEntity>)this.executeQuery(queryManager);

			HashMap<String, Search32172000_StaffEntity> staffEntityMap = new HashMap<String, Search32172000_StaffEntity>();

			if( !staffEntityList.isEmpty()){
				for(Search32172000_StaffEntity staffEntity :staffEntityList){
					staffEntityMap.put(staffEntity.getHmr_clsno(), staffEntity );
				}
			}
			printFormBean.setStaffEntityMap(staffEntityMap);

			// ------------------------------------------------------------------------------------------
			// ���ԑS�̂̏o���ꗗ�W�v�l���擾����
			// ------------------------------------------------------------------------------------------
			String sem_start_date = printFormBean.getSemStart();
			String sem_end_date   = printFormBean.getSemEnd();

			param = new Object[]{user ,nendo , sem_start_date, sem_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_semesterAttendList.sql", param, Print32172000_AttendEntity.class);

			plussql  = " AND cls_group IN ("+ clsno_list +") ";
			plussql += " GROUP BY grade, hmr_class ,cls_group, cls_reference_number, stucode, student_name ";
			plussql += " ORDER BY hmr_class, cls_reference_number, stucode ";
			queryManager.setPlusSQL(plussql);

			List<Print32172000_AttendEntity> semesterAttendEntityList = (List<Print32172000_AttendEntity>)this.executeQuery(queryManager);

			LinkedHashMap<String, Print32172000_AttendEntity> semesterAttendEntityMap = new LinkedHashMap<String, Print32172000_AttendEntity>();

			if( !semesterAttendEntityList.isEmpty()){
				for(Print32172000_AttendEntity print32172000_AttendEntity :semesterAttendEntityList){
					String key = print32172000_AttendEntity.getCls_group()+"_"+print32172000_AttendEntity.getStucode() ;
					semesterAttendEntityMap.put(key, print32172000_AttendEntity );
				}
			}
			printFormBean.setSemesterAttendEntityList(semesterAttendEntityList);
			printFormBean.setSemesterAttendEntityMap(semesterAttendEntityMap);

			// ------------------------------------------------------------------------------------------
			// ���Ԃ̌��ʏo���ꗗ���擾����
			// ------------------------------------------------------------------------------------------
			param = new Object[]{user ,nendo , sem_start_date, sem_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_attendList.sql", param, Print32172000_AttendEntity.class);

			plussql  = " AND cls_group IN ("+ clsno_list +") ";
			plussql += " GROUP BY grade, hmr_class ,cls_group, cls_reference_number, stucode, student_name, SUBSTRING(enf_date,1,4), SUBSTRING(enf_date, 5, 2) ";
			plussql += " ORDER BY hmr_class, cls_reference_number, stucode ";
			queryManager.setPlusSQL(plussql);

			List<Print32172000_AttendEntity> attendEntityList = (List<Print32172000_AttendEntity>)this.executeQuery(queryManager);

			LinkedHashMap<String, Print32172000_AttendEntity> attendEntityMap = new LinkedHashMap<String, Print32172000_AttendEntity>();

			if( !attendEntityList.isEmpty()){
				for(Print32172000_AttendEntity print32172000_AttendEntity :attendEntityList){
					String key = print32172000_AttendEntity.getCls_group()+"_"+print32172000_AttendEntity.getEnf_month() +"_"+print32172000_AttendEntity.getStucode() ;
					attendEntityMap.put(key, print32172000_AttendEntity );
				}
			}
			printFormBean.setAttendEntityList(attendEntityList);
			printFormBean.setAttendEntityMap(attendEntityMap);

			// ------------------------------------------------------------------------------------------
			// �N�Ԃ̏o���ꗗ�W�v�l���擾����
			// ------------------------------------------------------------------------------------------

			String nendo_start_date = nendo + sessionBean.getSystemNendoStartDate().trim();

			// �N�x�I�������J�n�����Ⴂ�ꍇ�N�x��+1
			String nendo_end_date   = "";
			if(sessionBean.getSystemNendoStartDate().compareTo(sessionBean.getSystemNendoEndDate()) > 0){
				nendo_end_date = (Integer.parseInt(nendo) + 1) + sessionBean.getSystemNendoEndDate().trim();
			}else{
				nendo_end_date = nendo + sessionBean.getSystemNendoEndDate().trim();
			}

			param = new Object[]{user ,nendo , nendo_start_date, nendo_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_yearAttendList.sql", param, Print32172000_AttendEntity.class);

			plussql  = " AND cls_group IN ("+ clsno_list +") ";
			plussql += " GROUP BY grade, hmr_class ,cls_group, cls_reference_number, stucode, student_name  ";
			plussql += " ORDER BY hmr_class, cls_reference_number, stucode ";
			queryManager.setPlusSQL(plussql);

			List<Print32172000_AttendEntity> yearAttendEntityList = (List<Print32172000_AttendEntity>)this.executeQuery(queryManager);

			LinkedHashMap<String, Print32172000_AttendEntity> yearAttendEntityMap = new LinkedHashMap<String, Print32172000_AttendEntity>();

			if( !yearAttendEntityList.isEmpty()){
				for(Print32172000_AttendEntity print32172000_AttendEntity :yearAttendEntityList){
					String key = print32172000_AttendEntity.getCls_group()+"_"+print32172000_AttendEntity.getStucode() ;
					yearAttendEntityMap.put(key, print32172000_AttendEntity );
				}
			}
			printFormBean.setYearAttendEntityList(yearAttendEntityList);
			printFormBean.setYearAttendEntityMap(yearAttendEntityMap);

			// ------------------------------------------------------------------------------------------
			// �e���̎��Ɠ������擾����
			// ------------------------------------------------------------------------------------------
			LinkedHashMap<String, Print32172000_EnforcedayEntity> enforceDayTotalMap = new LinkedHashMap<String, Print32172000_EnforcedayEntity>();

			param = new Object[]{user, nendo, grade, sem_start_date, sem_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_enforcedayByMonth.sql", param, Print32172000_EnforcedayEntity.class);

			List<Print32172000_EnforcedayEntity> enforceDayEntityList = (List<Print32172000_EnforcedayEntity>)this.executeQuery(queryManager);
			if( !enforceDayEntityList.isEmpty()){
				for(Print32172000_EnforcedayEntity enforceDayEntity :enforceDayEntityList){
					String key = enforceDayEntity.getEnf_month() ;
					enforceDayTotalMap.put(key, enforceDayEntity );
				}
			}

			// ------------------------------------------------------------------------------------------
			// �w���̎��Ɠ������擾����
			// ------------------------------------------------------------------------------------------
			param = new Object[]{user, nendo, grade, sem_start_date, sem_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_enforcedayByTerm.sql", param, Print32172000_EnforcedayEntity.class);

			List<Print32172000_EnforcedayEntity> semEnforceDayEntityList = (List<Print32172000_EnforcedayEntity>)this.executeQuery(queryManager);
			if( !semEnforceDayEntityList.isEmpty()){
				for(Print32172000_EnforcedayEntity enforceDayEntity :semEnforceDayEntityList){
					enforceDayTotalMap.put("SEM", enforceDayEntity );
				}
			}

			// ------------------------------------------------------------------------------------------
			// �N�Ԏ��Ɠ������擾����
			// ------------------------------------------------------------------------------------------
			param = new Object[]{user, nendo, grade, nendo_start_date, nendo_end_date };
			queryManager = new QueryManager("cus/oyama/getData32172000_enforcedayByTerm.sql", param, Print32172000_EnforcedayEntity.class);

			List<Print32172000_EnforcedayEntity> yearEnforceDayEntityList = (List<Print32172000_EnforcedayEntity>)this.executeQuery(queryManager);
			if( !yearEnforceDayEntityList.isEmpty()){
				for(Print32172000_EnforcedayEntity enforceDayEntity :yearEnforceDayEntityList){
					enforceDayTotalMap.put("YEAR", enforceDayEntity );
				}
			}
			printFormBean.setEnforceDayTotalMap(enforceDayTotalMap);

			// ------------------------------------------------------------------------------------------
			// �����A�N�x���擾����
			// ------------------------------------------------------------------------------------------
			String specified_output_date = printFormBean.getOutputYear() + printFormBean.getOutputMonth() + printFormBean.getOutputDay();
			int int_specified_output_date   = Integer.parseInt(specified_output_date);

			Object[] nengo_param = { user, 90 };
			QueryManager codeQuery = new  QueryManager("common/getCodeByUserAndKind.sql", nengo_param, CodeEntity.class);

			List<CodeEntity> codeEntityList = (List<CodeEntity>)this.executeQuery(codeQuery);

			String gengo_name         = "" ;
			String gengo_start_date   = "" ;
			String gengo_end_date     = "" ;

			int wareki_nendo = 0 ;

			// �w�肵���N�����̔N�x�J�n���A�N�x�I�������擾����
			String nendoStartYYYYMMDD = printFormBean.getNendo() + sessionBean.getSystemNendoStartDate().trim();

			int int_startMMDD = Integer.parseInt( sessionBean.getSystemNendoStartDate().trim());
			int int_endMMDD   = Integer.parseInt( sessionBean.getSystemNendoEndDate().trim());

			String nendoEndYYYYMMDD   = "";

			if( int_startMMDD > int_endMMDD ){
				nendoEndYYYYMMDD = String.valueOf(Integer.parseInt( printFormBean.getNendo() ) + 1 ) +  sessionBean.getSystemNendoEndDate().trim();
			}else{
				nendoEndYYYYMMDD = sessionBean.getSystemNendoEndDate().trim();
			}

			// �N�x�J�n���@�I�������擾����B
			int int_nendo_start_date = Integer.parseInt(nendoStartYYYYMMDD);
			int int_nendo_end_date   = Integer.parseInt(nendoEndYYYYMMDD);

			for( CodeEntity codeEntity : codeEntityList ){

				int int_nendo_start_year = Integer.parseInt(nendo); // �Ώ۔N�x

				gengo_start_date = codeEntity.getCod_start(); // �����J�n��
				gengo_end_date   = codeEntity.getCod_end();   // �����I����

				int int_gengo_start_date = Integer.parseInt(gengo_start_date);
				int int_gengo_end_date   = Integer.parseInt(gengo_end_date);

				// �J�n���A�I�����͈̔͂ɏo�͓�����v����B
				if( int_gengo_start_date <= int_specified_output_date && int_specified_output_date <= int_gengo_end_date ) {

					// ������ݒ肷��B
					gengo_name = codeEntity.getCod_name1();

					// �����J�n��,�I�����̔N�A�������擾����B
					String temp_gengo_start_YYYY = codeEntity.getCod_start().substring(0,4);
					String temp_gengo_start_MMDD = codeEntity.getCod_start().substring(5,8);

					// �����J�n���@�I����
					int int_gengo_start_YYYY     = Integer.parseInt(temp_gengo_start_YYYY);
					int int_gengo_start_MMDD     = Integer.parseInt(temp_gengo_start_MMDD);

					boolean gengo_start_same_nendo = false;
					if( int_startMMDD <= int_gengo_start_MMDD && int_gengo_start_MMDD <=1231 ){
						gengo_start_same_nendo = true;
					}else{
						gengo_start_same_nendo = false;
					}

					// �����̊J�n�����̔N�x�͈͓̔��ɂ���΁A�������N�x�Ƃ��Ď�舵��
					if( int_nendo_start_date <= int_gengo_start_date && int_gengo_start_date <= int_nendo_end_date ){

						// �w������A�N�x�I�����@�ȓ��ɂ���ꍇ(��΂ɂ���͂�)
						if( int_specified_output_date <= int_nendo_end_date ){
							// �����N�ƌ����N�x������
							if( gengo_start_same_nendo == true) {
								wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY + 1 ;
							}else{
								int_gengo_start_YYYY -- ;
								wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY + 1 ;
							}
						}
					}
					// �����̊J�n�����������N�x�͈̔͊O
					else{
						// �����N�ƌ����N�x������
						if( gengo_start_same_nendo == true ){
							wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY + 1 ;
						}else{
							int_gengo_start_YYYY -- ;
							wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY  ;
						}
					}

					break;
				}

			}

			String output_title_nendo = "";
			if( wareki_nendo == 1 ) {
				output_title_nendo = "��";
			}else{
				output_title_nendo = String.valueOf(wareki_nendo);
			}

			printFormBean.setOutput_title_nendo( output_title_nendo );
			printFormBean.setOutput_title_gengo( gengo_name );

		} catch (Exception e) {
			log.error("�o���Ȉꗗ�o�� DB�擾�����Ɏ��s���܂����B", e);
			throw new TnaviException(e);
		}
	}

	public Print32172000FormBean getPrintFormBean() {
		return printFormBean;
	}

	public void setPrintFormBean(Print32172000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

	public SystemInfoBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SystemInfoBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
