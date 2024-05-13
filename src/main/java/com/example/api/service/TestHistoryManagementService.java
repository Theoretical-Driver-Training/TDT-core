package com.example.api.service;

import com.example.api.model.history.TestHistory;
import com.example.api.model.history.TestHistoryAnswer;
import com.example.api.model.history.TestHistoryResult;
import com.example.api.model.history.TestStatus;
import com.example.api.model.test.PossibleAnswer;
import com.example.api.model.test.Test;
import com.example.api.model.test.TestQuestion;
import com.example.api.model.user.User;
import com.example.api.repository.test.TestHistoryAnswerRepository;
import com.example.api.repository.test.TestHistoryRepository;
import com.example.api.repository.test.TestHistoryResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TestHistoryManagementService {

    @Autowired
    private TestHistoryRepository testHistoryRepository;

    @Autowired
    private TestHistoryAnswerRepository testHistoryAnswerRepository;

    @Autowired
    private TestHistoryResultRepository testHistoryResultRepository;

    @Transactional(readOnly = true)
    public List<TestHistory> getTestHistories(Long userId) {
        log.info("Requesting test histories for user id {}", userId);
        return testHistoryRepository.findTestHistoriesByUserId(userId).stream()
                .toList();
    }

    @Transactional
    public TestHistory getTestHistory(Long testHistoryId) {
        log.info("Requesting test history with id {}", testHistoryId);
        return testHistoryRepository.findTestHistoryById(testHistoryId)
                .orElse(null);
    }

    @Transactional
    public TestHistory getCurrentTestHistory(Long userId, Long testId) {
        log.info("Requesting current test history with id {}", testId);
        return testHistoryRepository.findTestHistoryByUserIdAndTestId(userId, testId).stream()
                .filter(testHistory -> testHistory.getStatus().equals(TestStatus.IN_PROGRESS))
                .findFirst().orElse(null);
    }

    @Transactional
    public List<TestHistoryAnswer> getTestHistoryAnswers(Long testHistoryId) {
        log.debug("Requesting test history answer with id {}", testHistoryId);
        return testHistoryAnswerRepository.findTestHistoryAnswersById(testHistoryId);
    }

    @Transactional
    public void addTestHistoryAnswer(Long testId, TestQuestion testQuestion, PossibleAnswer possibleAnswer) {
        log.debug("Adding test history answer for test id {}", testId);
        testHistoryRepository.findTestHistoryByTestId(testId)
                .filter(testHistory -> testHistory.getStatus().equals(TestStatus.IN_PROGRESS))
                .map(testHistory -> {
                    TestHistoryAnswer newTestHistoryAnswer = new TestHistoryAnswer();
                    newTestHistoryAnswer.setTestHistory(testHistory);
                    newTestHistoryAnswer.setQuestion(testQuestion);
                    newTestHistoryAnswer.setAnswer(possibleAnswer);
                    testHistoryAnswerRepository.save(newTestHistoryAnswer);

                    testHistory.getAnswerList().add(newTestHistoryAnswer);
                    return testHistoryRepository.save(testHistory);
                });
    }

    @Transactional
    public void addTestHistoryResult(Long testId, String name, Integer value, String description) {
        log.debug("Adding test history result for test id {}", testId);
        testHistoryRepository.findTestHistoryByTestId(testId)
                .filter(testHistory -> testHistory.getStatus().equals(TestStatus.COMPLETED))
                .map(testHistory -> {
                    TestHistoryResult newTestHistoryResult = new TestHistoryResult();
                    newTestHistoryResult.setTestHistory(testHistory);
                    newTestHistoryResult.setName(name);
                    newTestHistoryResult.setValue(value);
                    newTestHistoryResult.setDescription(description);
                    testHistoryResultRepository.save(newTestHistoryResult);

                    testHistory.getResultList().add(newTestHistoryResult);
                    return testHistoryRepository.save(testHistory);
                });
    }

    @Transactional
    public void createTestHistory(User user, Test test) {
        log.info("Create test history from user by id: {}", user.getId());
        TestHistory testHistory = new TestHistory();
        testHistory.setUser(user);
        testHistory.setTest(test);
        testHistory.setStatus(TestStatus.IN_PROGRESS);
        testHistory.setStartTime(LocalDateTime.now());
        testHistoryRepository.save(testHistory);
    }

    @Transactional
    public void updateTestHistory(Long testHistoryId, TestHistory testHistory) {
        log.info("Updating test history with id {}", testHistoryId);
        testHistory.setEndTime(LocalDateTime.now());
        testHistory.setStatus(TestStatus.COMPLETED);
        testHistoryRepository.save(testHistory);
    }
}
