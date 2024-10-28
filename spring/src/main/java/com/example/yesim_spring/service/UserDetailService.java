package com.example.yesim_spring.service;

import com.example.yesim_spring.database.entity.UserEntity;
import com.example.yesim_spring.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
       return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + userId)
                );
    }

}
