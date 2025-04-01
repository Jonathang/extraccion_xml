package com.extraccion.xml.service;

import com.extraccion.xml.model.response.ResponseBusqueda;


public interface XmlBusquedaService {

    ResponseBusqueda buscarPersonaPorId(Long id);
}
