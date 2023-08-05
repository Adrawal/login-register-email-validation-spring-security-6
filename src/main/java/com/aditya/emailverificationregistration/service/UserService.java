package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.view.RegistrationRequestView;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Users> getAllUsers();

    Optional<Users> userFindByEmail(String email);

    Users registerUser(RegistrationRequestView registrationRequest);

    Users saveUser (Users userData);


    void createPasswordResetTokenForUser(Users user, String newGeneratePasswordToken);

    void resetPassword(Users users, String newPassword);
}
