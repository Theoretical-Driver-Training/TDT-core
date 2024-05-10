package com.example.api.model.test;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EQAnswer {
    CORRECT("Верно"),
    MOSTLY_CORRECT("Скорее верно"),
    MOSTLY_INCORRECT("Скорее неверно"),
    INCORRECT("Неверно");

    private final String description;
}
