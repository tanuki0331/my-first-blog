package jp.co.systemd.tnavi.cus.oyama.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.service.Search32179000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32179000FormBean;

/**
 * <PRE>
 *  成績通知表印刷(小山市_小学校) 検索Action.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Search32179000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32179000Action.class);

	@Override
	public boolean isSkip(ServletContext sc, HttpServletRequest request, SystemInfoBean sessionBean) {
		return false;
	}

	@Override
	public boolean doCheck(ServletContext sc, HttpServletRequest request, SystemInfoBean sessionBean) {
		return true;
	}

	@Override
	public String doAction(ServletContext sc, HttpServletRequest request, HttpServletResponse response,
			SystemInfoBean sessionBean) {
		// ------------------------------------------------------------------------------------------
		// 開始ログ出力
		// ------------------------------------------------------------------------------------------
		log.info("【画面】成績通知表印刷(小山市_小学校) 検索 START");

		// ------------------------------------------------------------------------------------------
		// 初期設定 - FormBeanに値をセット
		// ------------------------------------------------------------------------------------------
		// サービスクラスの生成
		Search32179000Service service = new Search32179000Service();

		// Request(通常遷移の場合) 又は Session(｢戻る｣ボタン押下の場合)に設定されている値からFormBeanを作成する
		service.execute(request, sessionBean);

		// 作成したFormBeanを取得
		Search32179000FormBean formBean = service.getListFormBean();

		// RequestにFormBeanをセットする。
		request.setAttribute("FORM_BEAN", formBean);

		// ------------------------------------------------------------------------------------------
		// 処理終了ログ出力
		// ------------------------------------------------------------------------------------------
		log.info("【画面】成績通知表印刷(小山市_小学校) 検索 END");

		return null;
	}

}
