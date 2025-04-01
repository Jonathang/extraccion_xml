package com.extraccion.xml.service;

import com.extraccion.xml.model.request.RequestSociodemograficos;
import com.extraccion.xml.model.response.ResponseBusqueda;
import jakarta.xml.bind.JAXBException;


public interface XmlBusquedaService {

    ResponseBusqueda buscarPersonaPorId(Long id);
}
