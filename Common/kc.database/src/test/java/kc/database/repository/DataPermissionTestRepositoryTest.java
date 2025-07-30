package kc.database.repository;

import kc.database.DataPermissionTestObj;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.*;

@Disabled
@SpringBootTest
@ExtendWith(SpringExtension.class)
class DataPermissionTestRepositoryTest extends TestBase{
	private Logger logger = LoggerFactory.getLogger(DataPermissionTestRepositoryTest.class);
	
	@BeforeEach
	void setUpBeforeClass() {
		intilize();
		TenantContext.setCurrentTenant(TestTenant);

		TenantDataSourceProvider.addDataSource(TenantConstant.DbaTenantApiAccessInfo);
		TenantDataSourceProvider.addDataSource(TenantConstant.TestTenantApiAccessInfo);

		DataPermissionTestObj newUser1 = new DataPermissionTestObj(
				"test-1",
				Arrays.asList("1".split(",")), // orgIds
				Arrays.asList("1,2".split(",")), // roleIds
				Arrays.asList("1,2,3".split(",")));// userIds
		DataPermissionTestObj newUser2 = new DataPermissionTestObj(
				"test-2",
				Arrays.asList("1,2".split(",")), // orgIds
				Arrays.asList("1,2".split(",")), // roleIds
				Arrays.asList("1,2".split(",")));// userIds
		DataPermissionTestObj newUser3 = new DataPermissionTestObj(
				"test-3",
				Arrays.asList("1,2,3".split(",")), // orgIds
				Arrays.asList("1,2,3".split(",")), // roleIds
				Arrays.asList("1,2,3".split(",")));// userIds

		List<DataPermissionTestObj> allUsers = Arrays.asList(newUser1, newUser2, newUser3);
		List<DataPermissionTestObj> dbUsers = userRepository.saveAll(allUsers);
	}
	
	@AfterEach
	void setDownAfterClass() {
		DataPermissionTestObj newUser1 = userRepository.findByName("test-1");
		DataPermissionTestObj newUser2 = userRepository.findByName("test-2");
		DataPermissionTestObj newUser3 = userRepository.findByName("test-3");
		userRepository.delete(newUser1);
		userRepository.delete(newUser2);
		userRepository.delete(newUser3);

		tearDown();
		TenantDataSourceProvider.clearDataSource();
	}
	
	@Autowired
	private IDatePermissionTestRepository userRepository;

	@Test
	void test_user_TestTenant_crud() {
		TenantContext.setCurrentTenant(TestTenant);
		logger.debug(String.format("-----test_user_Dba_crud Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		List<String> orgIds = Arrays.asList("2", "3");

		List<DataPermissionTestObj> users1 = userRepository.findAll(new Specification<DataPermissionTestObj>() {
			@Override
			public Predicate toPredicate(Root<DataPermissionTestObj> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.conjunction());
				if (orgIds.size() > 0) {
					List<Predicate> orgPredicates = new ArrayList<>();
					Path<String> orgIdsPath = root.get("orgIdsString");//定义查询的字段
					for (int i = 0; i < orgIds.size(); i++) {
						orgPredicates.add(criteriaBuilder.like(orgIdsPath, "%" + orgIds.get(i) + "%"));
					}

					predicates.add(criteriaBuilder.or(orgPredicates.toArray(new Predicate[orgPredicates.size()])));
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		assertEquals(2, users1.size());
		System.out.println(users1.stream().map(DataPermissionTestObj::getOrgIds));

	}
}
