package com.example.skillshare.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "connections")
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Users recipient;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private Users requester;

    @Column(nullable = false)
    private String status; // PENDING, ACCEPTED, REJECTED

    public Connection() {}

    public Connection(Users recipient, Users requester, String status) {
        this.recipient = recipient;
        this.requester = requester;
        this.status = status;
    }
}
