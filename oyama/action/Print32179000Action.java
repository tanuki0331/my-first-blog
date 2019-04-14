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
import jp.co.systemd.tnavi.cus.ashigarakami.action.Print31910000Action;
import jp.co.systemd.tnavi.cus.oyama.db.service.Print32179000Service;
import jp.co.systemd.tnavi.cus.oyama.print.Print32179000;

/**
 * ���ђʒm�\���(���R�s_���w�Z) ���  Action.
 *
 * <B>Create</B> 2019.04.13 BY AIVICK<BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000Action extends AbstractPrintAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Print31910000Action.class);

	@Override
	protected String doPrint(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {

		log.info("���ђʒm�\���(���R�s_���w�Z) ���  START");

		//-----���[�o�͗p�̃f�[�^���擾���󎚂���B
		PdfDocumentBeanCR pdfDocumentBeanCR = new PdfDocumentBeanCR("���ђʒm�\");

		try {
			Print32179000Service service = new Print32179000Service();
			service.execute(request, sessionBean);

			Print32179000 print32179000 = new Print32179000();
			print32179000.setPrintFormBean(service.getPrintFormBean());
			print32179000.setPdfDocumentBeanCR(pdfDocumentBeanCR);
			print32179000.execute("cus/oyama/form32179000.cfx");

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

		log.info("���ђʒm�\���(���R�s_���w�Z) ���  END");

		return null;
	}

	@Override
	protected Log getLogClass() {
		return null;
	}

}
