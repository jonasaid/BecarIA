package com.tt.becaria.controller.impl;

import com.tt.becaria.controller.UsuarioController;
import com.tt.becaria.model.UsuarioTemporal;
import com.tt.becaria.service.impl.UsuarioServiceImpl;
import com.tt.becaria.model.ResponseData;
import com.tt.becaria.model.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase es un controlador de endpoints relacionados con la entidad Usuario.
 * Proporciona endpoints para la gestión de registros Usuario, como la obtención
 * de todos los registros,
 * la obtención de un registro por ID, la creación de nuevos registros, la
 * actualización de registros existentes
 * y la eliminación de registros.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UsuarioControllerImpl implements UsuarioController {
    private final UsuarioServiceImpl controller;

    /**
     * Constructor de la clase UsuarioControllerImpl.
     *
     * @param controller El controlador UsuarioServiceImpl utilizado para realizar
     *                   las operaciones de gestión de registros Usuario.
     */
    public UsuarioControllerImpl(UsuarioServiceImpl controller) {
        this.controller = controller;
    }

    /**
     * Obtiene todos los registros de Usuario.
     *
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se encuentran registros de Usuario, se devuelve una respuesta con
     *         estado "success", el código HTTP correspondiente y los registros.
     *         Si no se encuentran registros, se devuelve una respuesta con estado
     *         "error", el código HTTP correspondiente y un mensaje de error.
     */
    @GetMapping("/usuario")
    public ResponseData getAllUsuarios() {
        List<Usuario> usuarios = controller.obtenerUsuarios(null, false);
        if (usuarios.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(), "No se encontraron registros de usuarios.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), usuarios);
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
    @GetMapping("/usuario/{id}")
    public ResponseData getUsuarioById(@PathVariable int id) {
        Map<String, Object> where = new HashMap<>();
        where.put("id", id);
        List<Usuario> result = controller.obtenerUsuarios(where, false);
        if (result.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(),
                    "No se encontró usuario con el ID especificado.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), result.get(0));
        }
    }

    /**
     * Crea un nuevo registro de Usuario.
     *
     * @param usuario El objeto Usuario que contiene los datos a crear.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se crea el registro de Usuario correctamente, se devuelve una
     *         respuesta con estado "success", el código HTTP correspondiente y el
     *         registro creado.
     *         Si no se puede crear el registro, se devuelve una respuesta con
     *         estado "error", el código HTTP correspondiente y un mensaje de error.
     */
    @PostMapping("/usuario")
    public ResponseData createUsuario(@RequestBody UsuarioTemporal usuario) {
        boolean success = controller.nuevoUsuario(usuario);
        if (success) {
            return new ResponseData("success", HttpStatus.CREATED.value(), usuario);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo crear el registro de usuario.");
        }
    }

    /**
     * Actualiza un registro de Usuario existente.
     *
     * @param usuario El objeto Usuario que contiene los nuevos datos a actualizar.
     * @param id     El ID del registro de Usuario a actualizar.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se actualiza el registro de Usuario correctamente, se devuelve una
     *         respuesta con estado "success", el código HTTP correspondiente y el
     *         registro actualizado.
     *         Si no se puede actualizar el registro, se devuelve una respuesta con
     *         estado "error", el código HTTP correspondiente y un mensaje de error.
     */
    @PutMapping("/usuario/{id}")
    public ResponseData updateUsuario(@RequestBody Usuario usuario, @PathVariable int id) {
        usuario.setId(id);
        boolean success = controller.actualizaUsuario(usuario);
        if (success) {
            return new ResponseData("success", HttpStatus.OK.value(), usuario);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo actualizar el registro de usuario.");
        }
    }

    /**
     * Elimina un registro de Usuario.
     *
     * @param id El ID del registro de Usuario a eliminar.
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se elimina el registro de Usuario correctamente, se devuelve una
     *         respuesta con estado "success", el código HTTP correspondiente y un
     *         mensaje de éxito.
     *         Si no se puede eliminar el registro, se devuelve una respuesta con
     *         estado "error", el código HTTP correspondiente y un mensaje de error.
     */
    @DeleteMapping("/usuario/{id}")
    public ResponseData deleteUsuario(@PathVariable int id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        boolean success = controller.eliminaUsuario(usuario);
        if (success) {
            return new ResponseData("success", HttpStatus.OK.value(), "Registro de usuario eliminado exitosamente.");
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo eliminar el registro de usuario.");
        }
    }
}
