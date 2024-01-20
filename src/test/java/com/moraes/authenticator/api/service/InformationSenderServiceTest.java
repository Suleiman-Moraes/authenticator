package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockParam;
import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;

class InformationSenderServiceTest {

    @Spy
    @InjectMocks
    private InformationSenderService service;

    private MockParam mockParam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockParam = new MockParam();
    }

    @Test
    void testBuildResetPasswordEmail_ValidInput_ReturnsCorrectEmailContent() {
        // Arrange
        final String name = "John Doe";
        final String token = "abc123";
        final String timeExpiration = "24 horas";
        final Param paramUrlFrontend = mockParam.mockEntity(ParamEnum.URL_FRONT_END);

        // Act
        final String emailContent = service.buildResetPasswordEmail(name, token, timeExpiration, paramUrlFrontend);

        // Assert
        final String expectedEmailContent = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Redefinição de Senha</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div style=\"max-width: 600px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif;\">\n" +
            "        <h2>Redefinição de Senha</h2>\n" +
            "        <p>Olá John Doe,</p>\n" +
            "        <p>Recebemos uma solicitação para redefinir a senha da sua conta. Se você não fez essa solicitação, por favor, ignore este e-mail.</p>\n" +
            "        <p>Para redefinir sua senha, clique no link abaixo:</p>\n" +
            "        <a href=\"http://localhost:4200/reset-password/abc123\" style=\"display: inline-block; padding: 10px 20px; background-color: #007BFF; color: #fff; text-decoration: none; border-radius: 5px;\">Redefinir Senha</a>\n" +
            "        <p>Este link expirará em 24 horas.</p>\n" +
            "        <p>Se você estiver enfrentando problemas ao clicar no botão, copie e cole o seguinte URL em seu navegador: http://localhost:4200/reset-password/abc123</p>\n" +
            "        <p>Obrigado,<br>Equipe de Suporte</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>\n";
        assertEquals(expectedEmailContent, emailContent);
    }
}
