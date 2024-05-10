package com.example.api.repository.test;

import com.example.api.model.test.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

    Optional<TestQuestion> findByTestIdAndQuestionNumber(Long testId, int questionNumber);

}
