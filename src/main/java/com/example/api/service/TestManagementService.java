package com.example.api.service;

import com.example.api.model.test.Test;
import com.example.api.model.test.TestQuestion;
import com.example.api.payload.response.PossibleAnswerResponse;
import com.example.api.payload.response.QuestionResponse;
import com.example.api.payload.response.TestResponse;
import com.example.api.service.test.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestManagementService {

    @Autowired
    private TestService testService;

    @Transactional(readOnly = true)
    public List<TestResponse> getTests() {
        log.info("Requesting tests");
        return Optional.ofNullable(testService.getTests()).stream()
                .flatMap(Collection::stream)
                .map(test -> new TestResponse(test.getId(), test.getName(), test.getDescription(),
                        test.getQuestions().size()))
                .toList();
    }

    @Transactional(readOnly = true)
    public TestResponse getTest(Long id) {
        log.info("Requesting test {}", id);
        return Optional.ofNullable(testService.getTestById(id))
                .map(test -> new TestResponse(test.getId(), test.getName(), test.getDescription(),
                        test.getQuestions().size()))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long testId, Integer questionNumber) {
        log.info("Requesting question {}", testId);
        return Optional.ofNullable(testService.getTestById(testId))
                .flatMap(test -> extractQuestionFromTestByQuestionNumber(test, questionNumber))
                .map(this::convertAnswerToTestQuestionResponse)
                .orElse(null);
    }

    private Optional<TestQuestion> extractQuestionFromTestByQuestionNumber(Test test, Integer questionNumber) {
        return test.getQuestions().stream()
                .filter(question -> question.getNumber() == questionNumber)
                .findFirst();
    }

    private QuestionResponse convertAnswerToTestQuestionResponse(TestQuestion testQuestion) {
        return new QuestionResponse(testQuestion.getNumber(), testQuestion.getContent(),
                testQuestion.getPossibleAnswers().stream()
                        .map(answer -> new PossibleAnswerResponse(answer.getNumber(), answer.getContent()))
                        .toList());
    }
}

