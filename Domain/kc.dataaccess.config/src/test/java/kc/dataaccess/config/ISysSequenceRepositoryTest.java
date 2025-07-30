package kc.dataaccess.config;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.model.config.SysSequence;
import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ISysSequenceRepositoryTest extends TestBase {
	private Logger logger = LoggerFactory.getLogger(ISysSequenceRepositoryTest.class.getName());

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
	private ISysSequenceRepository sysSequenceRepository;

	@Test
	public void test_sysSequence_devdb_crud() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("-----Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		SysSequence newUser = SysSequence.builder().sequenceName("TestSeq").initValue(1).maxValue(10000).currentValue(1)
				.stepValue(1).currDate("2019-01-01").preFixString("CRU").postFixString("End").comments("Test").build();
		SysSequence dbUser = sysSequenceRepository.saveAndFlush(newUser);
		assertEquals(true, dbUser != null);
		assertEquals(newUser.getSequenceName(), dbUser.getSequenceName());

		boolean isExists = sysSequenceRepository.existsById("TestSeq");
		assertEquals(true, isExists);
		
		//SysSequence user = sysSequenceRepository.findByPreFixString("CRU");
		//SysSequence user2 = sysSequenceRepository.findByPreFixString("End");

		sysSequenceRepository.delete(dbUser);
	}

}
