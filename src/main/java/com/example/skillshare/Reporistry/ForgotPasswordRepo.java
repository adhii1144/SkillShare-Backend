package com.example.skillshare.Reporistry;

import com.example.skillshare.model.ForgotPassword;
import org.slf4j.helpers.FormattingTuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Integer> {

//    @Query("select fp from ForgotPassword fp where fp.otp=?1 and fp.user= ?2")
//    Optional<ForgotPassword> findByEmailAndUser(Integer otp, String email);


    Optional<ForgotPassword> findByOtpAndUser_Email(Integer otp, String email);



}
