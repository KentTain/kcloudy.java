package kc.service.webapiservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.framework.extension.StringExtensions;
import org.springframework.beans.factory.annotation.Autowired;

import kc.framework.GlobalConfig;
import kc.framework.GlobalConfigData;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import kc.service.webapiservice.thridparty.impl.GlobalConfigApiService;

@Disabled
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(classes = { GlobalConfigApiService.class })
class GlobalConfigApiServiceTest extends TestBase{
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.SystemType = SystemType.Dev;
		GlobalConfig.AccWebDomain = "http://cTest.localhost:2002/";
    	GlobalConfig.SSOWebDomain = "http://localhost:1001/";
    	
    	intilize();
	}

	@AfterAll
	static void setDownAfterClass() throws Exception {
		GlobalConfig.EncryptKey = "";
		GlobalConfig.AccWebDomain = "";
    	GlobalConfig.SSOWebDomain = "";
    	
    	tearDown();
	}
	
	@Autowired
	private IGlobalConfigApiService _globalConfigApiService;
	
	@Test
	void test_GetGlobalConfigData() {
		GlobalConfigData result = _globalConfigApiService.GetGlobalConfigData();
    	System.out.println(result);
    	assertNotNull(result);
        assertEquals(GlobalConfig.SSOWebDomain, result.getSsoWebDomain());
	}

	@Test
	void test_GetUEditorConfig() {
		String result = _globalConfigApiService.GetUEditorConfig();
		System.out.println(result);
		assertFalse(StringExtensions.isNullOrEmpty(result));
	}
}
