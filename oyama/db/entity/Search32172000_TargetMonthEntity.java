/**------------------------------------------------------------**
 * Te@cherNavi
 *  Copyright(C) 2019 SystemD inc., All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

import jp.co.systemd.tnavi.common.db.constants.CommonConstantsUseable;

/**
 * <PRE>
 * 	oΘκoΝ@oΝEntity.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY aivick<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Search32172000_TargetMonthEntity implements CommonConstantsUseable {

    /**
     * ΞΫ
     */
    private String month;

    /**
     * ΞΫΌ
     */
    private String month_name;

    /**
     * ΞΫN
     */
    private String yyyymm;

    /**
     * Iπ
     */
    private String checked;

    /**
     * ΞΫ
     */
    private String disabled;


	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getMonth_name() {
		return month_name;
	}

	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}

	public String getYyyymm() {
		return yyyymm;
	}

	public void setYyyymm(String yyyymm) {
		this.yyyymm = yyyymm;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}




}