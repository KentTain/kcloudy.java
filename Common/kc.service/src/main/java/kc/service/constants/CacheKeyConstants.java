package kc.service.constants;

public class CacheKeyConstants {
	private final static String ComPrx = "com-";

    public class Prefix
    {
        public final static String CurrentUserId = ComPrx + "currentUserId-";

        public final static String TenantAuthTokenEndpoint = ComPrx + "OAuth-TokenEndpoint-";
        public final static String TenantAccessToken = ComPrx + "OAuth-AccessToken-";
        public final static String TenantRefreashToken = ComPrx + "OAuth-RefreashToken-";

        public final static String TenantName = ComPrx + "tenant-";

        public final static String ConfigName = ComPrx + "config-";

    }

    public static class WeiXinCacheKey
    {
        public static String AccessTokenKey(String tenantName)
        {
            return ComPrx + "WeiXinAccessToken-" + tenantName.toLowerCase();
        }
        public static String JsTicketKey(String tenantName)
        {
            return ComPrx + "WeixinJsTicket-" + tenantName.toLowerCase();
        }
        public static String ConfigKey(String tenantName)
        {
            return ComPrx + "WeixinConfig-" + tenantName.toLowerCase();
        }
    }
}
