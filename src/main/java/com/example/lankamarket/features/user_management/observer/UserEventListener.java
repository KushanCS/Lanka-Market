package com.example.lankamarket.features.user_management.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener demonstrating Observer pattern
 * Listens to user events and performs actions
 */
@Component
public class UserEventListener {
    
    private static final Logger logger = LoggerFactory.getLogger(UserEventListener.class);
    
    @EventListener
    public void handleUserCreated(UserEvent event) {
        logger.info("User created: {} - {}", event.getUser().getId(), event.getUser().getEmail());
        // Here you could add additional logic like:
        // - Send welcome email
        // - Create user profile
        // - Log to audit system
        // - Update analytics
    }
    
    @EventListener
    public void handleUserUpdated(UserEvent event) {
        logger.info("User updated: {} - {}", event.getUser().getId(), event.getUser().getEmail());
        // Additional logic for user updates
    }
    
    @EventListener
    public void handleUserDeleted(UserEvent event) {
        logger.info("User deleted: {} - {}", event.getUser().getId(), event.getUser().getEmail());
        // Additional logic for user deletion
    }
    
    @EventListener
    public void handleUserRestored(UserEvent event) {
        logger.info("User restored: {} - {}", event.getUser().getId(), event.getUser().getEmail());
        // Additional logic for user restoration
    }
    
    @EventListener
    public void handleUserLogin(UserEvent event) {
        logger.info("User login: {} - {}", event.getUser().getId(), event.getUser().getEmail());
        // Additional logic for user login
    }
}
