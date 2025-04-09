package com.extraccion.xml.utils.archivos;

import com.extraccion.xml.model.response.Direccion;
import com.extraccion.xml.model.response.Persona;
import com.extraccion.xml.model.response.ResponseBusqueda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusquedaDocumentoTest {

    @InjectMocks
    private BusquedaDocumento busquedaDocumento;
    @Mock
    private DocumentBuilder documentBuilder;

    @Mock
    private Document document;

    @Mock
    private Documento documento;

    @Mock
    private XPath xpath;

    @BeforeEach
    void setUp() throws Exception {
        busquedaDocumento = new BusquedaDocumento();
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        // Documento XML de prueba
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<datos>\n" +
                "    <persona>\n" +
                "        <id>1</id>\n" +
                "        <nombre>Juan</nombre>\n" +
                "        <apellidoPaterno>Perez</apellidoPaterno>\n" +
                "        <apellidoMaterno>Gomez</apellidoMaterno>\n" +
                "        <fechaNacimiento>1980-01-01</fechaNacimiento>\n" +
                "        <curp>PEGJ800101HDFRRN01</curp>\n" +
                "        <direccion>\n" +
                "            <calle>Av. Principal</calle>\n" +
                "            <colonia>Centro</colonia>\n" +
                "            <municipio>Benito Juarez</municipio>\n" +
                "            <entidad>Ciudad de México</entidad>\n" +
                "            <codigoPostal>01000</codigoPostal>\n" +
                "        </direccion>\n" +
                "    </persona>\n" +
                "    <persona>\n" +
                "        <id>2</id>\n" +
                "        <nombre>Maria</nombre>\n" +
                "        <apellidoPaterno>Lopez</apellidoPaterno>\n" +
                "        <direccion>\n" +
                "            <calle>Calle Secundaria</calle>\n" +
                "        </direccion>\n" +
                "    </persona>\n" +
                "</datos>";
        document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Configuración básica de XPath
        xpath = XPathFactory.newInstance().newXPath();
    }

    @Test
    @DisplayName("findPersonNodeById - Debe encontrar nodo cuando existe")
    void findPersonNodeById_WhenExists() throws Exception {
        // Act
        Node node = busquedaDocumento.findPersonNodeById(document, 1L);

        // Assert
        assertNotNull(node);
        assertEquals("persona", node.getNodeName());

        // Verificar contenido del nodo
        Element element = (Element) node;
        assertEquals("1", element.getElementsByTagName("id").item(0).getTextContent());
        assertEquals("Juan", element.getElementsByTagName("nombre").item(0).getTextContent());
    }

    @Test
    @DisplayName("findPersonNodeById - Debe retornar null cuando no existe")
    void findPersonNodeById_WhenNotExists() throws Exception {
        // Act
        Node node = busquedaDocumento.findPersonNodeById(document, 999L);

        // Assert
        assertNull(node);
    }


    @Test
    @DisplayName("getElementValue - Debe retornar valor cuando existe")
    void getElementValue_WhenExists() {
        // Arrange
        Element personaElement = (Element) document.getElementsByTagName("persona").item(0);

        // Act
        String nombre = busquedaDocumento.getElementValue(personaElement, "nombre");

        // Assert
        assertEquals("Juan", nombre);
    }

    @Test
    @DisplayName("getElementValue - Debe retornar null cuando no existe")
    void getElementValue_WhenNotExists() {
        // Arrange
        Element personaElement = (Element) document.getElementsByTagName("persona").item(0);

        // Act
        String valorInexistente = busquedaDocumento.getElementValue(personaElement, "inexistente");

        // Assert
        assertNull(valorInexistente);
    }


    @Test
    @DisplayName("getElementValue - Debe retornar null cuando tagName es null")
    void getElementValue_WhenTagNameIsNull() {
        // Arrange
        BusquedaDocumento busquedaDocumento = new BusquedaDocumento();
        Element parent = mock(Element.class);

        // Act
        String result = busquedaDocumento.getElementValue(parent, null);

        // Assert
        assertNull(result, "Debe retornar null cuando el nombre del tag es null");
    }

    @Test
    @DisplayName("getElementValue - Debe retornar null cuando nodeList está vacío")
    void getElementValue_WhenNodeListIsEmpty() {
        // Arrange
        BusquedaDocumento busquedaDocumento = new BusquedaDocumento();
        Element parent = mock(Element.class);
        NodeList emptyNodeList = mock(NodeList.class);

        when(parent.getElementsByTagName(anyString())).thenReturn(emptyNodeList);
        when(emptyNodeList.getLength()).thenReturn(0);

        // Act
        String result = busquedaDocumento.getElementValue(parent, "tagInexistente");

        // Assert
        assertNull(result, "Debe retornar null cuando no se encuentra el tag");
    }

    @Test
    @DisplayName("getElementValue - Debe retornar null cuando nodeList es null")
    void getElementValue_WhenNodeListIsNull() {
        // Arrange
        BusquedaDocumento busquedaDocumento = new BusquedaDocumento();
        Element parent = mock(Element.class);

        when(parent.getElementsByTagName(anyString())).thenReturn(null);

        // Act
        String result = busquedaDocumento.getElementValue(parent, "tagCualquiera");

        // Assert
        assertNull(result, "Debe retornar null cuando nodeList es null");
    }

    @Test
    @DisplayName("buildPersonaFromElement - Debe construir Persona correctamente")
    void buildPersonaFromElement_CompleteData() {
        // Arrange
        Element personaElement = (Element) document.getElementsByTagName("persona").item(0);

        // Act
        Persona persona = busquedaDocumento.buildPersonaFromElement(personaElement);

        // Assert
        assertNotNull(persona);
        assertEquals(1L, persona.getId());
        assertEquals("Juan", persona.getNombre());
        assertEquals("Perez", persona.getApellidoPaterno());
        assertEquals("Gomez", persona.getApellidoMaterno());
        assertEquals("1980-01-01", persona.getFechaNacimiento());
        assertEquals("PEGJ800101HDFRRN01", persona.getCurp());

        Direccion direccion = persona.getDireccion();
        assertNotNull(direccion);
        assertEquals("Av. Principal", direccion.getCalle());
        assertEquals("Centro", direccion.getColonia());
        assertEquals("Benito Juarez", direccion.getMunicipio());
        assertEquals("Ciudad de México", direccion.getEntidad());
        assertEquals("01000", direccion.getCodigoPostal());
    }

    @Test
    @DisplayName("buildPersonaFromElement - Debe manejar datos faltantes")
    void buildPersonaFromElement_MissingData() {
        // Arrange
        Element personaElement = (Element) document.getElementsByTagName("persona").item(1);

        // Act
        Persona persona = busquedaDocumento.buildPersonaFromElement(personaElement);

        // Assert
        assertNotNull(persona);
        assertEquals(2L, persona.getId());
        assertEquals("Maria", persona.getNombre());
        assertEquals("Lopez", persona.getApellidoPaterno());
        assertNull(persona.getApellidoMaterno());
        assertNull(persona.getFechaNacimiento());
        assertNull(persona.getCurp());

        Direccion direccion = persona.getDireccion();
        assertNotNull(direccion);
        assertEquals("Calle Secundaria", direccion.getCalle());
        assertNull(direccion.getColonia());
        assertNull(direccion.getMunicipio());
        assertNull(direccion.getEntidad());
        assertNull(direccion.getCodigoPostal());
    }

    @Test
    @DisplayName("extractAddress - Debe retornar null cuando no hay dirección")
    void extractAddress_WhenNotExists() {
        // Arrange
        Document doc = documentBuilder.newDocument();
        Element personaElement = doc.createElement("persona");

        // Act
        Direccion direccion = busquedaDocumento.extractAddress(personaElement);

        // Assert
        assertNull(direccion);
    }

    @Test
    @DisplayName("parseLong - Debe convertir string a Long correctamente")
    void parseLong_ValidNumber() {
        assertEquals(123L, busquedaDocumento.parseLong("123"));
    }

    @Test
    @DisplayName("parseLong - Debe retornar null para string vacío o nulo")
    void parseLong_NullOrEmpty() {
        assertNull(busquedaDocumento.parseLong(null));
        assertNull(busquedaDocumento.parseLong(""));
    }

    @Test
    @DisplayName("parseLong - Debe lanzar IllegalArgumentException para string inválido")
    void parseLong_InvalidNumber() {
        assertThrows(IllegalArgumentException.class, () -> busquedaDocumento.parseLong("abc"));
    }

    @Test
    @DisplayName("buildResponseFromNode - Debe construir ResponseBusqueda correctamente")
    void buildResponseFromNode_ValidNode() {
        // Arrange
        Element personaElement = (Element) document.getElementsByTagName("persona").item(0);

        // Act
        ResponseBusqueda response = busquedaDocumento.buildResponseFromNode(personaElement);

        // Assert
        assertNotNull(response);
        List<Persona> personas = response.getPersonas();
        assertNotNull(personas);
        assertEquals(1, personas.size());

        Persona persona = personas.get(0);
        assertEquals(1L, persona.getId());
        assertEquals("Juan", persona.getNombre());
    }
}