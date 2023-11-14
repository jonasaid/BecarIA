package com.tt.becaria.controller.impl;

import com.tt.becaria.controller.ConversacionController;
import com.tt.becaria.model.Conversacion;
import com.tt.becaria.model.ResponseData;
import com.tt.becaria.model.Usuario;
import com.tt.becaria.service.impl.ConversacionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class ConversacionControllerImpl implements ConversacionController {
    private final ConversacionServiceImpl controller;

    public ConversacionControllerImpl(ConversacionServiceImpl controller) {
        this.controller = controller;
    }

    @GetMapping("/conversacion")
    public ResponseData getAllConversaciones() {
        List<Conversacion> conversaciones = controller.obtenerConversaciones(null, false);
        if (conversaciones.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(), "No se encontraron registros de conversaciones.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), conversaciones);
        }
    }

    @GetMapping("/conversacion/{id}")
    public ResponseData getConversacionById(@PathVariable int id) {
        Map<String, Object> where = new HashMap<>();
        where.put("id", id);
        List<Conversacion> result = controller.obtenerConversaciones(where, false);
        if (result.isEmpty()) {
            return new ResponseData("error", HttpStatus.NOT_FOUND.value(),
                    "No se encontró la conversación con el ID especificado.");
        } else {
            return new ResponseData("success", HttpStatus.OK.value(), result.get(0));
        }
    }

    @PostMapping("/conversacion")
    public ResponseData createConversacion(@RequestBody Conversacion conversacion) {
        boolean success = controller.nuevaConversacion(conversacion);
        if (success) {
            return new ResponseData("success", HttpStatus.CREATED.value(), conversacion);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo crear el registro de conversación.");
        }
    }

    @PutMapping("/conversacion/{id}")
    public ResponseData updateConversacion(@RequestBody Conversacion conversacion, @PathVariable int id) {
        conversacion.setId(id);
        boolean success = controller.actualizaConversacion(conversacion);
        if (success) {
            return new ResponseData("success", HttpStatus.OK.value(), conversacion);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo actualizar el registro de conversación.");
        }
    }

    @DeleteMapping("/conversacion/{id}")
    public ResponseData deleteConversacion(@PathVariable int id) {
        Conversacion conversacion = new Conversacion();
        conversacion.setId(id);
        boolean success = controller.eliminaConversacion(conversacion);
        if (success) {
            return new ResponseData("success", HttpStatus.OK.value(), "Registro de conversación eliminado exitosamente.");
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo eliminar el registro de conversación.");
        }
    }
}
