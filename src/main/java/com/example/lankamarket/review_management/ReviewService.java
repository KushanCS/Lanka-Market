package com.example.lankamarket.review_management;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.user_management.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ReviewService with comprehensive business logic and error handling
 */
@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create a new review
    public Review createReview(Review review) {
        try {
            // Validate review data
            validateReviewForCreation(review);
            
            // Ensure isDeleted is set to false
            review.setIsDeleted(false);
            
            return reviewRepository.save(review);
            
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new RuntimeException("Failed to create review: " + e.getMessage(), e);
        }
    }
    
    // Create review with individual parameters
    public Review createReview(String title, String content, Integer rating, Long userId) {
        try {
            // Find user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
            
            Review review = new Review(title, content, rating, user);
            return createReview(review);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create review with title '" + title + "': " + e.getMessage(), e);
        }
    }
    
    // Get review by ID (only if not deleted)
    public Optional<Review> getReviewById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Review ID cannot be null");
            }
            
            return reviewRepository.findById(id)
                    .filter(review -> !review.getIsDeleted());
                    
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get review by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get review by ID (throws exception if not found)
    public Review getReviewByIdOrThrow(Long id) {
        try {
            Optional<Review> review = getReviewById(id);
            if (review.isPresent()) {
                return review.get();
            } else {
                throw new RuntimeException("Review not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get review by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get all active reviews
    public List<Review> getAllActiveReviews() {
        try {
            return reviewRepository.findByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all active reviews: " + e.getMessage(), e);
        }
    }
    
    // Get all deleted reviews
    public List<Review> getAllDeletedReviews() {
        try {
            return reviewRepository.findByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all deleted reviews: " + e.getMessage(), e);
        }
    }
    
    // Get reviews by user
    public List<Review> getReviewsByUser(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            
            return reviewRepository.findByUserIdAndIsDeletedFalse(userId);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews by user ID " + userId + ": " + e.getMessage(), e);
        }
    }
    
    // Get reviews by rating
    public List<Review> getReviewsByRating(Integer rating) {
        try {
            if (rating == null || rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            
            return reviewRepository.findByRatingAndIsDeletedFalse(rating);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews by rating " + rating + ": " + e.getMessage(), e);
        }
    }
    
    // Get reviews by rating range
    public List<Review> getReviewsByRatingRange(Integer minRating, Integer maxRating) {
        try {
            if (minRating == null || maxRating == null) {
                throw new IllegalArgumentException("Min rating and max rating cannot be null");
            }
            
            if (minRating < 1 || maxRating > 5 || minRating > maxRating) {
                throw new IllegalArgumentException("Rating range must be between 1 and 5, with min <= max");
            }
            
            return reviewRepository.findByRatingBetweenAndIsDeletedFalse(minRating, maxRating);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews by rating range: " + e.getMessage(), e);
        }
    }
    
    // Get high-rated reviews (4-5 stars)
    public List<Review> getHighRatedReviews() {
        try {
            return reviewRepository.findByRatingGreaterThanEqualAndIsDeletedFalse(4);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get high-rated reviews: " + e.getMessage(), e);
        }
    }
    
    // Get low-rated reviews (1-2 stars)
    public List<Review> getLowRatedReviews() {
        try {
            return reviewRepository.findByRatingLessThanEqualAndIsDeletedFalse(2);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get low-rated reviews: " + e.getMessage(), e);
        }
    }
    
    // Get top-rated reviews (5 stars)
    public List<Review> getTopRatedReviews() {
        try {
            return reviewRepository.getTopRatedReviews();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get top-rated reviews: " + e.getMessage(), e);
        }
    }
    
    // Get reviews ordered by rating descending
    public List<Review> getReviewsOrderedByRatingDesc() {
        try {
            return reviewRepository.findByIsDeletedFalseOrderByRatingDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews ordered by rating descending: " + e.getMessage(), e);
        }
    }
    
    // Get reviews ordered by rating ascending
    public List<Review> getReviewsOrderedByRatingAsc() {
        try {
            return reviewRepository.findByIsDeletedFalseOrderByRatingAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews ordered by rating ascending: " + e.getMessage(), e);
        }
    }
    
    // Get reviews ordered by creation date descending
    public List<Review> getReviewsOrderedByCreatedDateDesc() {
        try {
            return reviewRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews ordered by creation date descending: " + e.getMessage(), e);
        }
    }
    
    // Get reviews ordered by creation date ascending
    public List<Review> getReviewsOrderedByCreatedDateAsc() {
        try {
            return reviewRepository.findByIsDeletedFalseOrderByCreatedAtAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews ordered by creation date ascending: " + e.getMessage(), e);
        }
    }
    
    // Get reviews ordered by title
    public List<Review> getReviewsOrderedByTitle() {
        try {
            return reviewRepository.findByIsDeletedFalseOrderByTitleAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews ordered by title: " + e.getMessage(), e);
        }
    }
    
    // Update review
    public Review updateReview(Long id, Review updatedReview) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Review ID cannot be null");
            }
            
            // Validate updated review data
            validateReviewForUpdate(updatedReview, id);
            
            // Find existing review
            Review existingReview = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
            
            // Check if review is deleted
            if (existingReview.getIsDeleted()) {
                throw new RuntimeException("Cannot update deleted review with ID: " + id);
            }
            
            // Update fields
            existingReview.setTitle(updatedReview.getTitle());
            existingReview.setContent(updatedReview.getContent());
            existingReview.setRating(updatedReview.getRating());
            existingReview.setUser(updatedReview.getUser());
            
            return reviewRepository.save(existingReview);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update review with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Soft delete review
    public void deleteReview(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Review ID cannot be null");
            }
            
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
            
            if (review.getIsDeleted()) {
                throw new RuntimeException("Review with ID " + id + " is already deleted");
            }
            
            review.softDelete();
            reviewRepository.save(review);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete review with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Hard delete review (permanent deletion)
    public void permanentDeleteReview(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Review ID cannot be null");
            }
            
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
            
            reviewRepository.delete(review);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to permanently delete review with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Restore deleted review
    public Review restoreReview(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Review ID cannot be null");
            }
            
            Review review = reviewRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
            
            if (!review.getIsDeleted()) {
                throw new RuntimeException("Review with ID " + id + " is not deleted");
            }
            
            review.restore();
            return reviewRepository.save(review);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore review with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Search reviews by title
    public List<Review> searchReviewsByTitle(String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Search title cannot be null or empty");
            }
            
            return reviewRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(title);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search reviews by title '" + title + "': " + e.getMessage(), e);
        }
    }
    
    // Search reviews by content
    public List<Review> searchReviewsByContent(String content) {
        try {
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Search content cannot be null or empty");
            }
            
            return reviewRepository.findByContentContainingIgnoreCaseAndIsDeletedFalse(content);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search reviews by content '" + content + "': " + e.getMessage(), e);
        }
    }
    
    // Search reviews by multiple criteria
    public List<Review> searchReviewsByMultipleCriteria(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("Search term cannot be null or empty");
            }
            
            return reviewRepository.searchReviewsByMultipleCriteria(searchTerm);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search reviews by multiple criteria '" + searchTerm + "': " + e.getMessage(), e);
        }
    }
    
    // Get recent reviews (within last N days)
    public List<Review> getRecentReviews(int days) {
        try {
            if (days <= 0) {
                throw new IllegalArgumentException("Days must be greater than 0");
            }
            
            LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
            return reviewRepository.getRecentReviews(sinceDate);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get recent reviews: " + e.getMessage(), e);
        }
    }
    
    // Get reviews by user ordered by rating
    public List<Review> getReviewsByUserOrderedByRating(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
            
            return reviewRepository.getReviewsByUserOrderedByRating(user);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get reviews by user ordered by rating: " + e.getMessage(), e);
        }
    }
    
    // Get review count statistics
    public long getActiveReviewCount() {
        try {
            return reviewRepository.countByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active review count: " + e.getMessage(), e);
        }
    }
    
    public long getDeletedReviewCount() {
        try {
            return reviewRepository.countByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get deleted review count: " + e.getMessage(), e);
        }
    }
    
    public long getReviewCountByUser(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            
            return reviewRepository.countByUserIdAndIsDeletedFalse(userId);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get review count by user ID " + userId + ": " + e.getMessage(), e);
        }
    }
    
    public long getReviewCountByRating(Integer rating) {
        try {
            if (rating == null || rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            
            return reviewRepository.countByRatingAndIsDeletedFalse(rating);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get review count by rating " + rating + ": " + e.getMessage(), e);
        }
    }
    
    public long getHighRatedReviewCount() {
        try {
            return reviewRepository.countByRatingGreaterThanEqualAndIsDeletedFalse(4);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get high-rated review count: " + e.getMessage(), e);
        }
    }
    
    public long getLowRatedReviewCount() {
        try {
            return reviewRepository.countByRatingLessThanEqualAndIsDeletedFalse(2);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get low-rated review count: " + e.getMessage(), e);
        }
    }
    
    // Get average rating statistics
    public Double getAverageRating() {
        try {
            return reviewRepository.getAverageRating();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get average rating: " + e.getMessage(), e);
        }
    }
    
    public Double getAverageRatingByUser(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
            
            return reviewRepository.getAverageRatingByUser(user);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get average rating by user ID " + userId + ": " + e.getMessage(), e);
        }
    }
    
    // Get rating distribution
    public List<Object[]> getRatingDistribution() {
        try {
            return reviewRepository.getRatingDistribution();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get rating distribution: " + e.getMessage(), e);
        }
    }
    
    // Validation methods
    private void validateReviewForCreation(Review review) {
        if (review.getTitle() == null || review.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Review title is required");
        }
        
        if (review.getContent() == null || review.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Review content is required");
        }
        
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Review rating must be between 1 and 5");
        }
        
        if (review.getUser() == null) {
            throw new IllegalArgumentException("Review user is required");
        }
    }
    
    private void validateReviewForUpdate(Review review, Long existingReviewId) {
        if (review.getTitle() == null || review.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Review title is required");
        }
        
        if (review.getContent() == null || review.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Review content is required");
        }
        
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("Review rating must be between 1 and 5");
        }
        
        if (review.getUser() == null) {
            throw new IllegalArgumentException("Review user is required");
        }
    }
}
