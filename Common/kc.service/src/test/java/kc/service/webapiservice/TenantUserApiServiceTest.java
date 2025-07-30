package kc.service.webapiservice;

import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.Tenant;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.thridparty.ITenantUserApiService;
import kc.service.webapiservice.thridparty.impl.TenantUserApiService;

@Disabled
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(classes = { TenantUserApiService.class })
class TenantUserApiServiceTest extends TestBase{
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.SystemType = SystemType.Dev;
		GlobalConfig.SSOWebDomain  = "http://localhost:1001/";
    	GlobalConfig.AdminWebDomain = "http://localhost:1003/";
    	GlobalConfig.AccWebDomain = "http://cTest.localhost:2001/";
    	
    	intilize();
	}

	@AfterAll
	static void setDownAfterClass() throws Exception {
		GlobalConfig.EncryptKey = "";
		GlobalConfig.SSOWebDomain = "";
    	GlobalConfig.AdminWebDomain = "";
    	
    	tearDown();
	}
	
	@Autowired
	private ITenantUserApiService tenantUserApiService;

	@Test
	void test_ExistTenantName() {
    	ServiceResult<Boolean> result = tenantUserApiService.ExistTenantName(TestTenant.getTenantName());
        assertTrue(result != null && result.isSuccess());
	}
	
	@Test
	void test_GetTenantByName() {
		Tenant result = tenantUserApiService.GetTenantByName(TestTenant.getTenantName());
        assertTrue(result != null && result.getTenantDisplayName().equalsIgnoreCase(TestTenant.getTenantDisplayName()));
	}
	
	@Test
	void test_GetTenantEndWithDomainName() {
		String domain = StringExtensions.replaceLast(GlobalConfig.AccWebDomain.replace("https://", "").replace("http://", ""), "/", "");
		Tenant result = tenantUserApiService.GetTenantEndWithDomainName(domain);
    	//System.out.println("----test_GetString: " + result);
		assertTrue(result != null && result.getTenantName().equalsIgnoreCase(TestTenant.getTenantName()));
	}

}
