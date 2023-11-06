package com.tt.becaria.service;

import com.tt.becaria.model.Usuario;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface UsuarioService {
    boolean nuevoUsuario(Usuario usuario);
    LinkedList<Usuario> obtenerUsuarios(Map<String, Object> where, boolean isConsultaLike);
    boolean actualizaUsuario(Usuario usuario);
    boolean eliminaUsuario(Usuario usuario);
}
