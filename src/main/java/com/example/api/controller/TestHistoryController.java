package com.example.api.controller;

import com.example.api.model.history.TestHistory;
import com.example.api.model.history.TestHistoryAnswer;
import com.example.api.model.history.TestHistoryResult;
import com.example.api.payload.response.TestHistoryAnswerResponse;
import com.example.api.payload.response.TestHistoryResponse;
import com.example.api.payload.response.TestHistoryResultResponse;
import com.example.api.service.TestHistoryManagementService;
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
public class TestHistoryController {

    @Autowired
    private TestHistoryManagementService testHistoryManagementService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @GetMapping("/histories")
    public ResponseEntity<?> getAllTestHistory(@RequestHeader("Authorization") String bearerToken) {
        log.info("Get test histories");
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        Long userId = userAuthenticationService.getUserDetailsFromSecurityContext().getUser().getId();
        List<TestHistoryResponse> responses = getTestHistoryResponses(userId);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/history/{testHistoryId}")
    public ResponseEntity<?> getTestHistory(@RequestHeader("Authorization") String bearerToken,
                                            @PathVariable Long testHistoryId) {
        log.info("Get test history by id: {}", testHistoryId);
        ResponseEntity<?> responseEntity = userAuthenticationService.validateUserAuthenticationBearerToken(bearerToken);
        if (responseEntity.getStatusCode().isError()) return responseEntity;

        TestHistoryResponse response = getTestHistoryResponse(testHistoryId);
        return ResponseEntity.ok().body(response);
    }

    private TestHistoryResponse getTestHistoryResponse(Long testHistoryId) {
        log.debug("Get test history response with id {}", testHistoryId);
        return Optional.of(testHistoryManagementService.getTestHistory(testHistoryId)).stream()
                .map(this::convertHistoryToResponse)
                .findFirst().orElse(null);
    }

    public List<TestHistoryResponse> getTestHistoryResponses(Long userId) {
        log.info("Requesting test histories for user id {}", userId);
        return testHistoryManagementService.getTestHistories(userId).stream()
                .map(this::convertHistoryToResponse)
                .toList();
    }

    private TestHistoryResponse convertHistoryToResponse(TestHistory testHistory) {
        log.debug("Converting test history to response {}", testHistory);
        return new TestHistoryResponse(testHistory.getId(), testHistory.getTest().getName(),
                testHistory.getStatus().toString(), testHistory.getStartTime(), testHistory.getEndTime(),
                testHistory.getAnswerList().stream()
                        .map(this::convertHistoryAnswerToResponse)
                        .toList(),
                testHistory.getResultList().stream()
                        .map(this::convertHistoryResultToResultResponse)
                        .toList());
    }

    private TestHistoryAnswerResponse convertHistoryAnswerToResponse(TestHistoryAnswer testHistoryAnswer) {
        log.debug("Converting test history answer to response {}", testHistoryAnswer);
        return new TestHistoryAnswerResponse(testHistoryAnswer.getId(), testHistoryAnswer.getQuestion().getContent(),
                testHistoryAnswer.getAnswer().getContent());
    }

    private TestHistoryResultResponse convertHistoryResultToResultResponse(TestHistoryResult testHistoryResult) {
        log.debug("Converting test history result to response {}", testHistoryResult);
        return new TestHistoryResultResponse(testHistoryResult.getId(), testHistoryResult.getName(),
                testHistoryResult.getValue(), testHistoryResult.getDescription());
    }
}
