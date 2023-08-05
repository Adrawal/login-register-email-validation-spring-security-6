package com.aditya.emailverificationregistration.config.events;

import com.aditya.emailverificationregistration.entity.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public class UserRegistrationCompleteEvent extends ApplicationEvent {

    private  Users users;

    private String registrationURL;

    public UserRegistrationCompleteEvent( Users users, String registrationURL) {
        super(users);
        this.users = users;
        this.registrationURL = registrationURL;
    }


}
