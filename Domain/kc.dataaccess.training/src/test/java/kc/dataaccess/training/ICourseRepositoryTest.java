package kc.dataaccess.training;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.model.training.Course;
import kc.framework.tenant.TenantContext;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ICourseRepositoryTest extends TestBase {

	@BeforeAll
	public static void setUpBeforeClass() throws Exception {
		intilize();

		TenantDataSourceProvider.addDataSource(DbaTenant);
		TenantDataSourceProvider.addDataSource(TestTenant);
	}

	@AfterAll
	public static void tearDownAfterClass() throws Exception {
		tearDown();

		TenantDataSourceProvider.clearDataSource();
	}

	@Autowired
    private ICourseRepository courseRepository;
	



}
