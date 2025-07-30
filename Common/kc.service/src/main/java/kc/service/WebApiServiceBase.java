package kc.service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import kc.framework.tenant.Tenant;
import kc.service.constants.OpenIdConnectConstants;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.fasterxml.jackson.core.type.TypeReference;

import kc.framework.GlobalConfig;
import kc.framework.util.SerializeHelper;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.TenantConstant;
import kc.service.base.IdSrvAccessTokenResult;
import kc.service.base.ErrorInfoResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@lombok.extern.slf4j.Slf4j
public abstract class WebApiServiceBase {

    @Value("${GlobalConfig.SSOWebDomain}")
    protected String ssoWebDomain;

    @Value("${GlobalConfig.ResWebDomain}")
    protected String resWebDomain;

    protected WebApiServiceBase() {
    }

    /**
     * 租户信息Api接口地址：http://admin.kcloudy.com/api/  <br/>
     * 本地测试接口地址：http://localhost:1009/api/
     *
     * @return
     */
    protected String AdminApiServiceUrl() {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.AdminWebDomain))
            return null;

        return GlobalConfig.AdminWebDomain
                .replace("admin.", "adminapi.")
                .replace(":1003", ":1004")
                .replace(":1013", ":1014") + "api/";
    }

    /**
     * 租户用户接口地址：http://ctest.sso.kcloudy.com/ <br/>
     * 本地测试接口地址：http://ctest.localhost:1001/
     *
     * @return
     */
    protected String SSOServerUrl() {
        return getTenantSSOServerUrl(getOAuth2ClientInfo().getTenantName());
    }


    /**
     * 租户SSO接口地址：http://ctest.ssoapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://ctest.localhost:1009/api/
     *
     * @return
     */
    protected String SsoApiServerUrl() {
        return SsoApiUrl();
    }

    /**
     * SSO登录接口：http://ssoapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:1001/api/
     *
     * @return
     */
    protected String SsoApiUrl() {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.SSOWebDomain))
            return null;

        return GlobalConfig.SSOWebDomain
                .replace("sso.", "ssoapi.")
                .replace(":1001", ":1002")
                .replace(":1011", ":1012") + "api/";
    }

    /**
     * subdomain的接口地址：http://(tenantName).sso.kcloudy.com/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:1001/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String getTenantSSOServerUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.SSOWebDomain))
            return null;

        if (GlobalConfig.SSOWebDomain.startsWith("http://"))
            return GlobalConfig.SSOWebDomain.replace("http://", "http://" + tenantName + ".");

        return GlobalConfig.SSOWebDomain.replace("https://", "https://" + tenantName + ".");
    }

    /**
     * 获取租户信息接口地址：http://blogapi.kcloudy.com/
     *   本地测试接口地址：http://localhost:1006/api/
     * @return
     */
    protected String BlogApiUrl() {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.BlogWebDomain))
            return null;

        return GlobalConfig.BlogWebDomain
                .replace("blog.", "blogapi.")
                .replace(":1005", ":1006")
                .replace(":1015", ":1016") + "api/";
    }

    /**
     * 获取配置信息接口地址：http://(tenantName).cfgapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:1102/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetConfigApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.CfgWebDomain))
            return null;

        return GlobalConfig.CfgWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("cfg.", "cfgapi.")
                .replace(":1101", ":1102")
                .replace(":1111", ":1112") + "api/";
    }

    /**
     * 获取配置信息接口地址：http://ctest.cfgapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:1102/api/
     *
     * @return
     */
    protected String ConfigApiUrl() {
        return GetConfigApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取字典信息接口地址：http://(tenantName).dicapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:1104/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetDictionaryApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.DicWebDomain))
            return null;

        return GlobalConfig.DicWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("dic.", "dicapi.")
                .replace(":1103", ":1104")
                .replace(":1113", ":1114") + "api/";
    }

    /**
     * 获取配置信息接口地址：http://ctest.dicapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:1104/api/
     *
     * @return
     */
    protected String DictionaryApiUrl() {
        return GetDictionaryApiUrl(getOAuth2ClientInfo().getTenantName());
    }


    /**
     * 获取应用信息接口地址：http://(tenantName).appapi.kcloudy.com/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:1106/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetAppApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.AppWebDomain))
            return null;

        return GlobalConfig.AppWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("app.", "appapi.")
                .replace(":1105", ":1106")
                .replace(":1115", ":1116") + "api/";
    }

    /**
     * 获取配置信息接口地址：http://ctest.appapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:1106/api/
     *
     * @return
     */
    protected String ApplicationApiUrl() {
        return GetAppApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取文档接口地址：http://(tenantName).msgapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:2000/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetMessageApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.MsgWebDomain))
            return null;

        return GlobalConfig.MsgWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("msg.", "msgapi.")
                .replace(":1109", ":2000")
                .replace(":1119", ":2010") + "api/";
    }

    /**
     * 获取配置信息接口地址：http://ctest.cfgapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:1102/api/
     *
     * @return
     */
    protected String MessageApiUrl() {
        return GetMessageApiUrl(getOAuth2ClientInfo().getTenantName());
    }


    /**
     * 获取账户信息接口地址：http://(tenantName).accapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:2002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetAccountApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.AccWebDomain))
            return null;

        return GlobalConfig.AccWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("acc.", "accapi.")
                .replace(":2001", ":2002")
                .replace(":2011", ":2012") + "api/";
    }

    /**
     * 租户用户接口地址：http://ctest.accapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:2002/api/
     *
     * @return
     */
    protected String AccountApiUrl() {
        return GetAccountApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取电子合同接口地址：http://(tenantName).econapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:2004/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetEContractApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.EconWebDomain))
            return null;

        return GlobalConfig.EconWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("econ.", "econapi.")
                .replace(":2003", ":2004")
                .replace(":2013", ":2014") + "api/";
    }

    /**
     * 获取文档接口地址：http://(tenantName).docapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:2006/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetDocumentApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.DocWebDomain))
            return null;

        return GlobalConfig.DocWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("doc.", "docapi.")
                .replace(":2005", ":2006")
                .replace(":2015", ":2016") + "api/";
    }

    /**
     * 租户用户接口地址：http://ctest.docapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:2006/api/
     *
     * @return
     */
    protected String DocumentApiUrl() {
        return GetDocumentApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取文档接口地址：http://(tenantName).hrapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:2008/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetHumanResourceApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.HrWebDomain))
            return null;

        return GlobalConfig.HrWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("hr.", "hrapi.")
                .replace(":2007", ":2008")
                .replace(":2017", ":2018") + "api/";
    }


    /**
     * 获取客户信息接口地址：http://(tenantName).crmapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:3002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetCrmApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.CrmWebDomain))
            return null;

        return GlobalConfig.CrmWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("crm.", "crmapi.")
                .replace(":3011", ":3012")
                .replace(":3001", ":3002") + "api/";
    }

    /**
     * 租户CRM接口地址：http://ctest.crmapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://ctest.localhost:3002/api/
     *
     * @return
     */
    protected String CrmApiServerUrl() {
        return GetCrmApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取供应商信息接口地址：http://(tenantName).srmapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:3004/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetSrmApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.SrmWebDomain))
            return null;

        return GlobalConfig.SrmWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("srm.", "srmapi.")
                .replace(":3003", ":3004")
                .replace(":3013", ":3014") + "api/";
    }

    /**
     * 获取商品信息接口地址：http://(tenantName).prdapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:3006/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetPrdApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.PrdWebDomain))
            return null;

        return GlobalConfig.PrdWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("prd.", "prdapi.")
                .replace(":3005", ":3006")
                .replace(":3015", ":3016") + "api/";
    }

    /**
     * 获取物料信息接口地址：http://(tenantName).Pmcapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:3008/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetPmcApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.PmcWebDomain))
            return null;

        return GlobalConfig.PmcWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("pmc.", "pmcapi.")
                .replace(":3007", ":3008")
                .replace(":3017", ":3018") + "api/";
    }


    /**
     * 获取采购订单接口地址：http://(tenantName).somapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:4004/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetSomApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.SomWebDomain))
            return null;

        return GlobalConfig.SomWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("som.", "somapi.")
                .replace(":4003", ":4004")
                .replace(":4013", ":4014") + "api/";
    }

    /**
     * 获取销售订单接口地址：http://(tenantName).pomapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:4006/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetPomApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.PomWebDomain))
            return null;

        return GlobalConfig.PomWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("pom.", "pomapi.")
                .replace(":4005", ":4006")
                .replace(":4015", ":4016") + "api/";
    }

    /**
     * 获取仓储信息接口地址：http://(tenantName).wmsapi.cfwin.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:4008/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetWmsApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.WmsWebDomain))
            return null;

        return GlobalConfig.WmsWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("wms.", "wmsapi.")
                .replace(":4007", ":4008")
                .replace(":4017", ":4018") + "api/";
    }

    /**
     * 获取登录用户接口地址：http://(tenantName).portalapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:4002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetPortalApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.PortalWebDomain))
            return null;

        return GlobalConfig.PortalWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("shop.", "shopapi.")
                .replace(":4011", ":4012")
                .replace(":4001", ":4002") + "api/";
    }

    /**
     * 租户用户接口地址：http://ctest.portalapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:4002/api/
     *
     * @return
     */
    protected String PortalApiUrl() {
        return GetPortalApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 租户融资接口地址：http://ctest.marketapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://localhost:6002/api/
     *
     * @return
     */
    protected String MarketApiUrl() {
        return GetMarketApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 租户融资Subdomin接口地址：http://subdomin.marketapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://subdomin.localhost:5002/api/
     *
     * @return
     */
    protected String MarketSubdomainApiServerUrl() {
        return GetMarketApiUrl(TenantConstant.SubDomain);
    }

    /**
     * 获取登录用户接口地址：http://(tenantName).jrapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:5002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetMarketApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.JRWebDomain))
            return null;

        return GlobalConfig.JRWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("jr.", "jrapi.")
                .replace(":5001", ":5002")
                .replace(":5011", ":5012") + "api/";
    }

    /**
     * 获取登录用户接口地址：http://(tenantName).flowapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:7002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetFlowApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.WorkflowWebDomain))
            return null;

        return GlobalConfig.WorkflowWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("flow.", "flowapi.")
                .replace(":7001", ":7002")
                .replace(":7011", ":7012") + "api/";
    }

    /**
     * 租户WorkFlow接口地址：http://ctest.flowapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://ctest.localhost:7002/api/
     *
     * @return
     */
    protected String WorkflowApiServerUrl() {
        return GetFlowApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取登录用户接口地址：http://(tenantName).payapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:8002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetPaymentApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.PayWebDomain))
            return null;

        return GlobalConfig.PayWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("pay.", "payapi.")
                .replace(":8001", ":8002")
                .replace(":8011", ":8012") + "api/";
    }

    /**
     * 租户Payment接口地址：http://ctest.Paymentapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://ctest.localhost:8002/api/
     *
     * @return
     */
    protected String PaymentApiUrl() {
        return GetPaymentApiUrl(getOAuth2ClientInfo().getTenantName());
    }

    /**
     * 获取登录用户接口地址：http://(tenantName).wxapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://(tenantName).localhost:9002/api/
     *
     * @param tenantName 租户代码
     * @return
     */
    protected String GetWeixinApiUrl(String tenantName) {
        if (StringExtensions.isNullOrEmpty(GlobalConfig.WXWebDomain))
            return null;

        return GlobalConfig.WXWebDomain
                .replace(TenantConstant.SubDomain, tenantName)
                .replace("wx.", "wxapi.")
                .replace(":9001", ":9002")
                .replace(":9011", ":9012") + "api/";
    }

    /**
     * 租户Weixin接口地址：http://ctest.wxapi.kcloudy.com/api/ <br/>
     * 本地测试接口地址：http://ctest.localhost:9002/api/
     *
     * @return
     */
    protected String WeixinApiUrl() {
        return GetWeixinApiUrl(getOAuth2ClientInfo().getTenantName());
    }


    protected abstract OAuth2ClientInfo getOAuth2ClientInfo();

    protected OAuth2ClientInfo getTenantOAuth2ClientInfo(Tenant tenant) {
        String tenantName = tenant.getTenantName();
        String clientId = TenantConstant.GetClientIdByTenant(tenant);
        String clientSecret = TenantConstant.GetClientSecretByTenant(tenant);
        String credential = TenantConstant.Sha256(clientSecret);

        String tokenEndpoint = ssoWebDomain + OpenIdConnectConstants.OAuth2_Token_Action;
        //String tokenEndpoint = SSOServerUrl() + OpenIdConnectConstants.OAuth2_Token_Action;
        String grantType = "client_credentials";
        return new OAuth2ClientInfo(tenantName, clientId, clientSecret, credential, tokenEndpoint, grantType);
    }

    /**
     * 使用http请求（Get方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendGet(TypeReference<T> clazz, String serviceName, String url, String scope, Function<T, T> callback,
                               Consumer<ErrorInfoResult> failCallback, boolean needOAuthAuthenticated) {
        return WebSendGet(clazz, serviceName, url, scope, ClientExtensions.DefaultContentType, callback, failCallback,
                needOAuthAuthenticated);
    }

    /**
     * 使用http请求（Get方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param contentType            返回对象的contentType，默认为：application/json
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendGet(TypeReference<T> clazz, String serviceName, String url, String scope, String contentType,
                               Function<T, T> callback, Consumer<ErrorInfoResult> failCallback, boolean needOAuthAuthenticated) {
        return WebSendGet(clazz, serviceName, url, scope, contentType, null, callback, failCallback,
                needOAuthAuthenticated);
    }

    /**
     * 使用http请求（Get方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param contentType            返回对象的contentType，默认为：application/json
     * @param headers                http请求的Headers
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendGet(TypeReference<T> clazz, String serviceName, String url, String scope, String contentType,
                               Map<String, String> headers, Function<T, T> callback, Consumer<ErrorInfoResult> failCallback,
                               boolean needOAuthAuthenticated) {
        log.debug(String.format("开始调用服务[%s]，参数如下：URL=%s", serviceName, url));

        if (StringExtensions.isNullOrEmpty(url) && failCallback != null) {
            failCallback.accept(new ErrorInfoResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    String.format("Service's [%s] url is null or empty. ", serviceName)));
            return null;
        }
        if (needOAuthAuthenticated && StringExtensions.isNullOrEmpty(scope) && failCallback != null) {
            failCallback.accept(new ErrorInfoResult(HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    String.format("Service's [%s] scope is null or empty. ", serviceName)));
            return null;
        }

        if (headers == null) {
            headers = new HashMap<>();
        }

        if (needOAuthAuthenticated) {
            IdSrvAccessTokenResult accessToken = accessToken(scope);
            if (null == accessToken) {
                failCallback.accept(new ErrorInfoResult(HttpStatus.SC_BAD_REQUEST,
                        String.format("[%s] AccessToken is null or empty. ", serviceName)));
                return null;
            }
            //System.out.println(accessToken.getAccessToken());
            headers.put("Authorization", "Bearer " + accessToken.getAccessToken());
        }

        try (CloseableHttpResponse response = ClientExtensions.doGet(false, url, ClientExtensions.DefaultContentType,
                headers)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK) {
                String errorString = ClientExtensions.toString(response);
                if (failCallback != null)
                    failCallback.accept(new ErrorInfoResult(status.getStatusCode(), errorString));

                String error = String.format("调用服务[%s]出现异常，参数如下：URL=%s，错误消息: %s", serviceName, url,
                        errorString);
                log.error(error);
                return null;
            }
            // 内容
            String responseString = ClientExtensions.toString(response);
            if (callback != null && !StringExtensions.isNullOrEmpty(responseString)) {
                TypeReference<String> strType = new TypeReference<String>() {
                };
                if (strType.getType() == clazz.getType()) {
                    @SuppressWarnings("unchecked")
                    T result = (T) responseString;
                    return callback.apply(result);
                }

                T result = SerializeHelper.FromJson(responseString, clazz);
                return callback.apply(result);
            }

            return null;
        } catch (Exception ex) {
            String error = String.format("调用服务[%s]出现异常，参数如下：URL=%s，错误消息: %s，详细信息: %s", serviceName, url,
                    ex.getMessage(), ex.getStackTrace());
            log.error(error);
            if (failCallback != null)
                failCallback.accept(new ErrorInfoResult(HttpStatus.SC_INTERNAL_SERVER_ERROR, error));
            return null;
        } finally {
        }
    }

    /**
     * 使用http请求（Post方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param postJsonData           需要Post的对象JSon字符串
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendPost(TypeReference<T> clazz, String serviceName, String url, String scope, String postJsonData,
                                Function<T, T> callback, Consumer<ErrorInfoResult> failCallback, boolean needOAuthAuthenticated) {
        return WebSendPost(clazz, serviceName, url, scope, postJsonData, null, callback, failCallback,
                needOAuthAuthenticated);
    }

    /**
     * 使用http请求（Post方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param postJsonData           需要Post的对象JSon字符串
     * @param headers                返回对象的contentType，默认为：application/json
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendPost(TypeReference<T> clazz, String serviceName, String url, String scope, String postJsonData,
                                Map<String, String> headers, Function<T, T> callback, Consumer<ErrorInfoResult> failCallback,
                                boolean needOAuthAuthenticated) {
        return WebSendPost(clazz, serviceName, url, scope, postJsonData, ClientExtensions.DefaultContentType, headers,
                callback, failCallback, needOAuthAuthenticated);
    }

    /**
     * 使用http请求（Post方式）获取远程服务器端数据
     *
     * @param clazz                  返回对象的TypeReference<T>
     * @param serviceName            调用服务的名称（日志记录用）
     * @param url                    远程服务器地址
     * @param scope                  WebApi的Scope
     * @param postJsonData           需要Post的对象JSon字符串
     * @param contentType            返回对象的contentType，默认为：application/json
     * @param headers                http请求的Headers
     * @param callback               远程服务调用成功后的回调函数
     * @param failCallback           远程服务调用失败后的回调函数
     * @param needOAuthAuthenticated WebApi接口是否被OAuth保护
     * @return T 返回序列化后的调用对象
     */
    protected <T> T WebSendPost(TypeReference<T> clazz, String serviceName, String url, String scope, String postJsonData,
                                String contentType, Map<String, String> headers, Function<T, T> callback,
                                Consumer<ErrorInfoResult> failCallback, boolean needOAuthAuthenticated) {
        log.debug(String.format("开始调用服务[%s]，参数如下：URL=%s、postJsonData=%s", serviceName, url, postJsonData));

        if (StringExtensions.isNullOrEmpty(url) && failCallback != null) {
            failCallback.accept(new ErrorInfoResult(HttpStatus.SC_BAD_REQUEST,
                    String.format("Service's [%s] url is null or empty. ", serviceName)));
            return null;
        }
        if (needOAuthAuthenticated && StringExtensions.isNullOrEmpty(scope) && failCallback != null) {
            failCallback.accept(new ErrorInfoResult(HttpStatus.SC_BAD_REQUEST,
                    String.format("Service's [%s] scope is null or empty. ", serviceName)));
            return null;
        }

        if (headers == null) {
            headers = new HashMap<>();
        }

        if (needOAuthAuthenticated) {
            IdSrvAccessTokenResult accessToken = accessToken(scope);
            if (null == accessToken) {
                failCallback.accept(new ErrorInfoResult(HttpStatus.SC_BAD_REQUEST,
                        String.format("[%s] AccessToken is null or empty. ", serviceName)));
                return null;
            }
            //System.out.println(accessToken.getAccessToken());
            headers.put("Authorization", "Bearer " + accessToken.getAccessToken());
        }

        try (CloseableHttpResponse response = ClientExtensions.doPost(false, url, ClientExtensions.DefaultContentType,
                headers, postJsonData)) {
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK) {
                String errorString = ClientExtensions.toString(response);
                if (failCallback != null)
                    failCallback.accept(new ErrorInfoResult(status.getStatusCode(), errorString));

                String error = String.format("调用服务[%s]出现异常，参数如下：URL=%s，错误消息: %s", serviceName, url,
                        errorString);
                log.error(error);
                return null;
            }
            // 内容
            String responseString = ClientExtensions.toString(response);
            if (callback != null && !StringExtensions.isNullOrEmpty(responseString)) {
                TypeReference<String> strType = new TypeReference<String>() {
                };
                if (strType.getType() == clazz.getType()) {
                    @SuppressWarnings("unchecked")
                    T result = (T) responseString;
                    return callback.apply(result);
                }

                T result = SerializeHelper.FromJson(responseString, clazz);
                return callback.apply(result);
            }

            return null;
        } catch (Exception ex) {
            String error = String.format("调用服务[%s]出现异常，参数如下：URL=%s，错误消息: %s，详细信息: %s", serviceName, url,
                    ex.getMessage(), ex.getStackTrace());
            if (failCallback != null)
                failCallback.accept(new ErrorInfoResult(HttpStatus.SC_BAD_REQUEST, error));

            log.error(error);
            return null;
        }
    }

    /**
     * 获取access_token
     *
     * @param scope
     * @return
     */
    protected IdSrvAccessTokenResult accessToken(String scope) {
        IdSrvAccessTokenResult accessTokenResult = null;

        OAuth2ClientInfo client = getOAuth2ClientInfo();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + client.getClientCredential());
        headers.put("Content-Type", ClientExtensions.FormContentType);

        Map<String, String> postData = new HashMap<>();
        postData.put(TenantConstant.ClaimTypes_TenantName, client.getTenantName());
        postData.put("client_id", client.getClientId());
        postData.put("client_secret", client.getClientSecret());
        postData.put("grant_type", client.getGrantType());
        postData.put("scope", scope);

        CloseableHttpResponse response = ClientExtensions.doPostFormData(client.getTokenEndpoint(), headers, postData);
        String result = ClientExtensions.toString(response);
        if (result != null) {
            accessTokenResult = SerializeHelper.FromJson(result, IdSrvAccessTokenResult.class);
        }

        return accessTokenResult;
    }

    /**
     * refreshToken 重新获取 accessToken
     *
     * @param refreshToken
     * @return
     */
    protected IdSrvAccessTokenResult refreshToken(String refreshToken, String scope) {
        IdSrvAccessTokenResult accessTokenResult = null;

        OAuth2ClientInfo client = getOAuth2ClientInfo();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + refreshToken);
        headers.put("Content-Type", ClientExtensions.FormContentType);

        Map<String, String> postData = new HashMap<>();
        postData.put(TenantConstant.ClaimTypes_TenantName, client.getTenantName());
        postData.put("client_id", client.getClientId());
        postData.put("client_secret", client.getClientSecret());
        postData.put("grant_type", "refresh_token");
        postData.put("scope", scope);

        CloseableHttpResponse response = ClientExtensions.doPostFormData(client.getTokenEndpoint(), headers, postData);
        String result = ClientExtensions.toString(response);
        if (result != null) {
            accessTokenResult = SerializeHelper.FromJson(result, IdSrvAccessTokenResult.class);
        }

        return accessTokenResult;
    }

    protected class OAuth2ClientInfo implements java.io.Serializable {
        private static final long serialVersionUID = 5711006555433265129L;

        private String tenantName;
        private String clientId;
        private String clientSecret;
        private String clientCredential;

        private String tokenEndpoint;
        private String grantType;

        public OAuth2ClientInfo(String tenantName, String clientId, String clientSecret, String
                clientCredential, String tokenEndpoint, String grantType) {
            this.tenantName = tenantName;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.clientCredential = clientCredential;

            this.tokenEndpoint = tokenEndpoint;
            this.grantType = grantType;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getClientCredential() {
            return clientCredential;
        }

        public void setClientCredential(String clientCredential) {
            this.clientCredential = clientCredential;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getTokenEndpoint() {
            return tokenEndpoint;
        }

        public void setTokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || o.getClass() != this.getClass())
                return false;
            if (!super.equals(o))
                return false;

            OAuth2ClientInfo node = (OAuth2ClientInfo) o;

            if (clientId != null ? !clientId.equals(node.clientId) : node.clientId != null)
                return false;
            if (clientSecret != null ? !clientSecret.equals(node.clientSecret) : node.clientSecret != null)
                return false;
            if (tokenEndpoint != null ? !tokenEndpoint.equals(node.tokenEndpoint) : node.tokenEndpoint != null)
                return false;
            if (grantType != null ? !grantType.equals(node.grantType) : node.grantType != null)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (clientId != null ? clientId.hashCode() : 0);
            result = 31 * result + (clientSecret != null ? clientSecret.hashCode() : 0);
            result = 31 * result + (tokenEndpoint != null ? tokenEndpoint.hashCode() : 0);
            result = 31 * result + (grantType != null ? grantType.hashCode() : 0);
            return result;
        }

    }
}
