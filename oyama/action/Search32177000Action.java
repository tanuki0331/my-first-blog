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
 *  �o�ȕ���(���ʎx���w���@���R�s�p) �����E�S�C���� ����Action.
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
		log.info("�y��ʁz�o�ȕ���(�����E�S�C����) �i�����jSTART");


		Search32177000FormBean searchFormBean = (Search32177000FormBean)copyRequestParamToFormBean(request, new Search32177000FormBean());
		searchFormBean.setClassType(AttPrintSearchFormBeanImpl.CLASS_TYPE_SPCLASS);

		Search32177000Service searchService = new Search32177000Service(request, sessionBean, searchFormBean);
		searchService.execute();

		//Request��FormBean���Z�b�g����
		request.setAttribute("FORM_BEAN", searchFormBean);

		log.info("�y��ʁz�o�ȕ���(�����E�S�C����) �i�����jEND");

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean doCheck(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// ���̓`�F�b�N����
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSkip(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// �X�L�b�v�Ȃ�
		return false;
	}




}
