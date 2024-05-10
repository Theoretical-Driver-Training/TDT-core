package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionResponse {

    private int questionNumber;

    private String content;

    private List<PossibleAnswerResponse> possibleAnswers;

}
