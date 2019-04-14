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
 * �o���敪�}�X�^�@�R�[�h���݃`�F�b�NService.
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

	/** SQL�X�g�����O */
	private static final String SQL_FILE = "cus/oyama/getAttednKind32176000.sql";

	/** �����R�[�h */
	protected String user;

	/** �N�x **/
	protected String nendo;

	/** �o����ʃR�[�h **/
	protected String code;

	/** �N�G���֘A */
	protected Object[] paramObj = null;
	protected QueryManager query = null;

	/** �߂萔 */
	protected int count = 0 ;

	/**
	 * �R���X�g���N�^
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
			// �o���敪�̏d���`�F�b�N
			paramObj =  new Object[]{
						this.user,
						this.nendo,
						this.code
					};

			query = new QueryManager(SQL_FILE, paramObj, AttendkindJrEntity.class);

			List<AttendkindJrEntity> resultList = (List<AttendkindJrEntity>) this.executeQuery(query);

			this.count = resultList.size();

		} catch (Exception e) {
			// ���[���o�b�N
			log.error("�o���敪�}�X�^�`�F�b�N�@DB�����Ɏ��s���܂����B");
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