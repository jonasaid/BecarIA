package com.tt.becaria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
@AllArgsConstructor //Esta anotación genera un constructor que acepta todos los atributos de la clase como argumentos.
public class EmailRemitente {

    /**
     * Quien envia el correo.
     */
    private String remitente;

    /**
     * Cuerpo del correo.
     */
    private String contenido;
}
