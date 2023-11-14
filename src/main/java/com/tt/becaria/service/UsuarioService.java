package com.tt.becaria.service;

import com.tt.becaria.model.Usuario;
import com.tt.becaria.model.UsuarioTemporal;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface UsuarioService {
    boolean nuevoUsuario(UsuarioTemporal usuario);
    LinkedList<Usuario> obtenerUsuarios(Map<String, Object> where, boolean isConsultaLike);
    boolean actualizaUsuario(Usuario usuario);
    boolean eliminaUsuario(Usuario usuario);
}
