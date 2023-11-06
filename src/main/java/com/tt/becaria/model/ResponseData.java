package com.tt.becaria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa una respuesta de datos. Contiene información sobre el estado,
 * el código y la respuesta de una solicitud.
 *
 * El proposito de esta clase es facilitar y generalizar el formato de respuesta JSON que tienen los endpoints o handlers.
 */
@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
@AllArgsConstructor //Esta anotación genera un constructor que acepta todos los atributos de la clase como argumentos.
public class ResponseData {

    /**
     * Estado de la respuesta.
     */
    private String status;

    /**
     * Código de la respuesta.
     */
    private int code;

    /**
     * Objeto de respuesta.
     */
    private Object response;
}
