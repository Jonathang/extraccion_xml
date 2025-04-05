package com.extraccion.xml.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Objeto de solicitud que representa un identificador único para búsquedas.
 * <p>
 * Se utiliza como cuerpo de solicitud en endpoints REST que requieren un ID válido.
 * </p>
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
public class RequestIdentificador {

    @NotNull(message = "El ID no puede ser nulo")
    @Min(value = 1, message = "El ID debe ser mayor o igual a 1")
    private Long id;
}
