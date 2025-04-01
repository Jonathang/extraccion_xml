package com.extraccion.xml.controller;

import com.extraccion.xml.model.request.RequestSociodemograficos;
import com.extraccion.xml.model.response.ResponseBusqueda;
import com.extraccion.xml.service.impl.XmlBusquedaServiceImpl;
import com.extraccion.xml.utils.Urls;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Urls.V1)
public class xmlController {

    private static final Logger logger = (Logger) LogManager.getLogger(xmlController.class);

    @Autowired
    private XmlBusquedaServiceImpl personaXMLService;

    @PostMapping(Urls.BUSQUEDAS)
    public ResponseEntity<ResponseBusqueda> buscarDatosSociodemograficos (
            @Validated @RequestBody RequestSociodemograficos request ){

        try {

            ResponseBusqueda respuesta =
                    personaXMLService.buscarPersonaPorId(Long.valueOf(request.getId()));


            if (respuesta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseBusqueda.builder().persona(null).build());
            }

            return ResponseEntity.ok(respuesta);
        }catch (Exception e ){
            logger.error("Entro a la excepcion: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
