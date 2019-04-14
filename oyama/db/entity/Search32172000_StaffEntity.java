/**------------------------------------------------------------**
 * Te@cherNavi
 *  Copyright(C) 2019 SystemD inc., All Rights Reserved.
 **------------------------------------------------------------**/
package jp.co.systemd.tnavi.cus.oyama.db.entity;

import jp.co.systemd.tnavi.common.db.constants.CommonConstantsUseable;

/**
 * <PRE>
 * 	出欠席一覧出力　担任取得 Entity.
 * </PRE>
 *
 * <B>Create</B> 2019.02.28 BY aivick<BR>
 * <B>remark</B><BR>
 *
 * @since 1.0.
 */
public class Search32172000_StaffEntity implements CommonConstantsUseable {

    /**
     * 学年
     */
    private String hmr_glade;

    /**
     * 組
     */
    private String hmr_class;

    /**
     * クラス
     */
    private String hmr_clsno;

    /**
     * 職員コード
     */
    private String stfh_stfcode;

    /**
     * 職員名
     */
    private String stf_name_w;



	public String getHmr_glade() {
		return hmr_glade;
	}

	public void setHmr_glade(String hmr_glade) {
		this.hmr_glade = hmr_glade;
	}

	public String getHmr_class() {
		return hmr_class;
	}

	public void setHmr_class(String hmr_class) {
		this.hmr_class = hmr_class;
	}

	public String getHmr_clsno() {
		return hmr_clsno;
	}

	public void setHmr_clsno(String hmr_clsno) {
		this.hmr_clsno = hmr_clsno;
	}

	public String getStfh_stfcode() {
		return stfh_stfcode;
	}

	public void setStfh_stfcode(String stfh_stfcode) {
		this.stfh_stfcode = stfh_stfcode;
	}

	public String getStf_name_w() {
		return stf_name_w;
	}

	public void setStf_name_w(String stf_name_w) {
		this.stf_name_w = stf_name_w;
	}


}