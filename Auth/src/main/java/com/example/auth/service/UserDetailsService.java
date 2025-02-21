package com.example.auth.service;

import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var repository = userRepository.findByUsername(username);
        if(repository.isEmpty())
            throw new UsernameNotFoundException("Username not found");
        var user = repository.get();
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
