package com.extraccion.xml.component;

import com.extraccion.xml.constantes.Constantes;
import com.extraccion.xml.controller.xmlController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Muestra el monitor del microservicio
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 *
 */
@RestController
@RequestMapping("/monitor")
public class Monitor implements HealthIndicator {

    private static final Logger logger = LogManager.getLogger(Monitor.class);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${info.app.version:${project.version}}")
    private String appVersion;

    @Value("${java.version}")
    private String javaVersion;

    /**
     * Muestra en formato json las caracteristicas del microservicio
     *
     * @return {@link Health} Un objeto que representa el estado del servicio, con detalles como:
     * <ul>
     *     <li><b>microservice</b>: Nombre de la aplicación ({@code appName}).</li>
     *     <li><b>version</b>: Versión actual del microservicio ({@code appVersion}).</li>
     *     <li><b>javaVersion</b>: Versión de Java en uso.</li>
     *     <li><b>runtime</b>: Entorno de ejecución de Java (ej. "OpenJDK Runtime Environment").</li>
     * </ul>
     * @since 05-04-2025
     */
    @Override
    public Health health() {
        logger.info(Constantes.PING);
        return Health.up()
                .withDetail(Constantes.APPNAME, appName)
                .withDetail(Constantes.APP_VERSION, appVersion)
                .withDetail(Constantes.JAVA_VERSION, javaVersion)
                .withDetail(Constantes.RUNTIME, System.getProperty(Constantes.JAVA_RUNTIME_NAME))
                .build();
    }
}
