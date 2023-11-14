package com.tt.becaria.service.impl;

import com.tt.becaria.model.Conversacion;
import com.tt.becaria.model.InsertionResult;
import com.tt.becaria.service.ConversacionService;
import com.tt.becaria.service.UsuarioService;
import com.tt.becaria.util.DbDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Esta clase es el controlador para la gestión de la entidad Conversacion.
 * Permite realizar operaciones de creación, consulta, actualización y eliminación de registros de conversaciones en la base de datos.
 */
@Component
public class ConversacionServiceImpl implements ConversacionService {
    /**
     * Atributo de la clase DbDriver que permite manejar la información en la base de datos, a través,
     * de usar los métodos asociados al CRUD.
     */
    private final DbDriver driver = DbDriver.getInstance();

    /**
     * Controlador para gestionar las operaciones relacionadas con la entidad Usuario.
     */
    private final UsuarioService usuarioService;

    /**
     * Constructor de la clase ConversacionServiceImpl.
     *
     * @param usuarioService El servicio de UsuarioService utilizado para realizar operaciones relacionadas con usuarios.
     */
    @Autowired
    public ConversacionServiceImpl(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Crea un nuevo registro de conversación en la base de datos con la fecha y hora actual.
     *
     * @param conversacion El objeto Conversacion que contiene los datos de la conversación a insertar.
     * @return true si la inserción es exitosa, false en caso contrario.
     */
    public boolean nuevaConversacion(Conversacion conversacion) {
        // Obtiene la fecha y hora actual
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        // Asigna la fecha y hora actual al objeto Conversacion
        conversacion.setFecha_hora(fechaHoraActual);

        // Prepara los datos para la inserción
        Map<String, Object> dataConversacion = new HashMap<>();
        dataConversacion.put("id_usuario", conversacion.getId_usuario());
        dataConversacion.put("fecha_hora", fechaHoraActual);

        // Realiza la inserción en la base de datos
        InsertionResult insertionResult = driver.insertWithId("conversacion", dataConversacion);
        return insertionResult.isSuccess();
    }


    /**
     * Obtiene una lista de registros de conversaciones de la base de datos.
     *
     * @param where           Un mapa que contiene las condiciones de consulta opcionales.
     * @param isConsultaLike  Indica si se debe realizar una consulta con coincidencia parcial (LIKE) en las condiciones.
     * @return Una lista de objetos Conversacion que cumplen con las condiciones de consulta.
     */
    public LinkedList<Conversacion> obtenerConversaciones(Map<String, Object> where, boolean isConsultaLike) {
        LinkedList<Conversacion> conversaciones = new LinkedList<>();
        LinkedList<Map<String, Object>> data;

        if (where == null) {
            data = driver.select("*", "conversacion");
        } else {
            data = driver.select("*", "conversacion", where, isConsultaLike);
        }

        for (Map<String, Object> mapa : data) {
            Conversacion conversacion = new Conversacion();
            conversacion.setId(Integer.parseInt(mapa.get("id").toString()));
            conversacion.setId_usuario(Integer.parseInt(mapa.get("id_usuario").toString()));
            conversacion.setFecha_hora((LocalDateTime) mapa.get("fecha_hora"));
            conversaciones.add(conversacion);
        }

        return conversaciones;
    }

    /**
     * Actualiza un registro de conversación existente en la base de datos.
     *
     * @param conversacion El objeto Conversacion con los datos actualizados.
     * @return true si la actualización es exitosa, false en caso contrario.
     */
    public boolean actualizaConversacion(Conversacion conversacion) {
        Map<String, Object> dataConversacion = new HashMap<>();
        Map<String, Object> whereConversacion = new HashMap<>();

        dataConversacion.put("id_usuario", conversacion.getId_usuario());
        dataConversacion.put("fecha_hora", conversacion.getFecha_hora());

        whereConversacion.put("id", conversacion.getId());

        return driver.update("conversacion", dataConversacion, whereConversacion);
    }

    /**
     * Elimina un registro de conversación de la base de datos.
     *
     * @param conversacion El objeto Conversacion a eliminar.
     * @return true si la eliminación es exitosa, false en caso contrario.
     */
    public boolean eliminaConversacion(Conversacion conversacion) {
        Map<String, Object> whereConversacion = new HashMap<>();
        whereConversacion.put("id", conversacion.getId());
        return driver.delete("conversacion", whereConversacion);
    }
}
