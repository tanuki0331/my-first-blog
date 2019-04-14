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
 * 出席簿印刷(特別支援学級　小山市用) 印刷 データ取得 Service.
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

	/** 検索FormBean */
	private Search32177000FormBean searchFormBean;

	/** 印刷FormBean */
	private Print32177000FormBean printFormBean;



	public Print32177000Service(HttpServletRequest request, SystemInfoBean sessionBean, Search32177000FormBean searchFormBean, Print32177000FormBean printFormBean) {
		super(request, sessionBean, searchFormBean, printFormBean);

		searchFormBean.setOutputNameFlg(request.getParameter("outputNameFlg"));		// 氏名出力種別 1:通称　2:戸籍
		searchFormBean.setOutputKanaFlg(request.getParameter("outputKanaFlg"));		// かな出力種別 1:漢字　2:かな
		this.searchFormBean = searchFormBean;
		this.printFormBean = printFormBean;
		this.printFormBean.setSearchFormBean(searchFormBean);


		// 処理対象月をセット
		printFormBean.setTargetYearMonth(searchFormBean.getYyyymm());
		printFormBean.setGroupName(this.searchFormBean.getGroupname());


		// 特別支援なので交流学級は含まない
		searchFormBean.setExcFlg("2");
		super.exc_flg = "2";
	}

	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}

	@Override
	protected void doQuery() throws TnaviDbException {

		// 対象生徒情報を取得
		getTargetStudents();

		// 出欠情報を取得
		getAttendList();

		// 出欠集計を取得
		getAttendSummary();

		// 転編入情報を取得
		getEntryStuList();

		// 転退学情報を取得
		getLeaveStuList();

		// 月の日付情報を取得
		getHeaderDays();

		// 学級正担任氏名取得
		printFormBean.setTeacherNameMain(getTeacherName("1", 3));

		// 学級副担任氏名取得
		printFormBean.setTeacherNameSub(getTeacherName("2", 3));

		// 月別統計を取得
		getMonthEnforce();

		// 月別統計(前月末)を取得
		getMonthStatistics();

		// 出力学年毎の月情報を取得する
		for (Entry<String, AttPrintStudentFormBean> entry : printFormBean.getStudentMap().entrySet()) {
			AttPrintStudentFormBean studentFormBean = entry.getValue();

			String grade = studentFormBean.getCls_glade();
			if(!printFormBean.getDaysMap().containsKey(grade)){

				AttPrintDayFormBean[] days = getDayMap(grade);
				printFormBean.getDaysMap().put(grade, days);

			}
		}

		// 休業日基準学年と、各学年の授業日内容を比較して、内容の調整を行う
		checkDays();
	}

	/**
	 * 対象となる生徒のMapを作成する
	 */
	@Override
	protected void getTargetStudents(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		//履歴検索は5:通称、1:戸籍
		String sth_kind = ("1".equals(searchFormBean.getOutputNameFlg())? "5":"1");
		Object[] param = new Object[]{
				 sessionBean.getUserCode()		//所属コード
				,searchFormBean.getNendo()		//年度
				,getClsType()					//クラス種別
				,getGroupCode()					// [グループコード]
				,strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1")			//月初日
				,strLastDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth())	//月末日
				,sth_kind		//戸籍・通称種別
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
			//プロパティ変換エラー
			log.error("BeanUtils変換エラー:AttPrintStudentEntity To AttPrintStudentFormBean");
			throw new TnaviException(e);
		}

	}

	/**
	 * 出欠情報を取得
	 */
	@Override
	protected void getAttendList(){

		Object[] param = new Object[]{
				 sessionBean.getUserCode()		// [所属コード]
				,searchFormBean.getNendo()		// [処理中の年度]
				,getClsType()					// 検索する種別
				,getGroupCode()					// 検索する種別に合わせたグループコード
				,searchFormBean.getMonth()		// [対処月]
				,firstDayOfMonth				// [対象月の月初日]
				,lastDayOfMonth					// [対象月の月末日]
				,exc_flg
				,printFormBean.getAttendDisplayValue()
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000AttendWith.sql", param, AttPrintAttendEntity.class);
		qm.setPlusSQLFile("cus/oyama/get32177000AttPrintAttend.sql");

		@SuppressWarnings("unchecked")
		List<AttPrintAttendEntity> entityList = (List<AttPrintAttendEntity>) this.executeQuery(qm);

		// 出欠情報を生徒毎にセット
		for(AttPrintAttendEntity entity : entityList){

			if(!StringUtils.isEmpty(entity.getAtt_date())){
				Integer day = Integer.parseInt(entity.getAtt_date().substring(6, 8));
				printFormBean.getStudentMap().get(entity.getCls_stucode()).getAttend()[day - 1] = entity.getDisplay();	// 出欠記号
				printFormBean.getStudentMap().get(entity.getCls_stucode()).getStopKibikiFlg()[day - 1] = entity.getStopKibikiFlg();	// 出停忌引等扱いのフラグ
			}
		}
	}


	/**
	 * 出欠集計を取得
	 */
	@Override
	protected void getAttendSummary(){

		Object[] param = new Object[]{
				 sessionBean.getUserCode()		// [所属コード]
				,searchFormBean.getNendo()		// [処理中の年度]
				,getClsType()					// 検索する種別
				,getGroupCode()					// 検索する種別に合わせたグループコード
				,searchFormBean.getMonth()		// [対処月]
				,firstDayOfMonth				// [対象月の月初日]
				,lastDayOfMonth					// [対象月の月末日]
				,exc_flg
				,printFormBean.getAttendDisplayValue()
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000AttendWith.sql", param, AttPrintAttendSummaryEntity.class);
		qm.setPlusSQLFile("cus/oyama/getPrint32177000AttendSummary.sql");

		@SuppressWarnings("unchecked")
		List<AttPrintAttendSummaryEntity> entityList = (List<AttPrintAttendSummaryEntity>) this.executeQuery(qm);

		for(AttPrintAttendSummaryEntity entity : entityList){
			// 対象となる生徒に集計結果をセット
			printFormBean.getStudentMap().get(entity.getCls_stucode()).setAttendSummary(entity);
		}
	}


	/**
	 * 指定学年の月データを取得する。
	 * @param grade 学年
	 * @return 月データの配列
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
					// 休業日名称
					dayFormBean.getText().append(monthData.getHol_name());
				}
				if(!StringUtils.isEmpty(monthData.getClo_printword())){
					// 学級閉鎖印字文言
					dayFormBean.getText().append(monthData.getClo_printword());
					dayFormBean.setClo_printword(monthData.getClo_printword());
					// 学級閉鎖の場合、非授業日に設定する。
					dayFormBean.setEnfoce(0);
				}

				days[dayIndex] = dayFormBean;
			}else if(days[dayIndex].getText().toString().length() > 0){
				// 同一日付に複数の休業日が設定されている場合
				days[dayIndex].getText().append("　");

				if(!StringUtils.isEmpty(monthData.getHol_name())){
					// 休業日名称
					days[dayIndex].getText().append(monthData.getHol_name());
				}
				if(!StringUtils.isEmpty(monthData.getClo_printword())){
					// 学級閉鎖印字文言
					days[dayIndex].getText().append(monthData.getClo_printword());
					days[dayIndex].setClo_printword(monthData.getClo_printword());
					// 学級閉鎖の場合、非授業日に設定する。
					days[dayIndex].setEnfoce(0);
				}
			}

		}

		return days;
	}


	/**
	 * 特別支援学級担任氏名を取得
	 * @param kind 1:正担任、2:副担任
	 * @param numOfStaff 履歴の人数
	 * @return 学級担任氏名
	 */
	@Override
	protected String getTeacherName(String kind, int numOfStaff){

		Object[] param = new Object[]{
			    sessionBean.getUserCode()		// [所属コード]
			   ,searchFormBean.getNendo()		// [処理中の年度]
			   ,searchFormBean.getGroupcode()	// [処理中クラスのグループコード]
			   ,kind							// 1:正担任、2:副担任
			   ,firstDayOfMonth					// [対象月の月初日]
			   ,lastDayOfMonth					// [対象月の月末日]
			   ,numOfStaff						// [履歴の人数]
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
	 * 休業日基準学年と、各学年の授業日内容を比較して、内容の調整を行う
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
				// 各学年で授業日がある（混在する）場合は休業日名称をクリアする
				days[i].setText(new StringBuilder());
			}
		}
	}

	/**
	 * 月末統計を取得
	 */
	private void getMonthEnforce(){

		// 対象月の月初日
		String targetYearMonthFirstDay =strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1");
		// 対象月の月末日
		String targetYearMonthLastDay = strLastDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth());

		Object[] param = new Object[]{
				sessionBean.getUserCode()			// [所属コード]
				,searchFormBean.getNendo()			// [処理中の年度]
				,targetYearMonthFirstDay			// [対象月の月初日]
				,targetYearMonthLastDay				// [対象月の月末日]
				,getClsType()
				,getGroupCode()

		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000EnforceNum.sql", param, Print32177000EnforceEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32177000EnforceEntity> entityList = (List<Print32177000EnforceEntity>) this.executeQuery(qm);

		// 月別統計をセット
		printFormBean.setEnforceList(entityList);
	}

	/**
	 * 月末統計を取得
	 */
	private void getMonthStatistics(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// 対象月の月初日
		String targetYearMonthFirstDay =strDayOfMonth(sessionBean, searchFormBean.getNendo(), searchFormBean.getMonth(), "1");
		// 対象月の月末日
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
				sessionBean.getUserCode()		// [所属コード]
				,searchFormBean.getNendo()		// [処理中の年度]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,beforeYearMonthFirstDay		// [対象月の月初日]
				,beforeYearMonthLastDay			// [対象月の月末日]
				,getClsType()
				,getGroupCode()
		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000MonthStatistics.sql", param, Print32177000StatisticsEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32177000StatisticsEntity> entityList = (List<Print32177000StatisticsEntity>) this.executeQuery(qm);


		// 男女毎の月末統計を格納するMap
		Map<String, Print32177000StatisticsFormBean> map = new HashMap<String, Print32177000StatisticsFormBean>();
		try{
			for(Print32177000StatisticsEntity entity : entityList){
				Print32177000StatisticsFormBean statisticsFormBean = new Print32177000StatisticsFormBean();
				beanUtil.copyProperties(statisticsFormBean, entity);
				map.put(entity.getStu_sex(), statisticsFormBean);
			}
		}catch(Exception e){
			//プロパティ変換エラー
			log.error("BeanUtils変換エラー:Print32177000StatisticsEntity To Print32177000StatisticsFormBean");
			throw new TnaviException(e);
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_MALE)){
			// 男性の統計データが取得できなかった場合に空データ（全てゼロ）をセット
			map.put(CommonConstantsUseable.COD_CODE_SEX_MALE, new Print32177000StatisticsFormBean());
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_FEMALE)){
			// 女性の統計データが取得できなかった場合に空データ（全てゼロ）をセット
			map.put(CommonConstantsUseable.COD_CODE_SEX_FEMALE, new Print32177000StatisticsFormBean());
		}

		// 月別統計をセット
		printFormBean.setStatisticsMap(map);
	}

	public Print32177000FormBean getPrintFormBean() {
		return printFormBean;
	}

}
