package com.example.api.repository.test;

import com.example.api.model.history.TestHistoryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestHistoryResultRepository extends JpaRepository<TestHistoryResult, Long> {

}
