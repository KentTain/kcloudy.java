package kc.dataaccess.app;

import java.util.List;
import java.util.Set;

import kc.model.app.AppSetting;
import kc.model.app.AppSettingProperty;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
//@Sql(scripts = "/config-data.sql")
public class IAppSettingRepositoryTest extends TestBase{
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
    private IAppSettingPropertyRepository appSettingPropertyRepository;
     
    @Autowired
    private IAppSettingRepository appSettingRepository;
    
    @Test
    public void test_cDba_ConfigEnity_findByConfigName() {
    	TenantContext.setCurrentTenant(DbaTenant);
    	AppSetting configEntiy = appSettingRepository.findByCode("汇潮支付");
    	assertEquals(configEntiy.getName(), "汇潮支付");
    	
    	List<AppSettingProperty> items = configEntiy.getPropertyAttributeList();
    	assertTrue(items.size() > 0);
    	
    }

    
    @Test
    public void test_cDba_ConfigAttribute_findByName() {
    	TenantContext.setCurrentTenant(TestTenant);
		AppSettingProperty item = appSettingPropertyRepository.findByName("AdminAdviceURL");
        assertEquals(item.getName(), "AdminAdviceURL");

		AppSetting entity = item.getAppSetting();
        assertTrue(entity != null);
    }

}
