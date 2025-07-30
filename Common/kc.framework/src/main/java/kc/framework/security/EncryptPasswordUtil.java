package kc.framework.security;

import java.util.Random;

import kc.framework.extension.StringExtensions;

@lombok.extern.slf4j.Slf4j
public class EncryptPasswordUtil {
	public final static String DEFAULT_Key = "KCloudy-Microsoft-EncryptKey";

	public static String EncryptPassword(String password, String key) {
		try {
			if (StringExtensions.isNullOrEmpty(key))
				return DesProvider.EncryptString(password, DEFAULT_Key);

			return DesProvider.EncryptString(password, key);
		} catch (Exception e) {
			log.error(String.format("EncryptPasswordUtil: 【{%s}】--【{%s}】 throw: {%s}",key, password, e.getMessage()));
			return "";
		}
	}

	public static String DecryptPassword(String password, String key) {
		try {
			if (StringExtensions.isNullOrEmpty(key))
				return DesProvider.DecryptString(password, DEFAULT_Key);

			return DesProvider.DecryptString(password, key);
		} catch (Exception e) {
			log.error(e.getMessage());
			return "";
		}
	}

	/**生成随机字符串
     * @param len 随机字符串长度（最小值为：8）
     * @return String
     */
    public static String GetRandomString(int len)
    {
    	if (len < 8)
            len = 8;
    	
    	Random ra = new Random();
    	int first = ra.ints(65, 90).limit(10).findFirst().getAsInt();
    	int last = ra.ints(97, 123).limit(10).findFirst().getAsInt();
    	int intStr = ra.ints(0, 9).findFirst().getAsInt();
    	String uuidStr = java.util.UUID.randomUUID().toString().substring(0, len - 3);
        return String.format("%s%s%s%s", toASCII(first), intStr, uuidStr,  toASCII(last));
    }
    
    private static String toASCII(int value) {
        
        return Character.toString((char) value);
    }
}
