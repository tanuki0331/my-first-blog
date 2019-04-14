package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.junior.mst.db.entity.AttendkindJrEntity;

/**
 * <PRE>
 * 出欠区分マスタ　コード存在チェックService.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 * @author System D, Inc.
 * @since 1.0.
 */
public class CheckRegist32176000Service extends AbstractExecuteQuery {
	/** log4j */
	private static final Log log = LogFactory.getLog(CheckRegist32176000Service.class);

	/** SQLストリング */
	private static final String SQL_FILE = "cus/oyama/getAttednKind32176000.sql";

	/** 所属コード */
	protected String user;

	/** 年度 **/
	protected String nendo;

	/** 出欠種別コード **/
	protected String code;

	/** クエリ関連 */
	protected Object[] paramObj = null;
	protected QueryManager query = null;

	/** 戻り数 */
	protected int count = 0 ;

	/**
	 * コンストラクタ
	 * @param
	 */
	public CheckRegist32176000Service() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() throws TnaviDbException {
		super.execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doQuery() throws TnaviDbException {
		try {
			// 出欠区分の重複チェック
			paramObj =  new Object[]{
						this.user,
						this.nendo,
						this.code
					};

			query = new QueryManager(SQL_FILE, paramObj, AttendkindJrEntity.class);

			List<AttendkindJrEntity> resultList = (List<AttendkindJrEntity>) this.executeQuery(query);

			this.count = resultList.size();

		} catch (Exception e) {
			// ロールバック
			log.error("出欠区分マスタチェック　DB処理に失敗しました。");
			log.error(e);
			throw new TnaviDbException(e);
		}
	}

	public void setParameters(String user, String nendo, String code ) {
		this.user  = user;
		this.nendo = nendo;
		this.code  = code;
	}

	public int getCount() {
		return count;
	}


}