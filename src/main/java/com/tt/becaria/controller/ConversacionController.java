package com.tt.becaria.controller;

import com.tt.becaria.model.Conversacion;
import com.tt.becaria.model.ResponseData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ConversacionController {
    ResponseData getAllConversaciones();
    ResponseData getConversacionById(@PathVariable int id);
    ResponseData createConversacion(@RequestBody Conversacion conversacion);
    ResponseData updateConversacion(@RequestBody Conversacion conversacion, @PathVariable int id);
    ResponseData deleteConversacion(@PathVariable int id);
}
