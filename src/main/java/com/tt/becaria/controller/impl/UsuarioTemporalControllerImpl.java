package com.tt.becaria.controller.impl;

import com.tt.becaria.controller.UsuarioTemporalController;
import com.tt.becaria.model.ResponseData;
import com.tt.becaria.model.Usuario;
import com.tt.becaria.model.UsuarioTemporal;
import com.tt.becaria.service.impl.UsuarioTemporalServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase es un controlador de endpoints relacionados con la entidad UsuarioTemporal.
 * Proporciona endpoints para la gestión de registros UsuarioTemporal, como la obtención
 * de todos los registros,
 * la creación de nuevos registros y la eliminación de registros.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UsuarioTemporalControllerImpl implements UsuarioTemporalController {
    private final UsuarioTemporalServiceImpl controller;

    /**
     * Constructor de la clase UsuarioTemporalControllerImpl.
     *
     * @param controller El controlador UsuarioTemporalServiceImpl utilizado para realizar
     *                   las operaciones de gestión de registros UsuarioTemporal.
     */
    public UsuarioTemporalControllerImpl(UsuarioTemporalServiceImpl controller) {
        this.controller = controller;
    }

    /**
     * Obtiene todos los registros de UsuarioTemporal.
     *
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se encuentran registros de UsuarioTemporal, se devuelve una respuesta con
     *         estado "success", el código HTTP correspondiente y los registros.
     *         Si no se encuentran registros, se devuelve una respuesta con estado
     *         "error", el código HTTP correspondiente y un mensaje de error.
     */
    @GetMapping("/usuario_temporal")
    public ResponseData getAllUsuariosTemporales() {
        LinkedList<UsuarioTemporal> usuariosTemporales = controller.obtenerUsuariosTemporales(null, false);
        if (usuariosTemporales.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(), "No se encontraron registros de usuarios temporales.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), usuariosTemporales);
        }
    }

    /**
     * Obtiene un registro de Usuario por su ID.
     *
     * @param id El ID del registro de Usuario a obtener.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se encuentra el registro de Usuario, se devuelve una respuesta con
     *         estado "success", el código HTTP correspondiente y el registro.
     *         Si no se encuentra el registro, se devuelve una respuesta con estado
     *         "error", el código HTTP correspondiente y un mensaje de error.
     */
    @GetMapping("/usuario_temporal/{id}")
    public ResponseData getUsuarioById(@PathVariable int id) {
        Map<String, Object> where = new HashMap<>();
        where.put("id", id);
        List<UsuarioTemporal> result = controller.obtenerUsuariosTemporales(where, false);
        if (result.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(),
                    "No se encontró usuario con el ID especificado.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), result.get(0));
        }
    }

    /**
     * Crea un nuevo registro de UsuarioTemporal.
     *
     * @param usuario El objeto UsuarioTemporal que contiene los datos a crear.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se crea el registro de UsuarioTemporal correctamente, se devuelve una
     *         respuesta con estado "success", el código HTTP correspondiente y el
     *         registro creado.
     *         Si no se puede crear el registro, se devuelve una respuesta con
     *         estado "error", el código HTTP correspondiente y un mensaje de error.
     */
    @PostMapping("/usuario_temporal")
    public ResponseData createUsuarioTemporal(@RequestBody UsuarioTemporal usuario) {
        boolean success = controller.nuevoUsuarioTemporal(usuario);
        if (success) {
            return new ResponseData("success", HttpStatus.CREATED.value(), usuario);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo crear el registro de usuario temporal.");
        }
    }

    /**
     * Elimina un registro de UsuarioTemporal.
     *
     * @param id El ID del registro de UsuarioTemporal a eliminar.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se elimina el registro de UsuarioTemporal correctamente, se devuelve una
     *         respuesta con estado "success", el código HTTP correspondiente y un
     *         mensaje de éxito.
     *         Si no se puede eliminar el registro, se devuelve una respuesta con
     *         estado "error", el código HTTP correspondiente y un mensaje de error.
     */
    @DeleteMapping("/usuario_temporal/{id}")
    public ResponseData deleteUsuarioTemporal(@PathVariable int id) {
        UsuarioTemporal usuarioTemporal = new UsuarioTemporal();
        usuarioTemporal.setId(id);
        boolean success = controller.eliminaUsuarioTemporal(usuarioTemporal);
        if (success) {
            return new ResponseData("success", HttpStatus.OK.value(), "Registro de usuario temporal eliminado exitosamente.");
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo eliminar el registro de usuario temporal.");
        }
    }
}
