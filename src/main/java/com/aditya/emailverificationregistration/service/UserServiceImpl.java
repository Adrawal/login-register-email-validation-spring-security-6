package com.aditya.emailverificationregistration.service;

import com.aditya.emailverificationregistration.customException.UserAlreadyExistException;
import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.repository.UserRepository;
import com.aditya.emailverificationregistration.view.RegistrationRequestView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

private final PasswordResetService passwordResetService;

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users registerUser(RegistrationRequestView request) {
        //checking if user existed
        Optional<Users> userInfo = userRepository.findByEmail(request.email());

        Users userResponse=null;
        if(userInfo.isPresent()){
            log.info("user already existed");
            throw new UserAlreadyExistException("A user with Email address "+ request.email()+ " already exists");
        }else{
            var newUserDetails = new Users();
            newUserDetails.setEmail(request.email());
            newUserDetails.setFirstName(request.firstName());
            newUserDetails.setLastName(request.lastName());
            newUserDetails.setPassword(passwordEncoder.encode(request.password()));
            newUserDetails.setRole(request.role());
            userResponse=    userRepository.save(newUserDetails);
        }

        return userResponse;
    }

    @Override
    public Users saveUser(Users userData) {
        return userRepository.save(userData);
    }

    @Override
    public void createPasswordResetTokenForUser(Users user, String newGeneratePasswordToken) {
    passwordResetService.generatePasswordResetToken(user, newGeneratePasswordToken);
    }

    @Override
    public void resetPassword(Users users, String newPassword) {
        users.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(users);
    }


    @Override
    public Optional<Users> userFindByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
