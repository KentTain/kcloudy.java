package kc.framework.extension;

import kc.framework.tenant.TenantConstant;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringExtensions {
    /**
     * 根据访问的url地址返回host
     *
     * @param url Url地址，如：http://www.xxxx.com/aaa/bbb.html
     * @return host，如：www.xxxx.com
     */
    public static String getHost(String url) {
        if (isNullOrEmpty(url))
            return null;

        url = url.toLowerCase();
        if (!(url.startsWith("http://") || url.startsWith("https://"))) {
            url = "http://" + url;
        }

        String returnVal = "";
        try {
            Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
            Matcher m = p.matcher(url);
            if (m.find()) {
                returnVal = m.group();
            }
        } catch (Exception ignored) {
        }
        if ((returnVal.endsWith(".html") || returnVal.endsWith(".htm"))) {
            returnVal = "";
        }
        return returnVal;
    }

    /**
     * 根据访问的url地址返回域名url地址
     *
     * @param url Url地址，如：http://www.xxxx.com/aaa/bbb.html
     * @return 域名url地址，如：http://www.xxxx.com
     */
    public static String getHostUrl(String url) {
        String host = getHost(url);
        if (isNullOrEmpty(host))
            return null;

        return url.startsWith("http://")
                ? "http://" + host
                : url.startsWith("https://")
                ? "https://" + host
                : url;
    }

    /**
     * 根据请求的的Host获取TenantName </br>
     * 例如：localhost：1001 为 cDba 的本地域名的租户代码 </br>
     * cdba.localhost：1001 为 cDba 的本地域名的租户代码 </br>
     * sso.kcloudy.com 为 cDba 的租户域名的租户代码 </br>
     * cdba.kcloudy.com 为 cDba 的租户域名的租户代码</br>
     * sso.xxx.com 为 sso.xxx.com 的独立域名的租户代码
     *
     * @param requestHost
     * @return 请求的Host的租户代码
     */
    public static String getTenantNameByHost(String requestHost) {
        if (isNullOrEmpty(requestHost))
            return null;
        if (requestHost.contains("127.0.0.1"))
            return TenantConstant.DbaTenantName;
        boolean isLocal = requestHost.toLowerCase().contains("localhost");
        boolean isKCloudy = requestHost.toLowerCase().contains("kcloudy.com");
        requestHost = requestHost.replace("https://", "").replace("http://", "");
        String[] urlTenantSplits = requestHost.split("\\.");
        if (isLocal) {
            if (urlTenantSplits.length == 1) {
                //顶级域名，例如：localhost:1001
                return TenantConstant.DbaTenantName;
            } else if (urlTenantSplits.length > 1) {
                //二级域名，例如：cdba.localhost:1001
                return urlTenantSplits[0];
            }
        } else if (isKCloudy) {
            if (urlTenantSplits.length == 4) {
                //三级级域名，例如：cdba.sso.kcloudy.com
                return urlTenantSplits[0];
            } else if (urlTenantSplits.length < 4) {
                //二级域名，例如：sso.kcloudy.com
                return TenantConstant.DbaTenantName;
            }
        }
        return requestHost;
    }

    /**
     * 获取域名中的业务代码 </br>
     * 例如：http://cdba.doc.kcoudy.com 为 doc 的本地域名业务代码 </br>
     * http://cdba.localhost:2001 为 2001 的租户域名业务代码 </br>
     * http://doc.xxx.com 为 doc 的独立域名业务代码
     *
     * @param requestHost
     * @return 请求的Host的租户代码
     */
    public static String getBusNameByHost(String requestHost) {
        if (isNullOrEmpty(requestHost))
            return "";
        if (requestHost.contains("127.0.0.1"))
            return "";
        boolean isLocal = requestHost.toLowerCase().contains("localhost");
        boolean isKCloudy = requestHost.toLowerCase().contains("kcloudy.com");
        requestHost = requestHost.replace("https://", "").replace("http://", "");
        requestHost = trimEndSlash(requestHost);
        if (isLocal) {
            String[] urlPortSplits = requestHost.split(":");
            //返回端口号
            if (urlPortSplits.length == 2) {
                //域名，例如：localhost:1001、cdba.localhost:1001
                return urlPortSplits[1];
            } else {
                //二级域名，例如：cdba.localhost
                return "";
            }
        } else if (isKCloudy) {
            String[] urlTenantSplits = requestHost.split("\\.");
            if (urlTenantSplits.length == 4) {
                //三级级域名，例如：cdba.sso.kcloudy.com
                return urlTenantSplits[1];
            } else {
                //二级域名，例如：sso.kcloudy.com
                return urlTenantSplits[0];
            }
        } else {
            String[] urlTenantSplits = requestHost.split("\\.");
            //独立域名，例如：sso.xxx.com、sso.xxx.xxx.com
            if (urlTenantSplits.length >= 3)
                return urlTenantSplits[0];
            else
                return "";
        }
    }

    /**
     * 移除从startIndex到endIndex的字符串
     *
     * @param str
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String remove(String str, int startIndex, int endIndex) {
        StringBuilder builder = new StringBuilder(str);
        return builder.delete(startIndex, endIndex).toString();
    }

    /**
     * String左对齐
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * String右对齐
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * 判断字符是否为空
     *
     * @param str 判断是否为空的字符串
     * @return boolean
     * @author tianc
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null || "".equals(str));
    }

    /**
     * 获取座机号中的分机号 </br>
     * 例如：获取座机号82680051-9003的分机号吗：9003
     *
     * @param text
     * @return String
     * @author tianc
     */
    public static String getExtensionNumber(String text) {
        if (isNullOrEmpty(text))
            return "";

        String[] tels = text.split("-");
        if (tels.length > 0)
            return tels[0];
        return "";
    }


    /**
     * 替换最后字符
     *
     * @param text            需要替换的字符串
     * @param strToReplace    需要替换的旧字符
     * @param replaceWithThis 需要替换的新字符
     * @return String
     * @author tianc
     */
    public static String replaceLast(String text, String strToReplace, String replaceWithThis) {
        if (isNullOrEmpty(text))
            return "";

        return text.replaceFirst("(?s)" + strToReplace + "(?!.*?" + strToReplace + ")", replaceWithThis);
    }

    /**
     * 将列表中每个项进行加工后返回新的列表（for List） </br>
     * List<String> stringList = Arrays.asList("1","2","3"); </br>
     * List<Integer> integerList = convertList(stringList, s ->
     * Integer.parseInt(s));
     *
     * @param <T>  源列表数据类型：例如 List<String>
     * @param <U>  结果列表数据类型：例如 List<String>
     * @param from 源列表
     * @param func 数据转换的lambda表达式
     * @return List<U>
     * @author tianc
     */
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    /**
     * 将列表中每个项进行加工后返回新的列表（for arrays） </br>
     * String[] stringArr = {"1","2","3"}; </br>
     * Double[] doubleArr = convertArray(stringArr, Double::parseDouble,
     * Double[]::new);
     *
     * @param <T>       源列表数据类型：例如 String[]
     * @param <U>       结果列表数据类型：例如 String[]
     * @param from      源列表
     * @param func      数据转换的lambda表达式
     * @param generator
     * @return U[]
     * @author tianc
     */
    public static <T, U> U[] convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }

    /**
     * 将aaa,bbb,ccc转换为列表项 String[]{aaa,bbb,ccc}
     *
     * @param text aaa,bbb,ccc
     * @return String[]
     * @author tianc
     */
    public static String[] arrayFromCommaDelimitedStrings(String text) {
        if (isNullOrEmpty(text))
            return new String[]{};

        String[] tokens = text.trim().split(",");
        List<String> tokenList = Arrays.asList(tokens).stream().map(m -> m.trim()).filter(m -> !isNullOrEmpty(m))
                .collect(Collectors.toList());

        return tokenList.toArray(new String[tokenList.size()]);
    }

    /**
     * 将aaa,bbb,ccc转换为列表项 String[]{aaa,bbb,ccc}
     *
     * @param text      aaa,bbb,ccc 或 aaa-bbb-ccc
     * @param separator 分隔符
     * @return String[]
     * @author tianc
     */
    public static String[] arrayFromCommaDelimitedStringsBySplitChar(String text, String separator) {
        if (isNullOrEmpty(text))
            return new String[]{};

        separator = handlerSpecialSplitChar(separator);

        String[] tokens = text.trim().split(separator);
        List<String> tokenList = Arrays.asList(tokens).stream().map(m -> m.trim()).filter(m -> !isNullOrEmpty(m))
                .collect(Collectors.toList());
        return tokenList.toArray(new String[tokenList.size()]);
    }

    /**
     * 将字符串：1,2,3 转换为列表项 int[]{1,2,3}
     *
     * @param text 字符串：1,2,3
     * @return Integer[]
     * @author tianc
     */
    public static Integer[] arrayFromCommaDelimitedIntegers(String text) {
        if (isNullOrEmpty(text))
            return new Integer[]{};

        String[] tokens = text.trim().split(",");
        Integer[] result = convertArray(tokens, Integer::parseInt, Integer[]::new);

        return result;
    }

    /**
     * 将字符串：1|2|3 转换为列表项 int[]{1,2,3}
     *
     * @param text      1,2,3 或者 1-2-3
     * @param separator 分隔符
     * @return Integer[]
     * @author tianc
     */
    public static Integer[] arrayFromCommaDelimitedIntegersBysplitChar(String text, String separator) {
        if (isNullOrEmpty(text))
            return new Integer[]{};

        separator = handlerSpecialSplitChar(separator);

        String[] tokens = text.trim().split(separator);
        Integer[] result = convertArray(tokens, Integer::parseInt, Integer[]::new);

        return result;
    }

    /**
     * 根据Key值获取连接字符串对应的值，例如：
     * 将AccessId=xxxxxx;AccessKey=yyyyy;  获取AccessId的值：xxxxxx
     *
     * @param connectionString 接字符串：AccessId=xxxxxx;AccessKey=yyyyy;
     * @param key key值：AccessId
     * @return String
     * @author tianc
     */
    public static String getValueFromConnectionString(String connectionString, String key) {
        if (isNullOrEmpty(connectionString))
            return null;

        Dictionary<String, String> keyValuePair = keyValuePairFromConnectionString(connectionString);
        if (null == keyValuePair)
            return null;

        return keyValuePair.get(key);
    }

    /**
     * 将AccessId=xxxxxx;AccessKey=xxxxxx;转换为列表项 Dictionary<String, String>()
     *
     * @param connectionString
     * @return Dictionary<String, String>
     * @author tianc
     */
    public static Dictionary<String, String> keyValuePairFromConnectionString(String connectionString) {
        if (isNullOrEmpty(connectionString))
            return null;

        String[] keyPairs = connectionString.split(";");
        Dictionary<String, String> result = new Hashtable<String, String>();
        for (String keyPair : keyPairs) {
            if (!isNullOrEmpty(keyPair)) {
                if (keyPair.contains("=")) {
                    int index = keyPair.indexOf("=");
                    String key = keyPair.substring(0, index).toLowerCase();
                    String value = keyPair.substring(index + 1);
                    result.put(key, value);
                } else if (keyPair.contains("//")) {
                    int index = keyPair.indexOf("//");
                    String key = keyPair.substring(0, index).toLowerCase();
                    String value = keyPair.substring(index + 2);
                    result.put(key, value);
                }
            }
        }

        return result;
    }

    /**
     * 将AccessId=xxxxxx;AccessKey=xxxxxx;转换为列表项 Dictionary<String, String>()
     *
     * @param connectionString
     * @param firstSplitChar   例如：";"
     * @param secondSplitChar  例如："="
     * @return Dictionary<String, String>
     * @author tianc
     */
    public static Dictionary<String, String> keyValuePairFromConnectionString(String connectionString,
                                                                              String firstSplitChar, String secondSplitChar) {
        if (isNullOrEmpty(connectionString))
            return null;

        firstSplitChar = handlerSpecialSplitChar(firstSplitChar);
        secondSplitChar = handlerSpecialSplitChar(secondSplitChar);

        String[] keyPairs = connectionString.split(firstSplitChar);
        Dictionary<String, String> result = new Hashtable<String, String>();
        for (String keyPair : keyPairs) {
            if (!isNullOrEmpty(keyPair)) {
                int index = keyPair.indexOf(secondSplitChar);

                String key = keyPair.substring(0, index).toLowerCase();
                String value = keyPair.substring(index + 1);
                result.put(key, value);
            }
        }

        return result;
    }

    private static String handlerSpecialSplitChar(String splitChar) {
        if (splitChar.equals("."))
            splitChar = "\\.";

        if (splitChar.equals("|"))
            splitChar = "\\|";

        return splitChar;
    }

    /**
     * 根据url获取Domain
     *
     * @param url 输入的url，例如：http(s)://ctest.domain.com/controller/action?query=
     * @return String 域名，例如：ctest.domain.com
     * @author tianc
     */
    public static String getDomainNameFromUrl(String url) {
        if (isNullOrEmpty(url))
            return "";

        if (!isUrl(url))
            return "";

        try {
            java.net.URI uri = new java.net.URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return "";
        }
    }

    /**
     * 根据url获取租户代码：TenantName
     *
     * @param url 输入的url，例如：http(s)://ctest.domain.com/controller/action?query=
     * @return String 租户代码：TenantName，例如：ctest
     * @author tianc
     */
    public static String getTenantNameFromUrl(String url) {
        if (isNullOrEmpty(url))
            return "";

        if (!isUrl(url))
            return "";

        String domain = getDomainNameFromUrl(url);
        if (isNullOrEmpty(domain))
            return "";

        String[] splitDomains = domain.split("\\.");
        return splitDomains[0];
    }

    /**
     * 将中文字符转化Unicode编码
     *
     * @param str
     * @return String
     * @author tianc
     */
    public static String chineseToUnicode(String str) {
        if (isNullOrEmpty(str))
            return "";

        char[] chars = str.toCharArray();
        String returnStr = "";
        for (int i = 0; i < chars.length; i++) {
            returnStr += "\\u" + Integer.toString(chars[i], 16);
        }
        return returnStr;
    }

    /**
     * 将Unicode编码字符转化中文
     *
     * @param str
     * @return String
     * @author tianc
     */
    public static String unicodeToChinese(String str) {
        if (isNullOrEmpty(str))
            return "";

        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格 */
        String[] strs = str.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    /**
     * 验证身份证是否合法
     *
     * @param idCard 要验证的身份证
     * @return boolean
     * @author tianc
     */
    public static boolean isIdCard(String idCard) {
        // 如果为空，认为验证合格
        if (isNullOrEmpty(idCard)) {
            return false;
        }

        // 清除要验证字符串中的空格
        idCard = idCard.trim();

        // 模式字符串
        StringBuilder rule = new StringBuilder();
        rule.append("^(11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|");
        rule.append("50|51|52|53|54|61|62|63|64|65|71|81|82|91)");
        rule.append("(\\d{13}|\\d{15}[\\dx])$");

        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(idCard);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证EMail是否合法
     *
     * @param email 要验证的Email
     * @return boolean
     * @author tianc
     */
    public static boolean isEmail(String email) {
        // 如果为空，认为验证不合格
        if (isNullOrEmpty(email)) {
            return false;
        }

        // 清除要验证字符串中的空格
        email = email.trim();

        // 模式字符串
        String rule = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";

        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证银行帐号是否合法
     *
     * @param bankAccount
     * @return boolean
     * @author tianc
     */
    public static boolean isBankAccount(String bankAccount) {
        if (isNullOrEmpty(bankAccount))
            return false;

        bankAccount = bankAccount.trim();

        String rule = "^([1-9]{1})(\\d{14}|\\d{15}|\\d{16}|\\d{17}|\\d{18})$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(bankAccount);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证url是否合法
     *
     * @param url URL地址，实例如下：<br/>
     *            http(s)://www.domai.com/controller/action/query= <br/>
     *            http(s)://ctest.domai.com/controller/action/query= <br/>
     * @return boolean
     * @author tianc
     */
    public static boolean isUrl(String url) {
        if (isNullOrEmpty(url))
            return false;

        if (url.contains("localhost") || url.contains("127.0.0.1"))
            return true;

        url = url.trim();

        String rule = "^(https?|ftp):\\/\\/(((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:)*)?(((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]))|((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?)(:\\d*)?)(\\/((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|)+(\\/(([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|)*)*)?)?(\\?((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|)|[\\uE000-\\uF8FF]|\\/|\\?)*)?(\\#((([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(%[\\da-f]{2})|[!\\$&'\\(\\)\\*\\+,;=]|:|)|\\/|\\?)*)?$";

        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证座机号
     *
     * @param phone 座机号
     * @return boolean
     * @author tianc
     */
    public static boolean isTelephone(String phone) {
        if (isNullOrEmpty(phone))
            return false;

        String rule = "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return boolean
     * @author tianc
     */
    public static boolean isMobile(String phone) {
        if (isNullOrEmpty(phone))
            return false;

        String rule = "^(13|14|15|17|18)\\d{9}$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证手机号
     *
     * @param phone 手机号
     * @return boolean
     * @author tianc
     */
    public static boolean isTelOrMobile(String phone) {
        return isTelephone(phone) || isMobile(phone);
    }

    /**
     * 验证QQ号
     *
     * @param qq QQ号
     * @return boolean
     * @author tianc
     */
    public static boolean isQQ(String qq) {
        if (isNullOrEmpty(qq))
            return false;

        String rule = "^[1-9]\\d{4,11}$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(qq);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    public static boolean engNum(String engnum) {
        if (isNullOrEmpty(engnum))
            return false;

        String rule = "^[0-9a-zA-Z]*$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(engnum);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    public static boolean lenEngNum(String lenEngNum, int length) {
        if (isNullOrEmpty(lenEngNum))
            return false;

        return length >= lenEngNum.length() && engNum(lenEngNum);
    }

    /**
     * 验证IP地址是否合法
     *
     * @param ip 要验证的IP地址
     * @return boolean
     * @author tianc
     */
    public static boolean isIP(String ip) {
        // 如果为空，认为验证合格
        if (isNullOrEmpty(ip)) {
            return true;
        }

        // 清除要验证字符串中的空格
        ip = ip.trim();

        // 模式字符串
        String rule = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ip);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证是否为数字
     *
     * @param number 要验证的数字
     * @return boolean
     * @author tianc
     */
    public static boolean isNumber(String number) {
        // 如果为空，认为验证不合格
        if (isNullOrEmpty(number)) {
            return false;
        }

        // 清除要验证字符串中的空格
        number = number.trim();

        // 模式字符串
        String rule = "^[0-9]+[0-9]*[.]?[0-9]*$";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /// <summary>
    /// HTML转行成TEXT
    /// </summary>
    /// <param name="strHtml"></param>
    /// <returns></returns>
    public static String htmlToTxt(String strHtml) {
        if (isNullOrEmpty(strHtml))
            return "";

        String[] aryReg = {"<script[^>]*?>.*?</script>",
                "<(\\/\\s*)?!?((\\w+:)?\\w+)(\\w+(\\s*=?\\s*(([\"\"'])(\\\\[\"\"'tbnr]|[^\\7])*?\\7|\\w+)|.{0})|\\s)*?(\\/\\s*)?>",
                "([\\r\\n])[\\s]+", "&(quot|#34);", "&(amp|#38);", "&(lt|#60);", "&(gt|#62);", "&(nbsp|#160);",
                "&(iexcl|#161);", "&(cent|#162);", "&(pound|#163);", "&(copy|#169);", "&#(\\d+);", "-->", "<!--.*\\n"};

        // String newReg = aryReg[0];
        String strOutput = strHtml;
        for (int i = 0; i < aryReg.length; i++) {
            // Regex regex = new Regex(aryReg[i], RegexOptions.IgnoreCase);
            // strOutput = regex.Replace(strOutput, "");
        }

        strOutput.replace("<", "");
        strOutput.replace(">", "");
        strOutput.replace("\r\n", "");

        return strOutput;
    }

    /**
     * 把手机号码8位账户替换为*
     *
     * @param source
     * @return String
     * @author tianc
     */
    public static String toHideMobile(String source) {
        if (source == null || source.length() != 11)
            return source;
        return source.substring(0, 3) + "********";
    }

    /**
     * 前台显示邮箱的掩码替换(由tzhqq.com等替换成t*****qq.com)
     *
     * @param email 邮箱
     * @return String
     * @author tianc
     */
    public static String toHideEmail(String email) {
        if (isNullOrEmpty(email))
            return "";

        String strArg = "";
        String SendEmail = "";

        String rule = "(\\\\w)\\\\w+";
        // 验证
        Pattern pattern = Pattern.compile(rule.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) {
            strArg = matcher.group(1) + "*****";
            SendEmail = email.replaceFirst("\\w+", strArg);
        } else
            SendEmail = email;
        return SendEmail;
    }

    /**
     * 下划线转驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     *
     * @param underlineStr     带有下划线的字符串
     * @param isUpperFirstCase 首字母是否大写
     * @return 驼峰字符串
     */
    public static String toCamelCase(String underlineStr, boolean isUpperFirstCase) {
        if (isNullOrEmpty(underlineStr))
            return "";
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return isUpperFirstCase
                ? toUpperFirstCase(buffer.toString())
                : buffer.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @param isLowerCase  是否小写下划线字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr, boolean isLowerCase) {
        if (isNullOrEmpty(camelCaseStr))
            return "";
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (i != 0 && charArray[i] >= 65 && charArray[i] <= 90) {
                if (isLowerCase)
                    buffer.append("_").append(charArray[i] += 32);
                else
                    buffer.append("_").append(charArray[i]);
            } else {
                buffer.append(charArray[i]);
            }
        }

        return isLowerCase
                ? buffer.toString().toLowerCase()
                : buffer.toString();
    }

    /**
     * ⾸字母⼤写(进⾏字母的ascii编码前移，效率是最⾼的)
     *
     * @param source 需要转化的字符串
     */
    public static String toUpperFirstCase(String source) {
        char[] chars = source.toCharArray();
        chars[0] -= 32;
        return String.valueOf(chars);
    }

    /**
     * ⾸字母⼤写(进⾏字母的ascii编码前移，效率是最⾼的)
     *
     * @param source 需要转化的字符串
     */
    public static String toLowerFirstCase(String source) {
        char[] chars = source.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public static boolean isOwnDomain(String source) {
        if (isNullOrEmpty(source))
            return false;

        boolean isOwnDomain = !source.endsWith("cfwin.com") || !source.endsWith("starlu.com");
        return isOwnDomain;
    }

    /**
     * 获取以/结尾的Url路径， </br>
     * 例如：www.xxxx.com/
     *
     * @param text
     * @return String
     * @author tianc
     */
    public static String endWithSlash(String text) {
        if (isNullOrEmpty(text))
            return "";

        return text.endsWith("/") ? text : text + "/";
    }

    /**
     * 获取以无/结尾的Url路径，例如：www.xxxx.com
     *
     * @param source 传入字符串
     * @return String
     * @author tianc
     */
    public static String trimEnd(String source, String suffix) {
        if (isNullOrEmpty(source))
            return "";

        while (source.endsWith(suffix)) {
            source = source.substring(0, source.length() - suffix.length());
        }

        return source;
    }

    /**
     * 获取以无/结尾的Url路径，例如：www.xxxx.com
     *
     * @param source 传入字符串
     * @return String
     * @author tianc
     */
    public static String trimEndSlash(String source) {
        if (isNullOrEmpty(source))
            return "";

        return source.endsWith("/") ? trimEnd(source, "/") : source;
    }

    /**
     * 小数去掉末尾的0和.
     *
     * @param source 传入字符串
     * @return String
     * @author tianc
     */
    public static String trimEndZero(String source) {
        if (isNullOrEmpty(source))
            return "";

        return trimEnd(source, "0.");
    }

    /**
     * 字符串右补空格
     * 该方法默认采用空格(其ASCII码为32)来右补字符
     */
    public static String rightPadForBlank(String str, int size) {
        return rightPadForByte(str, size, 32);
    }

    /**
     * 字符串右补0
     *
     * @param str
     * @param size
     */
    public static String rightPadForZero(String str, int size) {
        return rightPadForByte(str, size, 48);
    }

    /**
     * 字符串右补字符
     * 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
     * 若对普通字符串进行右补字符,建议org.apache.commons.lang.StringUtils.rightPad(...)
     *
     * @param size
     * @param padStrByASCII 该值为所补字符的ASCII码,如
     *                      32表示空格,
     *                      48表示0,
     *                      64表示@等
     */
    public static String rightPadForByte(String str, int size, int padStrByASCII) {
        byte[] srcByte = str.getBytes();
        byte[] destByte = null;
        if (srcByte.length >= size) {
            //由于size不大于原数组长度,故该方法此时会按照size自动截取,它会在数组右侧填充'(byte)0'以使其具有指定的长度
            destByte = Arrays.copyOf(srcByte, size);
        } else {
            destByte = Arrays.copyOf(srcByte, size);
            Arrays.fill(destByte, srcByte.length, size, (byte) padStrByASCII);
        }
        return new String(destByte);
    }

    /**
     * 字符串左补空格
     * 该方法默认采用空格(其ASCII码为32)来左补字符
     */
    public static String leftPadForBlank(String str, int size) {
        return leftPadForByte(str, size, 32);
    }

    /**
     * 字符串左补0
     *
     * @param str
     * @param size
     * @return
     */
    public static String leftPadForZero(String str, int size) {
        return leftPadForByte(str, size, 48);
    }

    /**
     * 字符串左补字符
     * 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
     *
     * @param padStrByASCII 该值为所补字符的ASCII码,如
     *                      32表示空格,
     *                      48表示0,
     *                      64表示@等
     */
    public static String leftPadForByte(String str, int size, int padStrByASCII) {
        byte[] srcByte = str.getBytes();
        byte[] destByte = new byte[size];
        Arrays.fill(destByte, (byte) padStrByASCII);
        if (srcByte.length >= size) {
            System.arraycopy(srcByte, 0, destByte, 0, size);
        } else {
            System.arraycopy(srcByte, 0, destByte, size - srcByte.length, srcByte.length);
        }
        return new String(destByte);
    }
}
