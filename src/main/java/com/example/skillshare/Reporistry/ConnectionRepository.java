package com.example.skillshare.Reporistry;

import com.example.skillshare.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {

    @Query("SELECT c FROM Connection c WHERE c.recipient.id = :recipientId")
    Optional<Connection> findByRecipientId(int recipientId);

    @Query("SELECT c FROM Connection c WHERE c.status = 'ACCEPTED'")
    List<Connection> findAllAccepted();

    // You can also add methods to find by requester if needed
    @Query("SELECT c FROM Connection c WHERE c.requester.id = :requesterId")
    List<Connection> findByRequesterId(int requesterId);
}
