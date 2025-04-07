package com.extraccion.xml.utils.archivos;

import com.extraccion.xml.model.response.Direccion;
import com.extraccion.xml.model.response.Persona;
import com.extraccion.xml.model.response.ResponseBusqueda;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.List;

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

    /**
     * Documento XML cacheado para mejorar rendimiento.
     */
    private Document cachedDocument;

    /**
     *  Método protegido para permitir mocking en tests
     *
     * @return regresa el xml como respuesta
     */
    protected ClassPathResource getClassPathResource() {
        return new ClassPathResource("cliente.xml");
    }

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
     * @throws Exception para validar errores
     */
    public Document getDocument() throws Exception {
        if (cachedDocument == null) {
            DocumentBuilder builder = createDocumentBuilderFactory().newDocumentBuilder();
            cachedDocument = builder.parse(getClassPathResource().getInputStream());
        }
        return cachedDocument;
    }

    /**
     * Busca un nodo de persona por ID usando expresiones XPath.
     *
     * @param document documento xml
     * @param id de la persona a buscar
     * @return Nodo XML correspondiente a la persona o {@code null} si no se encuentra
     * @throws XPathExpressionException Si la expresión XPath es inválida
     */
    public Node findPersonNodeById(Document document, Long id) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = String.format("/datos/persona[id=%d]", id);
        return (Node) xpath.compile(expression).evaluate(document, XPathConstants.NODE);
    }

    /**
     * Construye un objeto ResponseBusqueda desde un elemento XML de persona
     *
     * @param personaElement representa a una persona en el xml
     * @return ResponseBusqueda persona encontrada en una lista
     */
    public ResponseBusqueda buildResponseFromNode(Element personaElement) {
        Persona persona = buildPersonaFromElement(personaElement);
        return new ResponseBusqueda(List.of(persona));
    }

    /**
     * Obtiene un elemento de la lista
     *
     * @param parent elemento padre
     * @param tagName nombre del tag
     * @return Contenido de texto del primer elemento encontrado o {@code null} si no existe
     */
    String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        // Verifica si nodeList es null o si no hay elementos
        if (nodeList != null && nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    /**
     * Construye el objeto con builder de la persona encontrada
     * <p>
     * Mapea los siguientes campos:
     * <ul>
     *   <li>id, nombre, apellidos</li>
     *   <li>fechaNacimiento, CURP</li>
     *   <li>dirección (como objeto anidado)</li>
     * </ul>
     * </p>
     *
     * @param personaElement representa una persona en el xml
     * @return retorna objeto de persona
     */
    public Persona buildPersonaFromElement(Element personaElement) {
        return Persona.builder()
                .id(parseLong(getElementValue(personaElement, "id")))
                .nombre(getElementValue(personaElement, "nombre"))
                .apellidoPaterno(getElementValue(personaElement, "apellidoPaterno"))
                .apellidoMaterno(getElementValue(personaElement, "apellidoMaterno"))
                .fechaNacimiento(getElementValue(personaElement, "fechaNacimiento"))
                .curp(getElementValue(personaElement, "curp"))
                .direccion(extractAddress(personaElement))
                .build();
    }

    /**
     * Extrae información de dirección desde un elemento persona.
     *
     * <p>
     * Mapea los siguientes campos:
     * <ul>
     *   <li>calle, colonia, municipio</li>
     *   <li>entidad, codigoPostal</li>
     * </ul>
     * </p>
     *
     * @param personaElement elemento xml de una persona
     * @return retorna el objeto construido de la direccion
     */
    Direccion extractAddress(Element personaElement) {
        NodeList direccionNodes = personaElement.getElementsByTagName("direccion");
        if (direccionNodes.getLength() == 0) return null;

        Element direccionElement = (Element) direccionNodes.item(0);

        return Direccion.builder()
                .calle(getElementValue(direccionElement, "calle"))
                .colonia(getElementValue(direccionElement, "colonia"))
                .municipio(getElementValue(direccionElement, "municipio"))
                .entidad(getElementValue(direccionElement, "entidad"))
                .codigoPostal(getElementValue(direccionElement, "codigoPostal"))
                .build();
    }

    /**
     * Convierte una cadena de texto a un valor {@link Long}
     * <p>
     * Si el valor es {@code null} o está vacío, se retorna {@code null}.
     * Si el valor no representa un número válido, lanza una {@link IllegalArgumentException}.
     * </p>
     *
     * @param value cadena de texto a convertir
     * @return valor numérico de tipo {@code Long} o {@code null} si el valor es nulo o vacío
     * @throws IllegalArgumentException si el valor no puede convertirse a número
     */
    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor no es un número válido: " + value, e);
        }
    }
}