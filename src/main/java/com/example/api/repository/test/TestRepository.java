package com.example.api.repository.test;

import com.example.api.model.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    boolean existsByName(String name);

}
