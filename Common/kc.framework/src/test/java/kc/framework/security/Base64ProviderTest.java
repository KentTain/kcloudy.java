package kc.framework.security;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;


class Base64ProviderTest {

	@Test
	void testBase64Provider() {
		String except = "L3c132f119l";

		String encryptString = Base64Provider.EncodeString(except);
		String decryptString = Base64Provider.DecodeString(encryptString);

		//System.out.println("testBase64Provider EncryptString: " + encryptString);
		//System.out.println("testBase64Provider DecryptString: " + decryptString);
		assertEquals(except, decryptString);
		
		String encryptStringWithNet = "TDNjMTMyZjExOWw=";
		assertEquals(encryptString, encryptStringWithNet);
		//System.out.println();
	}

}
