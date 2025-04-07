package com.extraccion.xml.constantes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantesTest {

    @Test
    public void testConstructorNoHaceNada() {
        // Verificar que el constructor no lanza excepciones
        assertDoesNotThrow(() -> new Constantes());
    }

}