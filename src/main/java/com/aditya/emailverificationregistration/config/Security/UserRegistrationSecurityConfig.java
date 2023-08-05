package com.aditya.emailverificationregistration.config.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class UserRegistrationSecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/userRegister",
            "/userRegister/**",
            "/users",
            "/users/**"
    };


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    return  http.cors().and().csrf()
            .disable().authorizeHttpRequests().requestMatchers("/userRegister/**").permitAll()
            .and().authorizeHttpRequests().requestMatchers("/users/**").hasAnyAuthority("USER","ADMIN")
            .and().formLogin().and().build();
     /*   http.authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();*/
    }
}
