package com.tt.becaria.service;

import com.tt.becaria.model.Pregunta;

import java.util.LinkedList;
import java.util.Map;

public interface PreguntaService {
    boolean nuevaPregunta(Pregunta pregunta);
    LinkedList<Pregunta> obtenerPreguntas(Map<String, Object> where, boolean isConsultaLike);
    boolean actualizaPregunta(Pregunta pregunta);
    boolean eliminaPregunta(int id);

}
