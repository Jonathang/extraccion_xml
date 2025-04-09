package com.extraccion.xml.utils.archivos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentoTest {

    @Mock
    private ClassPathResource classPathResource;

    @Mock
    private DocumentBuilderFactory documentBuilderFactory;

    @Mock
    private DocumentBuilder documentBuilder;

    @Mock
    private Document document;

    @Mock
    private InputStream inputStream;

    @InjectMocks
    private Documento documento;

    @BeforeEach
    void setUp() {
        // Configuración básica para el mock del documento
        documento = new Documento() {
            @Override
            protected DocumentBuilderFactory createDocumentBuilderFactory() {
                return documentBuilderFactory;
            }
        };
        ReflectionTestUtils.setField(documento, "archivoXml", "cliente.xml");

    }

    @Test
    @DisplayName("createDocumentBuilderFactory - Debe configurar propiedades correctamente")
    void createDocumentBuilderFactory_ShouldConfigureProperties() {
        // Arrange
        Documento doc = new Documento();

        // Act
        DocumentBuilderFactory factory = doc.createDocumentBuilderFactory();

        // Assert
        assertTrue(factory.isNamespaceAware());
        assertTrue(factory.isIgnoringComments());
        assertTrue(factory.isIgnoringElementContentWhitespace());
    }

  /*
  *  getDocument
  */
  @Test
  void getDocument_ShouldReturnCachedDocument_WhenCalledMultipleTimes() throws Exception {
      // Configuración de los mocks
      when(documentBuilderFactory.newDocumentBuilder()).thenReturn(documentBuilder);
      // Usar any(InputStream.class) en lugar del mock específico
      when(documentBuilder.parse(any(InputStream.class))).thenReturn(document);

      Document firstCall = documento.getDocument();
      Document secondCall = documento.getDocument();

      assertSame(firstCall, secondCall, "Debería devolver la misma instancia en caché");
      verify(documentBuilderFactory, times(1)).newDocumentBuilder();
      verify(documentBuilder, times(1)).parse(any(InputStream.class));
  }

    @Test
    void getDocument_ShouldThrowException_WhenXmlFileIsNull() throws Exception {
        // Usar reflexión para establecer el campo archivoXml
        Field field = Documento.class.getDeclaredField("archivoXml");
        field.setAccessible(true);
        field.set(documento, null); // documento es tu instancia de Documento

        assertThrows(Exception.class, () -> documento.getDocument());
    }


}