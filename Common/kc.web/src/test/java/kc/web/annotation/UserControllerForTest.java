package kc.web.annotation;

import kc.framework.tenant.RoleConstants;
import kc.framework.tenant.TenantConstant;

public class UserControllerForTest {
	
	/* 菜单数据
	 * |----1: 系统管理 				<br/>
	 * |----|----2: 用户管理		<br/>
	 * |----|----|----3: 用户管理	<br/>
	 * |----|----2: 组织管理		<br/>
	 * |----|----|----3: 组织管理	<br/>
	 */
	@MenuAnnotation(ParentMenuName = "系统管理", MenuName = "系统管理", Url = "/Home/System", 
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-user", AuthorityId = "C731F392-8567-43A8-A3C2-2B2E38AD7ACE", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 1)
	public void System() {
	}
	
	@MenuAnnotation(ParentMenuName = "系统管理", MenuName = "用户管理", Url = "/Home/User", 
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-user", AuthorityId = "8221F5A3-9657-4F75-8F02-34C3A43AD534", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
	public void User() {
	}
	
	@MenuAnnotation(ParentMenuName = "系统管理", MenuName = "组织管理", Url = "/Home/Organization", 
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-user", AuthorityId = "DE56620C-6183-4E3A-9AED-18E80A4FABB8", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 2)
	public void Organization() {
	}

	/*权限数据
	 * |----1: 组织管理:组织管理 		<br/> 
	 * |----1: 用户管理:用户管理 		<br/> 
	 * |----|----2: 用户管理:删除用户	<br/>
	 * |----|----2: 用户管理:用户详情 	<br/>
	 */
	@MenuAnnotation(ParentMenuName = "用户管理", MenuName = "用户管理", Url = "/User/Index", 
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-user", AuthorityId = "730D5415-C702-4948-A209-A077AD20D4DA", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 1, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户管理", Url = "/user/Index", IsPage = true, Order = 1, 
			AuthorityId = "D3C10538-BB36-44BE-8AED-2F45E2ED0389", DefaultRoleId = "126AC4CF-84CF-410B-8989-A4EB8397EC3F")
	public void Index() {
	}

	@PermissionAnnotation(MenuName = "用户管理", PermissionName = "用户详情", Url = "/user/userdetail", IsPage = false, Order = 2, 
			AuthorityId = "DD6466FA-FB91-4B77-A099-08F9A614F9AD", DefaultRoleId = "126AC4CF-84CF-410B-8989-A4EB8397EC3F")
	public void UserDetail() {
	}

	@PermissionAnnotation(MenuName = "用户管理", PermissionName = "删除用户", Url = "/user/removeuser", IsPage = false, Order = 3, 
			AuthorityId = "126AC4CF-84CF-410B-8989-A4EB8397EC3F", DefaultRoleId = "126AC4CF-84CF-410B-8989-A4EB8397EC3F")
	public void RemoveUser() {
	}

	@MenuAnnotation(ParentMenuName = "组织管理", MenuName = "组织管理", Url = "/organization/index",
			Version = TenantConstant.DefaultVersion, TenantType = TenantConstant.DefaultTenantType,
			SmallIcon = "fa fa-user", AuthorityId = "938A7166-D93C-4247-984A-8E19DE39B1EE", 
			DefaultRoleId = RoleConstants.AdminRoleId, Order = 2, IsExtPage = false, Level = 3)
	@PermissionAnnotation(MenuName = "组织管理", PermissionName = "组织管理", Url = "/organization/index", IsPage = true, Order = 1,
			AuthorityId = "D3C10538-BB36-44BE-8AED-2F45E2ED0389", DefaultRoleId = "11BAB4DC-6575-44BA-8BE3-72292CEC533A")
	public void OrgIndex() {
	}
}
