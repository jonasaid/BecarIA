package com.tt.becaria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa un correo electrónico y contiene información sobre el destinatario, el asunto y el contenido del correo.
 */

@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
@AllArgsConstructor //Esta anotación genera un constructor que acepta todos los atributos de la clase como argumentos.
public class Email {
    /**
     * Correo destino.
     */
    private String destinatario;
    /**
     * El asunto del correro.
     */
    private String asunto;
    /**
     * Cuerpo del correo.
     */
    private String contenido;

}
