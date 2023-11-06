package com.tt.becaria.controller.impl;

import com.tt.becaria.controller.EmailController;
import com.tt.becaria.model.Email;
import com.tt.becaria.model.EmailRemitente;
import com.tt.becaria.model.ResponseData;
import com.tt.becaria.service.impl.EmailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Esta clase es un controlador de endpoints relacionados con el envío de
 * correos electrónicos.
 * Proporciona un endpoint para enviar correos electrónicos utilizando el
 * controlador EmailServiceImpl.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class EmailControllerImpl implements EmailController {

    private final EmailServiceImpl emailServiceImpl;

    /**
     * Crea una instancia del controlador EmailControllerImpl con el
     * EmailServiceImpl especificado.
     *
     * @param emailServiceImpl El EmailServiceImpl utilizado para enviar correos
     *                         electrónicos.
     */
    @Autowired
    public EmailControllerImpl(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    /**
     * Endpoint para enviar un correo electrónico.
     * Recibe un objeto Email en el cuerpo de la solicitud y utiliza el
     * EmailServiceImpl para enviar el correo electrónico.
     *
     * @param email El objeto Email que contiene los datos del correo electrónico a
     *              enviar.
     *
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se logra enviar el correo, se devuelve una respuesta con
     *         estado "success", el código HTTP correspondiente y el objeto Email.
     *         Si no se encuentran registros, se devuelve una respuesta con estado
     *         "error", el código HTTP correspondiente y un mensaje de error.
     *
     */
    @PostMapping("/enviar_correo_destinatario")
    public ResponseData enviarCorreoDestinatario(@RequestBody Email email) {
        // Obtener los datos del correo electrónico desde el cuerpo de la solicitud
        // Enviar el correo electrónico
        boolean success = emailServiceImpl.enviarCorreoDestinatario(email.getDestinatario(), email.getAsunto(), email.getContenido());
        if (success) {
            return new ResponseData("success", HttpStatus.CREATED.value(), email);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo enviar el correo.");
        }
    }

    /**
     * Endpoint para enviar un correo electrónico.
     * Recibe un objeto EmailRemitente en el cuerpo de la solicitud y utiliza el
     * EmailServiceImpl para enviar el correo electrónico.
     *
     * @param email El objeto EmailRemitente que contiene los datos del correo electrónico a
     *              enviar.
     *
     * @return Un objeto ResponseData que contiene la respuesta de la solicitud.
     *         Si se logra enviar el correo, se devuelve una respuesta con
     *         estado "success", el código HTTP correspondiente y el objeto EmailRemitente.
     *         Si no se encuentran registros, se devuelve una respuesta con estado
     *         "error", el código HTTP correspondiente y un mensaje de error.
     *
     */
    @PostMapping("/enviar_correo")
    public ResponseData enviarCorreo(@RequestBody EmailRemitente email) {
        // Obtener los datos del correo electrónico desde el cuerpo de la solicitud
        // Enviar el correo electrónico
        boolean success = emailServiceImpl.enviarCorreo(email.getRemitente(), email.getContenido());
        if (success) {
            return new ResponseData("success", HttpStatus.CREATED.value(), email);
        } else {
            return new ResponseData("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "No se pudo enviar el correo.");
        }
    }
}
