package kc.storage.util;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.parallel.Execution;
import org.springframework.test.context.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

import kc.storage.util.Encryption;

class EncryptionTest {

	@Test
	void test_Encryption() {
		String encryptionStr = "测试加密Bytes的字符串";
		String key = "dev-cfwin-EncryptKey";
		String realEncryptionKey = Encryption.GetEncryptionKey(key, false, false, null);

		String encryptResultWithNet = "Nk+eE2IF4x+2Jby3LVuyYGwCW9PY6h+5B7ADsIMBIzk=";
		String encryptResult = Encryption.Encrypt(encryptionStr, realEncryptionKey);
		String decryptResult = Encryption.Decrypt(encryptResult, realEncryptionKey);
		
		System.out.println("---test_Encryption realEncryptionKey: " + realEncryptionKey);
		System.out.println("---test_Encryption encryptResult: " + encryptResult);
		System.out.println("---test_Encryption encryptResultWithNet: " + encryptResultWithNet);
		System.out.println("---test_Encryption decryptResult: " + decryptResult);

        assertEquals(encryptionStr, decryptResult);
        assertEquals(encryptResult, encryptResultWithNet);
	}

}
