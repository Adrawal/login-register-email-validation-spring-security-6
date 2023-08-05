package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.entity.VerificationToken;
import com.aditya.emailverificationregistration.repository.VerificationTokenRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService{
    private final VerificationTokenRepo verificationTokenRepo;
    private final UserService userService;

    @Override
    public void saveUserVerificationToken(Users user, String token) {
        var verificationToken = new VerificationToken( token, user);
        verificationTokenRepo.save(verificationToken);
    }

    @Override
    public String validateToken(String token) {

        VerificationToken currentToken = verificationTokenRepo.findByVerificationToken(token);
        if(currentToken==null){
            return "Invalid verification token or link";
        }
        Users user = currentToken.getUser();
        Calendar calender =Calendar.getInstance();
        if(currentToken.getExpirationTime().getTime() - calender.getTime().getTime() <=0  ) {
           // verificationTokenRepo.deleteById(currentToken.getId());
            return "token already expired";
        }
        user.setEnable(true);
        userService.saveUser(user);
        return "valid Token";
    }

    @Override
    public VerificationToken fetTokenDetailsByToken(String token) {
        return verificationTokenRepo.findByVerificationToken(token);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken oldTokenfromDB = verificationTokenRepo.findByVerificationToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        oldTokenfromDB.setVerificationToken(UUID.randomUUID().toString());
        oldTokenfromDB.setExpirationTime(verificationTokenTime.getExpirationTime());
        return verificationTokenRepo.save(oldTokenfromDB);
    }
}
