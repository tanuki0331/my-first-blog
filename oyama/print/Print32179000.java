package jp.co.systemd.tnavi.cus.oyama.print;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.hos.coreports.CrLayer;
import jp.co.hos.coreports.CrLayers;
import jp.co.hos.coreports.CrObject;
import jp.co.hos.coreports.object.CrImageField;
import jp.co.hos.coreports.object.CrListField;
import jp.co.hos.coreports.object.CrText;
import jp.co.systemd.tnavi.common.db.entity.StaffEntity;
import jp.co.systemd.tnavi.common.exception.TnaviPrintException;
import jp.co.systemd.tnavi.common.print.AbstractPdfManagerAdditionPagesCR;
import jp.co.systemd.tnavi.common.utility.DateFormatUtility;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ActviewEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000AttendEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ForeignLangActEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000ScoreEntity;
import jp.co.systemd.tnavi.cus.oyama.db.entity.Print32179000SpActEntity;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000FormBean;
import jp.co.systemd.tnavi.cus.oyama.formbean.Print32179000StudentFormBean;

/**
 * ���ђʒm�\���(���R�s_���w�Z) ��� �o�̓N���X.
 *
 * <B>Create</B> 2019.04.13 BY AIVICK <BR>
 *
 * @author SystemD Inc.
 * @since 1.0.
 */
public class Print32179000 extends AbstractPdfManagerAdditionPagesCR {

	/** log4j */
	private final Log log = LogFactory.getLog(Print32179000.class);

	/** ���C�A�E�g�I�u�W�F�N�g */
	/* �\�� */
	private static final String LAYER_COVER = "Layer_Cover";
	private static final String LAYER_DETAIL_STANDARD = "_Detail_Standard";
	private static final String LAYER_DETAIL_SPGD = "_Detail_Spgd";
	private static final String LAYER_SPGD_1 = "_1"; // ���ʎx���w��
	private static final String LAYER_SPGD_2 = "_2"; // �𗬊w��
	private static final String LAYER_SPGD_3 = "_3"; // �𗬂Ɠ��ʎx���w��

	/* �C���� */
	private static final String LAYER_DEED = "Layer_Deed";
	private static final String LAYER_SPGD = "_Spgd";
	private static final String LAYER_STANDARD = "_Standard";
	private static final String LAYER_MIKATA = "_Mikata";

	/* �w�K�̋L�^ */
	private static final String LAYER_SCORE = "Layer_Score";
	private static final String LAYER_HEADER = "_Header";

	/* �w�N�ʃ��C���[�p */
	private static final String LAYER_GRADE_12 = "_12";
	private static final String LAYER_GRADE_34 = "_34";
	private static final String LAYER_GRADE_56 = "_56";

	/** �o�̓I�u�W�F�N�g */
	// �\��
	private static final String ITEM_NENDO_COVER = "Nendo_Cover"; // �N�x
	private static final String ITEM_SCHOOL_NAME_COVER = "SchoolName_Cover"; // �w�Z��
	private static final String ITEM_SCHOOL_STAMP02_COVER = "SchoolStamp02_Cover"; // �ʒm�[�\��
	private static final String ITEM_SCHOOL_STAMP04_COVER = "SchoolStamp04_Cover"; // �ʒm�[�摜

	private static final String ITEM_STUDENT_CLASS_COVER = "StudentGradeClass_Cover"; // �N�E�g
	private static final String ITEM_STUDENT_NAME_COVER = "StudentName_Cover"; // ������
	private static final String ITEM_PRINCIPAL_NAME_COVER = "PrincipalName_Cover"; // �Z����
	private static final String ITEM_TANNIN_NAME_COVER = "TanninName_Cover"; // �S�C��
	private static final String ITEM_SPGD = "_Spgd"; // ���ʎx���w��
	private static final String ITEM_ALTERNAT = "_Alternat"; // �𗬊w��
	private static final String ITEM_STANDARD = "_Standard"; // �ʏ�w��
	private static final String ITEM_DISP_NO1 = "_1"; // �N�E�g�A�S�C���̕\�����C�A�E�g
	private static final String ITEM_DISP_NO2 = "_2"; // �N�E�g�A�S�C���̕\�����C�A�E�g

	// �C����
	private static final String ITEM_STUDENT_NAME_DEED = "StudentName_Deed"; // ������
	private static final String ITEM_GRAD_GRADE_DEED = "GradGrade_Deed"; // �w�N
	private static final String ITEM_GRAD_DATE_DEED = "GradDate_Deed"; // �C����
	private static final String ITEM_SCHOOL_NAME_DEED = "SchoolName_Deed"; // �w�Z��
	private static final String ITEM_PRINCIPAL_NAME_DEED = "PrincipalName_Deed"; // �Z����
	private static final String ITEM_PRINCIPAL_STAMP_DEED = "PrincipalStamp_Deed"; // �Z����

	// �w�K�̋L�^
	private static final String ITEM_TERM_SCORE = "Term_Score"; // �o�͎���
	private static final String ITEM_STUDENT_NUMBER_SCORE = "StudentNumber_Score"; // ��
	private static final String ITEM_STUDENT_NAME_SCORE = "StudentName_Score"; // ������
	private static final String ITEM_TANNIN_NAME_SCORE = "TanninName_Score"; // �S�C��

	private static final String ITEM_SCORE_LIST_SCORE = "ScoreList_Score"; // �w�K�̋L�^���X�g
	private static final String ITEM_SCORE_LINE_SCORE = "ScoreList_Line"; // �w�K�̋L�^���X�g�g
	private static final Integer ITEM_SCORE_MAX_LINE = 45; // �w�K�̋L�^���X�g�̍ő�s��
	private static final int SCORE_COL_CELL0 = 0; // ����
	private static final int SCORE_COL_CELL1 = 1; // �w�K�̖ڕW
	private static final int SCORE_COL_CELL2 = 2; // �]��

	private static final String ITEM_TOTAL_LIST_SCORE = "TotalList_Score"; // �s���̋L�^���X�g
	private static final String ITEM_TOTAL_LINE_SCORE = "TotalList_Line"; // �s���̋L�^���X�g�g
	private static final Integer ITEM_TOTAL_MAX_LINE = 15; // �w�K�̋L�^���X�g�̍ő�s��
	private static final int TOTAL_COL_CELL0 = 0; // �s���̊ϓ_
	private static final int TOTAL_COL_CELL1 = 1; // �s���̕]��

	private static final String ITEM_ATTEND_LINE_SCORE = "Attend_Score"; // �o���̋L�^���X�g
	private static final Integer ITEM_ATTEND_MAX_LINE = 7; // �o���̋L�^���X�g�̍ő�s��
	private static final int ATTEND_COL_CELL0 = 0; // ��
	private static final int ATTEND_COL_CELL1 = 1; // ���Ɠ���
	private static final int ATTEND_COL_CELL2 = 2; // �o�����
	private static final int ATTEND_COL_CELL3 = 3; // ����
	private static final int ATTEND_COL_CELL4 = 4; // �o�ȓ���
	private static final int ATTEND_COL_CELL5 = 5; // �x��
	private static final int ATTEND_COL_CELL6 = 6; // ����

	private static final String ITEM_TOTAL_OPINION = "Total_Opinion"; // ��������
	private static final String ITEM_HR_ACTIONTITLE = "HomeRoom_ActionTitle"; // ���ʊ����̃^�C�g��
	private static final String ITEM_HR_ACTION = "HomeRoom_Action"; // ���ʊ����̋L�^
	private static final String ITEM_STUDENT_HEIGHT = "Student_Height"; // �g��
	private static final String ITEM_STUDENT_WEIGHT = "Student_Weight"; // �̏d
	private static final String ITEM_TOTAL_STUDY = "Total_Stady"; // �����I�Ȋw�K�̋L�^
	private static final String ITEM_FOREIGN_LANG = "Foreign_Lang"; // �O���ꊈ���̋L�^

	// ����
	private static final String ITEM_PREVIOUS = "_Previous"; // �O��
	private static final String ITEM_LATE = "_Late"; // ���

	/** ���t�����ݒ� */
	private DateFormatUtility dfu = null;

	/** ���FormBean */
	private Print32179000FormBean printFormBean;

	/** ���I�ɕ���ύX���郊�X�g�t�B�[���h�̕��A�����̏����l */
	private Integer scoreListFieldDefaultRow;
	private Integer scoreListFieldDefaultHeight;

	private Integer actListFieldDefaultHeight;
	private Integer actListFieldDefaultRow;

	private Integer attendListFieldDefaultHeight;
	private Integer attendListFieldDefaultRow;

	/** ���C���[�\���ڍ׎�ʊi�[�p */
	private String layerDetail = "";
	/** ���C���[�󎚎�ʊi�[�p */
	private String layerPrint = "";
	/** ���C���[�C���؎�ʊi�[�p */
	private String layerDeed = "";
	/** ���C���[�L�^��ʊi�[�p */
	private String layerScore = "";
	/** ���C���[�w�N�i�[�p */
	private String layerGrade = "";

	@Override
	protected void doPrint() throws TnaviPrintException {
		try {

			Map<String, Print32179000StudentFormBean> studentMap = printFormBean.getStudentMap();
			Map<String, Print32179000StudentFormBean> deedStudentMap = printFormBean.getDeedStudentMap();

			dfu = new DateFormatUtility(printFormBean.getUserCode());
			dfu.setZeroToSpace(true);

			/**
			 * ���C���[�ڍ׎��
			 */
			if (printFormBean.isSpgdFlg()) {
				// ���ʎx���w��
				this.layerDetail = LAYER_DETAIL_SPGD;
				this.layerDeed = LAYER_SPGD;

				/**
				 * ���C���[�𗬎��
				 */
				if (Print32179000FormBean.OUTPUT_ALTERNAT_PRINT.equals(printFormBean.getAlternatFlg())) {
					// �𗬊w�����̈�
					this.layerPrint = LAYER_SPGD_2;
				} else if (Print32179000FormBean.OUTPUT_NOMAL_PRINT.equals(printFormBean.getAlternatFlg())) {
					// �ʏ�w�����̈�
					this.layerPrint = LAYER_SPGD_1;
				} else if (Print32179000FormBean.OUTPUT_BOTH_PRINT.equals(printFormBean.getAlternatFlg())) {
					// �𗬊w���y�ђʏ�w�����̈�
					this.layerPrint = LAYER_SPGD_3;
				}
			} else {
				// �ʏ�w��
				this.layerDetail = LAYER_DETAIL_STANDARD;
				this.layerDeed = LAYER_STANDARD;
				// �𗬎�ʂȂ�
				this.layerPrint = "";
			}

			for (String entrySet : printFormBean.getStudentList()) {

				// ���k���
				Print32179000StudentFormBean student = studentMap.get(entrySet);

				// �\��
				if (printFormBean.isOutput_cover()) {
					printOutputCover(student);
				}

				// �w�K�̋L�^
				if (printFormBean.isOutput_score()) {
					printOutputScore(student);
				}

				// �C���؏o��
				if (printFormBean.isOutput_deed()) {
					outputDeed(deedStudentMap.get(entrySet));
				}
			}

		} catch (Exception e) {
			log.error("��O�����@���[�쐬���s", e);
			throw new TnaviPrintException(e);
		}
	}

	/**
	 * ���FormBean��ݒ肷��.
	 * 
	 * @param printFormBean the printFormBean to set
	 */
	public void setPrintFormBean(Print32179000FormBean printFormBean) {
		this.printFormBean = printFormBean;
	}

	/**
	 * �\���̏o�͂��s��.
	 * 
	 * @param student ���k���
	 */
	private void printOutputCover(Print32179000StudentFormBean student) throws Exception {

		// �S���C���[��\��
		setVisibleAtPrintForAllLayout(false);
		// �\�����C���[�\��
		getCrLayerSetVisibleAtPrint(LAYER_COVER, true);
		getCrLayerSetVisibleAtPrint(LAYER_COVER + layerDetail, true);
		getCrLayerSetVisibleAtPrint(LAYER_COVER + layerDetail + this.layerPrint, true);

		// �N�x
		String nendo = dfu.formatDate("YYYY", 1, printFormBean.getNendo());
		StringBuffer outputNendo = new StringBuffer(nendo).insert(2, " ").append(" �N�x"); // �����ƔN�x�̊ԁA�N�x�̌��ɔ��p�X�y�[�X��}�����Ĕz�u�𒲐�
		getExistsFieldSetData(ITEM_NENDO_COVER, outputNendo.toString());

		// �ʒm�[�\��摜
		if (objectExists(ITEM_SCHOOL_STAMP02_COVER)) {
			if (printFormBean.getStampImage() != null) {
				setImageFieldDataCenterling(ITEM_SCHOOL_STAMP02_COVER, printFormBean.getStampImage());
			}
		}

		// �\���摜
		if (objectExists(ITEM_SCHOOL_STAMP04_COVER)) {
			if (printFormBean.getCoverImage() != null) {
				setImageFieldDataCenterling(ITEM_SCHOOL_STAMP04_COVER, printFormBean.getCoverImage());
			}
		}

		// �w�Z��
		getExistsFieldSetData(ITEM_SCHOOL_NAME_COVER, printFormBean.getSchoolName());

		// ��������
		getExistsFieldSetData(ITEM_STUDENT_NAME_COVER + this.layerDeed, getOutputStuName(student));

		// �Z����
		getExistsFieldSetData(ITEM_PRINCIPAL_NAME_COVER + this.layerDeed,
				getStaffNames(printFormBean.getPrincipalList()));

		// �N�E�g
		String stuGrade = "�� " + student.getStuGrade() + " �w�N ";
		String stuClass = student.getStuClass() + " �g";

		// �w���A�S�C����
		if (printFormBean.isSpgdFlg()) {

			stuGrade = "�� " + student.getStuGrade() + " �w�N ";
			stuClass = student.getExcClass() + " �g";

			/** ���C���[�𗬎�� */
			if (Print32179000FormBean.OUTPUT_ALTERNAT_PRINT.equals(printFormBean.getAlternatFlg())) {
				// �𗬊w�����̈�

				// �w�N�E�w��
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_ALTERNAT + ITEM_DISP_NO1, stuGrade + stuClass);
				// �S�C��
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_ALTERNAT + ITEM_DISP_NO1,
						getStaffNames(student.getStaffList()));
			} else if (Print32179000FormBean.OUTPUT_NOMAL_PRINT.equals(printFormBean.getAlternatFlg())) {
				// �ʏ�w�����̈�
				// �w�N�E�w��
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_SPGD + ITEM_DISP_NO1, student.getSpGroupname());
				// �S�C��
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_SPGD + ITEM_DISP_NO1,
						getStaffNames(student.getSpStaffList()));
			} else if (Print32179000FormBean.OUTPUT_BOTH_PRINT.equals(printFormBean.getAlternatFlg())) {
				// �𗬊w���y�ђʏ�w�����̈�
				// �w�N�E�w��
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_SPGD + ITEM_DISP_NO2, student.getSpGroupname());
				// �S�C��
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_SPGD + ITEM_DISP_NO2,
						getStaffNames(student.getSpStaffList()));
				// �S�C��
				getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_ALTERNAT + ITEM_DISP_NO2,
						getStaffNames(student.getStaffList()));
				// �w�N�E�w��
				getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_ALTERNAT + ITEM_DISP_NO2, stuGrade + stuClass);
			}
		} else {
			// �ʏ�w�����̈�
			// �w�N�E�w��
			getExistsFieldSetData(ITEM_STUDENT_CLASS_COVER + ITEM_STANDARD, stuGrade + stuClass);
			// �S�C��
			getExistsFieldSetData(ITEM_TANNIN_NAME_COVER + ITEM_STANDARD, getStaffNames(student.getStaffList()));
		}

		// �o��
		form.printOut();
		// �t�H�[��������
		form.initialize();
	}

	/**
	 * �C���؂̏o�͂��s��.
	 * 
	 * @param student ���k���
	 * @throws Exception �o�͎���O
	 */
	private void outputDeed(Print32179000StudentFormBean student) throws Exception {

		// �S���C���[��\��
		setVisibleAtPrintForAllLayout(false);
		// �C���؃��C���[�\��
		getCrLayerSetVisibleAtPrint(LAYER_DEED + this.layerDeed, true);
		// �ʏ�w���̏ꍇ
		if (!printFormBean.isSpgdFlg()) {
			// �C���؃��C���[�\��
			getCrLayerSetVisibleAtPrint(LAYER_DEED + LAYER_MIKATA + "_" + printFormBean.getUserCode(), true);
		}

		// ������
		getExistsFieldSetData(ITEM_STUDENT_NAME_DEED + this.layerDeed, getOutputStuName(student));

		// �C����
		getExistsFieldSetData(ITEM_GRAD_DATE_DEED + this.layerDeed,
				dfu.formatDate("YYYY�NMM��DD��", 1, printFormBean.getDeedDate()));

		// �w�N
		getExistsFieldSetData(ITEM_GRAD_GRADE_DEED + this.layerDeed, student.getStuGrade());

		// �Z����
		getExistsFieldSetData(ITEM_PRINCIPAL_NAME_DEED + this.layerDeed, printFormBean.getDeedPrincipalName());

		// �Z����(����̂ݕ\��)
		if (printFormBean.getTerm().equals("02") && objectExists(ITEM_PRINCIPAL_STAMP_DEED + this.layerDeed)) {
			CrImageField imageField = (CrImageField) form.getCrObject(ITEM_PRINCIPAL_STAMP_DEED + this.layerDeed);
			if (printFormBean.getPrincipalStampImage() != null) {
				imageField.setData(printFormBean.getPrincipalStampImage());
			}
		}

		// �w�Z��
		StringBuffer buffer = new StringBuffer();
		buffer.append(printFormBean.getSchoolName());
		buffer.append("��");
		getExistsFieldSetData(ITEM_SCHOOL_NAME_DEED + this.layerDeed, buffer.toString());

		// �o��
		form.printOut();
		// �t�H�[��������
		form.initialize();
	}

	/**
	 * �w�K�̋L�^�̏o�͂��s��.
	 * 
	 * @param student ���k���
	 * @throws �o�͎���O
	 */
	private void printOutputScore(Print32179000StudentFormBean student) throws Exception {

		// �o�͎���
		if (Print32179000FormBean.OUTPUT_TERM_PREVIOUS.equals(printFormBean.getTerm())) {
			this.layerScore = ITEM_PREVIOUS;
		} else {
			this.layerScore = ITEM_LATE;
		}
		// �o�͊w�N
		if (Print32179000FormBean.OUTPUT_GRADE_01.equals(student.getGrade())
				|| Print32179000FormBean.OUTPUT_GRADE_02.equals(student.getGrade())) {
			this.layerGrade = LAYER_GRADE_12;
		} else if (Print32179000FormBean.OUTPUT_GRADE_03.equals(student.getGrade())
				|| Print32179000FormBean.OUTPUT_GRADE_04.equals(student.getGrade())) {
			this.layerGrade = LAYER_GRADE_34;
		} else {
			this.layerGrade = LAYER_GRADE_56;
		}
		// �S���C���[��\��
		setVisibleAtPrintForAllLayout(false);
		// �w�K�̋L�^���C���[�\��
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + LAYER_HEADER, true);
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + this.layerGrade, true);
		getCrLayerSetVisibleAtPrint(LAYER_SCORE + this.layerScore, true);

		// �o�͎���
		getExistsFieldSetData(ITEM_TERM_SCORE, printFormBean.getTermName());
		// �w�N
		if ("1".equals(student.getIsKoryu())) {
			getExistsFieldSetData(ITEM_STUDENT_NUMBER_SCORE, student.getExcNumber());
		} else {
			getExistsFieldSetData(ITEM_STUDENT_NUMBER_SCORE, student.getStuNumber());
		}
		// ������
		getExistsFieldSetData(ITEM_STUDENT_NAME_SCORE, getOutputStuName(student));

		// �S�C��
		getExistsFieldSetData(ITEM_TANNIN_NAME_SCORE, getStaffNamesAlline(student.getStaffList()));

		// ���Ȃ̊w�K�̋L�^
		printScoreList(student);

		// �s���̋L�^
		printActiveList(student);

		// �o���̋L�^
		printAttendList(student);

		// ���ʊ����̋L�^
		StringBuffer spcialAction = new StringBuffer(); // �L�^
		StringBuffer spcialTitle = new StringBuffer(); // ���ʊ�������
		for (Print32179000SpActEntity entity : student.getSpActList()) {
			// ��łȂ����"/"�ǉ�
			if (spcialTitle != null && !spcialTitle.toString().equals("")) {
				spcialTitle.append("/");
			}
			if (spcialAction != null && !spcialAction.toString().equals("")) {
				spcialAction.append("/");
			}
			// �^�C�g���i�[
			if (entity.getCod_name1() != null) {
				spcialTitle.append(entity.getCod_name1());
			} else {
				spcialTitle.append("");
			}
			if (entity.getRsav_record() != null) {
				spcialAction.append(entity.getRsav_record());
			} else {
				spcialAction.append("");
			}
		}
		// �e���v���[�g�ɃZ�b�g
		if (spcialTitle != null && !spcialTitle.toString().equals("")) {
			getExistsFieldSetData(ITEM_HR_ACTIONTITLE + this.layerGrade, spcialTitle.toString());
		}
		if (spcialAction != null && !spcialAction.toString().equals("")) {
			getExistsFieldSetData(ITEM_HR_ACTION + this.layerGrade, spcialAction.toString());
		}

		// ��������
		if (student.getComment() != null && student.getComment().getRcom_comment() != null) {
			getExistsFieldSetData(ITEM_TOTAL_OPINION + this.layerGrade, student.getComment().getRcom_comment());
		}

		// ����̋L�^
		if (student.getHealthrecord() != null) {
			// �g��
			if (student.getHealthrecord().getHlr_height() != null) {
				getExistsFieldSetData(ITEM_STUDENT_HEIGHT + this.layerGrade, student.getHealthrecord().getHlr_height());
			} else {
				getExistsFieldSetData(ITEM_STUDENT_HEIGHT + this.layerGrade, "");
			}

			// �̏d
			if (student.getHealthrecord().getHlr_weight() != null) {
				getExistsFieldSetData(ITEM_STUDENT_WEIGHT + this.layerGrade, student.getHealthrecord().getHlr_weight());
			} else {
				getExistsFieldSetData(ITEM_STUDENT_WEIGHT + this.layerGrade, "");
			}
		}

		// �����I�Ȋ����̋L�^
		if (student.getTotalAct() != null && student.getTotalAct().getRtav_value() != null) {
			getExistsFieldSetData(ITEM_TOTAL_STUDY + this.layerGrade, student.getTotalAct().getRtav_value());
		}

		// �O���ꊈ���̋L�^
		StringBuffer foreignLang = new StringBuffer(); // ���ʊ�������
		for (Print32179000ForeignLangActEntity entity : student.getForeignLangActList()) {
			// ��łȂ����"/"�ǉ�
			if (foreignLang != null && !foreignLang.toString().equals("")) {
				foreignLang.append("/");
			}
			// �^�C�g���i�[
			if (entity.getRfla_eval() != null) {
				foreignLang.append(entity.getRfla_eval());
			} else {
				foreignLang.append("");
			}
		}
		// �e���v���[�g�ɃZ�b�g
		if (spcialTitle != null && !spcialTitle.toString().equals("")) {
			getExistsFieldSetData(ITEM_FOREIGN_LANG + this.layerGrade, foreignLang.toString());
		}

		// �o��
		form.printOut();
		// �t�H�[��������
		form.initialize();
	}

	/**
	 * �I�u�W�F�N�g�̑��݃`�F�b�N��ɒl���Z�b�g����.
	 * 
	 * @param objectName �I�u�W�F�N�g��
	 * @param value      �o�͂���l
	 * @throws Exception
	 */
	private void getExistsFieldSetData(String objectName, Object value) throws Exception {
		if (objectExists(objectName)) {
			getFieldSetData(objectName, value);
		}
	}

	/**
	 * �S���C����\���E��\���ɂ���.
	 * 
	 * @param flag �\���E��\���t���O
	 */
	private void setVisibleAtPrintForAllLayout(boolean flag) {
		CrLayers crLayers = form.getCrLayers();
		for (Iterator<CrLayer> i = crLayers.iterator(); i.hasNext();) {
			CrLayer layer = i.next();
			layer.setVisibleAtPrint(flag);
		}
	}

	/**
	 * ���k�����̌ːЁE�ʏ̔���
	 * 
	 * @param student
	 * @return
	 */
	private String getOutputStuName(Print32179000StudentFormBean student) {
		return student.getStuName();
	}

	/**
	 * �����̃��X�g���Â����ɕ��ׂ���������擾����
	 * 
	 * @param staffList �������X�g
	 * @return �ϊ��㋳����
	 */
	private String getStaffNames(List<StaffEntity> staffList) {
		StringBuffer sb = new StringBuffer();
		// �V�������ɕ���ł��邽�߁A�t����
		if (staffList != null) {
			for (int i = staffList.size() - 1; i >= 0; i--) {
				if (sb.length() > 0) {
					sb.append("\r\n");
				}
				// �����͊������g�p
				sb.append(staffList.get(i).getStf_name_w());
			}
		}

		return sb.toString();
	}

	/**
	 * �����̃��X�g���Â����ɕ��ׂ���������擾����
	 * 
	 * @param staffList �������X�g
	 * @return �ϊ��㋳����
	 */
	private String getStaffNamesAlline(List<StaffEntity> staffList) {
		StringBuffer sb = new StringBuffer();
		// �V�������ɕ���ł��邽�߁A�t����
		for (int i = staffList.size() - 1; i >= 0; i--) {
			if (sb.length() > 0) {
				sb.append(" �E ");
			}
			// �����͊������g�p
			sb.append(staffList.get(i).getStf_name_w());
		}

		return sb.toString();
	}

	/**
	 * �c�����̃Z���p�ɁA1�������ɉ��s��}�����đ̍ق𐮂���
	 * 
	 * @param itemName ���`�Ώۂ̕�����
	 * @param rowSize  �o�͍s�T�C�Y
	 * @return ���`��̕�����
	 */
	private String arrangeVertical(String itemName, int rowSize) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < itemName.length(); i++) {
			sb.append(itemName.charAt(i));
			if (i < itemName.length() - 1) {
				// 1�������Ƃɉ��s�R�[�h��}������
				sb.append("\r\n");
				if (itemName.length() == 2) {
					for (int j = 0; j < (rowSize - 3); j++) {
						sb.append("\r\n");
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * ���Ȃ̊w�K�̋L�^
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printScoreList(Print32179000StudentFormBean student) throws Exception {

		// ���ȁA�ϓ_�̏o��
		CrListField scoreGridField = getCrListField(ITEM_SCORE_LIST_SCORE + this.layerGrade);
		CrText[][] scoreGrid = getListFieldGetColumns(scoreGridField);
		// --�r���O���b�h
		CrListField lineGridField = getCrListField(ITEM_SCORE_LINE_SCORE + this.layerGrade);

		// �s�̃J�E���g
		int rowCount = 0;
		// �s���擾
		rowCount = student.getScoreList().size();

		if (rowCount > ITEM_SCORE_MAX_LINE)
			rowCount = ITEM_SCORE_MAX_LINE;

		// ���C�A�E�g�̍s������э����ێ�
		if (scoreListFieldDefaultRow == null) {
			scoreListFieldDefaultRow = scoreGrid[0].length;
			scoreListFieldDefaultHeight = scoreGridField.getHeight();
		} else {

			// �����l��������ꍇ�͏��������K�v
			scoreGridField.setHeight(scoreListFieldDefaultHeight);
			scoreGridField.setVisibleRows(scoreListFieldDefaultRow);

			lineGridField.setHeight(scoreListFieldDefaultHeight);
			lineGridField.setVisibleRows(scoreListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// ���C�A�E�g�s���ƍ�������A���ۂ̏o�͍s���ɂ������������Z�o(��\���s�l����������)
		BigDecimal bdMaxrow = new BigDecimal(scoreListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(scoreListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// ���C�A�E�g�s�������ۂ̏o�͍s�Ŋ���ƕK�v�Ȕ{�����o��̂ŁA���C�A�E�g�s���Ɗ|�����킹�Đݒ肷�鍂�����Z�o
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// �����l��������ꍇ�͏��������K�v
		scoreGridField.setHeight(culcHeight);
		lineGridField.setHeight(culcHeight);
		// �\���s���Z�b�g
		scoreGridField.setVisibleRows(rowCount);
		lineGridField.setVisibleRows(rowCount);

		// ���Ȃ��Ƃ̃X�^�[�g�s��ێ�����B�Z���}�[�W�p
		int itemStartRow = 0;

		Map<String, Integer> itemRows = new HashMap<String, Integer>();
		int itemRowCount = 0;
		int i = 0;
		for (Print32179000ScoreEntity bean : student.getScoreList()) {
			if (itemRowCount != 0 && !oldItem.equals(bean.getItemCode())) {
				if (!oldItem.isEmpty()) {
					itemRows.put(oldItem, itemRowCount);
				}
				itemRowCount = 0;
			}
			itemRowCount++;
			oldItem = bean.getItemCode();
			i++;
			if (i == student.getScoreList().size()) {
				itemRows.put(bean.getItemCode(), itemRowCount);
			}
		}
		oldItem = "";

		// �ϓ_���X�g
		for (Print32179000ScoreEntity bean : student.getScoreList()) {
			// �ϓ_�o��
			if (bean != null) {
				// �ő�s����11
				if (rowIndex > ITEM_SCORE_MAX_LINE - 1) {
					break;
				}

				int viewPointSize = 0;
				// �ϓ_�o��
				if (!oldItem.equals(bean.getItemCode())) {

					viewPointSize = itemRows.get(bean.getItemCode()) - 1;// ���Ȃ̊ϓ_��-1�擾

					// ���Ȗ����o��
					if (rowIndex + viewPointSize > ITEM_SCORE_MAX_LINE - 1) {
						setListFieldData(scoreGrid, SCORE_COL_CELL0, rowIndex,
								arrangeVertical(bean.getItemName(), ITEM_SCORE_MAX_LINE - 1 - (rowIndex + 1)));
					} else {
						setListFieldData(scoreGrid, SCORE_COL_CELL0, rowIndex,
								arrangeVertical(bean.getItemName(), viewPointSize + 1));
					}
				}
				// �f�[�^���Z�b�g����B
				// �ϓ_
				setListFieldData(scoreGrid, SCORE_COL_CELL1, rowIndex, bean.getPurpose());
				// �]��
				if (bean.getViewpointvalue() == null || bean.getViewpointvalue().isEmpty()) {
					setListFieldData(scoreGrid, SCORE_COL_CELL2, rowIndex, "");
				} else if (bean.getViewpointvalue().equals("*")) {
					CrObject crObject = scoreGrid[SCORE_COL_CELL2][rowIndex];
					printSlash(crObject, LINE_SLASH, 1, form.getCrLayer(LAYER_SCORE + this.layerGrade));
				} else if ("1".equals(bean.getRvpe_cannot_flg()) || "1".equals(bean.getRvpe_none_flg())) {
					CrObject crObject = scoreGrid[SCORE_COL_CELL2][rowIndex];
					printSlash(crObject, LINE_SLASH, 1, form.getCrLayer(LAYER_SCORE + this.layerGrade));
				} else {
					setListFieldData(scoreGrid, SCORE_COL_CELL2, rowIndex, bean.getViewpointvalue());
				}

				int itemEndRow = 0;
				itemEndRow = itemStartRow + viewPointSize;

				if (!oldItem.equals(bean.getItemCode())) {
					// ������p���X�g�ɑ΂��ăZ���̃}�[�W����������B
					// ����
					listFieldMergeCell(scoreGridField, SCORE_COL_CELL0, itemStartRow, SCORE_COL_CELL0, itemEndRow);

					// ������p���X�g�ɑ΂��Ă̏���
					// ���Ȗ�
					listFieldMergeCell(lineGridField, SCORE_COL_CELL0, itemStartRow, SCORE_COL_CELL0, itemEndRow);
					listFieldMergeCell(lineGridField, SCORE_COL_CELL1, itemStartRow, SCORE_COL_CELL1, itemEndRow);
					listFieldMergeCell(lineGridField, SCORE_COL_CELL2, itemStartRow, SCORE_COL_CELL2, itemEndRow);
				}

				// ���݂̋��Ȃ��擾���āA���O�̋����Ƃ���
				oldItem = bean.getItemCode();
				rowIndex++;
				itemStartRow++;
			}
		}
	}

	/**
	 * �o���̋L�^
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printAttendList(Print32179000StudentFormBean student) throws Exception {

		// �s���̋L�^�̏o��
		CrListField activeGridField = getCrListField(ITEM_ATTEND_LINE_SCORE + this.layerGrade);
		CrText[][] activeGrid = getListFieldGetColumns(activeGridField);

		// �s�̃J�E���g
		int rowCount = 0;

		if ("01".equals(printFormBean.getTerm())) {
			// �s���擾
			rowCount = 6;
		} else {
			rowCount = 7;
		}

		if (rowCount > ITEM_ATTEND_MAX_LINE)
			rowCount = ITEM_ATTEND_MAX_LINE;

		// ���C�A�E�g�̍s������э����ێ�
		if (attendListFieldDefaultRow == null) {
			attendListFieldDefaultRow = activeGrid[0].length;
			attendListFieldDefaultHeight = activeGridField.getHeight();
		} else {

			// �����l��������ꍇ�͏��������K�v
			activeGridField.setHeight(attendListFieldDefaultHeight);
			activeGridField.setVisibleRows(attendListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// ���C�A�E�g�s���ƍ�������A���ۂ̏o�͍s���ɂ������������Z�o(��\���s�l����������)
		BigDecimal bdMaxrow = new BigDecimal(attendListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(attendListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// ���C�A�E�g�s�������ۂ̏o�͍s�Ŋ���ƕK�v�Ȕ{�����o��̂ŁA���C�A�E�g�s���Ɗ|�����킹�Đݒ肷�鍂�����Z�o
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// �����l��������ꍇ�͏��������K�v
		activeGridField.setHeight(culcHeight);
		// �\���s���Z�b�g
		activeGridField.setVisibleRows(rowCount);

		// ���Ȃ��Ƃ̃X�^�[�g�s��ێ�����B�Z���}�[�W�p
		int itemStartRow = 0;

		// �ϓ_���X�g
		for (Print32179000AttendEntity bean : student.getAttendList()) {
			// �ϓ_�o��
			if (bean != null) {

				if (printFormBean.getTerm().equals(bean.getGoptcode())) {

					// �ő�s����11
					if (rowIndex > ITEM_ATTEND_MAX_LINE - 1) {
						break;
					}

					int viewPointSize = 0;
					// �f�[�^���Z�b�g����B
					// ��
					if (!bean.getMonth().equals("99")) {
						if (bean.getMonth().substring(0, 1).equals("0")) {
							setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex,
									(bean.getMonth().replace("0", "") + "��"));
						} else {
							setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex, (bean.getMonth() + "��"));
						}
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL0, rowIndex, "�N�Ԍv");
					}
					// ���Ɠ���
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL1, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL1, rowIndex, bean.getTeachingSum());
					}
					// �o���������
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL2, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL2, rowIndex, bean.getStopSum());
					}
					// ���ȓ���
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL3, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL3, rowIndex, bean.getAbsenceSum());
					}
					// �o�ȓ���
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL4, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL4, rowIndex, bean.getAttendSum());
					}
					// �x������
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL5, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL5, rowIndex, bean.getLateSum());
					}
					// ���ޓ���
					if (bean.getOnRegist().equals("0")) {// �ݐЂ��Ă��Ȃ����k�̓n�C�t������
						setListFieldData(activeGrid, ATTEND_COL_CELL6, rowIndex, "-");
					} else {
						setListFieldData(activeGrid, ATTEND_COL_CELL6, rowIndex, bean.getLeaveSum());
					}

					int itemEndRow = 0;
					itemEndRow = itemStartRow + viewPointSize;

					if (!oldItem.equals(bean.getMonth())) {
						// ������p���X�g�ɑ΂��ăZ���̃}�[�W����������B
						// ����
						listFieldMergeCell(activeGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);
					}

					// ���݂̋��Ȃ��擾���āA���O�̋����Ƃ���
					oldItem = bean.getMonth();
					rowIndex++;
					itemStartRow++;
				}
			}
		}
	}

	/**
	 * �s���̋L�^
	 * 
	 * @param student
	 * @throws Exception
	 */
	private void printActiveList(Print32179000StudentFormBean student) throws Exception {
		// �s���̋L�^�̏o��
		CrListField activeGridField = getCrListField(ITEM_TOTAL_LIST_SCORE + this.layerGrade);
		CrText[][] activeGrid = getListFieldGetColumns(activeGridField);
		// --�r���O���b�h
		CrListField lineGridField = getCrListField(ITEM_TOTAL_LINE_SCORE + this.layerGrade);

		// �s�̃J�E���g
		int rowCount = 0;
		// �s���擾
		rowCount = student.getActviewList().size();

		if (rowCount > ITEM_TOTAL_MAX_LINE)
			rowCount = ITEM_TOTAL_MAX_LINE;

		if (rowCount == 0)
			rowCount = ITEM_TOTAL_MAX_LINE;

		// ���C�A�E�g�̍s������э����ێ�
		if (actListFieldDefaultRow == null) {
			actListFieldDefaultRow = activeGrid[0].length;
			actListFieldDefaultHeight = activeGridField.getHeight();
		} else {

			// �����l��������ꍇ�͏��������K�v
			activeGridField.setHeight(actListFieldDefaultHeight);
			activeGridField.setVisibleRows(actListFieldDefaultRow);

			lineGridField.setHeight(actListFieldDefaultHeight);
			lineGridField.setVisibleRows(actListFieldDefaultRow);
		}

		int rowIndex = 0;
		String oldItem = "";

		// ���C�A�E�g�s���ƍ�������A���ۂ̏o�͍s���ɂ������������Z�o(��\���s�l����������)
		BigDecimal bdMaxrow = new BigDecimal(actListFieldDefaultRow);
		BigDecimal bdMaxheight = new BigDecimal(actListFieldDefaultHeight);
		BigDecimal bdRowCount = new BigDecimal(rowCount);

		// ���C�A�E�g�s�������ۂ̏o�͍s�Ŋ���ƕK�v�Ȕ{�����o��̂ŁA���C�A�E�g�s���Ɗ|�����킹�Đݒ肷�鍂�����Z�o
		int culcHeight = (bdMaxheight.multiply(bdMaxrow.divide(bdRowCount, 4, BigDecimal.ROUND_DOWN))).intValue();

		// �����l��������ꍇ�͏��������K�v
		activeGridField.setHeight(culcHeight);
		lineGridField.setHeight(culcHeight);
		// �\���s���Z�b�g
		activeGridField.setVisibleRows(rowCount);
		lineGridField.setVisibleRows(rowCount);

		// ���Ȃ��Ƃ̃X�^�[�g�s��ێ�����B�Z���}�[�W�p
		int itemStartRow = 0;

		// �ϓ_���X�g
		for (Print32179000ActviewEntity bean : student.getActviewList()) {
			// �ϓ_�o��
			if (bean != null) {
				// �ő�s����11
				if (rowIndex > ITEM_SCORE_MAX_LINE - 1) {
					break;
				}

				int viewPointSize = 0;
				// �f�[�^���Z�b�g����B
				// �ϓ_
				setListFieldData(activeGrid, TOTAL_COL_CELL0, rowIndex, bean.getRavtname());
				// �]��
				setListFieldData(activeGrid, TOTAL_COL_CELL1, rowIndex, bean.getReportdisplay());

				int itemEndRow = 0;
				itemEndRow = itemStartRow + viewPointSize;

				if (!oldItem.equals(bean.getGavtcode())) {
					// ������p���X�g�ɑ΂��ăZ���̃}�[�W����������B
					// ����
					listFieldMergeCell(activeGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);

					// ������p���X�g�ɑ΂��Ă̏���
					// ���Ȗ�
					listFieldMergeCell(lineGridField, TOTAL_COL_CELL0, itemStartRow, TOTAL_COL_CELL0, itemEndRow);
					listFieldMergeCell(lineGridField, TOTAL_COL_CELL1, itemStartRow, TOTAL_COL_CELL1, itemEndRow);
				}

				// ���݂̋��Ȃ��擾���āA���O�̋����Ƃ���
				oldItem = bean.getGavtcode();
				rowIndex++;
				itemStartRow++;
			}
		}
	}
}
