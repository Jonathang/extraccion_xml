package com.extraccion.xml.controller;

import com.extraccion.xml.constantes.Constantes;
import com.extraccion.xml.model.request.RequestIdentificador;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.impl.XmlBusquedaServiceImpl;
import com.extraccion.xml.utils.exepciones.ResponseService;
import com.extraccion.xml.utils.Urls;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para realizar busquedas de datos sociodemograficos en formato XML
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-204-2025
 */
@RestController
@RequestMapping(Urls.V1)
public class XmlController {

    private static final Logger logger = LogManager.getLogger(XmlController.class);
    /**
     * Servicio de busqueda de clientes en formato XML
     */
    @Autowired
    private XmlBusquedaServiceImpl personaXMLService;

    /**
     * Busca datos sociodemográficos de una persona por su identificador único
     *
     * @param request Objeto JSON con el identificador de la persona.
     *                Debe ser válido según las anotaciones de validación.
     * @return {@link ResponseEntity} con formato estándar de respuesta:
     *         <ul>
     *           <li><b>Código HTTP:</b> 200 (OK) si la búsqueda es exitosa.</li>
     *           <li><b>Cuerpo:</b> {@link ResponseService.ApiResponse} que encapsula
     *               los datos de {@link ResponseBusqueda}.</li>
     *         </ul>
     * @see ResponseService#successResponse(Object)
     * @return {@link ResponseEntity} con formato estándar de respuesta:
     *         <ul>
     *           <li><b>Código HTTP:</b> 404 (NOT_FOUND) si el identificador no existe.</li>
     *           <li><b>Cuerpo:</b> {@link ResponseService.ApiResponse} que encapsula
     *               los datos de {@link ResponseBusqueda}.</li>
     *         </ul>
     *
     * @see ResponseService#errorResponse(Object)
     *
     * @PostMapping(Urls.BUSQUEDAS)  // Ruta: /v1/busquedas (depende de Urls.BUSQUEDAS)
     */
    @PostMapping(Urls.BUSQUEDAS)
    public ResponseEntity<ResponseService.ApiResponse> buscarDatosSociodemograficos(
            @Validated @RequestBody RequestIdentificador request) throws Exception {

        /**
         * Solicita un ID y realiza una búsqueda dentro del archivo XML
         * para extraer los datos sociodemográficos del cliente identificado.
         */
        ResponseBusqueda respuesta = personaXMLService.buscarPersonaPorId(request.getId());

        // Validar si no se encuentra la persona en el archivo XML
        if (respuesta == null) {
            // Retorna una respuesta con un error si no se encuentra la persona
            return ResponseService.errorResponse(Constantes.DATA_NOT_FOUND);
        }

        // Si se encuentra la persona, devuelve la respuesta exitosa
        return ResponseService.successResponse(respuesta);
    }
}
