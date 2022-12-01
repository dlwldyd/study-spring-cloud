package com.example.userservice.controller;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.domain.vo.RequestUser;
import com.example.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final String message;
    private final UserService userService;

    public UserController(@Value("${greeting.message}") String message, UserService userService) {
        this.message = message;
        this.userService = userService;
    }

    @GetMapping("/health_check")

    public String status() {
        return "user-service works fine";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return message;
    }

    @PostMapping("/users")
    public String createUser(@RequestBody RequestUser requestUser) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(requestUser, UserDto.class);

        userService.create(userDto);

        return "a method to create user is called";
    }
}
