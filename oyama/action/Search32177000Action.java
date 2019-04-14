/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.service.Search32177000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32177000FormBean;


/**
 * <PRE>
 *  出席簿印刷(特別支援学級　小山市用) 教務・担任共通 検索Action.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32177000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32177000Action.class);

	public static final String ATTENDKINDJR_CODE_ATTEND = "00";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doAction(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {
		log.info("【画面】出席簿印刷(教務・担任共通) （検索）START");


		Search32177000FormBean searchFormBean = (Search32177000FormBean)copyRequestParamToFormBean(request, new Search32177000FormBean());
		searchFormBean.setClassType(AttPrintSearchFormBeanImpl.CLASS_TYPE_SPCLASS);

		Search32177000Service searchService = new Search32177000Service(request, sessionBean, searchFormBean);
		searchService.execute();

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
