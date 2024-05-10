package com.example.api.service.test;

import com.example.api.model.test.PossibleAnswer;
import com.example.api.repository.test.PossibleAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PossibleAnswerService {

    @Autowired
    private PossibleAnswerRepository repository;

    @Transactional(readOnly = true)
    public List<Object[]> getAnswersByQuestionId(Long questionId) {
        return repository.findByQuestionId(questionId).stream()
                       .map(answer -> new Object[]{answer.getAnswerNumber(), answer.getContent()})
                       .toList();
    }

    @Transactional(readOnly = true)
    public PossibleAnswer getByQuestionIdAndAnswerNumber(Long questionId, Integer answerNumber) {
        return repository.findByQuestionIdAndAnswerNumber(questionId, answerNumber).stream()
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public void savePossibleAnswer(PossibleAnswer possibleAnswer) {
        repository.save(possibleAnswer);
    }
}
