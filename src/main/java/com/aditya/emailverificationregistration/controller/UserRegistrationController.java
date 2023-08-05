package com.aditya.emailverificationregistration.controller;

import com.aditya.emailverificationregistration.config.events.UserRegistrationCompleteEvent;
import com.aditya.emailverificationregistration.config.events.UserRegistrationCompleteEventListener;
import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.entity.VerificationToken;
import com.aditya.emailverificationregistration.service.PasswordResetService;
import com.aditya.emailverificationregistration.service.UserService;
import com.aditya.emailverificationregistration.service.VerificationService;
import com.aditya.emailverificationregistration.view.PasswordResetRequest;
import com.aditya.emailverificationregistration.view.RegistrationRequestView;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/userRegister")
public class UserRegistrationController {

private final UserService userService;
    private final VerificationService verificationService;

    private final UserRegistrationCompleteEventListener listener;
    private final HttpServletRequest servletRequest;

    private final PasswordResetService passwordResetService;

    private final ApplicationEventPublisher publisher;
@PostMapping("/register")
    public String registerUser(@RequestBody RegistrationRequestView registrationRequest, final HttpServletRequest servletRequest){
    Users user = userService.registerUser(registrationRequest);
    publisher.publishEvent(new UserRegistrationCompleteEvent(user,createApplicationURL(servletRequest)));
    return "Success! Please check your Email for Registration confirmation link.";
}

@GetMapping("/verifyEmail")
public String verifyUserEmail( @RequestParam("token") String token){
    VerificationToken verifyToken = verificationService.fetTokenDetailsByToken(token);
    StringBuilder sb = new StringBuilder();
    sb.append(createApplicationURL(servletRequest));
    sb.append("/userRegister");
    sb.append("/resendVerification");
    sb.append("?token=");
    sb.append(token);

    String url = sb.toString();
    if(verifyToken.getUser().isEnable()){
        return "This account is already verified, please login";
    }
    //checking if token is correct or not
    String verificationResult = verificationService.validateToken(token);
    if(verificationResult.equalsIgnoreCase("valid Token")){
        return "your registered email verified successfully, please continue to login your account";
    }
    return "invalid verification token,  please <a href=\"" +url +"\"> click here </a> to generate new verification Mail";

}
    @GetMapping("/resendVerification")
public String resendVerificationToken(@RequestParam("token") String oldToken , final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

    VerificationToken token = verificationService.generateNewVerificationToken(oldToken);
    Users theUser = token.getUser();
    resendVerificationTokenEmail(theUser, createApplicationURL(request),token);

return "A new verification link has been sent to your registered email address. ";
}

@PostMapping("/passwordResetRequest")
public String resetPasswordRequest(@RequestBody PasswordResetRequest request, final HttpServletRequest httpRequest) throws MessagingException, UnsupportedEncodingException {
    Optional<Users> user = userService.userFindByEmail(request.getEmail());
    String passwordResetURL ="";
    if(user.isPresent()){
        String newGeneratePasswordToken = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser( user.get(),  newGeneratePasswordToken);
    passwordResetURL = passwordResetEmailLink(user, createApplicationURL (httpRequest), newGeneratePasswordToken);
    }
return passwordResetURL;
}
@PostMapping("/reset-password")
public String resetPassword(@RequestBody PasswordResetRequest request, @RequestParam("token")  String passwordResetToken){
    String tokenValidation = passwordResetService.validatePasswordResetToken(passwordResetToken);

    if(!"valid Token".equalsIgnoreCase(tokenValidation)){
        return "invalid password reset token";
    }else{
        Optional<Users> user = passwordResetService.findUserByPaswwordToken(passwordResetToken);

        if(user.isPresent()){
            userService.resetPassword(user.get(),request.getNewPassword());
            return "Password has been reset succesffully";
        }
    }
        return "invalid password reset token";
}
    private String passwordResetEmailLink(Optional<Users> user, String applicationURL, String newGeneratePasswordToken) throws MessagingException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(applicationURL);
        sb.append("/userRegister/reset-password?token=");
        sb.append(newGeneratePasswordToken);
        String verificationUrl = sb.toString();
        log.info("click the link to reset your password:  {}",verificationUrl);
        listener.sendPasswordResetVerificationEmail(verificationUrl,user.get());
        return verificationUrl;
    }


    private void resendVerificationTokenEmail(Users theUser, String applicationURL, VerificationToken token) throws MessagingException, UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(applicationURL);
        sb.append("/userRegister/verifyEmail?token=");
        sb.append(token.getVerificationToken());
        String verificationUrl = sb.toString();
        log.info("click the link to verify your registration:  {}",verificationUrl);
        listener. sendVerificationEmail(verificationUrl,theUser);
    }

    public String createApplicationURL(HttpServletRequest servletRequest){
    StringBuilder sb = new StringBuilder();
    sb.append("http://");
    sb.append(servletRequest.getServerName());
    sb.append(":");
    sb.append(servletRequest.getServerPort());
    sb.append(servletRequest.getContextPath());

    return sb.toString();
}

}
