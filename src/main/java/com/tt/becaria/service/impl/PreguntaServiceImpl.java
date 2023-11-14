package com.tt.becaria.service.impl;

import com.tt.becaria.model.InsertionResult;
import com.tt.becaria.model.Pregunta;
import com.tt.becaria.service.PreguntaService;
import com.tt.becaria.util.DbDriver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Component
public class PreguntaServiceImpl implements PreguntaService {
    private final DbDriver driver = DbDriver.getInstance();

    @Override
    public boolean nuevaPregunta(Pregunta pregunta) {
        Map<String, Object> dataPregunta = new HashMap<>();
        dataPregunta.put("id_conversacion", pregunta.getId_conversacion());
        dataPregunta.put("pregunta", pregunta.getPregunta());
        dataPregunta.put("respuesta", pregunta.getRespuesta());

        InsertionResult insertionResult = driver.insertWithId("pregunta", dataPregunta);
        return insertionResult.isSuccess();
    }

    @Override
    public LinkedList<Pregunta> obtenerPreguntas(Map<String, Object> where, boolean isConsultaLike) {
        LinkedList<Pregunta> preguntas = new LinkedList<>();
        LinkedList<Map<String, Object>> data;

        if (where == null) {
            data = driver.select("*", "pregunta");
        } else {
            data = driver.select("*", "pregunta", where, isConsultaLike);
        }

        for (Map<String, Object> mapa : data) {
            Pregunta pregunta = new Pregunta();
            pregunta.setId(Integer.parseInt(mapa.get("id").toString()));
            pregunta.setId_conversacion(Integer.parseInt(mapa.get("id_conversacion").toString()));
            pregunta.setPregunta(mapa.get("pregunta").toString());
            pregunta.setRespuesta(mapa.get("respuesta").toString());
            preguntas.add(pregunta);
        }

        return preguntas;
    }

    @Override
    public boolean actualizaPregunta(Pregunta pregunta) {
        Map<String, Object> dataPregunta = new HashMap<>();
        Map<String, Object> wherePregunta = new HashMap<>();

        dataPregunta.put("id_conversacion", pregunta.getId_conversacion());
        dataPregunta.put("pregunta", pregunta.getPregunta());
        dataPregunta.put("respuesta", pregunta.getRespuesta());

        wherePregunta.put("id", pregunta.getId());

        return driver.update("pregunta", dataPregunta, wherePregunta);
    }

    @Override
    public boolean eliminaPregunta(int id) {
        Map<String, Object> wherePregunta = new HashMap<>();
        wherePregunta.put("id", id);
        return driver.delete("pregunta", wherePregunta);
    }
}
