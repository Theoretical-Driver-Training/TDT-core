package com.example.api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninRequest {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;

}
