package com.aditya.emailverificationregistration.repository;

import com.aditya.emailverificationregistration.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepo extends JpaRepository<VerificationToken, Long> {


    VerificationToken findByVerificationToken(String token);
}
