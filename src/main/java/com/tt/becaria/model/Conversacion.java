package com.tt.becaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity // Indica que esta clase es una entidad JPA y se almacenará en la base de datos.
@Data // Anotación de Lombok para generar automáticamente getters, setters y toString.
@NoArgsConstructor //Esta anotación genera un constructor sin argumentos.
@AllArgsConstructor //Esta anotación genera un constructor que acepta todos los atributos de la clase como argumentos.
public class Conversacion {
    @Id
    private Integer id;
    private Integer id_usuario;
    private LocalDateTime fecha_hora;
}
