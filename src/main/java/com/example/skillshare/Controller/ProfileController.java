package com.example.skillshare.Controller;

import com.example.skillshare.Services.UserProfileService;
import com.example.skillshare.model.Users;
import com.example.skillshare.Jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private UserProfileService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token, @RequestBody Users user) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token format");
            }
            token = token.substring(7);  // Remove "Bearer " prefix

            String email = jwtTokenProvider.extractEmail(token);
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token or user not authenticated");
            }

            Users updatedUser = userService.updateUserProfile(email, user);
            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or could not be updated");
            }

            return ResponseEntity.ok(updatedUser);  // Return updated user
        } catch (Exception e) {
            e.printStackTrace();  // Log for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

}