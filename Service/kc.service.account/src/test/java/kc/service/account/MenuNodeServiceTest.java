package kc.service.account;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import kc.database.multitenancy.TenantDataSourceProvider;
import kc.dto.account.MenuNodeDTO;
import kc.framework.tenant.TenantContext;

@Disabled
@DisplayName("菜单管理")
class MenuNodeServiceTest extends AccountTestBase {
	@Autowired
	private IMenuNodeService menuService;

	@Test
	void test_GetRootMenusByName() {
		TenantContext.setCurrentTenant(TestTenant);

		List<MenuNodeDTO> data = menuService.GetRootMenusByName("系统");
		assertTrue(data.size() > 0);
		printTreeNodeDTOs(data);
	}
	
	@Test
	void test_UpdateRoleInMenu() {
		TenantContext.setCurrentTenant(TestTenant);

		boolean success = menuService.UpdateRoleInMenu(13, Arrays.asList(
				//"2B01379C-5DD6-4B1D-AAD4-1151CDEFEDEA",
				"2C8D8527-2C39-4321-8270-8549870718A9", 
				"0B6DE259-FBEA-401C-A112-4980AF659674", 
				"B779D882-68ED-4298-B233-AA68065C447B"), 
				"ede9edf9-5909-42d0-8563-aaa5c04cd8c8","admin");
		assertTrue(success);
	}

}
