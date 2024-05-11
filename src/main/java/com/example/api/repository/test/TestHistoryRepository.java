package com.example.api.repository.test;

import com.example.api.model.history.TestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Long> {

    Optional<TestHistory> findTestHistoryById(Long testHistoryId);

    List<TestHistory> findTestHistoriesByUserId(Long userId);

}
