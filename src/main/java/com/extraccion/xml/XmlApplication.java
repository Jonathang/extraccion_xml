package com.extraccion.xml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Inicia la aplicacion para extraer clientes desde un archivo xml
 * <p>
 *    Usando springboot, anotaciones con lombok y java 17
 * </p>
 */
@SpringBootApplication
public class XmlApplication {

	public static void main(String[] args) {
		SpringApplication.run(XmlApplication.class, args);
	}

}
