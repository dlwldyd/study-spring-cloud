package com.example.userservice.domain.dto;

import com.example.userservice.domain.vo.ResponseOrder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDto {

    private String email;

    private String name;

    private String password;

    private String username;

    private LocalDate createdDate;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
