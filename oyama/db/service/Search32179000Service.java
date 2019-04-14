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
 * 成績通知表印刷(小山市_小学校) 検索 Service.
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

	/** 実行SQL */
	private static final String EXEC_SQL_TERM = "cus/oyama/getPrint32179000TermList.sql";				// 出力時期
	private static final String EXEC_SQL_STUDENT = "cus/oyama/getPrint32179000StudentList.sql";			// 対象児童
	private static final String EXEC_SQL_SP_STUDENT = "cus/oyama/getPrint32179000SpStudentList.sql";	// 対象児童(特別支援学級)

	/** 共通SessionBean */
	private SystemInfoBean sessionBean;

	/** 一覧FormBean. */
	private Search32179000FormBean searchFormBean;

	/** ホームルーム情報FormBean. */
	private HroomKeyFormBean hroomKeyFormBean;

	/** 特別支援学級情報FormBean */
	SpClassKeyFormBean spClassKeyFormBean;

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

		// ホームルーム情報FormBeanを設定
		hroomKeyFormBean = sessionBean.getSelectHroomKey();

		// 特別支援学級情報取得
		spClassKeyFormBean = sessionBean.getSelectSpClassKey();

		// クエリを実行する
		super.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doQuery() throws TnaviDbException {

		// 年度
		String nendo = sessionBean.getSystemNendoSeireki();
		// 所属コード
		String user = sessionBean.getUserCode();
		// クラス名
		String hmrName = null;
		// クラス番号
		String clsno = null;
		// 特別支援学級判別用フラグ
		boolean isSpSupportCls = false;

		// 児童一覧
		List<Print32179000StudentEntity> studentList = new ArrayList<>();
		try {
			// ------------------------------------------------------------------------------------------
			// 一覧FormBeanの作成
			// ------------------------------------------------------------------------------------------
			searchFormBean = new Search32179000FormBean();

			// 特別支援学級の場合
			if (spClassKeyFormBean != null) {
				// 特別支援学級コード
				String spgcode = spClassKeyFormBean.getSpgcode();
				// クラス名
				hmrName = spClassKeyFormBean.getSpgname();
				// 特別支援学級所属生徒情報取得
				studentList = getSpSupportList(user, nendo, spgcode);
				// 特別支援学級判別用フラグ 取得
				isSpSupportCls = true;
			}
			// 担任学級の場合
			else if (hroomKeyFormBean != null) {
				// ------------------------------------------------------------------------------------------
				// ホームルーム情報 取得
				// ------------------------------------------------------------------------------------------
				// クラス名
				hmrName = hroomKeyFormBean.getHmrname();
				// クラス番号
				clsno = hroomKeyFormBean.getClsno();
				// 学生情報取得
				studentList = getStudentList(user, nendo, clsno);
			}

			// 現在日付セット
			String today = DateUtility.getSystemDate();
			// 出力日付
			searchFormBean.setOutput_year(today.substring(0, 4));
			searchFormBean.setOutput_month(today.substring(4, 6));
			searchFormBean.setOutput_day(today.substring(6));
			// 修了日付
			searchFormBean.setDeed_year(today.substring(0, 4));
			searchFormBean.setDeed_month(today.substring(4, 6));
			searchFormBean.setDeed_day(today.substring(6));

			// ------------------------------------------------------------------------------------------
			// 出力時期 取得
			// ------------------------------------------------------------------------------------------
			List<CmlguideoutputtermEntity> termList = getTermList(user, nendo);
			List<SimpleTagFormBean> outputTermList = getOutputTermList(termList);

			// ------------------------------------------------------------------------------------------
			// 出力日付 取得
			// ------------------------------------------------------------------------------------------
			List<SimpleTagFormBean> monthList = getOutputDateMonth();
			List<SimpleTagFormBean> dayList = getOutputDateDay();

			// 出力時期と出欠集計期間のチェック結果取得
			Map<String, String> errorMessage = getErrorMessage(termList);

			// ------------------------------------------------------------------------------------------
			// 取得したデータをFormBeanへ設定
			// ------------------------------------------------------------------------------------------
			// 年度
			searchFormBean.setNendo(nendo);
			// クラス名
			searchFormBean.setHmrName(hmrName);
			// 出力時期
			searchFormBean.setOutputTermList(outputTermList);
			// 出力日付 月
			searchFormBean.setMonthList(monthList);
			// 出力日付 日
			searchFormBean.setDayList(dayList);
			// 出力時期と出欠集計期間のチェック結果
			searchFormBean.setErrorMessage(errorMessage);
			// 生徒別通知表情報
			searchFormBean.setStudentList(studentList);
			// 特別支援学級フラグ
			searchFormBean.setSpSupportCls(isSpSupportCls);
			// 生徒の一覧数
			searchFormBean.setCntStu(studentList.size());

		} catch (Exception e) {
			log.error("成績通知票印刷(小山市_小学校) DB取得処理に失敗しました。", e);
			throw new TnaviException(e);
		}
	}

	/**
	 * 一覧FormBeanを取得する.
	 * 
	 * @return
	 */
	public Search32179000FormBean getListFormBean() {
		return searchFormBean;
	}

	/**
	 * 学期リストを取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @return 学期リスト
	 */
	private List<CmlguideoutputtermEntity> getTermList(String user, String nendo) {
		// パラメータの生成
		Object[] paramTag = { user, nendo };

		// QueryManagerの作成
		QueryManager queryManager = new QueryManager(EXEC_SQL_TERM, paramTag, CmlguideoutputtermEntity.class);

		// クエリの実行(List形式でデータ格納)
		@SuppressWarnings("unchecked")
		List<CmlguideoutputtermEntity> entityTermList = (List<CmlguideoutputtermEntity>) this.executeQuery(queryManager);

		return entityTermList;
	}

	/**
	 * 出力時期を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @return 出力時期リスト
	 */
	private List<SimpleTagFormBean> getOutputTermList(List<CmlguideoutputtermEntity> entityTermList) {

		List<SimpleTagFormBean> termList = new ArrayList<SimpleTagFormBean>();

		for (CmlguideoutputtermEntity entityTerm : entityTermList) {
			termList.add(new SimpleTagFormBean(entityTerm.getGopt_goptcode(), entityTerm.getGopt_name()));
		}

		return termList;
	}

	/**
	 * 出力日付月のリストを生成する.
	 * 
	 * @return 出力日付月のリスト
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
	 * 出力日付日のリストを生成する.
	 * 
	 * @return 出力日付日のリスト
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
	 * 児童一覧を取得する.
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param clsno クラス番号
	 * @return 児童一覧リスト
	 */
	private List<Print32179000StudentEntity> getStudentList(String user, String nendo, String clsno) {
		// パラメータの生成
		Object[] param = { user, nendo, clsno };

		// QueryManagerの作成
		QueryManager queryManager = new QueryManager(EXEC_SQL_STUDENT, param, Print32179000StudentEntity.class);

		// クエリの実行(List形式でデータ格納)
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

		// 特別支援学級所属生徒情報の取得
		return studentList;
	}

	/**
	 * 特別支援学級所属生徒情報を取得する
	 * 
	 * @param user 所属コード
	 * @param nendo 年度
	 * @param spgdcode 特別支援学級コード
	 * @return 特別支援学級所属生徒一覧リスト
	 */
	private List<Print32179000StudentEntity> getSpSupportList(String user, String nendo, String spgdcode) {

		// パラメータの生成
		Object[] param = { spgdcode, user, nendo };

		// QueryManagerの作成
		QueryManager queryManager = new QueryManager(EXEC_SQL_SP_STUDENT, param, Print32179000StudentEntity.class);

		// クエリの実行(List形式でデータ格納)
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
		// 特別支援学級所属生徒情報の取得
		return studentList;
	}

	/**
	 * 出力時期と出欠集計期間のチェック結果
	 * 
	 * @param termList 成績通知表出力時期リスト
	 * @return key:出力時期コード、value:出力メッセージ
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
						errMsg.append("、");
					}
					errMsg.append(tempTermName);
				}
			}
			if (errMsg.length() > 0) {
				errMsg.append("の出欠集計期間が設定されていません。");
				map.put(currentTermId, errMsg.toString());
			}
		}

		return map;
	}
}
