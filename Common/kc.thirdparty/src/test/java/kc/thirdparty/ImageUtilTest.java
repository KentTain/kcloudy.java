package kc.thirdparty;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@DisplayName("图片水印测试")
@lombok.extern.slf4j.Slf4j
class ImageUtilTest {
	private final String imagePath = "src/test/resources/Image/";
	private final String watermarkImagePath = "src/test/resources/Image/2.png";
	private final String watermarkContent = "http://www.baidu.com";
	
	@Test
	void testGetBufferedImageString() {
		BufferedImage result = ImageUtil.getBufferedImage(imagePath + "1.jpg");
		assertNotNull(result);
	}

	@Test
	void testGetThumbnailWithWidthAndHeight() {
		boolean result = ImageUtil.getThumbnail(imagePath + "1.jpg", imagePath + "1-thumbnail-width-height.jpg", 100, 100);
		assertTrue(result);
	}

	@Test
	void testGetThumbnailWithScale() {
		boolean result = ImageUtil.getThumbnail(imagePath + "1.jpg", imagePath + "1-thumbnail-scale.jpg", 0.5);
		assertTrue(result);
	}

	@Test
	void testGetWatermarkWithImage() {
		boolean result = ImageUtil.getWatermark(imagePath + "1.jpg", imagePath + "1-watermark-image.jpg", watermarkImagePath);
		assertTrue(result);
	}
	
	@Test
	void testAddWatermarkWithContent() {
		boolean result = ImageUtil.addWatermark(imagePath + "1.jpg", imagePath + "1-watermark-context.jpg", watermarkContent);
		assertTrue(result);
	}
	
	@Test
	void testInsertImage() throws IOException {
		boolean result = ImageUtil.insertImage(imagePath + "1.jpg", imagePath + "1-insert-2.jpg", watermarkImagePath);
		assertTrue(result);
	}

}
