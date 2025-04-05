package com.extraccion.xml.service;

import com.extraccion.xml.model.response.ResponseBusqueda;

/**
 * Interface para realizar búsquedas de personas en formato XML
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
public interface XmlBusquedaService {

    /**
     * Busca una persona por su identificador único.
     * <p>
     * Realiza una búsqueda en el sistema y devuelve los datos correspondientes
     * a la persona encontrada, encapsulados en un objeto {@link ResponseBusqueda}.
     * </p>
     *
     * @param id Identificador único de la persona. Debe ser un valor positivo.
     * @see ResponseBusqueda
     */
    ResponseBusqueda buscarPersonaPorId(Long id);
}
