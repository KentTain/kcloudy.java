package kc.web;

import kc.framework.GlobalConfig;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.Tenant;
import kc.framework.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GlobalViewModelInterceptor implements HandlerInterceptor {  //extends HandlerInterceptorAdapter
    @Autowired
    public Environment Env;
    @Autowired
    private kc.framework.tenant.ITenantResolver tenantResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //GlobalConfig.InitGlobalConfig(Env);

        if (TenantContext.getCurrentTenant() == null) {
            String url = request.getRequestURL().toString();
            Tenant tenant = tenantResolver.Resolve(url);
            if (tenant == null)
                throw new ServletException(String.format("未找到相关租户信息，租户访问域名为：%s", url));

            TenantContext.setCurrentTenant(tenant);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Tenant tenant = TenantContext.getCurrentTenant();
        if (tenant != null
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("TenantName")) {
            modelAndView.addObject("tenantName", tenant.getTenantName());
        }
        if (!StringExtensions.isNullOrEmpty(GlobalConfig.ResWebDomain)
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("resWebDomain"))
            modelAndView.addObject("resWebDomain", GlobalConfig.ResWebDomain);
        if (tenant != null && !StringExtensions.isNullOrEmpty(GlobalConfig.DocWebDomain)
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("docWebApiDomain")) {
            String tenantDocApi = GlobalConfig.GetTenantWebApiDomain(GlobalConfig.DocWebDomain, tenant.getTenantName());
            modelAndView.addObject("docWebApiDomain", tenantDocApi);
        }
        if (!StringExtensions.isNullOrEmpty(GlobalConfig.ApplicationId)
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("appId"))
            modelAndView.addObject("appId", GlobalConfig.ApplicationId);
        if (!StringExtensions.isNullOrEmpty(GlobalConfig.CurrentApplication.getAppName())
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("appName"))
            modelAndView.addObject("appName", GlobalConfig.CurrentApplication.getAppName());
        if (GlobalConfig.UploadConfig != null
                && modelAndView != null && modelAndView.getModelMap() != null
                && !modelAndView.getModelMap().containsKey("uploadConfig"))
            modelAndView.addObject("uploadConfig", GlobalConfig.UploadConfig);

        //super.postHandle(request, response, handler, modelAndView);
    }
}
