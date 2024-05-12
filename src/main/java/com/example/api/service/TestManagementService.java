package com.example.api.service;

import com.example.api.model.test.*;
import com.example.api.payload.response.PossibleAnswerResponse;
import com.example.api.payload.response.QuestionResponse;
import com.example.api.payload.response.TestResponse;
import com.example.api.repository.test.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TestManagementService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestQuestionRepository questionRepository;

    @Autowired
    private TestResultRepository resultRepository;

    @Autowired
    private PossibleAnswerRepository possibleAnswerRepository;

    @Autowired
    private PossibleResultRepository possibleResultRepository;

    @Transactional(readOnly = true)
    public List<TestResponse> getTests() {
        log.info("Requesting tests");
        return testRepository.findAll().stream()
                .map(this::convertTestToTestResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TestResponse getTest(Long testId) {
        log.info("Requesting test {}", testId);
        return testRepository.findById(testId)
                .map(this::convertTestToTestResponse)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public QuestionResponse getQuestion(Long testId, Integer questionNumber) {
        log.info("Requesting question {}", testId);
        return testRepository.findById(testId)
                .flatMap(test -> extractQuestionFromTestByQuestionNumber(test, questionNumber))
                .map(this::convertAnswerToTestQuestionResponse)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existTestByName(String name) {
        log.info("Requesting existing test {}", name);
        return testRepository.existsByName(name);
    }

    @Transactional
    public void saveTest(Test test) {
        log.info("Saving test {}", test);
        testRepository.save(test);
    }

    @Transactional
    public void saveQuestion(TestQuestion testQuestion) {
        log.info("Saving question {}", testQuestion);
        questionRepository.save(testQuestion);
    }

    @Transactional
    public void saveResult(TestResult testResult) {
        log.info("Saving result {}", testResult);
        resultRepository.save(testResult);
    }

    @Transactional
    public void savePossibleAnswer(PossibleAnswer possibleAnswer) {
        log.info("Saving possible answer {}", possibleAnswer);
        possibleAnswerRepository.save(possibleAnswer);
    }

    @Transactional
    public void savePossibleResult(PossibleResult possibleResult) {
        log.info("Saving possible result {}", possibleResult);
        possibleResultRepository.save(possibleResult);
    }

    private TestResponse convertTestToTestResponse(Test test) {
        log.debug("Converting test to response {}", test);
        return new TestResponse(test.getId(), test.getName(), test.getDescription(), test.getQuestions().size());
    }

    private Optional<TestQuestion> extractQuestionFromTestByQuestionNumber(Test test, Integer questionNumber) {
        log.debug("Extracting question {}", questionNumber);
        return test.getQuestions().stream()
                .filter(question -> question.getNumber() == questionNumber)
                .findFirst();
    }

    private QuestionResponse convertAnswerToTestQuestionResponse(TestQuestion testQuestion) {
        log.debug("Converting answer to response {}", testQuestion);
        return new QuestionResponse(testQuestion.getNumber(), testQuestion.getContent(),
                testQuestion.getPossibleAnswers().stream()
                        .map(this::convertPossibleAnswerToTestPossibleAnswerResponse)
                        .toList());
    }

    private PossibleAnswerResponse convertPossibleAnswerToTestPossibleAnswerResponse(PossibleAnswer possibleAnswer) {
        log.debug("Converting possible answer to response {}", possibleAnswer);
        return new PossibleAnswerResponse(possibleAnswer.getNumber(), possibleAnswer.getContent());
    }
}

