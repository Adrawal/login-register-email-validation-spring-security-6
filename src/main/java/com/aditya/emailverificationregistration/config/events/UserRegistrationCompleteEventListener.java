package com.aditya.emailverificationregistration.config.events;

import com.aditya.emailverificationregistration.entity.Users;
import com.aditya.emailverificationregistration.service.UserService;
import com.aditya.emailverificationregistration.service.VerificationService;
import com.aditya.emailverificationregistration.view.RegistrationRequestView;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationCompleteEventListener implements ApplicationListener<UserRegistrationCompleteEvent > {

    private final UserService userService;

    private final VerificationService verificationService;

    private final JavaMailSender emailSender;

    @Override
    public void onApplicationEvent(UserRegistrationCompleteEvent event) {
        //step:1  get the newly created user
            Users user = event.getUsers();
        //step:2 generate a verification token
        String token = UUID.randomUUID().toString();
        //step:3 save the verification token for user
        verificationService.saveUserVerificationToken(user, token);
        //step:4 Build the verification url to sent to user
        StringBuilder sb = new StringBuilder();
        sb.append(event.getRegistrationURL());
        sb.append("/userRegister/verifyEmail?token=");
        sb.append(token);
        String verificationUrl = sb.toString();
        log.info("click the link to verify your registration:  {}",verificationUrl);
        //step:5 send the email

        try {
            sendVerificationEmail(verificationUrl,user);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendVerificationEmail(String verificationLink, Users user) throws MessagingException, UnsupportedEncodingException {
        String subjectLine ="Email Verification Link";
        String sender = "Aditya Rawal";
        String contact  = "qualityteamassureforyou@gmail.com";

        String mailContent = "<p> Hi, "+ user.getFirstName()+ " ,</p>"+
                "<p> Thank you for registration with us, "+ "Please, follow the link to complete your registration.</p>"
                +"<a href=\"" + verificationLink + "\"> verify yourEmail to activate your account</a>"+
                "<p> Thank you <br> "+contact+ " </pr>";

        MimeMessage  message = emailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(contact, sender);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subjectLine);
        messageHelper.setText(mailContent,true);
        emailSender.send(message);
        //SimpleMailMessage message = new SimpleMailMessage();



    }

    public void sendPasswordResetVerificationEmail(String verificationLink, Users user) throws MessagingException, UnsupportedEncodingException {
        String subjectLine ="Password Reset Verification Link";
        String sender = "Aditya Rawal";
        String contact  = "qualityteamassureforyou@gmail.com";

        String mailContent = "<p> Hi, "+ user.getFirstName()+ " ,</p>"+
                "<p> you have recently requested for password change , "+ "Please, follow the link to reset your password.</p>"
                +"<a href=\"" + verificationLink + "\"> Reset Password </a>"+
                "<p> Thank you <br> "+contact+ " </pr>";

        MimeMessage  message = emailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(contact, sender);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subjectLine);
        messageHelper.setText(mailContent,true);
        emailSender.send(message);
        //SimpleMailMessage message = new SimpleMailMessage();



    }
}
