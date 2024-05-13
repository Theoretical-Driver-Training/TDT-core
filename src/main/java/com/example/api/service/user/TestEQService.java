package com.example.api.service.user;

import com.example.api.model.history.TestHistory;
import com.example.api.model.history.TestHistoryAnswer;
import com.example.api.model.test.EQResults;
import com.example.api.model.test.PossibleResult;
import com.example.api.service.TestHistoryManagementService;
import com.example.api.service.TestManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class TestEQService {

    private static final String NAME = "EQ";
    private static final String DESCRIPTION = "Тест на эмоциональный коэффициент (EQ)";

    private final Set<Integer> selfAwarenessAnswers = Set.of(1, 4, 5, 8, 10, 11, 12, 18, 20, 25, 31, 34, 39, 41);
    private final Set<Integer> socialInteractionAnswers = Set.of(6, 9, 13, 14, 15, 16, 17, 19, 21, 22, 23, 28, 35, 40);
    private final Set<Integer> lifeAttitudeAnswers = Set.of(2, 3, 7, 23, 25, 26, 28, 30, 32, 33, 36, 37, 38, 42);

    @Autowired
    private TestHistoryManagementService testHistoryManagementService;

    @Autowired
    private TestManagementService testManagementService;

    @Transactional
    public void calculateResults(TestHistory history) {
        log.info("Calculate test history");
        Long historyId = history.getId();
        List<TestHistoryAnswer> historyAnswers = testHistoryManagementService.getTestHistoryAnswers(historyId);

        Long testId = history.getTest().getId();
        processTestHistory(testId, EQResults.GENERAL_EQ.getName(), historyAnswers, null);
        processTestHistory(testId, EQResults.SELF_AWARENESS.getName(), historyAnswers, selfAwarenessAnswers);
        processTestHistory(testId, EQResults.SOCIAL_INTERACTION.getName(), historyAnswers, socialInteractionAnswers);
        processTestHistory(testId, EQResults.SOCIAL_INTERACTION.getName(), historyAnswers, lifeAttitudeAnswers);
    }

    private void processTestHistory(Long testId, String name, List<TestHistoryAnswer> historyAnswers, Set<Integer> categoryQuestions) {
        Integer value = categoryQuestions != null ? sumCategoryValues(historyAnswers, categoryQuestions) : sumCategoryValues(historyAnswers);
        String description = getTestDescription(testId, name, value);
        addTestHistoryResult(testId, name, value, description);
    }

    private Integer sumCategoryValues(List<TestHistoryAnswer> historyAnswers) {
        log.info("Sum Category Values");
        return historyAnswers.stream()
                .mapToInt(answer -> answer.getAnswer().getValue())
                .sum();
    }

    private Integer sumCategoryValues(List<TestHistoryAnswer> historyAnswers, Set<Integer> categoryQuestions) {
        log.info("Sum Category Values");
        return historyAnswers.stream()
                .filter(answer -> categoryQuestions.contains(answer.getQuestion().getNumber()))
                .mapToInt(answer -> answer.getAnswer().getValue())
                .sum();
    }

    private String getTestDescription(Long testId, String name, Integer value) {
        return testManagementService.getTest(testId)
                .getResults().stream()
                .filter(testResult -> Objects.equals(testResult.getName(), name))
                .flatMap(testResult -> testResult.getPossibleResults().stream()
                        .filter(possibleResult -> value >= possibleResult.getStartValue() && value <= possibleResult.getEndValue())
                        .map(PossibleResult::getContent))
                .findFirst()
                .orElse(null);
    }

    private void addTestHistoryResult(Long testId, String name, Integer value, String description) {
        testHistoryManagementService.addTestHistoryResult(testId, name, value, description);
    }

    public static String getTestName() {
        return NAME;
    }

    public static String getTestDescription() {
        return DESCRIPTION;
    }
}
