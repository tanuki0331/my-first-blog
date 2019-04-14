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
 * �o���Ȉꗗ�o�� -����Service.
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

	/** �����R�[�h */
	private String userCode;
	/** �Ώ۔N�x */
	private String nendo;
	/** �Ώۊw�N */
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
			// ���̈ꗗ�AsimpleTag ���쐬
			// ------------------------------------------------------------------------------------------

			// ���R���{�{�b�N�X�p�f�[�^(�`�F�b�N�{�b�N�X�p�j
			List<Search32172000_TargetMonthEntity> search32172000_TargetMonthEntityList = new ArrayList<Search32172000_TargetMonthEntity>();

			// ���R���{�{�b�N�X�p�f�[�^
			List<SimpleTagFormBean> simpleTagListMonth = new ArrayList<SimpleTagFormBean>();

			int int_startMonth = Integer.parseInt( sessionBean.getSystemNendoStartDate().substring(0,2));
			int int_endMonth   = Integer.parseInt( sessionBean.getSystemNendoEndDate().substring(0,2));

			for(int i = int_startMonth; i <= 12; i++){
				simpleTagListMonth.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));

				Search32172000_TargetMonthEntity search32172000_TargetMonthEntity = new Search32172000_TargetMonthEntity();
				search32172000_TargetMonthEntity.setMonth( String.valueOf(i) );
				search32172000_TargetMonthEntity.setMonth_name( String.valueOf(i)+"��" );
				search32172000_TargetMonthEntity.setYyyymm( nendo + String.format("%02d",i) );
				search32172000_TargetMonthEntityList.add(search32172000_TargetMonthEntity);
			}
			for(int i = 1; i <= int_endMonth; i++){
				simpleTagListMonth.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));

				Search32172000_TargetMonthEntity search32172000_TargetMonthEntity = new Search32172000_TargetMonthEntity();
				search32172000_TargetMonthEntity.setMonth( String.valueOf(i) );
				search32172000_TargetMonthEntity.setMonth_name( String.valueOf(i)+"��" );
				String next_year = String.valueOf( Integer.parseInt(nendo) + 1 );
				search32172000_TargetMonthEntity.setYyyymm( next_year + String.format("%02d",i) );
				search32172000_TargetMonthEntityList.add(search32172000_TargetMonthEntity);
			}
			searchFormBean.setMonthList(simpleTagListMonth);

			// ------------------------------------------------------------------------------------------
			// ���R���{�{�b�N�X�p�f�[�^
			// ------------------------------------------------------------------------------------------
			List<SimpleTagFormBean> simpleTagListDay = new ArrayList<SimpleTagFormBean>();

			for(int i = 1; i <= 31; i++){
				simpleTagListDay.add(new SimpleTagFormBean(String.format("%02d",i), String.format("%02d",i)));
			}
			searchFormBean.setDayList(simpleTagListDay);

			// ------------------------------------------------------------------------------------------
			// �w���p�f�[�^
			// ------------------------------------------------------------------------------------------
			Object[] param = {userCode, nendo };
			QueryManager queryManager = new QueryManager("common/getSemesterByUserAndYear.sql", param, SemesterEntity.class);

			List<SemesterEntity> semesterEntityList = (List<SemesterEntity>) this.executeQuery(queryManager);

			List<SimpleTagFormBean> simpleTagListSemester = new ArrayList<SimpleTagFormBean>();

			if(!semesterEntityList.isEmpty()){

				// �w���v���_�E�����쐬����
				for( SemesterEntity semesterEntity:semesterEntityList){
					simpleTagListSemester.add(new SimpleTagFormBean( semesterEntity.getSem_code(), semesterEntity.getSem_name() ) ) ;
				}

				// �w�肳�ꂽ�w���̊J�n���A�I�������擾����B
				int int_startYYYYMM = 0;
				int int_endYYYYMM   = 0;

				if( searchFormBean.getSemCode().equals("") ){
					// �w���̎w�肪�����ꍇ�́A�擪��I��
					int_startYYYYMM = Integer.parseInt( semesterEntityList.get(0).getSem_start().substring(0, 6));
					int_endYYYYMM   = Integer.parseInt( semesterEntityList.get(0).getSem_end().substring(0, 6));

					searchFormBean.setSemStart(semesterEntityList.get(0).getSem_start());
					searchFormBean.setSemEnd(semesterEntityList.get(0).getSem_end());
					searchFormBean.setSemName(semesterEntityList.get(0).getSem_name());
					searchFormBean.setSemCode(semesterEntityList.get(0).getSem_code());
				}else{
					// �w���̎w�肪����ꍇ
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

				// �擾�����w���̊J�n�A�I��������ΏۂƂȂ錎��I��
				int count = 0;
				for( Search32172000_TargetMonthEntity search32172000_TargetMonthEntity:search32172000_TargetMonthEntityList){

					int int_targetYYYYMM = Integer.parseInt(search32172000_TargetMonthEntity.getYyyymm());
					String[] checkedTargetMonth = searchFormBean.getCheckedTargetMonth();

					// �Ώی��̏ꍇ
					if( int_startYYYYMM <= int_targetYYYYMM && int_targetYYYYMM <= int_endYYYYMM  ){

						// �Ώی��̃`�F�b�N�{�b�N�X��Enable�Ƃ���
						search32172000_TargetMonthEntity.setDisabled("0");

						// �����[�h���A�����J�ڂ��𔻒�B
						if( searchFormBean.getReload().equals("1")){

							// �����[�h���ŔN�x�A�w�N�̕ύX�������ꍇ�A�`�F�b�N��Ԃ�ێ�����B
							if( searchFormBean.getReload_changed().equals("")){

								// �`�F�b�N�L��
								if( !checkedTargetMonth[count].equals("") ){
									search32172000_TargetMonthEntity.setChecked("1");
								}
								// �`�F�b�N�Ȃ�
								else{
									search32172000_TargetMonthEntity.setChecked("0");
								}
							}
							// �����[�h���ɔN�x�A�w�N�̕ύX���L��ꍇ�́A�������ŊY���w����S�ă`�F�b�N
							else{
								search32172000_TargetMonthEntity.setChecked("1");
							}
						}
						// �����J�ڂ̏ꍇ
						else{
							search32172000_TargetMonthEntity.setChecked("1");
						}
					}
					// �Ώی��Ŗ����ꍇ
					else{
						// �`�F�b�N�{�b�N�X��Disable�Ƃ���
						search32172000_TargetMonthEntity.setDisabled("1");
						search32172000_TargetMonthEntity.setChecked("0");
					}
					count ++ ;
				}
			}
			// �w�����X�g��ݒ�
			searchFormBean.setSemesterEntityList(semesterEntityList);
			// �w���v���_�E�����X�g��ݒ�
			searchFormBean.setSemesterList(simpleTagListSemester);
			// �o�͌��̐ݒ�
			searchFormBean.setTargetMonthList(search32172000_TargetMonthEntityList);

			// ------------------------------------------------------------------------------------------
			// �ΏۃN���X�擾
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
			log.error("�o���Ȉꗗ�o�� DB�擾�����Ɏ��s���܂����B", e);
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
	 * @param Search32172000FormBean �Z�b�g���� Search32172000FormBean
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
