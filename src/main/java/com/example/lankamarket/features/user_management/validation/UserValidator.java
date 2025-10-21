package com.example.lankamarket.features.user_management.validation;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.user_management.UserRepository;
import com.example.lankamarket.features.user_management.exceptions.InvalidUserOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Strategy pattern implementation for User validation
 * Different validation strategies can be implemented and used interchangeably
 */
@Component
public class UserValidator {
    
    @Autowired
    private UserRepository userRepository;
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Password validation pattern (at least 8 chars, 1 uppercase, 1 lowercase, 1 digit)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );
    
    /**
     * Validates a user for creation
     */
    public void validateForCreation(User user) {
        List<String> errors = new ArrayList<>();
        
        // Basic field validation
        validateBasicFields(user, errors);
        
        // Email uniqueness validation
        validateEmailUniqueness(user.getEmail(), null, errors);
        
        if (!errors.isEmpty()) {
            throw new InvalidUserOperationException("Validation failed: " + String.join(", ", errors));
        }
    }
    
    /**
     * Validates a user for update
     */
    public void validateForUpdate(User user, Long existingUserId) {
        List<String> errors = new ArrayList<>();
        
        // Basic field validation
        validateBasicFields(user, errors);
        
        // Email uniqueness validation (excluding current user)
        validateEmailUniqueness(user.getEmail(), existingUserId, errors);
        
        if (!errors.isEmpty()) {
            throw new InvalidUserOperationException("Validation failed: " + String.join(", ", errors));
        }
    }
    
    /**
     * Validates basic user fields
     */
    private void validateBasicFields(User user, List<String> errors) {
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            errors.add("First name is required");
        } else if (user.getFirstName().length() < 2 || user.getFirstName().length() > 50) {
            errors.add("First name must be between 2 and 50 characters");
        }
        
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            errors.add("Last name is required");
        } else if (user.getLastName().length() < 2 || user.getLastName().length() > 50) {
            errors.add("Last name must be between 2 and 50 characters");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("Email format is invalid");
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            errors.add("Password is required");
        } else if (!PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            errors.add("Password must be at least 8 characters with uppercase, lowercase, and digit");
        }
        
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            errors.add("Address is required");
        } else if (user.getAddress().length() > 255) {
            errors.add("Address must not exceed 255 characters");
        }
    }
    
    /**
     * Validates email uniqueness
     */
    private void validateEmailUniqueness(String email, Long excludeUserId, List<String> errors) {
        if (email != null && !email.trim().isEmpty()) {
            boolean emailExists;
            if (excludeUserId != null) {
                // For updates, check if email exists for other users
                emailExists = userRepository.findByEmailAndIsDeletedFalse(email)
                    .map(user -> !user.getId().equals(excludeUserId))
                    .orElse(false);
            } else {
                // For creation, check if email exists at all
                emailExists = userRepository.existsByEmailAndIsDeletedFalse(email);
            }
            
            if (emailExists) {
                errors.add("Email already exists");
            }
        }
    }
    
    /**
     * Validates user for deletion
     */
    public void validateForDeletion(User user) {
        if (user.getIsDeleted()) {
            throw new InvalidUserOperationException("User is already deleted");
        }
    }
    
    /**
     * Validates user for restoration
     */
    public void validateForRestoration(User user) {
        if (!user.getIsDeleted()) {
            throw new InvalidUserOperationException("User is not deleted");
        }
    }
    
    /**
     * Validates login credentials
     */
    public void validateCredentials(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserOperationException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidUserOperationException("Password is required");
        }
    }
}
