package kc.service.constants;

import kc.framework.GlobalConfig;
import kc.framework.enums.SystemType;
import kc.framework.extension.StringExtensions;

public class OpenIdConnectConstants {
	public static final String ClientCookieName = "sso.idrserver";
    public static final String AuthScheme = "Cookies";
    //public static final String AuthScheme = "sso.oauth.cookies";
    public static final String ChallengeScheme = "oidc";
    public static final String ApiAuthScheme = "Bearer";
    public static final String ClientAuthScheme = "idsrv";
    
    public static final String OAuth2_Authorize_Action = "connect/authorize";
    public static final String OAuth2_Token_Action = "connect/token";
    public static final String OAuth2_UserInfo_Action = "connect/userinfo";
    public static final String OAuth2_JwkSet_Action = ".well-known/openid-configuration/jwks";

    public static final String JavaCallbackPath = "/login/oauth2/code/idsrv";
    public static final String CallbackPath = "/oidc/signin-callback";
    public static final String SignOutPath = "/oidc/signout-callback";

    public static final String ClaimTypes_TenantName = "tenantname";
    public static final String ClaimTypes_TargetTenantName = "targettenantname";


    public static final String ClaimTypes_UserType = "type";
    public static final String ClaimTypes_Id = "sub";
    public static final String ClaimTypes_Name = "name";
    public static final String ClaimTypes_Email = "email";
    public static final String ClaimTypes_Phone = "phone";
    public static final String ClaimTypes_OrgId = "orgid";
    public static final String ClaimTypes_OrgCode = "orgcode";
    public static final String ClaimTypes_OrgName = "orgname";
    public static final String ClaimTypes_RoleId = "roleid";
    public static final String ClaimTypes_RoleName = "rolename";
    public static final String ClaimTypes_DisplayName = "disiplayname";

    public static final String ClaimTypes_AuthorityIds = "authid";
    
    public static final String ClaimTypes_preferred_username ="preferred_username";
    public static final String ClaimTypes_given_name ="given_name";
    public static final String ClaimTypes_sid ="sid";
    public static final String ClaimTypes_aud ="aud";
    public static final String ClaimTypes_iss ="iss";

    public static final String TestClient_ClientId = "testclient";
    public static final String TestClient_ClientSecret = "secret";
    public static final String WebApiTestClient_ClientId = "9BEF4002-442D-45DA-8079-28E11E3C8721";
	
    /// <summary>
    /// 根据租户代码：TenantName，获取OAuth的Authority
    ///     TenantName为空时，返回SSOWebDomain，例如：http://localhost:1001
    ///     否则返回，例如：http://cTest.localhost:1001
    /// </summary>
    /// <param name="tenantName">租户代码</param>
    /// <returns></returns>
    public static String GetAuthUrlByConfig(String tenantName)
    {
    	SystemType envType = GlobalConfig.SystemType;
        switch (envType)
        {
            case Dev:
                return StringExtensions.isNullOrEmpty(tenantName)
                        ? "http://localhost:1001"
                        : "http://" + tenantName + ".localhost:1001";
            case Test:
                return StringExtensions.isNullOrEmpty(tenantName)
                        ? "http://testsso.kcloudy.com"
                        : "http://" + tenantName + ".testsso.kcloudy.com";
            case Beta:
                return StringExtensions.isNullOrEmpty(tenantName)
                        ? "http://betasso.kcloudy.com"
                    : "http://" + tenantName + ".betasso.kcloudy.com";
            case Product:
                return StringExtensions.isNullOrEmpty(tenantName)
                        ? "http://sso.kcloudy.com"
                    : "http://" + tenantName + ".sso.kcloudy.com";
            default:
                return StringExtensions.isNullOrEmpty(tenantName)
                        ? "http://sso.kcloudy.com"
                    : "http://" + tenantName + ".sso.kcloudy.com";
        }
    }
}
