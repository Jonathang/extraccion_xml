package com.extraccion.xml.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
/**
 * Objeto de solicitud que representa los datos del domicilio del cliente
 *
 * @Data Genera automáticamente getters, setters, equals, hashCode y toString (Lombok)
 * @AllArgsConstructor Constructor con todos los campos (Lombok)
 * @Builder Patrón de construcción flexible (Lombok)
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
@Data
@AllArgsConstructor
@Builder
public class Direccion {

    private String calle;
    private String colonia;
    private String municipio;
    private String entidad;
    private String codigoPostal;
}
