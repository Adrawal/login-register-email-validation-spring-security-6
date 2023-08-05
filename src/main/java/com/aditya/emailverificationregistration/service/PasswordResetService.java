package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.entity.Users;

import java.util.Optional;

public interface PasswordResetService {
    public void generatePasswordResetToken(Users user, String passwordToken);
    public String validatePasswordResetToken(String token);
    public Optional<Users> findUserByPaswwordToken(String passwordToken);
}
