package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PossibleAnswerResponse {

    private int number;

    private String content;

}
