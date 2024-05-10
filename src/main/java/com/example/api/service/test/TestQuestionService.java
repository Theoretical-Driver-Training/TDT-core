package com.example.api.service.test;

import com.example.api.model.test.TestQuestion;
import com.example.api.repository.test.TestQuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TestQuestionService {

    private static final Logger log = LoggerFactory.getLogger(TestQuestionService.class);

    @Autowired
    private TestQuestionRepository repository;

    @Transactional(readOnly = true)
    public TestQuestion getByTestIdAndQuestionNumber(Long testId, Integer questionNumber) {
        log.info("Getting question by id: {} and question number: {}", testId, questionNumber);
        Optional<TestQuestion> question = repository.findByTestIdAndQuestionNumber(testId, questionNumber);
        if (question.isEmpty()) {
            log.error("No question found");
            return null;
        }
        return question.get();
    }

    @Transactional
    public void save(TestQuestion question) {
        repository.save(question);
    }
}
