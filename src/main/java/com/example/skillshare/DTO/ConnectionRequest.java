package com.example.skillshare.DTO;

import com.example.skillshare.model.Users;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "connection_requests")
public class ConnectionRequest {

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

    public ConnectionRequest() {}

    public ConnectionRequest(Users recipient, Users requester) {
        this.recipient = recipient;
        this.requester = requester;
        this.status = "PENDING";
    }
}
