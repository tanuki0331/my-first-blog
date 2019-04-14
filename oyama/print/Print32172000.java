package jp.co.systemd.tnavi.cus.oyama.print;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.CrLayer;
import jp.co.hos.coreports.object.CrListField;
import jp.co.hos.coreports.object.CrText;
import jp.co.systemd.tnavi.common.db.entity.HRoomEntity;
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.session.SystemInfoBean;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32172000_EnforcedayEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Search32172000_StaffEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32172000FormBean;

/**
 * 出欠席一覧出力 印刷 出力クラス.
 *
 * <B>Create</B> 2019.02.28 BY aivick <BR>
 *
 * @author AIVIC
 * @since 1.0.
 */
public class Print32172000 extends AbstractPdfManagerAdditionPagesCR {

	/** log4j */
	private final Log log = LogFactory.getLog(Print32172000.class);

	/** レイアウトオブジェクト */
	private static final String LAYER_HEADER		= "Layer_Header";		// ヘッダレイヤ
	private static final String LAYER_HEADER_CLASS	= "Layer_Header_Class"; // ヘッダレイヤ
	private static final String LAYER_STUDENT		= "Layer_Student";		// 生徒レイヤ

	private static final String LAYER_ATTEND		= "Layer_Attend_";		// 出欠席レイヤ

	private static final String COL_6				= "6";					// 出欠席レイヤ 6ブロックsuffix
	private static final String COL_5				= "5";					// 出欠席レイヤ 5ブロックsuffix
	private static final String COL_6Y				= "6Y";					// 出欠席レイヤ 6ブロックsuffix（年間計位置6）
	private static final String COL_5Y				= "5Y";					// 出欠席レイヤ 5ブロックsuffix（年間計位置5）
	private static final String COL_4Y				= "4Y";					// 出欠席レイヤ 5ブロックsuffix（年間計位置4）
	private static final String COL_3Y				= "3Y";					// 出欠席レイヤ 5ブロックsuffix（年間計位置3）

	/** ヘッダレイヤー */
	private static final String TITLE				= "Title"; 				// 年度＋タイトル
	private static final String GRADE_CLASS			= "GradeClass";			// 学年・組
	private static final String TEACHER_NAME		= "TeacherName";		// 担任氏名
	private static final String STUDENT_LIST		= "StudentList"; 		// 生徒情報

	private static final String HEADER_LIST			= "HeaderList";			// 月、学期計、年度計
	private static final String ATTEND_KIND			= "AttendKind";			// 期間の出席日数
	private static final String ATTEND_LIST			= "AttendList";			// 出欠席情報

	private static final String TITLE_TEXT			= "出欠席一覧";			// 出欠席情報

	private static final String ENFORCE_DAY_TEXT	= "授業日数";			// 授業日数
	private static final String MONTH_TEXT			= "月";					// 選択月用 月
	private static final String DAY_TEXT			= "日";					// 授業日数用 日
	private static final String TOTAL_TEXT			= "計";					// 学期用 計
	private static final String YEAR_TOTAL_TEXT		= "年間計";				// 年間計

	private static final String BLOCK_INDEX_SEM		= "SEM";				// ブロック判定用INDEX
	private static final String BLOCK_INDEX_YEAR	= "YEAR";				// ブロック判定用INDEX
	private static final String BLOCK_INDEX_BLANK	= "BLANK";				// ブロック判定用INDEX


	private static final int BLOCK_COUNT_6				= 6;				// 出欠席レイヤ 6ブロック
	private static final int BLOCK_COUNT_5				= 5;				// 出欠席レイヤ 5ブロック
	private static final int COL_IN_BLOCK_COUNT			= 6;				// ブロック内の列数

	private static final int OFFSET_DETAIL_SCHOOLKIND 	= 0 ;
	private static final int OFFSET_DETAIL_SICK       	= 1 ;
	private static final int OFFSET_DETAIL_ACCIDENT   	= 2 ;
	private static final int OFFSET_DETAIL_ATTEND     	= 3 ;
	private static final int OFFSET_DETAIL_LATE       	= 4 ;
	private static final int OFFSET_DETAIL_LEAVE      	= 5 ;

	/** 明細行数(固定値) */
	private static final int DETAIL_ROW_COUNT			= 40;
	private static final int BOLD_LINE_ROW_COUNT		= 5;				//５行毎太線用

	/** 罫線(細線)幅 */
	private static final int SOLID_LINE_WIDTH			= 1;
	/** 罫線(太線)幅 */
	private static final int BOLD_LINE_WIDTH			= 8;

	//レイアウト位置調整用OFFSET
	private static final int OFFSET_FORM_COL_BLOCK_5   		= 300;
	private static final int OFFSET_LAYER_HEADER_CLASS_5	= -700;

	/** 日付書式設定 */
	private DateFormatUtility dfu = null;

	/** 印刷FormBean */
	private Print32172000FormBean printFormBean;

	private SystemInfoBean sessionBean;

	// 最大出力ブロック数
	private int disp_block_count    = 0 ;

	private int disp_pre_block_count  = 0 ;
	private int disp_wide_block_count = 0 ;
	private int disp_post_block_count = 0 ;

	private int disp_wide_block_position = 0 ;

	@Override
	protected void doPrint() throws TnaviPrintException {

		try {
			dfu = new DateFormatUtility(sessionBean.getUserCode());
			dfu.setZeroToSpace(true);

			// 選択月数
			disp_pre_block_count = printFormBean.getCheckedCount();

			// 選択月数に、学期計の列を１追加
			if( disp_pre_block_count < BLOCK_COUNT_6 ){
				disp_pre_block_count ++ ;
			}

			// ３学期選択の場合、年度集計有りフラグを設定する。
			if( disp_pre_block_count < BLOCK_COUNT_6 && printFormBean.isYearTotalFlag() ) {
				disp_wide_block_count = 1;
			}

			disp_block_count = disp_pre_block_count + disp_wide_block_count;
			disp_wide_block_position = disp_block_count ;


			//　全ブロック数が５未満の場合は、ブランクブロックを作成する。
			if( disp_block_count < BLOCK_COUNT_5 ){
				disp_post_block_count = BLOCK_COUNT_5 - disp_pre_block_count - disp_wide_block_count ;
			}

			//　全ブロック数
			disp_block_count += disp_post_block_count;

			// 各ブロック位置に入れるインデックス名を設定
			String[] checkedTargetMonth = printFormBean.getCheckedTargetMonth();
			String[] dispTargetMonth = new String[disp_block_count];

			int counter_dispTartgetMonth = 0;

			for( int i = 0 ; i < checkedTargetMonth.length ; i ++  ){
				if( checkedTargetMonth[i].equals("")) continue;
				dispTargetMonth[counter_dispTartgetMonth] = checkedTargetMonth[i];
				counter_dispTartgetMonth ++ ;
			}

			if( counter_dispTartgetMonth < BLOCK_COUNT_6 ){
				dispTargetMonth[counter_dispTartgetMonth] = BLOCK_INDEX_SEM;
				counter_dispTartgetMonth ++ ;
			}
			if( counter_dispTartgetMonth < BLOCK_COUNT_6  && disp_wide_block_count == 1 ){
				dispTargetMonth[counter_dispTartgetMonth] = BLOCK_INDEX_YEAR;
				counter_dispTartgetMonth ++ ;
			}

			for( int i = counter_dispTartgetMonth ; i < BLOCK_COUNT_5 ; i ++  ){
				dispTargetMonth[i] = BLOCK_INDEX_BLANK;
			}

			// 年間計　表示の有
			String suffix = "";

			if( disp_block_count == BLOCK_COUNT_6 ){

				if( disp_wide_block_count > 0 ){
					suffix = COL_6Y ;
				}else{
					suffix = COL_6 ;
				}
			}else {
				if( disp_wide_block_count > 0 ){

					if( disp_wide_block_position == 3){
						suffix = COL_3Y ;
					}else if( disp_wide_block_position == 4){
						suffix = COL_4Y ;
					}else {
						suffix = COL_5Y ;
					}
				}else{
					suffix = COL_5 ;
				}
			}

			HashMap<String, Search32172000_StaffEntity> staffEntityMap = printFormBean.getStaffEntityMap();

			// 対象全クラスを出力する。
			for( HRoomEntity hRoomEntity : printFormBean.getHRoomEntityList() ){

				Search32172000_StaffEntity staffEntity = staffEntityMap.get(hRoomEntity.getHmr_clsno());

				// 列数、列幅およびそれに伴う位置・サイズ調整
				setLayout( suffix );

				// ヘッダ出力
				outputHeader( hRoomEntity , staffEntity );

				// 明細ヘッダ出力
				outputDetailHeader( suffix , dispTargetMonth );

				// 明細ヘッダ出力
				outputAttendKind( suffix );

				// 明細出力(生徒情報)
				outputDetailStudent( suffix  ,hRoomEntity , dispTargetMonth );

				// 出力
				form.printOut();
				form.initialize();//フォーム初期化
			}


		} catch (Exception e) {
			log.error("例外発生　帳票作成失敗", e);
			throw new TnaviPrintException(e);
		}
	}


	/**
	 * 使用するレイアウトをActivateする
	 *
	 * @throws Exception
	 */
	private void setLayout( String suffix ) throws Exception {

		// 表示レイヤーの設定。
		getCrLayerSetVisibleAtPrint(LAYER_HEADER       , true );
		getCrLayerSetVisibleAtPrint(LAYER_HEADER_CLASS , true );
		getCrLayerSetVisibleAtPrint(LAYER_STUDENT      , true );

		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_6  , false );
		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_5  , false );

		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_6Y , false );
		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_5Y , false );
		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_4Y , false );
		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + COL_3Y , false );


		getCrLayerSetVisibleAtPrint(LAYER_ATTEND + suffix , true );

		// フォーム　表示レイヤーのオフセット
		if( disp_block_count < BLOCK_COUNT_6 ){
			// フォーム　を右に寄せる。
			form.setOffsetX(OFFSET_FORM_COL_BLOCK_5);
			// クラス名、担任名のレイヤーを左に寄せる。
			CrLayer header_layer = form.getCrLayer(LAYER_HEADER_CLASS);
			header_layer.setOffsetX(OFFSET_LAYER_HEADER_CLASS_5);
		}

	}


	/**
	 * ヘッダ出力
	 *
	 * @throws Exception
	 */
	private void outputHeader( HRoomEntity hRoomEntity, Search32172000_StaffEntity staffEntity ) throws Exception {

		String nendo = printFormBean.getOutput_title_nendo();
		String gengo = printFormBean.getOutput_title_gengo();

		// 年度、学期名、タイトル名を出力
		String sem_name = printFormBean.getSemName();
		if( sem_name == null || sem_name.equals("")){
			sem_name = "    ";
		}
		getExistsFieldSetData(TITLE, gengo + " " + nendo + "年度" + " "+sem_name +" "+ TITLE_TEXT );

		// 学年・組を出力
		String gradeClass = printFormBean.getGrade() + "年 " + hRoomEntity.getHmr_class() + "組";
		getExistsFieldSetData(GRADE_CLASS, gradeClass);

		// 担任氏名を出力
		if( staffEntity != null ){
			getExistsFieldSetData(TEACHER_NAME, staffEntity.getStf_name_w() );
		}
	}

	/**
	 * 明細ヘッダ出力
	 *
	 * @throws Exception
	 */
	private void outputDetailHeader( String suffix,  String[] dispTargetMonth   ) throws Exception {


		CrListField headerListField       = null;
		CrText[][]  headerListFieldArray  = null;

		headerListField      = getCrListField( HEADER_LIST + suffix );
		headerListFieldArray = getListFieldGetColumns(headerListField);

		// --- 明細6列毎に罫線(太線) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// 明細
			printSlash(
				 getObjectCoordinates(headerListFieldArray[ col * COL_IN_BLOCK_COUNT ][0], 0, 0)
				,getObjectCoordinates(headerListFieldArray[ col * COL_IN_BLOCK_COUNT ][1], 0, 1)
				,BOLD_LINE_WIDTH
				,form.getCrLayer( LAYER_ATTEND + suffix ));
			listFieldMergeCell( headerListField, col * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 0, col * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE , 0);
			listFieldMergeCell( headerListField, col * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 1, col * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE , 1);
		}

		LinkedHashMap<String, Print32172000_EnforcedayEntity> enforceDayTotalMap = printFormBean.getEnforceDayTotalMap();

		int col_counter = 0;

		for( int i = 0 ; i < dispTargetMonth.length ; i ++  ){

			String disp_month = "";
			if( dispTargetMonth[i].equals("BLANK") ){

			}else if( dispTargetMonth[i].equals(BLOCK_INDEX_SEM)  ){
				disp_month = printFormBean.getSemName()+TOTAL_TEXT ;
				setListFieldData(headerListFieldArray, col_counter , 0, disp_month );

				String sem_key = BLOCK_INDEX_SEM;
				String display_enforceday = "";
				if( enforceDayTotalMap.containsKey(sem_key)){
					Print32172000_EnforcedayEntity print32172000_EnforcedayEntity = enforceDayTotalMap.get(sem_key);
					display_enforceday = print32172000_EnforcedayEntity.getTotal_count();
				}
				setListFieldData(headerListFieldArray, col_counter , 1, ENFORCE_DAY_TEXT+" "+ display_enforceday+DAY_TEXT );

			}else if( dispTargetMonth[i].equals(BLOCK_INDEX_YEAR) ){
				disp_month = YEAR_TOTAL_TEXT ;
				setListFieldData(headerListFieldArray, col_counter , 0, disp_month );

				String year_key = BLOCK_INDEX_YEAR;
				String display_enforceday = "";
				if( enforceDayTotalMap.containsKey(year_key)){
					Print32172000_EnforcedayEntity print32172000_EnforcedayEntity = enforceDayTotalMap.get(year_key);
					display_enforceday = print32172000_EnforcedayEntity.getTotal_count();
				}
				setListFieldData(headerListFieldArray, col_counter , 1, ENFORCE_DAY_TEXT+" "+ display_enforceday+DAY_TEXT );

			}else{
				disp_month = String.valueOf( Integer.parseInt(dispTargetMonth[i].substring(4,6)) ) + MONTH_TEXT;
				setListFieldData(headerListFieldArray, col_counter , 0, disp_month );

				String month_key = dispTargetMonth[i].substring(4,6) ;
				String display_enforceday = "";
				if( enforceDayTotalMap.containsKey(month_key)){
					Print32172000_EnforcedayEntity print32172000_EnforcedayEntity = enforceDayTotalMap.get(month_key);
					display_enforceday = print32172000_EnforcedayEntity.getTotal_count();
				}
				setListFieldData(headerListFieldArray, col_counter , 1, ENFORCE_DAY_TEXT+" "+display_enforceday+DAY_TEXT );
			}
			col_counter += COL_IN_BLOCK_COUNT ;
		}

	}

	/**
	 * 明細ヘッダ出席種別
	 *
	 * @throws Exception
	 */
	private void setAttendKindTitle( String LayerName, CrListField listField, CrText[][] listArray , int count ) throws Exception {

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 0, editVertical("忌引／出停") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,       0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT , 0);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,       0, "欠席" );

		printSlash(
				 getObjectCoordinates( listArray[ count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK][0], 0, 1)
				,getObjectCoordinates( listArray [count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT][0], 1, 1)
				,SOLID_LINE_WIDTH
				,form.getCrLayer( LayerName ));

		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,     1, editVertical("病欠席") );
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT, 1, editVertical("事故欠") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND,   0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND,   0, editVertical("出席日数") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE,     0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE,     0, editVertical("遅刻数") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE,    0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE,    0, editVertical("早退数") );
	}


	/**
	 * 明細ヘッダ出力
	 *
	 * @throws Exception
	 */
	private void outputAttendKind( String suffix) throws Exception {


		CrListField attendKindListField       = null;
		CrText[][]  attendKindListFieldArray  = null;

		attendKindListField  = getCrListField( ATTEND_KIND + suffix  );
		attendKindListFieldArray  = getListFieldGetColumns(attendKindListField);

		// --- 明細6列毎に罫線(太線) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// 明細
			printSlash(
				 getObjectCoordinates(attendKindListFieldArray[ col * COL_IN_BLOCK_COUNT ][0], 0, 0)
				,getObjectCoordinates(attendKindListFieldArray[ col * COL_IN_BLOCK_COUNT ][1], 0, 1)
				,BOLD_LINE_WIDTH
				,form.getCrLayer(LAYER_ATTEND + suffix ));
		}

		if( disp_block_count > 0 ){
			for ( int i=0 ; i < disp_block_count ; i++ ) {
				setAttendKindTitle( LAYER_ATTEND + suffix, attendKindListField, attendKindListFieldArray , i );
			}
		}

	}

	/**
	 * 明細出力(生徒情報)
	 *
	 * @throws Exception
	 */
	private void outputDetailStudent( String suffix, HRoomEntity hRoomEntity, String[] dispTargetMonth  ) throws Exception {

		// クラスを設定
		String targetClsno = hRoomEntity.getHmr_clsno() ;

		// 表示リストフィールド設定
		CrListField studentListField = getCrListField(STUDENT_LIST);
		CrText[][] studentListFieldArray = getListFieldGetColumns(studentListField);

		CrListField attendListField       = null;
		CrText[][]  attendListFieldArray  = null;

		attendListField  = getCrListField( ATTEND_LIST + suffix  );
		attendListFieldArray = getListFieldGetColumns(attendListField);

		// --- 明細5行毎に罫線(太線) ---
		for (int row = 0; row < DETAIL_ROW_COUNT; row += BOLD_LINE_ROW_COUNT) {
			// 生徒情報
			printSlash(
					 getObjectCoordinates(studentListFieldArray[0][row], 0, 0)
					,getObjectCoordinates(studentListFieldArray[1][row], 1, 0)
					,BOLD_LINE_WIDTH
					,form.getCrLayer(LAYER_HEADER ));

			if( disp_block_count > 0 ){
				// 明細
				printSlash(
					 getObjectCoordinates(attendListFieldArray[0][row], 0, 0)
					,getObjectCoordinates(attendListFieldArray[disp_block_count * COL_IN_BLOCK_COUNT - 1][row], 1, 0)
					,BOLD_LINE_WIDTH
					,form.getCrLayer(LAYER_ATTEND + suffix  ));
			}
		}

		// --- 明細6列毎に罫線(太線) ---
		printSlash(
			 getObjectCoordinates(studentListFieldArray[0][0], 0, 0)
			,getObjectCoordinates(studentListFieldArray[0][DETAIL_ROW_COUNT - 1 ], 0, 1)
			,SOLID_LINE_WIDTH
			,form.getCrLayer(LAYER_HEADER ));

		// --- 明細6列毎に罫線(太線) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// 明細
			printSlash(
				 getObjectCoordinates(attendListFieldArray[ col * COL_IN_BLOCK_COUNT ][0], 0, 0)
				,getObjectCoordinates(attendListFieldArray[ col * COL_IN_BLOCK_COUNT ][DETAIL_ROW_COUNT - 1 ], 0, 1)
				,BOLD_LINE_WIDTH
				,form.getCrLayer(LAYER_ATTEND + suffix ));
		}


		int row_counter = 0;
		List<Print32172000_AttendEntity> attendEntityList = null;

		//一覧の生徒を、表示期間の結果から取得する。
		if( printFormBean.isYearTotalFlag() ) {
			attendEntityList = printFormBean.getYearAttendEntityList();
		}else{
			attendEntityList = printFormBean.getSemesterAttendEntityList();
		}

		// 各個別の出欠席情報を取得する。
		LinkedHashMap<String, Print32172000_AttendEntity> monthAttendEntityMap    = printFormBean.getAttendEntityMap();
		LinkedHashMap<String, Print32172000_AttendEntity> semesterAttendEntityMap = printFormBean.getSemesterAttendEntityMap();
		LinkedHashMap<String, Print32172000_AttendEntity> yearAttendEntityMap     = printFormBean.getYearAttendEntityMap();

		for( Print32172000_AttendEntity  attendEntity : attendEntityList ){

			String cls_group = attendEntity.getCls_group();

			if( !targetClsno.equals(cls_group)) continue;

			String cls_refno = attendEntity.getCls_reference_number();
			String stucode   = attendEntity.getStucode();
			String stuname   = attendEntity.getStudent_name();

			// 生徒列にデータを設定する。
			setListFieldData(studentListFieldArray, 0, row_counter, cls_refno );
			setListFieldData(studentListFieldArray, 1, row_counter, stuname);

			// 月ごとのデータを設定する。
			int col_counter = 0;

			for( int i = 0 ; i < dispTargetMonth.length ; i ++  ){

				if( dispTargetMonth[i].equals(BLOCK_INDEX_BLANK)){

				}else if( dispTargetMonth[i].equals(BLOCK_INDEX_SEM)  ){

					String sem_key = targetClsno + "_" + stucode ;
					if( semesterAttendEntityMap.containsKey(sem_key) ){
						Print32172000_AttendEntity semesterAttendEntity =  semesterAttendEntityMap.get(sem_key);

						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SCHOOLKIND , row_counter, semesterAttendEntity.getSchoolkindCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SICK       , row_counter, semesterAttendEntity.getSickCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ACCIDENT   , row_counter, semesterAttendEntity.getAccidentCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ATTEND     , row_counter, semesterAttendEntity.getAttendCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LATE       , row_counter, semesterAttendEntity.getLateCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LEAVE      , row_counter, semesterAttendEntity.getLeaveCount() );
					}

				}else if( dispTargetMonth[i].equals(BLOCK_INDEX_YEAR) ){

					String year_key = targetClsno + "_" + stucode ;
					if( yearAttendEntityMap.containsKey(year_key) ){
						Print32172000_AttendEntity yearAttendEntity =  yearAttendEntityMap.get(year_key);

						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SCHOOLKIND , row_counter, yearAttendEntity.getSchoolkindCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SICK       , row_counter, yearAttendEntity.getSickCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ACCIDENT   , row_counter, yearAttendEntity.getAccidentCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ATTEND     , row_counter, yearAttendEntity.getAttendCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LATE       , row_counter, yearAttendEntity.getLateCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LEAVE      , row_counter, yearAttendEntity.getLeaveCount() );
					}
				}else{

					String key = targetClsno + "_" + dispTargetMonth[i].substring(4, 6)  + "_" + stucode ;
					if( monthAttendEntityMap.containsKey(key) ) {
						Print32172000_AttendEntity monthAttendEntity =  monthAttendEntityMap.get(key);

						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SCHOOLKIND , row_counter, monthAttendEntity.getSchoolkindCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_SICK       , row_counter, monthAttendEntity.getSickCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ACCIDENT   , row_counter, monthAttendEntity.getAccidentCount() );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_ATTEND     , row_counter, monthAttendEntity.getAttendCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LATE       , row_counter, monthAttendEntity.getLateCount()  );
						setListFieldData(attendListFieldArray, col_counter + OFFSET_DETAIL_LEAVE      , row_counter, monthAttendEntity.getLeaveCount() );
					}
				}
				col_counter +=COL_IN_BLOCK_COUNT ;
			}
			row_counter ++ ;
			if( row_counter == 40 ) break;
		}
	}


	/**
	 * オブジェクトの存在チェック後に値をセットする.
	 *
	 * @param objectName
	 *            オブジェクト名
	 * @param value
	 *            出力する値
	 * @throws Exception
	 */
	private void getExistsFieldSetData(String objectName, Object value) throws Exception {
		if (objectExists(objectName)) {
			getFieldSetData(objectName, value);
		}
	}

	/**
	 * 縦書きのセル用に、1文字毎に改行を挿入して体裁を整える(2文字の場合の特別処理なし)
	 *
	 * @param val 整形対象の文字列
	 * @return 整形後の文字列
	 */
	private String editVertical(String val) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<val.length(); i++){
			sb.append(val.charAt(i));
			if(i < val.length() - 1){
				// 1文字ごとに改行コードを挿入する
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	/**
	 * 印刷FormBeanを設定する.
	 *
	 * @param printFormBean
	 *            the printFormBean to set
	 */
	public void setPrintFormBean(Print32172000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

	public SystemInfoBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SystemInfoBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
