package com.example.skillshare.Controller;

import com.example.skillshare.Services.UserService;
import com.example.skillshare.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Fetch all users with optional filters
    @GetMapping("/search")
    public List<Users> searchUsers(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "bio", required = false) String bio,
            @RequestParam(value = "location", required = false) String location) {
        return userService.searchUsers(query, bio, location);
    }

    // Fetch a specific user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserProfile(@PathVariable String userId) {
        try {
            Integer parsedUserId = Integer.valueOf(userId); // Convert String to Integer
            Optional<Users> userOpt = userService.getUserProfile(parsedUserId);
            return userOpt.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null); // Return 400 if userId is invalid
        }
    }


    // Update user profile
    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUserProfile(@PathVariable("userId") String userIdStr, @RequestBody Users updatedUser) {
        try {
            Integer userId = Integer.valueOf(userIdStr); // Convert to Integer
            Users user = userService.updateUserProfile(userId, updatedUser);
            return ResponseEntity.ok(user);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null); // Bad request if invalid userId
        }
    }


    // Upload profile image
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Integer userId,
                                                     @RequestParam("image") MultipartFile file) {
        String imageUrl = "userpic.jpg" + file.getOriginalFilename();
        String result = userService.uploadProfileImage(userId, imageUrl);
        return ResponseEntity.ok(result);
    }
}
