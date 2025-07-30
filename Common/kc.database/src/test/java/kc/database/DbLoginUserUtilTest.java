package kc.database;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;


import kc.framework.GlobalConfig;
import kc.framework.TestBase;

@Disabled
class DbLoginUserUtilTest extends TestBase {
 
	@BeforeAll
	static void setUpBeforeClass() {
		intilize();
	}

	@AfterAll
	static void setDownAfterClass() {
		GlobalConfig.DatabaseConnectionString = "";
	}

	@Test
	void testCreateTenantDbLoginUser() {

		//DbLoginUserUtil.CreateTenantDbLoginUser(DbaTenant, TenantConstant.DefaultDatabaseConnectionString);
        //DbLoginUserUtil.CreateTenantDbLoginUser(TestTenant, TenantConstant.DefaultDatabaseConnectionString);
        //DbLoginUserUtil.CreateTenantDbLoginUser(BuyTenant, TenantConstant.DefaultDatabaseConnectionString);
        //DbLoginUserUtil.CreateTenantDbLoginUser(SaleTenant, TenantConstant.DefaultDatabaseConnectionString);
	}

}
