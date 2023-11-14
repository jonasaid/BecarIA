package com.tt.becaria.service;

import com.tt.becaria.model.UsuarioTemporal;

import java.util.LinkedList;
import java.util.Map;

public interface UsuarioTemporalService {
    boolean nuevoUsuarioTemporal(UsuarioTemporal usuario);
    LinkedList<UsuarioTemporal> obtenerUsuariosTemporales(Map<String, Object> where, boolean isConsultaLike);

    boolean eliminaUsuarioTemporal(UsuarioTemporal usuario);
}
