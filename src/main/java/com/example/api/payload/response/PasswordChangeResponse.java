package com.example.api.payload.response;

import com.example.api.model.token.PasswordChangeToken;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeResponse {

    String resetLink;

    String subject;

    String content;

    public PasswordChangeResponse(PasswordChangeToken resetToken) {
        resetLink = "https://localhost:8080/api/auth/reset_password?token=" + resetToken.getToken();

        //todo поменять

        subject = "Password Reset Request";
        content = "Hello,\n" +
                "You have requested to reset your password.\n" +
                "Click the link below to change your password:\n" +
                resetLink + "\n\n" +
                "Ignore this email if you remember your password, or you have not made the request.";
    }

}
