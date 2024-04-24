package com.example.api.payload.response;

import com.example.api.model.token.PasswordChangeToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordForgotResponse {

    private String activationLink;
    private String subject;
    private String content;

    public PasswordForgotResponse(PasswordChangeToken resetToken) {
        activationLink = "https://localhost:8080/api/profile/forgot_password?token=" + resetToken.getToken();

        subject = "Активация нового пароля";
        content = "<html><body>"
                + "<p style='font-family: Arial, sans-serif;'>Приветствуем вас в системе оценки функциональной готовности водителей автотранспорта!</p>"
                + "<p style='font-family: Arial, sans-serif;'>Вы получили это письмо, потому что запросили восстановление доступа к вашей учетной записи. Если вы не делали такого запроса, просто проигнорируйте это сообщение. В случае повторения таких писем, рекомендуем обратиться к администратору системы.</p>"
                + "<p style='font-family: Arial, sans-serif;'><strong>Для восстановления доступа к учетной записи, перейдите по ссылке ниже.</strong> После перехода вы сможете установить новый пароль.</p>"
                + "<div style='text-align: center; margin-top: 20px;'>"
                + "    <a href='" + activationLink + "' style='background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; border-radius: 5px; font-family: Arial, sans-serif;'>Восстановить пароль</a>"
                + "</div>"
                + "<p style='font-family: Arial, sans-serif;'>Обратите внимание: это письмо сформировано автоматически и ваш ответ на него не будет прочитан.</p>"
                + "</body></html>";
    }
}
