package kc.framework.tenant;

import java.text.ParseException;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;

class TenantTest extends TestBase {
	@BeforeAll
	static void setUpBeforeClass() {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		
		intilize();
	}

	@AfterAll
	static void setDownAfterClass() {
		GlobalConfig.EncryptKey = "";
		
		tearDown();
	}
	
	@Test
	void test_TenantConnection() {
		
		String expectedDbaConn = "jdbc:sqlserver://localhost;databaseName=MSSqlKCContext;user=cDba;password=P@ssw0rd";
		String dbaConnect = TenantConstant.DbaTenantApiAccessInfo.ConnectionString();
		assertEquals(expectedDbaConn, dbaConnect);
		
		String expectedTestConn = "jdbc:sqlserver://localhost;databaseName=MSSqlKCContext;user=cTest;password=P@ssw0rd";
		String testConnect = TenantConstant.TestTenantApiAccessInfo.ConnectionString();
		assertEquals(expectedTestConn, testConnect);
	}
	
	@Test
	void test_TenantClientSecret() {
		String exceptClientId = "Y0RiYQ==";
		String exceptClientSecret = "MmJmNWIyM2Q5ZjY4OWU5YzFmYWVkZTUwNzY2ZWJkNTg=";
		String exceptSha256Net = "P32flbpKKUOO6YF39o9j+wyuoLIbfNitgYMV3RTlgQc=";
		String clientIdResult = TenantConstant.GetClientIdByTenant(TenantConstant.DbaTenantApiAccessInfo);
		String clientSecretResult = TenantConstant.GetClientSecretByTenant(TenantConstant.DbaTenantApiAccessInfo);
		String clientSecretSha256Result = TenantConstant.Sha256(clientSecretResult);

        //System.out.println("\ncDba: ClientIdResult: " + clientIdResult);
        //System.out.println("\ncDba: CientSecretResult: " + clientSecretResult);
        //System.out.println("\ncDba: ClientSecretSha256Result: " + clientSecretSha256Result);
        assertEquals(exceptClientId, clientIdResult);
        assertEquals(exceptClientSecret, clientSecretResult);
        assertEquals(exceptSha256Net, clientSecretSha256Result);
        
        String exceptClientId1 = "Y1Rlc3Q=";
		String exceptClientSecret1 = "MmMyZjg3YTIzNzZkY2NhNjNlNDg0MGE4NGU4NzQ3YzM=";
		String clientIdResult1 = TenantConstant.GetClientIdByTenant(TenantConstant.TestTenantApiAccessInfo);
        String clientSecretResult1 = TenantConstant.GetClientSecretByTenant(TenantConstant.TestTenantApiAccessInfo);
        //System.out.println("\nClientIdResult1: " + clientIdResult1);
        //System.out.println("\nCientSecretResult1: " + clientSecretResult1);
        assertEquals(exceptClientId1, clientIdResult1);
        assertEquals(exceptClientSecret1, clientSecretResult1);
        
    }

}
