package com.tt.becaria.util;

import org.apache.commons.lang3.RandomStringUtils;

public class GeneradorCodigoValidacion {

    private static final int LONGITUD_CODIGO = 6; // Longitud del código de validación

    /**
     * Genera un código de validación aleatorio.
     *
     * @return El código de validación generado.
     */
    public static String generarCodigoValidacion() {
        return RandomStringUtils.randomAlphanumeric(LONGITUD_CODIGO).toUpperCase();
    }
}
