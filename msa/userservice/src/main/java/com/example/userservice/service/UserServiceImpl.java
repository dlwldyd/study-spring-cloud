package com.example.userservice.service;

import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.domain.entity.Member;
import com.example.userservice.domain.vo.MemberContext;
import com.example.userservice.domain.vo.ResponseOrder;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Environment env;

    private final RestTemplate restTemplate;

    @Override
    public UserDto create(UserDto userDto) {
        userDto.setUsername(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = modelMapper.map(userDto, Member.class);
        member.setEncryptedPwd(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(member);
        return modelMapper.map(member, UserDto.class);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        Member member = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("no data"));
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(member, UserDto.class);

//        List<ResponseOrder> responseOrders = new ArrayList<>();
        String orderUrl = String.format(env.getProperty("order-service.url"), username);
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
            orderUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ResponseOrder>>() {

            }
        );
        List<ResponseOrder> orderList = orderListResponse.getBody();
        userDto.setOrders(orderList);
        return userDto;
    }

    @Override
    public List<Member> getUserAll() {
        return userRepository.findAll();
    }
}
