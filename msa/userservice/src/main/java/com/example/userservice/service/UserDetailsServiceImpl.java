package com.example.userservice.service;

import com.example.userservice.domain.entity.Member;
import com.example.userservice.domain.vo.MemberContext;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No such User"));
        return new MemberContext(member,
                true, true, true, true
                , new ArrayList<>());
    }
}
