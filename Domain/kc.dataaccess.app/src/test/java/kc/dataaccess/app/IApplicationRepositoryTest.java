package kc.dataaccess.app;

import kc.model.app.Application;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;

import java.util.UUID;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IApplicationRepositoryTest extends TestBase {
	private Logger logger = LoggerFactory.getLogger(IApplicationRepositoryTest.class.getName());

	@BeforeEach
	public void setUpBeforeClass() throws Exception {
		intilize();
		
		TenantDataSourceProvider.addDataSource(DbaTenant);
		TenantDataSourceProvider.addDataSource(TestTenant);
	}

	@AfterEach
	public void setDownAfterClass() throws Exception {
		tearDown();
		
		TenantDataSourceProvider.clearDataSource();
	}

	@Autowired
	private IApplicationRepository sysSequenceRepository;

	@Test
	public void test_sysSequence_devdb_crud() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("-----Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		String appId = UUID.randomUUID().toString();
		Application newUser = Application.builder()
				.applicationId(appId)
				.applicationName("TestApplication")
				.applicationCode("testapp")
				.domainName("http://domain.com")
				.isEnabledWorkFlow(false).build();
		Application dbUser = sysSequenceRepository.saveAndFlush(newUser);
		assertNotNull(dbUser);
		assertEquals(newUser.getApplicationName(), dbUser.getApplicationName());

		boolean isExists = sysSequenceRepository.existsById(appId);
		assertTrue(isExists);
		
		//Application user = sysSequenceRepository.findByPreFixString("CRU");
		//Application user2 = sysSequenceRepository.findByPreFixString("End");

		sysSequenceRepository.delete(dbUser);
	}

}
