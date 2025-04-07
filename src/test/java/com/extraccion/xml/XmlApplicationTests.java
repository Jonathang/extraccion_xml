package com.extraccion.xml;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class XmlApplicationTests {

	@Test
	public void mainTest() {
		// Verifica que el método main puede ejecutarse sin errores
		assertDoesNotThrow(() -> XmlApplication.main(new String[] {}));
	}
}
