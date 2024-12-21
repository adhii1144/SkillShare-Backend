package com.example.skillshare.Controller;

import com.example.skillshare.DTO.ConnectionRequestDto;
import com.example.skillshare.Services.ConnectionService;
import com.example.skillshare.model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/requests")
    public ResponseEntity<Void> sendRequest(@RequestBody ConnectionRequestDto request) {
        connectionService.sendRequest(request.recipientId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{userId}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable int userId) {
        connectionService.acceptRequest(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{userId}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable int userId) {
        connectionService.rejectRequest(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Users>> getConnections() {
        List<Users> connections = connectionService.getConnections();
        return ResponseEntity.ok(connections);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeConnection(@PathVariable int userId) {
        connectionService.removeConnection(userId);
        return ResponseEntity.ok().build();
    }
}
