package com.extraccion.xml.component;

import com.extraccion.xml.constantes.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {

    @InjectMocks
    private Monitor monitor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Inyectamos valores simulados usando ReflectionTestUtils
        ReflectionTestUtils.setField(monitor, "appName", "test-service");
        ReflectionTestUtils.setField(monitor, "appVersion", "1.0.0");
        ReflectionTestUtils.setField(monitor, "javaVersion", "11.0.15");
    }

    @Test
    public void testHealth_ReturnsUpStatus() {
        // Act
        Health health = monitor.health();

        // Assert
        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    public void testHealth_ContainsExpectedDetails() {
        // Act
        Health health = monitor.health();

        // Assert
        assertAll(
                () -> assertEquals("test-service", health.getDetails().get(Constantes.APPNAME)),
                () -> assertEquals("1.0.0", health.getDetails().get(Constantes.APP_VERSION)),
                () -> assertEquals("11.0.15", health.getDetails().get(Constantes.JAVA_VERSION)),
                () -> assertNotNull(health.getDetails().get(Constantes.RUNTIME))
        );
    }

    @Test
    public void testHealth_RuntimeProperty() {
        // Act
        Health health = monitor.health();
        String runtime = (String) health.getDetails().get(Constantes.RUNTIME);

        // Assert
        assertNotNull(runtime);
        assertFalse(runtime.isEmpty());
    }
}
