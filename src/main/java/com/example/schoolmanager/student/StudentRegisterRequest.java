package com.example.schoolmanager.student;

import lombok.Data;

@Data
public class StudentRegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password; // student chooses password
}