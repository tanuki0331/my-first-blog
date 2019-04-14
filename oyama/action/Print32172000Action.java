/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2019 System D, Inc. All Rights Reserved.
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
import jp.co.systemd.tnavi.cus.oyama.db.service.Print32172000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32172000FormBean;
import jp.co.systemd.tnavi.cus.oyama.print.Print32172000;

/**
 * <PRE>
 *  �o���Ȉꗗ�o�� ���Action.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY aivick<BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Print32172000Action extends AbstractPrintAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Print32172000Action.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doPrint(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {

		log.info("�y��ʁz�o���Ȉꗗ�o�� �i����jSTART");

		// FormBean
		Print32172000FormBean printFormBean  = new Print32172000FormBean();

		// ���̓p�����^�̎擾
		String nendo           = request.getParameter("nendo");		// �N�x
		String sem_code        = request.getParameter("sem_code");	// �w��
		boolean year_total_flag = false ;
		if( sem_code.equals("03")){
			year_total_flag = true ;
		}

		String sem_start       = request.getParameter("sem_start"); // �w��(�J�n��)
		String sem_end         = request.getParameter("sem_end");   // �w��(�I����)
		String sem_name        = request.getParameter("sem_name");  // �w����

		String[] checkedTargetMonth = new String[12];

		int checkedCount = 0;
		for ( int i=0 ; i<12 ; i ++ ){
			checkedTargetMonth[i] = request.getParameter("targetMonth_" + i );
			if( checkedTargetMonth[i]==null){
				checkedTargetMonth[i]="";
			}else{
				checkedCount ++ ;
			}
		}

		String grade           = request.getParameter("grade");
		String hmrClsno        = request.getParameter("hmrClsno");

		String output_year     = request.getParameter("outputYear");
		String output_month    = request.getParameter("outputMonth");
		String output_day      = request.getParameter("outputDay");


		//CP���ɓn���p�����^�̐ݒ�
		printFormBean.setNendo(nendo);
		printFormBean.setSemCode(sem_code);
		printFormBean.setSemStart(sem_start);
		printFormBean.setSemEnd(sem_end);
		printFormBean.setSemName(sem_name);
		printFormBean.setGrade(grade);
		printFormBean.setHmrClsno(hmrClsno);
		printFormBean.setCheckedTargetMonth(checkedTargetMonth);
		printFormBean.setCheckedCount(checkedCount);     // �o�͑Ώی���
		printFormBean.setYearTotalFlag(year_total_flag); // �N�ԏW�v�̗L��

		printFormBean.setOutputYear(output_year);
		printFormBean.setOutputMonth(output_month);
		printFormBean.setOutputDay(output_day);


		//-----���[�o�͗p�̃f�[�^���擾���󎚂���B
		PdfDocumentBeanCR pdfDocumentBeanCR = new PdfDocumentBeanCR("�o���Ȉꗗ");

		try{

			Print32172000Service print32172000Service = new Print32172000Service();

			print32172000Service.execute(printFormBean, sessionBean);

			//���[�t�H�[���t�@�C��
			String formFileName = "cus/oyama/form32172000.cfx";

			Print32172000 print32172000 = new Print32172000();
			print32172000.setSessionBean(sessionBean);
			print32172000.setPrintFormBean(print32172000Service.getPrintFormBean());
			print32172000.setPdfDocumentBeanCR(pdfDocumentBeanCR);
			print32172000.execute(formFileName);

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

		log.info("�y��ʁz�o���Ȉꗗ�o�� �i����j�@END");

		return null;
	}

	@Override
	protected Log getLogClass() {
		return log;
	}
}

