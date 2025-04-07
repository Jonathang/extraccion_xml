package com.extraccion.xml.service.impl;

import com.extraccion.xml.model.response.ResponseBusqueda;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XmlBusquedaServiceImplTest {

    @InjectMocks
    private XmlBusquedaServiceImpl service;

    @Mock
    private Documento documento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPersonaPorId_idEsNulo_devuelveNull() throws Exception {
        ResponseBusqueda resultado = service.buscarPersonaPorId(null);
        assertNull(resultado);
        verify(documento, never()).getDocument();
    }

    @Test
    void buscarPersonaPorId_noEncuentraNodo_devuelveNull() throws Exception {
        Document mockDocument = mock(Document.class);

        when(documento.getDocument()).thenReturn(mockDocument);
        when(documento.findPersonNodeById(mockDocument, 123L)).thenReturn(null);

        ResponseBusqueda resultado = service.buscarPersonaPorId(123L);
        assertNull(resultado);
    }

    @Test
    void buscarPersonaPorId_encuentraPersona_devuelveRespuesta() throws Exception {
        Document mockDocument = mock(Document.class);
        Element mockPersonaElement = mock(Element.class);
        ResponseBusqueda mockResponse = new ResponseBusqueda(List.of()); // simular retorno

        when(documento.getDocument()).thenReturn(mockDocument);
        when(documento.findPersonNodeById(mockDocument, 123L)).thenReturn(mockPersonaElement);
        when(documento.buildResponseFromNode(mockPersonaElement)).thenReturn(mockResponse);

        ResponseBusqueda resultado = service.buscarPersonaPorId(123L);
        assertNotNull(resultado);
        assertEquals(mockResponse, resultado);
    }

    @Test
    void buscarPersonaPorId_lanzaExcepcion_wrapEnRuntime() throws Exception {
        when(documento.getDocument()).thenThrow(new RuntimeException("Fallo de lectura XML"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            service.buscarPersonaPorId(123L);
        });

        assertTrue(ex.getMessage().contains("Error al procesar el XML")); // depende de tu constante
    }
}