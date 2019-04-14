package jp.co.systemd.tnavi.cus.oyama.db.service;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.systemd.tnavi.common.db.AbstractExecuteQuery;
import jp.co.systemd.tnavi.common.db.QueryManager;
import jp.co.systemd.tnavi.common.db.entity.CodeEntity;
import jp.co.systemd.tnavi.common.db.service.FindCodeByUserAndKindAndCodeService;
import jp.co.systemd.tnavi.common.exception.TnaviDbException;
import jp.co.systemd.tnavi.junior.att.db.entity.MonthDataListEntity;

/**
 * <PRE>
 * 出席簿印刷(通常学級　小山市用) 出力対象月データ取得Service.
 * </PRE>
 *
 * <B>Create</B> 2019.03.19 BY SD yamazaki<BR>
 *
 * @author System D, Inc.
 * @since 1.0.
 */
public class CreateMonthDataTable32176000Service extends AbstractExecuteQuery {
	/** log4j */
	private static final Log log = LogFactory.getLog(CreateMonthDataTable32176000Service.class);

	/** SQLストリング */
	private static final String EXEC_SQL = "cus/oyama/getMonthData32176000.sql";

	/** 所属コード */
	protected String user;

	/** 年度 **/
	protected String nendo;

	/** 学年 **/
	private String grade;

	/** 組 **/
	private String hmrClass;

	/** 出力対象月 **/
	private String month;

	/** 出力対象月テーブル 形成HTML文字列 **/
	private String monthTableHtml;

	/** 出力対象月日数 **/
	private int monthDayCount = 0;

	/**
	 * コンストラクタ パラメータセット
	 * @param user
	 * @param nendo
	 * @param grade
	 * @param hmrclass
	 */
	public CreateMonthDataTable32176000Service(String user, String nendo,String grade, String hmrclass, String month) {
		this.user 		= user;
		this.nendo 		= nendo;
		this.grade 		= grade;
		this.hmrClass 	= hmrclass;
		this.month 		= month;
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
		try
		{

			// ---------------------------------------------------
			// 長期休業中における平日の曜日文字色デフォルト値 取得(cod_kind:606 code:007 cod_value3)
			//  0:赤書き, 1:黒書き  ※レコードが無い、またはNULLである場合は「0:赤書き」として扱う
			// ---------------------------------------------------
			String termHolidayLblColorFlg = null;
			// パラメータの生成
			FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(this.user,606,"007");

			// クエリの実行(List形式で取得)
			findCodeService.execute();
			List<CodeEntity> wk_codeEntity = (List<CodeEntity>)findCodeService.getObj();

			for (CodeEntity entity : wk_codeEntity) {
				termHolidayLblColorFlg = entity.getCod_value3();
			}

			//定義が無い等、取得できない場合
			if(termHolidayLblColorFlg == null){
				termHolidayLblColorFlg = "0";	// 0：赤書きをセット
			}

			// ---------------------------------------------------
			// 対象月データ取得
			// ---------------------------------------------------
			// パラメータ生成
			Object[] param = {month.substring(4,month.length()), user, nendo, grade, hmrClass };
			// QueryManager 生成
			QueryManager queryManager = new QueryManager(EXEC_SQL, param, MonthDataListEntity.class);
			// 取得処理実行
			List<MonthDataListEntity> monthDataList = (List<MonthDataListEntity>)this.executeQuery(queryManager);

			// 以下の場合は、対象月の日数に0を、カレンダーHTMLに長さ0の文字列を設定し処理終了
			if (0 == monthDataList.size()){
				// 学級が存在しない場合
				monthDayCount = 0;
				monthTableHtml = "";
				return;
			}else if (monthDataList.get(0).getEnf_day() == null){
				// 学級は存在するが、授業日設定が登録されていない場合
				monthDayCount = 0;
				monthTableHtml = "";
				return;
			}


			// ---------------------------------------------------
			// 休業日重複チェックとデフォルト曜日文字色の設定
			// ---------------------------------------------------
			String compday = "";

			// デフォルト曜日文字色格納Map [key:日 value:文字色(黒:"b" 赤:"r")]
			HashMap<String, String> defaultColorMap = new HashMap<String, String>();

			for(int dayIndex = 0; dayIndex < monthDataList.size(); dayIndex++)
			{
				if(dayIndex == 0)
				{
					compday = monthDataList.get(dayIndex).getEnf_day();
				}

				// デフォルト曜日文字色の判定
				setDefaultWeekdayColor(monthDataList.get(dayIndex), termHolidayLblColorFlg, defaultColorMap);

				// 同一日付に複数の休業日が設定されている場合
				if(dayIndex != 0 && compday.equals(monthDataList.get(dayIndex).getEnf_day()))
				{
					// 祝日が設定されているため、曜日文字色は赤
					defaultColorMap.put(monthDataList.get(dayIndex).getEnf_day(), "r");

					// 該当する休業日の名称とうをセットし、削除
					String holName = monthDataList.get(dayIndex -1).getHol_name() + "," + monthDataList.get(dayIndex).getHol_name();
					monthDataList.get(dayIndex - 1).setHol_name(holName);

					monthDataList.remove(dayIndex);
					// インデックスを一つ戻す
					dayIndex--;

				}
				compday = monthDataList.get(dayIndex).getEnf_day();
			}
			// 対象月の日数を取得
			monthDayCount = monthDataList.size();

			// ---------------------------------------------------
			// 対象月データテーブル(HTML) 生成
			// ---------------------------------------------------
			int monthDataIndex = 0;
			StringBuilder monthTableString = new StringBuilder();
			if(monthDataList.size() != 0)
			{
				// --- テーブルタグセット
				monthTableString.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\" class=\"list\" style=\"width:580px; table-layout:fixed;\">\n");
				for(MonthDataListEntity monthData : monthDataList)
				{
					// 日
					String day = monthData.getEnf_day();
					if(day.substring(0, 1).equals("0"))
					{
						day = day.substring(1,2);
					}
					// 休業日名称
					String holidayName = monthData.getHol_name() == null ? "" : monthData.getHol_name();
					// 学級閉鎖印字文言
					String closingClassWord = monthData.getClo_printword() == null ? "" : monthData.getClo_printword();

					// --- 列<TR>セット
					monthTableString.append("<tr style=\"height:25px;\"> \n");
					// --- 日セット
					monthTableString.append("<td style=\"text-align:center;width:30px;\">" + day + "</td>\n");
					// --- 曜日セット
					monthTableString.append("<td id=\"weekday_" + Integer.toString(monthDataIndex) + "\" style=\"text-align:center;width:50px;\">" + monthData.getEnf_weekday() + "</td>\n");
					// --- 休業日名称
					monthTableString.append("<td style=\"text-align:center;width:150px;\">" + holidayName + "</td>\n");
					// --- 学級閉鎖印字文言
					monthTableString.append("<td style=\"text-align:center;width:250px;\">" + closingClassWord + "</td>\n");

					// --- 曜日文字色
					monthTableString.append("<td style=\"text-align:center;width:100px;\">");

					/* #7211の対応により、判定変更 */
//					// 授業実施日の場合
//					if(monthData.getEnf_enforce().equals("1"))
//					{
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"0\" onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" checked>黒 ");
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"1\" onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\">赤 ");
//					}
//					// 休業日(授業実施日でない場合)
//					else if(monthData.getEnf_enforce().equals("0"))
//					{
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"0\"onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" >黒 ");
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"1\"onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" checked>赤 ");
//					}

					if( "b".equals(defaultColorMap.get(monthData.getEnf_day())) )
					{
						monthTableString.append(createWeekdayRadio(monthDataIndex, "黒", "checked"));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "赤", ""));

					}
					else if( "r".equals(defaultColorMap.get(monthData.getEnf_day())) )
					{
						monthTableString.append(createWeekdayRadio(monthDataIndex, "黒", ""));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "赤", "checked"));
					}
					else
					{
						// ありえない分岐だが念のため
						monthTableString.append(createWeekdayRadio(monthDataIndex, "黒", "checked"));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "赤", ""));
					}

					monthTableString.append("</td>\n");
					// --- 列</TR>セット
					monthTableString.append("</tr>\n");

					monthDataIndex++;
				}
				// --- テーブルタグ終了
				monthTableString.append("</table>");
			}
			this.monthTableHtml = monthTableString.toString();
		}
		catch (Exception e)
		{
			// ロールバック
			log.error("出席簿印刷　出力対象月DB処理に失敗しました。");
			log.error(e);
			throw new TnaviDbException(e);
		}
	}

	/**
	 * 曜日文字色用のラジオボタンを生成
	 * @param index インデックス
	 * @param color 「黒」もしくは「赤」を指定
	 * @param checkedValue チェック状態にする場合は"checked"を指定
	 * @return 曜日文字色用のラジオボタン(html)
	 */
	private String createWeekdayRadio(int index, String color, String checkedValue){

		// 黒
		String black = " <input type= \"radio\" name=\"weekday_color_INDEX\" id=\"black_INDEX\" value=\"0\" onClick=\"changeColor(INDEX)\" CHECKED><label for=\"black_INDEX\">黒</label>";
		// 赤
		String red   = " <input type= \"radio\" name=\"weekday_color_INDEX\" id=\"red_INDEX\"   value=\"1\" onClick=\"changeColor(INDEX)\" CHECKED><label for=\"red_INDEX\">赤</label>";

		String html = black;
		if("黒".equals(color)){
			html = black;
		}else if("赤".equals(color)){
			html = red;
		}

		// "INDEX" の文字列をを引数の値で置換
		html = html.replaceAll("INDEX", Integer.toString(index));

		// "CHECKED"の文字列を引数の値で置換
		html = html.replaceAll("CHECKED", checkedValue);


		return html;
	}


	/**
	 * デフォルト曜日文字色の判定
	 * @param ent
	 * @param termHolidayLblColorFlg
	 * @param defaultColorMap
	 */
	private void setDefaultWeekdayColor(MonthDataListEntity ent, String termHolidayLblColorFlg, HashMap<String, String> defaultColorMap){

		// HashMapのkeyとなる日付
		String day = ent.getEnf_day();

		if ( "2".equals(ent.getHol_kind()) ){
			// --- 休業日の種別が「2:期間」
			if ( "1".equals(ent.getHolWeekdayFlg()) ){
				// 土日であれば「赤書き」
				defaultColorMap.put(day, "r");

			}else{
				// 平日である場合
				// 汎用マスタ kind:606 code:007 cod_value3の値により判定
				// cod_value3が'0':赤書き cod_value3が'1':黒書き
				if ("0".equals(termHolidayLblColorFlg)){
					defaultColorMap.put(day, "r");
				}else{
					defaultColorMap.put(day, "b");
				}
			}

		}else if ( "1".equals(ent.getHol_kind()) ){
			// --- 休業日の種別が「1:一日」
			//  祝日は「赤書き」
			defaultColorMap.put(day, "r");

		}else{
			// --- 休業日ではない
			//  授業実施日であれば「黒書き」、授業実施日でなければ「赤書き」
			if ( "1".equals(ent.getEnf_enforce()) ){
				defaultColorMap.put(day, "b");
			}else{
				defaultColorMap.put(day, "r");
			}

		}

	}

	public String getMonthTableHtml() {
		return monthTableHtml;
	}

	public int getMonthDayCount() {
		return monthDayCount;
	}


}