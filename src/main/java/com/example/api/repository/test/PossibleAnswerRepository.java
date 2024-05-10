package com.example.api.repository.test;

import com.example.api.model.test.PossibleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PossibleAnswerRepository extends JpaRepository<PossibleAnswer, Long> {

    List<PossibleAnswer> findByQuestionId(Long questionId);

    List<PossibleAnswer> findByAnswerNumberAndContent(int answerNumber, String content);

    Optional<PossibleAnswer> findByQuestionIdAndAnswerNumber(Long questionId, int answerNumber);

}
