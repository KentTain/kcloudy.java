package kc.database.repository;

import java.util.Date;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kc.database.TestObj;
import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.tenant.TenantConstant;

@Disabled
@org.junit.Ignore
@SpringBootTest
@ExtendWith(SpringExtension.class)
class TestRepositoryTest extends TestBase{
	private Logger logger = LoggerFactory.getLogger(TestRepositoryTest.class);

	@org.mockito.Mock
	private kc.framework.tenant.ITenantResolver tenantResolver;


	@BeforeEach
	void setUpBeforeClass() {
		intilize();

		when(tenantResolver.Resolve(TenantConstant.DbaTenantName)).thenReturn(TenantConstant.DbaTenantApiAccessInfo);
		when(tenantResolver.Resolve(TenantConstant.TestTenantName)).thenReturn(TenantConstant.TestTenantApiAccessInfo);

		TenantDataSourceProvider.addDataSource(TenantConstant.DbaTenantApiAccessInfo);
		TenantDataSourceProvider.addDataSource(TenantConstant.TestTenantApiAccessInfo);
	}
	
	@AfterEach
	void setDownAfterClass() {
		tearDown();
		
		TenantDataSourceProvider.clearDataSource();
	}
	
	@Autowired
	private ITestRepository userRepository;
	
	@Test
	void test_user_Dba_crud() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("-----test_user_Dba_crud Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		TestObj newUser = new TestObj("dba-aa1", new Date(), 15000d);
		TestObj dbUser = userRepository.save(newUser);
		assertEquals(newUser.getUserName(), dbUser.getUserName());

		TestObj user = userRepository.findByUserName("dba-aa1");
		assertEquals("dba-aa1", user.getUserName());

		userRepository.delete(user);
	}
	
	@Test
	void test_user_test_crud() {
		TenantContext.setCurrentTenant(TestTenant);
		logger.debug(String.format("-----test_user_test_crud Tenant: %s test----", TestTenant.getTenantName()));

		TestObj newUser = new TestObj("test-aa1", new Date(), 15000d);
		TestObj dbUser = userRepository.save(newUser);
		assertEquals(newUser.getUserName(), dbUser.getUserName());

		TestObj user = userRepository.findByUserName("test-aa1");
		assertEquals("test-aa1", user.getUserName());

		userRepository.delete(user);
	}
	
}
