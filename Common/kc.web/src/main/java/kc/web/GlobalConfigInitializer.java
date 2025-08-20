package kc.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import kc.framework.GlobalConfig;
import kc.framework.GlobalConfigData;
import kc.framework.tenant.TenantConstant;
import kc.framework.tenant.TenantContext;
import kc.framework.util.PrintLogUtil;
import kc.service.webapiservice.IAccountApiService;
import kc.service.webapiservice.thridparty.IGlobalConfigApiService;
import kc.web.annotation.PermissionData;

@Component
public class GlobalConfigInitializer implements ApplicationListener<ContextRefreshedEvent> {
	private static IGlobalConfigApiService globalConfigApiService;
	private static IAccountApiService accountApiService;

	@Value("${spring.application.controller:kc.web.controller}")
	private String packageName;

	@Autowired
	private Environment env;

	@Autowired
	public void setGlobalConfigApiService(IGlobalConfigApiService globalConfigApiService) {
		GlobalConfigInitializer.globalConfigApiService = globalConfigApiService;
	}

	@Autowired
	public void setAccountApiService(IAccountApiService accountApiService) {
		GlobalConfigInitializer.accountApiService = accountApiService;
	}

	@Override
	public void onApplicationEvent(@NonNull
	ContextRefreshedEvent event) {
		// 确保只初始化一次
		if (event.getApplicationContext().getParent() == null) {
			initWebAfterStarted(packageName, env);
		}
	}

	public static void initWebAfterStarted(String packageName, Environment env) {
		GlobalConfig.InitGlobalConfig(env);
		GlobalConfigData configData = globalConfigApiService.GetGlobalConfigData();
		GlobalConfig.InitGlobalConfigWithApiData(env, configData);

		kc.web.annotation.AnnotationUtil.initAnnotationDataByPackageName(packageName);

		// 设置租户cDba为当前租户，并保存应用的所有菜单及权限至租户cDba
		TenantContext.setCurrentTenant(TenantConstant.DbaTenantApiAccessInfo);
		Boolean isMenusSaveSuccess = accountApiService.SaveMenusAsync(kc.web.annotation.MenuData.AllMenus,
				GlobalConfig.ApplicationGuid);
		Boolean isPermissionsSaveSuccess = accountApiService.SavePermissionsAsync(PermissionData.AllPermissions,
				GlobalConfig.ApplicationGuid);

		PrintLogUtil.printInfo(String.format(
				"GlobalConfig SystemType: %s; EncryptKey: %s; SSO Domain: %s Current Domain: %s; upgrade MenuData is success? %s; upgrade PermissionData is success? %s",
				GlobalConfig.SystemType, GlobalConfig.EncryptKey, GlobalConfig.SSOWebDomain,
				GlobalConfig.CurrentApplication != null ? GlobalConfig.CurrentApplication.getAppDomain() : "null",
				isMenusSaveSuccess, isPermissionsSaveSuccess));
	}

}
