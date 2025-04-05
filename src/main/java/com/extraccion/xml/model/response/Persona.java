package com.extraccion.xml.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Objeto de solicitud que representa los datos la persona
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
public class Persona {

    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String curp;
    private Direccion direccion;
}
