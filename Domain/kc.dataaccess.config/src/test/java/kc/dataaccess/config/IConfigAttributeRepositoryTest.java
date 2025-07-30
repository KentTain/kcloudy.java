package kc.dataaccess.config;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.base.ConfigAttribute;
import kc.framework.tenant.TenantContext;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
class IConfigAttributeRepositoryTest extends TestBase {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		intilize();

		TenantDataSourceProvider.addDataSource(DbaTenant);
		TenantDataSourceProvider.addDataSource(TestTenant);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		tearDown();

		TenantDataSourceProvider.clearDataSource();
	}

	@Autowired
    private IConfigAttributeRepository configAttributeRepository;
	
	@Test
	void test_FindByName() {
		TenantContext.setCurrentTenant(TestTenant);
		ConfigAttribute configAttribute = configAttributeRepository.findByName("汇潮支付");
    	assertNull(configAttribute);
	}

	@Test
	void test_FindByConfigId() {
		TenantContext.setCurrentTenant(TestTenant);
		List<ConfigAttribute> configAttributes = configAttributeRepository.findByConfigId(1);
		assertTrue(configAttributes.size() > 0);
	}

}
