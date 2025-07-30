package kc.service.training;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.PaginatedBaseDTO;
import kc.dto.training.TeacherDTO;
import kc.framework.TestBase;
import kc.framework.tenant.TenantContext;
import kc.service.training.ITeacherService;

@Disabled
@DisplayName("配置测试")
@lombok.extern.slf4j.Slf4j
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(webEnvironment = WebEnvironment.MOCK)
//@org.springframework.boot.test.context.SpringBootTest(classes = { ConfigService.class, ConfigEntityMappingImpl.class })
class ConfigServiceTest extends TestBase {

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
	private ITeacherService configService;

//	@MockBean
//	private ITeacherRepository TeacherRepository;
//	@MockBean
//	private TeacherMapping TeacherMapping;

//	@Before
//	public void init() {
//		configService = new ConfigService();
//
//		// 写桩方法
//		when(TeacherMapping.queryHotTotal(DateUtil.today())).thenReturn(hopAppTotal);
//	}

	@Test
	void test_FindAll() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<TeacherDTO> data = configService.findPaginatedTeachersByNameAndType(0, 10, null, null);
		assertTrue(data.getRows().size() > 0);
	}
	
	@Test
	void test_FindConfigListByConfigNameAndConfigType() {
		TenantContext.setCurrentTenant(TestTenant);

		PaginatedBaseDTO<TeacherDTO> data = configService.findPaginatedTeachersByNameAndType(0, 10, "支付", null);
		assertTrue(data.getRows().size() > 0);
	}

}
