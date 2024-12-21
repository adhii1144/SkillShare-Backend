package com.example.skillshare.Services;

import com.example.skillshare.DTO.UserProfile;
import com.example.skillshare.Reporistry.Signin_Repo;
import com.example.skillshare.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@CrossOrigin(origins = "http://localhost:5173")
public class UserProfileService {

    @Autowired
    private Signin_Repo userRepository;

    public UserProfile getProfile(String email) {

        // Fetch the user from the database
        Users user = userRepository.findByEmail(email);

        // Convert the User entity to the UserProfile DTO
        if (user != null) {
            UserProfile userProfile = new UserProfile();
            userProfile.setName(user.getName());
            userProfile.setTitle(user.getTitle());
            userProfile.setEmail(user.getEmail());
            userProfile.setMobile(user.getMobile());
            userProfile.setBio(user.getBio());
            userProfile.setAddress(user.getAddress());
            userProfile.setWebsite(user.getWebsite());
//            userProfile.setAvatar(user.getAvatar()); // Optional field

            return userProfile;
        } else {
            return null; // Or throw a custom exception if the user is not found
        }
    }

    public Users updateUserProfile(String email, Users userDetails) {
        // Find the user by email
        Users existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Update user profile fields
        existingUser.setName(userDetails.getName());
        existingUser.setTitle(userDetails.getTitle());
        existingUser.setMobile(userDetails.getMobile());
        existingUser.setWebsite(userDetails.getWebsite());
        existingUser.setBio(userDetails.getBio());
        existingUser.setAddress(userDetails.getAddress());
        // Save and return updated user
        return userRepository.save(existingUser);
    }
}
