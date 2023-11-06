package com.tt.becaria.controller;

import com.tt.becaria.model.Email;
import com.tt.becaria.model.EmailRemitente;
import com.tt.becaria.model.ResponseData;

public interface EmailController {
    ResponseData enviarCorreoDestinatario(Email email);
    ResponseData enviarCorreo(EmailRemitente email);
}
