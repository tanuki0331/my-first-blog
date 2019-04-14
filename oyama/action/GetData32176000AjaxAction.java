package jp.co.systemd.tnavi.cus.oyama.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.action.AbstractAjaxServlet;
import jp.co.systemd.tnavi.common.action.RequestEncodeServlet;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.service.CreateMonthDataTable32176000Service;


/**
 * <PRE>
 * Ajax 出席簿印刷(通常学級　小山市用) データ(月データ)取得Servlet.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 * @since 1.0.
 */
public class GetData32176000AjaxAction extends AbstractAjaxServlet implements
		RequestEncodeServlet {

	/** シリアルバージョン ID */
	private static final long serialVersionUID = 1L;

	/** log4j */
	private static final Log log = LogFactory.getLog(GetData32176000AjaxAction.class);

	@Override
	protected Object processAjaxRequest(HttpServletRequest request,SystemInfoBean sessionBean) throws IOException, ServletException {

		log.info("【画面】Ajax受信：リストチェック処理 START");

		// ---------------------------------------------------
		// パラメータ取得
		// ---------------------------------------------------
		String user 		= sessionBean.getUserCode();			// 所属コード
		String year 		= request.getParameter("year");			// 年度
		String grade 		= request.getParameter("grade");		// 学年
		String hmrClass 	= request.getParameter("hmrClass");		// 組
		String month 		= request.getParameter("month");		// 月
		// ---------------------------------------------------
		// 対象月一覧 HTML生成
		// ---------------------------------------------------
		// --- パラメータセット
		CreateMonthDataTable32176000Service  createListService = new CreateMonthDataTable32176000Service(user, year, grade, hmrClass, month);
		// --- 生成処理実行
		createListService.execute();
		// --- HTML取得
		String monthTableHtml = createListService.getMonthTableHtml();
		int monthDayCount = createListService.getMonthDayCount();

		// 戻り値にセット
		Object[] result = new Object[4];
		result[0] = monthTableHtml;
		result[1] = monthDayCount;

		log.info("【画面】Ajax受信：リストのリロード処理 END");

		return result;
	}

}

