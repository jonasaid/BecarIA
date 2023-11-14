package com.tt.becaria.service.impl;

import com.tt.becaria.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Esta clase es un controlador para el envío de correos electrónicos.
 * Permite enviar correos electrónicos utilizando el servicio de envío de correo de Spring.
 */
@Service
@PropertySource("classpath:application.properties")
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String correoDestinatario;

    /**
     * Crea una instancia del controlador EmailServiceImpl con el JavaMailSender especificado.
     *
     * @param javaMailSender El JavaMailSender utilizado para enviar correos electrónicos.
     */
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Envía un correo electrónico al destinatario especificado con el asunto y contenido dados.
     *
     * @param destinatario El destinatario del correo electrónico.
     * @param asunto       El asunto del correo electrónico.
     * @param contenido    El contenido del correo electrónico.
     */
    public boolean enviarCorreoDestinatario(String destinatario, String asunto, String contenido) {
        try {
            MimeMessage correo = javaMailSender.createMimeMessage();
            // true para contenido multipart (HTML y texto)
            MimeMessageHelper mensaje = new MimeMessageHelper(correo, true);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            // true para interpretar el contenido como HTML
            mensaje.setText(contenido, true);

            javaMailSender.send(correo);
            return true;
        } catch (MailException e) {
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Envía un correo electrónico a el mail que esta configurado en application.properties con un asunto fijo y contenido dado.
     *
     * @param remitente    El remitente del correo electrónico.
     * @param contenido    El contenido del correo electrónico.
     */
    public boolean enviarCorreo(String remitente, String contenido) {
        try {
            MimeMessage correo = javaMailSender.createMimeMessage();
            // true para contenido multipart (HTML y texto)
            MimeMessageHelper mensaje = new MimeMessageHelper(correo, true);
            mensaje.setTo(correoDestinatario);
            mensaje.setSubject("BecarIA");
            mensaje.setText(contenido,true);

            javaMailSender.send(correo);
            return true;
        } catch (MailException e) {
            return false;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
