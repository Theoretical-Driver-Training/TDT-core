package com.example.api.controller;

import com.example.api.model.test.PossibleAnswer;
import com.example.api.model.test.Test;
import com.example.api.model.test.TestQuestion;
import com.example.api.payload.response.PossibleAnswerResponse;
import com.example.api.payload.response.QuestionResponse;
import com.example.api.payload.response.TestResponse;
import com.example.api.service.TestManagementService;
import com.example.api.service.TestProcessingService;
import com.example.api.service.user.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TestController {

    @Autowired
    private TestManagementService testManagementService;

    @Autowired
    private TestProcessingService testProcessingService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/tests")
    public ResponseEntity<?> getTests(@RequestHeader("Authorization") String bearerToken) {
        log.info("Get all tests");
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        List<TestResponse> responses = getTestsResponse();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/test/{testId}")
    public ResponseEntity<?> getTest(@RequestHeader("Authorization") String bearerToken,
                                     @PathVariable Long testId) {
        log.info("Get test by id: {}", testId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        TestResponse response = getTestResponse(testId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/test/{testId}/{questionNumber}")
    public ResponseEntity<?> getQuestion(@RequestHeader("Authorization") String bearerToken,
                                         @PathVariable Long testId,
                                         @PathVariable Integer questionNumber) {
        log.info("Get question by test id: {} and question number: {}", testId, questionNumber);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        QuestionResponse response = getQuestionResponse(testId, questionNumber);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/test/{testId}/{questionNumber}/{answerNumber}")
    public ResponseEntity<?> setAnswer(@RequestHeader("Authorization") String bearerToken,
                                       @PathVariable Long testId,
                                       @PathVariable Integer questionNumber,
                                       @PathVariable Integer answerNumber) {
        log.info("Set answer to question: {} and answer: {}", testId, questionNumber);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        testManagementService.setAnswerToQuestion(testId, questionNumber, answerNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test/{testId}/start")
    public ResponseEntity<?> startTest(@RequestHeader("Authorization") String bearerToken,
                                       @PathVariable Long testId) {
        log.info("Start test with id {}", testId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        testProcessingService.startTest(testId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test/{testId}/end")
    public ResponseEntity<?> endTest(@RequestHeader("Authorization") String bearerToken,
                                     @PathVariable Long testId) {
        log.info("End test with id {}", testId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        testProcessingService.endTest(testId);
        return ResponseEntity.ok().build();
    }

    private List<TestResponse> getTestsResponse() {
        log.debug("Get tests response");
        return testManagementService.getTests().stream()
                .map(this::convertResponse)
                .toList();
    }

    private TestResponse getTestResponse(Long testId) {
        log.debug("Get test response with id {}", testId);
        return Optional.of(testManagementService.getTest(testId))
                .map(this::convertResponse)
                .orElse(null);
    }

    private QuestionResponse getQuestionResponse(Long testId, Integer questionNumber) {
        log.debug("Get question response with number {}", questionNumber);
        return Optional.of(testManagementService.getQuestion(testId, questionNumber))
                .map(this::convertResponse)
                .orElse(null);
    }

    private TestResponse convertResponse(Test test) {
        log.debug("Converting test to response {}", test);
        return new TestResponse(test.getId(), test.getName(), test.getDescription(), test.getQuestions().size());
    }

    private QuestionResponse convertResponse(TestQuestion testQuestion) {
        log.debug("Converting question to response {}", testQuestion);
        return new QuestionResponse(testQuestion.getNumber(), testQuestion.getContent(),
                testQuestion.getPossibleAnswers().stream()
                        .map(this::convertResponse)
                        .toList());
    }

    private PossibleAnswerResponse convertResponse(PossibleAnswer possibleAnswer) {
        log.debug("Converting possible answer to response {}", possibleAnswer);
        return new PossibleAnswerResponse(possibleAnswer.getNumber(), possibleAnswer.getContent());
    }

}
