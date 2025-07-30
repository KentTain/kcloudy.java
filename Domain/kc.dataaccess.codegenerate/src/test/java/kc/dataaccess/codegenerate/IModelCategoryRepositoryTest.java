package kc.dataaccess.codegenerate;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
//@Sql(scripts = "/config-data.sql")
public class IModelCategoryRepositoryTest extends TestBase{
	//private Logger logger = LoggerFactory.getLogger(ConfigRepositoryTest.class.getName());

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
    private IModelCategoryRepository appSettingRepository;

}
