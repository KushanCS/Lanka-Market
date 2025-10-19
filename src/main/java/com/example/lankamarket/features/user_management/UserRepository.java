package com.example.lankamarket.features.user_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find user by email and not deleted
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    
    // Find all users that are not deleted
    List<User> findByIsDeletedFalse();
    
    // Find all users that are deleted
    List<User> findByIsDeletedTrue();
    
    // Check if email exists and user is not deleted
    boolean existsByEmailAndIsDeletedFalse(String email);
    
    // Find users by first name (not deleted)
    List<User> findByFirstNameContainingIgnoreCaseAndIsDeletedFalse(String firstName);
    
    // Find users by last name (not deleted)
    List<User> findByLastNameContainingIgnoreCaseAndIsDeletedFalse(String lastName);
    
    // Find users by full name search (not deleted)
    @Query("SELECT u FROM User u WHERE " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND u.isDeleted = false")
    List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);
    
    // Count active users
    long countByIsDeletedFalse();
    
    // Count deleted users
    long countByIsDeletedTrue();
}
