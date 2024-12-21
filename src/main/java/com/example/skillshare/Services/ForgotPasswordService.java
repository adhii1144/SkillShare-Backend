package com.example.skillshare.Services;

import com.example.skillshare.DTO.ChangePassword;
import com.example.skillshare.DTO.MailBody;
import com.example.skillshare.Reporistry.ForgotPasswordRepo;
import com.example.skillshare.Reporistry.Signin_Repo;
import com.example.skillshare.model.ForgotPassword;
import com.example.skillshare.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class ForgotPasswordService {

    private final Signin_Repo signinRepo;
    private final ForgotPasswordRepo forgotPasswordRepo;
    private final MailService mailService;
    private static final long OTP_EXPIRY_DURATION = 70 * 1000; // 70 seconds
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public ForgotPasswordService(Signin_Repo signinRepo, ForgotPasswordRepo forgotPasswordRepo, MailService mailService) {
        this.signinRepo = signinRepo;
        this.forgotPasswordRepo = forgotPasswordRepo;
        this.mailService = mailService;
    }

    public void initiatePasswordReset(String email) {
        Users user = signinRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Integer otp = generateOtp();
        ForgotPassword forgotPassword = ForgotPassword.builder()
                .otp(otp)
                .otpexpireyTime(new Date(System.currentTimeMillis() + OTP_EXPIRY_DURATION))
                .user(user)
                .build();

        MailBody mailBody = MailBody.builder()
                .To(email)
                .Body("This is your OTP for password reset: " + otp)
                .Subject("OTP for Password Reset")
                .build();

        mailService.sendSimpleMail(mailBody);
        forgotPasswordRepo.save(forgotPassword);
    }

    public void verifyOtp(String email, Integer otp) {
        Users user = signinRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        ForgotPassword forgotPassword = forgotPasswordRepo.findByOtpAndUser_Email(otp, email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP or email."));

        if (forgotPassword.getOtpexpireyTime().before(new Date())) {
            forgotPasswordRepo.deleteById(forgotPassword.getFid());
            // Instead of throwing an exception, return a response with an expired message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP expired.");
        }
    }

    public void changePassword(String email, ChangePassword changePassword) {
        if (!Objects.equals(changePassword.Password() , changePassword.repeatPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }
        String hashPass = bCryptPasswordEncoder.encode(changePassword.Password());
        signinRepo.updatePassword(email, hashPass);
    }


    private Integer generateOtp() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
