package kc.thirdparty;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import kc.framework.extension.DateExtensions;
import kc.framework.extension.StringExtensions;

@lombok.extern.slf4j.Slf4j
public class POIExcelWriter extends POIExcelAbstract {
	public int MaxWidth = 40;

	public POIExcelWriter(boolean isXlsx) {
		super(isXlsx);
	}

	public POIExcelWriter(byte[] bytes) throws IOException {
		super(bytes);
	}

	public POIExcelWriter(InputStream stream) throws IOException {
		super(stream);
	}

	/**
	 * 设置自定义单元格的值
	 * 
	 * @param definedName 自定义单元格的名称
	 * @param value       自定义单元格的值
	 * @throws IOException
	 */
	public void setDefinedNameSingleValue(String definedName, String value) throws IOException {
		try {
			Name name = Workbook.getName(definedName);
			if (StringExtensions.isNullOrEmpty(definedName) || name == null)
				throw new Exception("Missing defined name " + definedName);

			String sheetName = name.getSheetName();
			String formula = name.getRefersToFormula();

			Sheet sheet = Workbook.getSheet(sheetName);
			CellReference cr = new CellReference(formula);
			Row row = sheet.getRow(cr.getRow());
			Cell cell = row.getCell(cr.getCol());
			cell.setCellValue(value);
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	public void setCellValueByName(String workSheetName, String cellName, String cellValue) throws IOException {
		setCellValueByName(workSheetName, cellName, cellValue, null);
	}

	/**
	 * 设置某个单元格（例如：A1）的值
	 * 
	 * @param workSheetName Sheet的名称
	 * @param cellName      单元格的名称（例如：A1）
	 * @param cellValue     单元格的值
	 * @param style         单元格样式
	 * @throws IOException
	 */
	public void setCellValueByName(String workSheetName, String cellName, String cellValue, Object style)
			throws IOException {
		try {
			if (StringExtensions.isNullOrEmpty(workSheetName))
				workSheetName = "Sheet1";

			Sheet sheet = Workbook.getSheet(workSheetName);
			if (sheet == null)
				sheet = Workbook.createSheet(workSheetName);

			CellReference cr = new CellReference(cellName);
			if (StringExtensions.isNullOrEmpty(cellName) || cr.getRow() < 0 || cr.getCol() < 0)
				throw new Exception("Cannot process excel: The excel sheet's cell is empty or wrong.");
			CellStyle cellStyle = null;
			if (style == null) {
				cellStyle = Workbook.createCellStyle();
				cellStyle.setWrapText(true);
			} else {
				cellStyle = (CellStyle) style;
			}
			Row row = sheet.getRow(cr.getRow());
			if (row == null)
				row = sheet.createRow(cr.getRow());
			Cell cell = row.getCell(cr.getCol());
			if (cell == null)
				cell = row.createCell(cr.getCol());
			cell.setCellStyle(cellStyle);
			cell.setCellValue(cellValue);
			int length = getLength(cellValue);
			int width = length <= MaxWidth ? length * 256 : MaxWidth * 256;
			if (sheet.getColumnWidth(cr.getCol()) < width)
				sheet.setColumnWidth(cr.getCol(), width);
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 设置某个Sheet表单（例如：Sheet1）的值
	 * 
	 * @param workSheetName Sheet的名称
	 * @param valueList     设置Sheet表单的值列表： var data = new List List<String/>>()
	 *                      </br>
	 *                      {</br>
	 *                      new List<String/>()
	 *                      {"Column1","Column2","Column3","Column4"},</br>
	 *                      new List<String/>() {"R11","R12","R13","R14"},</br>
	 *                      new List<String/>() {"R21","R22","R23","R24"},</br>
	 *                      new List<String/>() {"R31","R32","R33","R34"},</br>
	 *                      };
	 * @throws IOException
	 */
	public void setWorksheetDataRaw(String workSheetName, List<List<String>> valueList) throws IOException {
		try {
			if (StringExtensions.isNullOrEmpty(workSheetName))
				workSheetName = "Sheet1";

			Sheet sheet = Workbook.getSheet(workSheetName);
			if (sheet == null)
				sheet = Workbook.createSheet(workSheetName);

			int i = 0;
			for (List<String> row : valueList) {
				int j = 0;
				Row dataRow = sheet.createRow(i);
				for (String column : row) {
					Cell newCell = dataRow.createCell(j);
					newCell.setCellValue(column);
					j++;
				}
				i++;
			}
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 设置Sheet表单的值
	 * 
	 * @param workSheetName Sheet的名称
	 * @param startRow      第几行开始
	 * @param startColumn   第几列开始
	 * @param valueList     设置值得对象集合列表
	 * @param style
	 * @throws IOException
	 */
	public void setWorksheetDataRawAndWidth(String workSheetName, int startRow, int startColumn,
			List<List<String>> valueList, Object style) throws IOException {
		try {
			if (StringExtensions.isNullOrEmpty(workSheetName))
				workSheetName = "Sheet1";

			Sheet sheet = Workbook.getSheet(workSheetName);
			if (sheet == null)
				sheet = Workbook.createSheet(workSheetName);

			List<Integer> columnWidths = new ArrayList<Integer>();
			for (int c = 0; c < valueList.get(0).size(); c++)
				columnWidths.add(0);
			CellStyle cellStyle = null;
			if (style == null) {
				cellStyle = Workbook.createCellStyle();
				cellStyle.setWrapText(true);
			} else {
				cellStyle = (CellStyle) style;
			}
			for (List<String> row : valueList) {
				int j = startColumn;
				Row dataRow = sheet.createRow(startRow);
				for (String column : row) {
					Cell newCell = dataRow.createCell(j);
					newCell.setCellValue(column);
					newCell.setCellStyle(cellStyle);
					int length = getLength(column);
					if (columnWidths.get(j) < length)
						columnWidths.set(j, length);
					j++;
				}
				startRow++;
			}

			for (int c = 0; c < columnWidths.size(); c++) {
				int width = columnWidths.get(c) <= MaxWidth ? columnWidths.get(c) * 256 : MaxWidth * 256;
				if (width > sheet.getColumnWidth(c))
					sheet.setColumnWidth(c, width);
			}
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * 设置某个Sheet表单（例如：Sheet1）的值
	 * 
	 * @param workSheetName Sheet的名称
	 * @param objList       设置Sheet表单的值列表
	 * @throws IOException
	 */
	public <T> void setWorksheetObject(String workSheetName, List<T> objList) throws IOException {
		try {
			if (StringExtensions.isNullOrEmpty(workSheetName))
				workSheetName = "Sheet1";

			Sheet sheet = Workbook.getSheet(workSheetName);
			if (sheet == null)
				sheet = Workbook.createSheet(workSheetName);

			CellStyle dateStyle = Workbook.createCellStyle();
			dateStyle.setBorderTop(BorderStyle.THIN);
			dateStyle.setBorderRight(BorderStyle.THIN);
			dateStyle.setBorderLeft(BorderStyle.THIN);
			dateStyle.setBorderBottom(BorderStyle.THIN);
			DataFormat format = Workbook.createDataFormat();
			dateStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));

			int i = 0;
			Class<? extends Object> t = objList.get(0).getClass();
			Field[] properties = t.getDeclaredFields();
			for (T obj : objList) {
				int j = 0;
				Row dataRow = sheet.createRow(i);
				for (Field property : properties) {
					String drValue = property.get(obj).toString();
					Cell newCell = dataRow.createCell(j);
					Class<?> typeString = property.getType();
					switch (typeString.toString()) {
					case "System.String":// 字符串类型
						newCell.setCellValue(drValue);
						break;
					case "System.DateTime":// 日期类型
						newCell.setCellValue(DateExtensions.getDate(drValue));
						newCell.setCellStyle(dateStyle);// 格式化显示
						break;
					case "System.boolean":// 布尔型
						newCell.setCellValue(Boolean.parseBoolean(drValue));
						break;
					case "System.Int16":// 整型
					case "System.Int32":
					case "System.Int64":
					case "System.Byte":
						newCell.setCellValue(Integer.parseInt(drValue));
						break;
					case "System.Decimal":// 浮点型
					case "System.Double":
						newCell.setCellValue(Double.parseDouble(drValue));
						break;
					case "System.DBNull":// 空值处理
						newCell.setCellValue("");
						break;
					default:
						newCell.setCellValue("");
						break;
					}

					j++;
				}
				i++;
			}
		} catch (Exception ex) {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}
			log.error(ex.getMessage(), ex);
		}
	}

	public void MergedRegion(int startRow, int endRow, int startColumn, int endColumn, String workSheetName) {
		if (StringExtensions.isNullOrEmpty(workSheetName))
			workSheetName = "Sheet1";

		Sheet sheet = Workbook.getSheet(workSheetName);
		if (sheet == null)
			sheet = Workbook.createSheet(workSheetName);

		sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startColumn, endColumn));
	}

	/**
	 * 将表单保存为Excel文件（OpenXml的格式--Excel2010以后的版本）
	 * 
	 * @param filePath 保存Excel文件路径
	 * @throws IOException
	 */
	public void SaveAsExcel(String filePath) throws IOException {
		FileOutputStream fs = new FileOutputStream(filePath);
		try {
			Workbook.write(fs);
		} catch (Exception ex) {

			log.error(ex.getMessage(), ex);
		} finally {
			if (Workbook != null) {
				Workbook.close();
				Workbook = null;
			}

			if (fs != null)
				fs.close();
		}
	}

	public int getLength(String value) throws UnsupportedEncodingException {
		String rule = "\\n";
		// 验证
		Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);

		int wrapNumber = matcher.groupCount();

		// String rule2 = "\\d";
		// 验证
		// Pattern pattern2 = Pattern.compile(rule2.toString(),
		// Pattern.CASE_INSENSITIVE);
		Matcher matcher2 = pattern.matcher(value);

		int blankNumber = matcher2.groupCount();

		int length = 0;
		if (wrapNumber == 0)
			return value.getBytes("UTF-8").length + blankNumber * 1;
		List<String> strList = Arrays.asList(value.split("\\n"));
		for (String m : strList) {
			byte[] bytes = null;
			try {
				bytes = m.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			if (bytes != null) {
				int item = bytes.length + blankNumber * 1;
				if (item > length)
					length = item;
			}
		}
		;
		return length;
	}

	public Object setCellStyle(short fontHeight, boolean alignmenCenter) {
		CellStyle style = Workbook.createCellStyle();
		Font font = Workbook.createFont();
		font.setFontHeight(fontHeight);
		font.setFontName("宋体");
		font.setBold(true);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		if (alignmenCenter)
			style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setWrapText(true);
		style.setFont(font);
		return style;
	}

}