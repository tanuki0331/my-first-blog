/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2019 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.ArrayList;
import java.util.List;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.db.entity.SemesterEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.common.formbean.SimpleTagFormBean;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_TargetMonthEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32172000FormBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * <PRE>
 * 出欠席一覧出力 -検索Service.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Search32172000Service extends AbstractExecuteQuery{

	/** log4j */
	private static final Log log = LogFactory.getLog(Search32172000Service.class);

	/** 所属コード */
	private String userCode;
	/** 対象年度 */
	private String nendo;
	/** 対象学年 */
	private String grade;

	private SystemInfoBean sessionBean;

	private Search32172000FormBean searchFormBean;


	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doQuery() throws TnaviDbException {

		try {

			// ------------------------------------------------------------------------------------------
			// 月の一覧、simpleTag を作成
			// ------------------------------------------------------------------------------------------

			// 月コンボボックス用データ(チェックボックス用）
			List<Search32172000_TargetMonthEntity> search32172000_TargetMonthEntityList = new ArrayList<Search32172000_TargetMonthEntity>();

			// 月コンボボックス用データ
			List<SimpleTagFormBean> simpleTagListMonth = new ArrayList<SimpleTagFormBean>();

			int int_startMonth = Integer.parseInt( sessionBean.getSystemNendoStartDate().substring(0,2));
			int int_endMonth   = Integer.parseInt( sessionBean.getSystemNendoEndDate().substring(0,2));

			for(int i = int_startMonth; i <= 12; i++){
				simpleTagListMonth.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));

				Search32172000_TargetMonthEntity search32172000_TargetMonthEntity = new Search32172000_TargetMonthEntity();
				search32172000_TargetMonthEntity.setMonth( String.valueOf(i) );
				search32172000_TargetMonthEntity.setMonth_name( String.valueOf(i)+"月" );
				search32172000_TargetMonthEntity.setYyyymm( nendo + String.format("%02d",i) );
				search32172000_TargetMonthEntityList.add(search32172000_TargetMonthEntity);
			}
			for(int i = 1; i <= int_endMonth; i++){
				simpleTagListMonth.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));

				Search32172000_TargetMonthEntity search32172000_TargetMonthEntity = new Search32172000_TargetMonthEntity();
				search32172000_TargetMonthEntity.setMonth( String.valueOf(i) );
				search32172000_TargetMonthEntity.setMonth_name( String.valueOf(i)+"月" );
				String next_year = String.valueOf( Integer.parseInt(nendo) + 1 );
				search32172000_TargetMonthEntity.setYyyymm( next_year + String.format("%02d",i) );
				search32172000_TargetMonthEntityList.add(search32172000_TargetMonthEntity);
			}
			searchFormBean.setMonthList(simpleTagListMonth);

			// ------------------------------------------------------------------------------------------
			// 日コンボボックス用データ
			// ------------------------------------------------------------------------------------------
			List<SimpleTagFormBean> simpleTagListDay = new ArrayList<SimpleTagFormBean>();

			for(int i = 1; i <= 31; i++){
				simpleTagListDay.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));
			}
			searchFormBean.setDayList(simpleTagListDay);

			// ------------------------------------------------------------------------------------------
			// 学期用データ
			// ------------------------------------------------------------------------------------------
			Object[] param = {userCode, nendo };
			QueryManager queryManager = new QueryManager("common/getSemesterByUserAndYear.sql", param, SemesterEntity.class);

			List<SemesterEntity> semesterEntityList = (List<SemesterEntity>) this.executeQuery(queryManager);

			List<SimpleTagFormBean> simpleTagListSemester = new ArrayList<SimpleTagFormBean>();

			if(!semesterEntityList.isEmpty()){

				// 学期プルダウンを作成する
				for( SemesterEntity semesterEntity:semesterEntityList){
					simpleTagListSemester.add(new SimpleTagFormBean( semesterEntity.getSem_code(), semesterEntity.getSem_name() ) ) ;
				}

				// 指定された学期の開始日、終了日を取得する。
				int int_startYYYYMM = 0;
				int int_endYYYYMM   = 0;

				if( searchFormBean.getSemCode().equals("") ){
					// 学期の指定が無い場合は、先頭を選択
					int_startYYYYMM = Integer.parseInt( semesterEntityList.get(0).getSem_start().substring(0, 6));
					int_endYYYYMM   = Integer.parseInt( semesterEntityList.get(0).getSem_end().substring(0, 6));

					searchFormBean.setSemStart(semesterEntityList.get(0).getSem_start());
					searchFormBean.setSemEnd(semesterEntityList.get(0).getSem_end());
					searchFormBean.setSemName(semesterEntityList.get(0).getSem_name());
					searchFormBean.setSemCode(semesterEntityList.get(0).getSem_code());
				}else{
					// 学期の指定がある場合
					for( SemesterEntity semesterEntity:semesterEntityList){

						if( semesterEntity.getSem_code().equals(searchFormBean.getSemCode()) ) {
							int_startYYYYMM = Integer.parseInt( semesterEntity.getSem_start().substring(0, 6));
							int_endYYYYMM   = Integer.parseInt( semesterEntity.getSem_end().substring(0, 6));
							searchFormBean.setSemStart(semesterEntity.getSem_start());
							searchFormBean.setSemEnd(semesterEntity.getSem_end());
							searchFormBean.setSemName(semesterEntity.getSem_name());
							searchFormBean.setSemCode(semesterEntity.getSem_code());
							break;
						}
					}
				}

				// 取得した学期の開始、終了日から対象となる月を選択
				int count = 0;
				for( Search32172000_TargetMonthEntity search32172000_TargetMonthEntity:search32172000_TargetMonthEntityList){

					int int_targetYYYYMM = Integer.parseInt(search32172000_TargetMonthEntity.getYyyymm());
					String[] checkedTargetMonth = searchFormBean.getCheckedTargetMonth();

					// 対象月の場合
					if( int_startYYYYMM <= int_targetYYYYMM && int_targetYYYYMM <= int_endYYYYMM  ){

						// 対象月のチェックボックスはEnableとする
						search32172000_TargetMonthEntity.setDisabled("0");

						// リロードか、初期遷移かを判定。
						if( searchFormBean.getReload().equals("1")){

							// リロード時で年度、学年の変更が無い場合、チェック状態を保持する。
							if( searchFormBean.getReload_changed().equals("")){

								// チェック有り
								if( !checkedTargetMonth[count].equals("") ){
									search32172000_TargetMonthEntity.setChecked("1");
								}
								// チェックなし
								else{
									search32172000_TargetMonthEntity.setChecked("0");
								}
							}
							// リロード時に年度、学年の変更が有る場合は、初期化で該当学期を全てチェック
							else{
								search32172000_TargetMonthEntity.setChecked("1");
							}
						}
						// 初期遷移の場合
						else{
							search32172000_TargetMonthEntity.setChecked("1");
						}
					}
					// 対象月で無い場合
					else{
						// チェックボックスはDisableとする
						search32172000_TargetMonthEntity.setDisabled("1");
						search32172000_TargetMonthEntity.setChecked("0");
					}
					count ++ ;
				}
			}
			// 学期リストを設定
			searchFormBean.setSemesterEntityList(semesterEntityList);
			// 学期プルダウンリストを設定
			searchFormBean.setSemesterList(simpleTagListSemester);
			// 出力月の設定
			searchFormBean.setTargetMonthList(search32172000_TargetMonthEntityList);

			// ------------------------------------------------------------------------------------------
			// 対象クラス取得
			// ------------------------------------------------------------------------------------------
			param = new Object[]{ userCode, nendo, grade };
			queryManager = new QueryManager("cus/oyama/getData32172000_classList.sql", param, HRoomEntity.class);
			List<HRoomEntity>  hRoomEntityList = (List<HRoomEntity>) this.executeQuery(queryManager);

			List<SimpleTagFormBean> simpleTagListClass = new ArrayList<SimpleTagFormBean>();

			if(!hRoomEntityList.isEmpty()){
				for( HRoomEntity hRoomEntity:hRoomEntityList){
					simpleTagListClass.add(new SimpleTagFormBean( hRoomEntity.getHmr_clsno(), hRoomEntity.getHmr_class() ) ) ;
				}
			}
			searchFormBean.setClassList(simpleTagListClass);


		} catch (Exception e) {
			log.error("出欠席一覧出力 DB取得処理に失敗しました。", e);
			throw new TnaviException(e);
		}
	}

	public void setParameters( SystemInfoBean sessionBean, String nendo, String grade ) {
		this.sessionBean = sessionBean;
		this.userCode = sessionBean.getUserCode();
		this.nendo = nendo;
		this.grade = grade;
	}

	/**
	 * @param Search32172000FormBean セットする Search32172000FormBean
	 */
	public void setSearchFormBean(Search32172000FormBean searcFormBean) {
		this.searchFormBean = searcFormBean;
	}

	/**
	 * @return Search32172000FormBean
	 */
	public Search32172000FormBean getSearchFormBean() {
		return searchFormBean;
	}

}
