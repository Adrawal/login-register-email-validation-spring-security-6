package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.entity.PasswordResetToken;
import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.repository.PasswordResetTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepo passwordResetTokenRepo;

    @Override
    public void generatePasswordResetToken(Users user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        PasswordResetToken password = passwordResetTokenRepo.findByVerificationToken(passwordToken);
        if(password!=null && null!= password.getId())
        {
            passwordResetTokenRepo.deleteById(password.getId());
        }
        passwordResetTokenRepo.save(passwordResetToken);



    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken currentToken = passwordResetTokenRepo.findByVerificationToken(token);
        if (currentToken == null) {
            return "Invalid password verification token or link";
        }
        Users user = currentToken.getUser();
        Calendar calender = Calendar.getInstance();
        if (currentToken.getExpirationTime().getTime() - calender.getTime().getTime() <= 0) {
            // verificationTokenRepo.deleteById(currentToken.getId());
            return "link already expired, Resend Link";
        }
        return "valid Token";

    }
    @Override
    public Optional<Users> findUserByPaswwordToken(String passwordToken) {
        return Optional.ofNullable(passwordResetTokenRepo.findByVerificationToken(passwordToken).getUser());
    }
}
