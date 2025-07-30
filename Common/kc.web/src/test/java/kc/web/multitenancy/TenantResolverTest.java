package kc.web.multitenancy;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.tenant.ITenantResolver;
import kc.framework.tenant.Tenant;

@Disabled
//@org.junit.runner.RunWith(org.springframework.test.context.junit4.SpringRunner.class)
//@org.springframework.boot.test.context.SpringBootTest(classes = { TenantResolver.class, TenantUserApiService.class })
public class TenantResolverTest extends TestBase{

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.AdminWebDomain = "http://localhost:1003/";
    	GlobalConfig.SSOWebDomain = "http://localhost:1001/";
    	
    	intilize();
	}

	@AfterAll
	public static void setDownAfterClass() throws Exception {
		GlobalConfig.EncryptKey = "";
		GlobalConfig.AdminWebDomain = "";
    	GlobalConfig.SSOWebDomain = "";
    	
    	tearDown();
	}
	
	@Autowired
	private ITenantResolver tenantResolver;
	
	@Test
	public void test_Resolve() {
		String tenantNameOrUrl = TestTenant.getTenantName();
		Tenant tenant = tenantResolver.Resolve(tenantNameOrUrl);
		assertEquals(TestTenant.getTenantName(), tenant.getTenantName());
		
		tenantNameOrUrl = "http://ctest.localhost:1101/api";
		tenant = tenantResolver.Resolve(tenantNameOrUrl);
		assertEquals(TestTenant.getTenantName(), tenant.getTenantName());
	}

}
