package com.moraes.authenticator.api.service;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.service.interfaces.IInformationSenderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InformationSenderService implements IInformationSenderService {

    @Override
    public void sendEmailResetPassword(User user, String timeExpiration, Param paramUrlFrontend) {
        buildResetPasswordEmail(user.getPerson().getName(), user.getTokenResetPassword().toString(), String.format("%s horas", timeExpiration), paramUrlFrontend);
    }

    public String buildResetPasswordEmail(String name, String token, String timeExpiration, Param paramUrlFrontend) {
        final String resetLink = String.format("%s/reset-password/%s", paramUrlFrontend.getValue(), token);
        StringBuilder emailContent = new StringBuilder();

        emailContent.append("<!DOCTYPE html>\n");
        emailContent.append("<html lang=\"en\">\n");
        emailContent.append("<head>\n");
        emailContent.append("    <meta charset=\"UTF-8\">\n");
        emailContent.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        emailContent.append("    <title>Redefinição de Senha</title>\n");
        emailContent.append("</head>\n");
        emailContent.append("<body>\n");
        emailContent.append("    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;\">\n");
        emailContent.append("        <h2>Redefinição de Senha</h2>\n");
        emailContent.append("        <p>Olá ").append(name).append(",</p>\n");
        emailContent.append("        <p>Recebemos uma solicitação para redefinir a senha da sua conta. Se você não fez essa solicitação, por favor, ignore este e-mail.</p>\n");
        emailContent.append("        <p>Para redefinir sua senha, clique no link abaixo:</p>\n");
        emailContent.append("        <a href=\"").append(resetLink);
        emailContent.append("\" style=\"display: inline-block; padding: 10px 20px; background-color: #007BFF; color: #fff; text-decoration: none; border-radius: 5px;\">Redefinir Senha</a>\n");
        emailContent.append("        <p>Este link expirará em ").append(timeExpiration).append(".</p>\n");
        emailContent.append("        <p>Se você estiver enfrentando problemas ao clicar no botão, copie e cole o seguinte URL em seu navegador: ");
        emailContent.append(resetLink).append("</p>\n");
        emailContent.append("        <p>Obrigado,<br>Equipe de Suporte</p>\n");
        emailContent.append("    </div>\n");
        emailContent.append("</body>\n");
        emailContent.append("</html>\n");

        log.info("Email content: " + emailContent.toString());
        return emailContent.toString();
    }
}
