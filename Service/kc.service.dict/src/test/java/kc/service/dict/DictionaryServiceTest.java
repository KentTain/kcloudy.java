package kc.service.dict;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.PaginatedBaseDTO;
import kc.dto.config.ConfigEntityDTO;
import kc.dto.dict.DictValueDTO;
import kc.framework.GlobalConfigData;
import kc.framework.TestBase;
import kc.framework.tenant.TenantContext;

import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Disabled
@DisplayName("字典测试")
@lombok.extern.slf4j.Slf4j
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(webEnvironment = WebEnvironment.MOCK)
//@org.springframework.boot.test.context.SpringBootTest(classes = { ConfigService.class, ConfigEntityMappingImpl.class })
class DictionaryServiceTest extends TestBase {
	@Autowired
	private IGlobalConfigApiService globalConfigApiService;

	@BeforeEach
	void setUpBeforeClass() {
		GlobalConfigData configData = globalConfigApiService.GetGlobalConfigData();
		intilize(configData);

		TenantDataSourceProvider.addDataSource(DbaTenant);
		TenantDataSourceProvider.addDataSource(TestTenant);
	}

	@AfterEach
	void setDownAfterClass() {
		tearDown();

		TenantDataSourceProvider.clearDataSource();
	}

	@Autowired
	private IDictionaryService dictionaryService;

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
	void test_findPaginatedDictValues() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<DictValueDTO> data = dictionaryService.findPaginatedDictValuesByFilter(0, 10, null, null);
		assertTrue(data.getRows().size() > 0);
	}
	
	@Test
	void test_findPaginatedDictValuesByFilter() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<DictValueDTO> data = dictionaryService.findPaginatedDictValuesByFilter(0, 10, "公告", null);
		assertTrue(data.getRows().size() > 0);
	}

}
