package com.example.api.repository.test;

import com.example.api.model.history.TestHistoryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestHistoryAnswerRepository extends JpaRepository<TestHistoryAnswer, Long> {

    List<TestHistoryAnswer> findTestHistoryAnswersById(Long testHistoryId);

}
