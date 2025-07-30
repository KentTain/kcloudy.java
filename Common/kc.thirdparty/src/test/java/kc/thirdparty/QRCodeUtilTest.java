package kc.thirdparty;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

//@Disabled
@DisplayName("二维码测试")
@lombok.extern.slf4j.Slf4j
class QRCodeUtilTest {
	private final String text = "http://www.baidu.com";
	private final String qrCodePath = "src/test/resources/Image/qrcode";
	
	@Test
	void testEncode() throws Exception {
		String logPath = "src/test/resources/Image/1.jpg";

        // 不指定logo
        boolean success0 = QRCodeUtil.encode(text, null, qrCodePath + "-0.png", true);
		assertTrue(success0);
        // 指定logo
        boolean success1 = QRCodeUtil.encode(text, logPath, qrCodePath + "-1.png", true);
		assertTrue(success1);
	}

	@Test
	void testDecodeString() throws Exception {
		QRCodeUtil.encode(text, null, qrCodePath + "-0.png", true);
		
		String result = QRCodeUtil.decode(qrCodePath + "-0.png");
		
		assertEquals(text, result);
	}

}
