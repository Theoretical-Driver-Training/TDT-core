package com.example.api.service.user;

import com.example.api.payload.response.SignupMailResponse;
import com.example.api.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserRegistrationService {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MailService mailService;

    @Transactional
    public ResponseEntity<?> registerUser(String username, String password) {
        log.info("Registering user {}", username);
        if (userExists(username)) {
            return handleUserAlreadyExistsError(username);
        }
        createNewUser(username, password);
        sendSignupMailAsync(username);
        return buildSuccessResponse(username);
    }

    private boolean userExists(String username) {
        return userDetailsService.existsUserByUsername(username);
    }

    private ResponseEntity<?> handleUserAlreadyExistsError(String username) {
        String errorMsg = "User with username " + username + " already exists";
        log.error(errorMsg);
        return ResponseEntity.badRequest().body(errorMsg);
    }

    private void createNewUser(String username, String password) {
        userDetailsService.createUser(username, password, null);
    }

    private void sendSignupMailAsync(String username) {
        SignupMailResponse mailResponse = new SignupMailResponse();
        String subject = mailResponse.getSubject();
        String content = mailResponse.getContent();
        mailService.sendMail(username, subject, content);
    }

    private ResponseEntity<?> buildSuccessResponse(String username) {
        String logMsg = "Registered user " + username + " successfully";
        log.info(logMsg);
        return ResponseEntity.ok().body(logMsg);
    }
}
