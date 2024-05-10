package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestResponse {

    private Long id;

    private String name;

    private String description;

    private Integer questions;

}
