package com.tt.becaria.controller;

import com.tt.becaria.model.ResponseData;
import com.tt.becaria.model.UsuarioTemporal;

public interface UsuarioTemporalController {
    ResponseData getAllUsuariosTemporales();
    ResponseData getUsuarioById(int id);
    ResponseData createUsuarioTemporal(UsuarioTemporal usuario);
    ResponseData deleteUsuarioTemporal(int id);
}
