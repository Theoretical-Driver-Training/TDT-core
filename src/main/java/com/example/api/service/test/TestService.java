package com.example.api.service.test;

import com.example.api.model.test.Test;
import com.example.api.repository.test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository repository;
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Transactional(readOnly = true)
    public List<Test> getAllTests() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Test getById(Long id) {
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
