package com.extraccion.xml.controller;

import com.extraccion.xml.model.request.RequestIdentificador;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.impl.XmlBusquedaServiceImpl;
import com.extraccion.xml.utils.Urls;
import com.extraccion.xml.utils.exepciones.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class xmlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private XmlBusquedaServiceImpl personaXMLService;

    @InjectMocks
    private XmlController xmlController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(xmlController).build();
    }

    @Test
    public void buscarDatosSociodemograficos_Success() throws Exception {
        // Arrange
        RequestIdentificador request = new RequestIdentificador();
        request.setId(1234L);

        ResponseBusqueda mockResponse = new ResponseBusqueda();
        // Configura mockResponse según sea necesario

        when(personaXMLService.buscarPersonaPorId(anyLong())).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post(Urls.V1 + Urls.BUSQUEDAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        // Puedes añadir más verificaciones del contenido de la respuesta
    }

    @Test
    public void buscarDatosSociodemograficos_InvalidRequest() throws Exception {
        // Arrange - Request sin ID (inválido según @Validated)
        RequestIdentificador invalidRequest = new RequestIdentificador();

        // Act & Assert
        mockMvc.perform(post(Urls.V1 + Urls.BUSQUEDAS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // Prueba adicional para verificar el logging
    @Test
    public void buscarDatosSociodemograficos_VerifyLogging() throws Exception {
        // Arrange
        RequestIdentificador request = new RequestIdentificador();
        request.setId(1234L);

        ResponseBusqueda mockResponse = new ResponseBusqueda();
        when(personaXMLService.buscarPersonaPorId(anyLong())).thenReturn(mockResponse);

        // Act
        mockMvc.perform(post(Urls.V1 + Urls.BUSQUEDAS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }
    @Test
    @DisplayName("buscarDatosSociodemograficos - Debe retornar error 404 cuando no se encuentra la persona")
    void testBuscarDatosSociodemograficos_NotFound() throws Exception {
        RequestIdentificador request = new RequestIdentificador();
        request.setId(1234L);

        // Arrange
        when(personaXMLService.buscarPersonaPorId(request.getId())).thenReturn(null);

        // Act
        ResponseEntity<ResponseService.ApiResponse> response =
                xmlController.buscarDatosSociodemograficos(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error al procesar el xml", response.getBody().getMensaje());
        assertEquals("No se encontraron datos para el ID proporcionado.", response.getBody().getResultado());

        verify(personaXMLService, times(1)).buscarPersonaPorId(request.getId());
    }


}