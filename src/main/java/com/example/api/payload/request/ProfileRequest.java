package com.example.api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class ProfileRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String middleName;

    @NotBlank
    private double weight;

    @NotBlank
    private double height;

    @NotBlank
    private Date birthDate;

}
