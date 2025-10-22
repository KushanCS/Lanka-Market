package com.example.lankamarket.features.user_management.observer;

import com.example.lankamarket.features.user_management.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Observer pattern implementation for User events
 * Publishes events when user operations occur
 */
@Component
public class UserEventPublisher {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public UserEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    public void publishUserCreated(User user) {
        UserEvent event = new UserEvent(UserEventType.CREATED, user, LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }
    
    public void publishUserUpdated(User user) {
        UserEvent event = new UserEvent(UserEventType.UPDATED, user, LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }
    
    public void publishUserDeleted(User user) {
        UserEvent event = new UserEvent(UserEventType.DELETED, user, LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }
    
    public void publishUserRestored(User user) {
        UserEvent event = new UserEvent(UserEventType.RESTORED, user, LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }
    
    public void publishUserLogin(User user) {
        UserEvent event = new UserEvent(UserEventType.LOGIN, user, LocalDateTime.now());
        eventPublisher.publishEvent(event);
    }
}

/**
 * User event types
 */
enum UserEventType {
    CREATED,
    UPDATED,
    DELETED,
    RESTORED,
    LOGIN
}

/**
 * User event class
 */
class UserEvent {
    private final UserEventType eventType;
    private final User user;
    private final LocalDateTime timestamp;
    
    public UserEvent(UserEventType eventType, User user, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.user = user;
        this.timestamp = timestamp;
    }
    
    public UserEventType getEventType() {
        return eventType;
    }
    
    public User getUser() {
        return user;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "UserEvent{" +
                "eventType=" + eventType +
                ", userId=" + user.getId() +
                ", timestamp=" + timestamp +
                '}';
    }
}
