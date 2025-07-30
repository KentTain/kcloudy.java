package kc.dataaccess.training;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.model.training.Course;
import kc.model.training.Teacher;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
//@Sql(scripts = "/config-data.sql")
public class ITeacherRepositoryTest extends TestBase{
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
    private ICourseRepository courseRepository;
     
    @Autowired
    private ITeacherRepository TeacherRepository;


}
