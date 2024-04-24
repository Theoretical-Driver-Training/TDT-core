package com.example.api.service;

import com.example.api.model.token.PasswordChangeToken;
import com.example.api.model.user.User;
import com.example.api.payload.request.PasswordForgotRequest;
import com.example.api.payload.response.PasswordChangeResponse;
import com.example.api.payload.response.PasswordForgotResponse;
import com.example.api.security.jwt.JwtUtils;
import com.example.api.service.mail.MailService;
import com.example.api.service.token.BlacklistTokenService;
import com.example.api.service.token.PasswordChangeTokenService;
import com.example.api.service.token.TokenService;
import com.example.api.service.user.UserDetailsServiceImpl;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProfilePasswordService {

    private final Logger logger = LoggerFactory.getLogger(ProfilePasswordService.class);

    private final TokenService tokenService;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final MailService mailService;

    private final PasswordChangeTokenService changePasswordTokenService;
    private final BlacklistTokenService blacklistTokenService;

    public ProfilePasswordService(TokenService tokenService, UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils, MailService mailService, PasswordChangeTokenService changePasswordTokenService, BlacklistTokenService blacklistTokenService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.mailService = mailService;
        this.changePasswordTokenService = changePasswordTokenService;
        this.blacklistTokenService = blacklistTokenService;
    }

    public ResponseEntity<?> resetPassword(String bearerToken) {
        logger.info("Change password request received");
        if (validateBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Invalid bearer token");
        }

        String token = tokenService.extractToken(bearerToken);
        if (validateTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (validateUserIsEmpty(userOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        PasswordChangeToken changePasswordToken = changePasswordTokenService.getOnCreateChangePasswordToken(user);
        PasswordChangeResponse passwordChangeResponse = new PasswordChangeResponse(changePasswordToken);

        mailService.sendMail(user.getUsername(), passwordChangeResponse.getSubject(), passwordChangeResponse.getContent());

        logger.info("Password change request completed");
        return ResponseEntity.ok().body("Password change request completed");
    }

    public ResponseEntity<?> changePassword(String changeToken, String currentPassword, String newPassword) {
        logger.info("Changing password");
        if (validateChangeToken(changeToken)) {
            return ResponseEntity.badRequest().body("Invalid change token");
        }

        String token = changePasswordTokenService.extractToken(changeToken);
        if (validateTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        Optional<PasswordChangeToken> changeTokenOptional = changePasswordTokenService.getByToken(token);
        if (validateUserIsEmpty(changeTokenOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = changeTokenOptional.get().getUser();
        if (validateCurrentPassword(currentPassword, user)) {
            return ResponseEntity.badRequest().body("Current password invalid");
        }
        if (validateNewPasswordIsCurrentPassword(newPassword, currentPassword)) {
            return ResponseEntity.badRequest().body("New password is same as current password");
        }

        user.setPassword(encoder.encode(newPassword));
        user.setLastPasswordChangeDate(new Date(System.currentTimeMillis()));
        userDetailsService.saveUser(user);

        blacklistTokenService.saveToken(token);

        return ResponseEntity.ok().body("Password changed successfully");
    }



    public ResponseEntity<?> forgotPassword(String username) {
        logger.info("Forgot password request received");
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (validateUserIsEmpty(userOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        PasswordChangeToken changeToken = changePasswordTokenService.getOnCreateChangePasswordToken(user);
        PasswordForgotResponse forgotResponse = new PasswordForgotResponse(changeToken);
        mailService.sendMail(user.getUsername(), forgotResponse.getSubject(), forgotResponse.getContent());

        logger.info("Forgot password request completed");
        return ResponseEntity.ok().body("Password forgot successfully");
    }

    public ResponseEntity<?> validateCurrentPassword(String bearerToken, String currentPassword, String newPassword) {
        logger.info("Validating password");
        if (validateBearerToken(bearerToken)) {
            return ResponseEntity.badRequest().body("Invalid Bearer Token");
        }

        String token = tokenService.extractToken(bearerToken);
        if (validateTokenIsBlacklist(token)) {
            return ResponseEntity.badRequest().body("Token is blacklist");
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> userOptional = userDetailsService.getUserByUsername(username);
        if (validateUserIsEmpty(userOptional)) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        if (validateCurrentPassword(currentPassword, user)) {
            return ResponseEntity.badRequest().body("Current password invalid");
        }
        if (validateNewPasswordIsCurrentPassword(newPassword, currentPassword)) {
            return ResponseEntity.badRequest().body("New password is same as current password");
        }

        logger.info("Validating password Successfully");
        return ResponseEntity.ok().body("Validating password Successfully");
    }

    private boolean validateBearerToken(String bearerToken) {
        logger.info("Validate bearer token: {}", bearerToken);
        if (tokenService.isValidBearerToken(bearerToken)) {
            logger.error("Bearer token is in list of blacklist");
            return true;
        }
        return false;
    }

    private boolean validateChangeToken(String changeToken) {
        logger.info("Validate change token: {}", changeToken);
        if (changePasswordTokenService.isValidChangePasswordToken(changeToken)) {
            logger.error("Change token is in list of blacklist");
            return true;
        }
        return false;
    }

    private boolean validateTokenIsBlacklist(String token) {
        logger.info("Validate token is blacklist: {}", token);
        if (jwtUtils.isTokenInBlacklist(token)) {
            logger.error("Token is in list of blacklist");
            return true;
        }
        return false;
    }

    private boolean validateUserIsEmpty(Optional<?> optional) {
        logger.info("Validate user is not found: {}", optional);
        if (optional.isEmpty()) {
            logger.error("User not found");
            return true;
        }
        return false;
    }

    private boolean validateCurrentPassword(String currentPassword, User user) {
        logger.info("Validate current password");
        if (encoder.matches(currentPassword, user.getPassword())) {
            logger.error("Current password invalid");
            return false;
        }
        return true;
    }

    private boolean validateNewPasswordIsCurrentPassword(String newPassword, String currentPassword) {
        logger.info("Validate new password is current password");
        if (currentPassword.equals(newPassword)) {
            logger.error("New password is same as current password");
            return true;
        }
        return false;
    }
}
