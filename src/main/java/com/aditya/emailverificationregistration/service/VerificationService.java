package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.entity.VerificationToken;

public interface VerificationService {
    void saveUserVerificationToken(Users user, String token);
    String validateToken(String token);

    VerificationToken fetTokenDetailsByToken(String token);
    VerificationToken generateNewVerificationToken(String oldToken);
}
