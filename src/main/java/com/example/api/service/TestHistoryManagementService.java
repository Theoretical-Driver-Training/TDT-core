package com.example.api.service;

import com.example.api.model.history.TestHistory;
import com.example.api.model.history.TestHistoryAnswer;
import com.example.api.model.history.TestHistoryResult;
import com.example.api.payload.response.TestHistoriesResponse;
import com.example.api.payload.response.TestHistoryAnswerResponse;
import com.example.api.payload.response.TestHistoryResponse;
import com.example.api.payload.response.TestHistoryResultResponse;
import com.example.api.repository.test.TestHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestHistoryManagementService {

    @Autowired
    private TestHistoryRepository testHistoryRepository;

    @Transactional(readOnly = true)
    public List<TestHistoriesResponse> getTestHistoriesByUserId(Long userId) {
        log.info("Requesting test histories for user id {}", userId);
        return Optional.ofNullable(testHistoryRepository.findTestHistoriesByUserId(userId)).stream()
                .flatMap(Collection::stream)
                .map(testHistory -> new TestHistoriesResponse(testHistory.getId(), testHistory.getTest().getName(),
                        testHistory.getStatus().toString(), testHistory.getStartTime(), testHistory.getEndTime()))
                .toList();
    }

    public TestHistoryResponse getTestHistoryById(Long testHistoryId) {
        log.info("Requesting test history with id {}", testHistoryId);
        return Optional.of(testHistoryRepository.findTestHistoryById(testHistoryId)).stream()
                .flatMap(Optional::stream)
                .map(this::convertTestHistoryToTestHistoryResponse)
                .findFirst().orElse(null);
    }

    private TestHistoryResponse convertTestHistoryToTestHistoryResponse(TestHistory testHistory) {
        log.debug("Converting test history to response {}", testHistory);
        return new TestHistoryResponse(testHistory.getId(), testHistory.getTest().getName(),
                testHistory.getStatus().toString(), testHistory.getStartTime(), testHistory.getEndTime(),
                testHistory.getAnswerList().stream()
                        .map(this::convertTestHistoryAnswerToTestHistoryAnswerResponse)
                        .toList(),
                testHistory.getResultList().stream()
                        .map(this::convertTestHistoryResultToTestHistoryResultResponse)
                        .toList());
    }

    private TestHistoryAnswerResponse convertTestHistoryAnswerToTestHistoryAnswerResponse(TestHistoryAnswer testHistoryAnswer) {
        log.debug("Converting test history answer to response {}", testHistoryAnswer);
        return new TestHistoryAnswerResponse(testHistoryAnswer.getId(), testHistoryAnswer.getQuestion().getContent(),
                testHistoryAnswer.getAnswer().getContent());
    }

    private TestHistoryResultResponse convertTestHistoryResultToTestHistoryResultResponse(TestHistoryResult testHistoryResult) {
        log.debug("Converting test history result to response {}", testHistoryResult);
        return new TestHistoryResultResponse(testHistoryResult.getId(), testHistoryResult.getName(),
                testHistoryResult.getValue(), testHistoryResult.getDescription());
    }
}
