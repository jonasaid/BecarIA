package com.tt.becaria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase representa el resultado de una operación de inserción.
 * Se utiliza mayormente cuando es invocado el método insertWithId de la clase DbDriver.
 * Su propósito es proporcionar información sobre el éxito de la operación de inserción y, en caso de éxito, el ID faltante asociado a esa inserción.
 *
 * El motivo de existencia de esta clase se debe a que, al insertar datos en tablas relacionadas que utilizan secuencias de ID,
 * es necesario recuperar el ID o valor que establece la relación entre las tablas. El proceso de inserción automática verifica si
 * hay ID faltantes en la secuencia, y si los encuentra, inserta el siguiente número o valor disponible.
 */

@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
@AllArgsConstructor //Esta anotación genera un constructor que acepta todos los atributos de la clase como argumentos.
public class InsertionResult {
    /**
     * Indica si la operación de inserción fue exitosa.
     */
    private boolean success;

    /**
     * El ID faltante en caso de que la operación sea exitosa.
     */
    private int missingId;
}
