package com.example.skillshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String Name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String mobile;
    private String title;
    @Column(length = 500)
    private String bio;
    private String address;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String website;
//    @Column(nullable = false, unique = true)
//    private String username;
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    @Column(name = "profile_photo_url")
    private String profilePhoto;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    // Maintain password change history
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasswordChangeHistory> passwordChangeHistory;

    public Users() {}

    public Users(String name, String email, String password, String mobile, String title, String bio, String address, String website) {
        this.Name = name;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.title = title;
        this.bio = bio;
        this.address = address;
        this.website = website;
    }

    // Getters and Setters for all fields

    public String getProfilePhotoUrl() {
        return profilePhoto;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhoto = profilePhotoUrl;
    }
}
//    private String avatar;
//
//    @Column(nullable = false)
//    private boolean connected;
//
//    @Column(nullable = false)
//    private boolean isOnline;

