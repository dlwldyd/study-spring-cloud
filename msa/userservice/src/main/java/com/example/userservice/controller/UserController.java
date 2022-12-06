package com.example.userservice.controller;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.vo.RequestUser;
import com.example.userservice.domain.vo.ResponseUser;
import com.example.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class UserController {

    private final String message;
    private final UserService userService;
    private final Environment env;

    public UserController(@Value("${greeting.message}") String message, UserService userService, Environment env) {
        this.message = message;
        this.userService = userService;
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("user-service works on PORT %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/welcome")
    public String welcome() {
        return message;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody RequestUser requestUser) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // 필드이름이 정확히 같아야하는 정책, 꼭 안써도 됨
        UserDto userDto = modelMapper.map(requestUser, UserDto.class); // requestUser를 UserDto 객체로 변환

        userService.create(userDto);

        ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

//        return new ResponseEntity<>(responseUser, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getUserAll();

        ModelMapper modelMapper = new ModelMapper();
        List<ResponseUser> responseUsers = users.stream()
                .map(user -> modelMapper.map(user, ResponseUser.class))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(responseUsers);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        UserDto userDto = userService.getUserByUsername(username);

        ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
}
