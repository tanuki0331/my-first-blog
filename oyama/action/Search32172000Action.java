/**------------------------------------------------------------**
 * Te@cherNavi
 *  Copyright(C) 2019 SystemD inc,All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateUtility;
import jp.co.systemd.tnavi.cus.oyama.db.service.Search32172000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32172000FormBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ���������\������
 * �o���Ȉꗗ�o�� ����Action.
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */
public class Search32172000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32172000Action.class);

	@Override
	public String doAction(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {

		// ------------------------------------------------------------------------------------------
		// �����J�n���O�o��
		// ------------------------------------------------------------------------------------------
		log.info("�y��ʁz�o���Ȉꗗ�o�� ���� START");

		// �V�X�e�����t���擾
		String today = DateUtility.getSystemDate();

		// ------------------------------------------------------------------------------------------
		// FormBean����
		// ------------------------------------------------------------------------------------------
		Search32172000FormBean searchFormBean= new Search32172000FormBean();

		String reload_changed = "";

		String nendo         = request.getParameter("nendo");
		if(nendo == null) nendo = "";

		String init_nendo    = request.getParameter("init_nendo");
		if(init_nendo == null) init_nendo = "";

		String sem_code      = request.getParameter("sem_code");
		if(sem_code == null) sem_code = "";

		String init_sem_code = request.getParameter("init_sem_code");
		if(init_sem_code == null) init_sem_code = "";

		// �N�x�A�w�����ύX����Ă���ꍇ�A�o�͎����͏���������B
		if( !nendo.equals(init_nendo) || !sem_code.equals(init_sem_code)){
			reload_changed = "1";
		}

		String output_year   = request.getParameter("outputYear");
		String output_month  = request.getParameter("outputMonth");
		String output_day    = request.getParameter("outputDay");

		// �o�͓��ݒ�p�@�N������ݒ肷��B
		if(output_year == null){
			output_year = today.substring(0,4);
		}
		if(output_month == null){
			output_month = today.substring(4,6);
		}
		if(output_day == null){
			output_day = today.substring(6);
        }

		// ------------------------------------------------------------------------------------------
		// �p�����[�^�l �擾 ( reload�� )
		// ------------------------------------------------------------------------------------------
		String[] checkedTargetMonth = new String[12];

		for ( int i=0 ; i<12 ; i ++ ){
			checkedTargetMonth[i] = request.getParameter("targetMonth_" + i );
			if( checkedTargetMonth[i]==null){
				checkedTargetMonth[i]="";
			}
		}
		searchFormBean.setCheckedTargetMonth(checkedTargetMonth);

		// ------------------------------------------------------------------------------------------
		// �����ݒ� - FormBean�ɒl���Z�b�g
		// ------------------------------------------------------------------------------------------

		HroomKeyFormBean  hroomKeyFormBean = sessionBean.getSelectHroomKey();

		String reload       = request.getParameter("reload");
		if(reload == null){
			reload = "";
		}

		String transitionMode = "";
		String grade          = "";
		String hmrClsno       = "";
		String hmrClass       = "";
		String hmrName        = "";

		nendo      = request.getParameter("nendo");
		grade      = request.getParameter("grade");
		hmrClsno   = request.getParameter("hmrClsno");
		hmrClass   = request.getParameter("hmrClass");
		hmrName    = request.getParameter("hmrName");

		if( hroomKeyFormBean != null ){
			// �S�C����̑J��
			transitionMode = "1";

			if( nendo == null ){
				nendo     = sessionBean.getSystemNendoSeireki();
			}
			if( grade == null ){
				grade     = sessionBean.getSelectHroomKey().getGlade();
			}
			if( hmrClsno == null ){
				hmrClsno  = sessionBean.getSelectHroomKey().getClsno();
				hmrClass  = sessionBean.getSelectHroomKey().getHmrclass();
				hmrName   = sessionBean.getSelectHroomKey().getHmrname();
			}
		}else{
			// ��������̑J��
			transitionMode = "";

			if( nendo == null ){
				nendo = sessionBean.getSystemNendoSeireki();
			}
			if( grade == null ){
				grade = "1";
			}
			if( hmrClsno == null ){
				hmrClsno = "";
			}
		}

		searchFormBean.setTransitionMode(transitionMode);
		searchFormBean.setReload(reload);
		searchFormBean.setReload_changed(reload_changed);

		searchFormBean.setNendo(nendo);
		searchFormBean.setSemCode(sem_code);

		searchFormBean.setGrade(grade);
		searchFormBean.setHmrClsno(hmrClsno);
		searchFormBean.setHmrClass(hmrClass);
		searchFormBean.setHmrName(hmrName);

		searchFormBean.setOutputYear(output_year);
		searchFormBean.setOutputMonth(output_month);
		searchFormBean.setOutputDay(output_day);


		// ------------------------------------------------------------------------------------------
		// �T�[�r�X����f�[�^�擾
		// ------------------------------------------------------------------------------------------
		Search32172000Service service = new Search32172000Service();

		service.setParameters(sessionBean, nendo, grade);
		service.setSearchFormBean(searchFormBean);

		service.execute();
		searchFormBean = service.getSearchFormBean();

		// ------------------------------------------------------------------------------------------
		// Request��FormBean���Z�b�g
		// ------------------------------------------------------------------------------------------
		request.setAttribute("FORM_BEAN", searchFormBean);

		// ------------------------------------------------------------------------------------------
		// �����I�����O�o��
		// ------------------------------------------------------------------------------------------
		log.info("�o���Ȉꗗ�o�� ���� END");

		return null;
	}

	@Override
	public boolean doCheck(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// ���̓`�F�b�N����
		return true;
	}

	@Override
	public boolean isSkip(ServletContext sc, HttpServletRequest request,
			SystemInfoBean sessionBean) {
		// �X�L�b�v�Ȃ�
		return false;
	}
}
