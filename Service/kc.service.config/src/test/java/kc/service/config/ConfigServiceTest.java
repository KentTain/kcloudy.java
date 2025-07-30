package kc.service.config;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.PaginatedBaseDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.framework.TestBase;
import kc.framework.tenant.TenantContext;

@Disabled
@lombok.extern.slf4j.Slf4j
class ConfigServiceTest extends ConfigTestBase {

	@Autowired
	private IConfigService configService;

//	@MockBean
//	private IConfigEntityRepository configEntityRepository;
//	@MockBean
//	private ConfigEntityMapping configEntityMapping;

//	@Before
//	public void init() {
//		configService = new ConfigService();
//
//		// 写桩方法
//		when(configEntityMapping.queryHotTotal(DateUtil.today())).thenReturn(hopAppTotal);
//	}

	@Test
	void test_FindAll() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<ConfigEntityDTO> data = configService.findPaginatedConfigsByNameAndType(0, 10, null, null);
		assertTrue(data.getRows().size() > 0);
	}
	
	@Test
	void test_FindConfigListByConfigNameAndConfigType() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<ConfigEntityDTO> data = configService.findPaginatedConfigsByNameAndType(0, 10, "支付", null);
		assertTrue(data.getRows().size() > 0);
	}

}
