package com.extraccion.xml.utils.archivos;

import com.extraccion.xml.Mockdata;
import com.extraccion.xml.model.response.Direccion;
import com.extraccion.xml.model.response.Persona;
import com.extraccion.xml.model.response.ResponseBusqueda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

@ExtendWith(MockitoExtension.class)
class DocumentoTest {

    @InjectMocks
    private Documento documento;

    @Mock
    private Method parseLongMethod;

    @BeforeEach
    void setup() throws Exception {
        documento = Mockito.spy(new Documento() {
            @Override
            protected ClassPathResource getClassPathResource() {
                return null; // no lo usamos porque inyectamos directamente el InputStream
            }

            @Override
            protected DocumentBuilderFactory createDocumentBuilderFactory() {
                return super.createDocumentBuilderFactory();
            }
        });

        // Preparar el documento XML simulado
        DocumentBuilder builder = documento.createDocumentBuilderFactory().newDocumentBuilder();
        InputStream xmlInputStream = new ByteArrayInputStream(Mockdata.SAMPLE_XML.getBytes(StandardCharsets.UTF_8));
        Document doc = builder.parse(xmlInputStream);

        // Setear el documento cacheado directamente para evitar leer archivos reales
        java.lang.reflect.Field field = Documento.class.getDeclaredField("cachedDocument");
        field.setAccessible(true);
        field.set(documento, doc);

        parseLongMethod = Documento.class.getDeclaredMethod("parseLong", String.class);
        parseLongMethod.setAccessible(true);
    }

    @Test
    void testGetDocument() throws Exception {
        Document doc = documento.getDocument();
        assertNotNull(doc);
        assertEquals("datos", doc.getDocumentElement().getTagName());
    }

    @Test
    void testFindPersonNodeById() throws Exception {
        Document doc = documento.getDocument();
        Node personNode = documento.findPersonNodeById(doc, 123L);
        assertNotNull(personNode);
        assertTrue(personNode instanceof Element);
        assertEquals("persona", ((Element) personNode).getTagName());
    }

    @Test
    void testBuildPersonaFromElement() throws Exception {
        Document doc = documento.getDocument();
        Node personNode = documento.findPersonNodeById(doc, 123L);
        assertNotNull(personNode);

        Persona persona = documento.buildPersonaFromElement((Element) personNode);

        assertEquals(123L, persona.getId());
        assertEquals("Ana", persona.getNombre());
        assertEquals("Martínez", persona.getApellidoPaterno());
        assertEquals("Gómez", persona.getApellidoMaterno());
        assertEquals("1990-05-20", persona.getFechaNacimiento());
        assertEquals("ANMG900520HDFRZN00", persona.getCurp());

        assertNotNull(persona.getDireccion());
        assertEquals("Av. Juárez", persona.getDireccion().getCalle());
        assertEquals("Centro", persona.getDireccion().getColonia());
    }

    @Test
    void testBuildResponseFromNode() throws Exception {
        Document doc = documento.getDocument();
        Node personNode = documento.findPersonNodeById(doc, 123L);
        assertNotNull(personNode);

        ResponseBusqueda response = documento.buildResponseFromNode((Element) personNode);
        assertNotNull(response);
        assertEquals(1, response.getPersonas().size());
        Persona persona = response.getPersonas().get(0);

        assertEquals("Ana", persona.getNombre());
    }

    private Long invokeParseLong(String input) throws Exception {
        return (Long) parseLongMethod.invoke(documento, input);
    }


    @Test
    void testValidNumber() throws Exception {
        Long result = invokeParseLong("12345");
        assertEquals(12345L, result);
    }

    @Test
    void testNullValue() throws Exception {
        Long result = invokeParseLong(null);
        assertNull(result);
    }

    @Test
    void testEmptyValue() throws Exception {
        Long result = invokeParseLong("");
        assertNull(result);
    }

    @Test
    void testInvalidNumberThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> invokeParseLong("abc123"));
        Throwable cause = exception.getCause(); // Because reflection wraps the real exception

        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("El valor no es un número válido: abc123", cause.getMessage());
    }
}