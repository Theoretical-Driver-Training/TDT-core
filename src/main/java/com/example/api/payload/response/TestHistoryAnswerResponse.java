package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestHistoryAnswerResponse {

    private Long id;

    private String question;

    private String answer;

}
