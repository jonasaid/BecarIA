package com.tt.becaria.controller;

import com.tt.becaria.model.ResponseData;
import com.tt.becaria.model.Usuario;
import com.tt.becaria.model.UsuarioTemporal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UsuarioController {
    ResponseData getAllUsuarios();
    ResponseData getUsuarioById(@PathVariable int id);
    ResponseData createUsuario(@RequestBody UsuarioTemporal usuario);
    ResponseData updateUsuario(@RequestBody Usuario usuario, @PathVariable int id);
    ResponseData deleteUsuario(@PathVariable int id);
}
