package com.aditya.emailverificationregistration.repository;

import com.aditya.emailverificationregistration.entity.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepo  extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken  findByVerificationToken(String token);
}
