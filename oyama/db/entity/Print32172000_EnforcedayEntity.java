package jp.co.systemd.tnavi.cus.oyama.db.entity;

import jp.co.systemd.tnavi.junior.common.db.entity.EnforcedayEntity;

/**
 * <PRE>
 * �o���Ȉꗗ�o�� ���Ɠ��� Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.2.28 aivick<BR>
 * <B>remark</B><BR>
 *
 * @author SystemD inc.
 * @since 1.0.
 */

public class Print32172000_EnforcedayEntity extends EnforcedayEntity {

    /**
     * ���Ɠ������v��
     */
    protected String total_count ;

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

}
