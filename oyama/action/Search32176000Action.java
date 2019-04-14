/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.db.entity.CodeEntity;
import jp.co.systemd.tnavi.common.db.service.FindCodeByUserAndKindAndCodeService;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateUtility;
import jp.co.systemd.tnavi.cus.oyama.db.service.CheckRegist32176000Service;
import jp.co.systemd.tnavi.cus.oyama.db.service.CreateMonthDataTable32176000Service;
import jp.co.systemd.tnavi.cus.oyama.db.service.Detail32176000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32176000FormBean;
import jp.co.systemd.tnavi.junior.att.utility.AttUtility;

/**
 * <PRE>
 *  出席簿印刷(通常学級　小山市用) 教務・担任共通 検索Action.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>

 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32176000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32176000Action.class);

	public static final String ATTENDKINDJR_CODE_ATTEND = "00";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doAction(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {
		log.info("【画面】出席簿印刷(教務・担任共通) （検索）START");

		//-----FormBean生成
		Search32176000FormBean searchFormBean = new Search32176000FormBean();
		String user = sessionBean.getUserCode();

		// ---------------------------------------------------
		// パラメータ取得
		// ---------------------------------------------------
		// 表示時に画面に表示していた年度、学科、年を取得する
		String init_nendo  = request.getParameter("init_nendo");
		String init_grade  = request.getParameter("init_grade");

		// システム日付
		String today = DateUtility.getSystemDate();

		// システム日付が処理年度の年度期間範囲外である場合、処理年度切り替えされていると判断し
		// 処理年月には年度終了日の年月を設定する
		String yyyymm = "";
		String nendoStartYmd = sessionBean.getSystemNendoSeireki() + sessionBean.getSystemNendoStartDate().trim();
		String nendoEndYmd = DateUtility.getNendoEndDate(sessionBean.getSystemNendoSeireki(),
								sessionBean.getSystemNendoStartDate().trim(), sessionBean.getSystemNendoEndDate().trim());

		if (nendoStartYmd.compareTo(today) <= 0 && today.compareTo(nendoEndYmd) <= 0) {
			// システム日付が処理年度内
			yyyymm = today.substring(0, 6);
		}else{
			yyyymm = nendoEndYmd.substring(0, 6);
		}

		String req_yyyymm     = request.getParameter("yyyymm");
		if (req_yyyymm == null){
			req_yyyymm = yyyymm;
		}

		String org_hmrclass = "";
		String org_hmrgrade = "";


		// クラス選択で選択したクラスをセッションから取得
		HroomKeyFormBean hroomKeyFormBean = sessionBean.getSelectHroomKey();

		if( hroomKeyFormBean == null ) {

			searchFormBean.setTran_mode("");

			// tbl_hroom から最初のデータを取得する。
			Detail32176000Service detail32176000Service = new Detail32176000Service();

			detail32176000Service.setSearch32176000FormBean(searchFormBean);

			// メニューからの初期表示の場合
			if (init_nendo == null) {

				detail32176000Service.setParameters(user, sessionBean.getSystemNendoSeireki() );
				detail32176000Service.execute();

				org_hmrclass = searchFormBean.getHmrclassAtFirst();
				org_hmrgrade = searchFormBean.getGradeAtFirst();

				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());

				searchFormBean.setGrade(org_hmrgrade);
				searchFormBean.setHmrclass(org_hmrclass);

				searchFormBean.setYyyymm(yyyymm);

			} else {

				// requestの内容をコピー
				searchFormBean = (Search32176000FormBean)copyRequestParamToFormBean(request, searchFormBean);

				// プルダウン変更によるリロードを判定
				// 年度が変更された場合
				if (! searchFormBean.getNendo().equals(init_nendo)) {

					detail32176000Service.setParameters(user, searchFormBean.getNendo() );
					detail32176000Service.execute();

					org_hmrclass = searchFormBean.getHmrclassAtFirst();
					org_hmrgrade = searchFormBean.getGradeAtFirst();

					// 年を初期化
					searchFormBean.setGrade(org_hmrgrade);
					// 組を初期化
					searchFormBean.setHmrclass(org_hmrclass);
					// 月を初期化
					searchFormBean.setYyyymm(yyyymm);

				// 学年が変更された場合
				} else if (! searchFormBean.getGrade().equals(init_grade)) {

					detail32176000Service.setParameters(user, searchFormBean.getNendo() );
					detail32176000Service.execute();

					org_hmrclass = searchFormBean.getHmrclassAtFirst();
					// 組を初期化
					searchFormBean.setHmrclass(org_hmrclass);
					// 月を初期化
					searchFormBean.setYyyymm(searchFormBean.getYyyymm());
				}
			}

		}else{
			searchFormBean.setTran_mode("CLS");

			searchFormBean.setNendo( sessionBean.getSystemNendoSeireki());
			searchFormBean.setGrade( hroomKeyFormBean.getGlade() );
			searchFormBean.setClsno( hroomKeyFormBean.getClsno() );
			searchFormBean.setHmrclass( hroomKeyFormBean.getHmrclass() );
			searchFormBean.setYyyymm(req_yyyymm);
		}



		// ---------------------------------------------------
		// 月選択用DropDownの作成
		// ---------------------------------------------------
		AttUtility attUtility = new AttUtility();

		String start_date = sessionBean.getSystemNendoStartDate().trim();

		attUtility.setYear(searchFormBean.getNendo());
		attUtility.setMonth_format("");
		attUtility.setSelected_yyyymm(searchFormBean.getYyyymm() );
		attUtility.setStart_mmdd(start_date);
		attUtility.generatetNextYear();

		String dropdownform = "";

		try {
			dropdownform = attUtility.createDropDownForMonthByNendoStart();
		} catch (Exception e) {
			log.error("例外発生：年度開始月DropDown ",e);
		}
		searchFormBean.setDropDownForMonth(dropdownform);

		// ---------------------------------------------------
		// 出欠区分コード（出席：00）の登録有無チェック
		// ---------------------------------------------------
		CheckRegist32176000Service service = new CheckRegist32176000Service();
		service.setParameters(sessionBean.getUserCode(), searchFormBean.getNendo(), ATTENDKINDJR_CODE_ATTEND );
		service.execute();
		int resultcount = service.getCount();

		if (resultcount == 0) {
			searchFormBean.setNodata("0");
		} else {
			searchFormBean.setNodata("1");
		}

		// ---------------------------------------------------
		// 対象月テーブル生成
		// ---------------------------------------------------
		// --- パラメータセット
		CreateMonthDataTable32176000Service  createListService = new CreateMonthDataTable32176000Service(user, searchFormBean.getNendo(),searchFormBean.getGrade(), searchFormBean.getHmrclass(), searchFormBean.getYyyymm());
		// --- 処理実行
		createListService.execute();
		String monthTableHtml = createListService.getMonthTableHtml();
		int monthDayCount = createListService.getMonthDayCount();
		searchFormBean.setMonthDayCount(monthDayCount);
		searchFormBean.setMonthTableHtml(monthTableHtml);

		// ---------------------------------------------------
		// 戸籍・通称選択可能フラグ 取得(cod_kind:606 code:007 cod_value2)
		// 0：使用しない　1:使用する(通称デフォ)　2：使用する(戸籍デフォ)
		// ---------------------------------------------------
		String outputNameFlg =null;
		String outputKanaFlg =null;
		// パラメータの生成
		FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(sessionBean.getUserCode(),606,"007");

		// クエリの実行(List形式で取得)
		findCodeService.execute();
		List<CodeEntity> wk_codeEntity = (List<CodeEntity>)findCodeService.getObj();

		for (CodeEntity entity : wk_codeEntity) {
			outputNameFlg = entity.getCod_value2();
			outputKanaFlg = entity.getCod_name2();
		}

		//定義が無い等、取得できない場合は使用しない:0をセット
		if(outputNameFlg == null){
			outputNameFlg = "0";
		}
		if(outputKanaFlg == null){
			outputKanaFlg = "0";
		}

		searchFormBean.setOutputNameFlg(outputNameFlg);
		searchFormBean.setOutputKanaFlg(outputKanaFlg);

		//RequestにFormBeanをセットする
		request.setAttribute("FORM_BEAN", searchFormBean);

		log.info("【画面】出席簿印刷(教務・担任共通) （検索）END");

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean doCheck(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// 入力チェック正常
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSkip(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// スキップなし
		return false;
	}


}
