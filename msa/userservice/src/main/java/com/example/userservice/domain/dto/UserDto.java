package com.example.userservice.domain.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private String email;

    private String name;

    private String password;

    private String username;

    private LocalDate createdDate;

    private String encryptedPwd;
}
