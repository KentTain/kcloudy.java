package kc.thirdparty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@lombok.extern.slf4j.Slf4j
public class POIExcelAbstract {
	/**
	 * 年月日时分秒 默认格式
	 */
	public static SimpleDateFormat COMMON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 时间 默认格式
	 */
	public static SimpleDateFormat COMMON_TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	/**
	 * 年月日 默认格式
	 */
	public static SimpleDateFormat COMMON_DATE_FORMAT_NYR = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 年月 默认格式
	 */
	public static SimpleDateFormat COMMON_DATE_FORMAT_NY = new SimpleDateFormat("yyyy-MM");
	/**
	 * 月日 默认格式
	 */
	public static SimpleDateFormat COMMON_DATE_FORMAT_YR = new SimpleDateFormat("MM-dd");
	/**
	 * 月 默认格式
	 */
	public static SimpleDateFormat COMMON_DATE_FORMAT_Y = new SimpleDateFormat("MM");
	/**
	 * 星期 默认格式
	 */
	public static String COMMON_DATE_FORMAT_XQ = "星期";
	/**
	 * 周 默认格式
	 */
	public static String COMMON_DATE_FORMAT_Z = "周";
	/**
	 * 07版时间(非日期) 总time
	 */
	public static List<Short> EXCEL_FORMAT_INDEX_07_TIME = Arrays.asList(
			new Short[]{18, 19, 20, 21, 32, 33, 45, 46, 47, 55, 56, 176, 177, 178, 179, 180, 181,
					182, 183, 184, 185, 186}
	);
	/**
	 * 07版日期(非时间) 总date
	 */
	public static List<Short> EXCEL_FORMAT_INDEX_07_DATE = Arrays.asList(
			new Short[]{14, 15, 16, 17, 22, 30, 31, 57, 58, 187, 188, 189, 190, 191, 192, 193,
					194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208}
	);
	/**
	 * 03版时间(非日期) 总time
	 */
	public static List<Short> EXCEL_FORMAT_INDEX_03_TIME = Arrays.asList(
			new Short[]{18, 19, 20, 21, 32, 33, 45, 46, 47, 55, 56, 176, 177, 178, 179, 180, 181,
					182, 183, 184, 185, 186}
	);
	/**
	 * 07版日期(非日期) 总date
	 */
	public static List<Short> EXCEL_FORMAT_INDEX_03_DATE = Arrays.asList(
			new Short[]{14, 15, 16, 17, 22, 30, 31, 57, 58, 187, 188, 189, 190, 191, 192, 193,
					194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208}
	);
	/**
	 * date-年月日时分秒
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_NYRSFM_STRING = Arrays.asList(
			"yyyy/m/d\\ h:mm;@", "m/d/yy h:mm", "yyyy/m/d\\ h:mm\\ AM/PM",
			"[$-409]yyyy/m/d\\ h:mm\\ AM/PM;@", "yyyy/mm/dd\\ hh:mm:dd", "yyyy/mm/dd\\ hh:mm",
			"yyyy/m/d\\ h:m", "yyyy/m/d\\ h:m:s", "yyyy/m/d\\ h:mm", "m/d/yy h:mm;@", "yyyy/m/d\\ h:mm\\ AM/PM;@",
			"yyyy\\-mm\\-dd hh:mm:dd;@","yyyy\\/mm\\/dd hh:mm:dd;@"
	);
	/**
	 * date-年月日
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_NYR_STRING = Arrays.asList(
			"m/d/yy", "[$-F800]dddd\\,\\ mmmm\\ dd\\,\\ yyyy",
			"[DBNum1][$-804]yyyy\"年\"m\"月\"d\"日\";@", "yyyy\"年\"m\"月\"d\"日\";@", "yyyy/m/d;@", "yy/m/d;@", "m/d/yy;@",
			"[$-409]d/mmm/yy", "[$-409]dd/mmm/yy;@", "reserved-0x1F", "reserved-0x1E", "mm/dd/yy;@", "yyyy/mm/dd", "d-mmm-yy",
			"[$-409]d\\-mmm\\-yy;@", "[$-409]d\\-mmm\\-yy", "[$-409]dd\\-mmm\\-yy;@", "[$-409]dd\\-mmm\\-yy",
			"[DBNum1][$-804]yyyy\"年\"m\"月\"d\"日\"", "yy/m/d", "mm/dd/yy", "dd\\-mmm\\-yy","yyyy\\-mm\\-dd;@","yyyy\\/mm\\/dd;@"
	);
	/**
	 * date-年月
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_NY_STRING = Arrays.asList(
			"[DBNum1][$-804]yyyy\"年\"m\"月\";@", "[DBNum1][$-804]yyyy\"年\"m\"月\"",
			"yyyy\"年\"m\"月\";@", "yyyy\"年\"m\"月\"", "[$-409]mmm\\-yy;@", "[$-409]mmm\\-yy",
			"[$-409]mmm/yy;@", "[$-409]mmm/yy", "[$-409]mmmm/yy;@","[$-409]mmmm/yy",
			"[$-409]mmmmm/yy;@", "[$-409]mmmmm/yy", "mmm-yy", "yyyy/mm", "mmm/yyyy",
			"[$-409]mmmm\\-yy;@", "[$-409]mmmmm\\-yy;@", "mmmm\\-yy", "mmmmm\\-yy"
	);
	/**
	 * date-月日
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_YR_STRING = Arrays.asList(
			"[DBNum1][$-804]m\"月\"d\"日\";@", "[DBNum1][$-804]m\"月\"d\"日\"",
			"m\"月\"d\"日\";@", "m\"月\"d\"日\"", "[$-409]d/mmm;@", "[$-409]d/mmm",
			"m/d;@", "m/d", "d-mmm", "d-mmm;@", "mm/dd", "mm/dd;@", "[$-409]d\\-mmm;@", "[$-409]d\\-mmm"
	);
	/**
	 * date-星期X
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_XQ_STRING = Arrays.asList("[$-804]aaaa;@", "[$-804]aaaa");
	/**
	 * date-周X
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_Z_STRING = Arrays.asList("[$-804]aaa;@", "[$-804]aaa");
	/**
	 * date-月X
	 */
	public static List<String> EXCEL_FORMAT_INDEX_DATE_Y_STRING = Arrays.asList("[$-409]mmmmm;@","mmmmm","[$-409]mmmmm");
	/**
	 * time - 时间
	 */
	public static List<String> EXCEL_FORMAT_INDEX_TIME_STRING = Arrays.asList(
			"mm:ss.0", "h:mm", "h:mm\\ AM/PM", "h:mm:ss", "h:mm:ss\\ AM/PM",
			"reserved-0x20", "reserved-0x21", "[DBNum1]h\"时\"mm\"分\"", "[DBNum1]上午/下午h\"时\"mm\"分\"", "mm:ss",
			"[h]:mm:ss", "h:mm:ss;@", "[$-409]h:mm:ss\\ AM/PM;@", "h:mm;@", "[$-409]h:mm\\ AM/PM;@",
			"h\"时\"mm\"分\";@", "h\"时\"mm\"分\"\\ AM/PM;@", "h\"时\"mm\"分\"ss\"秒\";@", "h\"时\"mm\"分\"ss\"秒\"_ AM/PM;@",
			"上午/下午h\"时\"mm\"分\";@", "上午/下午h\"时\"mm\"分\"ss\"秒\";@", "[DBNum1][$-804]h\"时\"mm\"分\";@",
			"[DBNum1][$-804]上午/下午h\"时\"mm\"分\";@", "h:mm AM/PM","h:mm:ss AM/PM", "[$-F400]h:mm:ss\\ AM/PM"
	);
	/**
	 * date-当formatString为空的时候-年月
	 */
	public static Short EXCEL_FORMAT_INDEX_DATA_EXACT_NY = 57;
	/**
	 * date-当formatString为空的时候-月日
	 */
	public static Short EXCEL_FORMAT_INDEX_DATA_EXACT_YR = 58;
	/**
	 * time-当formatString为空的时候-时间
	 */
	public static List<Short> EXCEL_FORMAT_INDEX_TIME_EXACT = Arrays.asList(new Short[]{55, 56});
	/**
	 * 格式化星期或者周显示
	 */
	public static String[] WEEK_DAYS = { "日", "一", "二", "三", "四", "五", "六" };
	/**
	 * 07版 excel dataformat
	 */
	public static DataFormatter EXCEL_07_DATA_FORMAT = new DataFormatter();
	/**
	 * 小数 正则
	 */
	public static Pattern PATTERN_DECIMAL = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+)$");
	/**
	 * 07版excel后缀名
	 */
	public static String EXCEL_SUFFIX_07 = "xlsx";
	/**
	 * 03版excel后缀名
	 */
	public static String EXCEL_SUFFIX_03 = "xls";

	//private boolean disposed;
	//private InputStream stream = null;
	protected Workbook Workbook;
	public int iMaxCount = 65536;

	public boolean IsXlsx;

	protected POIExcelAbstract(boolean isXlsx) {
		IsXlsx = isXlsx;
		iMaxCount = IsXlsx ? 1048576 : 65536;
		if (IsXlsx) {
			Workbook = new XSSFWorkbook();
		} else {
			Workbook = new HSSFWorkbook();
		}
	}

	protected POIExcelAbstract(byte[] bytes) throws IOException {
		try {
			InputStream input = new ByteArrayInputStream(bytes);
			Workbook = WorkbookFactory.create(input);
			IsXlsx = Workbook instanceof XSSFWorkbook;
			iMaxCount = IsXlsx ? 1048576 : 65536;
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	protected POIExcelAbstract(InputStream stream) throws IOException {
		//this.stream = stream;

		try {
			Workbook = WorkbookFactory.create(stream);
			IsXlsx = Workbook instanceof XSSFWorkbook;
			// iMaxCount = Workbook is XSSFWorkbook ? 1048576 : 65536;
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}

			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 日期转星期
	 * @param date
	 * @return
	 */
	protected String dateToWeek(Date date) {
		if (date == null){
			return "";
		}
		// 获得一个日历
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 指示一个星期中的某天。
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return WEEK_DAYS[w];
	}
}
