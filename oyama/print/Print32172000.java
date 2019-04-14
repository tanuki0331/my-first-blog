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
 * �o���Ȉꗗ�o�� ��� �o�̓N���X.
 *
 * <B>Create</B> 2019.02.28 BY aivick <BR>
 *
 * @author AIVIC
 * @since 1.0.
 */
public class Print32172000 extends AbstractPdfManagerAdditionPagesCR {

	/** log4j */
	private final Log log = LogFactory.getLog(Print32172000.class);

	/** ���C�A�E�g�I�u�W�F�N�g */
	private static final String LAYER_HEADER		= "Layer_Header";		// �w�b�_���C��
	private static final String LAYER_HEADER_CLASS	= "Layer_Header_Class"; // �w�b�_���C��
	private static final String LAYER_STUDENT		= "Layer_Student";		// ���k���C��

	private static final String LAYER_ATTEND		= "Layer_Attend_";		// �o���ȃ��C��

	private static final String COL_6				= "6";					// �o���ȃ��C�� 6�u���b�Nsuffix
	private static final String COL_5				= "5";					// �o���ȃ��C�� 5�u���b�Nsuffix
	private static final String COL_6Y				= "6Y";					// �o���ȃ��C�� 6�u���b�Nsuffix�i�N�Ԍv�ʒu6�j
	private static final String COL_5Y				= "5Y";					// �o���ȃ��C�� 5�u���b�Nsuffix�i�N�Ԍv�ʒu5�j
	private static final String COL_4Y				= "4Y";					// �o���ȃ��C�� 5�u���b�Nsuffix�i�N�Ԍv�ʒu4�j
	private static final String COL_3Y				= "3Y";					// �o���ȃ��C�� 5�u���b�Nsuffix�i�N�Ԍv�ʒu3�j

	/** �w�b�_���C���[ */
	private static final String TITLE				= "Title"; 				// �N�x�{�^�C�g��
	private static final String GRADE_CLASS			= "GradeClass";			// �w�N�E�g
	private static final String TEACHER_NAME		= "TeacherName";		// �S�C����
	private static final String STUDENT_LIST		= "StudentList"; 		// ���k���

	private static final String HEADER_LIST			= "HeaderList";			// ���A�w���v�A�N�x�v
	private static final String ATTEND_KIND			= "AttendKind";			// ���Ԃ̏o�ȓ���
	private static final String ATTEND_LIST			= "AttendList";			// �o���ȏ��

	private static final String TITLE_TEXT			= "�o���Ȉꗗ";			// �o���ȏ��

	private static final String ENFORCE_DAY_TEXT	= "���Ɠ���";			// ���Ɠ���
	private static final String MONTH_TEXT			= "��";					// �I�����p ��
	private static final String DAY_TEXT			= "��";					// ���Ɠ����p ��
	private static final String TOTAL_TEXT			= "�v";					// �w���p �v
	private static final String YEAR_TOTAL_TEXT		= "�N�Ԍv";				// �N�Ԍv

	private static final String BLOCK_INDEX_SEM		= "SEM";				// �u���b�N����pINDEX
	private static final String BLOCK_INDEX_YEAR	= "YEAR";				// �u���b�N����pINDEX
	private static final String BLOCK_INDEX_BLANK	= "BLANK";				// �u���b�N����pINDEX


	private static final int BLOCK_COUNT_6				= 6;				// �o���ȃ��C�� 6�u���b�N
	private static final int BLOCK_COUNT_5				= 5;				// �o���ȃ��C�� 5�u���b�N
	private static final int COL_IN_BLOCK_COUNT			= 6;				// �u���b�N���̗�

	private static final int OFFSET_DETAIL_SCHOOLKIND 	= 0 ;
	private static final int OFFSET_DETAIL_SICK       	= 1 ;
	private static final int OFFSET_DETAIL_ACCIDENT   	= 2 ;
	private static final int OFFSET_DETAIL_ATTEND     	= 3 ;
	private static final int OFFSET_DETAIL_LATE       	= 4 ;
	private static final int OFFSET_DETAIL_LEAVE      	= 5 ;

	/** ���׍s��(�Œ�l) */
	private static final int DETAIL_ROW_COUNT			= 40;
	private static final int BOLD_LINE_ROW_COUNT		= 5;				//�T�s�������p

	/** �r��(�א�)�� */
	private static final int SOLID_LINE_WIDTH			= 1;
	/** �r��(����)�� */
	private static final int BOLD_LINE_WIDTH			= 8;

	//���C�A�E�g�ʒu�����pOFFSET
	private static final int OFFSET_FORM_COL_BLOCK_5   		= 300;
	private static final int OFFSET_LAYER_HEADER_CLASS_5	= -700;

	/** ���t�����ݒ� */
	private DateFormatUtility dfu = null;

	/** ���FormBean */
	private Print32172000FormBean printFormBean;

	private SystemInfoBean sessionBean;

	// �ő�o�̓u���b�N��
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

			// �I������
			disp_pre_block_count = printFormBean.getCheckedCount();

			// �I�������ɁA�w���v�̗���P�ǉ�
			if( disp_pre_block_count < BLOCK_COUNT_6 ){
				disp_pre_block_count ++ ;
			}

			// �R�w���I���̏ꍇ�A�N�x�W�v�L��t���O��ݒ肷��B
			if( disp_pre_block_count < BLOCK_COUNT_6 && printFormBean.isYearTotalFlag() ) {
				disp_wide_block_count = 1;
			}

			disp_block_count = disp_pre_block_count + disp_wide_block_count;
			disp_wide_block_position = disp_block_count ;


			//�@�S�u���b�N�����T�����̏ꍇ�́A�u�����N�u���b�N���쐬����B
			if( disp_block_count < BLOCK_COUNT_5 ){
				disp_post_block_count = BLOCK_COUNT_5 - disp_pre_block_count - disp_wide_block_count ;
			}

			//�@�S�u���b�N��
			disp_block_count += disp_post_block_count;

			// �e�u���b�N�ʒu�ɓ����C���f�b�N�X����ݒ�
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

			// �N�Ԍv�@�\���̗L
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

			// �ΏۑS�N���X���o�͂���B
			for( HRoomEntity hRoomEntity : printFormBean.getHRoomEntityList() ){

				Search32172000_StaffEntity staffEntity = staffEntityMap.get(hRoomEntity.getHmr_clsno());

				// �񐔁A�񕝂���т���ɔ����ʒu�E�T�C�Y����
				setLayout( suffix );

				// �w�b�_�o��
				outputHeader( hRoomEntity , staffEntity );

				// ���׃w�b�_�o��
				outputDetailHeader( suffix , dispTargetMonth );

				// ���׃w�b�_�o��
				outputAttendKind( suffix );

				// ���׏o��(���k���)
				outputDetailStudent( suffix  ,hRoomEntity , dispTargetMonth );

				// �o��
				form.printOut();
				form.initialize();//�t�H�[��������
			}


		} catch (Exception e) {
			log.error("��O�����@���[�쐬���s", e);
			throw new TnaviPrintException(e);
		}
	}


	/**
	 * �g�p���郌�C�A�E�g��Activate����
	 *
	 * @throws Exception
	 */
	private void setLayout( String suffix ) throws Exception {

		// �\�����C���[�̐ݒ�B
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

		// �t�H�[���@�\�����C���[�̃I�t�Z�b�g
		if( disp_block_count < BLOCK_COUNT_6 ){
			// �t�H�[���@���E�Ɋ񂹂�B
			form.setOffsetX(OFFSET_FORM_COL_BLOCK_5);
			// �N���X���A�S�C���̃��C���[�����Ɋ񂹂�B
			CrLayer header_layer = form.getCrLayer(LAYER_HEADER_CLASS);
			header_layer.setOffsetX(OFFSET_LAYER_HEADER_CLASS_5);
		}

	}


	/**
	 * �w�b�_�o��
	 *
	 * @throws Exception
	 */
	private void outputHeader( HRoomEntity hRoomEntity, Search32172000_StaffEntity staffEntity ) throws Exception {

		String nendo = printFormBean.getOutput_title_nendo();
		String gengo = printFormBean.getOutput_title_gengo();

		// �N�x�A�w�����A�^�C�g�������o��
		String sem_name = printFormBean.getSemName();
		if( sem_name == null || sem_name.equals("")){
			sem_name = "    ";
		}
		getExistsFieldSetData(TITLE, gengo + " " + nendo + "�N�x" + " "+sem_name +" "+ TITLE_TEXT );

		// �w�N�E�g���o��
		String gradeClass = printFormBean.getGrade() + "�N " + hRoomEntity.getHmr_class() + "�g";
		getExistsFieldSetData(GRADE_CLASS, gradeClass);

		// �S�C�������o��
		if( staffEntity != null ){
			getExistsFieldSetData(TEACHER_NAME, staffEntity.getStf_name_w() );
		}
	}

	/**
	 * ���׃w�b�_�o��
	 *
	 * @throws Exception
	 */
	private void outputDetailHeader( String suffix,  String[] dispTargetMonth   ) throws Exception {


		CrListField headerListField       = null;
		CrText[][]  headerListFieldArray  = null;

		headerListField      = getCrListField( HEADER_LIST + suffix );
		headerListFieldArray = getListFieldGetColumns(headerListField);

		// --- ����6�񖈂Ɍr��(����) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// ����
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
	 * ���׃w�b�_�o�Ȏ��
	 *
	 * @throws Exception
	 */
	private void setAttendKindTitle( String LayerName, CrListField listField, CrText[][] listArray , int count ) throws Exception {

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SCHOOLKIND, 0, editVertical("�����^�o��") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,       0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT , 0);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,       0, "����" );

		printSlash(
				 getObjectCoordinates( listArray[ count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK][0], 0, 1)
				,getObjectCoordinates( listArray [count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT][0], 1, 1)
				,SOLID_LINE_WIDTH
				,form.getCrLayer( LayerName ));

		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_SICK,     1, editVertical("�a����") );
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ACCIDENT, 1, editVertical("���̌�") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND,   0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_ATTEND,   0, editVertical("�o�ȓ���") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE,     0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LATE,     0, editVertical("�x����") );

		listFieldMergeCell( listField, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE,    0, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE , 1);
		setListFieldData(   listArray, count * COL_IN_BLOCK_COUNT + OFFSET_DETAIL_LEAVE,    0, editVertical("���ސ�") );
	}


	/**
	 * ���׃w�b�_�o��
	 *
	 * @throws Exception
	 */
	private void outputAttendKind( String suffix) throws Exception {


		CrListField attendKindListField       = null;
		CrText[][]  attendKindListFieldArray  = null;

		attendKindListField  = getCrListField( ATTEND_KIND + suffix  );
		attendKindListFieldArray  = getListFieldGetColumns(attendKindListField);

		// --- ����6�񖈂Ɍr��(����) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// ����
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
	 * ���׏o��(���k���)
	 *
	 * @throws Exception
	 */
	private void outputDetailStudent( String suffix, HRoomEntity hRoomEntity, String[] dispTargetMonth  ) throws Exception {

		// �N���X��ݒ�
		String targetClsno = hRoomEntity.getHmr_clsno() ;

		// �\�����X�g�t�B�[���h�ݒ�
		CrListField studentListField = getCrListField(STUDENT_LIST);
		CrText[][] studentListFieldArray = getListFieldGetColumns(studentListField);

		CrListField attendListField       = null;
		CrText[][]  attendListFieldArray  = null;

		attendListField  = getCrListField( ATTEND_LIST + suffix  );
		attendListFieldArray = getListFieldGetColumns(attendListField);

		// --- ����5�s���Ɍr��(����) ---
		for (int row = 0; row < DETAIL_ROW_COUNT; row += BOLD_LINE_ROW_COUNT) {
			// ���k���
			printSlash(
					 getObjectCoordinates(studentListFieldArray[0][row], 0, 0)
					,getObjectCoordinates(studentListFieldArray[1][row], 1, 0)
					,BOLD_LINE_WIDTH
					,form.getCrLayer(LAYER_HEADER ));

			if( disp_block_count > 0 ){
				// ����
				printSlash(
					 getObjectCoordinates(attendListFieldArray[0][row], 0, 0)
					,getObjectCoordinates(attendListFieldArray[disp_block_count * COL_IN_BLOCK_COUNT - 1][row], 1, 0)
					,BOLD_LINE_WIDTH
					,form.getCrLayer(LAYER_ATTEND + suffix  ));
			}
		}

		// --- ����6�񖈂Ɍr��(����) ---
		printSlash(
			 getObjectCoordinates(studentListFieldArray[0][0], 0, 0)
			,getObjectCoordinates(studentListFieldArray[0][DETAIL_ROW_COUNT - 1 ], 0, 1)
			,SOLID_LINE_WIDTH
			,form.getCrLayer(LAYER_HEADER ));

		// --- ����6�񖈂Ɍr��(����) ---
		for (int col = 0; col < disp_block_count; col ++) {
			// ����
			printSlash(
				 getObjectCoordinates(attendListFieldArray[ col * COL_IN_BLOCK_COUNT ][0], 0, 0)
				,getObjectCoordinates(attendListFieldArray[ col * COL_IN_BLOCK_COUNT ][DETAIL_ROW_COUNT - 1 ], 0, 1)
				,BOLD_LINE_WIDTH
				,form.getCrLayer(LAYER_ATTEND + suffix ));
		}


		int row_counter = 0;
		List<Print32172000_AttendEntity> attendEntityList = null;

		//�ꗗ�̐��k���A�\�����Ԃ̌��ʂ���擾����B
		if( printFormBean.isYearTotalFlag() ) {
			attendEntityList = printFormBean.getYearAttendEntityList();
		}else{
			attendEntityList = printFormBean.getSemesterAttendEntityList();
		}

		// �e�ʂ̏o���ȏ����擾����B
		LinkedHashMap<String, Print32172000_AttendEntity> monthAttendEntityMap    = printFormBean.getAttendEntityMap();
		LinkedHashMap<String, Print32172000_AttendEntity> semesterAttendEntityMap = printFormBean.getSemesterAttendEntityMap();
		LinkedHashMap<String, Print32172000_AttendEntity> yearAttendEntityMap     = printFormBean.getYearAttendEntityMap();

		for( Print32172000_AttendEntity  attendEntity : attendEntityList ){

			String cls_group = attendEntity.getCls_group();

			if( !targetClsno.equals(cls_group)) continue;

			String cls_refno = attendEntity.getCls_reference_number();
			String stucode   = attendEntity.getStucode();
			String stuname   = attendEntity.getStudent_name();

			// ���k��Ƀf�[�^��ݒ肷��B
			setListFieldData(studentListFieldArray, 0, row_counter, cls_refno );
			setListFieldData(studentListFieldArray, 1, row_counter, stuname);

			// �����Ƃ̃f�[�^��ݒ肷��B
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
	 * �I�u�W�F�N�g�̑��݃`�F�b�N��ɒl���Z�b�g����.
	 *
	 * @param objectName
	 *            �I�u�W�F�N�g��
	 * @param value
	 *            �o�͂���l
	 * @throws Exception
	 */
	private void getExistsFieldSetData(String objectName, Object value) throws Exception {
		if (objectExists(objectName)) {
			getFieldSetData(objectName, value);
		}
	}

	/**
	 * �c�����̃Z���p�ɁA1�������ɉ��s��}�����đ̍ق𐮂���(2�����̏ꍇ�̓��ʏ����Ȃ�)
	 *
	 * @param val ���`�Ώۂ̕�����
	 * @return ���`��̕�����
	 */
	private String editVertical(String val) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<val.length(); i++){
			sb.append(val.charAt(i));
			if(i < val.length() - 1){
				// 1�������Ƃɉ��s�R�[�h��}������
				sb.append("\r\n");
			}
		}
		return sb.toString();
	}

	/**
	 * ���FormBean��ݒ肷��.
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
