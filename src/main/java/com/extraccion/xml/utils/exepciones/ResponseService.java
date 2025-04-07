package com.extraccion.xml.utils.exepciones;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Se crean las respuestas json personalizadas
 *
 * @author Jonathan Garcia
 * @version 1.0.0
 * @since 05-04-2025
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseService {

    /**
     * prepara la respuesta con operacion exitosa 200 OK
     *
     * @param resultado manda un resultado de respuesta exitosa
     * @return retorna el resultado de un ResponseEntity dentro del cuerpo del mensaje
     */
    public static ResponseEntity<ApiResponse> successResponse(Object resultado) {
        ApiResponse response = new ApiResponse(
                HttpStatus.OK.value(),
                "Operación exitosa",
                resultado
        );
        return ResponseEntity.ok().body(response);
    }

    /**
     * Crea las variables de la respuesta con su respectivo constructor
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ApiResponse {
        private final int codigo;
        private final String mensaje;
        private final Object resultado;

        public ApiResponse(int codigo, String mensaje, Object resultado) {
            this.codigo = codigo;
            this.mensaje = mensaje;
            this.resultado = resultado;
        }
    }
}