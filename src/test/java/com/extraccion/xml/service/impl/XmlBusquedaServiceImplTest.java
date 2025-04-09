package com.extraccion.xml.service.impl;

import com.extraccion.xml.Mockdata;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.utils.archivos.BusquedaDocumento;
import com.extraccion.xml.utils.archivos.Documento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlBusquedaServiceImplTest {

    @InjectMocks
    private XmlBusquedaServiceImpl service;

    @Mock
    private Documento file;

    @Mock
    private BusquedaDocumento busqueda;

    private Document xmlDocument;

    @BeforeEach
    void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        xmlDocument = builder.parse(new InputSource(new StringReader(Mockdata.SAMPLE_XML)));
    }

    @Test
    void buscarPersonaPorId_idNulo_retornarNull() throws Exception {
        ResponseBusqueda resultado = service.buscarPersonaPorId(null);
        assertNull(resultado);
    }

    @Test
    void buscarPersonaPorId_noEncuentraPersona_retornarNull() throws Exception {
        Long idBuscado = 999L;

        when(file.getDocument()).thenReturn(xmlDocument);
        when(busqueda.findPersonNodeById(xmlDocument, idBuscado)).thenReturn(null);

        ResponseBusqueda resultado = service.buscarPersonaPorId(idBuscado);

        assertNull(resultado);
        verify(busqueda).findPersonNodeById(xmlDocument, idBuscado);
    }

    @Test
    void buscarPersonaPorId_encuentraPersona_retornarResponse() throws Exception {
        Long idBuscado = 50L;
        Node mockNode = xmlDocument.getElementsByTagName("persona").item(0);
        ResponseBusqueda expectedResponse = new ResponseBusqueda(); // Simula el contenido esperado

        when(file.getDocument()).thenReturn(xmlDocument);
        when(busqueda.findPersonNodeById(xmlDocument, idBuscado)).thenReturn(mockNode);
        when(busqueda.buildResponseFromNode((Element) mockNode)).thenReturn(expectedResponse);

        ResponseBusqueda resultado = service.buscarPersonaPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(expectedResponse, resultado);
        verify(busqueda).findPersonNodeById(xmlDocument, idBuscado);
        verify(busqueda).buildResponseFromNode((Element) mockNode);
    }

    @Test
    void buscarPersonaPorId_excepcionAlProcesarXml_lanzaRuntimeException() throws Exception {
        Long idBuscado = 1L;

        when(file.getDocument()).thenThrow(new RuntimeException("Fallo al leer XML"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarPersonaPorId(idBuscado);
        });

        assertTrue(ex.getMessage().contains("Error al procesar el XML"));
    }
}