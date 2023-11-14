package com.tt.becaria.service.impl;

import com.tt.becaria.model.UsuarioTemporal;
import com.tt.becaria.util.Cripto;
import com.tt.becaria.util.DbDriver;
import com.tt.becaria.util.GeneradorCodigoValidacion;
import com.tt.becaria.model.InsertionResult;
import com.tt.becaria.service.UsuarioTemporalService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Esta clase es un controlador para la entidad UsuarioTemporal.
 * Proporciona métodos para la creación, obtención, actualización y eliminación de registros de UsuarioTemporal.
 */
@Component
public class UsuarioTemporalServiceImpl implements UsuarioTemporalService {
    private final DbDriver driver = DbDriver.getInstance();
    @Autowired
    private JavaMailSender sender;

    /**
     * Crea un nuevo registro de UsuarioTemporal.
     *
     * @param usuario El objeto UsuarioTemporal que contiene los datos a crear.
     * @return true si se crea el registro de UsuarioTemporal correctamente, false de lo contrario.
     */
    @Override
    public boolean nuevoUsuarioTemporal(UsuarioTemporal usuario) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", usuario.getNombre());
        data.put("password", Cripto.cifrar(usuario.getPassword()));
        data.put("correo", usuario.getCorreo());
        String codigoValidacion = GeneradorCodigoValidacion.generarCodigoValidacion();
        data.put("codigo_validacion", codigoValidacion);

        EmailServiceImpl emailService = new EmailServiceImpl(sender);
        // Enviar código de validación al correo electrónico del usuario
        String mensajeCorreo = "Su código de validación es: " + codigoValidacion;
        boolean envioExitoso = emailService.enviarCorreoDestinatario(usuario.getCorreo(), "Código de Validación", mensajeCorreo);

        if (envioExitoso) {
            InsertionResult insertionResult = driver.insertWithId("usuario_temporal", data);
            return insertionResult.isSuccess();
        }else{
            return false;
        }
    }

    /**
     * Obtiene una lista de registros de UsuarioTemporal según los criterios de búsqueda especificados.
     *
     * @param where           Un mapa que contiene los criterios de búsqueda en forma de pares atributo-valor.
     * @param isConsultaLike  Indica si la búsqueda es una consulta "LIKE" (parcial) o una coincidencia exacta.
     * @return Una lista enlazada de objetos UsuarioTemporal que coinciden con los criterios de búsqueda.
     */
    @Override
    public LinkedList<UsuarioTemporal> obtenerUsuariosTemporales(Map<String, Object> where, boolean isConsultaLike) {
        LinkedList<UsuarioTemporal> usuariosTemporales = new LinkedList<>();

        LinkedList<Map<String, Object>> data;
        if (where == null) {
            data = driver.select("*", "usuario_temporal");
        } else {
            data = driver.select("*", "usuario_temporal", where, isConsultaLike);
        }

        for (Map<String, Object> mapa : data) {
            usuariosTemporales.add(new UsuarioTemporal(
                    Integer.parseInt(mapa.get("id").toString()),
                    mapa.get("nombre").toString(),
                    Cripto.descifrar(mapa.get("password").toString()),
                    mapa.get("correo").toString(),
                    mapa.get("codigo_validacion").toString()
            ));
        }

        return usuariosTemporales;
    }

    /**
     * Elimina un registro de UsuarioTemporal.
     *
     * @param usuario El objeto UsuarioTemporal que especifica el registro a eliminar.
     * @return true si se elimina el registro de UsuarioTemporal correctamente, false de lo contrario.
     */
    @Override
    public boolean eliminaUsuarioTemporal(UsuarioTemporal usuario) {
        Map<String, Object> where = new HashMap<>();
        where.put("id", usuario.getId());
        return driver.delete("usuario_temporal", where);
    }
}
