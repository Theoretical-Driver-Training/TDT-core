package com.example.api.payload.response;

import com.example.api.model.token.TokenChangePassword;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordResponse {

    String confirmationLink;

    String subject;

    String content;

    public ChangePasswordResponse(TokenChangePassword changeToken) {
        confirmationLink = "https://localhost:8080/api/profile/change_password?token=" + changeToken.getToken();

        subject = "Изменение пароля";
        content = "<html><body>"
                + "<p style='font-family: Arial, sans-serif;'>Приветствуем вас в системе оценки функциональной готовности водителей автотранспорта!</p>"
                + "<p style='font-family: Arial, sans-serif;'>Вы получили это письмо, потому что инициировали процесс изменения пароля для вашей учетной записи. Если вы не делали такого запроса, немедленно свяжитесь с администрацией системы.</p>"
                + "<p style='font-family: Arial, sans-serif;'><strong>Для установки нового пароля, перейдите по ссылке ниже.</strong> После перехода вы сможете установить и подтвердить ваш новый пароль.</p>"
                + "<div style='text-align: center; margin-top: 20px;'>"
                + "    <a href='" + confirmationLink + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-family: Arial, sans-serif;'>Установить новый пароль</a>"
                + "</div>"
                + "<p style='font-family: Arial, sans-serif;'>Обратите внимание: это письмо сформировано автоматически и ваш ответ на него не будет прочитан.</p>"
                + "</body></html>";
    }
}
