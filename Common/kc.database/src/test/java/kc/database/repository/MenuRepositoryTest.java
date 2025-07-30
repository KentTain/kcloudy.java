package kc.database.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import kc.database.MenuNode;
import kc.framework.tenant.TenantContext;
import kc.database.multitenancy.TenantDataSourceProvider;
import kc.framework.TestBase;
import kc.framework.tenant.TenantConstant;

@Disabled
@SpringBootTest
@ExtendWith(SpringExtension.class)
class MenuRepositoryTest extends TestBase{
	private static Logger logger = LoggerFactory.getLogger(MenuRepositoryTest.class);

	@Autowired
	private IMenuRepository menuRepository;

	@BeforeEach
	void setUpBeforeClass() {
		intilize();
		
		TenantDataSourceProvider.addDataSource(TenantConstant.DbaTenantApiAccessInfo);
		TenantDataSourceProvider.addDataSource(TenantConstant.TestTenantApiAccessInfo);
	}
	@AfterEach
	void setDownAfterClass() {
		tearDown();
		
		TenantDataSourceProvider.clearDataSource();
	}
	

	@Test
	void test_menu_dba_getTreeNodeWithNestChildById() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("---getTreeNodeWithNestChildById Tenant: %s test----",
				TenantContext.getCurrentTenant().getTenantName()));

		//初始化菜单数据
		List<MenuNode> menusData = InitMenus(TenantConstant.DbaTenantName);
		List<MenuNode> dbMenus = menuRepository.saveAllTreeNodeWithExtensionFields(menusData);
		Optional<MenuNode> rootNodeOptional = dbMenus.stream().findFirst();
		MenuNode firstRoot = rootNodeOptional.get();
		//MenuNode firstLevel2 = firstRoot.getChildNodes().iterator().next();
		
		int firstRootId = firstRoot == null ? 1 : firstRoot.getId();
		MenuNode firstRootResult = menuRepository.getTreeNodeWithNestChildById(MenuNode.class, firstRootId);
		MenuNode firstLevel2Result = firstRootResult.getChildNodes().iterator().next();
		TestBase.printTreeNode(firstRootResult, 1);

		assertNotNull(firstRootResult);
		assertNotNull(firstLevel2Result);
		//assertEquals(firstRoot.getId(), firstRootResult.getId());
		//assertEquals(firstLevel2.getName(), firstLevel2Result.getName());
		
		//删除初始化的菜单数据
		menuRepository.removeTreeNodesWithChild(dbMenus);
	}
	
	@Test
	void test_menu_dba_findAllTreeNodeWithNestChildByName() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("---findAllTreeNodeWithNestChildByName Tenant: %s test----",
				TenantContext.getCurrentTenant().getTenantName()));

		// 初始化菜单数据
		List<MenuNode> menusData = InitMenus(TenantConstant.DbaTenantName);
		List<MenuNode> dbMenus = menuRepository.saveAllTreeNodeWithExtensionFields(menusData);
		Optional<MenuNode> rootNodeOptional = dbMenus.stream().findFirst();
		MenuNode firstRoot = rootNodeOptional.get();
		//MenuNode firstLevel2 = firstRoot.getChildNodes().iterator().next();
		
		int firstRootId = firstRoot == null ? 1 : firstRoot.getId();
		MenuNode firstRootResult = menuRepository.getTreeNodeWithNestChildById(MenuNode.class, firstRootId);
		MenuNode firstLevel2Result = firstRootResult.getChildNodes().iterator().next();

		assertNotNull(firstRootResult);
		assertNotNull(firstLevel2Result);
		//assertEquals(firstRoot.getId(), firstRootResult.getId());
		//assertEquals(firstLevel2.getName(), firstLevel2Result.getName());
		
		//删除初始化的菜单数据
		menuRepository.removeTreeNodesWithChild(dbMenus);
		TestBase.printTreeNode(firstRootResult, 1);
	}
	
	@Test
	void test_menu_dba_findTreeNodesWithNestParentAndChildByTreeCode() {
		TenantContext.setCurrentTenant(DbaTenant);
		logger.debug(String.format("---findTreeNodesWithNestParentAndChildByTreeCode Tenant: %s test----",
				TenantContext.getCurrentTenant().getTenantName()));

		// 初始化菜单数据
		List<MenuNode> menusData = InitMenus(TenantConstant.DbaTenantName);
		List<MenuNode> dbMenus = menuRepository.saveAllTreeNodeWithExtensionFields(menusData);
		Optional<MenuNode> rootNodeOptional = dbMenus.stream().findFirst();
		MenuNode firstRoot = rootNodeOptional.get();
		MenuNode firstLevel2 = firstRoot.getChildNodes().iterator().next();
		
		String firstLevel2TreeCode = firstLevel2 == null ? "1-2" : firstLevel2.getTreeCode();
		List<MenuNode> menus = menuRepository.findTreeNodesWithNestParentAndChildByTreeCode(MenuNode.class, firstLevel2TreeCode);
		Optional<MenuNode> rootNodeResultOptional = menus.stream().findFirst();
		MenuNode firstRootResult = rootNodeResultOptional.get();
		MenuNode firstLevel2Result = firstRootResult.getChildNodes().iterator().next();
		TestBase.printTreeNodes(menus);

		assertNotNull(firstRootResult);
		assertNotNull(firstLevel2Result);
		//assertEquals(firstRoot.getId(), firstRootResult.getId());
		//assertEquals(firstLevel2.getName(), firstLevel2Result.getName());

		//删除初始化的菜单数据
		menuRepository.removeTreeNodesWithChild(dbMenus);
	}

	@Test
	void test_menu_test_crud() {
		TenantContext.setCurrentTenant(TestTenant);
		logger.debug(String.format("-----test_menu_test_crud Tenant: %s test----", TenantContext.getCurrentTenant().getTenantName()));

		List<MenuNode> menus = InitMenus(TenantConstant.TestTenantName);
		TestBase.printTreeNodes(menus);

		List<MenuNode> dbMenus = menuRepository.saveAllTreeNodeWithExtensionFields(menus);
		assertTrue(dbMenus != null && dbMenus.size() > 0);
		assertEquals(menus.get(0).getName(), dbMenus.get(0).getName());

		MenuNode menu = menuRepository.findByName(TenantConstant.TestTenantName + "-menu-1");
		assertNotNull(menu);

		int i = menuRepository.removeTreeNodesWithChild(dbMenus);
		assertTrue(i > 0);
	}
	
	/**
	 * 初始化菜单数据 <br/>
	 * cDba-root-1 <br/>
	 * |----cDba-menu-1 <br/>
	 * |----|----cDba-menu-1-1 <br/>
	 * |----|----cDba-menu-1-2 <br/>
	 * |----|----cDba-menu-1-3 <br/>
	 * |----|----cDba-menu-1-4 <br/>
	 * |----|----|----cDba-menu-1-4-1 <br/>
	 * |----|----|----cDba-menu-1-4-2 <br/>
	 * |----|----|----cDba-menu-1-4-3 <br/>
	 * |----cDba-menu-2 <br/>
	 * |----|----cDba-menu-2-3 <br/>
	 * |----|----cDba-menu-2-2 <br/>
	 * |----|----cDba-menu-2-1 <br/>
	 * cDba-root-2 <br/>
	 * |----cDba-menu-1 <br/>
	 * |----cDba-menu-2 <br/>
	 * @return List<MenuNode>
	 */
	private static List<MenuNode> InitMenus(String tenantName) {
		List<MenuNode> resultList = new ArrayList<>();

		MenuNode rootNode0 = new MenuNode(tenantName + "-root-1");
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
		MenuNode node0 = new MenuNode(tenantName + "-menu-1");
		node0.setName(tenantName + "-menu-1");
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
		MenuNode node0_0 = new MenuNode(tenantName + "-menu-1-1");
		node0_0.setName(tenantName + "-menu-1-1");
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
		MenuNode node0_1 = new MenuNode(tenantName + "-menu-1-2");
		node0_1.setName(tenantName + "-menu-1-2");
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
		MenuNode node0_2 = new MenuNode(tenantName + "-menu-1-3");
		node0_2.setName(tenantName + "-menu-1-3");
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
		MenuNode node0_3 = new MenuNode(tenantName + "-menu-1-4");
		node0_3.setName(tenantName + "-menu-1-4");
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
		MenuNode node0_3_0 = new MenuNode(tenantName + "-menu-1-4-1");
		node0_3_0.setName(tenantName + "-menu-1-4-1");
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
		MenuNode node0_3_1 = new MenuNode(tenantName + "-menu-1-4-2");
		node0_3_1.setName(tenantName + "-menu-1-4-2");
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
		MenuNode node0_3_2 = new MenuNode(tenantName + "-menu-1-4-3");
		node0_3_2.setName(tenantName + "-menu-1-4-3");
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
		MenuNode node1 = new MenuNode(tenantName + "-menu-2");
		node1.setName(tenantName + "-menu-2");
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
		MenuNode node1_0 = new MenuNode(tenantName + "-menu-2-1");
		node1_0.setName(tenantName + "-menu-2-1");
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
		MenuNode node1_1 = new MenuNode(tenantName + "-menu-2-2");
		node1_1.setName(tenantName + "-menu-2-2");
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
		MenuNode node1_2 = new MenuNode(tenantName + "-menu-2-3");
		node1_2.setName(tenantName + "-menu-2-3");
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

		MenuNode rootNode1 = new MenuNode(tenantName + "-root-2");
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
		MenuNode node01 = new MenuNode(tenantName + "-menu-1");
		node01.setName(tenantName + "-menu-1");
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
		MenuNode node02 = new MenuNode(tenantName + "-menu-2");
		node02.setName(tenantName + "-menu-2");
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
}
