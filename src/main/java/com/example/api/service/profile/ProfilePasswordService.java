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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilePasswordService {

    private final Logger logger = LoggerFactory.getLogger(ProfilePasswordService.class);

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
        logger.info("Change password request received");
        if (!tokenService.isValidBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Invalid bearer token");
        }

        String token = tokenService.extractBearerToken(bearerToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        String username = tokenService.getUserNameFromJwtToken(token);
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (userDetailsService.isUserIsEmpty(userOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        PasswordChangeToken passwordChangeToken = passwordChangeTokenService.getOnCreateChangePasswordToken(user);
        PasswordChangeResponse passwordChangeResponse = new PasswordChangeResponse(passwordChangeToken);

        mailService.sendMail(user.getUsername(), passwordChangeResponse.getSubject(), passwordChangeResponse.getContent());

        logger.info("Password change request completed");
        return ResponseEntity.ok().body("Password change request completed");
    }

    public ResponseEntity<?> changePassword(String changeToken, String oldPassword, String newPassword) {
        logger.info("Changing password");
        if (!passwordChangeTokenService.isValidChangeToken(changeToken)) {
            return ResponseEntity.badRequest().body("Invalid bearer token");
        }

        String token = passwordChangeTokenService.extractChangeToken(changeToken);
        if (tokenService.isTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        Optional<PasswordChangeToken> changeTokenOptional = passwordChangeTokenService.getByToken(token);
        if (userDetailsService.isUserIsEmpty(changeTokenOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = changeTokenOptional.get().getUser();
        if (!userDetailsService.isValidOldPassword(user, oldPassword)) {
            return ResponseEntity.badRequest().body("Old password is not the same");
        }
        if (!userDetailsService.isValidNewPassword(newPassword, oldPassword)) {
            return ResponseEntity.badRequest().body("New password is the same as old password");
        }

        userDetailsService.updatePassword(user, newPassword);

        blacklistTokenService.saveToken(token);

        logger.info("Password changed successfully");
        return ResponseEntity.ok().body("Password changed successfully");
    }

    public ResponseEntity<?> forgotPassword(String username) {
        logger.info("Forgot password request received");
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (userDetailsService.isUserIsEmpty(userOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        PasswordChangeToken passwordChangeToken = passwordChangeTokenService.getOnCreateChangePasswordToken(user);
        PasswordForgotResponse passwordForgotResponse = new PasswordForgotResponse(passwordChangeToken);

        mailService.sendMail(user.getUsername(), passwordForgotResponse.getSubject(), passwordForgotResponse.getContent());

        logger.info("Forgot password request completed");
        return ResponseEntity.ok().body("Password forgot successfully");
    }
}
