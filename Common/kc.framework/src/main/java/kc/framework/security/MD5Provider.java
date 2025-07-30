package kc.framework.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kc.framework.extension.StringExtensions;

public class MD5Provider {
	private MD5Provider() {
	}

	/**
	 * 计算指定字符串的MD5哈希值
	 * 
	 * @param message 要进行哈希计算的字符串
	 * @param isLower 是否小写字母
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String Hash(String message, boolean isLower)
			throws NoSuchAlgorithmException {
		if (StringExtensions.isNullOrEmpty(message)) {
			return "";
		} else {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(message.getBytes(StandardCharsets.UTF_8));
			byte[] targetData = md5.digest();

			String pwd = new BigInteger(1, targetData).toString(16);
			if (pwd.length() % 2 == 1) {
				pwd = "0" + pwd;
			}
			int length = pwd.length();
			StringBuffer hex = new StringBuffer(length + length / 2 - 1);
			for (int i = 0; i < length; i += 2) {
				hex.append(pwd.substring(i, i + 2));
			}
			
			if (isLower)
				return hex.toString().toLowerCase();
			return hex.toString().toUpperCase();
		}
	}
}
