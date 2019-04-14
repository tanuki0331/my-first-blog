package jp.co.systemd.tnavi.cus.oyama.db.entity;

import jp.co.systemd.tnavi.junior.common.db.entity.EnforcedayEntity;

/**
 * <PRE>
 * 出欠席一覧出力 授業日数 Entity.
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
     * 授業日数合計数
     */
    protected String total_count ;

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

}
