package kc.framework.extension;

import kc.framework.tenant.TenantConstant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Dictionary;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class StringExtensionsTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void test_ArrayFromCommaDelimitedStrings() {
        String listStr = "aaa, bbb, ccc";
        String[] except = new String[]{"aaa", "bbb", "ccc"};
        String[] result = StringExtensions.arrayFromCommaDelimitedStrings(listStr);

        assertArrayEquals(except, result);
    }

    @Test
    void test_ArrayFromCommaDelimitedStringsBysplitChar() {
        String listStr = "aaa-bbb-ccc";
        String[] except = new String[]{"aaa", "bbb", "ccc"};
        String[] result = StringExtensions.arrayFromCommaDelimitedStringsBySplitChar(listStr, "-");

        assertArrayEquals(except, result);
    }

    @Test
    void test_ArrayFromCommaDelimitedIntegers() {
        String listStr = "1,2,3";
        Integer[] except = new Integer[]{1, 2, 3};
        Integer[] result = StringExtensions.arrayFromCommaDelimitedIntegers(listStr);

        assertArrayEquals(except, result);
    }

    @Test
    void test_ArrayFromCommaDelimitedIntegersBySplitChar() {
        String listStr = "1-2-3";
        Integer[] except = new Integer[]{1, 2, 3};
        Integer[] result = StringExtensions.arrayFromCommaDelimitedIntegersBysplitChar(listStr, "-");

        assertArrayEquals(except, result);
    }

    @Test
    void test_KeyValuePairFromConnectionString() {
        String listStr = "AccessId=aaa;AccessKey=bbb;";
        Dictionary<String, String> except = new Hashtable<String, String>();
        except.put("accessid", "aaa");
        except.put("accesskey", "bbb");
        Dictionary<String, String> result = StringExtensions.keyValuePairFromConnectionString(listStr);

        //System.out.print("\ntestKeyValuePairFromConnectionString except AccessId: " + except.get("accessid"));
        //System.out.println("\ntestKeyValuePairFromConnectionString result AccessId: " + result.get("accessid"));

        assertEquals(except.get("accessid"), result.get("accessid"));
        assertEquals(except.get("accesskey"), result.get("accesskey"));
    }

    @Test
    void test_getDomainNameFromUrl() {
        String url = "http://ctest.domai.com/controller/action/query=";
        String result = StringExtensions.getDomainNameFromUrl(url);
        assertEquals("ctest.domai.com", result);

        url = "http://ctest.localhost:1003";
        result = StringExtensions.getDomainNameFromUrl(url);
        assertEquals("ctest.localhost", result);

        url = "ctest.localhost:1003";
        result = StringExtensions.getTenantNameFromUrl(url);
        assertEquals("", result);
    }

    @Test
    void test_getTenantNameFromUrl() {
        String url = "http://ctest.domain.com/controller/action/query=";
        String result = StringExtensions.getTenantNameFromUrl(url);
        assertEquals("ctest", result);

        url = "http://ctest.localhost:1003";
        result = StringExtensions.getTenantNameFromUrl(url);
        assertEquals("ctest", result);

        url = "ctest.localhost:1003";
        result = StringExtensions.getTenantNameFromUrl(url);
        assertEquals("", result);
    }

    @Test
    void test_IsUrl() {
        String url = "http://www.domai.com/controller/action/query=";
        boolean isUrl = StringExtensions.isUrl(url);
        assertTrue(isUrl);

        url = "https://www.domai.com/controller/action/query=";
        isUrl = StringExtensions.isUrl(url);
        assertTrue(isUrl);

        url = "https://ctest.localhost/controller/action/query=";
        isUrl = StringExtensions.isUrl(url);
        assertTrue(isUrl);

        url = "http://127.0.0.1";
        isUrl = StringExtensions.isUrl(url);
        assertTrue(isUrl);

        url = "ftp://127.0.0.1/folder";
        isUrl = StringExtensions.isUrl(url);
        assertTrue(isUrl);

        url = "aaa";
        isUrl = StringExtensions.isUrl(url);
        assertFalse(isUrl);

        url = "aaa.bbbb";
        isUrl = StringExtensions.isUrl(url);
        assertFalse(isUrl);
    }

    @Test
    void test_getHost() {
        String url = "http://gitlab.kcloudy.com/demo/net/kcloudy.demotest.net.git";
        String host = StringExtensions.getHost(url);
        assertEquals("gitlab.kcloudy.com", host);

        String domainUrl = StringExtensions.getHostUrl(url);
        assertEquals("http://gitlab.kcloudy.com", domainUrl);

        String domainUrl2 = StringExtensions.getHostUrl("https://gitlab.kcloudy.com/demo/net/kcloudy.demotest.net.git");
        assertEquals("https://gitlab.kcloudy.com", domainUrl2);
    }

    @Test
    void test_getBusNameByHost() {
        String domain = "127.0.0.1";
        String result4 = StringExtensions.getTenantNameByHost(domain);
        assertEquals(TenantConstant.DbaTenantName, result4);
        result4 = StringExtensions.getBusNameByHost(domain);
        assertEquals("", result4);

        domain = "localhost:1001";
        String result5 = StringExtensions.getTenantNameByHost(domain);
        assertEquals(TenantConstant.DbaTenantName, result5);
        result5 = StringExtensions.getBusNameByHost(domain);
        assertEquals("1001", result5);

        domain = "ctest.localhost:1001";
        String result6 = StringExtensions.getTenantNameByHost(domain);
        assertEquals("ctest", result6);
        result6 = StringExtensions.getBusNameByHost(domain);
        assertEquals("1001", result6);

        domain = "sso.kcloudy.com";
        String result7 = StringExtensions.getTenantNameByHost(domain);
        assertEquals(TenantConstant.DbaTenantName, result7);
        result7 = StringExtensions.getBusNameByHost(domain);
        assertEquals("sso", result7);

        domain = "ctest.sso.kcloudy.com";
        String result8 = StringExtensions.getTenantNameByHost(domain);
        assertEquals("ctest", result8);
        result8 = StringExtensions.getBusNameByHost(domain);
        assertEquals("sso", result8);

        domain = "sso.kcloudy.cn";
        String result9 = StringExtensions.getTenantNameByHost(domain);
        assertEquals("sso.kcloudy.cn", result9);
        result9 = StringExtensions.getBusNameByHost(domain);
        assertEquals("sso", result9);
    }

    @Test
    void test_toCamel_toUnderline() {
        String source = "user_name";
        String result = StringExtensions.toCamelCase(source, false);
        assertEquals("userName", result);

        result = StringExtensions.toCamelCase(source, true);
        assertEquals("UserName", result);

        source = "UserName";
        result = StringExtensions.toUnderlineCase(source, false);
        assertEquals("User_Name", result);

        result = StringExtensions.toUnderlineCase(source, true);
        assertEquals("user_name", result);

        source = "userName";
        result = StringExtensions.toUpperFirstCase(source);
        assertEquals("UserName", result);

        source = "UserName";
        result = StringExtensions.toLowerFirstCase(source);
        assertEquals("userName", result);
    }

}
