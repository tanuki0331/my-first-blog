package jp.co.systemd.tnavi.cus.oyama.db.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * ¬ÑÊm[óü(¬Rs_¬wZ) óü ¶kîñ FormBean.
 * </PRE>
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 * <B>remark</B><BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
@Setter @Getter
public class Print32179000StudentEntity {

	public final static String DEFALUT_VALUE = "";

	/** Nx **/
	private String nendo = DEFALUT_VALUE;

	/** wN **/
	private String stuGrade = DEFALUT_VALUE;

	/** g **/
	private String stuClass = DEFALUT_VALUE;

	/** oÈÔ **/
	private String stuNumber = DEFALUT_VALUE;

	/** oÈÔið¬j */
	private String refNumber = DEFALUT_VALUE;

	/** wR[h */
	private String stuClsno = DEFALUT_VALUE;

	/** wÐÔ **/
	private String stuCode = DEFALUT_VALUE;

	/** ¶kEÊÌ¶¼ **/
	private String stuName = DEFALUT_VALUE;

	/** ¶kEËÐ¶¼ **/
	private String stuKosekiName = DEFALUT_VALUE;

	/** ¶kE¶¼©È **/
	private String stuKana = DEFALUT_VALUE;

	/** ¶kEËÐ¶¼©È **/
	private String stuKosekiKana = DEFALUT_VALUE;

	/** ¶Nú */
	private String stuBirth = DEFALUT_VALUE;

	/** ÁÊxwO[v¼Ì */
	private String spGroupname = DEFALUT_VALUE;

	/** ÁÊxð¬æw¼ */
	private String excClass = DEFALUT_VALUE;

	/** ÁÊxð¬æoÈÔ */
	private String excNumber = DEFALUT_VALUE;

	/** ÁÊxð¬æNXNo */
	private String excClsno = DEFALUT_VALUE;

	/** ð¬wQÁÒtO */
	private String isKoryu;
}
