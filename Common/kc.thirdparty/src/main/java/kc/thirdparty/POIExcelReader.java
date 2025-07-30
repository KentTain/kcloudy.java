package kc.thirdparty;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.format.CellFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.formula.eval.ErrorEval;

import kc.framework.extension.StringExtensions;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

@lombok.extern.slf4j.Slf4j
public class POIExcelReader extends POIExcelAbstract {
    public POIExcelReader(byte[] bytes) throws IOException {
        super(bytes);
    }

    public POIExcelReader(InputStream stream) throws IOException {
        super(stream);
    }

    public List<String> getWorkSheetNames() {
        List<String> worksheets = new ArrayList<String>();
        int count = Workbook.getNumberOfSheets();
        for (int i = 0; i < count; i++) {
            Sheet name = Workbook.getSheetAt(i);
            String sheetName = name.getSheetName();
            worksheets.add(sheetName);
        }

        return worksheets;
    }

    /**
     * 获取所有的自定义单元格列表及其值
     *
     * @return Dictionary<String, String>
     * @throws IOException
     */
    public Dictionary<String, String> getDefinedNames() throws IOException {
        try {
            Dictionary<String, String> definedNames = new Hashtable<String, String>();
            int count = Workbook.getNumberOfSheets();

            List<? extends Name> names = Workbook.getAllNames();
            for (int i = 0; i < count; i++) {
                for (Name name : names) {
                    String formula = name.getRefersToFormula();

                    Sheet sheet = Workbook.getSheetAt(i);
                    CellReference cr = new CellReference(formula);
                    Row row = sheet.getRow(cr.getRow());
                    Cell cell = row.getCell(cr.getCol());
                    String value = cell.getStringCellValue();

                    definedNames.put(name.getNameName(), value);
                }
            }

            return definedNames;
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }

            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 获取自定义单元格的值
     *
     * @param definedName 自定义单元格名称（例如：定义A3单元格为Title）
     * @return String
     * @throws IOException
     */
    public String getDefinedNameSingleValue(String definedName) throws IOException {
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
            return cell.getStringCellValue();
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }

            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 获取某个单元格（例如：A1）的值
     *
     * @param workSheetName Sheet名称
     * @param cellName      某个单元格（例如：A1）
     * @return String
     * @throws IOException
     */
    public String getCellValueByName(String workSheetName, String cellName) throws IOException {
        try {
            Sheet sheet =  StringExtensions.isNullOrEmpty(workSheetName)
                    ? Workbook.getSheetAt(0)
                    : Workbook.getSheet(workSheetName);
            if (sheet == null)
                return null;

            CellReference cr = new CellReference(cellName);
            Row row = sheet.getRow(cr.getRow());
            if (row == null)
                throw new Exception("Cannot process excel: The excel sheet's cell is empty or wrong.");
            Cell cell = row.getCell(cr.getCol());
            if (StringExtensions.isNullOrEmpty(cellName) || cell == null)
                throw new Exception("Cannot process excel: The excel sheet's cell is empty or wrong.");

            return getCellValue(cell);
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }

            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 获取某个表单（例如：Sheet1）的所有值：</br>
     * A1-Value11、B1-Value12、C1-Value13 </br>
     * A2-Value21、B2-Value22、C2-Value23
     *
     * @param workSheetName Sheet名称
     * @param maxRows       获取的最大行数限制
     * @param skipRowCount  获取跳过几行后的数据
     * @return Dictionary<String, String>
     * @throws IOException
     */
    public Dictionary<String, String> getWorksheetDictData(String workSheetName, int maxRows, int skipRowCount)
            throws IOException {
        try {
            Sheet sheet =  StringExtensions.isNullOrEmpty(workSheetName)
                    ? Workbook.getSheetAt(0)
                    : Workbook.getSheet(workSheetName);
            if (sheet == null)
                return null;

            Hashtable<String, String> result = new Hashtable<String, String>();
            Row headerRow = sheet.getRow(0);
            int cellCount = headerRow == null ? 0 : headerRow.getLastCellNum();
            int cellMax = sheet.getLastRowNum();
            int maxRow = cellMax <= maxRows ? cellMax : maxRows;
            for (int i = skipRowCount; i <= maxRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                cellCount = cellCount != 0 ? cellCount : row.getLastCellNum();
                for (int j = 0; j < cellCount; j++) {
                    Cell cell = row.getCell(j);
                    if (cell == null)
                        continue;

                    String cellValue = getCellValue(cell);
                    String alphabet = IndexToColumn(j + 1);
                    if (!result.containsKey(alphabet)) {
                        result.put(alphabet + (i + 1), cellValue);
                    }
                }
            }

            return result;
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 根据Excel文件，获取Sheet中的数据：List<List<RowValue>>
     *
     * @param workSheetName          Sheet名
     * @param maxRows                获取的最大行数限制
     * @param skipRowCount           获取跳过几行后的数据
     * @param useRowHeaderColumnSize 是否使用第一行列数为表格最大列数
     * @return List<List < RowValue>>
     * @throws IOException
     */
    public List<List<RowValue>> getWorksheetRowListData(String workSheetName, int maxRows, int skipRowCount, boolean useRowHeaderColumnSize)
            throws IOException {
        try {
            Sheet sheet =  StringExtensions.isNullOrEmpty(workSheetName)
                    ? Workbook.getSheetAt(0)
                    : Workbook.getSheet(workSheetName);
            if (sheet == null)
                return null;

            ArrayList<List<RowValue>> result = new ArrayList<List<RowValue>>();
            Row headerRow = sheet.getRow(0);
            int cellCount = headerRow == null ? 0 : headerRow.getLastCellNum();
            int cellMax = sheet.getLastRowNum();
            int maxRow = cellMax <= maxRows ? cellMax : maxRows;
            for (int i = skipRowCount; i <= maxRow; i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                cellCount = useRowHeaderColumnSize && cellCount != 0 ? cellCount : row.getLastCellNum();
                List<RowValue> columns = new ArrayList<RowValue>();
                for (int j = 0; j < cellCount; j++) {
                    String alphabet = IndexToColumn(j + 1);
                    Cell headerCell = headerRow.getCell(j);
                    String headerValue = getCellValue(headerCell);
                    String columnName = !StringExtensions.isNullOrEmpty(headerValue) ? headerValue
                            : Integer.toString(i);

                    String cellValue = null;
                    String cellColor = null;
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cellValue = getCellValue(cell);
                        cellColor = getCellColor(cell);
                    }

                    RowValue rowValue = new RowValue();
                    {
                        rowValue.setRowId(i);
                        rowValue.setColumnId(j);
                        rowValue.setCellName(alphabet);
                        rowValue.setColumnName(columnName);
                        rowValue.setCellValue(cellValue);
                        rowValue.setCellColor(cellColor);
                    }
                    columns.add(rowValue);
                }

                result.add(columns);
            }

            return result;
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * 获取sheet所有的数据
     *
     * @param sheetIndex 第几个sheet
     * @return
     * @throws IOException
     */
    public List<List<RowValue>> getWorksheetRowListData(int sheetIndex) throws IOException {
        try {
            Sheet sheet = Workbook.getSheetAt(sheetIndex);
            if (sheet == null)
                return null;
            List<List<RowValue>> result = new ArrayList<List<RowValue>>();
            Row headerRow = sheet.getRow(0);
            int cellCount = headerRow.getLastCellNum();
            int maxRow = sheet.getLastRowNum();
            for (int i = 0; i <= maxRow; i++) {
                Row row = sheet.getRow(i);
                boolean emptyRow = true;
                if (row == null)
                    continue;

                cellCount = cellCount != 0 ? cellCount : row.getLastCellNum();
                List<RowValue> columns = new ArrayList<RowValue>();
                for (int j = 0; j < cellCount; j++) {
                    String alphabet = IndexToColumn(j + 1);
                    Cell headerCell = headerRow.getCell(j);
                    String headerValue = getCellValue(headerCell);
                    String columnName = !StringExtensions.isNullOrEmpty(headerValue) ? headerValue
                            : Integer.toString(i);

                    String cellValue = null;
                    String cellColor = null;
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        cellValue = getCellValue(cell);
                        cellColor = getCellColor(cell);
                    }
                    if (cellValue != null && !StringExtensions.isNullOrEmpty(cellValue)) {
                        emptyRow = false;
                    }
                    RowValue rowValue = new RowValue();
                    {
                        rowValue.setRowId(i);
                        rowValue.setColumnId(j);
                        rowValue.setCellName(alphabet);
                        rowValue.setColumnName(columnName);
                        rowValue.setCellValue(cellValue);
                        rowValue.setCellColor(cellColor);
                    }
                    columns.add(rowValue);
                }
                if (!emptyRow) {
                    result.add(columns);
                }
            }
            return result;
        } catch (Exception ex) {
            if (Workbook != null) {
                Workbook.close();
                Workbook = null;
            }
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    final int ColumnBase = 26;
    final int DigitMax = 7; // ceil(log26(Int32.Max))
    final String Digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String IndexToColumn(int index) {
        if (index <= 0)
            throw new IndexOutOfBoundsException("index must be a positive number");

        if (index <= ColumnBase)
            return Character.toString(Digits.charAt(index - 1));

        StringBuilder sb = new StringBuilder();
        sb.append(DigitMax);
        int current = index;
        int offset = DigitMax;
        while (current > 0) {
            sb.insert(--offset, Digits.charAt(--current % ColumnBase));
            current /= ColumnBase;
        }
        return sb.toString().substring(offset, DigitMax - offset);
    }

    private String getCellValue(Cell cell) {
        if (cell == null)
            return null;
        String result;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 小写的mm表示的是分钟
        switch (cell.getCellType()) {
            case BOOLEAN:
                result = Boolean.toString(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                result = getDateStringFromNumbric(cell);
                break;
            case STRING:
                String strValue = cell.getStringCellValue();
                result = !StringExtensions.isNullOrEmpty(strValue) ? strValue : null;
                break;
            case ERROR:
                result = ErrorEval.getText(cell.getErrorCellValue());
                break;
            case FORMULA:
                switch (cell.getCellType()) {
                    case BOOLEAN:
                        result = Boolean.toString(cell.getBooleanCellValue());
                        break;
                    case NUMERIC:
                        result = getDateStringFromNumbric(cell);
                        break;
                    case STRING:
                        strValue = cell.getStringCellValue();
                        result = !StringExtensions.isNullOrEmpty(strValue) ? strValue : null;
                        break;
                    case ERROR:
                        result = ErrorEval.getText(cell.getErrorCellValue());
                        break;
                    case BLANK:
                    default:
                        result = "";
                        break;
                }
                break;
            case BLANK:
            default:
                result = "";
                break;
        }

        return result;
    }

    private String getDateStringFromNumbric(Cell cell){
        CellStyle format =  cell.getCellStyle();
        String result  = getDateValue(format.getDataFormat(), format.getDataFormatString(), cell.getNumericCellValue());
        if (result == null){
            result = String.valueOf(cell.getNumericCellValue());
        }
        return result;
    }

    private String getCellColor(Cell cell) {
        if (cell == null)
            return null;
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle instanceof XSSFCellStyle) {
            //单元格样式
            XSSFCellStyle xs = (XSSFCellStyle) cellStyle;
            //单元格背景颜色
            XSSFColor color = xs.getFillForegroundXSSFColor();
            if (color != null) {// 一定是argb
                return color.getARGBHex();
            }
        } else if (cellStyle instanceof HSSFCellStyle) {
            //单元格样式
            HSSFCellStyle xs = (HSSFCellStyle) cellStyle;
            //单元格背景颜色
            HSSFColor color = xs.getFillForegroundColorColor();
            if (color != null) {
                return color.getHexString();
            }
        }

        return null;
    }

    /**
     * 得到date单元格格式的值
     * @param dataFormat 日期格式：https://blog.csdn.net/phoenixx123/article/details/12720431
     * @param dataFormatString
     * @param value
     * @return
     */
    protected String getDateValue(Short dataFormat, String dataFormatString, double value){
        if (!DateUtil.isValidExcelDate(value)){
            return null;
        }

        Date date = DateUtil.getJavaDate(value);
        /**
         * 年月日时分秒
         */
        if (EXCEL_FORMAT_INDEX_DATE_NYRSFM_STRING.contains(dataFormatString)) {
            return COMMON_DATE_FORMAT.format(date);
        }
        /**
         * 年月日
         */
        if (EXCEL_FORMAT_INDEX_07_DATE.contains(dataFormat)
                || EXCEL_FORMAT_INDEX_03_DATE.contains(dataFormat)
                || EXCEL_FORMAT_INDEX_DATE_NYR_STRING.contains(dataFormatString)) {
            return COMMON_DATE_FORMAT_NYR.format(date);
        }
        /**
         * 年月
         */
        if (EXCEL_FORMAT_INDEX_DATE_NY_STRING.contains(dataFormatString)
                || EXCEL_FORMAT_INDEX_DATA_EXACT_NY.equals(dataFormat)) {
            return COMMON_DATE_FORMAT_NY.format(date);
        }
        /**
         * 月日
         */
        if (EXCEL_FORMAT_INDEX_DATE_YR_STRING.contains(dataFormatString)
                || EXCEL_FORMAT_INDEX_DATA_EXACT_YR.equals(dataFormat)) {
            return COMMON_DATE_FORMAT_YR.format(date);

        }
        /**
         * 月
         */
        if (EXCEL_FORMAT_INDEX_DATE_Y_STRING.contains(dataFormatString)) {
            return COMMON_DATE_FORMAT_Y.format(date);
        }
        /**
         * 星期X
         */
        if (EXCEL_FORMAT_INDEX_DATE_XQ_STRING.contains(dataFormatString)) {
            return COMMON_DATE_FORMAT_XQ + dateToWeek(date);
        }
        /**
         * 周X
         */
        if (EXCEL_FORMAT_INDEX_DATE_Z_STRING.contains(dataFormatString)) {
            return COMMON_DATE_FORMAT_Z + dateToWeek(date);
        }
        /**
         * 时间格式
         */
        if (EXCEL_FORMAT_INDEX_07_TIME.contains(dataFormat)
                || EXCEL_FORMAT_INDEX_03_TIME.contains(dataFormat)
                || EXCEL_FORMAT_INDEX_TIME_STRING.contains(dataFormatString)
                || EXCEL_FORMAT_INDEX_TIME_EXACT.contains(dataFormat)) {
            return COMMON_TIME_FORMAT.format(DateUtil.getJavaDate(value));
        }
        /**
         * 单元格为其他未覆盖到的类型
         */
        if (DateUtil.isADateFormat(dataFormat, dataFormatString)) {
            return COMMON_TIME_FORMAT.format(value);
        }

        return null;
    }

}
