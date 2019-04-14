/**------------------------------------------------------------**
 * Te@cherNavi
 *  Copyright(C) 2019 SystemD inc,All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateUtility;
import jp.co.systemd.tnavi.cus.oyama.db.service.Search32172000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32172000FormBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ＜＜初期表示＞＞
 * 出欠席一覧出力 検索Action.
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Search32172000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32172000Action.class);

	@Override
	public String doAction(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {

		// ------------------------------------------------------------------------------------------
		// 処理開始ログ出力
		// ------------------------------------------------------------------------------------------
		log.info("【画面】出欠席一覧出力 検索 START");

		// システム日付を取得
		String today = DateUtility.getSystemDate();

		// ------------------------------------------------------------------------------------------
		// FormBean生成
		// ------------------------------------------------------------------------------------------
		Search32172000FormBean searchFormBean= new Search32172000FormBean();

		String reload_changed = "";

		String nendo         = request.getParameter("nendo");
		if(nendo == null) nendo = "";

		String init_nendo    = request.getParameter("init_nendo");
		if(init_nendo == null) init_nendo = "";

		String sem_code      = request.getParameter("sem_code");
		if(sem_code == null) sem_code = "";

		String init_sem_code = request.getParameter("init_sem_code");
		if(init_sem_code == null) init_sem_code = "";

		// 年度、学期が変更されている場合、出力時期は初期化する。
		if( !nendo.equals(init_nendo) || !sem_code.equals(init_sem_code)){
			reload_changed = "1";
		}

		String output_year   = request.getParameter("outputYear");
		String output_month  = request.getParameter("outputMonth");
		String output_day    = request.getParameter("outputDay");

		// 出力日設定用　年月日を設定する。
		if(output_year == null){
			output_year = today.substring(0,4);
		}
		if(output_month == null){
			output_month = today.substring(4,6);
		}
		if(output_day == null){
			output_day = today.substring(6);
        }

		// ------------------------------------------------------------------------------------------
		// パラメータ値 取得 ( reload時 )
		// ------------------------------------------------------------------------------------------
		String[] checkedTargetMonth = new String[12];

		for ( int i=0 ; i<12 ; i ++ ){
			checkedTargetMonth[i] = request.getParameter("targetMonth_" + i );
			if( checkedTargetMonth[i]==null){
				checkedTargetMonth[i]="";
			}
		}
		searchFormBean.setCheckedTargetMonth(checkedTargetMonth);

		// ------------------------------------------------------------------------------------------
		// 初期設定 - FormBeanに値をセット
		// ------------------------------------------------------------------------------------------

		HroomKeyFormBean  hroomKeyFormBean = sessionBean.getSelectHroomKey();

		String reload       = request.getParameter("reload");
		if(reload == null){
			reload = "";
		}

		String transitionMode = "";
		String grade          = "";
		String hmrClsno       = "";
		String hmrClass       = "";
		String hmrName        = "";

		nendo      = request.getParameter("nendo");
		grade      = request.getParameter("grade");
		hmrClsno   = request.getParameter("hmrClsno");
		hmrClass   = request.getParameter("hmrClass");
		hmrName    = request.getParameter("hmrName");

		if( hroomKeyFormBean != null ){
			// 担任からの遷移
			transitionMode = "1";

			if( nendo == null ){
				nendo     = sessionBean.getSystemNendoSeireki();
			}
			if( grade == null ){
				grade     = sessionBean.getSelectHroomKey().getGlade();
			}
			if( hmrClsno == null ){
				hmrClsno  = sessionBean.getSelectHroomKey().getClsno();
				hmrClass  = sessionBean.getSelectHroomKey().getHmrclass();
				hmrName   = sessionBean.getSelectHroomKey().getHmrname();
			}
		}else{
			// 教務からの遷移
			transitionMode = "";

			if( nendo == null ){
				nendo = sessionBean.getSystemNendoSeireki();
			}
			if( grade == null ){
				grade = "1";
			}
			if( hmrClsno == null ){
				hmrClsno = "";
			}
		}

		searchFormBean.setTransitionMode(transitionMode);
		searchFormBean.setReload(reload);
		searchFormBean.setReload_changed(reload_changed);

		searchFormBean.setNendo(nendo);
		searchFormBean.setSemCode(sem_code);

		searchFormBean.setGrade(grade);
		searchFormBean.setHmrClsno(hmrClsno);
		searchFormBean.setHmrClass(hmrClass);
		searchFormBean.setHmrName(hmrName);

		searchFormBean.setOutputYear(output_year);
		searchFormBean.setOutputMonth(output_month);
		searchFormBean.setOutputDay(output_day);


		// ------------------------------------------------------------------------------------------
		// サービスからデータ取得
		// ------------------------------------------------------------------------------------------
		Search32172000Service service = new Search32172000Service();

		service.setParameters(sessionBean, nendo, grade);
		service.setSearchFormBean(searchFormBean);

		service.execute();
		searchFormBean = service.getSearchFormBean();

		// ------------------------------------------------------------------------------------------
		// RequestにFormBeanをセット
		// ------------------------------------------------------------------------------------------
		request.setAttribute("FORM_BEAN", searchFormBean);

		// ------------------------------------------------------------------------------------------
		// 処理終了ログ出力
		// ------------------------------------------------------------------------------------------
		log.info("出欠席一覧出力 検索 END");

		return null;
	}

	@Override
	public boolean doCheck(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// 入力チェック正常
		return true;
	}

	@Override
	public boolean isSkip(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// スキップなし
		return false;
	}
}
