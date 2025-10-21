package com.example.lankamarket.review_management;

import com.example.lankamarket.features.user_management.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Review title is required")
    @Size(min = 2, max = 200, message = "Review title must be between 2 and 200 characters")
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotBlank(message = "Review content is required")
    @Size(max = 1000, message = "Review content must not exceed 1000 characters")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;
    
    // Default constructor
    public Review() {
    }
    
    // Constructor with required fields
    public Review(String title, String content, Integer rating, User user) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.isDeleted = false;
    }
    
    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    // Helper method for soft delete
    public void softDelete() {
        this.isDeleted = true;
    }
    
    // Helper method for restore
    public void restore() {
        this.isDeleted = false;
    }
    
    // Helper method to get rating stars
    public String getRatingStars() {
        if (rating == null) return "";
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) {
            stars.append("★");
        }
        for (int i = rating; i < 5; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
    
    // Helper method to get rating description
    public String getRatingDescription() {
        if (rating == null) return "No rating";
        switch (rating) {
            case 1: return "Poor";
            case 2: return "Fair";
            case 3: return "Good";
            case 4: return "Very Good";
            case 5: return "Excellent";
            default: return "Unknown";
        }
    }
    
    // Helper method to check if review is recent (within last 30 days)
    public boolean isRecent() {
        if (createdAt == null) return false;
        return createdAt.isAfter(LocalDateTime.now().minusDays(30));
    }
    
    // Helper method to get display title with rating
    public String getDisplayTitleWithRating() {
        return title + " (" + getRatingStars() + ")";
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }
}
