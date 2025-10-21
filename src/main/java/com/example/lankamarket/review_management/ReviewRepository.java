package com.example.lankamarket.review_management;

import com.example.lankamarket.features.user_management.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // Find all reviews that are not deleted
    List<Review> findByIsDeletedFalse();
    
    // Find all reviews that are deleted
    List<Review> findByIsDeletedTrue();
    
    // Find reviews by user (not deleted)
    List<Review> findByUserAndIsDeletedFalse(User user);
    
    // Find reviews by user ID (not deleted)
    List<Review> findByUserIdAndIsDeletedFalse(Long userId);
    
    // Find reviews by rating (not deleted)
    List<Review> findByRatingAndIsDeletedFalse(Integer rating);
    
    // Find reviews by rating range (not deleted)
    List<Review> findByRatingBetweenAndIsDeletedFalse(Integer minRating, Integer maxRating);
    
    // Find reviews with high rating (4-5 stars, not deleted)
    List<Review> findByRatingGreaterThanEqualAndIsDeletedFalse(Integer minRating);
    
    // Find reviews with low rating (1-2 stars, not deleted)
    List<Review> findByRatingLessThanEqualAndIsDeletedFalse(Integer maxRating);
    
    // Find reviews by title containing (not deleted)
    List<Review> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title);
    
    // Find reviews by content containing (not deleted)
    List<Review> findByContentContainingIgnoreCaseAndIsDeletedFalse(String content);
    
    // Find reviews created after a specific date (not deleted)
    List<Review> findByCreatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    
    // Find reviews created before a specific date (not deleted)
    List<Review> findByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    
    // Find reviews created between dates (not deleted)
    List<Review> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    
    // Count active reviews
    long countByIsDeletedFalse();
    
    // Count deleted reviews
    long countByIsDeletedTrue();
    
    // Count reviews by user (not deleted)
    long countByUserAndIsDeletedFalse(User user);
    
    // Count reviews by user ID (not deleted)
    long countByUserIdAndIsDeletedFalse(Long userId);
    
    // Count reviews by rating (not deleted)
    long countByRatingAndIsDeletedFalse(Integer rating);
    
    // Count reviews by rating range (not deleted)
    long countByRatingBetweenAndIsDeletedFalse(Integer minRating, Integer maxRating);
    
    // Count reviews with high rating (4-5 stars, not deleted)
    long countByRatingGreaterThanEqualAndIsDeletedFalse(Integer minRating);
    
    // Count reviews with low rating (1-2 stars, not deleted)
    long countByRatingLessThanEqualAndIsDeletedFalse(Integer maxRating);
    
    // Find reviews ordered by rating descending (not deleted)
    List<Review> findByIsDeletedFalseOrderByRatingDesc();
    
    // Find reviews ordered by rating ascending (not deleted)
    List<Review> findByIsDeletedFalseOrderByRatingAsc();
    
    // Find reviews ordered by creation date descending (not deleted)
    List<Review> findByIsDeletedFalseOrderByCreatedAtDesc();
    
    // Find reviews ordered by creation date ascending (not deleted)
    List<Review> findByIsDeletedFalseOrderByCreatedAtAsc();
    
    // Find reviews ordered by title (not deleted)
    List<Review> findByIsDeletedFalseOrderByTitleAsc();
    
    // Find reviews ordered by update date (not deleted)
    List<Review> findByIsDeletedFalseOrderByUpdatedAtDesc();
    
    // Search reviews by multiple criteria
    @Query("SELECT r FROM Review r WHERE " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND r.isDeleted = false")
    List<Review> searchReviewsByMultipleCriteria(@Param("searchTerm") String searchTerm);
    
    // Search reviews by user and multiple criteria
    @Query("SELECT r FROM Review r WHERE " +
           "r.user = :user AND " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND r.isDeleted = false")
    List<Review> searchReviewsByUserAndMultipleCriteria(@Param("user") User user, @Param("searchTerm") String searchTerm);
    
    // Find reviews by user and rating
    @Query("SELECT r FROM Review r WHERE " +
           "r.user = :user AND " +
           "r.rating = :rating " +
           "AND r.isDeleted = false")
    List<Review> findReviewsByUserAndRating(@Param("user") User user, @Param("rating") Integer rating);
    
    // Find reviews by user and rating range
    @Query("SELECT r FROM Review r WHERE " +
           "r.user = :user AND " +
           "r.rating BETWEEN :minRating AND :maxRating " +
           "AND r.isDeleted = false")
    List<Review> findReviewsByUserAndRatingRange(@Param("user") User user, 
                                                @Param("minRating") Integer minRating, 
                                                @Param("maxRating") Integer maxRating);
    
    // Get average rating for a user
    @Query("SELECT AVG(r.rating) FROM Review r WHERE " +
           "r.user = :user AND " +
           "r.isDeleted = false")
    Double getAverageRatingByUser(@Param("user") User user);
    
    // Get average rating for all reviews
    @Query("SELECT AVG(r.rating) FROM Review r WHERE " +
           "r.isDeleted = false")
    Double getAverageRating();
    
    // Get rating distribution (count by rating)
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE " +
           "r.isDeleted = false " +
           "GROUP BY r.rating " +
           "ORDER BY r.rating DESC")
    List<Object[]> getRatingDistribution();
    
    // Get recent reviews (within last N days)
    @Query("SELECT r FROM Review r WHERE " +
           "r.createdAt >= :sinceDate " +
           "AND r.isDeleted = false " +
           "ORDER BY r.createdAt DESC")
    List<Review> getRecentReviews(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Get top rated reviews (5 stars)
    @Query("SELECT r FROM Review r WHERE " +
           "r.rating = 5 " +
           "AND r.isDeleted = false " +
           "ORDER BY r.createdAt DESC")
    List<Review> getTopRatedReviews();
    
    // Get reviews by user ordered by rating
    @Query("SELECT r FROM Review r WHERE " +
           "r.user = :user " +
           "AND r.isDeleted = false " +
           "ORDER BY r.rating DESC, r.createdAt DESC")
    List<Review> getReviewsByUserOrderedByRating(@Param("user") User user);
}
