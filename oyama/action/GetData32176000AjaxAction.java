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
 * Ajax �o�ȕ���(�ʏ�w���@���R�s�p) �f�[�^(���f�[�^)�擾Servlet.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 * @since 1.0.
 */
public class GetData32176000AjaxAction extends AbstractAjaxServlet implements
		RequestEncodeServlet {

	/** �V���A���o�[�W���� ID */
	private static final long serialVersionUID = 1L;

	/** log4j */
	private static final Log log = LogFactory.getLog(GetData32176000AjaxAction.class);

	@Override
	protected Object processAjaxRequest(HttpServletRequest request,SystemInfoBean sessionBean) throws IOException, ServletException {

		log.info("�y��ʁzAjax��M�F���X�g�`�F�b�N���� START");

		// ---------------------------------------------------
		// �p�����[�^�擾
		// ---------------------------------------------------
		String user 		= sessionBean.getUserCode();			// �����R�[�h
		String year 		= request.getParameter("year");			// �N�x
		String grade 		= request.getParameter("grade");		// �w�N
		String hmrClass 	= request.getParameter("hmrClass");		// �g
		String month 		= request.getParameter("month");		// ��
		// ---------------------------------------------------
		// �Ώی��ꗗ HTML����
		// ---------------------------------------------------
		// --- �p�����[�^�Z�b�g
		CreateMonthDataTable32176000Service  createListService = new CreateMonthDataTable32176000Service(user, year, grade, hmrClass, month);
		// --- �����������s
		createListService.execute();
		// --- HTML�擾
		String monthTableHtml = createListService.getMonthTableHtml();
		int monthDayCount = createListService.getMonthDayCount();

		// �߂�l�ɃZ�b�g
		Object[] result = new Object[4];
		result[0] = monthTableHtml;
		result[1] = monthDayCount;

		log.info("�y��ʁzAjax��M�F���X�g�̃����[�h���� END");

		return result;
	}

}

