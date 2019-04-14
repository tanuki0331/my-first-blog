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
 *  oΘλσό(Κνw@¬Rsp) σόAction.
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

		log.info("yζΚzoΘλσό iσόjSTART");

		// [oΝpΜf[^πζΎ΅σ·ιB
		PdfDocumentBeanCR pdfDocumentBeanCR = new PdfDocumentBeanCR("oΘλ", false);

		// [tH[t@C
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
			log.error("αO­Ά",cex);
			pdfDocumentBeanCR.abortJob();
			throw new TnaviException(cex);
		} catch(TnaviException tex) {
	    	throw new TnaviException(tex);
	    } catch(Exception e) {
	    	log.error("αO­Ά",e);
	    	throw new TnaviException(e);
	    } finally {
	    	//\[XΜJϊ
	    	if(pdfDocumentBeanCR != null) {
	    		pdfDocumentBeanCR.close();
	    	}
	    }

		log.info("yζΚzoΘλσό iσόj@END");

		return null;
	}


	@Override
	protected Log getLogClass() {
		return log;
	}
}
