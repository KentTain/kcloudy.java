package kc.thirdparty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@lombok.extern.slf4j.Slf4j
public class PDFUtil {

	private final static String DEFAULT_LOGO_IMAGE_PATH ="/logo.png";
	public static ByteArrayOutputStream getWatermark(InputStream originalPdf, String watermarkContent)
	{
		try {
			// read existing pdf
			PdfReader reader = new PdfReader(originalPdf);
			
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PdfStamper stamper = new PdfStamper(reader, outStream);

			// text watermark
			Font FONT = new Font(Font.FontFamily.HELVETICA, 34, Font.BOLD, new GrayColor(0.5f));
			Phrase p = new Phrase(watermarkContent, FONT);

			// image watermark
			Image img = Image.getInstance(getResource(DEFAULT_LOGO_IMAGE_PATH));
			float w = img.getScaledWidth();
			float h = img.getScaledHeight();

			// properties
			PdfContentByte over;
			Rectangle pagesize;
			float x, y;

			// loop over every page
			int n = reader.getNumberOfPages();
			for (int i = 1; i <= n; i++) {

				// get page size and position
				pagesize = reader.getPageSizeWithRotation(i);
				x = (pagesize.getLeft() + pagesize.getRight()) / 2;
				y = (pagesize.getTop() + pagesize.getBottom()) / 2;
				over = stamper.getOverContent(i);
				over.saveState();

				// set transparency
				PdfGState state = new PdfGState();
				state.setFillOpacity(0.2f);
				over.setGState(state);

				// add watermark text and image
				if (i % 2 == 1) {
					ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, x, y, 0);
				} else {
					over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
				}

				over.restoreState();
			}
			stamper.close();
			reader.close();

			return outStream;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	public static boolean addWatermark(String originalPdfPath, String targetPdfPath, String watermarkContent) {
		try {
			// read existing pdf
			InputStream inStream = new FileInputStream(originalPdfPath);
			try(ByteArrayOutputStream byteArrayOutputStream = getWatermark(inStream, watermarkContent))
			{
			try(OutputStream outputStream = new FileOutputStream(targetPdfPath)) {
				  byteArrayOutputStream.writeTo(outputStream);
				}
			}
			inStream.close();
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public static boolean htmlToPdf(String html, String targetPdfPath) {
		try {
			// step 1
			Document document = new Document();
			// step 2
			OutputStream file = new FileOutputStream(new File(targetPdfPath));
			PdfWriter writer = PdfWriter.getInstance(document, file);
			// step 3
			document.open();
			// step 4
			// HTMLWorker htmlWorker = new HTMLWorker(document);
			// htmlWorker.parse(new StringReader(html));
			InputStream is = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, is);
			// step 5
			document.close();
			file.close();

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);

			return false;
		}
	}

	private static URL getResource(String name) {
		return PDFUtil.class.getResource(name);
	}
}
