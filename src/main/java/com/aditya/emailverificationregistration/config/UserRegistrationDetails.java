package com.aditya.emailverificationregistration.config;

import com.aditya.emailverificationregistration.entity.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Data
public class UserRegistrationDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean isEnable;
    private List<GrantedAuthority> authorities;

    public UserRegistrationDetails(Users userObj) {
        this.userName = userObj.getEmail();
        this.password = userObj.getPassword();
        this.isEnable = userObj.isEnable();
        this.authorities = Arrays.stream(userObj.getRole().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
