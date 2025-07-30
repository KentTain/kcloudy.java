package kc.storage.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kc.framework.extension.StringExtensions;
import kc.framework.security.Base64Provider;
import kc.framework.security.Rfc2898DeriveBytes;

@lombok.extern.slf4j.Slf4j
public class Encryption {
	private final static String SECRETE_ENCRYPTION_KEY = "saltIsGoodForYou";

	public static String GetEncryptionKey(String encryptionKey, boolean isUserLevelBlob, boolean isInternal,
			String userId) {
		if (StringExtensions.isNullOrEmpty(encryptionKey)) {
			return null; // Do not encrypt
		}

		String encryptKey = encryptionKey;
		if (isUserLevelBlob) {
			encryptKey += "|" + (isInternal ? "Internal" : userId);
		}
		return encryptKey;
	}

	public static String Encrypt(String value, String key) {
		if (StringExtensions.isNullOrEmpty(value))
			return "";

		try {
			byte[] valueBytes = value.getBytes("UTF-8");
			return Base64Provider.Encode(Encrypt(valueBytes, key));
		} catch (Exception e) {
			log.error("kc.storage.util.Encrypt throw: ", e);
			return "";
		}
	}

	public static byte[] Encrypt(byte[] data, String key) {
		if (data == null)
			return null;

		String encryptionKey = StringExtensions.isNullOrEmpty(key) 
				? SECRETE_ENCRYPTION_KEY
				: SECRETE_ENCRYPTION_KEY + "|" + key;

		try {
			byte[] saltBytes = encryptionKey.getBytes("UTF-8");

			// We're using the PBKDF2 standard for password-based key generation
			Rfc2898DeriveBytes keyGenerator = new Rfc2898DeriveBytes("thePassword", saltBytes, 1000);
			byte[] bKey = keyGenerator.getBytes(32);
			byte[] bIv = keyGenerator.getBytes(16);

			SecretKeySpec skeySpec = new SecretKeySpec(bKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
			IvParameterSpec _iv = new IvParameterSpec(bIv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, _iv);
			byte[] encryptBytes = cipher.doFinal(data);
			return encryptBytes;
		} catch (Exception e) {
			log.error("kc.storage.util.Encrypt throw: ", e);
			return null;
		}
	}

	public static String Encrypt(String input) {
		if (input == null)
			return null;

		String encryptedString;
		try {
			byte[] binaryData = input.getBytes("UTF-8");
			byte[] encryptBytes = Encrypt(binaryData, null);

			encryptedString = Base64Provider.Encode(encryptBytes);
			return encryptedString;
		} catch (Exception e) {
			log.error("kc.storage.util.Encrypt throw: ", e);
			return null;
		}

	}

	public static byte[] Decrypt(byte[] data, String publicKey) {
		if (data == null)
			return null;

		try {
			String encryptionKey = StringExtensions.isNullOrEmpty(publicKey) 
					? SECRETE_ENCRYPTION_KEY
					: SECRETE_ENCRYPTION_KEY + "|" + publicKey;

			byte[] saltBytes = encryptionKey.getBytes("UTF-8");
			// We're using the PBKDF2 standard for password-based key generation
			Rfc2898DeriveBytes keyGenerator = new Rfc2898DeriveBytes("thePassword", saltBytes, 1000);
			byte[] bKey = keyGenerator.getBytes(32);
			byte[] bIv = keyGenerator.getBytes(16);

			SecretKeySpec skeySpec = new SecretKeySpec(bKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec _iv = new IvParameterSpec(bIv);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, _iv);
			byte[] decryptBytes = cipher.doFinal(data);

			return decryptBytes;
		} catch (Exception e) {
			log.error("kc.storage.util.Decrypt throw: ", e);
			return null;
		}
	}

	public static String Decrypt(String base64Input) {
		if (base64Input == null)
			return null;

		try {
			byte[] encryptBytes = Base64Provider.Decode(base64Input);
			byte[] decryptBytes = Decrypt(encryptBytes, null);

			String decryptedString = new String(decryptBytes, StandardCharsets.UTF_8);
			return decryptedString;
		} catch (Exception e) {
			log.error("kc.storage.util.Decrypt throw: ", e);
			return null;
		}
	}

	public static String Decrypt(String base64Input, String key) {
		byte[] encryptBytes = Base64Provider.Decode(base64Input);

		byte[] decryptBytes = Decrypt(encryptBytes, key);

		String decryptedString = new String(decryptBytes, StandardCharsets.UTF_8);

		return decryptedString;
	}

	/**
	 * 构建密钥字节码
	 * 
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] GeneralKey(String keyStr) throws Exception {
		byte[] bytes = keyStr.getBytes("utf-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(bytes);
		return md.digest();
	}

	/**
	 * 构建加解密向量字节码
	 * 
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] GeneralIv(String keyStr) throws Exception {
		byte[] bytes = keyStr.getBytes("utf-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(bytes);
		return md.digest();
	}
}
