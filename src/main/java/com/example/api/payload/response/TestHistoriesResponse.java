package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TestHistoriesResponse {

    private Long id;

    private String test;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
