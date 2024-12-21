package com.example.skillshare.Reporistry;

import com.example.skillshare.DTO.ConnectionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Integer> {

    Optional<ConnectionRequest> findByRecipientId(int recipientId);
}
