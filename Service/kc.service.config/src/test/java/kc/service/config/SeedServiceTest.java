package kc.service.config;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.base.SeedEntity;
import kc.framework.tenant.TenantContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Disabled
@lombok.extern.slf4j.Slf4j
class SeedServiceTest extends ConfigTestBase {
	@Autowired
	private ISeedService seedService;

	@Test
	void test_getSeedCodeByName() {
		TenantContext.setCurrentTenant(TestTenant);

		String data = seedService.getSeedCodeByName("Customer", 1);
		assertNotNull(data);

		log.info(data);
	}

	@Test
	void test_getSeedEntityByName() {
		TenantContext.setCurrentTenant(TestTenant);

        SeedEntity data = seedService.getSeedEntityByName("Customer", 1);
		assertNotNull(data);

		log.info(data.getSeedValue());
	}


}
