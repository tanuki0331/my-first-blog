/**------------------------------------------------------------**
 * Te@cherNavi
 * Copyright(C) 2016 System D, Inc. All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.common.exception.TnaviException;
import jp.co.systemd.tnavi.cus.oyama.formbean.Search32176000FormBean;

/**
 * <PRE>
 * HRoom マスター データ取得 Service.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Detail32176000Service extends AbstractExecuteQuery{

	/** log4j */
	private static final Log log = LogFactory.getLog(Detail32176000Service.class);

	/** 所属コード */
	private String user ="";

	/** 年度 */
	private String nendo ="";

	/**FormBean**/
	private Search32176000FormBean search32176000FormBean;

	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doQuery() throws TnaviDbException {

		//プリペアドステートメントの用意
		QueryManager qm = null;

		try {

			Object[] param = new Object[]{user, nendo } ;

			qm = new QueryManager("cus/oyama/getHroomAtFirst.sql", param, HRoomEntity.class);

			List<HRoomEntity> hRoomEntityList = (List<HRoomEntity>) this.executeQuery(qm);

			for (HRoomEntity entity : hRoomEntityList) {
				search32176000FormBean.setGradeAtFirst( entity.getHmr_glade() );
				search32176000FormBean.setHmrclassAtFirst( entity.getHmr_class() );
			}

		} catch (Exception e) {
			// ロールバック
			log.error(e.getLocalizedMessage(), e);
			throw new TnaviException(e);
		}finally{
			if(qm != null){
				qm = null;
			}
		}
	}

	public void setParameters(String user,String nendo ) {
		this.user = user;
		this.nendo = nendo;
	}

	public Search32176000FormBean getSearch32176000FormBean() {
		return search32176000FormBean;
	}

	public void setSearch32176000FormBean(Search32176000FormBean search32176000FormBean) {
		this.search32176000FormBean = search32176000FormBean;
	}
}
