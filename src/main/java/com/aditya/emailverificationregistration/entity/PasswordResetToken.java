package com.aditya.emailverificationregistration.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String verificationToken;

    private Date expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private static final int EXPIRATION_TIME = 1;

    public PasswordResetToken(String verificationToken, Users user) {
        super();
        this.verificationToken = verificationToken;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();

    }
    public PasswordResetToken(String verificationToken) {
        super();
        this.verificationToken = verificationToken;
        this.expirationTime = this.getTokenExpirationTime();

    }
    private Date getTokenExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date( cal.getTime().getTime());
    }
}
