package com.example.api.repository.test;

import com.example.api.model.test.PossibleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PossibleResultRepository extends JpaRepository<PossibleResult, Long> {

    List<PossibleResult> findByResultId(Long resultId);

}
