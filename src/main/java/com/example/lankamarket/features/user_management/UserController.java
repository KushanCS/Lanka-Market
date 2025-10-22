package com.example.lankamarket.features.user_management;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            user.get().setReviews(null);
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get all active users
    @GetMapping
    public ResponseEntity<List<User>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get all deleted users
    @GetMapping("/deleted")
    public ResponseEntity<List<User>> getAllDeletedUsers() {
        List<User> users = userService.getAllDeletedUsers();
        return ResponseEntity.ok(users);
    }
    
    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<User> existingUser = userService.getUserById(id);
            if (!existingUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            user.setPassword(existingUser.get().getPassword());
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Soft delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("User with id " + id + " has been deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Permanent delete user
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<?> permanentDeleteUser(@PathVariable Long id) {
        try {
            userService.permanentDeleteUser(id);
            return ResponseEntity.ok("User with id " + id + " has been permanently deleted");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Restore deleted user
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {
        try {
            User restoredUser = userService.restoreUser(id);
            return ResponseEntity.ok(restoredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // Search users by name
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    // Search users by first name
    @GetMapping("/search/firstname")
    public ResponseEntity<List<User>> searchUsersByFirstName(@RequestParam String firstName) {
        List<User> users = userService.searchUsersByFirstName(firstName);
        return ResponseEntity.ok(users);
    }
    
    // Search users by last name
    @GetMapping("/search/lastname")
    public ResponseEntity<List<User>> searchUsersByLastName(@RequestParam String lastName) {
        List<User> users = userService.searchUsersByLastName(lastName);
        return ResponseEntity.ok(users);
    }
    
    // Get user statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats() {
        long activeCount = userService.getActiveUserCount();
        long deletedCount = userService.getDeletedUserCount();
        
        return ResponseEntity.ok(new UserStats(activeCount, deletedCount));
    }
    
    // Check if user exists by email
    @GetMapping("/exists")
    public ResponseEntity<?> checkUserExists(@RequestParam String email) {
        boolean exists = userService.userExistsByEmail(email);
        return ResponseEntity.ok(new UserExistsResponse(exists));
    }
    
    // Validate user credentials (for login)
    @PostMapping("/validate")
    public ResponseEntity<?> validateCredentials(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> user = userService.validateUserCredentials(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );
            
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error: " + e.getMessage());
        }
    }
    
    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            user.setReviews(null);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Authentication Error", e.getMessage()));
        }
    }
    
    // Inner classes for response objects
    public static class UserStats {
        private long activeUsers;
        private long deletedUsers;
        
        public UserStats(long activeUsers, long deletedUsers) {
            this.activeUsers = activeUsers;
            this.deletedUsers = deletedUsers;
        }
        
        // Getters
        public long getActiveUsers() { return activeUsers; }
        public long getDeletedUsers() { return deletedUsers; }
    }
    
    public static class UserExistsResponse {
        private boolean exists;
        
        public UserExistsResponse(boolean exists) {
            this.exists = exists;
        }
        
        public boolean isExists() { return exists; }
    }
    
    public static class ErrorResponse {
        private String error;
        private String message;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
        
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
    
    public static class LoginRequest {
        private String email;
        private String password;
        
        // Default constructor
        public LoginRequest() {}
        
        // Constructor
        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
        
        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
