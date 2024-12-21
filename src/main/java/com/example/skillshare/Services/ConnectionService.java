package com.example.skillshare.Services;

import com.example.skillshare.model.Connection;
import com.example.skillshare.model.Users;
import com.example.skillshare.Reporistry.ConnectionRepository;
import com.example.skillshare.Reporistry.Signin_Repo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final Signin_Repo usersRepository;

    public ConnectionService(ConnectionRepository connectionRepository, Signin_Repo usersRepository) {
        this.connectionRepository = connectionRepository;
        this.usersRepository = usersRepository;
    }

    // Method to send connection request
    public void sendRequest(int recipientId) {
        // Fetch the recipient using the recipientId
        Users recipient = usersRepository.findById(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Recipient not found"));

        // Fetch the currently authenticated user (requester)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assuming the username is used for identification

        Users requester = usersRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));

        // Check if a connection request already exists
        Optional<Connection> existingRequest = connectionRepository.findByRecipientId(recipientId);
        if (existingRequest.isPresent()) {
            throw new IllegalArgumentException("Connection request already exists");
        }

        // Create and save the new connection request
        Connection newRequest = new Connection();
        newRequest.setRecipient(recipient);
        newRequest.setRequester(requester);
        newRequest.setStatus("PENDING"); // Default status is PENDING
        connectionRepository.save(newRequest);
    }

    // Method to accept a connection request
    public void acceptRequest(int recipientId) {
        Connection request = connectionRepository.findByRecipientId(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus("ACCEPTED");
        connectionRepository.save(request);
    }

    // Method to reject a connection request
    public void rejectRequest(int recipientId) {
        Connection request = connectionRepository.findByRecipientId(recipientId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        connectionRepository.delete(request);
    }

    // Method to get all accepted connections
    public List<Users> getConnections() {
        List<Connection> connections = connectionRepository.findAllAccepted();
        // Convert Connection to Users for the response
        return connections.stream()
                .map(Connection::getRecipient)
                .collect(Collectors.toList());
    }

    // Method to remove a connection
    public void removeConnection(int userId) {
        Connection connection = connectionRepository.findByRecipientId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Connection not found"));

        connectionRepository.delete(connection);
    }
}
