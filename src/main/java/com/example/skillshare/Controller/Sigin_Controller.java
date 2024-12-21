package com.example.skillshare.Controller;

import com.example.skillshare.DTO.UserProfile;
import com.example.skillshare.Jwt.JwtTokenProvider;
import com.example.skillshare.Reporistry.Signin_Repo;
import com.example.skillshare.Services.Signin_Service;
import com.example.skillshare.Services.UserProfileService;
import com.example.skillshare.model.LoginRequest;
import com.example.skillshare.model.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class Sigin_Controller {


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private Signin_Service signin_Service;

    @Autowired
    private Signin_Repo repo;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    // Endpoint to register a new user
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        return signin_Service.registerUser(user);
    }

    // Endpoint to login and generate JWT token
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Retrieve user from the database using the email
        Users user = repo.findByEmail(email);

        // If user is not found or password does not match
        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        // Generate a token using the user's email
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Return the generated token
        return ResponseEntity.ok(token);
    }


    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> users = repo.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 if no users found
            }
            return new ResponseEntity<>(users, HttpStatus.OK); // Return 200 OK with users list
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Handle unexpected errors
        }
    }

    // Endpoint to fetch the user's profile, only accessible if the user is authenticated
    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getProfile() {
        try {
            // Extract the email from the security context (after authentication)
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Fetching profile for email: " + email);  // Add logging for debugging

            // Fetch the user profile based on the email
            UserProfile profile = userProfileService.getProfile(email);
            if (profile != null) {
                return ResponseEntity.ok(profile);
            } else {
                return ResponseEntity.notFound().build();  // Return 404 if profile not found
            }
        } catch (Exception e) {
            System.err.println("Error fetching profile: " + e.getMessage());  // Log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Return 500 if there's an error
        }
    }
}
