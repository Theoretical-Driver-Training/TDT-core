package com.example.api.service.test;

import com.example.api.model.test.PossibleResult;
import com.example.api.repository.test.PossibleResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PossibleResultService {

    @Autowired
    private PossibleResultRepository repository;

    @Transactional
    public void save(PossibleResult possibleResults) {
        repository.save(possibleResults);
    }
}
