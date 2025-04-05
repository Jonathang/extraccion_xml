package com.extraccion.xml.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Objeto de respuesta que contiene los resultados de una búsqueda de personas.
 * Lista de personas encontradas en la búsqueda.
 *
 * @Data          // Genera getters, setters, equals, hashCode y toString (Lombok)
 * @Builder       // Permite construcción flexible mediante patrón Builder (Lombok)
 * @NoArgsConstructor  // Constructor sin argumentos (Lombok)
 * @AllArgsConstructor // Constructor con todos los campos (Lombok)
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBusqueda{

    // Se maneja List para multiples personas
    private List<Persona> personas;
}
