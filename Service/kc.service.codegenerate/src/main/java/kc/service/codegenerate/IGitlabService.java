package kc.service.codegenerate;

import kc.service.base.IServiceBase;

import java.io.File;
import java.io.IOException;

public interface IGitlabService extends IServiceBase {
    /**
     * 创建租户管理员账号及租户分组，并返回管理员账号的token
     *
     * @param tenantName    租户编码
     * @param adminPassword 租户管理员密码
     * @return 返回租户管理员的token
     */
    String createTenantGitlabGroupAndUser(String tenantName, String adminPassword);

    /**
     * 在Gitlab中创建应用的项目
     *
     * @param appCode 应用编码
     * @return 是否成功
     */
    boolean createAppGitProject(String appCode);

    /**
     * 在Gitlab中删除应用的项目
     *
     * @param appCode 应用编码
     * @return 是否成功
     */
    boolean deleteAppGitProject(String appCode);

    /**
     * 在本地初始化Gitlab的项目
     *
     * @param appCode 应用编码
     * @return 是否成功
     */
    boolean cloneAppGitProject(String appCode);
}
