package com.example.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;

}
