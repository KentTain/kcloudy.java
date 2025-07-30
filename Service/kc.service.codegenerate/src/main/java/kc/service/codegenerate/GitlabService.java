package kc.service.codegenerate;

import kc.framework.GlobalConfig;
import kc.framework.exceptions.ComponentException;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.framework.tenant.Tenant;
import kc.service.base.ServiceBase;
import kc.service.util.Gitlab4jUtil;
import kc.service.util.JGitUtil;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.transport.*;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class GitlabService extends ServiceBase implements IGitlabService {
    public static final String GIT_EXT = ".git";
    public static final String DEFAULT_BRANCH = "main";
    public static final String TENANT_GROUP_NAME = "tenant";

    public GitlabService(IGlobalConfigApiService globalConfigApiService) {
        super(globalConfigApiService);
    }

    /**
     * 获取配置的Gitlab域名，例如：http://gitlab.kcloudy.com
     *
     * @return Gitlab的域名
     */
    private String getGitlabHostUrl() {
        String codeConnectString = GlobalConfig.GetDecryptCodeConnectionString();
        Assert.hasLength(codeConnectString, "未配置平台租户管理员的Gitlab相关连接字符串.");
        return StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.CodeEndpoint);
    }

    /**
     * 获取项目在gitlab中的地址，例如：http://gitlab.kcloudy.com/tenant/cTest/cTest-appCode.git
     *
     * @param appCode 应用编码
     * @return 项目在gitlab中的地址
     */
    private String getTenantProjectGitlabUrl(String appCode) {
        String gitlabHostUrl = getGitlabHostUrl();
        return String.format("%s/%s/%s/%s%s",
                StringExtensions.trimEndSlash(gitlabHostUrl),
                TENANT_GROUP_NAME,
                getCurrentTenantName(),
                getCurrentTenantName() + "-" + appCode, GIT_EXT);
    }

    /**
     * 获取项目在本地克隆的地址，例如：/temp/cTest-appCode
     *
     * @param appCode 应用编码
     * @return 项目在本地克隆的地址
     */
    private String getTenantProjectLocalPath(String appCode) {
        String tempPath = GlobalConfig.TempFilePath;
        return String.format("%s/%s", StringExtensions.trimEndSlash(tempPath), getCurrentTenantName() + "-" + appCode);
    }

    /**
     * 获取访问Gitlab api的客户端
     *
     * @return Gitlab api的客户端
     */
    private GitLabApi getAdminGitlabClient() {
        String codeConnectString = GlobalConfig.GetDecryptCodeConnectionString();
        Assert.hasLength(codeConnectString, "未配置平台租户管理员的Gitlab相关连接字符串.");

        String gitHostUrl = StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.CodeEndpoint);
        String admin_token = StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.AccessKey);

        GitLabApi result = Gitlab4jUtil.getGitlabApi(gitHostUrl, admin_token);
        result.setIgnoreCertificateErrors(true);
        return result;
    }

    /**
     * 获取租户设置的访问Gitlab api的客户端
     *
     * @return Gitlab api的客户端
     */
    private GitLabApi getTenantGitlabClient() {
        Tenant tenant = super.getCurrentTenant();
        Assert.notNull(tenant, "当前Gitlab服务的租户为空.");

        String codeConnectString = tenant.GetDecryptCodeConnectionString();
        Assert.hasLength(codeConnectString, "该用户未配置Gitlab相关的连接字符串.");

        String gitHostUrl = StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.CodeEndpoint);
        String tenant_token = StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.AccessKey);

        GitLabApi result = Gitlab4jUtil.getGitlabApi(gitHostUrl, tenant_token);
        result.setIgnoreCertificateErrors(true);
        return result;
    }

    /**
     * 获取租户的Git身份认证信息
     *
     * @return 租户的Git身份认证信息
     */
    private CredentialsProvider getTenantGitCredentialsProvider() {
        Tenant tenant = super.getCurrentTenant();
        Assert.notNull(tenant, "当前Gitlab服务的租户为空.");

        String codeConnectString = tenant.GetDecryptCodeConnectionString();
        Assert.hasLength(codeConnectString, "该用户未配置Gitlab相关的连接字符串.");

        String tenant_token = StringExtensions.getValueFromConnectionString(codeConnectString, ConnectionKeyConstant.AccessKey);
        return JGitUtil.getGitCredentials(tenant_token);
    }

    @Override
    public String createTenantGitlabGroupAndUser(String tenantName, String adminPassword) {
        final String tenant_group_name = tenantName;
        final String tenant_tokenName = tenantName + "-token";
        final Date token_expiredDate = Date.from(Instant.now().plus(365 * 3, ChronoUnit.DAYS));
        GitLabApi gitlabAdminApi = getAdminGitlabClient();

        try {
            //获取租户分组
            Integer parentId;
            Optional<Group> optGroup = gitlabAdminApi.getGroupApi().getGroupsStream()
                    .filter(m -> m.getPath().equalsIgnoreCase(TENANT_GROUP_NAME))
                    .findFirst();
            if (optGroup.isPresent()) {
                parentId = optGroup.get().getId();
            } else {
                GroupParams groupSpec = new GroupParams()
                        .withName(TENANT_GROUP_NAME)
                        .withPath(TENANT_GROUP_NAME)
                        .withProjectCreationLevel(GroupParams.ProjectCreationLevel.DEVELOPER);
                Group adminGroup = gitlabAdminApi.getGroupApi().createGroup(groupSpec);
                parentId = adminGroup.getId();
                log.info("----Create a admin group: " + adminGroup.getName());
            }

            //创建租户Gitlab管理员
            User tenantAdminUser;
            Optional<User> userOpt = gitlabAdminApi.getUserApi().getOptionalUser(tenantName);
            if (userOpt.isPresent()) {
                tenantAdminUser = userOpt.get();
            } else {
                User userRequest = new User()
                        .withName(tenantName)
                        .withEmail(tenantName + "@126.com")
                        .withUsername(tenantName)
                        .withProjectsLimit(20)
                        .withState("active")
                        .withIsAdmin(false)
                        .withSkipConfirmation(true)
                        .withCanCreateGroup(true)
                        .withCanCreateProject(true);
                tenantAdminUser = gitlabAdminApi.getUserApi().createUser(userRequest, adminPassword, false);
                log.info("----1. 租户【{}】 创建Gitlab的管理员账号: {}", tenantName, tenantAdminUser.getName());
            }

            //创建用户token
            String token = null;
            List<ImpersonationToken> userTokens = gitlabAdminApi.getUserApi().getImpersonationTokens(tenantAdminUser, Constants.ImpersonationState.ACTIVE);
            if (null == userTokens || userTokens.stream().noneMatch(m -> m.getName().equalsIgnoreCase(tenant_tokenName) && !m.getRevoked())) {
                ImpersonationToken.Scope[] scopes = new ImpersonationToken.Scope[]{
                        ImpersonationToken.Scope.API,
                        ImpersonationToken.Scope.READ_USER,
                        ImpersonationToken.Scope.READ_REPOSITORY,
                        ImpersonationToken.Scope.WRITE_REPOSITORY,
                        //ImpersonationToken.Scope.SUDO   //超级用户权限，除Owner外不允许授予此权限
                };
                ImpersonationToken userToken = gitlabAdminApi.getUserApi().createImpersonationToken(
                        tenantAdminUser.getId(), tenant_tokenName, token_expiredDate, scopes);
                token = userToken.getToken();
                log.info("----2. 租户【{}】 创建Gitlab租户管理员Token: {}", tenantName, token);
            }

            //创建gitlab分组
            Group tenantGroup;
            Optional<Group> groupOpt = gitlabAdminApi.getGroupApi().getGroupsStream()
                    .filter(m -> m.getPath().equalsIgnoreCase(tenant_group_name))
                    .findFirst();
            if (!groupOpt.isPresent()) {
                GroupParams groupSpec = new GroupParams()
                        .withName(tenant_group_name)
                        .withPath(tenant_group_name)
                        .withParentId(parentId)
                        .withProjectCreationLevel(GroupParams.ProjectCreationLevel.MAINTAINER);
                tenantGroup = gitlabAdminApi.getGroupApi().createGroup(groupSpec);
                //将租户管理员用户加入gitlab的分组中
                gitlabAdminApi.getGroupApi().addMember(tenantGroup, tenantAdminUser.getId(), AccessLevel.OWNER);
                log.info("----3. 租户【{}】 创建Gitlab的租户分组: {}", tenantName, tenantGroup.getFullName());
            }

            return token;
        } catch (GitLabApiException ex) {
            log.error("创建租户管理员账号及租户分组失败", ex);
            return null;
        }
    }

    @Override
    public boolean createAppGitProject(String appCode) {
        final String tenant_group_name = super.getCurrentTenantName();
        final String tenant_project_name = super.getCurrentTenantName() + "-" + appCode;

        try {
            //使用新用户Token创建分组及项目
            GitLabApi tenant_gitLabApi = getTenantGitlabClient();
            tenant_gitLabApi.setIgnoreCertificateErrors(true);

            //判断Gitlab下的租户分组是否存在
            String tenantGroupPath = TENANT_GROUP_NAME + "/" + tenant_group_name;
            Optional<Group> groupOpt = tenant_gitLabApi.getGroupApi().getGroupsStream()
                    .filter(m -> m.getPath().equalsIgnoreCase(tenant_group_name))
                    .findFirst();
            if (!groupOpt.isPresent()) {
                throw new ComponentException(String.format("未找到租户的Gitlab相关分组【{%s}】的值", tenantGroupPath));
            }
            Group tenantGroup = groupOpt.get();

            //创建gitlab项目
            Project newProject;
            Optional<Project> projectOpt = tenant_gitLabApi.getProjectApi().getOptionalProject(tenantGroupPath, tenant_project_name);
            if (projectOpt.isPresent()) {
                newProject = projectOpt.get();
                System.out.println("----Exist the project: " + newProject.getName());
            } else {
                Project projectSpec = new Project()
                        .withNamespaceId(tenantGroup.getId())
                        .withName(tenant_project_name)
                        .withDefaultBranch(DEFAULT_BRANCH)
                        .withPublic(false);
                newProject = tenant_gitLabApi.getProjectApi().createProject(projectSpec);
                //将租户管理员用户加入gitlab的分组中
                //gitlabAdminApi.getProjectApi().addMember(newProject, tenantAdminUser.getId(), AccessLevel.MAINTAINER);
            }
            System.out.println("----project path: " + newProject.getPath() + "-namespace: " + newProject.getNamespace());

            return true;
        } catch (GitLabApiException ex) {
            throw new ComponentException(ex);
        }
    }

    @Override
    public boolean deleteAppGitProject(String appCode) {
        final String tenant_project_name = getCurrentTenantName() + "-" + appCode;
        final String tenantProjectNamespace = TENANT_GROUP_NAME + "/" + getCurrentTenantName();

        try {
            GitLabApi gitlabAdminApi = getAdminGitlabClient();
            Optional<Project> projectOpt = gitlabAdminApi.getProjectApi().getOptionalProject(tenantProjectNamespace, tenant_project_name);
            if (!projectOpt.isPresent()) {
                return false;
            }

            Project project = projectOpt.get();
            gitlabAdminApi.getProjectApi().deleteProject(project.getId());
            return true;
        } catch (GitLabApiException ex) {
            throw new ComponentException(ex);
        }
    }

    @Override
    public boolean cloneAppGitProject(String appCode) {
        String gitRemoteUrl = getTenantProjectGitlabUrl(appCode);
        String gitLocalPath = getTenantProjectLocalPath(appCode);
        Assert.hasLength(gitRemoteUrl, "该应用未设置Gitlab的远程访问地址.");
        Assert.hasLength(gitLocalPath, "该应用未设置Gitlab的本地克隆地址.");

        //设置本地Git文件目录
        File basedir = new File(gitLocalPath);

        //获取Git的身份认证
        CredentialsProvider gitCredentials = getTenantGitCredentialsProvider();

        //删除项目在本地已有的git相关文件
        JGitUtil.deleteDirIfExists(basedir);

        //将gitlab项目克隆到本地
        JGitUtil.cloneRepository(gitCredentials, gitRemoteUrl, gitLocalPath, DEFAULT_BRANCH);

        log.info("=======Initialize the git service with remote url: {}  local path: {}", gitRemoteUrl, gitLocalPath);
        return true;
    }

}
