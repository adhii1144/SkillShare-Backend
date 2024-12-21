package com.example.skillshare.Services;

import com.example.skillshare.Reporistry.Signin_Repo;
import com.example.skillshare.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Signin_Service {

    @Autowired
    private Signin_Repo repo;

    @Autowired
    private AzureBlobService azureBlobService; // Service for file handling in Azure

    // Register a new user
    public ResponseEntity<Users> registerUser(Users user) {
        if (repo.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // User already exists
        }

        Users savedUser = repo.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    // Upload or update profile photo
    public ResponseEntity<String> uploadProfilePhoto(String email, MultipartFile file) {
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded.");
            }

            // Find the user by email
            Users user = repo.findByEmail(email);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            // Upload file to Azure Blob Storage and get the file URL
            String fileUrl = azureBlobService.uploadFile(file);

            // Update user's profile photo URL (using `profilePhotoUrl` as an example field)
            user.setProfilePhotoUrl(fileUrl);
            repo.save(user);

            return ResponseEntity.ok("Profile photo uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading profile photo: " + e.getMessage());
        }
    }

    // Fetch the user's profile photo
    public ResponseEntity<byte[]> fetchProfilePhoto(String email) {
        try {
            Users user = repo.findByEmail(email);
            if (user == null || user.getProfilePhotoUrl() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Profile photo not found.".getBytes());
            }

            // Extract the blob name from the file URL
            String blobName = user.getProfilePhotoUrl().substring(user.getProfilePhotoUrl().lastIndexOf('/') + 1);

            // Download the file content from Azure Blob Storage
            byte[] fileContent = azureBlobService.downloadFile(blobName);
            return ResponseEntity.ok(fileContent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error fetching profile photo: " + e.getMessage()).getBytes());
        }
    }
}
