package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TestHistoryResponse {

    private Long id;

    private String test;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<TestHistoryAnswerResponse> answerList;

    private List<TestHistoryResultResponse> resultList;

}
