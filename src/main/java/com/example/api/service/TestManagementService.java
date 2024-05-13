package com.example.api.service;

import com.example.api.model.test.*;
import com.example.api.repository.test.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private TestHistoryManagementService testHistoryManagementService;

    @Transactional(readOnly = true)
    public List<Test> getTests() {
        log.info("Requesting tests");
        return testRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Test getTest(Long testId) {
        log.info("Requesting test by id {}", testId);
        return testRepository.findById(testId)
                .orElse(null);
    }

    public TestQuestion getQuestion(Long testId, Integer questionNumber) {
        log.info("Requesting question by id {} with number {}", testId, questionNumber);
        return testRepository.findById(testId)
                .map(test -> extractQuestion(test, questionNumber))
                .orElse(null);
    }

    public void setAnswerToQuestion(Long testId, Integer questionNumber, Integer answerNumber) {
        log.info("Setting answer to question with number {}", questionNumber);
        TestQuestion question = getQuestion(testId, questionNumber);
        PossibleAnswer possibleAnswer = getPossibleAnswer(testId, questionNumber, answerNumber);
        testHistoryManagementService.addTestHistoryAnswer(testId, question, possibleAnswer);
    }

    @Transactional(readOnly = true)
    public boolean existTest(String name) {
        log.info("Request to check if test exists with name {}", name);
        return testRepository.existsByName(name);
    }

    @Transactional
    public void save(Test test) {
        log.info("Saving test {}", test);
        testRepository.save(test);
    }

    @Transactional
    public void save(TestQuestion question) {
        log.info("Saving question {}", question);
        questionRepository.save(question);
    }

    @Transactional
    public void save(TestResult result) {
        log.info("Saving result {}", result);
        resultRepository.save(result);
    }

    @Transactional
    public void save(PossibleAnswer answer) {
        log.info("Saving possible answer {}", answer);
        possibleAnswerRepository.save(answer);
    }

    @Transactional
    public void save(PossibleResult result) {
        log.info("Saving possible result {}", result);
        possibleResultRepository.save(result);
    }

    private PossibleAnswer getPossibleAnswer(Long testId, Integer questionNumber, Integer answerNumber) {
        log.debug("Requesting possible answer with number {}", answerNumber);
        return testRepository.findById(testId)
                .map(test -> extractQuestion(test, questionNumber))
                .map(question -> extractPossibleAnswer(question, answerNumber))
                .orElse(null);
    }

    private TestQuestion extractQuestion(Test test, Integer questionNumber) {
        log.debug("Extracting question with number {}", questionNumber);
        return test.getQuestions().stream()
                .filter(question -> question.getNumber() == questionNumber)
                .findFirst()
                .orElse(null);
    }

    private PossibleAnswer extractPossibleAnswer(TestQuestion question, Integer answerNumber) {
        log.debug("Extracting possible answer from question {}", question);
        return question.getPossibleAnswers().stream()
                .filter(answer -> answer.getNumber() == answerNumber)
                .findFirst()
                .orElse(null);
    }
}

