package kc.framework.security;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DesProviderTest {

	@Test
	void test_DesProvider() throws Exception {
		String key = "12345678";
		String except = "L3c132f119l";

		String encryptString = DesProvider.EncryptString(except, key);
		String encryptStringWithNet = "cE5D920Mea2yKtdND0c1pQ=="; // C#
		String decryptString = DesProvider.DecryptString(encryptString, key);

		//System.out.println("test_DesProvider EncryptString: " + encryptString);
		//System.out.println("test_DesProvider DecryptString: " + decryptString);
		assertEquals(except, decryptString);
		assertEquals(encryptString, encryptStringWithNet);
		//System.out.println();
	}

	@Test
	void test_Tenant_DesProvider() throws Exception {
		String key = "kcloudy-microsoft-encrypt-key";
		String except = "P@ssw0rd";

		String encryptString = DesProvider.EncryptString(except, key);
		String encryptStringWithNet = "ReSLVpe326WjPEmMrFVXWQ=="; // C#
		String decryptString = DesProvider.DecryptString(encryptString, key);

		//System.out.println("test_Tenant_DesProvider EncryptString: " + encryptString);
		//System.out.println("test_Tenant_DesProvider DecryptString: " + decryptString);
		assertEquals(except, decryptString);
		assertEquals(encryptString, encryptStringWithNet);
		//System.out.println();
	}

	@Test
	void testDESProvider() throws Exception {
		String encryptString = "cF0V6oCzMrat9RYDOyRfuVdUKI64x5mOfFipfUzVklho/Y2EEVyZ21Ip3zxYoHrw7U9nuF3wzNt/QvFSH1NIZQ==";
		String decryptString = DesProvider.EncryptString(encryptString, "K7ef0139cbk");
		
		String result = DesProvider.DecryptString(decryptString, "K7ef0139cbk");
		assertEquals(encryptString, result);
		//System.out.println("testDESProvider decryptString: " + decryptString);
		//System.out.println("testDESProvider result: " + result);
		//System.out.println();
	}

}
