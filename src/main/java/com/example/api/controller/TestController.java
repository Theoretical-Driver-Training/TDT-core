package com.example.api.controller;

import com.example.api.service.TestManagementService;
import com.example.api.service.user.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TestController {

    @Autowired
    private TestManagementService testManagementService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/tests/")
    public ResponseEntity<?> getAllTests(@RequestHeader("Authorization") String bearerToken) {
        log.info("Get all tests");
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        return ResponseEntity.ok().body(testManagementService.getTests());
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<?> getTestById(@RequestHeader("Authorization") String bearerToken,
                                         @PathVariable Long testId) {
        log.info("Get test by id: {}", testId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        return ResponseEntity.ok().body(testManagementService.getTest(testId));
    }

    @GetMapping("/test/{testId}/{questionNumber}")
    public ResponseEntity<?> getQuestionByTestId(@RequestHeader("Authorization") String bearerToken,
                                                 @PathVariable Long testId,
                                                 @PathVariable Integer questionNumber) {
        log.info("Get question by id: {}", testId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        return ResponseEntity.ok().body(testManagementService.getQuestion(testId, questionNumber));
    }


}
