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
 *  ���ђʒm�\���(���R�s_���w�Z) ����Action.
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
		// �J�n���O�o��
		// ------------------------------------------------------------------------------------------
		log.info("�y��ʁz���ђʒm�\���(���R�s_���w�Z) ���� START");

		// ------------------------------------------------------------------------------------------
		// �����ݒ� - FormBean�ɒl���Z�b�g
		// ------------------------------------------------------------------------------------------
		// �T�[�r�X�N���X�̐���
		Search32179000Service service = new Search32179000Service();

		// Request(�ʏ�J�ڂ̏ꍇ) ���� Session(��߂飃{�^�������̏ꍇ)�ɐݒ肳��Ă���l����FormBean���쐬����
		service.execute(request, sessionBean);

		// �쐬����FormBean���擾
		Search32179000FormBean formBean = service.getListFormBean();

		// Request��FormBean���Z�b�g����B
		request.setAttribute("FORM_BEAN", formBean);

		// ------------------------------------------------------------------------------------------
		// �����I�����O�o��
		// ------------------------------------------------------------------------------------------
		log.info("�y��ʁz���ђʒm�\���(���R�s_���w�Z) ���� END");

		return null;
	}

}
