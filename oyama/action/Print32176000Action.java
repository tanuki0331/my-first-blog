/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.exception.CrException;
import jp.co.systemd.tnavi.common.action.AbstractPrintAction;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.print.PdfDocumentBeanCR;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.service.Print32176000Service;
import jp.co.systemd.tnavi.cus.oyama.print.Print32176000;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) ���Action.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Print32176000Action extends AbstractPrintAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Print32176000Action.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doPrint(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {

		log.info("�y��ʁz�o�ȕ��� �i����jSTART");

		//���[�o�͗p�̃f�[�^���擾���󎚂���B
		PdfDocumentBeanCR pdfDocumentBeanCR = new PdfDocumentBeanCR("�o�ȕ�", false);

		//���[�t�H�[���t�@�C��
		String formFileName = this.getCrForm(request, sessionBean, "crlayout", "cus/oyama/form32176000.cfx");

		try{

			Print32176000Service service = new Print32176000Service();
			service.execute(request, sessionBean);

			Print32176000 print32176000 = new Print32176000();
			print32176000.setPrintFormBean(service.getPrintFormBean());
			print32176000.setPdfDocumentBeanCR(pdfDocumentBeanCR);
			print32176000.execute(formFileName);

			pdfDocumentBeanCR.responseOutput(response);

		} catch (CrException cex) {
			log.error("��O����",cex);
			pdfDocumentBeanCR.abortJob();
			throw new TnaviException(cex);
		} catch(TnaviException tex) {
	    	throw new TnaviException(tex);
	    } catch(Exception e) {
	    	log.error("��O����",e);
	    	throw new TnaviException(e);
	    } finally {
	    	//���\�[�X�̊J��
	    	if(pdfDocumentBeanCR != null) {
	    		pdfDocumentBeanCR.close();
	    	}
	    }

		log.info("�y��ʁz�o�ȕ��� �i����j�@END");

		return null;
	}


	@Override
	protected Log getLogClass() {
		return log;
	}
}
