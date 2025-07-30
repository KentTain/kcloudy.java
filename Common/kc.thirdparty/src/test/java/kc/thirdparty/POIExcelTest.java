package kc.thirdparty;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.framework.extension.DateExtensions;

@Disabled
@DisplayName("Excel导入导出")
@lombok.extern.slf4j.Slf4j
class POIExcelTest {

    @Test
    void testExcelReader() throws IOException {
        String excelPath = "Excel/ExcelReaderTest.xlsx";
        //String excelPath = "Excel/poolImportTemplate.xlsx";
        String sheet1 = "Sheet1";

        ClassLoader classLoader = this.getClass().getClassLoader();

        File xlsxfile = new File(classLoader.getResource(excelPath).getFile());
        byte[] xlsxBytes = Files.readAllBytes(xlsxfile.toPath());
        POIExcelReader xlsxEr = new POIExcelReader(xlsxBytes);
        Dictionary<String, String> definedNames = xlsxEr.getDefinedNames();
        String definedValue = xlsxEr.getDefinedNameSingleValue("Title");
        assertEquals("Test Title", definedValue);
        assertTrue(definedNames.keys().hasMoreElements());

        String cellValue = xlsxEr.getCellValueByName(sheet1, "B3");
        String cellValue1 = xlsxEr.getCellValueByName(sheet1, "E4");
        assertEquals("B3", cellValue);
        assertEquals("E4", cellValue1);

        //根据Excel日期格式转换对应的String字符串
        String dateValue = xlsxEr.getCellValueByName(sheet1, "B5");//2013/07/19（实际：2013/07/19）
        String dateValue1 = xlsxEr.getCellValueByName(sheet1, "C5");//2013年07月19日（实际：2013/07/19）
        String dateValue2 = xlsxEr.getCellValueByName(sheet1, "D5");//2013-07-19（实际：2013/07/19）
        String dateValue3 = xlsxEr.getCellValueByName(sheet1, "E5");//11:00:00（实际：2013/07/19 11:00:00）
        String dateValue4 = xlsxEr.getCellValueByName(sheet1, "F5");//2013年7月（实际：2013/07/19）
        assertEquals("2013-07-19", dateValue);
        assertEquals("2013-07-19", dateValue1);
        assertEquals("2013-07-19", dateValue2);
        assertEquals("11:00:00", dateValue3);
        assertEquals("2013-07-19", dateValue4);

        Dictionary<String, String> cellResult = xlsxEr.getWorksheetDictData(sheet1, 10, 0);
        assertTrue(cellResult.keys().hasMoreElements());

        excelPath = "Excel/ExcelReaderTest.xls";
        File xlsfile = new File(classLoader.getResource(excelPath).getFile());
        byte[] xlsBytes = Files.readAllBytes(xlsfile.toPath());
        POIExcelReader xlsEr = new POIExcelReader(xlsBytes);
        Dictionary<String, String> definedNamesXls = xlsEr.getDefinedNames();
        String definedValueXls1 = xlsEr.getDefinedNameSingleValue("Title");
        assertEquals("Test Title", definedValueXls1);
        assertTrue(definedNamesXls.keys().hasMoreElements());

        String cellValueXls = xlsEr.getCellValueByName(sheet1, "B3");
        String cellValueXls1 = xlsEr.getCellValueByName(sheet1, "E4");
        assertEquals("B3", cellValueXls);
        assertEquals("E4", cellValueXls1);

        Dictionary<String, String> cellResultXls = xlsEr.getWorksheetDictData(sheet1, 10, 0);
        assertEquals("B3", cellResultXls.get("B3"));
        assertEquals("E4", cellResultXls.get("E4"));
    }


    @Test
    void testExcelWriter() throws ParseException, IOException {
        //Data
        String sheet1 = "Sheet1";
        String sheet2 = "Sheet2";
        String sheet3 = "Sheet3";
        String excelPath = "src/test/resources/Excel/ExcelWriterTest.xlsx";

        String saveFilePath = excelPath.replace("ExcelWriterTest", "ExcelWriterTest-0-" + DateExtensions.getNowDateString("yyyyMMdd-hhmm"));
        String saveFilePath1 = excelPath.replace("ExcelWriterTest", "ExcelWriterTest-1-" + DateExtensions.getNowDateString("yyyyMMdd-hhmm"));

        List<List<String>> dataList = new ArrayList<List<String>>();
        dataList.add(Arrays.asList("Column1", "Column2", "Column3", "Column4"));
        dataList.add(Arrays.asList("R11", "R12", "R13", "R14"));
        dataList.add(Arrays.asList("R21", "R22", "R23", "R24"));
        dataList.add(Arrays.asList("R31", "R32", "R33", "R34"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date1 = formatter.parse("2019/03/02 12:20:00");
        Date date2 = formatter.parse("2019/03/02 12:20:00");
        Date date3 = formatter.parse("2019/03/02 12:20:00");
        List<POIExcelTestObj> objList = new ArrayList<POIExcelTestObj>();
        objList.add(new POIExcelTestObj(1, "Name1", false, date1, 12.1d));
        objList.add(new POIExcelTestObj(2, "Name2", true, date2, 22.1d));
        objList.add(new POIExcelTestObj(3, "Name3", false, date3, 32.1d));

        //先读Excel文件，再向该Excel写入数据
        File writerFile = new File(excelPath);
        byte[] writerBytes = Files.readAllBytes(writerFile.toPath());
        POIExcelWriter ew = new POIExcelWriter(writerBytes);
        {
            ew.setDefinedNameSingleValue("Title", "Test Title");

            ew.setCellValueByName(sheet1, "B3", "B3");
            ew.setCellValueByName(sheet1, "E4", "E4");

            ew.setWorksheetDataRaw(sheet2, dataList);
            ew.setWorksheetObject(sheet3, objList);

            ew.SaveAsExcel(saveFilePath);
        }

        File readerfile = new File(saveFilePath);
        byte[] readerBytes = Files.readAllBytes(readerfile.toPath());
        POIExcelReader er = new POIExcelReader(readerBytes);
        {
            Dictionary<String, String> definedNames = er.getDefinedNames();
            String definedValue = er.getDefinedNameSingleValue("Title");
            assertEquals("Test Title", definedValue);
            assertTrue(definedNames.keys().hasMoreElements());

            String cellValue = er.getCellValueByName(sheet1, "B3");
            String cellValue1 = er.getCellValueByName(sheet1, "E4");
            assertEquals("B3", cellValue);
            assertEquals("E4", cellValue1);

            Dictionary<String, String> cellResult = er.getWorksheetDictData(sheet1, 10, 3);
            assertNull(cellResult.get("B3"));
            assertEquals("E4", cellResult.get("E4"));
            Dictionary<String, String> cellResult2 = er.getWorksheetDictData(sheet2, 10, 1);
            assertEquals("R22", cellResult2.get("B3"));
            assertEquals("R34", cellResult2.get("D4"));
        }

        //生成一个空的Excel文件，再写入数据
        POIExcelWriter ewnull = new POIExcelWriter(true);
        {
            //所生成的空Excel文件，不包含自定义单元格，所有不能用该方法
            //er.setDefinedNameSingleValue("Title", "Test Title");

            ewnull.setCellValueByName(sheet1, "B3", "B3");
            ewnull.setCellValueByName(sheet1, "E4", "E4");

            ewnull.setWorksheetDataRaw(sheet2, dataList);
            ewnull.setWorksheetObject(sheet3, objList);

            ewnull.SaveAsExcel(saveFilePath1);
        }

        File nullfile = new File(saveFilePath1);
        byte[] nullbytes = Files.readAllBytes(nullfile.toPath());
        POIExcelReader ernull = new POIExcelReader(nullbytes);
        {
            //Dictionary<String, String>  definedNames = ernull.getDefinedNames();
            //所生成的空Excel文件，不包含自定义单元格，所有不能用该方法
            //var definedValue = er.getDefinedNameSingleValue("Title");
            //Equal("Test Title", definedValue);
            //assertTrue(definedNames.keys().hasMoreElements());

            String cellValue = ernull.getCellValueByName(sheet1, "B3");
            String cellValue1 = ernull.getCellValueByName(sheet1, "E4");
            assertEquals("B3", cellValue);
            assertEquals("E4", cellValue1);

            Dictionary<String, String> cellResult = ernull.getWorksheetDictData(sheet1, 10, 3);
            assertNull(cellResult.get("B3"));
            assertEquals("E4", cellResult.get("E4"));
            Dictionary<String, String> cellResult2 = ernull.getWorksheetDictData(sheet2, 10, 1);
            assertEquals("R22", cellResult2.get("B3"));
            assertEquals("R34", cellResult2.get("D4"));
        }
    }

//    @Test
//    public void testExcelImportFromUrl() throws IOException {
//        String singleSheetName = "单选题";
//        String multiSelectSheetName = "多选题";
//        String trueOrFalseSheetName = "判断题";
//        String completeSheetName = "填空题";
//        String essaySheetName = "简答题";
//
//        String strUrl = "https://minio.ytshare.com:443/pdjumoon6-ide/anoy_C8FD7CA3ECF0000145B1CBDCB395B5B0poolImportTemplate.xlsx?fileName=poolImportTemplate.xlsx";
//
//        URL urlObj = new URL(strUrl);
//        HttpURLConnection urlCon = (HttpURLConnection) urlObj.openConnection();
//        urlCon.setConnectTimeout(10000);
//        urlCon.setReadTimeout(30000);
//
//        InputStream is;
//        int code = urlCon.getResponseCode();
//        System.out.println("-------get Import stream code: " + code + "---");
//
//        if (code == 200) {
//            is = urlCon.getInputStream(); // 得到网络返回的输入流
//        } else {
//            is = urlCon.getErrorStream(); // 得到网络返回的输入流
//        }
//
//        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//        byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
//        int rc = 0;
//        while ((rc = is.read(buff, 0, 100)) > 0) {
//            swapStream.write(buff, 0, rc);
//        }
//        byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
//        is.close();
//        urlCon.disconnect();
//
//        POIExcelReader xlsxEr = new POIExcelReader(in_b);
//        System.out.println("-------get Import excel reader: " + in_b.length);
//
//        List<List<RowValue>> rows = xlsxEr.getWorksheetRowListData(singleSheetName, 10, 1, false);
//
//        assertTrue(rows.size() > 0);
//        for (List<RowValue> columns : rows) {
//            if (columns.size() <= 0)
//                continue;
//            for (RowValue column : columns) {
//                if (column == null || column.getCellValue().isEmpty())
//                    continue;
//
//                System.out.println(String.format("-----Row name: %s, value: %s, color: %s",
//                        column.getCellName() + column.getRowId(), column.getCellValue(), column.getCellColor()));
//            }
//        }
//    }

    @Test
    public void testExcelImportFromFile() throws IOException {
        String singleSheetName = "单选题";
        String multiSelectSheetName = "多选题";
        String trueOrFalseSheetName = "判断题";
        String completeSheetName = "填空题";
        String essaySheetName = "简答题";

        String excelPath = "Excel/poolImportTemplate.xlsx";
        ClassLoader classLoader = this.getClass().getClassLoader();

        File xlsxFile = new File(classLoader.getResource(excelPath).getFile());
        byte[] xlsxBytes = Files.readAllBytes(xlsxFile.toPath());
        POIExcelReader xlsxEr = new POIExcelReader(xlsxBytes);
        List<List<RowValue>> rows = xlsxEr.getWorksheetRowListData(singleSheetName, 10, 1, false);
        assertTrue(rows.size() > 0);
        for (List<RowValue> columns : rows) {
            if (columns.size() <= 0)
                continue;
            for (RowValue column : columns) {
                if (null == column || null == column.getCellValue()  ||  column.getCellValue().isEmpty())
                    continue;

                System.out.println(String.format("-----Row name: %s, value: %s, color: %s",
                        column.getCellName() + column.getRowId(), column.getCellValue(), column.getCellColor()));
            }
        }

    }
}
