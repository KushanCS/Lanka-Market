package com.example.lankamarket.features.user_management;

import com.example.lankamarket.features.user_management.builder.UserBuilder;
import com.example.lankamarket.features.user_management.exceptions.*;
import com.example.lankamarket.features.user_management.factory.UserOperationFactory;
import com.example.lankamarket.features.user_management.observer.UserEventPublisher;
import com.example.lankamarket.features.user_management.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserValidator userValidator;
    
    @Autowired
    private UserOperationFactory operationFactory;
    
    @Autowired
    private UserEventPublisher eventPublisher;
    
    // Create a new user using Builder pattern and validation
    public User createUser(User user) {
        // Validate user data using Strategy pattern
        userValidator.validateForCreation(user);
        
        // Ensure isDeleted is set to false
        user.setIsDeleted(false);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Publish event using Observer pattern
        eventPublisher.publishUserCreated(savedUser);
        
        return savedUser;
    }
    
    // Create user using Builder pattern
    public User createUser(String firstName, String lastName, String email, String password, String address) {
        User user = UserBuilder.createActiveUser()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .address(address)
                .build();
        
        return createUser(user);
    }
    
    // Get user by ID (only if not deleted) using Factory pattern
    public Optional<User> getUserById(Long id) {
        try {
            User user = operationFactory.createUserFinder().findByIdAndNotDeleted(id);
            return Optional.of(user);
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }
    
    // Get user by ID (throws exception if not found)
    public User getUserByIdOrThrow(Long id) {
        return operationFactory.createUserFinder().findByIdAndNotDeleted(id);
    }
    
    // Get user by email (only if not deleted) using Factory pattern
    public Optional<User> getUserByEmail(String email) {
        try {
            User user = operationFactory.createUserFinder().findByEmailAndNotDeleted(email);
            return Optional.of(user);
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }
    }
    
    // Get user by email (throws exception if not found)
    public User getUserByEmailOrThrow(String email) {
        return operationFactory.createUserFinder().findByEmailAndNotDeleted(email);
    }
    
    // Get all active users using Factory pattern
    public List<User> getAllActiveUsers() {
        List<User> users = operationFactory.createUserSearcher().findAllActive();
    
        for (User user : users) {
            user.setReviews(null);
        }
    
        return users;
    }
    
    
    // Get all deleted users using Factory pattern
    public List<User> getAllDeletedUsers() {
        return operationFactory.createUserSearcher().findAllDeleted();
    }
    
    // Update user using Factory pattern and validation
    public User updateUser(Long id, User updatedUser) {
        // Find existing user using Factory pattern
        User existingUser = operationFactory.createUserFinder().findByIdAndNotDeleted(id);
        
        // Validate updated user data using Strategy pattern
        userValidator.validateForUpdate(updatedUser, id);
        
        // Update user fields using Factory pattern
        User savedUser = operationFactory.createUserUpdater().updateUserFields(existingUser, updatedUser);
        
        // Publish event using Observer pattern
        eventPublisher.publishUserUpdated(savedUser);
        
        return savedUser;
    }
    
    // Soft delete user using Factory pattern and validation
    public void deleteUser(Long id) {
        // Find user using Factory pattern
        User user = operationFactory.createUserFinder().findByIdAndNotDeleted(id);
        
        // Validate for deletion using Strategy pattern
        userValidator.validateForDeletion(user);
        
        // Soft delete using Factory pattern
        operationFactory.createUserDeleter().softDelete(user);
        
        // Publish event using Observer pattern
        eventPublisher.publishUserDeleted(user);
    }
    
    // Hard delete user (permanent deletion) using Factory pattern
    public void permanentDeleteUser(Long id) {
        // Find user using Factory pattern
        User user = operationFactory.createUserFinder().findById(id);
        
        // Publish event before deletion using Observer pattern
        eventPublisher.publishUserDeleted(user);
        
        // Permanent delete using Factory pattern
        operationFactory.createUserDeleter().permanentDelete(user);
    }
    
    // Restore deleted user using Factory pattern and validation
    public User restoreUser(Long id) {
        // Find user using Factory pattern
        User user = operationFactory.createUserFinder().findById(id);
        
        // Validate for restoration using Strategy pattern
        userValidator.validateForRestoration(user);
        
        // Restore user using Factory pattern
        User restoredUser = operationFactory.createUserUpdater().restoreUser(user);
        
        // Publish event using Observer pattern
        eventPublisher.publishUserRestored(restoredUser);
        
        return restoredUser;
    }
    
    // Search users by name using Factory pattern
    public List<User> searchUsersByName(String name) {
        return operationFactory.createUserSearcher().searchByName(name);
    }
    
    // Search users by first name using Factory pattern
    public List<User> searchUsersByFirstName(String firstName) {
        return operationFactory.createUserSearcher().searchByFirstName(firstName);
    }
    
    // Search users by last name using Factory pattern
    public List<User> searchUsersByLastName(String lastName) {
        return operationFactory.createUserSearcher().searchByLastName(lastName);
    }
    
    // Get user count statistics
    public long getActiveUserCount() {
        return userRepository.countByIsDeletedFalse();
    }
    
    public long getDeletedUserCount() {
        return userRepository.countByIsDeletedTrue();
    }
    
    // Check if user exists by email
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmailAndIsDeletedFalse(email);
    }
    
    // Validate user credentials (for login) using Strategy pattern and Observer pattern
    public Optional<User> validateUserCredentials(String email, String password) {
        // Validate credentials using Strategy pattern
        userValidator.validateCredentials(email, password);
        
        // Find user and validate password
        Optional<User> user = userRepository.findByEmailAndIsDeletedFalse(email)
                .filter(u -> u.getPassword().equals(password));
        
        // Publish login event if user found
        if (user.isPresent()) {
            eventPublisher.publishUserLogin(user.get());
        }
        
        return user;
    }
    
    // Login method for users
    public User loginUser(String email, String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            
            Optional<User> user = validateUserCredentials(email, password);
            if (user.isPresent()) {
                return user.get();
            } else {
                throw new RuntimeException("Invalid email or password");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }
}
