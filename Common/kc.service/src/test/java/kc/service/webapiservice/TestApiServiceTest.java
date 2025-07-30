package kc.service.webapiservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;
import kc.framework.extension.StringExtensions;

@Disabled
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(classes = { TestApiService.class })
public class TestApiServiceTest extends TestBase{
	
	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.SystemType = SystemType.Dev;
		GlobalConfig.AccWebDomain = "http://cTest.localhost:2002/";
    	GlobalConfig.SSOWebDomain = "http://localhost:1001/";
    	
    	intilize();
	}

	@AfterAll
	public static void setDownAfterClass() throws Exception {
		GlobalConfig.EncryptKey = "";
		GlobalConfig.AccWebDomain = "";
    	GlobalConfig.SSOWebDomain = "";
    	
    	tearDown();
	}
	
	@Autowired
	private ITestApiService _testApiService;
	
	
	@Test
	public void test_Get() {
    	List<String> result = _testApiService.Get();
    	//System.out.println("----test_Get: " + result);
        assertTrue(result != null && result.size() > 0);
	}
	@Test
	public void test_GetString() {
		String result = _testApiService.GetString();
    	//System.out.println("----test_GetString: " + result);
        assertTrue(!StringExtensions.isNullOrEmpty(result));
	}
	@Test
	public void test_GetById() {
    	String result = _testApiService.GetById(12345);
    	//System.out.println("----test_GetString: " + result);
        assertTrue(!StringExtensions.isNullOrEmpty(result));
	}

}
