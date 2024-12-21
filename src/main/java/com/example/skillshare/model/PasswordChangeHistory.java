package com.example.skillshare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PasswordChangeHistory")
public class PasswordChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private String changedPassword;

    @Column(nullable = false)
    private Date changeDate;

    public PasswordChangeHistory(Users user, String changedPassword) {
        this.user = user;
        this.changedPassword = changedPassword;
        this.changeDate = new Date();
    }
}
