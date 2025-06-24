package com.guidev1911.ChatAlive.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent =
                    "<p>Olá,</p>" +
                            "<p>Você solicitou um código de confirmação para concluir seu cadastro no <strong>ChatAlive</strong>.</p>" +
                            "<p><strong>Seu código de confirmação é:</strong> <span style='font-size: 16px; color: #00FFFF;'><strong>" + code + "</strong></span></p>" +
                            "<p>⚠️ Este código é válido por apenas <strong>5 minutos</strong>.</p>" +
                            "<p style='color: #999;'>Caso você não tenha solicitado este código, por favor ignore esta mensagem.</p>" +
                            "<p>Atenciosamente,<br><em>Equipe ChatAlive</em></p>";

            helper.setTo(to);
            helper.setSubject("Confirmação de Registro - ChatAlive");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail de confirmação de registro: " + e.getMessage());
        }
    }

    public void sendResetCode(String to, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String htmlContent =
                    "<p>Olá,</p>" +
                            "<p>Recebemos uma solicitação para redefinir a senha da sua conta no <strong>ChatAlive</strong>.</p>" +
                            "<p><strong>Seu código de verificação é:</strong> <span style='font-size: 16px; color: #00FFFF;'><strong>" + code + "</strong></span></p>" +
                            "<p>⚠️ Este código é válido por apenas <strong>5 minutos</strong>.</p>" +
                            "<p style='color: #999;'>Caso você não tenha solicitado esta redefinição, por favor ignore esta mensagem.</p>" +
                            "<p>Atenciosamente,<br><em>Equipe ChatAlive</em></p>";

            helper.setTo(to);
            helper.setSubject("Redefinição de Senha - ChatAlive");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail de redefinição de senha: " + e.getMessage());
        }
    }
}
