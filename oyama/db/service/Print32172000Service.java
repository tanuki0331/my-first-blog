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
 * 出欠席一覧出力 印刷 Service.
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

	/** 印刷FormBean　*/
	private Print32172000FormBean printFormBean;

	private SystemInfoBean sessionBean;

	/**
	 * <pre>
	 * Requestの内容でFormBeanを作成する
	 * この処理はAction.doActionメソッド内で呼び出される
	 * </pre>
	 *
	 * @param request
	 *            HTTPリクエスト
	 * @param sessionBean
	 *            システム情報Bean(セッション情報)
	 * @throws TnaviDbException
	 *             DB例外発生時にスローされる
	 */
	public void execute( Print32172000FormBean printFormBean, SystemInfoBean sessionBean)
			throws TnaviDbException {

		// FormBeanを設定
		this.printFormBean = printFormBean;

		this.sessionBean = sessionBean;
		// クエリを実行する
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
			// 対象クラス取得
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
			// 出力対象クラスの担任名を取得する　　
			// ------------------------------------------------------------------------------------------

			// 学期終了日の担任を出力する
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
			// 期間全体の出欠一覧集計値を取得する
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
			// 期間の月別出欠一覧を取得する
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
			// 年間の出欠一覧集計値を取得する
			// ------------------------------------------------------------------------------------------

			String nendo_start_date = nendo + sessionBean.getSystemNendoStartDate().trim();

			// 年度終了日が開始日より若い場合年度を+1
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
			// 各月の授業日数を取得する
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
			// 学期の授業日数を取得する
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
			// 年間授業日数を取得する
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
			// 元号、年度を取得する
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

			// 指定した年月日の年度開始日、年度終了日を取得する
			String nendoStartYYYYMMDD = printFormBean.getNendo() + sessionBean.getSystemNendoStartDate().trim();

			int int_startMMDD = Integer.parseInt( sessionBean.getSystemNendoStartDate().trim());
			int int_endMMDD   = Integer.parseInt( sessionBean.getSystemNendoEndDate().trim());

			String nendoEndYYYYMMDD   = "";

			if( int_startMMDD > int_endMMDD ){
				nendoEndYYYYMMDD = String.valueOf(Integer.parseInt( printFormBean.getNendo() ) + 1 ) +  sessionBean.getSystemNendoEndDate().trim();
			}else{
				nendoEndYYYYMMDD = sessionBean.getSystemNendoEndDate().trim();
			}

			// 年度開始日　終了日を取得する。
			int int_nendo_start_date = Integer.parseInt(nendoStartYYYYMMDD);
			int int_nendo_end_date   = Integer.parseInt(nendoEndYYYYMMDD);

			for( CodeEntity codeEntity : codeEntityList ){

				int int_nendo_start_year = Integer.parseInt(nendo); // 対象年度

				gengo_start_date = codeEntity.getCod_start(); // 元号開始日
				gengo_end_date   = codeEntity.getCod_end();   // 元号終了日

				int int_gengo_start_date = Integer.parseInt(gengo_start_date);
				int int_gengo_end_date   = Integer.parseInt(gengo_end_date);

				// 開始日、終了日の範囲に出力日が一致する。
				if( int_gengo_start_date <= int_specified_output_date && int_specified_output_date <= int_gengo_end_date ) {

					// 元号を設定する。
					gengo_name = codeEntity.getCod_name1();

					// 元号開始日,終了日の年、月日を取得する。
					String temp_gengo_start_YYYY = codeEntity.getCod_start().substring(0,4);
					String temp_gengo_start_MMDD = codeEntity.getCod_start().substring(5,8);

					// 元号開始日　終了日
					int int_gengo_start_YYYY     = Integer.parseInt(temp_gengo_start_YYYY);
					int int_gengo_start_MMDD     = Integer.parseInt(temp_gengo_start_MMDD);

					boolean gengo_start_same_nendo = false;
					if( int_startMMDD <= int_gengo_start_MMDD && int_gengo_start_MMDD <=1231 ){
						gengo_start_same_nendo = true;
					}else{
						gengo_start_same_nendo = false;
					}

					// 元号の開始日がの年度の範囲内にあれば、元号初年度として取り扱い
					if( int_nendo_start_date <= int_gengo_start_date && int_gengo_start_date <= int_nendo_end_date ){

						// 指定日が、年度終了日　以内にある場合(絶対にあるはず)
						if( int_specified_output_date <= int_nendo_end_date ){
							// 元号年と元号年度が同一
							if( gengo_start_same_nendo == true) {
								wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY + 1 ;
							}else{
								int_gengo_start_YYYY -- ;
								wareki_nendo = int_nendo_start_year - int_gengo_start_YYYY + 1 ;
							}
						}
					}
					// 元号の開始日が元号初年度の範囲外
					else{
						// 元号年と元号年度が同一
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
				output_title_nendo = "元";
			}else{
				output_title_nendo = String.valueOf(wareki_nendo);
			}

			printFormBean.setOutput_title_nendo( output_title_nendo );
			printFormBean.setOutput_title_gengo( gengo_name );

		} catch (Exception e) {
			log.error("出欠席一覧出力 DB取得処理に失敗しました。", e);
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
