package com.example.api.model.history;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Table(name = "test status")
public enum TestStatus {
    COMPLETED,
    IN_PROGRESS
}
