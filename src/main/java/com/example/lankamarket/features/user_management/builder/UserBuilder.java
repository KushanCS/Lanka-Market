package com.example.lankamarket.features.user_management.builder;

import com.example.lankamarket.features.user_management.User;

/**
 * Builder pattern implementation for User creation
 * Provides a fluent interface for building User objects
 */
public class UserBuilder {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private Boolean isDeleted = false;
    
    public UserBuilder() {}
    
    public UserBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    
    public UserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
    
    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }
    
    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }
    
    public UserBuilder address(String address) {
        this.address = address;
        return this;
    }
    
    public UserBuilder isDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        return this;
    }
    
    public User build() {
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setAddress(this.address);
        user.setIsDeleted(this.isDeleted);
        return user;
    }
    
    // Static factory method
    public static UserBuilder builder() {
        return new UserBuilder();
    }
    
    // Convenience methods for common scenarios
    public static UserBuilder createActiveUser() {
        return new UserBuilder().isDeleted(false);
    }
    
    public static UserBuilder createDeletedUser() {
        return new UserBuilder().isDeleted(true);
    }
}
