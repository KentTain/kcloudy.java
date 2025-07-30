package kc.service.codegenerate;

import kc.framework.tenant.TenantContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Disabled
@DisplayName("GitLab服务测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GitlabServiceTest extends CodeGenerateTestBase{

	private final String testProjectName = "test";
	@Autowired
	private IGitlabService gitService;

	@Test
	@Order(1)
	@DisplayName("在Gitlab中创建应用的项目")
	void test_createTenantGitlabGroupAndUser() {
		TenantContext.setCurrentTenant(TestTenant);

		String token = gitService.createTenantGitlabGroupAndUser(TestTenant.getTenantName(), "123456");
		System.out.println("====cTest token: " + token);
	}

	@Test
	@Order(2)
	@DisplayName("在Gitlab中创建应用的项目")
	void test_createTenantGitProject() {
		TenantContext.setCurrentTenant(TestTenant);

		boolean success = gitService.createAppGitProject(testProjectName);
		assertTrue(success);
	}

	@Test
	@Order(3)
	@DisplayName("在本地初始化Gitlab的项目")
	void test_cloneAppGitProject() {
		boolean success = gitService.cloneAppGitProject(testProjectName);
		assertTrue(success);
	}

	@Test
	@Order(4)
	@DisplayName("在Gitlab中删除应用的项目")
	void test_deleteAppGitProject() {
		TenantContext.setCurrentTenant(TestTenant);

		boolean success = gitService.deleteAppGitProject(testProjectName);
		assertTrue(success);
	}
}
