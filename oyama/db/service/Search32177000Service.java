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
 * 出席簿印刷(特別支援学級　小山市用) 教務・担任共通 検索 Service.
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

	/** クラス選択画面で選択したホームルーム情報 */
	private HroomKeyFormBean hroomKey;

	/** 特別支援学級選択画面で選択した学級グループ情報 */
	private SpClassKeyFormBean spClassKey;

	public Search32177000Service(HttpServletRequest request, SystemInfoBean sessionBean, Search32177000FormBean searchFormBean) {
		super(request, sessionBean, searchFormBean, searchFormBean.getClassType());

		hroomKey = sessionBean.getSelectHroomKey();
		spClassKey = sessionBean.getSelectSpClassKey();

		if(hroomKey == null && spClassKey == null){
			// 教務メニューからの遷移
			searchFormBean.setTranMode(AttPrintSearchFormBeanImpl.TRAN_MODE_KYOMU);
		}


		// 年度未設定の場合は初期値をセットする
		if(StringUtils.isEmpty(searchFormBean.getNendo())){

			if(hroomKey != null){
				// 学級担任メニューからの遷移
				searchFormBean.setNendo(sessionBean.getSystemNendoSeireki());
				searchFormBean.setGrade(hroomKey.getGlade());
				searchFormBean.setHmrclass(hroomKey.getHmrclass());
			}else if(spClassKey != null){
				// 特別支援メニューからの遷移
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
		// 特別支援学級
		// -----------------------------------------------------------------------
		if(isSpClass()){

			// 特別支援グループセレクトボックス用リストをセット
			searchFormBean.setGroupSelectboxList(getSpGroupList(sessionBean.getUserCode(), searchFormBean.getNendo()));

			// 特別支援グループコード未設定の場合、セレクトボックスの先頭の値をセット
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

			// 休業日基準学年セレクトボックス用リストをセット
			searchFormBean.setHolidayGradeSelectboxList(getSpGroupGradeList(sessionBean.getUserCode(), searchFormBean.getNendo(), searchFormBean.getGroupcode()));

			// 休業日基準学年未設定の場合、セレクトボックスの先頭の値をセット
			if(StringUtils.isEmpty(searchFormBean.getHolidayGrade())){
				if(searchFormBean.getHolidayGradeSelectboxList().size() > 0){
					searchFormBean.setHolidayGrade(searchFormBean.getHolidayGradeSelectboxList().get(0).getCode());
				}
			}
		}
		// -----------------------------------------------------------------------
		// 複式学級学級
		// -----------------------------------------------------------------------
/*		else if(isPlClass()){

			// 特別支援グループセレクトボックス用リストをセット
			searchFormBean.setGroupSelectboxList(getPlGroupList(sessionBean.getUserCode(), searchFormBean.getNendo()));

			// 特別支援グループコード未設定の場合、セレクトボックスの先頭の値をセット
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

			// 休業日基準学年セレクトボックス用リストをセット
			searchFormBean.setHolidayGradeSelectboxList(getPlGroupGradeList(sessionBean.getUserCode(), searchFormBean.getNendo(), searchFormBean.getGroupcode()));

			// 休業日基準学年未設定の場合、セレクトボックスの先頭の値をセット
			if(StringUtils.isEmpty(searchFormBean.getHolidayGrade())){
				if(searchFormBean.getHolidayGradeSelectboxList().size() > 0){
					searchFormBean.setHolidayGrade(searchFormBean.getHolidayGradeSelectboxList().get(0).getCode());
				}
			}
		}
*/


		// 月情報を取得・セット
		searchFormBean.setDayMap(getDayMap(getMstcodeSetting()));
		String yyyymm = strMonth(sessionBean,searchFormBean.getNendo(), searchFormBean.getMonth());
		searchFormBean.setYyyymm(yyyymm);

	}

	public Search32177000FormBean getSearchFormBean() {
		return searchFormBean;
	}

	/**
	 * 対象年月を取得する
	 * @param sessionBean システム情報
	 * @param nendo 年度
	 * @param month 月
	 * @return 対象月の末日(YYYYMMDD)
	 */
	private String strMonth(SystemInfoBean sessionBean, String nendo, String month){
		Calendar calendar = getCalendar(sessionBean, nendo, month, "01");

		return String.format("%04d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
	}

	/**
	 * 年度内の最小学年を取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @return 学年
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
	 * 検索用ホームループのリストを取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @param grade 学年
	 * @return 検索用ホームルームリスト
	 */
	private List<SimpleTagFormBean> getHroomList(String userCode, String nendo, String grade){

		// カスタムタグ「ClassCodeTag」は画面遷移のオプションがないため、
		// ClassCodeTagのサービスを使用して取得したデータをSimpleTag用のリストに詰め替える

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
	 * 検索用特別支援グループのリストを取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @return 検索用ホームルームリスト
	 */
	private List<SimpleTagFormBean> getSpGroupList(String userCode, String nendo){

		// カスタムタグ「ClassCodeTag」は画面遷移のオプションがないため、
		// ClassCodeTagのサービスを使用して取得したデータをSimpleTag用のリストに詰め替える

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
	 * 複式学級グループのリストを取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @return 検索用ホームルームリスト
	 */
/*	private List<SimpleTagFormBean> getPlGroupList(String userCode, String nendo){

		// カスタムタグ「ClassCodeTag」は画面遷移のオプションがないため、
		// ClassCodeTagのサービスを使用して取得したデータをSimpleTag用のリストに詰め替える

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
	 * 特別支援学級の休業日基準学年用セレクトボックスのリストを取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @param spGroupCode グループコード
	 * @return 学年リスト
	 */
	private List<SimpleTagFormBean> getSpGroupGradeList(String userCode, String nendo, String spGroupCode){

		// カスタムタグ「ClassCodeTag」は画面遷移のオプションがないため、
		// ClassCodeTagのサービスを使用して取得したデータをSimpleTag用のリストに詰め替える

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
	 * 複式学級学級の休業日基準学年用セレクトボックスのリストを取得します。
	 * @param userCode 所属コード
	 * @param nendo 年度
	 * @param spGroupCode グループコード
	 * @return 学年リスト
	 */
/*	private List<SimpleTagFormBean> getPlGroupGradeList(String userCode, String nendo, String groupCode){

		// カスタムタグ「ClassCodeTag」は画面遷移のオプションがないため、
		// ClassCodeTagのサービスを使用して取得したデータをSimpleTag用のリストに詰め替える

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
	 * 汎用コードマスタから出席簿印刷設定を取得する
	 * @return 汎用コードマスタデータ
	 */
	private CodeEntity getMstcodeSetting(){

		FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(sessionBean.getUserCode(), 606, "007");

		// クエリの実行(List形式で取得)
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
