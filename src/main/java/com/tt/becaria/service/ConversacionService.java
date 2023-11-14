package com.tt.becaria.service;

import com.tt.becaria.model.Conversacion;

import java.util.LinkedList;
import java.util.Map;

public interface ConversacionService {
    boolean nuevaConversacion(Conversacion conversacion);
    LinkedList<Conversacion> obtenerConversaciones(Map<String, Object> where, boolean isConsultaLike);
    boolean actualizaConversacion(Conversacion conversacion);
    boolean eliminaConversacion(Conversacion conversacion);
}
