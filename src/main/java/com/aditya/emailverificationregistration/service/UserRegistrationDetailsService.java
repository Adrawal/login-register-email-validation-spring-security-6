package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.config.UserRegistrationDetails;
import com.aditya.emailverificationregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(UserRegistrationDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("email not found "));
    }
}
