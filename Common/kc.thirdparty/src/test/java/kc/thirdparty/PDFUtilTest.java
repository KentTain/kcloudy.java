package kc.thirdparty;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@DisplayName("Pdf测试")
@lombok.extern.slf4j.Slf4j
class PDFUtilTest {
	private final String pdfPath = "src/test/resources/Pdf/";
	private final String watermarkContent = "http://www.baidu.com";
	
	@BeforeAll
	static void setUpBeforeClass() {
	}

	@BeforeEach
	void setUp() {
	}

	@Test
	void testAddWatermark() {
		boolean result = PDFUtil.addWatermark(pdfPath + "1.pdf", pdfPath + "1-wathermark.pdf", watermarkContent);
		assertTrue(result);
	}

	@Test
	void testHtmlToPdf() {
		String html="<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n" + 
				"<html>\r\n" +
				"    <head>\r\n" + 
				"        <title>HTML to PDF</title>\r\n" + 
				"        <link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n" + 
				"    </head>\r\n" + 
				"    <body>\r\n" + 
				"        <h1>HTML to PDF</h1>\r\n" + 
				"        <p>\r\n" + 
				"            <span class=\"itext\">itext</span> 5.4.2 <span class=\"description\"> converting HTML to PDF</span>\r\n" + 
				"        </p>\r\n" + 
				"        <table>\r\n" + 
				"          <tr>\r\n" + 
				"                <th class=\"label\">Title</th>\r\n" + 
				"                <td>iText - Java HTML to PDF</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <th>URL</th>\r\n" + 
				"                <td>http://www.kcloudy.com/</td>\r\n" + 
				"            </tr>\r\n" + 
				"        </table>\r\n" + 
				"    </body>\r\n" + 
				"</html>";
		
		boolean result = PDFUtil.htmlToPdf(html, pdfPath + "htmlToPdf.pdf");
		assertTrue(result);
	}

}
