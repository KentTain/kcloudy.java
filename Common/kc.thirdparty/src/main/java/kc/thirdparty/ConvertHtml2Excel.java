package kc.thirdparty;

import java.util.ArrayList;
import java.util.List;


import kc.framework.extension.StringExtensions;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * 将html table 转成 excel
 *
 * 记录下来所占的行和列，然后填充合并
 */
public class ConvertHtml2Excel {
    /**
     * html表格转excel
     *
     * @param tableHtml 如
     *            <table>
     *            ..
     *            </table>
     * @return
     */
    public static HSSFWorkbook table2Excel(String tableHtml) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();

        List<CrossRangeCellMeta> crossRowEleMetaLs = new ArrayList<>();
        int rowIndex = 0;
        try {
            Document data = DocumentHelper.parseText(tableHtml);
            // 生成表头
            Element thead = data.getRootElement().element("thead");
            HSSFCellStyle titleStyle = getTitleStyle(wb);
            if (thead != null) {
                List<Element> trLs = thead.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("th");
                    makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    row.setHeightInPoints(17);
                    rowIndex++;
                }
            }
            // 生成表体
            Element tbody = data.getRootElement().element("tbody");
            if (tbody != null) {
                HSSFCellStyle contentStyle = getContentStyle(wb);
                List<Element> trLs = tbody.elements("tr");
                for (Element trEle : trLs) {
                    HSSFRow row = sheet.createRow(rowIndex);
                    List<Element> thLs = trEle.elements("th");
                    int cellIndex = makeRowCell(thLs, rowIndex, row, 0, titleStyle, crossRowEleMetaLs);
                    List<Element> tdLs = trEle.elements("td");
                    makeRowCell(tdLs, rowIndex, row, cellIndex, contentStyle, crossRowEleMetaLs);
                    row.setHeightInPoints(18);
                    rowIndex++;
                }

                // 必须在单元格设值以后进行
                // 设置为根据内容自动调整列宽
                for (int k = 0; k < trLs .size(); k++) {
                    sheet.autoSizeColumn(k);
                }
                // 处理中文不能自动调整列宽的问题
                setSizeColumn(sheet, trLs.size());
            }
            // 合并表头
            for (CrossRangeCellMeta crcm : crossRowEleMetaLs) {
                CellRangeAddress cra = new CellRangeAddress(crcm.getFirstRow(), crcm.getLastRow(), crcm.getFirstCol(), crcm.getLastCol());
                sheet.addMergedRegion(cra);
                //注意：边框样式需要重新设置一下
                RegionUtil.setBorderTop(BorderStyle.MEDIUM, cra, sheet);
                RegionUtil.setBorderBottom(BorderStyle.MEDIUM, cra, sheet);
                RegionUtil.setBorderLeft(BorderStyle.MEDIUM, cra, sheet);
                RegionUtil.setBorderRight(BorderStyle.MEDIUM, cra, sheet);
            }



        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //自动调整列宽
        for (int i = 0; i < 15; i++) {
            sheet.autoSizeColumn((short)i);
        }
        return wb;
    }

    /**
     * 生产行内容
     *
     * @return 最后一列的cell index
     */
    /**
     * @param tdLs th或者td集合
     * @param rowIndex 行号
     * @param row POI行对象
     * @param startCellIndex 开始行索引
     * @param cellStyle 样式
     * @param crossRowEleMetaLs 跨行元数据集合
     * @return
     */
    private static int makeRowCell(List<Element> tdLs, int rowIndex, HSSFRow row, int startCellIndex, HSSFCellStyle cellStyle,
                                   List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int i = startCellIndex;
        for (int eleIndex = 0; eleIndex < tdLs.size(); i++, eleIndex++) {
            int captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            while (captureCellSize > 0) {
                for (int j = 0; j < captureCellSize; j++) {// 当前行跨列处理（补单元格）
                    row.createCell(i);
                    i++;
                }
                captureCellSize = getCaptureCellSize(rowIndex, i, crossRowEleMetaLs);
            }
            Element thEle = tdLs.get(eleIndex);
            String val = getElementValue(thEle);
            HSSFCell c = row.createCell(i);
            if (StringExtensions.isNumber(val)) {
                c.setCellValue(Double.parseDouble(val));
                c.setCellType(CellType.NUMERIC);
            } else {
                c.setCellValue(val);
            }
            c.setCellStyle(cellStyle);
            String rows = thEle.attributeValue("rowspan");
            String cols = thEle.attributeValue("colspan");
            int rowSpan = 1;
            int colSpan = 1;
            if (!StringExtensions.isNullOrEmpty(rows) && StringExtensions.isNumber(rows)){
                rowSpan = Integer.parseInt(rows);
            }
            if (!StringExtensions.isNullOrEmpty(cols) && StringExtensions.isNumber(cols)){
                colSpan = Integer.parseInt(cols);
            }
            if (rowSpan > 1 || colSpan > 1) { // 存在跨行或跨列
                crossRowEleMetaLs.add(new CrossRangeCellMeta(rowIndex, i, rowSpan, colSpan));
            }
            if (colSpan > 1) {// 当前行跨列处理（补单元格）
                for (int j = 1; j < colSpan; j++) {
                    i++;
                    row.createCell(i);
                }
            }
        }
        return i;
    }

    /**
     * 获得table中某行某列的值
     *
     * @param thEle 某个元素
     * @return 当前行在某列需要占据单元格
     */
    private static String getElementValue(Element thEle) {
        String val = thEle.getTextTrim();
        if (StringExtensions.isNullOrEmpty(val)) {
            Element p = thEle.element("p");
            Element a = thEle.element("a");
            Element span = thEle.element("span");
            if (p != null) {
                val = p.getTextTrim();
            } else if (a != null) {
                val = a.getTextTrim();
            } else if (span != null) {
                val = span.getTextTrim();
            }
        }

        return val;
    }

    /**
     * 获得因rowSpan占据的单元格
     *
     * @param rowIndex 行号
     * @param colIndex 列号
     * @param crossRowEleMetaLs 跨行列元数据
     * @return 当前行在某列需要占据单元格
     */
    private static int getCaptureCellSize(int rowIndex, int colIndex, List<CrossRangeCellMeta> crossRowEleMetaLs) {
        int captureCellSize = 0;
        for (CrossRangeCellMeta crossRangeCellMeta : crossRowEleMetaLs) {
            if (crossRangeCellMeta.getFirstRow() < rowIndex && crossRangeCellMeta.getLastRow() >= rowIndex) {
                if (crossRangeCellMeta.getFirstCol() <= colIndex && crossRangeCellMeta.getLastCol() >= colIndex) {
                    captureCellSize = crossRangeCellMeta.getLastCol() - colIndex + 1;
                }
            }
        }
        return captureCellSize;
    }

    /**
     * 获得标题样式
     *
     * @param workbook
     * @return
     */
    private static HSSFCellStyle getTitleStyle(HSSFWorkbook workbook) {
        short titlebackgroundcolor = HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex();
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);

        BorderStyle border = BorderStyle.MEDIUM;
        style.setBorderBottom(border);
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(titlebackgroundcolor);// 背景色

        HSSFFont font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * 获得内容样式
     *
     * @param wb
     * @return
     */
    private static HSSFCellStyle getContentStyle(HSSFWorkbook wb) {
        short fontSize = 12;
        String fontName = "宋体";
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //style.setAlignment(HorizontalAlignment.CENTER);

        BorderStyle border = BorderStyle.MEDIUM;
        style.setBorderBottom(border);
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        HSSFFont font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        return style;
    }

    // 自适应宽度(中文支持)
    private static void setSizeColumn(HSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }

                if (currentRow.getCell(columnNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == CellType.STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
}

