package com.example.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ProfileResponse {

    private Date lastPasswordChangeDate;

    private String firstName;

    private String lastName;

    private String middleName;

    private double weight;

    private double height;

    private Date birthDate;

}
