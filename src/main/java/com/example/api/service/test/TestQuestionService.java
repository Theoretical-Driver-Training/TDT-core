package com.example.api.service.test;

import com.example.api.model.test.TestQuestion;
import com.example.api.repository.test.TestQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestQuestionService {

    @Autowired
    private TestQuestionRepository repository;

    @Transactional
    public void save(TestQuestion question) {
        repository.save(question);
    }
}
