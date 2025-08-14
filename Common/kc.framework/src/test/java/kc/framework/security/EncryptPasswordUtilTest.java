package kc.framework.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kc.framework.GlobalConfig;
import kc.framework.TestBase;
import kc.framework.base.ServiceRequestToken;
import kc.framework.enums.SystemType;
import kc.framework.extension.StringExtensions;
import kc.framework.tenant.TenantConstant;

@DisplayName("加密解密测试")
class EncryptPasswordUtilTest extends TestBase {
    @BeforeAll
    static void setUpBeforeClass() {
        GlobalConfig.EncryptKey = "dev-cfwin-EncryptKey";
        GlobalConfig.SystemType = SystemType.Dev;

        intilize();
    }

    @AfterAll
    static void setDownAfterClass() {
        GlobalConfig.EncryptKey = "";

        tearDown();
    }

    @Test
    void test_default_EncryptPasswordUtil() {
        // 生产环境
        String key = "KCloudy-Microsoft-EncryptKey";
        String pwd = "NG6lJCNxSxZHrihmlyXS";
        String encrypPwd = "Hcqqkeum+lPvQlPHyHOhM33xffnXWK2P";
        System.out.println("--test_EncryptPasswordUtil key: " + key);
        System.out.println("--test_EncryptPasswordUtil pwd: " + pwd);

        String encryptString = EncryptPasswordUtil.EncryptPassword(pwd, key);
        System.out.println("--test_EncryptPasswordUtil encryptString: " + encryptString);
        assertEquals(encrypPwd, encryptString);

        String decryptString = EncryptPasswordUtil.DecryptPassword(encryptString, key);
        assertEquals(pwd, decryptString);

        // 开发环境
        key = "dev-cfwin-EncryptKey";
        pwd = "P@ssw0rd";
        encrypPwd = "0QVw0yFoX2GuwkMSQyz1tg==";
        System.out.println("--test_EncryptPasswordUtil key: " + key);
        System.out.println("--test_EncryptPasswordUtil pwd: " + pwd);

        encryptString = EncryptPasswordUtil.EncryptPassword(pwd, key);
        System.out.println("--test_EncryptPasswordUtil encryptString: " + encryptString);
        assertEquals(encrypPwd, encryptString);

        decryptString = EncryptPasswordUtil.DecryptPassword(encryptString, key);
        assertEquals(pwd, decryptString);
    }

    @Test
    void test_EncryptPasswordUtil() {
        String key = "L3c132f119l";
        String pwd = "Yv3RtfeR/OF7DK+6Y9a13zQoZEoKNLphqo/NKm1NLMfnnvtoJMx3s42fEdHotcj39uFQ93ifXU4B9EJAFG3dFA==";

        // System.out.println("--test_EncryptPasswordUtil pwd: " + pwd);
        // System.out.println("--test_EncryptPasswordUtil key: " + key);
        String encryptString = EncryptPasswordUtil.EncryptPassword(pwd, key);
        // System.out.println("--test_EncryptPasswordUtil encryptString: " +
        // encryptString);

        String decryptString = EncryptPasswordUtil.DecryptPassword(encryptString, key);
        // System.out.println("--test_EncryptPasswordUtil decryptString: " +
        // decryptString);

        assertEquals(pwd, decryptString);

        String encryptStringWithNet = "OfPxrtMdGomTB5ZKD2X/RdPt8gd0v0MMERAdfnohJuGmR6K8vBF+7H8TY2g/yYP+vH9Aqjw+YcDfttUcbGtBbKZvh0Pm4DwZBDsTQTPLpGYLurAr2d5NfOh9PgdyRehj";// C#
        assertEquals(encryptString, encryptStringWithNet);
        // System.out.println();
    }

    @Test
    void test_TenantEncryptPassword() {
        String decExcept = "4+jbEzR3oUB9k8s+n1gOTifJ40FnghwhobaekoLzruTR0GM6sDnKzZiIHDzE8eN7grq5e5QwQxwcvzoTZeTh/kOTrJWSwGcaroHrEwQ4qPnYDWmzRHd/zoj1x218NLeI";
        String decryptString = EncryptPasswordUtil.DecryptPassword(decExcept, "L3c132f119l");
        // System.out.println("--test_TenantEncryptPassworddecryptString: " +
        // decryptString);

        String encryptString = EncryptPasswordUtil.EncryptPassword(decryptString, "L3c65fd29");
        // System.out.println("--test_TenantEncryptPasswordencryptString: " +
        // encryptString);

        String decryptString2 = EncryptPasswordUtil.DecryptPassword(encryptString, "L3c65fd29");
        // System.out.println("--test_TenantEncryptPassworddecryptString2: " +
        // decryptString2);

        String database = EncryptPasswordUtil.DecryptPassword("ld9km+/hXXgxD9YgsPSnrA==", "Ad9525565");
        // System.out.println("--test_TenantEncryptPassworddatabase: " + database);
        // System.out.println();

        assertEquals(decryptString, decryptString2);
        assertEquals("P@ssw0rd", database);
    }

    @Test
    void test_EncryptString_Tenant() {
        // MsSql用户密码加密秘钥
        String databaseKey = "P@ssw0rd";
        String encryptKey = "dev-cfwin-EncryptKey";
        String decryptDatabaseKey = EncryptPasswordUtil.EncryptPassword(databaseKey, encryptKey);
        System.out.println("MsSql decrypt key: " + decryptDatabaseKey + " with encrypt: " + encryptKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        decryptDatabaseKey = EncryptPasswordUtil.EncryptPassword(databaseKey, encryptKey);
        System.out.println("MsSql decrypt key: " + decryptDatabaseKey + " with encrypt: " + encryptKey);

        String databaseConnString = GlobalConfig.GetDecryptDatabaseConnectionString();
        System.out.println("MsSql ConnectionString: " + databaseConnString);

        // MySql用户密码加密秘钥
        String mysqlKey = "P@ssw0rd";
        encryptKey = "dev-cfwin-EncryptKey";
        String decryptMySqlKey = EncryptPasswordUtil.EncryptPassword(mysqlKey, encryptKey);
        System.out.println("mySql decrypt key: " + decryptMySqlKey + " with encrypt: " + encryptKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        decryptMySqlKey = EncryptPasswordUtil.EncryptPassword(mysqlKey, encryptKey);
        System.out.println("mySql decrypt key: " + decryptMySqlKey + " with encrypt: " + encryptKey);

        String mySqlConnString = GlobalConfig.GetDecryptMySqlConnectionString();
        System.out.println("mySql ConnectionString: " + mySqlConnString);

        // storage用户密码加密秘钥
        String storageKey = "P@ssw0rd";
        encryptKey = "dev-cfwin-EncryptKey";
        String decryptStorageKey = EncryptPasswordUtil.EncryptPassword(storageKey, encryptKey);
        System.out.println("storage decrypt key: " + decryptStorageKey + " with encrypt: " + encryptKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        decryptStorageKey = EncryptPasswordUtil.EncryptPassword(storageKey, encryptKey);
        System.out.println("storage decrypt key: " + decryptStorageKey + " with encrypt: " + encryptKey);

        String storageConnString = GlobalConfig.GetDecryptStorageConnectionString();
        System.out.println("storage ConnectionString: " + storageConnString);

        // 队列用户密码加密秘钥
        String queueKey = "P@ssw0rd";
        encryptKey = "dev-cfwin-EncryptKey";
        String decryptQueueKey = EncryptPasswordUtil.EncryptPassword(queueKey, encryptKey);
        System.out.println("queue decrypt key: " + decryptQueueKey + " with encrypt: " + encryptKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        decryptQueueKey = EncryptPasswordUtil.EncryptPassword(queueKey, encryptKey);
        System.out.println("queue decrypt key: " + decryptQueueKey + " with encrypt: " + encryptKey);

        String queueConnString = GlobalConfig.GetDecryptQueueConnectionString();
        System.out.println("queue ConnectionString: " + queueConnString);

        // redis用户密码加密秘钥
        String redisKey = "P@ssw0rd";
        encryptKey = "dev-cfwin-EncryptKey";
        String decryptRedisKey = EncryptPasswordUtil.EncryptPassword(redisKey, encryptKey);
        System.out.println("redis decrypt key: " + decryptRedisKey + " with encrypt: " + encryptKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        decryptRedisKey = EncryptPasswordUtil.EncryptPassword(redisKey, encryptKey);
        System.out.println("redis decrypt key: " + decryptRedisKey + " with encrypt: " + encryptKey);

        String redisConnString = GlobalConfig.GetDecryptRedisConnectionString();
        System.out.println("redis ConnectionString: " + redisConnString);

        // gitlab用户（tenant-admin)Token加密秘钥
        String gitlabKey = "xpkmXq6Nx_EdtS8PQiS4";
        encryptKey = "dev-cfwin-EncryptKey";
        String encryptGitlabKey = EncryptPasswordUtil.EncryptPassword(gitlabKey, encryptKey);
        String decryptGitlabKey = EncryptPasswordUtil.DecryptPassword(encryptGitlabKey, encryptKey);
        System.out.println("gitlab decrypt key: " + encryptGitlabKey + " with encrypt: " + encryptKey
                + " with decrypt: " + decryptGitlabKey);
        encryptKey = "KCloudy-Microsoft-EncryptKey";
        encryptGitlabKey = EncryptPasswordUtil.EncryptPassword(gitlabKey, encryptKey);
        decryptGitlabKey = EncryptPasswordUtil.DecryptPassword(encryptGitlabKey, encryptKey);
        System.out.println("gitlab decrypt key: " + encryptGitlabKey + " with encrypt: " + encryptKey
                + " with decrypt: " + decryptGitlabKey);

        String gitlabConnString = GlobalConfig.GetDecryptCodeConnectionString();
        System.out.println("gitlab ConnectionString: " + gitlabConnString);
    }

    @Test
    void test_DatabasePasswordHash() {
        String dbaDatabasePassword = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.DbaTenantApiAccessInfo.getDatabasePasswordHash(),
                TenantConstant.DbaTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(dbaDatabasePassword));
        System.out.print("----Dba's Database password: " + dbaDatabasePassword);
        String dbaStorageConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.DbaTenantApiAccessInfo.getStorageAccessKeyPasswordHash(),
                TenantConstant.DbaTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(dbaStorageConnect));
        System.out.print("----Dba's Storage connect string: " + dbaStorageConnect);
        String dbaQueueConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.DbaTenantApiAccessInfo.getQueueAccessKeyPasswordHash(),
                TenantConstant.DbaTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(dbaQueueConnect));
        System.out.println("----Dba's Queue connect string: " + dbaQueueConnect);

        String devdbDatabasePassword = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.TestTenantApiAccessInfo.getDatabasePasswordHash(),
                TenantConstant.TestTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(devdbDatabasePassword));
        System.out.print("----DevDb's Database password: " + devdbDatabasePassword);
        String devdbStorageConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.TestTenantApiAccessInfo.getStorageAccessKeyPasswordHash(),
                TenantConstant.TestTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(devdbStorageConnect));
        System.out.print("----DevDb's Storage connect string: " + devdbStorageConnect);
        String devdbQueueConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.TestTenantApiAccessInfo.getQueueAccessKeyPasswordHash(),
                TenantConstant.TestTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(devdbQueueConnect));
        System.out.println("----DevDb's Queue connect string: " + devdbQueueConnect);

        String BuyDatabasePassword = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.BuyTenantApiAccessInfo.getDatabasePasswordHash(),
                TenantConstant.BuyTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(BuyDatabasePassword));
        System.out.print("----Buy's Database password: " + BuyDatabasePassword);
        String BuyStorageConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.BuyTenantApiAccessInfo.getStorageAccessKeyPasswordHash(),
                TenantConstant.BuyTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(BuyStorageConnect));
        System.out.print("----Buy's Storage connect string: " + BuyStorageConnect);
        String BuyQueueConnect = EncryptPasswordUtil.DecryptPassword(
                TenantConstant.BuyTenantApiAccessInfo.getQueueAccessKeyPasswordHash(),
                TenantConstant.BuyTenantApiAccessInfo.getPrivateEncryptKey());
        assertFalse(StringExtensions.isNullOrEmpty(BuyQueueConnect));
        System.out.println("----Buy's Queue connect string: " + BuyQueueConnect);
        // System.out.println();
    }

    @Test
    void test_TenantSignature() throws Exception {
        // String DbaSignatureObj = new
        // KC.Framework.Base.ServiceRequestToken(TenantConstant.DbaTenantId.ToString(),
        // TenantConstant.DbaTenantName, TenantConstant.DefaultPrivateEncryptKey);
        // String DbaSignature = DbaSignatureObj.GetEncrptSignature();
        String DbaSignature = TenantConstant.DbaTenantApiAccessInfo.GenerateSignature();
        String DbaSignatureWithNet = "W8mKe7ozff0E1OIHtodfC4+E7yEmIIpCG0ist4RJhCaGDgAZoGcZWgn/c56g8tFLI0tpJsQM18SNUhYSma1WODXzPxPHwpRowAsCd2ATiak=";
        assertFalse(StringExtensions.isNullOrEmpty(DbaSignature));
        // System.out.println("----Dba's Signature: " + DbaSignature);
        // System.out.println("----Dba's Signature: " + DbaSignatureWithNet);
        assertEquals(DbaSignature, DbaSignatureWithNet);
        // String exceptDbaSignature = new
        // KC.Framework.Base.ServiceRequestToken(DbaSignature,
        // TenantConstant.DefaultPrivateEncryptKey);
        ServiceRequestToken exceptDbaSignature = TenantConstant.DbaTenantApiAccessInfo.GetServiceToken();
        assertTrue(StringExtensions.isNullOrEmpty(exceptDbaSignature.IsValid(TenantConstant.DbaTenantName)));
        assertEquals(TenantConstant.DbaTenantApiAccessInfo.getTenantName(), exceptDbaSignature.MemberId);

        // String DevDbSignatureObj = new
        // KC.Framework.Base.ServiceRequestToken(TenantConstant.TestTenantId.ToString(),
        // TenantConstant.TestTenantName, TenantConstant.DefaultPrivateEncryptKey);
        // String DevDbSignature = DevDbSignatureObj.GetEncrptSignature();
        String DevDbSignature = TenantConstant.TestTenantApiAccessInfo.GenerateSignature();
        assertFalse(StringExtensions.isNullOrEmpty(DevDbSignature));
        // System.out.println("----DevDb's Signature: " + DevDbSignature);
        // String exceptDevDBSignature = new
        // KC.Framework.Base.ServiceRequestToken(DevDbSignature,
        // TenantConstant.DefaultPrivateEncryptKey);
        ServiceRequestToken exceptDevDBSignature = TenantConstant.TestTenantApiAccessInfo.GetServiceToken();
        assertTrue(StringExtensions.isNullOrEmpty(exceptDevDBSignature.IsValid(TenantConstant.TestTenantName)));
        assertEquals(TenantConstant.TestTenantApiAccessInfo.getTenantName(), exceptDevDBSignature.MemberId);

        // String BuySignatureObj = new
        // KC.Framework.Base.ServiceRequestToken(TenantConstant.BuyTenantId.ToString(),
        // TenantConstant.BuyTenantName, TenantConstant.DefaultPrivateEncryptKey);
        // String BuySignature = BuySignatureObj.GetEncrptSignature();
        String BuySignature = TenantConstant.BuyTenantApiAccessInfo.GenerateSignature();
        assertFalse(StringExtensions.isNullOrEmpty(BuySignature));
        // System.out.println("----Buy's Signature: " + BuySignature);
        // String exceptBuySignature = new
        // KC.Framework.Base.ServiceRequestToken(BuySignature,
        // TenantConstant.DefaultPrivateEncryptKey);
        ServiceRequestToken exceptBuySignature = TenantConstant.BuyTenantApiAccessInfo.GetServiceToken();
        assertTrue(StringExtensions.isNullOrEmpty(exceptBuySignature.IsValid(TenantConstant.BuyTenantName)));
        assertEquals(TenantConstant.BuyTenantApiAccessInfo.getTenantName(), exceptBuySignature.MemberId);

        // String SaleSignatureObj = new
        // KC.Framework.Base.ServiceRequestToken(TenantConstant.SaleTenantId.ToString(),
        // TenantConstant.SaleTenantName, TenantConstant.DefaultPrivateEncryptKey);
        // String SaleSignature = SaleSignatureObj.GetEncrptSignature();
        String SaleSignature = TenantConstant.SaleTenantApiAccessInfo.GenerateSignature();
        assertTrue(!StringExtensions.isNullOrEmpty(SaleSignature));
        // System.out.println("----Sale's Signature: " + SaleSignature);
        // String exceptSaleSignature = new
        // KC.Framework.Base.ServiceRequestToken(SaleSignature,
        // TenantConstant.DefaultPrivateEncryptKey);
        ServiceRequestToken exceptSaleSignature = TenantConstant.SaleTenantApiAccessInfo.GetServiceToken();
        assertTrue(StringExtensions.isNullOrEmpty(exceptSaleSignature.IsValid(TenantConstant.SaleTenantName)));
        assertEquals(TenantConstant.SaleTenantApiAccessInfo.getTenantName(), exceptSaleSignature.MemberId);
        // System.out.println();
    }

    @Test
    void test_Diff_Env_EncryptPassword() {
        String dev_key = "dev-cfwin-EncryptKey";
        String dev_password = "P@ssw0rd";
        String dev_enString = EncryptPasswordUtil.EncryptPassword(dev_password, dev_key);
        System.out.println("-----dev_enString: " + dev_enString);

        String test_key = "test-cfwin-EncryptKey";
        String test_password = "P@ssw0rd";
        String test_enString = EncryptPasswordUtil.EncryptPassword(test_password, test_key);
        System.out.println("-----test_enString: " + test_enString);

        String beta_key = "beta-cfwin-EncryptKey";
        String beta_password = "P@ssw0rd";
        String beta_enString = EncryptPasswordUtil.EncryptPassword(beta_password, beta_key);
        System.out.println("-----beta_enString: " + beta_enString);

        String product_key = "KCloudy-Microsoft-EncryptKey";
        String product_password = "P@ssw0rd";
        String product_enString = EncryptPasswordUtil.EncryptPassword(product_password, product_key);
        System.out.println("-----product_enString: " + product_enString);
    }

    @Test
    void testGetRandomString() {
        int len = 11;
        String randomStr = EncryptPasswordUtil.GetRandomString(len);
        System.out.println("----randomStr: " + randomStr);
        assertEquals(len, randomStr.length());
    }

}
