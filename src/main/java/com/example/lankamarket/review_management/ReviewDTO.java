package com.example.lankamarket.review_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class ReviewDTO {
    
    private Long id;
    
    @NotBlank(message = "Review title is required")
    @Size(min = 2, max = 200, message = "Review title must be between 2 and 200 characters")
    private String title;
    
    @NotBlank(message = "Review content is required")
    @Size(max = 1000, message = "Review content must not exceed 1000 characters")
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private Long userId; // Only user ID, not the full user object
    private String userName; // User's name for display
    
    // Calculated fields
    private String ratingStars;
    private String ratingDescription;
    private boolean isRecent;
    private String displayTitleWithRating;
    
    // Default constructor
    public ReviewDTO() {
    }
    
    // Constructor from Review entity
    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
        this.userId = review.getUser() != null ? review.getUser().getId() : null;
        this.userName = review.getUser() != null ? 
            (review.getUser().getFirstName() + " " + review.getUser().getLastName()).trim() : null;
        
        // Calculate derived fields
        this.ratingStars = calculateRatingStars();
        this.ratingDescription = calculateRatingDescription();
        this.isRecent = calculateIsRecent();
        this.displayTitleWithRating = calculateDisplayTitleWithRating();
    }
    
    // Constructor for creating new review
    public ReviewDTO(String title, String content, Integer rating, Long userId, String userName) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.userId = userId;
        this.userName = userName;
        
        // Calculate derived fields
        this.ratingStars = calculateRatingStars();
        this.ratingDescription = calculateRatingDescription();
        this.isRecent = calculateIsRecent();
        this.displayTitleWithRating = calculateDisplayTitleWithRating();
    }
    
    // Helper methods for calculated fields
    private String calculateRatingStars() {
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
    
    private String calculateRatingDescription() {
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
    
    private boolean calculateIsRecent() {
        if (createdAt == null) return false;
        return createdAt.isAfter(LocalDateTime.now().minusDays(30));
    }
    
    private String calculateDisplayTitleWithRating() {
        return title + " (" + ratingStars + ")";
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getRatingStars() {
        return ratingStars;
    }
    
    public void setRatingStars(String ratingStars) {
        this.ratingStars = ratingStars;
    }
    
    public String getRatingDescription() {
        return ratingDescription;
    }
    
    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }
    
    public boolean isRecent() {
        return isRecent;
    }
    
    public void setRecent(boolean recent) {
        isRecent = recent;
    }
    
    public String getDisplayTitleWithRating() {
        return displayTitleWithRating;
    }
    
    public void setDisplayTitleWithRating(String displayTitleWithRating) {
        this.displayTitleWithRating = displayTitleWithRating;
    }
    
    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", ratingStars='" + ratingStars + '\'' +
                ", ratingDescription='" + ratingDescription + '\'' +
                ", isRecent=" + isRecent +
                '}';
    }
}

// Create Review Request DTO
class CreateReviewRequestDTO {
    
    @NotBlank(message = "Review title is required")
    @Size(min = 2, max = 200, message = "Review title must be between 2 and 200 characters")
    private String title;
    
    @NotBlank(message = "Review content is required")
    @Size(max = 1000, message = "Review content must not exceed 1000 characters")
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Default constructor
    public CreateReviewRequestDTO() {
    }
    
    // Getters and Setters
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
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "CreateReviewRequestDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", userId=" + userId +
                '}';
    }
}

// Update Review Request DTO
class UpdateReviewRequestDTO {
    
    @Size(min = 2, max = 200, message = "Review title must be between 2 and 200 characters")
    private String title;
    
    @Size(max = 1000, message = "Review content must not exceed 1000 characters")
    private String content;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    // Default constructor
    public UpdateReviewRequestDTO() {
    }
    
    // Getters and Setters
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
    
    @Override
    public String toString() {
        return "UpdateReviewRequestDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                '}';
    }
}
