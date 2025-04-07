package com.extraccion.xml.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UrlsTest {

    @Test
    public void testConstructorNoHaceNada() {
        // Verificar que el constructor no lanza excepciones
        assertDoesNotThrow(() -> new Urls());
    }

}