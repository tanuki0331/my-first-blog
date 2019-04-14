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
 * 出席簿印刷(通常学級　小山市用) 印刷 データ取得 Service.
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

	/** 所属コード */
	private String user = "";

	/** 年度 */
	private String nendo = "";

	/** 年度開始日 */
	private String nendoStartDate = "";

	/** 年度終了日 */
	private String nendoEndDate = "";

	/** 検索FormBean */
	private Search32176000FormBean searchFormBean;

	/** 印刷FormBean */
	private Print32176000FormBean printFormBean;


	public void execute(HttpServletRequest request, SystemInfoBean sessionBean) {

		this.user   = sessionBean.getUserCode();
		this.nendo	= request.getParameter("nendo");
		this.nendoStartDate = this.nendo + sessionBean.getSystemNendoStartDate();

		if(sessionBean.getSystemNendoStartDate().compareTo(sessionBean.getSystemNendoEndDate()) > 0){
			// 年度終了日が開始日より若い場合年度を+1
			this.nendoEndDate = (Integer.parseInt(this.nendo) + 1) + sessionBean.getSystemNendoEndDate();
		}else{
			this.nendoEndDate = this.nendo + sessionBean.getSystemNendoEndDate();
		}

		searchFormBean = new Search32176000FormBean();
		printFormBean = new Print32176000FormBean();

		// 学校所属の区分をセット
		printFormBean.setUserKind(sessionBean.getUseKind2());

		// 処理対象月をセット
		printFormBean.setTargetYearMonth(request.getParameter("yyyymm"));

		// 出力対象月の日数をセット
		int monthDayCnt = Integer.parseInt(request.getParameter("monthDayCount"));
		printFormBean.setMonthDayCnt(monthDayCnt);

		// 出力対象日の情報をセット
		Print32176000DayFormBean[] days = new Print32176000DayFormBean[printFormBean.getMonthDayCnt()];
		for(int i=0; i<days.length; i++){
			days[i] = new Print32176000DayFormBean();
			days[i].setDay(i + 1);
			days[i].setEnfoce(1);	// 初期値は授業日とする
			days[i].setWeekDayColor(request.getParameter("weekday_color_" +  Integer.toString(i)));	// 曜日の色
			days[i].setText(new StringBuilder());
		}
		printFormBean.setDays(days);
		//ユーザーコードをセット
		printFormBean.setUserCode(this.user);

		// 検索条件を格納
		searchFormBean.setGrade(request.getParameter("grade"));						// 学年
		searchFormBean.setHmrclass(request.getParameter("hmrclass"));				// ホームルームクラスコード
		searchFormBean.setOutputNameFlg(request.getParameter("outputNameFlg"));		// 氏名出力種別 1:通称　2:戸籍
		searchFormBean.setOutputKanaFlg(request.getParameter("outputKanaFlg"));		// かな出力種別 1:漢字　2:かな
		printFormBean.setGrade(request.getParameter("grade"));						// 学年
		printFormBean.setHmrclass(request.getParameter("hmrclass"));				// ホームルームクラスコード


		super.execute();
	}


	@Override
	protected void doQuery() throws TnaviDbException {

		// ホームルーム番号を取得
		getClsno();

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

		// 出力優先順位は 非授業日 < 休業日 < 学級閉鎖日 のため、順次データを取得して、取得できた場合は上書きを行う
		// 非授業日を取得
		getNotEnforceday();

		// 休業日を取得
		getHoliday();

		// 学級閉鎖日を取得
		getClosingclass();

		// 学級担任氏名取得
		getTeacherName();
	}

	/**
	 * ホームルームからクラス番号を取得する
	 */
	private void getClsno(){

		Object[] param = new Object[]{
				 this.user						// [所属コード]
				,this.nendo						// [処理中の年度]
				,searchFormBean.getGrade()		// [処理中クラスの学年]
				,searchFormBean.getHmrclass()	// [処理中ホームルーム]
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
	 * 対象となる生徒のMapを作成する
	 */
	private void getTargetStudents(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// 対象月の最終日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		//履歴検索は5:通称、1:戸籍
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
				,targetYearMonthLastDay					// 月末時点で入学している児童生徒のみ
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Student.sql", param, Print32176000StudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000StudentEntity> entityList = (List<Print32176000StudentEntity>) this.executeQuery(qm);


		// 印刷時に順次出力するためLinkedHashMap
		Map<String, Print32176000StudentFormBean> map = new LinkedHashMap<String, Print32176000StudentFormBean>();
		try {
			for(Print32176000StudentEntity entity : entityList){

				Print32176000StudentFormBean studentFormBean = new Print32176000StudentFormBean();
				beanUtil.copyProperties(studentFormBean, entity);

				String stu_name = "";
				// 通称か戸籍かはDBから取得時に判定しているため、ここでは漢字・かなの判定のみ
				if("0".equals(searchFormBean.getOutputKanaFlg())){
					// 漢字
					stu_name = entity.getStu_name();
				}else if("1".equals(searchFormBean.getOutputKanaFlg())){
					// かな
					stu_name = entity.getStu_kana();
				}
				studentFormBean.setStu_name(stu_name);
				studentFormBean.setAttend(new String[printFormBean.getMonthDayCnt()]);
				studentFormBean.setStopKibikiFlg(new String[printFormBean.getMonthDayCnt()]);
				studentFormBean.setAttendSummary(new Print32176000AttendSummaryEntity());

				map.put(entity.getStu_stucode(), studentFormBean);
			}
		} catch (Exception e) {
			//プロパティ変換エラー
			log.error("BeanUtils変換エラー:Print32176000StudentEntity To Print32176000StudentFormBean");
			throw new TnaviException(e);
		}

		printFormBean.setStudentMap(map);

	}


	/**
	 * 出欠情報を取得
	 */
	private void getAttendList(){

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,this.user						// [所属コード]
				,this.nendo						// [処理中の年度]
				,searchFormBean.getGrade()		// [処理中クラスの学年]
				,searchFormBean.getClsno()		// [処理中クラスのクラス番号]
				,targetYearMonthLastDay			// [対象月の月末日]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Attend.sql", param, Print32176000AttendEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000AttendEntity> entityList = (List<Print32176000AttendEntity>) this.executeQuery(qm);

		// 出欠情報を生徒毎にセット
		for(Print32176000AttendEntity entity : entityList){

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
	private void getAttendSummary(){

		// 対象月
		String targetMonth = printFormBean.getTargetYearMonth().substring(4, 6);
		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						//[所属コード]
				,this.nendo						//[処理中の年度]
				,targetYearMonthFirstDay		//[対象月の月初日]
				,targetYearMonthLastDay			//[対象月の月末日]
				,targetYearMonthLastDay			//[対象月の月末日]
				,searchFormBean.getGrade()		//[処理中クラスの学年]
				,searchFormBean.getClsno()		//[処理中クラスのクラス番号]
				,this.user						//[所属コード]
				,this.nendo						//[処理中の年度]
				,targetMonth					//[対象月]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000AttendSummary.sql", param, Print32176000AttendSummaryEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000AttendSummaryEntity> entityList = (List<Print32176000AttendSummaryEntity>) this.executeQuery(qm);


		// 月別統計を取得
		Map<String, Print32176000StatisticsFormBean> statisticsMap = getMonthStatistics();

		for(Print32176000AttendSummaryEntity entity : entityList){
			// 対象となる生徒に集計結果をセット
			printFormBean.getStudentMap().get(entity.getCls_stucode()).setAttendSummary(entity);
		}

		// 月別統計をセット
		printFormBean.setStatisticsMap(statisticsMap);
	}

	/**
	 * 転編入者情報を取得
	 */
	private void getEntryStuList(){

		Map<String, Print32176000StudentFormBean> studentsMap = printFormBean.getStudentMap();

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
			     this.user						//[所属コード]
			    ,this.nendo						//[処理中の年度]
			    ,searchFormBean.getGrade()		//[処理中クラスの学年]
			    ,searchFormBean.getClsno()		//[処理中クラスのクラス番号]
			    ,targetYearMonthFirstDay		//[対象月の月初日]
			    ,targetYearMonthLastDay			//[対象月の月末日]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000EntryStuList.sql", param, Print32176000EntryStudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000EntryStudentEntity> entityList = (List<Print32176000EntryStudentEntity>) this.executeQuery(qm);

		List<Print32176000EntryLeaveFormBean> entryStuList = new ArrayList<Print32176000EntryLeaveFormBean>();
		for(Print32176000EntryStudentEntity entity : entityList){

			String stu_name = studentsMap.get(entity.getStu_stucode()).getStu_name();
			entryStuList.add(new Print32176000EntryLeaveFormBean(entity.getStu_stucode(), entity.getStu_entry(), stu_name, entity.getSt4_preschname(),entity.getStu_qualifi()));

			// 対象生徒の転入日をセット
			studentsMap.get(entity.getStu_stucode()).setEntry_start(entity.getStu_entry());
		}

		// 転編入者情報をセット
		printFormBean.setEntryStuList(entryStuList);
	}

	/**
	 * 転退学者情報を取得
	 */
	private void getLeaveStuList(){

		Map<String, Print32176000StudentFormBean> studentsMap = printFormBean.getStudentMap();

		Object[] param = new Object[]{
				 this.user						//[所属コード]
				,this.nendo						//[処理中の年度]
				,searchFormBean.getGrade()		//[処理中クラスの学年]
				,searchFormBean.getClsno()		//[処理中クラスのクラス番号]
				,this.nendoStartDate			//[年度開始日]	前月以前場合の処理があるため、範囲は年度開始日からとする
				,this.nendoEndDate				//[年度終了日]	次月以前場合の処理があるため、範囲は年度終了日までとする
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000LeaveStuList.sql", param, Print32176000LeaveStudentEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000LeaveStudentEntity> entityList = (List<Print32176000LeaveStudentEntity>) this.executeQuery(qm);


		List<Print32176000EntryLeaveFormBean> leaveStuList = new ArrayList<Print32176000EntryLeaveFormBean>();
		for(Print32176000LeaveStudentEntity entity : entityList){
			String stucode = entity.getReg_stucode();
			// 出力対象者に含まれていない場合は無視
			if (!studentsMap.containsKey(stucode)) {
				continue;
			}

			String stu_name = studentsMap.get(stucode).getStu_name();

			if(printFormBean.getTargetYearMonth().equals(entity.getLeave_date().substring(0, 6))){
				// 対象月の場合、リストに追加
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
	 * 非授業日を取得
	 */
	private void getNotEnforceday(){

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [所属コード]
				,searchFormBean.getGrade()		// [処理中クラスの学年]
				,this.nendo						// [処理中の年度]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
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
	 * 休業日を取得
	 */
	private void getHoliday(){

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [所属コード]
				,this.nendo						// [処理中の年度]
				,searchFormBean.getGrade()		// [処理中クラスの学年]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Holiday.sql", param, Print32176000NotEnfocedayEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000NotEnfocedayEntity> entityList = (List<Print32176000NotEnfocedayEntity>) this.executeQuery(qm);

		for(Print32176000NotEnfocedayEntity entity : entityList){

			int start_day = Integer.parseInt(entity.getStart_date().substring(6, 8));
			int end_day   = Integer.parseInt(entity.getEnd_date().substring(6, 8));

			String start_year_month = entity.getStart_date().substring(0, 6);
			String end_year_month = entity.getEnd_date().substring(0, 6);

			// 開始月と終了月が異なる場合の処理
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(start_year_month)){
				// 開始月が出力月と同じ場合は、終了日を月の末日で更新
				end_day = printFormBean.getMonthDayCnt();
			}
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(end_year_month)){
				// 終了月が出力月と同じ場合は、開始日を一日で更新
				start_day = 1;
			}

			for(int i=start_day; i<=end_day; i++){
				int arrayIndex = i - 1;
				// 授業日フラグを0:非授業日にセット
				printFormBean.getDays()[arrayIndex].setEnfoce(0);

				if(printFormBean.getDays()[arrayIndex].getText().length() > 0){
					// 既に休業日文言がある場合は空白を追加
					printFormBean.getDays()[arrayIndex].getText().append("　");
				}
				printFormBean.getDays()[arrayIndex].getText().append(entity.getText());
			}
		}
	}


	/**
	 * 学級閉鎖日を取得
	 */
	private void getClosingclass(){

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
		String targetYearMonthLastDay = printFormBean.getTargetYearMonth() + printFormBean.getMonthDayCnt().toString();

		Object[] param = new Object[]{
				 this.user						// [所属コード]
				,this.nendo						// [処理中の年度]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,searchFormBean.getGrade()		// [処理中クラスの学年]
				,searchFormBean.getClsno()		// [処理中クラスのクラス番号]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000Closingclass.sql", param, Print32176000NotEnfocedayEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000NotEnfocedayEntity> entityList = (List<Print32176000NotEnfocedayEntity>) this.executeQuery(qm);

		for(Print32176000NotEnfocedayEntity entity : entityList){

			int start_day = Integer.parseInt(entity.getStart_date().substring(6, 8));
			int end_day   = Integer.parseInt(entity.getEnd_date().substring(6, 8));

			String start_year_month = entity.getStart_date().substring(0, 6);
			String end_year_month = entity.getEnd_date().substring(0, 6);

			// 開始月と終了月が異なる場合の処理
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(start_year_month)){
				// 開始月が出力月と同じ場合は、終了日を月の末日で更新
				end_day = printFormBean.getMonthDayCnt();
			}
			if(!start_year_month.equals(end_year_month) && printFormBean.getTargetYearMonth().equals(end_year_month)){
				// 終了月が出力月と同じ場合は、開始日を一日で更新
				start_day = 1;
			}

			for(int i=start_day; i<=end_day; i++){
				int arrayIndex = i - 1;
				// 授業日フラグを0:非授業日にセット
				printFormBean.getDays()[arrayIndex].setEnfoce(0);

				if(printFormBean.getDays()[arrayIndex].getText().length() > 0){
					// 既に休業日文言がある場合は空白を追加
					printFormBean.getDays()[arrayIndex].getText().append("　");
				}
				printFormBean.getDays()[arrayIndex].getText().append(entity.getText());
				printFormBean.getDays()[arrayIndex].setClosingclass(true);
			}

		}
	}

	/**
	 * 学級担任氏名を取得
	 */
	private void getTeacherName(){

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";

		Object[] param = new Object[]{
			    this.user						// [所属コード]
			   ,this.nendo						// [処理中の年度]
			   ,searchFormBean.getClsno()		// [処理中クラスのクラス番号]
			   ,targetYearMonthFirstDay			// [対象月の月初日]
			   ,this.user						// [所属コード]
			   ,this.nendo						// [処理中の年度]
			   ,searchFormBean.getClsno()		// [処理中クラスのクラス番号]
			   ,this.user						// [所属コード]
				} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000TeacherName.sql", param, String.class);
		String teacherName = (String) this.executeQuery(qm);
		printFormBean.setTeacherName(teacherName);
	}

	/**
	 * 月末統計を取得
	 */
	private Map<String, Print32176000StatisticsFormBean> getMonthStatistics(){

		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();

		// 対象月の月初日
		String targetYearMonthFirstDay = printFormBean.getTargetYearMonth() + "01";
		// 対象月の月末日
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
				this.user						// [所属コード]
				,this.nendo						// [処理中の年度]
				,searchFormBean.getClsno()		// [処理中クラスのクラス番号]
				,targetYearMonthFirstDay		// [対象月の月初日]
				,targetYearMonthLastDay			// [対象月の月末日]
				,beforeYearMonthFirstDay		// [対象月の月初日]
				,beforeYearMonthLastDay			// [対象月の月末日]

		};

		QueryManager qm = new QueryManager("cus/oyama/getPrint32176000MonthStatistics.sql", param, Print32176000StatisticsEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32176000StatisticsEntity> entityList = (List<Print32176000StatisticsEntity>) this.executeQuery(qm);


		// 男女毎の月末統計を格納するMap
		Map<String, Print32176000StatisticsFormBean> map = new HashMap<String, Print32176000StatisticsFormBean>();
		try{
			for(Print32176000StatisticsEntity entity : entityList){
				Print32176000StatisticsFormBean statisticsFormBean = new Print32176000StatisticsFormBean();
				beanUtil.copyProperties(statisticsFormBean, entity);
				map.put(entity.getStu_sex(), statisticsFormBean);
			}
		}catch(Exception e){
			//プロパティ変換エラー
			log.error("BeanUtils変換エラー:Print32176000StatisticsEntity To Print32176000StatisticsFormBean");
			throw new TnaviException(e);
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_MALE)){
			// 男性の統計データが取得できなかった場合に空データ（全てゼロ）をセット
			map.put(CommonConstantsUseable.COD_CODE_SEX_MALE, new Print32176000StatisticsFormBean());
		}

		if(!map.containsKey(CommonConstantsUseable.COD_CODE_SEX_FEMALE)){
			// 女性の統計データが取得できなかった場合に空データ（全てゼロ）をセット
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
