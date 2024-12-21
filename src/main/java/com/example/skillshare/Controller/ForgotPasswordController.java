package com.example.skillshare.Controller;

import com.example.skillshare.DTO.ChangePassword;
import com.example.skillshare.Services.ForgotPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forgotPassword")
@CrossOrigin(origins = "http://localhost:5173")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/verifyEmail/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable String email) {
        try {
            System.out.println("Received request to send OTP to email: " + email);
            forgotPasswordService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("message", "OTP sent to the email."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Failed to send OTP. Please try again.")
            );
        }
    }

    @PostMapping("/verify/{otp}/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        try {
            forgotPasswordService.verifyOtp(email, otp);
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("message", "Invalid OTP. Please try again.")
            );
        }
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword(@PathVariable String email, @RequestBody ChangePassword changePassword) {
        try {
            forgotPasswordService.changePassword(email, changePassword);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Failed to change the password. Please try again.")
            );
        }
    }
}
