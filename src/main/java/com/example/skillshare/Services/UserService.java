package com.example.skillshare.Services;


import com.example.skillshare.Reporistry.Signin_Repo;
import com.example.skillshare.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private Signin_Repo userRepository;

    private static final String DEFAULT_PROFILE_PIC = "/images/default-profile.jpg"; // default image URL

    public List<Users> searchUsers(String query, String skill, String location) {
        // Implement search logic
        return userRepository.findAll();  // Basic example, can be extended
    }

    public Optional<Users> getUserProfile(Integer userId) {
        return userRepository.findById(userId);
    }

    public Users updateUserProfile(Integer userId, Users updatedUser) {
        if (userRepository.existsById(userId)) {
            updatedUser.setId(userId);

            // If no profile image URL is provided, use the default
            if (updatedUser.getProfilePhotoUrl() == null || updatedUser.getProfilePhotoUrl().isEmpty()) {
                updatedUser.setProfilePhotoUrl(DEFAULT_PROFILE_PIC);
            }

            return userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public String uploadProfileImage(Integer userId, String imageUrl) {
        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            user.setProfilePhotoUrl(imageUrl); // Update with the uploaded image URL
            userRepository.save(user);
            return imageUrl;
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
