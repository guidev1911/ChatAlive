package com.guidev1911.ChatAlive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirmação de Registro - ChatAlive");
        message.setText("Olá,\n\n" +
                "Você solicitou um código de confirmação para concluir seu cadastro no ChatAlive.\n\n" +
                "Seu código de confirmação é: " + code + "\n\n" +
                "⚠️ Este código é válido por apenas 5 minutos.\n" +
                "Caso não tenha solicitado este código, por favor ignore esta mensagem.\n\n" +
                "Atenciosamente,\nEquipe ChatAlive");
        mailSender.send(message);
    }
}
