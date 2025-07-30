package kc.service.webapiservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import kc.dto.account.MenuNodeSimpleDTO;
import kc.dto.account.PermissionSimpleDTO;
import kc.framework.GlobalConfig;
import kc.framework.util.SerializeHelper;
import kc.framework.TestBase;
import kc.framework.enums.SystemType;
import kc.framework.tenant.RoleConstants;
import kc.service.base.ServiceResult;
import kc.service.webapiservice.impl.AccountApiService;

@Disabled
@ExtendWith(SpringExtension.class)
@org.springframework.boot.test.context.SpringBootTest(classes = { AccountApiService.class })
class AccountApiServiceTest extends TestBase{
	
	@BeforeAll
	static void setUpBeforeClass() {
		GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
		GlobalConfig.SystemType = SystemType.Dev;
		GlobalConfig.AccWebDomain = "http://cTest.localhost:2002/";
    	GlobalConfig.SSOWebDomain = "http://localhost:1001/";
    	
    	intilize();
	}

	@AfterAll
	static void setDownAfterClass() {
		GlobalConfig.EncryptKey = "";
		GlobalConfig.AccWebDomain = "";
    	GlobalConfig.SSOWebDomain = "";
    	
    	tearDown();
	}

	@Autowired
	private IAccountApiService accountApiService;
	
	@Test
	void test_MenuJsonConvert() {
		MenuNodeSimpleDTO menu = new MenuNodeSimpleDTO();
		String menuJson = "{\"$id\":\"137\",\"areaName\":\"\",\"actionName\":\"Index\",\"controllerName\":\"Organization\",\"smallIcon\":\"fa fa-sitemap\",\"bigIcon\":null,\"url\":\"/Organization/Index\",\"isExtPage\":false,\"applicationId\":\"45672506-ddb7-4d57-ad44-bd0ab136b556\",\"parentName\":\"\",\"tenantType\":63,\"description\":\"二级菜单【组织管理】下的三级菜单【组织架构管理】\",\"version\":7,\"parameters\":null,\"defaultRoleId\":null,\"authorityId\":\"4DB22A38-1759-40B2-9926-44FACFA59E68\",\"id\":4,\"text\":\"组织架构管理\",\"parentId\":2,\"treeCode\":\"1-2-4\",\"leaf\":true,\"level\":3,\"index\":1,\"children\":[],\"checked\":false}";
		menu = SerializeHelper.FromJson(menuJson, menu.getClass());
		assertTrue(menu != null);
		
		List<MenuNodeSimpleDTO> menuList = new ArrayList<MenuNodeSimpleDTO>();
		String menuListJson="[{\"$id\":\"137\",\"areaName\":\"\",\"actionName\":\"Index\",\"controllerName\":\"Organization\",\"smallIcon\":\"fa fa-sitemap\",\"bigIcon\":null,\"url\":\"/Organization/Index\",\"isExtPage\":false,\"applicationId\":\"45672506-ddb7-4d57-ad44-bd0ab136b556\",\"parentName\":\"\",\"tenantType\":63,\"description\":\"二级菜单【组织管理】下的三级菜单【组织架构管理】\",\"version\":7,\"parameters\":null,\"defaultRoleId\":null,\"authorityId\":\"4DB22A38-1759-40B2-9926-44FACFA59E68\",\"id\":4,\"text\":\"组织架构管理\",\"parentId\":2,\"treeCode\":\"1-2-4\",\"leaf\":true,\"level\":3,\"index\":1,\"children\":[],\"checked\":false},{\"$id\":\"138\",\"areaName\":\"\",\"actionName\":\"Index\",\"controllerName\":\"User\",\"smallIcon\":\"fa fa-user\",\"bigIcon\":null,\"url\":\"/User/Index\",\"isExtPage\":false,\"applicationId\":\"45672506-ddb7-4d57-ad44-bd0ab136b556\",\"parentName\":\"\",\"tenantType\":63,\"description\":\"二级菜单【用户管理】下的三级菜单【用户管理】\",\"version\":7,\"parameters\":null,\"defaultRoleId\":null,\"authorityId\":\"730D5415-C702-4948-A209-A077AD20D4DA\"}]";
		menuList = SerializeHelper.FromJson(menuListJson, new TypeReference<List<MenuNodeSimpleDTO>>() {});
		assertTrue(menuList != null && menuList.size() > 0);
		
		ServiceResult<List<MenuNodeSimpleDTO>> menuResult = new ServiceResult<List<MenuNodeSimpleDTO>>();
		String menuResultJson = "{\"$id\":\"136\",\"httpStatusCode\":200,\"resultType\":0,\"message\":\"操作成功！\",\"logMessage\":null,\"result\":[{\"$id\":\"137\",\"areaName\":\"\",\"actionName\":\"Index\",\"controllerName\":\"Organization\",\"smallIcon\":\"fa fa-sitemap\",\"bigIcon\":null,\"url\":\"/Organization/Index\",\"isExtPage\":false,\"applicationId\":\"45672506-ddb7-4d57-ad44-bd0ab136b556\",\"parentName\":\"\",\"tenantType\":63,\"description\":\"二级菜单【组织管理】下的三级菜单【组织架构管理】\",\"version\":7,\"parameters\":null,\"defaultRoleId\":null,\"authorityId\":\"4DB22A38-1759-40B2-9926-44FACFA59E68\",\"id\":4,\"text\":\"组织架构管理\",\"parentId\":2,\"treeCode\":\"1-2-4\",\"leaf\":true,\"level\":3,\"index\":1,\"children\":[],\"checked\":false},{\"$id\":\"138\",\"areaName\":\"\",\"actionName\":\"Index\",\"controllerName\":\"User\",\"smallIcon\":\"fa fa-user\",\"bigIcon\":null,\"url\":\"/User/Index\",\"isExtPage\":false,\"applicationId\":\"45672506-ddb7-4d57-ad44-bd0ab136b556\",\"parentName\":\"\",\"tenantType\":63,\"description\":\"二级菜单【用户管理】下的三级菜单【用户管理】\",\"version\":7,\"parameters\":null,\"defaultRoleId\":null,\"authorityId\":\"730D5415-C702-4948-A209-A077AD20D4DA\"}]}";
		menuResult = SerializeHelper.FromJson(menuResultJson, new TypeReference<ServiceResult<List<MenuNodeSimpleDTO>>>() {});
		assertTrue(menuResult != null && menuResult.isSuccess());
	}
	
	@Test
	void test_GetUserMenusByRoleIds()
	{
		List<String> roleIds = new ArrayList<String>();
		roleIds.add(RoleConstants.AdminRoleId);
		List<MenuNodeSimpleDTO> menuList = accountApiService.LoadUserMenusByRoleIds(roleIds);
		assertTrue(menuList != null && menuList.size() > 0);
		
		List<MenuNodeSimpleDTO> menus = menuList.stream().filter(m -> true)
				.sorted(Comparator.comparing(MenuNodeSimpleDTO::getIndex))
				.collect(Collectors.toList());
		assertTrue(menus != null && menus.size() > 0);
		
	}
	
	@Test
	void test_GetUserPermissionsByRoleIds()
	{
		List<String> roleIds = new ArrayList<String>();
		roleIds.add(RoleConstants.AdminRoleId);
		List<PermissionSimpleDTO> menuList = accountApiService.LoadUserPermissionsByRoleIds(roleIds);
		assertTrue(menuList != null && menuList.size() > 0);
	}
}
