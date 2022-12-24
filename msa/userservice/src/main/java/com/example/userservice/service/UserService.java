package com.example.userservice.service;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.domain.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto getUserByUsername(String username);

    List<Member> getUserAll();
}
