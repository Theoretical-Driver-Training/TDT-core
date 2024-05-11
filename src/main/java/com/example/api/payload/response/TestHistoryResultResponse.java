package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestHistoryResultResponse {

    private Long id;

    private String name;

    private Integer value;

    private String description;

}
