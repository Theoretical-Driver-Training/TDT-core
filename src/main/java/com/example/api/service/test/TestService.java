package com.example.api.service.test;

import com.example.api.model.test.Test;
import com.example.api.repository.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository repository;

    @Transactional(readOnly = true)
    public List<Test> getTests() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Test getTestById(Long id) {
        return repository.findById(id).stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Transactional
    public Test save(Test test) {
        return repository.save(test);
    }
}
