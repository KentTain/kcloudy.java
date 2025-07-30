package kc.framework.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import kc.framework.extension.StringExtensions;

public class DesProvider {

	// 默认的初始化密钥
	private final static String DefaultKey = "tkGGRmBErvc=";
	private final static String DefaultKeyIV = "Kl7ZgtM1dvQ=";

	/**
	 * 采用DES算法对字符串加密（使用默认的加密字符串）
	 * 
	 * @param message 要加密的字符串
	 * @return String
	 * @throws Exception
	 */
	public static String EncryptString(String message) throws Exception {
		return EncryptString(message, DefaultKey);
	}

	/**
	 * 采用DES算法对字符串加密
	 * 
	 * @param message 要加密的字符串
	 * @param key     算法的密钥，长度为8的倍数，最大长度64
	 * @return String
	 * @throws Exception
	 */
	public static String EncryptString(String message, String key) throws Exception {
		if (StringExtensions.isNullOrEmpty(message))
			throw new IllegalArgumentException("DesProvider.encrypt：传入参数message不能为空。");

		if (StringExtensions.isNullOrEmpty(key))
			throw new IllegalArgumentException("DesProvider.encrypt：传入参数key不能为空。");

		// 将密钥转换成字节数组
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		// 设置初始化向量
		byte[] keyIV = DefaultKeyIV.getBytes(StandardCharsets.UTF_8);
		// 将加密字符串转换成UTF8编码的字节数组
		byte[] inputByteArray = message.getBytes(StandardCharsets.UTF_8);
		// byte[] inputByteArray = convertHexString(message);

		// 检查密钥数组长度是否是8的倍数并且长度是否小于64
		keyBytes = CheckByteArrayLength(keyBytes);
		// 检查初始化向量数组长度是否是8的倍数并且长度是否小于64
		keyIV = CheckByteArrayLength(keyIV);

		DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(keyIV);

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		byte[] bytes = cipher.doFinal(inputByteArray);

		return Base64Provider.Encode(bytes);
		//return donetEncodeString(Base64Provider.Encode(bytes));
		// return toHexString(bytes);
	}

	/**
	 * 采用DES算法对字符串解密（使用默认的加密字符串）
	 * 
	 * @param message 要解密的字符串
	 * @return String
	 * @throws Exception
	 */
	public static String DecryptString(String message) throws Exception {
		return DecryptString(message, DefaultKey);
	}

	/**
	 * 采用DES算法对字符串解密
	 * 
	 * @param message 要解密的字符串
	 * @param key     算法的密钥，长度为8的倍数，最大长度64
	 * @return String
	 * @throws Exception
	 */
	public static String DecryptString(String message, String key) throws Exception {
		if (StringExtensions.isNullOrEmpty(message))
			throw new IllegalArgumentException("DesProvider.DecryptString：传入参数message不能为空。");

		if (StringExtensions.isNullOrEmpty(key))
			throw new IllegalArgumentException("DesProvider.DecryptString：传入参数key不能为空。");

		// byte[] bytesrc = message.getBytes(StandardCharsets.UTF_8);
		// byte[] bytesrc = convertHexString(message);
		byte[] bytesrc = Base64Provider.Decode(message);
		//byte[] bytesrc = Base64Provider.Decode(donetDecodeString(message));
		
		// 将密钥转换成字节数组
		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
		// 设置初始化向量
		byte[] keyIV = DefaultKeyIV.getBytes(StandardCharsets.UTF_8);

		// 检查密钥数组长度是否是8的倍数并且长度是否小于64
		keyBytes = CheckByteArrayLength(keyBytes);
		// 检查初始化向量数组长度是否是8的倍数并且长度是否小于64
		keyIV = CheckByteArrayLength(keyIV);

		DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(keyIV);

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}

	public static String donetEncodeString(String source) {
		return source.replace("_", "/").replace("-", "+").replace("\r\n", "");
	}

	public static String donetDecodeString(String source) {
		return source.replace("/", "_").replace("+", "-").replace("\r\n", "");
	}

	/**
	 * 检查密钥或初始化向量的长度，如果不是8的倍数或长度大于64则截取前8个元素
	 * 
	 * @param byteArray 要检查的数组
	 * @return byte[]
	 */
	private static byte[] CheckByteArrayLength(byte[] byteArray) {
		byte[] resultBytes = new byte[8];
		// 如果数组长度小于8
		if (byteArray.length < 8) {
			byte[] defaultByteArry = DefaultKey.getBytes(StandardCharsets.UTF_8);
			byteArray = MergeBytes(byteArray, defaultByteArry);
		}

		System.arraycopy(byteArray, 0, resultBytes, 0, 8);
		return resultBytes;
	}

	private static byte[] MergeBytes(byte[] data1, byte[] data2) {
		byte[] all = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, all, 0, data1.length);
		System.arraycopy(data2, 0, all, data1.length, data2.length);

		return all;
	}
}
