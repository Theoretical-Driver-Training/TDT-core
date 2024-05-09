package com.example.api.service.profile;

import com.example.api.model.token.PasswordChangeToken;
import com.example.api.model.user.User;
import com.example.api.payload.response.PasswordChangeResponse;
import com.example.api.payload.response.PasswordForgotResponse;
import com.example.api.service.mail.MailService;
import com.example.api.service.token.BlacklistTokenService;
import com.example.api.service.token.PasswordChangeTokenService;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProfilePasswordService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordChangeTokenService passwordChangeTokenService;

    @Autowired
    private BlacklistTokenService blacklistTokenService;

    @Autowired
    private MailService mailService;

    public ResponseEntity<?> resetPassword(String bearerToken) {
        log.info("Change password request received");
        if (!tokenService.isValidBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Invalid bearer token");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        String username = tokenService.getUserNameFromJwtToken(token);
        User user = userDetailsService.getUserEntityByUsername(username);

        PasswordChangeToken passwordChangeToken = passwordChangeTokenService.getOnCreateChangePasswordToken(user);
        PasswordChangeResponse passwordChangeResponse = new PasswordChangeResponse(passwordChangeToken);

        mailService.sendMail(user.getUsername(), passwordChangeResponse.getSubject(), passwordChangeResponse.getContent());

        log.info("Password change request completed");
        return ResponseEntity.ok().body("Password change request completed");
    }

    public ResponseEntity<?> changePassword(String changeToken, String oldPassword, String newPassword) {
        log.info("Changing password");
        if (!passwordChangeTokenService.isValidChangeToken(changeToken)) {
            return ResponseEntity.badRequest().body("Invalid bearer token");
        }

        String token = passwordChangeTokenService.extractChangeToken(changeToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        Optional<PasswordChangeToken> changeTokenOptional = passwordChangeTokenService.getByToken(token);

        User user = changeTokenOptional.get().getUser();
        if (!userDetailsService.isValidOldPassword(user, oldPassword)) {
            return ResponseEntity.badRequest().body("Old password is not the same");
        }
        if (!userDetailsService.isValidNewPassword(newPassword, oldPassword)) {
            return ResponseEntity.badRequest().body("New password is the same as old password");
        }

        userDetailsService.updatePassword(user, newPassword);

        blacklistTokenService.saveToken(token);

        log.info("Password changed successfully");
        return ResponseEntity.ok().body("Password changed successfully");
    }

    public ResponseEntity<?> forgotPassword(String username) {
        log.info("Forgot password request received");
        User user = userDetailsService.getUserEntityByUsername(username);

        PasswordChangeToken passwordChangeToken = passwordChangeTokenService.getOnCreateChangePasswordToken(user);
        PasswordForgotResponse passwordForgotResponse = new PasswordForgotResponse(passwordChangeToken);

        mailService.sendMail(user.getUsername(), passwordForgotResponse.getSubject(), passwordForgotResponse.getContent());

        log.info("Forgot password request completed");
        return ResponseEntity.ok().body("Password forgot successfully");
    }
}
