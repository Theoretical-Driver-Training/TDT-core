package com.example.api.service.test;

import com.example.api.model.test.PossibleAnswer;
import com.example.api.repository.test.PossibleAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PossibleAnswerService {

    @Autowired
    private PossibleAnswerRepository repository;

    @Transactional
    public void savePossibleAnswer(PossibleAnswer possibleAnswer) {
        repository.save(possibleAnswer);
    }
}
