package kc.dataaccess.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;
import kc.model.account.Organization;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrganizationRepositoryTest extends TestBase{
	private static Logger logger = LoggerFactory.getLogger(OrganizationRepositoryTest.class);

	@Autowired
	private IOrganizationRepository organizationRepository;

	@BeforeEach
	public void setUpBeforeClass() throws Exception {
		intilize();
		
		TenantDataSourceProvider.addDataSource(TenantConstant.DbaTenantApiAccessInfo);
		TenantDataSourceProvider.addDataSource(TenantConstant.TestTenantApiAccessInfo);
	}
	@AfterEach
	public void setDownAfterClass() throws Exception {
		tearDown();
		
		TenantDataSourceProvider.clearDataSource();
	}
	
	@Test
	public void test_organization_crud() throws Exception {
		TenantContext.setCurrentTenant(TestTenant);
		logger.debug(String.format("-----test_organization Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		List<Organization> groups = InitGroups(TenantConstant.TestTenantName);
		TestBase.printTreeNodes(groups);

		List<Organization> dbgroups = organizationRepository.saveAllTreeNodeWithExtensionFields(groups);
		assertEquals(true, dbgroups != null && dbgroups.size() > 0);
		assertEquals(groups.get(0).getName(), dbgroups.get(0).getName());

		Organization group = organizationRepository.findWithParentById(2);
		assertEquals(true, group != null);

		int i = organizationRepository.removeTreeNodesWithChild(dbgroups);
		assertEquals(true, i > 0);
	}
	
	/**
	 * 初始化菜单数据 <br/>
	 * cDba-root-1 <br/>
	 * |----cDba-group-1 <br/>
	 * |----|----cDba-group-1-1 <br/>
	 * |----|----cDba-group-1-2 <br/>
	 * |----|----cDba-group-1-3 <br/>
	 * |----|----cDba-group-1-4 <br/>
	 * |----|----|----cDba-group-1-4-1 <br/>
	 * |----|----|----cDba-group-1-4-2 <br/>
	 * |----|----|----cDba-group-1-4-3 <br/>
	 * |----cDba-group-2 <br/>
	 * |----|----cDba-group-2-3 <br/>
	 * |----|----cDba-group-2-2 <br/>
	 * |----|----cDba-group-2-1 <br/>
	 * cDba-root-2 <br/>
	 * |----cDba-group-1 <br/>
	 * |----cDba-group-2 <br/>
	 * @return
	 */
	private static List<Organization> InitGroups(String tenantName) {
		List<Organization> resultList = new ArrayList<Organization>();

		Organization rootNode0 = new Organization();
		rootNode0.setName(tenantName + "-root-1");
		rootNode0.setParentNode(null);
		rootNode0.setLeaf(false);
		rootNode0.setLevel(0);
		rootNode0.setIndex(1);
		rootNode0.setCreatedBy("admin");
		rootNode0.setCreatedDate(new Date());
		rootNode0.setModifiedBy("admin");
		rootNode0.setModifiedDate(new Date());
		rootNode0.setDeleted(false);

		// 一级
		Organization node0 = new Organization();
		node0.setName(tenantName + "-group-1");
		node0.setLeaf(false);
		node0.setLevel(1);
		node0.setIndex(1);
		node0.setCreatedBy("admin");
		node0.setCreatedDate(new Date());
		node0.setModifiedBy("admin");
		node0.setModifiedDate(new Date());
		node0.setDeleted(false);
		node0.setParentNode(rootNode0);
		rootNode0.getChildNodes().add(node0);

		// 二级
		Organization node0_0 = new Organization();
		node0_0.setName(tenantName + "-group-1-1");
		node0_0.setLeaf(false);
		node0_0.setLevel(1);
		node0_0.setIndex(1);
		node0_0.setCreatedBy("admin");
		node0_0.setCreatedDate(new Date());
		node0_0.setModifiedBy("admin");
		node0_0.setModifiedDate(new Date());
		node0_0.setDeleted(false);
		node0_0.setParentNode(node0);
		node0.getChildNodes().add(node0_0);
		// 二级
		Organization node0_1 = new Organization();
		node0_1.setName(tenantName + "-group-1-2");
		node0_1.setLeaf(false);
		node0_1.setLevel(2);
		node0_1.setIndex(1);
		node0_1.setCreatedBy("admin");
		node0_1.setCreatedDate(new Date());
		node0_1.setModifiedBy("admin");
		node0_1.setModifiedDate(new Date());
		node0_1.setDeleted(false);
		node0_1.setParentNode(node0);
		node0.getChildNodes().add(node0_1);
		// 二级
		Organization node0_2 = new Organization();
		node0_2.setName(tenantName + "-group-1-3");
		node0_2.setLeaf(false);
		node0_2.setLevel(2);
		node0_2.setIndex(2);
		node0_2.setCreatedBy("admin");
		node0_2.setCreatedDate(new Date());
		node0_2.setModifiedBy("admin");
		node0_2.setModifiedDate(new Date());
		node0_2.setDeleted(false);
		node0_2.setParentNode(node0);
		node0.getChildNodes().add(node0_2);
		// 二级
		Organization node0_3 = new Organization();
		node0_3.setName(tenantName + "-group-1-4");
		node0_3.setLeaf(false);
		node0_3.setLevel(2);
		node0_3.setIndex(3);
		node0_3.setCreatedBy("admin");
		node0_3.setCreatedDate(new Date());
		node0_3.setModifiedBy("admin");
		node0_3.setModifiedDate(new Date());
		node0_3.setDeleted(false);
		node0_3.setParentNode(node0);
		node0.getChildNodes().add(node0_3);

		// 三级
		Organization node0_3_0 = new Organization();
		node0_3_0.setName(tenantName + "-group-1-4-1");
		node0_3_0.setLeaf(true);
		node0_3_0.setLevel(3);
		node0_3_0.setIndex(1);
		node0_3_0.setCreatedBy("admin");
		node0_3_0.setCreatedDate(new Date());
		node0_3_0.setModifiedBy("admin");
		node0_3_0.setModifiedDate(new Date());
		node0_3_0.setDeleted(false);
		node0_3_0.setParentNode(node0_3);
		node0_3.getChildNodes().add(node0_3_0);
		// 三级
		Organization node0_3_1 = new Organization();
		node0_3_1.setName(tenantName + "-group-1-4-2");
		node0_3_1.setLeaf(true);
		node0_3_1.setLevel(3);
		node0_3_1.setIndex(2);
		node0_3_1.setCreatedBy("admin");
		node0_3_1.setCreatedDate(new Date());
		node0_3_1.setModifiedBy("admin");
		node0_3_1.setModifiedDate(new Date());
		node0_3_1.setDeleted(false);
		node0_3_1.setParentNode(node0_3);
		node0_3.getChildNodes().add(node0_3_1);
		// 三级
		Organization node0_3_2 = new Organization();
		node0_3_2.setName(tenantName + "-group-1-4-3");
		node0_3_2.setLeaf(true);
		node0_3_2.setLevel(3);
		node0_3_2.setIndex(2);
		node0_3_2.setCreatedBy("admin");
		node0_3_2.setCreatedDate(new Date());
		node0_3_2.setModifiedBy("admin");
		node0_3_2.setModifiedDate(new Date());
		node0_3_2.setDeleted(false);
		node0_3_2.setParentNode(node0_3);
		node0_3.getChildNodes().add(node0_3_2);

		// 一级
		Organization node1 = new Organization();
		node1.setName(tenantName + "-group-2");
		node1.setLeaf(false);
		node1.setLevel(1);
		node1.setIndex(2);
		node1.setCreatedBy("admin");
		node1.setCreatedDate(new Date());
		node1.setModifiedBy("admin");
		node1.setModifiedDate(new Date());
		node1.setDeleted(false);
		node1.setParentNode(rootNode0);
		rootNode0.getChildNodes().add(node1);
		// 二级
		Organization node1_0 = new Organization();
		node1_0.setName(tenantName + "-group-2-1");
		node1_0.setLeaf(true);
		node1_0.setLevel(2);
		node1_0.setIndex(1);
		node1_0.setCreatedBy("admin");
		node1_0.setCreatedDate(new Date());
		node1_0.setModifiedBy("admin");
		node1_0.setModifiedDate(new Date());
		node1_0.setDeleted(false);
		node1_0.setParentNode(node1);
		node1.getChildNodes().add(node1_0);
		// 二级
		Organization node1_1 = new Organization();
		node1_1.setName(tenantName + "-group-2-2");
		node1_1.setLeaf(true);
		node1_1.setLevel(2);
		node1_1.setIndex(2);
		node1_1.setCreatedBy("admin");
		node1_1.setCreatedDate(new Date());
		node1_1.setModifiedBy("admin");
		node1_1.setModifiedDate(new Date());
		node1_1.setDeleted(false);
		node1_1.setParentNode(node1);
		node1.getChildNodes().add(node1_1);
		// 二级
		Organization node1_2 = new Organization();
		node1_2.setName(tenantName + "-group-2-3");
		node1_2.setLeaf(true);
		node1_2.setLevel(2);
		node1_2.setIndex(3);
		node1_2.setCreatedBy("admin");
		node1_2.setCreatedDate(new Date());
		node1_2.setModifiedBy("admin");
		node1_2.setModifiedDate(new Date());
		node1_2.setDeleted(false);
		node1_2.setParentNode(node1);
		node1.getChildNodes().add(node1_2);

		resultList.add(rootNode0);

		Organization rootNode1 = new Organization();
		rootNode1.setName(tenantName + "-root-2");
		rootNode1.setParentNode(null);
		rootNode1.setLeaf(false);
		rootNode1.setLevel(0);
		rootNode1.setIndex(1);
		rootNode1.setCreatedBy("admin");
		rootNode1.setCreatedDate(new Date());
		rootNode1.setModifiedBy("admin");
		rootNode1.setModifiedDate(new Date());
		rootNode1.setDeleted(false);

		// 一级
		Organization node01 = new Organization();
		node01.setName(tenantName + "-group-1");
		node01.setLeaf(false);
		node01.setLevel(1);
		node01.setIndex(1);
		node01.setCreatedBy("admin");
		node01.setCreatedDate(new Date());
		node01.setModifiedBy("admin");
		node01.setModifiedDate(new Date());
		node01.setDeleted(false);
		node01.setParentNode(rootNode1);
		rootNode1.getChildNodes().add(node01);

		// 一级
		Organization node02 = new Organization();
		node02.setName(tenantName + "-group-2");
		node02.setLeaf(false);
		node02.setLevel(1);
		node02.setIndex(2);
		node02.setCreatedBy("admin");
		node02.setCreatedDate(new Date());
		node02.setModifiedBy("admin");
		node02.setModifiedDate(new Date());
		node02.setDeleted(false);
		node02.setParentNode(rootNode1);
		rootNode1.getChildNodes().add(node02);

		resultList.add(rootNode1);

		return resultList;
	}
	
	@Test
	public void test_FindById() {
		TenantContext.setCurrentTenant(TestTenant);
		
		Organization group = organizationRepository.findWithParentById(2);
		assertEquals(true, group != null);
		assertEquals(true, group.getParentNode() != null);
	}

	@Test
	public void test_findAllTreeNodesWithNestParentAndChildByName() {
		TenantContext.setCurrentTenant(TestTenant);

		List<Organization> data = organizationRepository.findAllTreeNodesWithNestParentAndChildByName(Organization.class, "采购");
		assertTrue(data.size() > 0);
		TestBase.printTreeNodes(data);
	}
	
	@Test
	public void test_getTreeNodeWithNestChildById() {
		TenantContext.setCurrentTenant(TestTenant);

		Organization data = organizationRepository.getTreeNodeWithNestChildById(Organization.class, 1);
		assertTrue(data.getChildNodes().size() > 0);
		TestBase.printTreeNode(data, 1);
	}
	
	@Test
	public void test_existsByName() {
		TenantContext.setCurrentTenant(TestTenant);

		boolean exist = organizationRepository.existsByName("采购部");
		assertTrue(exist);
		exist = organizationRepository.existsByName("采购部--no");
		assertFalse(exist);
		
		exist = organizationRepository.existsByIdAndName(1, "采购部");
		assertTrue(exist);
		exist = organizationRepository.existsByIdAndName(1, "采购部--no");
		assertFalse(exist);
	}
}
