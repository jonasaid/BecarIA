package com.tt.becaria.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Indica que esta clase es una entidad JPA y se almacenará en la base de datos.
@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
public class UsuarioTemporal extends Usuario{
    private String codigo_validacion;

    public UsuarioTemporal(Integer id, String nombre, String password, String correo, String codigo_validacion) {
        super(id, nombre, password, correo);
        this.codigo_validacion = codigo_validacion;
    }
}
