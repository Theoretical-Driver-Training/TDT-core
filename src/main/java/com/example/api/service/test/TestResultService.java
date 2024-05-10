package com.example.api.service.test;

import com.example.api.model.test.TestResult;
import com.example.api.repository.test.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository repository;

    public void save(TestResult testResult) {
        repository.save(testResult);
    }

}
