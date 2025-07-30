package kc.dataaccess.config;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.base.ConfigAttribute;
import kc.framework.base.ConfigEntity;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
//@Sql(scripts = "/config-data.sql")
public class IConfigRepositoryTest extends TestBase{
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
    private IConfigAttributeRepository configAttributeRepository;
     
    @Autowired
    private IConfigEntityRepository configEntityRepository;
    
    @Test
    public void test_cDba_ConfigEnity_findByConfigName() {
    	TenantContext.setCurrentTenant(DbaTenant);
    	ConfigEntity configEntiy = configEntityRepository.findByConfigName("汇潮支付");
    	assertEquals(configEntiy.getConfigName(), "汇潮支付");
    	
    	List<ConfigAttribute> items = configEntiy.getConfigAttributes();
    	assertTrue(items.size() > 0);
    	
    }
    
    @Test
    public void test_cDba_ConfigEnity_findAllByConfigNameAndConfigType() {
    	TenantContext.setCurrentTenant(DbaTenant);
    	
    	Pageable pageable = PageRequest.of(0, 10);
    	Page<ConfigEntity> data = configEntityRepository.findAllByConfigNameContaining(pageable, "支付");
    	assertTrue(data.hasContent());
    }
    
    @Test
    public void test_cDba_ConfigAttribute_findByName() {
    	TenantContext.setCurrentTenant(TestTenant);
        ConfigAttribute item = configAttributeRepository.findByName("AdminAdviceURL");
        assertEquals(item.getName(), "AdminAdviceURL");
        
        ConfigEntity entity = item.getConfigEntity();
        assertTrue(entity != null);
    }

}
