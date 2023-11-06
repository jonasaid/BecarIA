package com.tt.becaria.service;

public interface EmailService {
    boolean enviarCorreoDestinatario(String destinatario, String asunto, String contenido);
    boolean enviarCorreo(String remitente, String contenido);
}
