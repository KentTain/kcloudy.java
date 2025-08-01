package kc.framework;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.env.Environment;

import kc.framework.base.ApplicationInfo;
import kc.framework.extension.StringExtensions;
import kc.framework.security.EncryptPasswordUtil;
import kc.framework.tenant.ApplicationConstant;
import kc.framework.tenant.ConnectionKeyConstant;
import kc.framework.tenant.TenantConstant;
import kc.framework.util.PrintLogUtil;

public class GlobalConfig implements java.io.Serializable {

    private static final long serialVersionUID = 1084979239272553188L;
    public static String ApplicationId;
    public static UUID ApplicationGuid;
    public static String EncryptKey;
    public static String BlobStorage;
    public static String AdminEmails;
    public static String TempFilePath;

    public static String DatabaseConnectionString;
    public static String MySqlConnectionString;
    public static String StorageConnectionString;
    public static String QueueConnectionString;
    public static String NoSqlConnectionString;
    public static String RedisConnectionString;
    public static String ServiceBusConnectionString;
    public static String VodConnectionString;
    public static String CodeConnectionString;

    public static String ClientId;
    public static String ClientSecret;

    public static String WeixinAppKey;
    public static String WeixinAppSecret;
    public static String WeixinAppToken;

    public static String ZZZLicenseName;
    public static String ZZZLicenseKey;

    public static List<ApplicationInfo> Applications;
    public static ApplicationInfo CurrentApplication;

    /**
     * subdomain的sso地址：http://sso.kcloudy.com/ <br/>
     * 本地测试接口地址：http://localhost:1001/
     */
    public static String SSOWebDomain;

    /**
     * subdomain的admin地址：http://admin.kcloudy.com/ <br/>
     * 本地测试接口地址：http://localhost:1003
     */
    public static String AdminWebDomain;

    /**
     * subdomain的Blog地址：http://blog.kcloudy.com/ <br/>
     * 本地测试接口地址：http://localhost:1005
     */
    public static String BlogWebDomain;

    /**
     * subdomain的CodeGenerate地址：http://code.kcloudy.com/ <br/>
     * 本地测试接口地址：http://localhost:1007
     */
    public static String CodeWebDomain;

    /**
     * subdomain的config地址：http://subdomain.cfg.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:1101/
     */
    public static String CfgWebDomain;

    /**
     * subdomain的dictionary地址：http://subdomain.dic.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:1103/
     */
    public static String DicWebDomain;

    /**
     * subdomain的app地址：http://subdomain.app.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:1105
     */
    public static String AppWebDomain;

    /**
     * subdomain的message地址：http://subdomain.msg.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:1109/
     */
    public static String MsgWebDomain;

    /**
     * subdomain的account地址：http://subdomain.acc.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:2001/
     */
    public static String AccWebDomain;

    /**
     * subdomain的contract地址：http://subdomain.econ.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:2003/
     */
    public static String EconWebDomain;
    /**
     * subdomain的dictionary地址：http://subdomain.doc.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:2005/
     */
    public static String DocWebDomain;

    /**
     * subdomain的Hr地址：http://subdomain.hr.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:2007/
     */
    public static String HrWebDomain;

    /**
     * subdomain的crm地址：http://subdomain.crm.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:3001/
     */
    public static String CrmWebDomain;

    /**
     * subdomain的srm地址：http://subdomain.srm.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:3003/
     */
    public static String SrmWebDomain;

    /**
     * subdomain的prd地址：http://subdomain.prd.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:3005/
     */
    public static String PrdWebDomain;

    /**
     * subdomain的pmc地址：http://subdomain.pmc.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:3007/
     */
    public static String PmcWebDomain;

    /**
     * subdomain的电商地址：http://subdomain.shop.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:4001/
     */
    public static String PortalWebDomain;
    /**
     * subdomain的som地址：http://subdomain.som.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:4003/
     */
    public static String SomWebDomain;

    /**
     * subdomain的pom地址：http://subdomain.pom.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:4005/
     */
    public static String PomWebDomain;

    /**
     * subdomain的Wms地址：http://subdomain.wms.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:4007/
     */
    public static String WmsWebDomain;

    /**
     * subdomain的融资地址：http://subdomain.market.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:5001/
     */
    public static String JRWebDomain;

    /**
     * subdomain的项目管理地址：http://subdomain.prj.kcloudy.com/ </br>
     * 本地测试接口地址：http://subdomain.localhost:5001/
     */
    public static String PrjWebDomain;

    /**
     * subdomain的会员管理地址：http://subdomain.mbr.kcloudy.com/ </br>
     * 本地测试接口地址：http://subdomain.localhost:5005/
     */
    public static String MbrWebDomain;

    /**
     * subdomain的erp地址：http://subdomain.erp.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:6001/
     */
    public static String TrainWebDomain;
    /**
     * subdomain的exam地址：http://subdomain.exam.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:6003/
     */
    public static String ExamWebDomain;
    /**
     * subdomain的工作流地址：http://subdomain.flow.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:7001/
     */
    public static String WorkflowWebDomain;

    /**
     * subdomain的sso地址：http://subdomain.pay.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:8001/
     */
    public static String PayWebDomain;

    /**
     * subdomain的微信地址：http://subdomain.wx.kcloudy.com/ <br/>
     * 本地测试接口地址：http://subdomain.localhost:9001/
     */
    public static String WXWebDomain;
    /**
     * subdomain的资源地址：http://resource.kcloudy.com/ 本地测试接口地址：http://localhost:9999/
     */
    public static String ResWebDomain;

    /**
     * subdomain的接口地址，无api/后缀：http://subdomain.api.kcloudy.com/
     */
    public static String ApiWebDomain;

    /**
     * 获取webDomain的租户域名地址：http://[tenantName].[webapi].kcloudy.com/
     * 本地测试地址：http://[tenantName].localhost:1002/
     *
     * @param webDomain  应用二级域名
     * @param tenantName 租户代码
     * @return 获取租户的Api接口地址
     */
    public static String GetTenantWebDomain(String webDomain, String tenantName) {
        if (StringExtensions.isNullOrEmpty(webDomain))
            return "";

        String tenantWebDomain = webDomain.replace(TenantConstant.SubDomain, tenantName);
        String busName = StringExtensions.getBusNameByHost(tenantWebDomain);
        List<String> level2Domains = Arrays.asList("http://localhost:1001/", "http://sso.kcloudy.com/",
                "http://ssotest.kcloudy.com/", "http://ssobeta.kcloudy.com/", "http://ssodemo.kcloudy.com/");
        boolean isLocal = tenantWebDomain.contains("localhost");
        if (level2Domains.contains(webDomain)) {
            return isLocal ? tenantWebDomain.replace("localhost", tenantName + ".localhost")
                    : tenantWebDomain.replace(busName + ".", tenantName + "." + busName + ".");
        }

        return tenantWebDomain;
    }

    /**
     * 获取租户的Api接口地址：http://(tenantName).accapi.kcloudy.com/api/
     *
     * @param webDomain  应用二级域名
     * @param tenantName 租户代码
     * @return 获取租户的Api接口地址
     */
    public static String GetTenantWebApiDomain(String webDomain, String tenantName) {
        if (StringExtensions.isNullOrEmpty(webDomain))
            return null;

        String tenantWebDomain = webDomain.replace(TenantConstant.SubDomain, tenantName);
        String busName = StringExtensions.getBusNameByHost(tenantWebDomain);
        List<String> level2Domains = Arrays.asList("http://localhost:1001/", "http://sso.kcloudy.com/",
                "http://ssotest.kcloudy.com/", "http://ssobeta.kcloudy.com/", "http://ssodemo.kcloudy.com/");
        boolean isLocal = tenantWebDomain.toLowerCase().contains("localhost");
        if (level2Domains.contains(webDomain)) {
            tenantWebDomain = isLocal ? tenantWebDomain.replace("localhost", tenantName + ".localhost")
                    : tenantWebDomain.replace(busName + ".", tenantName + "." + busName + ".");
        }

        if (IsDevelopment() || isLocal) {
            if (StringExtensions.isNumber(busName)) {
                String apiPort = String.valueOf(Integer.parseInt(busName) + 1);
                return StringExtensions.trimEndSlash(tenantWebDomain.replace(busName, apiPort)) + "/api/";
            }
        }
        return StringExtensions.trimEndSlash(tenantWebDomain.replace(busName, busName + "api")) + "/api/";
    }

    /**
     * subdomain的接口地址：http://subdomain.api.kcloudy.com/api/
     *
     * @return String
     */
    public static String ApiServerUrl() {
        if (StringExtensions.isNullOrEmpty(ApiWebDomain))
            return null;

        return ApiWebDomain + "api/";
    }

    public static String CertThumbprint;

    /**
     * 上传配置
     */
    public static kc.framework.UploadConfig UploadConfig;

    /**
     * 系统环境类型：Dev、Test、Beta、Production
     */
    public static kc.framework.enums.SystemType SystemType;

    public static boolean IsDevelopment() {
        return SystemType == kc.framework.enums.SystemType.Dev;
    }

    /**
     * 获取解密后的SqlServer连接字符串
     */
    public static String GetDecryptDatabaseConnectionString() {
        if (StringExtensions.isNullOrEmpty(DatabaseConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(DatabaseConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.DatabaseEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.DatabaseName);
            String user = keyValues.get(ConnectionKeyConstant.DatabaseUserID);
            String pwd = keyValues.get(ConnectionKeyConstant.DatabasePassword);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(user) || StringExtensions.isNullOrEmpty(pwd))
                return "";

            String azureConn = "Server=%s;Database=%s;User ID=%s;Password=%s;MultipleActiveResultSets=true;";
            return String.format(azureConn, endpoint, name, user, EncryptPasswordUtil.DecryptPassword(pwd, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的MySql连接字符串
     */
    public static String GetDecryptMySqlConnectionString() {
        if (StringExtensions.isNullOrEmpty(MySqlConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(MySqlConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.DatabaseEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.DatabaseName);
            String user = keyValues.get(ConnectionKeyConstant.DatabaseUserID);
            String pwd = keyValues.get(ConnectionKeyConstant.DatabasePassword);
            String port = keyValues.get(ConnectionKeyConstant.DatabasePort);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(user) || StringExtensions.isNullOrEmpty(pwd))
                return "";

            String azureConn = "Server=%s;Database=%s;User ID=%s;Password=%s;Port=%s;sslMode=None;";
            return String.format(azureConn, endpoint, name, user, EncryptPasswordUtil.DecryptPassword(pwd, EncryptKey),
                    port);
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的存储连接字符串
     */
    public static String GetDecryptStorageConnectionString() {
        if (StringExtensions.isNullOrEmpty(StorageConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(StorageConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.BlobEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "DefaultEndpointsProtocol=https;BlobEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的队列连接字符串
     */
    public static String GetDecryptQueueConnectionString() {
        if (StringExtensions.isNullOrEmpty(QueueConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(QueueConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.QueueEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "DefaultEndpointsProtocol=https;QueueEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的NoSql连接字符串
     */
    public static String GetDecryptNoSqlConnectionString() {
        if (StringExtensions.isNullOrEmpty(NoSqlConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(NoSqlConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.NoSqlEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "DefaultEndpointsProtocol=https;TableEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的服务总线连接字符串
     */
    public static String GetDecryptServiceBusConnectionString() {
        if (StringExtensions.isNullOrEmpty(ServiceBusConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(ServiceBusConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.ServiceBusEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.ServiceBusAccessName);
            String key = keyValues.get(ConnectionKeyConstant.ServiceBusAccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "Endpoint=%s;SharedAccessKeyName=%s;SharedAccessKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的VOD连接字符串
     */
    public static String GetDecryptVodConnectionString() {
        if (StringExtensions.isNullOrEmpty(VodConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(VodConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.VodEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "VodEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的代码仓库连接字符串
     */
    public static String GetDecryptCodeConnectionString() {
        if (StringExtensions.isNullOrEmpty(CodeConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(CodeConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.CodeEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "CodeEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    /**
     * 获取解密后的Redis连接字符串
     */
    public static String GetDecryptRedisConnectionString() {
        if (StringExtensions.isNullOrEmpty(RedisConnectionString))
            return "";
        try {
            Dictionary<String, String> keyValues = StringExtensions
                    .keyValuePairFromConnectionString(RedisConnectionString);
            String endpoint = keyValues.get(ConnectionKeyConstant.RedisEndpoint);
            String name = keyValues.get(ConnectionKeyConstant.AccessName);
            String key = keyValues.get(ConnectionKeyConstant.AccessKey);

            if (StringExtensions.isNullOrEmpty(endpoint) || StringExtensions.isNullOrEmpty(name)
                    || StringExtensions.isNullOrEmpty(key))
                return "";

            String azureConn = "RedisEndpoint=%s;AccountName=%s;AccountKey=%s";
            return String.format(azureConn, endpoint, name, EncryptPasswordUtil.DecryptPassword(key, EncryptKey));
        } catch (Exception ex) {
            PrintLogUtil.printError(ex.getMessage());
            return "";
        }
    }

    public static void InitGlobalConfig(Environment env) {
        if (env == null)
            return;

        ApplicationId = env.getProperty("GlobalConfig.ApplicationId");
        if (!StringExtensions.isNullOrEmpty(ApplicationId)) {
            ApplicationGuid = UUID.fromString(ApplicationId);
        }

        EncryptKey = env.getProperty("GlobalConfig.EncryptKey");
        BlobStorage = env.getProperty("GlobalConfig.BlobStorage");
        AdminEmails = env.getProperty("GlobalConfig.AdminEmails");
        TempFilePath = env.getProperty("GlobalConfig.TempFilePath");

        DatabaseConnectionString = env.getProperty("GlobalConfig.DatabaseConnectionString");
        MySqlConnectionString = env.getProperty("GlobalConfig.MySqlConnectionString");
        StorageConnectionString = env.getProperty("GlobalConfig.StorageConnectionString");
        QueueConnectionString = env.getProperty("GlobalConfig.QueueConnectionString");
        NoSqlConnectionString = env.getProperty("GlobalConfig.NoSqlConnectionString");
        RedisConnectionString = env.getProperty("GlobalConfig.RedisConnectionString");
        ServiceBusConnectionString = env.getProperty("GlobalConfig.ServiceBusConnectionString");
        VodConnectionString = env.getProperty("GlobalConfig.VodConnectionString");
        CodeConnectionString = env.getProperty("GlobalConfig.CodeConnectionString");

        SSOWebDomain = StringExtensions.endWithSlash(env.getProperty("GlobalConfig.SSOWebDomain"));
        ResWebDomain = StringExtensions.endWithSlash(env.getProperty("GlobalConfig.ResWebDomain"));

        ClientId = env.getProperty("GlobalConfig.ClientId");
        ClientSecret = env.getProperty("GlobalConfig.ClientSecret");

        WeixinAppKey = env.getProperty("GlobalConfig.WeixinAppKey");
        WeixinAppSecret = env.getProperty("GlobalConfig.WeixinAppSecret");
        WeixinAppToken = env.getProperty("GlobalConfig.WeixinAppToken");

        ZZZLicenseName = env.getProperty("GlobalConfig.ZZZLicenseName");
        ZZZLicenseKey = env.getProperty("GlobalConfig.ZZZLicenseKey");

        CertThumbprint = env.getProperty("GlobalConfig.ZZZLicenseKey");

        setSystemType();

        TenantConstant.InitTestTenant();
    }

    public static void InitGlobalConfigWithApiData(Environment env, GlobalConfigData data) {
        if (data == null)
            return;

        EncryptKey = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.EncryptKey"))
                ? (data.getEncryptKey())
                : env.getProperty("GlobalConfig.EncryptKey");
        BlobStorage = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.BlobStorage"))
                ? (data.getBlobStorage())
                : env.getProperty("GlobalConfig.BlobStorage");
        AdminEmails = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.AdminEmails"))
                ? (data.getAdminEmails())
                : env.getProperty("GlobalConfig.AdminEmails");
        TempFilePath = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.TempFilePath"))
                ? (data.getTempFilePath())
                : env.getProperty("GlobalConfig.TempFilePath");

        DatabaseConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.DatabaseConnectionString"))
                        ? (data.getDatabaseConnectionString())
                        : env.getProperty("GlobalConfig.DatabaseConnectionString");
        MySqlConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.MySqlConnectionString"))
                        ? (data.getMySqlConnectionString())
                        : env.getProperty("GlobalConfig.MySqlConnectionString");
        StorageConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.StorageConnectionString"))
                        ? (data.getStorageConnectionString())
                        : env.getProperty("GlobalConfig.StorageConnectionString");
        QueueConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.QueueConnectionString"))
                        ? (data.getQueueConnectionString())
                        : env.getProperty("GlobalConfig.QueueConnectionString");
        NoSqlConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.NoSqlConnectionString"))
                        ? (data.getNoSqlConnectionString())
                        : env.getProperty("GlobalConfig.NoSqlConnectionString");
        RedisConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.RedisConnectionString"))
                        ? (data.getRedisConnectionString())
                        : env.getProperty("GlobalConfig.RedisConnectionString");
        ServiceBusConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.ServiceBusConnectionString"))
                        ? (data.getServiceBusConnectionString())
                        : env.getProperty("GlobalConfig.ServiceBusConnectionString");
        VodConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.VodConnectionString"))
                        ? (data.getVodConnectionString())
                        : env.getProperty("GlobalConfig.VodConnectionString");
        CodeConnectionString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.CodeConnectionString"))
                        ? (data.getCodeConnectionString())
                        : env.getProperty("GlobalConfig.CodeConnectionString");

        SSOWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.SSOWebDomain"))
                ? StringExtensions.endWithSlash(data.getSsoWebDomain())
                : env.getProperty("GlobalConfig.SSOWebDomain");
        AdminWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.AdminWebDomain"))
                ? StringExtensions.endWithSlash(data.getAdminWebDomain())
                : env.getProperty("GlobalConfig.AdminWebDomain");
        BlogWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.BlogWebDomain"))
                ? StringExtensions.endWithSlash(data.getBlogWebDomain())
                : env.getProperty("GlobalConfig.BlogWebDomain");
        CodeWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.CodeWebDomain"))
                ? StringExtensions.endWithSlash(data.getCodeWebDomain())
                : env.getProperty("GlobalConfig.CodeWebDomain");

        AppWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.AppWebDomain"))
                ? StringExtensions.endWithSlash(data.getAppWebDomain())
                : env.getProperty("GlobalConfig.AppWebDomain");
        CfgWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.CfgWebDomain"))
                ? StringExtensions.endWithSlash(data.getCfgWebDomain())
                : env.getProperty("GlobalConfig.CfgWebDomain");
        DicWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.DicWebDomain"))
                ? StringExtensions.endWithSlash(data.getDicWebDomain())
                : env.getProperty("GlobalConfig.DicWebDomain");
        MsgWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.MsgWebDomain"))
                ? StringExtensions.endWithSlash(data.getMsgWebDomain())
                : env.getProperty("GlobalConfig.MsgWebDomain");

        AccWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.AccWebDomain"))
                ? StringExtensions.endWithSlash(data.getAccWebDomain())
                : env.getProperty("GlobalConfig.AccWebDomain");
        EconWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.EconWebDomain"))
                ? StringExtensions.endWithSlash(data.getEconWebDomain())
                : env.getProperty("GlobalConfig.EconWebDomain");
        DocWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.DocWebDomain"))
                ? StringExtensions.endWithSlash(data.getDocWebDomain())
                : env.getProperty("GlobalConfig.DocWebDomain");
        HrWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.HrWebDomain"))
                ? StringExtensions.endWithSlash(data.getHrWebDomain())
                : env.getProperty("GlobalConfig.HrWebDomain");

        CrmWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.CrmWebDomain"))
                ? StringExtensions.endWithSlash(data.getCrmWebDomain())
                : env.getProperty("GlobalConfig.CrmWebDomain");
        SrmWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.SrmWebDomain"))
                ? StringExtensions.endWithSlash(data.getSrmWebDomain())
                : env.getProperty("GlobalConfig.SrmWebDomain");
        PrdWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PrdWebDomain"))
                ? StringExtensions.endWithSlash(data.getPrdWebDomain())
                : env.getProperty("GlobalConfig.PrdWebDomain");
        PmcWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PmcWebDomain"))
                ? StringExtensions.endWithSlash(data.getPmcWebDomain())
                : env.getProperty("GlobalConfig.PmcWebDomain");

        PortalWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PortalWebDomain"))
                ? StringExtensions.endWithSlash(data.getPortalWebDomain())
                : env.getProperty("GlobalConfig.PortalWebDomain");
        SomWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.SomWebDomain"))
                ? StringExtensions.endWithSlash(data.getSomWebDomain())
                : env.getProperty("GlobalConfig.SomWebDomain");
        PomWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PomWebDomain"))
                ? StringExtensions.endWithSlash(data.getPomWebDomain())
                : env.getProperty("GlobalConfig.PomWebDomain");
        WmsWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.WmsWebDomain"))
                ? StringExtensions.endWithSlash(data.getWmsWebDomain())
                : env.getProperty("GlobalConfig.WmsWebDomain");

        JRWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.JRWebDomain"))
                ? StringExtensions.endWithSlash(data.getJrWebDomain())
                : env.getProperty("GlobalConfig.JRWebDomain");
        PrjWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PrjWebDomain"))
                ? StringExtensions.endWithSlash(data.getPrjWebDomain())
                : env.getProperty("GlobalConfig.PrjWebDomain");
        MbrWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.MbrWebDomain"))
                ? StringExtensions.endWithSlash(data.getMbrWebDomain())
                : env.getProperty("GlobalConfig.MbrWebDomain");

        TrainWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.TrainWebDomain"))
                ? StringExtensions.endWithSlash(data.getTrainWebDomain())
                : env.getProperty("GlobalConfig.TrainWebDomain");
        ExamWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.ExamWebDomain"))
                ? StringExtensions.endWithSlash(data.getExamWebDomain())
                : env.getProperty("GlobalConfig.ExamWebDomain");

        WorkflowWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.FlowWebDomain"))
                ? StringExtensions.endWithSlash(data.getFlowWebDomain())
                : env.getProperty("GlobalConfig.WorkflowWebDomain");
        PayWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.PayWebDomain"))
                ? StringExtensions.endWithSlash(data.getPayWebDomain())
                : env.getProperty("GlobalConfig.PayWebDomain");
        WXWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.WXWebDomain"))
                ? StringExtensions.endWithSlash(data.getWxWebDomain())
                : env.getProperty("GlobalConfig.WXWebDomain");
        ResWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.ResWebDomain"))
                ? StringExtensions.endWithSlash(data.getResWebDomain())
                : env.getProperty("GlobalConfig.ResWebDomain");

        ApiWebDomain = null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.ApiWebDomain"))
                ? StringExtensions.endWithSlash(data.getApiWebDomain())
                : env.getProperty("GlobalConfig.ApiWebDomain");

        Applications = null != data.getApplications() && data.getApplications().size() > 0 ? data.getApplications()
                : ApplicationConstant.GetAllApplications();
        Optional<ApplicationInfo> optApp = Applications.stream()
                .filter(m -> m.getAppId().equalsIgnoreCase(ApplicationId)).findFirst();

        optApp.ifPresent(applicationInfo -> CurrentApplication = applicationInfo);

        setSystemType();

        String imageMaxSizeString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.UploadConfig.ImageMaxSize"))
                        ? data.getUploadConfig() != null ? Integer.toString(data.getUploadConfig().getImageMaxSize())
                                : "10"
                        : env.getProperty("GlobalConfig.UploadConfig.ImageMaxSize");
        int imageMaxSize = 10;
        if (!StringExtensions.isNullOrEmpty(imageMaxSizeString)) {
            try {
                imageMaxSize = Integer.parseInt(imageMaxSizeString);
            } catch (NumberFormatException ignored) {
                imageMaxSize = 10;
            }
        }

        String fileMaxSizeString = null == env
                || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.UploadConfig.FileMaxSize"))
                        ? data.getUploadConfig() != null ? Integer.toString(data.getUploadConfig().getFileMaxSize())
                                : "10"
                        : env.getProperty("GlobalConfig.UploadConfig.FileMaxSize");
        int fileMaxSize = 10;
        if (!StringExtensions.isNullOrEmpty(fileMaxSizeString)) {
            try {
                fileMaxSize = Integer.parseInt(fileMaxSizeString);
            } catch (NumberFormatException ignored) {
                fileMaxSize = 10;
            }
        }

        UploadConfig = new kc.framework.UploadConfig();
        UploadConfig.setImageMaxSize(imageMaxSize);
        UploadConfig.setImageExt(
                null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.UploadConfig.ImageExt"))
                        ? data.getUploadConfig() != null ? data.getUploadConfig().getImageExt() : ""
                        : env.getProperty("GlobalConfig.UploadConfig.ImageExt"));
        UploadConfig.setFileMaxSize(fileMaxSize);
        UploadConfig.setFileExt(
                null == env || StringExtensions.isNullOrEmpty(env.getProperty("GlobalConfig.UploadConfig.FileExt"))
                        ? data.getUploadConfig() != null ? data.getUploadConfig().getFileExt() : ""
                        : env.getProperty("GlobalConfig.UploadConfig.FileExt"));

    }

    private static void setSystemType() {
        String webDomain = SSOWebDomain.toLowerCase();
        if (webDomain.contains("localhost") || webDomain.contains("localsso.") || webDomain.contains("devsso.")) {
            SystemType = kc.framework.enums.SystemType.Dev;
        } else if (webDomain.contains("testsso.")) {
            SystemType = kc.framework.enums.SystemType.Test;
        } else if (webDomain.contains("betasso.")) {
            SystemType = kc.framework.enums.SystemType.Beta;
        } else {
            SystemType = kc.framework.enums.SystemType.Product;
        }
    }
}
