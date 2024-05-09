package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupMailResponse {

    String subject;

    String content;

    public SignupMailResponse() {
        subject = "Добро пожаловать в систему!";
        content = "<html><body>"
                + "<p style='font-family: Arial, sans-serif;'>Добро пожаловать в систему оценки функциональной готовности водителей автотранспорта!</p>"
                + "<p style='font-family: Arial, sans-serif;'>Мы рады приветствовать вас в нашей системе. Ваша учетная запись успешно создана, и вы можете начать пользоваться всеми доступными функциями.</p>"
                + "<p style='font-family: Arial, sans-serif;'>Спасибо, что выбрали нас!</p>"
                + "<p style='font-family: Arial, sans-serif;'>Обратите внимание: это письмо сформировано автоматически и ваш ответ на него не будет прочитан.</p>"
                + "</body></html>";
    }
}
