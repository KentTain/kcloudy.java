package kc.thirdparty;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;

//@Disabled
@DisplayName("Hmtl转换为Excel")
@lombok.extern.slf4j.Slf4j
class ConvertHtml2ExcelTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {

	}

	@Test
	void testConvertHtml2Excel() {
		String excelPath = "src/test/resources/Excel/";
		String saveExcelPath = excelPath + "ConvertHtml2Excel.xls";
		String content = getExportHtml();

		//输出文件
		HSSFWorkbook wb = ConvertHtml2Excel.table2Excel(content);
		try {
			FileOutputStream fos = new FileOutputStream(new File(saveExcelPath));
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private String getExportHtml(){
		String theadStyle = "border-bottom-color:#000000; border-bottom-style:solid; border-bottom-width:0.75pt; border-left-color:#000000; border-left-style:solid; border-left-width:1.5pt; border-right-color:#000000; border-right-style:solid; border-right-width:0.75pt; border-top-color:#000000; border-top-style:solid; border-top-width:1.5pt; padding-left:4.5pt; padding-right:4.88pt; vertical-align:middle; width:4%";
		String theadPStyle = "margin:5pt 0pt; text-align:center";
		String spanStyle = "font-family:方正仿宋简体; font-size:6.5pt;margin:5pt 0pt; text-align:center";

		StringBuilder sbContent = new StringBuilder("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse; float:left; margin:0pt 9pt; width:100%'>");
		//table head
		sbContent.append(String.format("<thead><tr style='height:24.65pt'>" +
						"<th style='width:4%%; %1$s'><span style='font-weight:bold; %3$s'>一级项</span></th>" +
						"<th style='width:3.7%%; %1$s'><span style='font-weight:bold; %3$s'>二级项</span></th>" +
						"<th style='width:4.88%%; %1$s'><span style='font-weight:bold; %3$s'>三级项</span></th>" +
						"<th style='width:7.8%%; %1$s'><span style='font-weight:bold; %3$s'>评价内容</span></th>" +
						"<th style='width:8.3%%; %1$s'><span style='font-weight:bold; %3$s'>扣分标准</span></th>" +
						"<th style='width:2.2%%; %1$s'><span style='font-weight:bold; %3$s'>扣分</span></th>" +
						"<th style='width:2.2%%; %1$s'><span style='font-weight:bold; %3$s'>得分</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>证明材料名称</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>文件编号</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>是否归档</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>下一步工作</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>提供单位</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>联系人</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>联系方式</span></th>" +
						"<th style='width:5.82%%; %1$s'><span style='font-weight:bold; %3$s'>指标不适用原因说明</span></th>" +
						"<th style='width:5.12%%; %1$s'><span style='font-weight:bold; %3$s'>备注</span></th>" +
						"<th style='width:6.56%%; %1$s'><span style='font-weight:bold; %3$s'>牵头责任单位</span></th>" +
						"<th style='width:4.98%%; %1$s'><span style='font-weight:bold; %3$s'>责任单位</span></th>" +
						"<th style='width:3.7%%; %1$s'><span style='font-weight:bold; %3$s'>完成时限和进度</span></th>" +
						"</tr></thead>",
				theadStyle, theadPStyle, spanStyle));
		//table body
		sbContent.append("<tbody>");

		//4->19："一级项", "二级项", "三级项", "评价内容", "扣分标准", "扣分", "得分",
		//		"证明材料名称", "文件编号", "是否归档", "下一步工作", 提供单位, "联系人", "联系方式",
		//		"指标不适用原因说明", "备注", "牵头责任单位", "责任单位", "完成时限和进度"
		String level1Name = "4：一、城市安全源头治理（18分）";	//4：一级项
		String level2Name = "5：1.城市安全规划（5分）";	//5：二级项
		String level3Name = "6：1.1城市总体规划及防灾减灾等专项规划（2分）";	//6：三级项
		String level3Content = "7：评价内容";			//7：评价内容
		String factorContent = "8：扣分标准";			//8：扣分标准
		String level3SumActualScore = "9：实际总计扣分";	//9：实际总计扣分
		String level3Score = "10：得分";					//10：得分
		String factorFileName = "11：证明材料名称";		//11：证明材料名称
		String factorFileCode = "12：文件编号";			//12：文件编号
		String factorIsFit = "13：是否适用";				//13：是否适用
		String factorNextWork = "14：下一步工作";		//14：下一步工作
		String factorPersonCompany = "15：提供单位";		//15：提供单位
		String factorPersonName = "16：联系人";			//16：联系人
		String factorPersonPhone = "17：联系方式";		//17：联系方式
		String factorReason = "18：指标不适用原因说明";	//18：指标不适用原因说明
		String factorRemark = "19：备注";				//19：备注
		String solutionLeadName = "20：牵头单位";		//20：牵头单位
		String solutionUnitName = "21：责任单位";		//21：责任单位
		String solutionProcess = "22：完成时限和进度";	//22：完成时限和进度

		//一级-->二级-->三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						"<td rowspan='4' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td>" +
						"</tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));

		//扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='4' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						//"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						//"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						//"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						//"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						//"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						//"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						//"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td>" +
						"</tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));

		//三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='4' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td></tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='4' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						//"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						//"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						//"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						//"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						//"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						//"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						//"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td>" +
						"</tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//二级-->三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						"<td rowspan='3' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='1' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='1' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='1' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='1' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='1' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='1' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='1' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td></tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='3' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td></tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='7' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='3' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						//"<td rowspan='2' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						//"<td rowspan='2' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						//"<td rowspan='2' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						//"<td rowspan='2' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						//"<td rowspan='2' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						//"<td rowspan='2' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						//"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td>" +
						"</tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));

		//一级-->二级-->三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						"<td rowspan='3' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						"<td rowspan='1' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='1' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='1' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='1' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='1' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='1' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='1' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='1' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td>" +
						"</tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//二级-->三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='3' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='1' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='1' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='1' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='1' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='1' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='1' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='1' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td></tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));
		//三级-->扣分标准
		sbContent.append(String.format("<tr style='height:15.75pt'>" +
						//"<td rowspan='3' style='width:4%%; %1$s'><span style='%3$s'>%4$s</span></td>" +
						//"<td rowspan='2' style='width:3.7%%; %1$s'><span style='%3$s'>%5$s</span></td>" +
						"<td rowspan='1' style='width:4.88%%; %1$s'><span style='%3$s'>%6$s</span></td>" +
						"<td rowspan='1' style='width:7.8%%; %1$s'><span style='%3$s'>%7$s</span></td>" +
						"<td style='width:2.2%%; %1$s'><span style='%3$s'>%8$s</span></td>" +
						"<td rowspan='1' style='width:2.2%%; %1$s'><span style='%3$s'>%9$s</span></td>" +
						"<td rowspan='1' style='width:5.82%%; %1$s'><span style='%3$s'>%10$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%11$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%12$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%13$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%14$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%15$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%16$s</span></td>" +
						"<td style='width:5.82%%; %1$s'><span style='%3$s'>%17$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%18$s</span></td>" +
						"<td style='width:5.12%%; %1$s'><span style='%3$s'>%19$s</span></td>" +
						"<td rowspan='1' style='width:6.56%%; %1$s'><span style='%3$s'>%20$s</span></td>" +
						"<td rowspan='1' style='width:4.98%%; %1$s'><span style='%3$s'>%21$s</span></td>" +
						"<td rowspan='1' style='width:3.7%%; %1$s'><span style='%3$s'>%22$s</span></td></tr>",
				theadStyle, theadPStyle, spanStyle,
				level1Name, level2Name, level3Name, level3Content, factorContent, level3SumActualScore, level3Score,
				factorFileName, factorFileCode, factorIsFit, factorNextWork, factorPersonCompany, factorPersonName, factorPersonPhone,
				factorReason, factorRemark, solutionLeadName, solutionUnitName, solutionProcess));

		sbContent.append("</tbody>");
		sbContent.append("</table>");

		log.info(sbContent.toString());

		return sbContent.toString();
	}

}
