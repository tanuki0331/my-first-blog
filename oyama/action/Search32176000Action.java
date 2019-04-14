/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.action.AbstractAction;
import jp.co.systemd.tnavi.common.db.entity.CodeEntity;
import jp.co.systemd.tnavi.common.db.service.FindCodeByUserAndKindAndCodeService;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateUtility;
import jp.co.systemd.tnavi.cus.oyama.db.service.CheckRegist32176000Service;
import jp.co.systemd.tnavi.cus.oyama.db.service.CreateMonthDataTable32176000Service;
import jp.co.systemd.tnavi.cus.oyama.db.service.Detail32176000Service;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32176000FormBean;
import jp.co.systemd.tnavi.junior.att.utility.AttUtility;

/**
 * <PRE>
 *  �o�ȕ���(�ʏ�w���@���R�s�p) �����E�S�C���� ����Action.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>

 * <B>remark</B><BR>
 *
 * @author
 * @since 1.0.
 */
public class Search32176000Action extends AbstractAction {

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32176000Action.class);

	public static final String ATTENDKINDJR_CODE_ATTEND = "00";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String doAction(ServletContext sc, HttpServletRequest request,
			HttpServletResponse response, SystemInfoBean sessionBean) {
		log.info("�y��ʁz�o�ȕ���(�����E�S�C����) �i�����jSTART");

		//-----FormBean����
		Search32176000FormBean searchFormBean = new Search32176000FormBean();
		String user = sessionBean.getUserCode();

		// ---------------------------------------------------
		// �p�����[�^�擾
		// ---------------------------------------------------
		// �\�����ɉ�ʂɕ\�����Ă����N�x�A�w�ȁA�N���擾����
		String init_nendo  = request.getParameter("init_nendo");
		String init_grade  = request.getParameter("init_grade");

		// �V�X�e�����t
		String today = DateUtility.getSystemDate();

		// �V�X�e�����t�������N�x�̔N�x���Ԕ͈͊O�ł���ꍇ�A�����N�x�؂�ւ�����Ă���Ɣ��f��
		// �����N���ɂ͔N�x�I�����̔N����ݒ肷��
		String yyyymm = "";
		String nendoStartYmd = sessionBean.getSystemNendoSeireki() + sessionBean.getSystemNendoStartDate().trim();
		String nendoEndYmd = DateUtility.getNendoEndDate(sessionBean.getSystemNendoSeireki(),
								sessionBean.getSystemNendoStartDate().trim(), sessionBean.getSystemNendoEndDate().trim());

		if (nendoStartYmd.compareTo(today) <= 0 && today.compareTo(nendoEndYmd) <= 0) {
			// �V�X�e�����t�������N�x��
			yyyymm = today.substring(0, 6);
		}else{
			yyyymm = nendoEndYmd.substring(0, 6);
		}

		String req_yyyymm     = request.getParameter("yyyymm");
		if (req_yyyymm == null){
			req_yyyymm = yyyymm;
		}

		String org_hmrclass = "";
		String org_hmrgrade = "";


		// �N���X�I���őI�������N���X���Z�b�V��������擾
		HroomKeyFormBean hroomKeyFormBean = sessionBean.getSelectHroomKey();

		if( hroomKeyFormBean == null ) {

			searchFormBean.setTran_mode("");

			// tbl_hroom ����ŏ��̃f�[�^���擾����B
			Detail32176000Service detail32176000Service = new Detail32176000Service();

			detail32176000Service.setSearch32176000FormBean(searchFormBean);

			// ���j���[����̏����\���̏ꍇ
			if (init_nendo == null) {

				detail32176000Service.setParameters(user, sessionBean.getSystemNendoSeireki() );
				detail32176000Service.execute();

				org_hmrclass = searchFormBean.getHmrclassAtFirst();
				org_hmrgrade = searchFormBean.getGradeAtFirst();

				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());

				searchFormBean.setGrade(org_hmrgrade);
				searchFormBean.setHmrclass(org_hmrclass);

				searchFormBean.setYyyymm(yyyymm);

			} else {

				// request�̓��e���R�s�[
				searchFormBean = (Search32176000FormBean)copyRequestParamToFormBean(request, searchFormBean);

				// �v���_�E���ύX�ɂ�郊���[�h�𔻒�
				// �N�x���ύX���ꂽ�ꍇ
				if (! searchFormBean.getNendo().equals(init_nendo)) {

					detail32176000Service.setParameters(user, searchFormBean.getNendo() );
					detail32176000Service.execute();

					org_hmrclass = searchFormBean.getHmrclassAtFirst();
					org_hmrgrade = searchFormBean.getGradeAtFirst();

					// �N��������
					searchFormBean.setGrade(org_hmrgrade);
					// �g��������
					searchFormBean.setHmrclass(org_hmrclass);
					// ����������
					searchFormBean.setYyyymm(yyyymm);

				// �w�N���ύX���ꂽ�ꍇ
				} else if (! searchFormBean.getGrade().equals(init_grade)) {

					detail32176000Service.setParameters(user, searchFormBean.getNendo() );
					detail32176000Service.execute();

					org_hmrclass = searchFormBean.getHmrclassAtFirst();
					// �g��������
					searchFormBean.setHmrclass(org_hmrclass);
					// ����������
					searchFormBean.setYyyymm(searchFormBean.getYyyymm());
				}
			}

		}else{
			searchFormBean.setTran_mode("CLS");

			searchFormBean.setNendo( sessionBean.getSystemNendoSeireki());
			searchFormBean.setGrade( hroomKeyFormBean.getGlade() );
			searchFormBean.setClsno( hroomKeyFormBean.getClsno() );
			searchFormBean.setHmrclass( hroomKeyFormBean.getHmrclass() );
			searchFormBean.setYyyymm(req_yyyymm);
		}



		// ---------------------------------------------------
		// ���I��pDropDown�̍쐬
		// ---------------------------------------------------
		AttUtility attUtility = new AttUtility();

		String start_date = sessionBean.getSystemNendoStartDate().trim();

		attUtility.setYear(searchFormBean.getNendo());
		attUtility.setMonth_format("");
		attUtility.setSelected_yyyymm(searchFormBean.getYyyymm() );
		attUtility.setStart_mmdd(start_date);
		attUtility.generatetNextYear();

		String dropdownform = "";

		try {
			dropdownform = attUtility.createDropDownForMonthByNendoStart();
		} catch (Exception e) {
			log.error("��O�����F�N�x�J�n��DropDown ",e);
		}
		searchFormBean.setDropDownForMonth(dropdownform);

		// ---------------------------------------------------
		// �o���敪�R�[�h�i�o�ȁF00�j�̓o�^�L���`�F�b�N
		// ---------------------------------------------------
		CheckRegist32176000Service service = new CheckRegist32176000Service();
		service.setParameters(sessionBean.getUserCode(), searchFormBean.getNendo(), ATTENDKINDJR_CODE_ATTEND );
		service.execute();
		int resultcount = service.getCount();

		if (resultcount == 0) {
			searchFormBean.setNodata("0");
		} else {
			searchFormBean.setNodata("1");
		}

		// ---------------------------------------------------
		// �Ώی��e�[�u������
		// ---------------------------------------------------
		// --- �p�����[�^�Z�b�g
		CreateMonthDataTable32176000Service  createListService = new CreateMonthDataTable32176000Service(user, searchFormBean.getNendo(),searchFormBean.getGrade(), searchFormBean.getHmrclass(), searchFormBean.getYyyymm());
		// --- �������s
		createListService.execute();
		String monthTableHtml = createListService.getMonthTableHtml();
		int monthDayCount = createListService.getMonthDayCount();
		searchFormBean.setMonthDayCount(monthDayCount);
		searchFormBean.setMonthTableHtml(monthTableHtml);

		// ---------------------------------------------------
		// �ːЁE�ʏ̑I���\�t���O �擾(cod_kind:606 code:007 cod_value2)
		// 0�F�g�p���Ȃ��@1:�g�p����(�ʏ̃f�t�H)�@2�F�g�p����(�ːЃf�t�H)
		// ---------------------------------------------------
		String outputNameFlg =null;
		String outputKanaFlg =null;
		// �p�����[�^�̐���
		FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(sessionBean.getUserCode(),606,"007");

		// �N�G���̎��s(List�`���Ŏ擾)
		findCodeService.execute();
		List<CodeEntity> wk_codeEntity = (List<CodeEntity>)findCodeService.getObj();

		for (CodeEntity entity : wk_codeEntity) {
			outputNameFlg = entity.getCod_value2();
			outputKanaFlg = entity.getCod_name2();
		}

		//��`���������A�擾�ł��Ȃ��ꍇ�͎g�p���Ȃ�:0���Z�b�g
		if(outputNameFlg == null){
			outputNameFlg = "0";
		}
		if(outputKanaFlg == null){
			outputKanaFlg = "0";
		}

		searchFormBean.setOutputNameFlg(outputNameFlg);
		searchFormBean.setOutputKanaFlg(outputKanaFlg);

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
