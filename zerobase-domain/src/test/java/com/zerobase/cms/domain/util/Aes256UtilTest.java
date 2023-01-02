package com.zerobase.cms.domain.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.zerobase.cms.domain.util.Aes256Util;

class Aes256UtilTest {

	@Test
	void encrypt() {
		String encrypt = Aes256Util.encrypt("Hello World!");
		assertEquals(Aes256Util.decrypt(encrypt), "Hello World!");
	}
}
