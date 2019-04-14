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
 * �o�ȕ���(�ʏ�w���@���R�s�p) �o�͑Ώی��f�[�^�擾Service.
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

	/** SQL�X�g�����O */
	private static final String EXEC_SQL = "cus/oyama/getMonthData32176000.sql";

	/** �����R�[�h */
	protected String user;

	/** �N�x **/
	protected String nendo;

	/** �w�N **/
	private String grade;

	/** �g **/
	private String hmrClass;

	/** �o�͑Ώی� **/
	private String month;

	/** �o�͑Ώی��e�[�u�� �`��HTML������ **/
	private String monthTableHtml;

	/** �o�͑Ώی����� **/
	private int monthDayCount = 0;

	/**
	 * �R���X�g���N�^ �p�����[�^�Z�b�g
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
			// �����x�ƒ��ɂ����镽���̗j�������F�f�t�H���g�l �擾(cod_kind:606 code:007 cod_value3)
			//  0:�ԏ���, 1:������  �����R�[�h�������A�܂���NULL�ł���ꍇ�́u0:�ԏ����v�Ƃ��Ĉ���
			// ---------------------------------------------------
			String termHolidayLblColorFlg = null;
			// �p�����[�^�̐���
			FindCodeByUserAndKindAndCodeService findCodeService = new FindCodeByUserAndKindAndCodeService(this.user,606,"007");

			// �N�G���̎��s(List�`���Ŏ擾)
			findCodeService.execute();
			List<CodeEntity> wk_codeEntity = (List<CodeEntity>)findCodeService.getObj();

			for (CodeEntity entity : wk_codeEntity) {
				termHolidayLblColorFlg = entity.getCod_value3();
			}

			//��`���������A�擾�ł��Ȃ��ꍇ
			if(termHolidayLblColorFlg == null){
				termHolidayLblColorFlg = "0";	// 0�F�ԏ������Z�b�g
			}

			// ---------------------------------------------------
			// �Ώی��f�[�^�擾
			// ---------------------------------------------------
			// �p�����[�^����
			Object[] param = {month.substring(4,month.length()), user, nendo, grade, hmrClass };
			// QueryManager ����
			QueryManager queryManager = new QueryManager(EXEC_SQL, param, MonthDataListEntity.class);
			// �擾�������s
			List<MonthDataListEntity> monthDataList = (List<MonthDataListEntity>)this.executeQuery(queryManager);

			// �ȉ��̏ꍇ�́A�Ώی��̓�����0���A�J�����_�[HTML�ɒ���0�̕������ݒ肵�����I��
			if (0 == monthDataList.size()){
				// �w�������݂��Ȃ��ꍇ
				monthDayCount = 0;
				monthTableHtml = "";
				return;
			}else if (monthDataList.get(0).getEnf_day() == null){
				// �w���͑��݂��邪�A���Ɠ��ݒ肪�o�^����Ă��Ȃ��ꍇ
				monthDayCount = 0;
				monthTableHtml = "";
				return;
			}


			// ---------------------------------------------------
			// �x�Ɠ��d���`�F�b�N�ƃf�t�H���g�j�������F�̐ݒ�
			// ---------------------------------------------------
			String compday = "";

			// �f�t�H���g�j�������F�i�[Map [key:�� value:�����F(��:"b" ��:"r")]
			HashMap<String, String> defaultColorMap = new HashMap<String, String>();

			for(int dayIndex = 0; dayIndex < monthDataList.size(); dayIndex++)
			{
				if(dayIndex == 0)
				{
					compday = monthDataList.get(dayIndex).getEnf_day();
				}

				// �f�t�H���g�j�������F�̔���
				setDefaultWeekdayColor(monthDataList.get(dayIndex), termHolidayLblColorFlg, defaultColorMap);

				// ������t�ɕ����̋x�Ɠ����ݒ肳��Ă���ꍇ
				if(dayIndex != 0 && compday.equals(monthDataList.get(dayIndex).getEnf_day()))
				{
					// �j�����ݒ肳��Ă��邽�߁A�j�������F�͐�
					defaultColorMap.put(monthDataList.get(dayIndex).getEnf_day(), "r");

					// �Y������x�Ɠ��̖��̂Ƃ����Z�b�g���A�폜
					String holName = monthDataList.get(dayIndex -1).getHol_name() + "," + monthDataList.get(dayIndex).getHol_name();
					monthDataList.get(dayIndex - 1).setHol_name(holName);

					monthDataList.remove(dayIndex);
					// �C���f�b�N�X����߂�
					dayIndex--;

				}
				compday = monthDataList.get(dayIndex).getEnf_day();
			}
			// �Ώی��̓������擾
			monthDayCount = monthDataList.size();

			// ---------------------------------------------------
			// �Ώی��f�[�^�e�[�u��(HTML) ����
			// ---------------------------------------------------
			int monthDataIndex = 0;
			StringBuilder monthTableString = new StringBuilder();
			if(monthDataList.size() != 0)
			{
				// --- �e�[�u���^�O�Z�b�g
				monthTableString.append("<table border=\"0\" cellspacing=\"0\" cellpadding=\"3\" class=\"list\" style=\"width:580px; table-layout:fixed;\">\n");
				for(MonthDataListEntity monthData : monthDataList)
				{
					// ��
					String day = monthData.getEnf_day();
					if(day.substring(0, 1).equals("0"))
					{
						day = day.substring(1,2);
					}
					// �x�Ɠ�����
					String holidayName = monthData.getHol_name() == null ? "" : monthData.getHol_name();
					// �w�����󎚕���
					String closingClassWord = monthData.getClo_printword() == null ? "" : monthData.getClo_printword();

					// --- ��<TR>�Z�b�g
					monthTableString.append("<tr style=\"height:25px;\"> \n");
					// --- ���Z�b�g
					monthTableString.append("<td style=\"text-align:center;width:30px;\">" + day + "</td>\n");
					// --- �j���Z�b�g
					monthTableString.append("<td id=\"weekday_" + Integer.toString(monthDataIndex) + "\" style=\"text-align:center;width:50px;\">" + monthData.getEnf_weekday() + "</td>\n");
					// --- �x�Ɠ�����
					monthTableString.append("<td style=\"text-align:center;width:150px;\">" + holidayName + "</td>\n");
					// --- �w�����󎚕���
					monthTableString.append("<td style=\"text-align:center;width:250px;\">" + closingClassWord + "</td>\n");

					// --- �j�������F
					monthTableString.append("<td style=\"text-align:center;width:100px;\">");

					/* #7211�̑Ή��ɂ��A����ύX */
//					// ���Ǝ��{���̏ꍇ
//					if(monthData.getEnf_enforce().equals("1"))
//					{
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"0\" onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" checked>�� ");
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"1\" onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\">�� ");
//					}
//					// �x�Ɠ�(���Ǝ��{���łȂ��ꍇ)
//					else if(monthData.getEnf_enforce().equals("0"))
//					{
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"0\"onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" >�� ");
//						monthTableString.append(" <input type= \"radio\" name=\"weekday_color_" + Integer.toString(monthDataIndex)+ "\" value=\"1\"onClick=\"changeColor("+ Integer.toString(monthDataIndex) +")\" checked>�� ");
//					}

					if( "b".equals(defaultColorMap.get(monthData.getEnf_day())) )
					{
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", "checked"));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", ""));

					}
					else if( "r".equals(defaultColorMap.get(monthData.getEnf_day())) )
					{
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", ""));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", "checked"));
					}
					else
					{
						// ���肦�Ȃ����򂾂��O�̂���
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", "checked"));
						monthTableString.append(createWeekdayRadio(monthDataIndex, "��", ""));
					}

					monthTableString.append("</td>\n");
					// --- ��</TR>�Z�b�g
					monthTableString.append("</tr>\n");

					monthDataIndex++;
				}
				// --- �e�[�u���^�O�I��
				monthTableString.append("</table>");
			}
			this.monthTableHtml = monthTableString.toString();
		}
		catch (Exception e)
		{
			// ���[���o�b�N
			log.error("�o�ȕ����@�o�͑Ώی�DB�����Ɏ��s���܂����B");
			log.error(e);
			throw new TnaviDbException(e);
		}
	}

	/**
	 * �j�������F�p�̃��W�I�{�^���𐶐�
	 * @param index �C���f�b�N�X
	 * @param color �u���v�������́u�ԁv���w��
	 * @param checkedValue �`�F�b�N��Ԃɂ���ꍇ��"checked"���w��
	 * @return �j�������F�p�̃��W�I�{�^��(html)
	 */
	private String createWeekdayRadio(int index, String color, String checkedValue){

		// ��
		String black = " <input type= \"radio\" name=\"weekday_color_INDEX\" id=\"black_INDEX\" value=\"0\" onClick=\"changeColor(INDEX)\" CHECKED><label for=\"black_INDEX\">��</label>";
		// ��
		String red   = " <input type= \"radio\" name=\"weekday_color_INDEX\" id=\"red_INDEX\"   value=\"1\" onClick=\"changeColor(INDEX)\" CHECKED><label for=\"red_INDEX\">��</label>";

		String html = black;
		if("��".equals(color)){
			html = black;
		}else if("��".equals(color)){
			html = red;
		}

		// "INDEX" �̕�������������̒l�Œu��
		html = html.replaceAll("INDEX", Integer.toString(index));

		// "CHECKED"�̕�����������̒l�Œu��
		html = html.replaceAll("CHECKED", checkedValue);


		return html;
	}


	/**
	 * �f�t�H���g�j�������F�̔���
	 * @param ent
	 * @param termHolidayLblColorFlg
	 * @param defaultColorMap
	 */
	private void setDefaultWeekdayColor(MonthDataListEntity ent, String termHolidayLblColorFlg, HashMap<String, String> defaultColorMap){

		// HashMap��key�ƂȂ���t
		String day = ent.getEnf_day();

		if ( "2".equals(ent.getHol_kind()) ){
			// --- �x�Ɠ��̎�ʂ��u2:���ԁv
			if ( "1".equals(ent.getHolWeekdayFlg()) ){
				// �y���ł���΁u�ԏ����v
				defaultColorMap.put(day, "r");

			}else{
				// �����ł���ꍇ
				// �ėp�}�X�^ kind:606 code:007 cod_value3�̒l�ɂ�蔻��
				// cod_value3��'0':�ԏ��� cod_value3��'1':������
				if ("0".equals(termHolidayLblColorFlg)){
					defaultColorMap.put(day, "r");
				}else{
					defaultColorMap.put(day, "b");
				}
			}

		}else if ( "1".equals(ent.getHol_kind()) ){
			// --- �x�Ɠ��̎�ʂ��u1:����v
			//  �j���́u�ԏ����v
			defaultColorMap.put(day, "r");

		}else{
			// --- �x�Ɠ��ł͂Ȃ�
			//  ���Ǝ��{���ł���΁u�������v�A���Ǝ��{���łȂ���΁u�ԏ����v
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