package com.example.skillshare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.IdentityHashMap;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date otpexpireyTime;

    @OneToOne
    private  Users user;



}
