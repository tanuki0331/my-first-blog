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
 * 成績通知表印刷(小山市_小学校) 印刷  Service.
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

	/** 実行SQL */
	private static final String EXEC_SQL_SCHOOL_NAME = "cus/oyama/getPrint32179000Schoolname.sql";					// 学校名
	private static final String EXEC_SQL_PRINCIPAL_HISTORY = "cus/oyama/getPrint32179000PrincipalHistory.sql";		// 校長名
	private static final String EXEC_SQL_TEACHER_NAME = "cus/oyama/getPrint32179000StaffHistory.sql";				// 担任名
	private static final String EXEC_SQL_TEACHER_NAME2 = "cus/oyama/getPrint32179000HroomStaff.sql";				// 担任名
	private static final String EXEC_SQL_SP_TEACHER_NAME = "cus/oyama/getPrint32179000SpStaffHistory.sql";			// 特別支援学級担任名
	private static final String EXEC_SQL_SP_TEACHER_NAME2 = "cus/oyama/getPrint32179000SpHroomStaff.sql";			// 特別支援学級担任名
	private static final String EXEC_SQL_SCHOOLSTAMP = "sys/getSchoolStamp.sql";									// 校章
	private static final String EXEC_SQL_STUDENT = "cus/oyama/getPrint32179000Student.sql";							// 対象児童
	private static final String EXEC_SQL_ATTEND = "cus/oyama/getPrint32179000Attend.sql";							// 出欠の記録
	private static final String EXEC_SQL_ACT = "cus/oyama/getPrint32179000Act.sql";									// 行動の記録
	private static final String EXEC_SQL_SCORE = "cus/oyama/getPrint32179000Score.sql";								// 学習の記録
	private static final String EXEC_SQL_FOREIGN_LANGUAGE = "cus/oyama/getPrint32179000ForeignLangAct.sql";			// 外国語活動
	private static final String EXEC_SQL_COMMENT = "cus/oyama/getPrint32179000Comment.sql";							// 総合所見
	private static final String EXEC_SQL_TOTAL_ACT = "cus/oyama/getPrint32179000TotalAct.sql";						// 総合的な学習の記録
	private static final String EXEC_SQL_SEPCIAL_ACTIVITY = "cus/oyama/getPrint32179000SpAct.sql";					// 特別活動の記録
	private static final String EXEC_SQL_HEALTHRECORD_INFO = "cus/oyama/getPrint32179000Healthrecord.sql";			// 健康の記録
	private static final String EXEC_SQL_SPGROUP_NAME = "cus/oyama/getPrint32179000SpGroupName.sql";				// 特別支援学級グループ名称

	private static final String CHECK_ON = "1"; // チェックボックス:チェック有り

	/** 取得イメージ */
	private static final String STM_KIND_SCHOOL = "01";				// 校章
	private static final String STM_KIND_TITLE = "02";				// 通知票表題
	private static final String STM_KIND_EDUCATIONAL_GOAL = "05";	// 学校教育目標

	/** 共通SessionBean */
	private SystemInfoBean sessionBean;

	/** ホームルーム情報FormBean. */
	private HroomKeyFormBean hroomKeyFormBean;

	/** 特別支援学級クラス情報FormBean. */
	private SpClassKeyFormBean spClassKeyFormBean;

	/** 選択された期間 */
	private String term;

	/** 選択生徒の学籍番号 */
	private List<String> selectedStucodeList;

	/** 選択された生徒情報 */
	Map<String, Print32179000StudentFormBean> selectStudentMap;
	Map<String, Print32179000StudentFormBean> studentMap;
	Map<String, Print32179000StudentFormBean> deedStudentMap;

	/** 印刷FormBean */
	private Print32179000FormBean printFormBean;




	/** 取得イメージ ★不要 */
	private static final String STAMP_IMAGE_TITLE = "02"; // 通知票表題
	private static final String STAMP_PRINCIPAL = "03"; // 校長印
	private static final String STAMP_COVER_IMAGE = "04"; // 表紙画像

	/**
	 * <pre>
	 * Requestの内容でFormBeanを作成する
	 * この処理はAction.doActionメソッド内で呼び出される
	 * </pre>
	 *
	 * @param request HTTPリクエスト
	 * @param sessionBean システム情報Bean(セッション情報)
	 * @throws TnaviDbException DB例外発生時にスローされる
	 */
	public void execute(HttpServletRequest request, SystemInfoBean sessionBean) throws TnaviDbException {

		// 共通Sessionを設定
		this.sessionBean = sessionBean;

		// -----------------------------------------------
		// 印刷FormBean
		// -----------------------------------------------
		printFormBean = new Print32179000FormBean();

		// 所属コード
		printFormBean.setUserCode(sessionBean.getUserCode());
		// 年度
		printFormBean.setNendo(sessionBean.getSystemNendoSeireki());

		// ホームルーム情報FormBeanを設定
		hroomKeyFormBean = sessionBean.getSelectHroomKey();
		// 特別支援学級情報FormBeanを設定
		spClassKeyFormBean = sessionBean.getSelectSpClassKey();

		// リクエストパラメータの取得
		// 期間情報を取得、設定
		term = request.getParameter("goptCode");
		printFormBean.setTerm(term);
		printFormBean.setTermName(request.getParameter("termName"));

		// 出力ページを取得、設定
		printFormBean.setOutput_cover(CHECK_ON.equals(request.getParameter("output_cover")));		// 表紙
		printFormBean.setOutput_purpose(CHECK_ON.equals(request.getParameter("output_purpose")));	// めあて(すがた)
		printFormBean.setOutput_contents(CHECK_ON.equals(request.getParameter("output_contents")));	// 内容
		printFormBean.setOutput_deed(CHECK_ON.equals(request.getParameter("output_deed")));			// 修了証

		// 出力日付を取得、設定
		printFormBean.setOutputDate(request.getParameter("output_year") + request.getParameter("output_month") + request.getParameter("output_day"));

		// 修了日付を取得、設定
		printFormBean.setDeedDate(request.getParameter("deed_year") + request.getParameter("deed_month") + request.getParameter("deed_day"));

		// 生徒情報を取得、設定
		this.selectedStucodeList = new ArrayList<String>();
		if (request.getParameter("stucode") != null) {
			String[] selectedStucodes = request.getParameter("stucode").split(",");
			getParamStucode(selectedStucodes, request);
		}

		// 特別学級情報フラグ
		if (spClassKeyFormBean != null) {
			printFormBean.setSpgdFlg(true);
		}






		// 出力ページを取得、設定 ★不要
		printFormBean.setOutput_cover(CHECK_ON.equals(request.getParameter("output_cover"))); // 表紙
		printFormBean.setOutput_score(CHECK_ON.equals(request.getParameter("output_score"))); // 学習の記録
		printFormBean.setOutput_deed(CHECK_ON.equals(request.getParameter("output_deed"))); // 修了証

		// 修了日付を取得、設定 ★不要
		printFormBean.setDeedDate(sessionBean.getEntryNendoSeireki() + "0331");

		// 組・出席番号の出力モード取得
		String spHroomOutput = request.getParameter("spHroomOutput");
		if (spHroomOutput == null) {
			// 通常通り、在籍学級の組・出席番号を出力
			spHroomOutput = Print32179000FormBean.OUTPUT_ALTERNAT_PRINT;
		}
		printFormBean.setAlternatFlg(spHroomOutput);


		// クエリを実行する
		super.execute();
	}

	/**
	 * 生徒情報を設定する
	 * 
	 * @param selectedStucodes 選択された学籍番号
	 * @param request HTTPリクエスト
	 */
	private void getParamStucode(String[] selectedStucodes, HttpServletRequest request) {
		// 生徒数
		String stuCnt = request.getParameter("stuCnt");

		this.selectStudentMap = new HashMap<String, Print32179000StudentFormBean>();

		selectedStucodeList = new ArrayList<String>();
		// 選択された生徒情報
		for (int i = 0; i < Integer.parseInt(stuCnt); i++) {
			for (String stucode : selectedStucodes) {
				// 生徒情報FormBean
				Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
				// 学籍番号の取得
				String stu = request.getParameter("stucode_" + i);
				// 選択学籍番号と同じ学籍番号かをチェック
				if (stucode.equals(stu)) {
					// 学籍番号
					studentFormBean.setStuCode(stucode);
					// 学年
					studentFormBean.setStuGrade(request.getParameter("grade_" + i));
					// クラス番号
					studentFormBean.setStuClass(request.getParameter("class_" + i));
					// 学級番号
					studentFormBean.setStuClano(request.getParameter("clsno_" + i));
					// 出力学生情報
					this.selectStudentMap.put(stucode, studentFormBean);
					// 印刷対象学籍番号の取得
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

		// 所属コード
		String user = sessionBean.getUserCode();
		// 年度
		String nendoSeireki = sessionBean.getSystemNendoSeireki();
		// ホームルーム番号
		String clsno = null;
		// 学年
		String grade = null;
		try {

			// 学校名を取得
			getSchoolName(user, nendoSeireki);

			// 校長名リストを取得
			printFormBean.setPrincipalList(getPrincipalStaff(user, nendoSeireki, printFormBean.getOutputDate(), false));

			// 表紙印刷時
			if (printFormBean.isOutput_cover()) {

				// 通知票表題
				byte[] stampImage = getStampImage(user, STAMP_IMAGE_TITLE);
				printFormBean.setStampImage(stampImage);

				// 表紙画像
				byte[] coverImage = getStampImage(user, STAMP_COVER_IMAGE);
				printFormBean.setCoverImage(coverImage);

			}

			studentMap = new HashMap<String, Print32179000StudentFormBean>();
			deedStudentMap = new HashMap<String, Print32179000StudentFormBean>();

			for (String selectStucode : selectedStucodeList) {

				// ホームルーム番号
				clsno = this.selectStudentMap.get(selectStucode).getStuClano();
				// 学年
				grade = this.selectStudentMap.get(selectStucode).getStuGrade();

				// 担任学級情報FormBeanを設定
				if (hroomKeyFormBean != null) {

					// 生徒情報を取得
					getStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// 担任名を取得
					getTeacherName(user, nendoSeireki, clsno, selectStucode);

					// 学習の記録印刷時
					if (printFormBean.isOutput_score()) {
						// 学習の記録
						getScoreList(user, nendoSeireki, grade, selectStucode);

						// 評価・評定しないコードのMap
//						getNotHyokaMap(user, nendoSeireki, grade);
//						getNotHyoteiMap(user, nendoSeireki, grade);

						// 外国語活動の記録
						getForeignlangActList(user, nendoSeireki, grade, selectStucode);

						// 総合的な学習の時間
						getTotalAct(user, nendoSeireki, grade, selectStucode);

						// 特別活動の記録
						getSpecialActivity(user, nendoSeireki, grade, selectStucode);

						// 行動の記録
						getActviewList(user, nendoSeireki, grade, selectStucode);

						// 総合所見
						getComment(user, nendoSeireki, selectStucode);

						// 出欠の記録
						getAttendList(user, nendoSeireki, grade, selectStucode);

						// 発育の記録
						getHealthrecordList(user, nendoSeireki, grade, selectStucode);
					}
				}
				// 特別支援学級情報FormBeanを設定
				if (spClassKeyFormBean != null) {

					// 生徒情報を取得
					getStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// 担任名を取得
					getSpTeacherName(user, nendoSeireki, spClassKeyFormBean.getSpgcode(), selectStucode);

					// 交流先クラスNo
					String excClsNo = "";
					if (printFormBean != null && printFormBean.getStudentMap() != null
							&& printFormBean.getStudentMap().get(selectStucode) != null) {
						excClsNo = printFormBean.getStudentMap().get(selectStucode).getExcClsno();
					}
					if (excClsNo == null || excClsNo.isEmpty()) {
						excClsNo = clsno;
					}
					// 交流先担任名を取得
					getTeacherName(user, nendoSeireki, excClsNo, selectStucode);
				}

				// 修了証出力時
				if (printFormBean.isOutput_deed()) {

					// 生徒情報を取得
					getDeedStudentInfoEntityList(user, nendoSeireki, clsno, selectStucode);

					// 校長名を取得
					List<StaffEntity> principalStaffList = getPrincipalStaff(user, nendoSeireki,
							printFormBean.getDeedDate(), true);
					if (principalStaffList.size() > 0) {
						printFormBean.setDeedPrincipalName(principalStaffList.get(0).getStf_name_w());
					}

					// 校長印
					if (printFormBean.getTerm().equals("02")) {
						// 後期の場合のみ、出力
						byte[] principalStampImage = getStampImage(user, STAMP_PRINCIPAL);
						printFormBean.setPrincipalStampImage(principalStampImage);
					}
				}
			}
		} catch (Exception e) {
			log.error("通知表入力(通知表) DB取得処理に失敗しました。", e);
			throw new TnaviException(e);
		}

	}

	/**
	 * 生徒情報を取得する.
	 * 
	 * @param clsno
	 * @param nendoSeireki
	 * @param user
	 * @return 生徒情報のリスト
	 */
	private void getStudentInfoEntityList(String user, String nendo, String clsno, String stucode) {

		// 組に特別支援学級グループ名称を出力する場合はデータを取得しておく
		String spGroupName = "";
		if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
			spGroupName = getSpGroupName(user, nendo, clsno);
		}

		// 生徒情報を取得
		Print32179000StudentEntity entity = getStudentList(user, nendo, clsno, stucode, printFormBean.getOutputDate());

		// 生徒FormBeanにセット
		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();
		try {

			Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
			beanUtil.copyProperties(studentFormBean, entity);

			// 所属コード
			studentFormBean.setUsercode(user);
			// 年度
			studentFormBean.setNendo(nendo);

			// ホームルーム名称を設定
			studentFormBean.setStuClass(this.selectStudentMap.get(stucode).getStuClass());

			if (printFormBean.isSpgdFlg()) {
				if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
					// 特別支援学級グループ名称を出力する場合は、組を差し替え
					studentFormBean.setStuClass(spGroupName.replace("組", ""));

				} else if (printFormBean.getOutputHroomInfoMode()
						.equals(Print32179000FormBean.OUTPUT_HROOMINFO_KOURYU)) {
					// 交流先の組・出席番号を出力する場合は組、出席番号を差し替え
					studentFormBean.setStuClass(entity.getExcClass());
					studentFormBean.setStuNumber(entity.getExcNumber());
				}

				if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_KOUBO)) {
					// 出席番号（公簿）で出力
					studentFormBean.setStuNumber(entity.getRefNumber());
				} else if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_NORMAL)) {
					// 出席番号（交流）で出力の場合は、変更なし
				}
			} else {
				studentFormBean.setStuClass(entity.getStuClass());
				studentFormBean.setStuNumber(entity.getStuNumber());
			}
			this.studentMap.put(entity.getStuCode(), studentFormBean);
		} catch (Exception e) {
			log.error("BeanUtils変換エラー:Print32179000StudentEntity To Print32179000StudentFormBean");
			throw new TnaviException(e);
		}
		printFormBean.setStudentMap(studentMap);
	}

	/**
	 * 生徒情報を取得する.
	 * 
	 * @param clsno
	 * @param nendoSeireki
	 * @param user
	 * @return 生徒情報のリスト
	 */
	private void getDeedStudentInfoEntityList(String user, String nendo, String clsno, String stucode) {

		// 組に特別支援学級グループ名称を出力する場合はデータを取得しておく
		String spGroupName = "";
		if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
			spGroupName = getSpGroupName(user, nendo, clsno);
		}

		// 生徒情報を取得
		Print32179000StudentEntity entity = getStudentList(user, nendo, clsno, stucode, printFormBean.getDeedDate());

		// 生徒FormBeanにセット
		BeanUtilsBean beanUtil = BeanUtilManager.getDBToPresentationUtil();
		try {

			Print32179000StudentFormBean studentFormBean = new Print32179000StudentFormBean();
			beanUtil.copyProperties(studentFormBean, entity);

			// 所属コード
			studentFormBean.setUsercode(user);
			// 年度
			studentFormBean.setNendo(nendo);

			// ホームルーム名称を設定
			studentFormBean.setStuClass(this.selectStudentMap.get(stucode).getStuClass());

			if (printFormBean.isSpgdFlg()) {
				if (printFormBean.getOutputHroomInfoMode().equals(Print32179000FormBean.OUTPUT_HROOMINFO_GROUP)) {
					// 特別支援学級グループ名称を出力する場合は、組を差し替え
					studentFormBean.setStuClass(spGroupName.replace("組", ""));

				} else if (printFormBean.getOutputHroomInfoMode()
						.equals(Print32179000FormBean.OUTPUT_HROOMINFO_KOURYU)) {
					// 交流先の組・出席番号を出力する場合は組、出席番号を差し替え
					studentFormBean.setStuClass(entity.getExcClass());
					studentFormBean.setStuNumber(entity.getExcNumber());
				}

				if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_KOUBO)) {
					// 出席番号（公簿）で出力
					studentFormBean.setStuNumber(entity.getRefNumber());
				} else if (printFormBean.getOutputNumberKind().equals(Print32179000FormBean.OUTPUT_NUMBER_NORMAL)) {
					// 出席番号（交流）で出力の場合は、変更なし
				}
			} else {
				studentFormBean.setStuClass(entity.getStuClass());
				studentFormBean.setStuNumber(entity.getStuNumber());
			}
			this.deedStudentMap.put(entity.getStuCode(), studentFormBean);
		} catch (Exception e) {
			log.error("BeanUtils変換エラー:Print32179000StudentEntity To Print32179000StudentFormBean");
			throw new TnaviException(e);
		}
		printFormBean.setDeedStudentMap(deedStudentMap);
	}

	/**
	 * 特別支援学級グループ名称を取得する
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param clsno ホームルーム番号
	 * @return 特別支援学級グループ名称
	 */
	private String getSpGroupName(String user, String nendo, String clsno) {
		Object[] param = { user, nendo, clsno };
		QueryManager qm = new QueryManager(EXEC_SQL_SPGROUP_NAME, param, String.class);
		return (String) this.executeQuery(qm);
	}

	/**
	 * 児童一覧を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param clsno クラス番号
	 * @return 児童一覧リスト
	 */
	private Print32179000StudentEntity getStudentList(String user, String nendo, String clsno, String stucode,
			String date) {
		// パラメータの生成
		Object[] param = { user, nendo, clsno, user, nendo, clsno, user, date, user, date };

		// QueryManagerの作成
		QueryManager queryManager = new QueryManager(EXEC_SQL_STUDENT, param, Print32179000StudentEntity.class);
		StringBuffer plusSQL = new StringBuffer();
		plusSQL.append(" WHERE cls_stucode = '" + stucode + "'");
		plusSQL.append(" ORDER BY cls_number");
		queryManager.setPlusSQL(plusSQL.toString());

		// クエリの実行(List形式でデータ格納)
		@SuppressWarnings("unchecked")
		List<Print32179000StudentEntity> studentList = (List<Print32179000StudentEntity>) this
				.executeQuery(queryManager);

		if (studentList != null) {
			return studentList.get(0);
		}

		return null;
	}

	/**
	 * 学校長名履歴から学校長名を取得する.<br/>
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param date 基準日
	 * @return 校長名のリスト（降順）
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
	 * クラス担任履歴から担任名を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param clsno クラス番号
	 */
	private void getTeacherName(String user, String nendo, String clsno, String stucode) {

		StringBuffer startDate = new StringBuffer();
		startDate.append(sessionBean.getSystemNendoSeireki());
		startDate.append(sessionBean.getSystemNendoStartDate());
		Object[] param = { user, nendo, clsno, startDate.toString(), printFormBean.getOutputDate() };
		QueryManager queryManager = new QueryManager(EXEC_SQL_TEACHER_NAME, param, StaffEntity.class);

		@SuppressWarnings("unchecked")
		List<StaffEntity> staffList = (List<StaffEntity>) this.executeQuery(queryManager);

		// 担任名が取れなかった場合
		if (staffList.size() == 0) {
			param = new Object[] { user, nendo, clsno };
			queryManager = new QueryManager(EXEC_SQL_TEACHER_NAME2, param, StaffEntity.class);

			staffList = (List<StaffEntity>) this.executeQuery(queryManager);
		}

		printFormBean.getStudentMap().get(stucode).setStaffList(staffList);
	}

	/**
	 * クラス担任履歴から担任名を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param clsno クラス番号
	 */
	private void getSpTeacherName(String user, String nendo, String spgdCode, String stucode) {

		StringBuffer startDate = new StringBuffer();
		startDate.append(sessionBean.getSystemNendoSeireki());
		startDate.append(sessionBean.getSystemNendoStartDate());
		Object[] param = { user, nendo, startDate.toString(), printFormBean.getOutputDate(), spgdCode, user };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SP_TEACHER_NAME, param, StaffEntity.class);

		@SuppressWarnings("unchecked")
		List<StaffEntity> staffList = (List<StaffEntity>) this.executeQuery(queryManager);

		// 担任名が取れなかった場合
		if (staffList.size() == 0) {
			param = new Object[] { spgdCode, user, nendo, user };
			queryManager = new QueryManager(EXEC_SQL_SP_TEACHER_NAME2, param, StaffEntity.class);

			staffList = (List<StaffEntity>) this.executeQuery(queryManager);
		}

		printFormBean.getStudentMap().get(stucode).setSpStaffList(staffList);
	}

	/**
	 * 学校名を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 */
	private void getSchoolName(String user, String nendo) {

		Object[] param = { sessionBean.getUseKind2(), user, nendo };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SCHOOL_NAME, param, String.class);

		String schoolName = (String) this.executeQuery(queryManager);

		printFormBean.setSchoolName(schoolName);
	}

	/**
	 * 学校印イメージを取得する
	 * 
	 * @param user 所属コード
	 * @param kind 種類 "01":校章、"02":通知票表題、"03":校長印
	 * @return 校章イメージ
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
	 * 学習の記録を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
	 */
	private void getScoreList(String user, String nendoSeireki, String grade, String stucode) {

		Object[] param = { user, nendoSeireki, grade, term, stucode };
		QueryManager queryManager = new QueryManager(EXEC_SQL_SCORE, param, Print32179000ScoreEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000ScoreEntity> scoreList = (List<Print32179000ScoreEntity>) this.executeQuery(queryManager);

		printFormBean.getStudentMap().get(stucode).setScoreList(scoreList);
	}

	/**
	 * 外国語活動を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 総合的な学習の記録を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 特別活動の記録を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 学校生活のようすを取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 総合所見を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 出欠の記録の集計を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
	 * @return 出欠の記録のリスト
	 */
	private void getAttendList(String user, String nendoSeireki, String grade, String stucode) {

		Object[] paramArray = { grade, stucode, user, nendoSeireki, user, nendoSeireki, stucode };

		QueryManager queryManager = new QueryManager(EXEC_SQL_ATTEND, paramArray, Print32179000AttendEntity.class);

		@SuppressWarnings("unchecked")
		List<Print32179000AttendEntity> attendList = (List<Print32179000AttendEntity>) this.executeQuery(queryManager);

		// 日数素合計用Entity
		Print32179000AttendEntity totalAttendEntity = new Print32179000AttendEntity();
		// 学籍番号
		totalAttendEntity.setStucode(stucode);
		// 時期コード
		totalAttendEntity.setGoptcode("02");
		// 月
		totalAttendEntity.setMonth("99");

		// 授業日数（合計）
		int teachingSum = 0;
		// 出席日数（合計）
		int attendSum = 0;
		// 欠席日数（合計）
		int absenceSum = 0;
		// 出停忌引日数（合計）
		int stopSum = 0;
		// 遅刻日数（合計）
		int lateSum = 0;
		// 早退日数（合計）
		int leaveSum = 0;
		// 在籍判定
		String onRegist = null;

		for (Print32179000AttendEntity entity : attendList) {
			printFormBean.getStudentMap().get(stucode).getAttendList().add(entity);

			// 授業日数（合計）
			teachingSum += entity.getTeachingSum();
			// 出席日数（合計）
			attendSum += entity.getAttendSum();
			// 欠席日数（合計）
			absenceSum += entity.getAbsenceSum();
			// 出停忌引日数（合計）
			stopSum += entity.getStopSum();
			// 遅刻日数（合計）
			lateSum += entity.getLateSum();
			// 早退日数（合計）
			leaveSum += entity.getLeaveSum();
			// 在籍判定
			onRegist = entity.getOnRegist();
		}
		// 授業日数（合計）
		totalAttendEntity.setTeachingSum(teachingSum);
		// 出席日数（合計）
		totalAttendEntity.setAttendSum(attendSum);
		// 欠席日数（合計）
		totalAttendEntity.setAbsenceSum(absenceSum);
		// 出停忌引日数（合計）
		totalAttendEntity.setStopSum(stopSum);
		// 遅刻日数（合計）
		totalAttendEntity.setLateSum(lateSum);
		// 早退日数（合計）
		totalAttendEntity.setLeaveSum(leaveSum);
		// 在籍判定
		totalAttendEntity.setOnRegist(onRegist);
		// 合計行の追加
		printFormBean.getStudentMap().get(stucode).getAttendList().add(totalAttendEntity);

	}

	/**
	 * 身体のようすを取得する.
	 * 
	 * @param user 所属コード
	 * @param nendoSeireki 年度
	 * @param grade 学年
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
	 * 印刷FormBeanを取得する。
	 * 
	 * @return printFormBean
	 */
	public final Print32179000FormBean getPrintFormBean() {
		return printFormBean;
	}
}
