package com.tt.becaria.service.impl;

import com.tt.becaria.model.UsuarioTemporal;
import com.tt.becaria.util.Cripto;
import com.tt.becaria.util.DbDriver;
import com.tt.becaria.util.GeneradorCodigoValidacion;
import com.tt.becaria.model.InsertionResult;
import com.tt.becaria.model.Usuario;
import com.tt.becaria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Esta clase es un controlador para la entidad Usuario.
 * Proporciona métodos para la creación, obtención, actualización y eliminación de registros de Usuario.
 */
@Component
public class UsuarioServiceImpl implements UsuarioService {
    /**
     * Atributo de la clase DbDriver que permite manejar la información en la base de datos, a través,
     * de usar los métodos asociados al CRUD.
     */
    private final DbDriver driver = DbDriver.getInstance();

    /**
     * Crea un nuevo registro de Usuario.
     *
     * @param usuario El objeto Usuario que contiene los datos a crear.
     * @return true si se crea el registro de Usuario correctamente, false de lo contrario.
     */
    public boolean nuevoUsuario(UsuarioTemporal usuario) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", usuario.getNombre());
        data.put("password", Cripto.cifrar(usuario.getPassword()));
        data.put("correo", usuario.getCorreo());

        UsuarioTemporalServiceImpl usuarioTemporalService = new UsuarioTemporalServiceImpl();
        LinkedList<UsuarioTemporal> listaUsuarioTemporal = usuarioTemporalService.obtenerUsuariosTemporales(data, false);

        // Verificar si existe algún UsuarioTemporal con los mismos datos
        boolean usuarioExistente = listaUsuarioTemporal.stream()
                .anyMatch(registro -> registro.getNombre().equals(usuario.getNombre())
                        && Cripto.descifrar(registro.getPassword()).equals(Cripto.descifrar(usuario.getPassword()))
                        && registro.getCorreo().equals(usuario.getCorreo()));

        // Comprueba si el código de validación enviado por correo es igual al código introducido
        if (usuarioExistente) {
            boolean eliminarExito = driver.delete("usuario_temporal", data);
            if (eliminarExito) {
                InsertionResult insertionResult = driver.insertWithId("usuario", data);
                return insertionResult.isSuccess();
            }
            return eliminarExito;
        } else {
            return false;
        }
    }


    /**
     * Obtiene una lista de registros de Usuario según los criterios de búsqueda especificados.
     *
     * @param where           Un mapa que contiene los criterios de búsqueda en forma de pares atributo-valor.
     * @param isConsultaLike  Indica si la búsqueda es una consulta "LIKE" (parcial) o una coincidencia exacta.
     * @return Una lista enlazada de objetos Usuario que coinciden con los criterios de búsqueda.
     */
    public LinkedList<Usuario> obtenerUsuarios(Map<String, Object> where, boolean isConsultaLike) {
        LinkedList<Usuario> usuarios = new LinkedList<>();

        LinkedList<Map<String, Object>> data;
        if (where == null)
            data = driver.select("*", "usuario");
        else
            data = driver.select("*", "usuario", where, isConsultaLike);

        for (Map<String, Object> mapa : data) {
            usuarios.add(new Usuario(
                    Integer.parseInt(mapa.get("id").toString()),
                    mapa.get("nombre").toString(),
                    Cripto.descifrar(mapa.get("password").toString()),
                    mapa.get("correo").toString()
            ));
        }

        return usuarios;
    }

    /**
     * Actualiza un registro existente de Usuario.
     *
     * @param usuario El objeto Usuario que contiene los nuevos datos a actualizar.
     * @return true si se actualiza el registro de Usuario correctamente, false de lo contrario.
     */
    public boolean actualizaUsuario(Usuario usuario) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> where = new HashMap<>();

        data.put("nombre", usuario.getNombre());
        data.put("password", Cripto.cifrar(usuario.getPassword()));
        data.put("correo", usuario.getCorreo());

        where.put("id", usuario.getId());

        return driver.update("usuario", data, where);
    }

    /**
     * Elimina un registro de Usuario.
     *
     * @param usuario El objeto Usuario que especifica el registro a eliminar.
     * @return true si se elimina el registro de Usuario correctamente, false de lo contrario.
     */
    public boolean eliminaUsuario(Usuario usuario) {
        Map<String, Object> where = new HashMap<>();
        where.put("id", usuario.getId());
        return driver.delete("usuario", where);
    }
}
