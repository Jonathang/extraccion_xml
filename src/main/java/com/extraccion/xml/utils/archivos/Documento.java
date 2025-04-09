package com.extraccion.xml.utils.archivos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Componente para el manejo y procesamiento de documentos XML
 * <p>
 * Proporciona funcionalidades para:
 * <ul>
 *   <li>Cargar y cachear archivos XML</li>
 *   <li>Buscar nodos específicos usando XPath</li>
 *   <li>Construir objetos de dominio (Persona, Direccion) desde elementos XML</li>
 * </ul>
 * </p>
 *
 * @Component Indica que es un componente gestionado por Spring
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
@Component
public class Documento {

    @Value("${config.ruta}")
    private String archivoXml;

    /**
     * Documento XML cacheado para mejorar rendimiento.
     */
    public Document cachedDocument;

    /**
     *  Método protegido para permitir mocking en tests
     *
     * @return la construccion del documento
     */
    protected DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        return factory;
    }

    /**
     * Metodo para obtener el documento y validar si es vacio
     * tambien pérmite guardalo en memoria cache para una consulta mas rapida
     *
     * @return regresa el documento en cache
     * @throws IOException para validar errores
     * @throws ParserConfigurationException para validar error al configurar el parse xml
     * @throws SAXException para validar error al parsear el documento xml usando SAX
     */
    public Document getDocument() throws IOException, ParserConfigurationException, SAXException {
        if (cachedDocument == null) {
            synchronized (this) {
                    DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
                    try (InputStream archivo = new ClassPathResource(archivoXml).getInputStream()) {
                        cachedDocument = builder.parse(archivo);
                    }
                }
            }
        return cachedDocument;
    }


}