/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2017 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.att.db.service.AbstractAttPrintSearchService;
import jp.co.systemd.tnavi.att.formbean.AttPrintSearchFormBeanImpl;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.ClassEntity;
import jp.co.systemd.tnavi.common.db.entity.CodeEntity;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.db.entity.SpclassGroupEntity;
import jp.co.systemd.tnavi.common.db.service.ClassCodeTagService;
import jp.co.systemd.tnavi.common.db.service.FindCodeByUserAndKindAndCodeService;
import jp.co.systemd.tnavi.common.db.service.SpGropupCodeTagService;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.formbean.HroomKeyFormBean;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.common.formbean.SpClassKeyFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32177000FormBean;


/**
 * <PRE>
 * �o�ȕ���(���ʎx���w���@���R�s�p) �����E�S�C���� ���� Service.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Search32177000Service extends AbstractAttPrintSearchService{

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32177000Service.class);

	private Search32177000FormBean searchFormBean;

	/** �N���X�I����ʂőI�������z�[�����[����� */
	private HroomKeyFormBean hroomKey;

	/** ���ʎx���w���I����ʂőI�������w���O���[�v��� */
	private SpClassKeyFormBean spClassKey;

	public Search32177000Service(HttpServletRequest request, SystemInfoBean sessionBean, Search32177000FormBean searchFormBean) {
		super(request, sessionBean, searchFormBean, searchFormBean.getClassType());

		hroomKey = sessionBean.getSelectHroomKey();
		spClassKey = sessionBean.getSelectSpClassKey();

		if(hroomKey == null && spClassKey == null){
			// �������j���[����̑J��
			searchFormBean.setTranMode(AttPrintSearchFormBeanImpl.TRAN_MODE_KYOMU);
		}


		// �N�x���ݒ�̏ꍇ�͏����l���Z�b�g����
		if(StringUtils.isEmpty(searchFormBean.getNendo())){

			if(hroomKey != null){
				// �w���S�C���j���[����̑J��
				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());
				searchFormBean.setGrade(hroomKey.getGlade());
				searchFormBean.setHmrclass(hroomKey.getHmrclass());
			}else if(spClassKey != null){
				// ���ʎx�����j���[����̑J��
				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());
				searchFormBean.setGroupcode(spClassKey.getSpgcode());

			}else{
				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());
			}
		}

		this.searchFormBean = searchFormBean;
		this.sessionBean = sessionBean;

	}


	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}


	@Override
	protected void doQuery() throws TnaviDbException {
		super.doQuery();

		// -----------------------------------------------------------------------
		// ���ʎx���w��
		// -----------------------------------------------------------------------
		if(isSpClass()){

			// ���ʎx���O���[�v�Z���N�g�{�b�N�X�p���X�g���Z�b�g
			searchFormBean.setGroupSelectboxList(getSpGroupList(sessionBean.getUserCode(), searchFormBean.getNendo()));

			// ���ʎx���O���[�v�R�[�h���ݒ�̏ꍇ�A�Z���N�g�{�b�N�X�̐擪�̒l���Z�b�g
			if(StringUtils.isEmpty(searchFormBean.getGroupcode())){
				if(searchFormBean.getGroupSelectboxList().size() > 0){
					searchFormBean.setGroupcode(searchFormBean.getGroupSelectboxList().get(0).getCode());
					searchFormBean.setGroupname(searchFormBean.getGroupSelectboxList().get(0).getName());
				}
			}else {
				for(SimpleTagFormBean group : searchFormBean.getGroupSelectboxList()) {
					if(searchFormBean.getGroupcode().equals(group.getCode())) {
						searchFormBean.setGroupname(group.getName());

					}
				}
			}

			// �x�Ɠ���w�N�Z���N�g�{�b�N�X�p���X�g���Z�b�g
			searchFormBean.setHolidayGradeSelectboxList(getSpGroupGradeList(sessionBean.getUserCode(), searchFormBean.getNendo(), searchFormBean.getGroupcode()));

			// �x�Ɠ���w�N���ݒ�̏ꍇ�A�Z���N�g�{�b�N�X�̐擪�̒l���Z�b�g
			if(StringUtils.isEmpty(searchFormBean.getHolidayGrade())){
				if(searchFormBean.getHolidayGradeSelectboxList().size() > 0){
					searchFormBean.setHolidayGrade(searchFormBean.getHolidayGradeSelectboxList().get(0).getCode());
				}
			}
		}
		// -----------------------------------------------------------------------
		// �����w���w��
		// -----------------------------------------------------------------------
/*		else if(isPlClass()){

			// ���ʎx���O���[�v�Z���N�g�{�b�N�X�p���X�g���Z�b�g
			searchFormBean.setGroupSelectboxList(getPlGroupList(sessionBean.getUserCode(), searchFormBean.getNendo()));

			// ���ʎx���O���[�v�R�[�h���ݒ�̏ꍇ�A�Z���N�g�{�b�N�X�̐擪�̒l���Z�b�g
			if(StringUtils.isEmpty(searchFormBean.getGroupcode())){
				if(searchFormBean.getGroupSelectboxList().size() > 0){
					searchFormBean.setGroupcode(searchFormBean.getGroupSelectboxList().get(0).getCode());
					searchFormBean.setGroupname(searchFormBean.getGroupSelectboxList().get(0).getName());
				}
			}else {
				for(SimpleTagFormBean group : searchFormBean.getGroupSelectboxList()) {
					if(searchFormBean.getGroupcode().equals(group.getCode())) {
						searchFormBean.setGroupname(group.getName());

					}
				}
			}

			// �x�Ɠ���w�N�Z���N�g�{�b�N�X�p���X�g���Z�b�g
			searchFormBean.setHolidayGradeSelectboxList(getPlGroupGradeList(sessionBean.getUserCode(), searchFormBean.getNendo(), searchFormBean.getGroupcode()));

			// �x�Ɠ���w�N���ݒ�̏ꍇ�A�Z���N�g�{�b�N�X�̐擪�̒l���Z�b�g
			if(StringUtils.isEmpty(searchFormBean.getHolidayGrade())){
				if(searchFormBean.getHolidayGradeSelectboxList().size() > 0){
					searchFormBean.setHolidayGrade(searchFormBean.getHolidayGradeSelectboxList().get(0).getCode());
				}
			}
		}
*/


		// �������擾�E�Z�b�g
		searchFormBean.setDayMap(getDayMap(getMstcodeSetting()));
		String yyyymm = strMonth(sessionBean,searchFormBean.getNendo(), searchFormBean.getMonth());
		searchFormBean.setYyyymm(yyyymm);

	}

	public Search32177000FormBean getSearchFormBean() {
		return searchFormBean;
	}

	/**
	 * �Ώ۔N�����擾����
	 * @param sessionBean �V�X�e�����
	 * @param nendo �N�x
	 * @param month ��
	 * @return �Ώی��̖���(YYYYMMDD)
	 */
	private String strMonth(SystemInfoBean sessionBean, String nendo, String month){
		Calendar calendar = getCalendar(sessionBean, nendo, month, "01");

		return String.format("%04d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * �N�x���̍ŏ��w�N���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @return �w�N
	 */
	private String getFirstGradeInTheYear(String userCode, String nendo){

		QueryManager qm = new QueryManager("common/getHroomByUserAndYear.sql", new Object[]{userCode, nendo }, HRoomEntity.class);

		@SuppressWarnings("unchecked")
		List<HRoomEntity> hroomList = (List<HRoomEntity>) this.executeQuery(qm);

		if(hroomList.size() > 0){
			return hroomList.get(0).getHmr_glade();
		}

		return "";
	}

	/**
	 * �����p�z�[�����[�v�̃��X�g���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @param grade �w�N
	 * @return �����p�z�[�����[�����X�g
	 */
	private List<SimpleTagFormBean> getHroomList(String userCode, String nendo, String grade){

		// �J�X�^���^�O�uClassCodeTag�v�͉�ʑJ�ڂ̃I�v�V�������Ȃ����߁A
		// ClassCodeTag�̃T�[�r�X���g�p���Ď擾�����f�[�^��SimpleTag�p�̃��X�g�ɋl�ߑւ���

		ClassCodeTagService classCodeTagService = new ClassCodeTagService(userCode, nendo, grade);
		classCodeTagService.execute();

		@SuppressWarnings("unchecked")
		List<HRoomEntity> hRoomEntityList = (List<HRoomEntity>)classCodeTagService.getObj();

		List<SimpleTagFormBean> hRoomList = new ArrayList<SimpleTagFormBean>();
		for (HRoomEntity hRoomEntity : hRoomEntityList) {
			hRoomList.add(new SimpleTagFormBean(hRoomEntity.getHmr_class(), hRoomEntity.getHmr_class()));
		}

		return hRoomList;
	}

	/**
	 * �����p���ʎx���O���[�v�̃��X�g���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @return �����p�z�[�����[�����X�g
	 */
	private List<SimpleTagFormBean> getSpGroupList(String userCode, String nendo){

		// �J�X�^���^�O�uClassCodeTag�v�͉�ʑJ�ڂ̃I�v�V�������Ȃ����߁A
		// ClassCodeTag�̃T�[�r�X���g�p���Ď擾�����f�[�^��SimpleTag�p�̃��X�g�ɋl�ߑւ���

		SpGropupCodeTagService spGropupCodeTagService = new SpGropupCodeTagService(userCode, nendo);
		spGropupCodeTagService.execute();

		@SuppressWarnings("unchecked")
		List<SpclassGroupEntity> spclassGroupEntityList = (List<SpclassGroupEntity>)spGropupCodeTagService.getObj();

		List<SimpleTagFormBean> spGroupList = new ArrayList<SimpleTagFormBean>();
		for (SpclassGroupEntity spclassGroupEntity : spclassGroupEntityList) {
			spGroupList.add(new SimpleTagFormBean(spclassGroupEntity.getSpg_code(), spclassGroupEntity.getSpg_name()));
		}

		return spGroupList;
	}

	/**
	 * �����w���O���[�v�̃��X�g���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @return �����p�z�[�����[�����X�g
	 */
/*	private List<SimpleTagFormBean> getPlGroupList(String userCode, String nendo){

		// �J�X�^���^�O�uClassCodeTag�v�͉�ʑJ�ڂ̃I�v�V�������Ȃ����߁A
		// ClassCodeTag�̃T�[�r�X���g�p���Ď擾�����f�[�^��SimpleTag�p�̃��X�g�ɋl�ߑւ���

		PluClassGropupCodeTagService pluClassGropupCodeTagService = new PluClassGropupCodeTagService(userCode, nendo);
		pluClassGropupCodeTagService.execute();

		@SuppressWarnings("unchecked")
		List<PluclassGroupEntity> pluClassGroupEntityList = (List<PluclassGroupEntity>)pluClassGropupCodeTagService.getObj();

		List<SimpleTagFormBean> plGroupList = new ArrayList<SimpleTagFormBean>();
		for (PluclassGroupEntity spclassGroupEntity : pluClassGroupEntityList) {
			plGroupList.add(new SimpleTagFormBean(spclassGroupEntity.getPlg_code(), spclassGroupEntity.getPlg_name()));
		}

		return plGroupList;
	}
*/
	/**
	 * ���ʎx���w���̋x�Ɠ���w�N�p�Z���N�g�{�b�N�X�̃��X�g���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @param spGroupCode �O���[�v�R�[�h
	 * @return �w�N���X�g
	 */
	private List<SimpleTagFormBean> getSpGroupGradeList(String userCode, String nendo, String spGroupCode){

		// �J�X�^���^�O�uClassCodeTag�v�͉�ʑJ�ڂ̃I�v�V�������Ȃ����߁A
		// ClassCodeTag�̃T�[�r�X���g�p���Ď擾�����f�[�^��SimpleTag�p�̃��X�g�ɋl�ߑւ���

		Object[] spGroupParam = new Object[]{userCode, nendo, spGroupCode} ;

		QueryManager qm = new QueryManager("cus/oyama/getPrint32177000SpGroupGrade.sql", spGroupParam, ClassEntity.class);

		@SuppressWarnings("unchecked")
		List<ClassEntity> spGroupClassEntityList = (List<ClassEntity>) this.executeQuery(qm);

		List<SimpleTagFormBean> spGroupGradeList = new ArrayList<SimpleTagFormBean>();
		for (int i = 0; i < spGroupClassEntityList.size(); i++) {
			String grade = spGroupClassEntityList.get(i).getCls_glade();
			spGroupGradeList.add(new SimpleTagFormBean(grade, grade));
		}

		return spGroupGradeList;
	}

	/**
	 * �����w���w���̋x�Ɠ���w�N�p�Z���N�g�{�b�N�X�̃��X�g���擾���܂��B
	 * @param userCode �����R�[�h
	 * @param nendo �N�x
	 * @param spGroupCode �O���[�v�R�[�h
	 * @return �w�N���X�g
	 */
/*	private List<SimpleTagFormBean> getPlGroupGradeList(String userCode, String nendo, String groupCode){

		// �J�X�^���^�O�uClassCodeTag�v�͉�ʑJ�ڂ̃I�v�V�������Ȃ����߁A
		// ClassCodeTag�̃T�[�r�X���g�p���Ď擾�����f�[�^��SimpleTag�p�̃��X�g�ɋl�ߑւ���

		Object[] groupParam = new Object[]{userCode, nendo, groupCode} ;

		QueryManager qm = new QueryManager("att/getAttPrintPluGroupGrade.sql", groupParam, HroomEntity.class);

		@SuppressWarnings("unchecked")
		List<HroomEntity> groupClassEntityList = (List<HroomEntity>) this.executeQuery(qm);

		List<SimpleTagFormBean> groupGradeList = new ArrayList<SimpleTagFormBean>();
		for (int i = 0; i < groupClassEntityList.size(); i++) {
			String grade = groupClassEntityList.get(i).getHmr_glade();
			groupGradeList.add(new SimpleTagFormBean(grade, grade));
		}

		return groupGradeList;
	}
*/

	/**
	 * �ėp�R�[�h�}�X�^����o�ȕ����ݒ���擾����
	 * @return �ėp�R�[�h�}�X�^�f�[�^
	 */
	private CodeEntity getMstcodeSetting(){

		FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(sessionBean.getUserCode(), 606, "007");

		// �N�G���̎��s(List�`���Ŏ擾)
		findCodeService.execute();
		@SuppressWarnings("unchecked")
		List<CodeEntity> codeEntityList = (List<CodeEntity>)findCodeService.getObj();

		if(codeEntityList.size() > 0){
			return codeEntityList.get(0);
		}else{
			return new CodeEntity();
		}
	}

}
