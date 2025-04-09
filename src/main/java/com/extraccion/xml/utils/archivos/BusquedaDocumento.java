package com.extraccion.xml.utils.archivos;

import com.extraccion.xml.model.response.Direccion;
import com.extraccion.xml.model.response.Persona;
import com.extraccion.xml.model.response.ResponseBusqueda;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.List;

@Component
public class BusquedaDocumento {

    private final XPathFactory xpathFactory;

    public BusquedaDocumento() {
        this(XPathFactory.newInstance());
    }

    // Constructor para testing
    BusquedaDocumento(XPathFactory xpathFactory) {
        this.xpathFactory = xpathFactory;
    }

    /**
     * Busca un nodo de persona por ID usando expresiones XPath.
     *
     * @param document documento xml
     * @param id de la persona a buscar
     * @return Nodo XML correspondiente a la persona o {@code null} si no se encuentra
     * @throws XPathExpressionException Si la expresión XPath es inválida
     */
    public Node findPersonNodeById(Document document, Long id) throws Exception {
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
    Long parseLong(String value) {
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
