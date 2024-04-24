package com.example.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordForgotRequest {

    @NotBlank
    private String username;

}
