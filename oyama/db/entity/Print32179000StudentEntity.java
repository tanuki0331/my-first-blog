package jp.co.systemd.tnavi.cus.oyama.db.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * <PRE>
 * 成績通知票印刷(小山市_小学校) 印刷 生徒情報 FormBean.
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

	/** 年度 **/
	private String nendo = DEFALUT_VALUE;

	/** 学年 **/
	private String stuGrade = DEFALUT_VALUE;

	/** 組 **/
	private String stuClass = DEFALUT_VALUE;

	/** 出席番号 **/
	private String stuNumber = DEFALUT_VALUE;

	/** 出席番号（交流） */
	private String refNumber = DEFALUT_VALUE;

	/** 学級コード */
	private String stuClsno = DEFALUT_VALUE;

	/** 学籍番号 **/
	private String stuCode = DEFALUT_VALUE;

	/** 生徒・通称児童氏名 **/
	private String stuName = DEFALUT_VALUE;

	/** 生徒・戸籍児童氏名 **/
	private String stuKosekiName = DEFALUT_VALUE;

	/** 生徒・児童氏名かな **/
	private String stuKana = DEFALUT_VALUE;

	/** 生徒・戸籍児童氏名かな **/
	private String stuKosekiKana = DEFALUT_VALUE;

	/** 生年月日 */
	private String stuBirth = DEFALUT_VALUE;

	/** 特別支援学級グループ名称 */
	private String spGroupname = DEFALUT_VALUE;

	/** 特別支援交流先学級名 */
	private String excClass = DEFALUT_VALUE;

	/** 特別支援交流先出席番号 */
	private String excNumber = DEFALUT_VALUE;

	/** 特別支援交流先クラスNo */
	private String excClsno = DEFALUT_VALUE;

	/** 交流学級参加者フラグ */
	private String isKoryu;
}
