package kc.service.util;

import kc.dto.codegenerate.GitFileTreeDTO;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Gitlab测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Gitlab4jUtilTest {
    /**
     * http://gitlab.kcloudy.com
     */
    static final String gitLabUrl = "http://gitlab.kcloudy.com";
    static final String local_test_git_path = "D:\\Project\\git-test\\test";

    static final String branch_main = "main";

    static final String tenant_group_name = "tenant";
    static final String admin_user_name = "tester";
    static final String admin_password = "abc123456";
    static final String admin_token = "uEjxNXLsit9P5KyZySxy";

    static final String test_user_name = "tenant-tester";
    static final String test_password = "P@ssw0rd";
    static final String test_group_name = "tenant-group";
    static final String test_project_name = "tenant-project";

    @Test
    @Order(1)
    @DisplayName("Gitlab异常测试")
    void test_verify_userAndPassword_Exception() {
        Assertions.assertThrows(GitLabApiException.class, () -> {
            Gitlab4jUtil.getGitlabApi(gitLabUrl, "tester", "abc12345");
        });
    }

    @Test
    @Order(2)
    @DisplayName("Gitlab获取项目测试")
    void test_verify_gitToken() throws GitLabApiException {
        GitLabApi gitLabApi = Gitlab4jUtil.getGitlabApi(gitLabUrl, admin_user_name, admin_password);
        // Get the list of projects your account has access to
        List<Project> projects = gitLabApi.getProjectApi().getProjects();
        assertTrue(projects.size() > 0);

        String codeUrl = "http://gitlab.kcloudy.com/demo/net/kcloudy.demotest.net.git";
        Optional<Project> optionalProject = gitLabApi.getProjectApi().getProjectsStream()
                .filter(m -> m.getHttpUrlToRepo().equalsIgnoreCase(codeUrl))
                .findFirst();
        Project project = optionalProject.get();
        assertNotNull(project);

        // Create a GitLabApi instance to communicate with your GitLab server
        gitLabApi = Gitlab4jUtil.getGitlabApi(gitLabUrl, admin_token);
        assertNotNull(gitLabApi);
    }

    @Test
    @Order(3)
    @DisplayName("Gitlab获取源码zip文件")
    void test_download_source_zip() throws GitLabApiException, IOException {
        GitLabApi gitLabApi = Gitlab4jUtil.getGitlabApi(gitLabUrl, admin_user_name, admin_password);
        // Get the list of projects your account has access to
        List<Project> projects = gitLabApi.getProjectApi().getProjects();
        assertTrue(projects.size() > 0);

        String codeUrl = "http://gitlab.kcloudy.com/demo/net/kcloudy.demotest.net.git";
        Optional<Project> optionalProject = gitLabApi.getProjectApi().getProjectsStream()
                .filter(m -> m.getHttpUrlToRepo().equalsIgnoreCase(codeUrl))
                .findFirst();
        Project project = optionalProject.get();
        assertNotNull(project);

        // Create a GitLabApi instance to communicate with your GitLab server
        InputStream inStream  = gitLabApi.getRepositoryApi().getRepositoryArchive(project, "8931a2ea231f97ce27208df7fd62a1af8e9c397f", "zip");
        assertNotNull(inStream);
        //Files.copy(inStream, Paths.get(local_test_git_path, "kcloudy.demotest.net.zip"), StandardCopyOption.REPLACE_EXISTING);

    }

    @Test
    @Order(4)
    @DisplayName("创建用户、分组及项目测试")
    void test_create_project() throws GitLabApiException {
        GitLabApi admin_gitLabApi = Gitlab4jUtil.getGitlabApi(gitLabUrl, admin_user_name, admin_password);
        //获取租户分组
        Integer parentId = null;
        Optional<Group> optGroup = admin_gitLabApi.getGroupApi().getGroupsStream()
                .filter(m -> m.getPath().equalsIgnoreCase(tenant_group_name))
                .findFirst();
        if (optGroup.isPresent()) {
            parentId = optGroup.get().getId();
            System.out.println("----Exist the parent project: " + parentId);
        } else {
            GroupParams groupSpec = new GroupParams()
                    .withName(tenant_group_name)
                    .withPath(tenant_group_name)
                    .withProjectCreationLevel(GroupParams.ProjectCreationLevel.DEVELOPER);
            Group newGroup = admin_gitLabApi.getGroupApi().createGroup(groupSpec);
            parentId = newGroup.getId();
        }
        assertNotNull(parentId);

        //创建用户
        User newUser;
        Optional<User> userOpt = admin_gitLabApi.getUserApi().getOptionalUser(test_user_name);
        if (userOpt.isPresent()) {
            newUser = userOpt.get();
            System.out.println("----Exist the user: " + newUser.getUsername());
        } else {
            User user = new User()
                    .withName(test_user_name)
                    .withEmail(test_user_name + "@126.com")
                    .withUsername(test_user_name)
                    .withProjectsLimit(20)
                    .withState("active")
                    .withIsAdmin(false)
                    .withSkipConfirmation(true)
                    .withCanCreateGroup(true)
                    .withCanCreateProject(true);
            newUser = admin_gitLabApi.getUserApi().createUser(user, test_password, false);
        }
        assertNotNull(newUser);

        //创建用户token
        String tokenName = "tenant-token";
        Date expiredDate = Date.from(Instant.now().plus(10, ChronoUnit.DAYS));
        ImpersonationToken.Scope[] scopes = new ImpersonationToken.Scope[]{
                ImpersonationToken.Scope.API,
                ImpersonationToken.Scope.READ_USER,
                ImpersonationToken.Scope.READ_REPOSITORY,
                ImpersonationToken.Scope.WRITE_REPOSITORY,
                //ImpersonationToken.Scope.SUDO   //超级用户权限，除Owner外不允许授予此权限
        };
        ImpersonationToken userToken = admin_gitLabApi.getUserApi().createImpersonationToken(newUser.getId(), tokenName, expiredDate, scopes);
        assertNotNull(userToken);

        //创建gitlab分组
        Group newGroup;
        Optional<Group> groupOpt =admin_gitLabApi.getGroupApi().getGroupsStream()
                .filter(m -> m.getPath().equalsIgnoreCase(test_group_name))
                .findFirst();
        if (groupOpt.isPresent()) {
            newGroup = groupOpt.get();
            System.out.println("----Exist the group: " + newGroup.getName());
        } else {
            GroupParams groupSpec = new GroupParams()
                    .withName(test_group_name)
                    .withPath(test_group_name)
                    .withParentId(parentId)
                    .withProjectCreationLevel(GroupParams.ProjectCreationLevel.MAINTAINER);
            newGroup = admin_gitLabApi.getGroupApi().createGroup(groupSpec);
            //将租户管理员用户加入gitlab的分组中
            admin_gitLabApi.getGroupApi().addMember(newGroup, newUser.getId(), AccessLevel.OWNER);
        }
        assertNotNull(newGroup);

        //使用新用户Token创建分组及项目
        GitLabApi tenant_gitLabApi = new GitLabApi(gitLabUrl, userToken.getToken());
        tenant_gitLabApi.setIgnoreCertificateErrors(true);

        //创建gitlab项目
        Project newProject;
        String tenantProjectNamespace = tenant_group_name + "/" + test_group_name;
        Optional<Project> projectOpt = tenant_gitLabApi.getProjectApi().getOptionalProject(tenantProjectNamespace, test_project_name);
        if (projectOpt.isPresent()) {
            newProject = projectOpt.get();
            System.out.println("----Exist the project: " + newProject.getName());
        } else {
            Project projectSpec = new Project()
                    .withNamespaceId(newGroup.getId())
                    .withName(test_project_name)
                    .withDefaultBranch(branch_main)
                    .withPublic(false);
            newProject = tenant_gitLabApi.getProjectApi().createProject(projectSpec);
            //将租户管理员用户加入gitlab的分组中
            //admin_gitLabApi.getProjectApi().addMember(newProject, newUser.getId(), AccessLevel.MAINTAINER);
        }
        assertNotNull(newProject);
        System.out.println("---project path: " + newProject.getPath() + "-namespace: " + newProject.getNamespace());

        //创建gitlab项目分支
        Branch newBranch;
        Optional<Branch> branchOpt = tenant_gitLabApi.getRepositoryApi().getOptionalBranch(newProject, branch_main);
        if (branchOpt.isPresent()) {
            newBranch = branchOpt.get();
            System.out.println("----Exist the branch: " + newBranch.getName());
        } else {
            newBranch = tenant_gitLabApi.getRepositoryApi().createBranch(newProject, branch_main, branch_main);
        }
        assertNotNull(newBranch);
        System.out.println("----branch name: " + newBranch.getName() + "  default: " + newBranch.getDefault());

        List<Project> projects = tenant_gitLabApi.getProjectApi().getProjects();
        assertTrue(projects.size() > 0);
        System.out.println("----Success to get projects by tenant user token.");

        //tenant_gitLabApi.getProjectApi().deleteProject(newProject.getId());
        //tenant_gitLabApi.getGroupApi().deleteGroup(newGroup.getId());

        //使用管理员账号删除用户
        //admin_gitLabApi.getUserApi().deleteUser(newUser.getId(), true);
        System.out.println("----Success to create & delete the group & project & user.");

    }

    @Test
    @Order(5)
    @DisplayName("获取项目目录结构")
    void test_get_project_dict() throws GitLabApiException {
        GitLabApi gitLabApi = Gitlab4jUtil.getGitlabApi(gitLabUrl, admin_user_name, admin_password);
        String codeUrl = "http://gitlab.kcloudy.com/demo/net/kcloudy.demotest.net.git";
        Optional<Project> optionalProject = gitLabApi.getProjectApi().getProjectsStream()
                .filter(m -> m.getHttpUrlToRepo().equalsIgnoreCase(codeUrl))
                .findFirst();
        Project project = optionalProject.get();

        List<GitFileTreeDTO> root = Gitlab4jUtil.getAllFiles(gitLabApi, project.getId(), null, 1, "/", "main");
        assertNotNull(root);
        printTreeDtoList(root);
    }

    /**
     * 打印树结构
     *
     * @param nodes 树结构的列表对象
     */
    private void printTreeDtoList(List<GitFileTreeDTO> nodes) {
        for (GitFileTreeDTO node : nodes) {
            printNestTree(node, 1);
        }
    }

    private void printNestTree(GitFileTreeDTO node, int level) {
        StringBuilder preStr = new StringBuilder();
        for (int i = 0; i < level; i++) {
            preStr.append("|----");
        }
        System.out.println(preStr.toString() + node.getLevel() + ": " + node.getName() + "-" + node.getIndex());
        for (GitFileTreeDTO children : node.getChildren()) {
            printNestTree(children, level + 1);
        }
    }
}
