package com.example.userservice.service;

import com.example.userservice.domain.dto.UserDto;

public interface UserService {

    UserDto create(UserDto userDto);
}