package com.example.lankamarket.review_management;

import com.example.lankamarket.features.user_management.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ReviewController with comprehensive REST endpoints and error handling
 */
@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserRepository userRepository;
    // Create a new review
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequestDTO request) {
        try {
            Review createdReview = reviewService.createReview(request.getTitle(), request.getContent(), request.getRating(), request.getUserId());
            ReviewDTO reviewDTO = new ReviewDTO(createdReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Create review with individual parameters
    @PostMapping("/create")
    public ResponseEntity<?> createReviewWithParams(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer rating,
            @RequestParam Long userId) {
        try {
            Review createdReview = reviewService.createReview(title, content, rating, userId);
            ReviewDTO reviewDTO = new ReviewDTO(createdReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        try {
            Optional<Review> review = reviewService.getReviewById(id);
            if (review.isPresent()) {
                ReviewDTO reviewDTO = new ReviewDTO(review.get());
                return ResponseEntity.ok(reviewDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get all active reviews
    @GetMapping
    public ResponseEntity<?> getAllActiveReviews() {
        try {
            List<Review> reviews = reviewService.getAllActiveReviews();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get all deleted reviews
    @GetMapping("/deleted")
    public ResponseEntity<?> getAllDeletedReviews() {
        try {
            List<Review> reviews = reviewService.getAllDeletedReviews();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUser(userId);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews by rating
    @GetMapping("/rating/{rating}")
    public ResponseEntity<?> getReviewsByRating(@PathVariable Integer rating) {
        try {
            List<Review> reviews = reviewService.getReviewsByRating(rating);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews by rating range
    @GetMapping("/rating-range")
    public ResponseEntity<?> getReviewsByRatingRange(@RequestParam Integer minRating, @RequestParam Integer maxRating) {
        try {
            List<Review> reviews = reviewService.getReviewsByRatingRange(minRating, maxRating);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get high-rated reviews (4-5 stars)
    @GetMapping("/high-rated")
    public ResponseEntity<?> getHighRatedReviews() {
        try {
            List<Review> reviews = reviewService.getHighRatedReviews();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get low-rated reviews (1-2 stars)
    @GetMapping("/low-rated")
    public ResponseEntity<?> getLowRatedReviews() {
        try {
            List<Review> reviews = reviewService.getLowRatedReviews();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get top-rated reviews (5 stars)
    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedReviews() {
        try {
            List<Review> reviews = reviewService.getTopRatedReviews();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews ordered by rating descending
    @GetMapping("/ordered/rating-desc")
    public ResponseEntity<?> getReviewsOrderedByRatingDesc() {
        try {
            List<Review> reviews = reviewService.getReviewsOrderedByRatingDesc();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews ordered by rating ascending
    @GetMapping("/ordered/rating-asc")
    public ResponseEntity<?> getReviewsOrderedByRatingAsc() {
        try {
            List<Review> reviews = reviewService.getReviewsOrderedByRatingAsc();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews ordered by creation date descending
    @GetMapping("/ordered/created-desc")
    public ResponseEntity<?> getReviewsOrderedByCreatedDateDesc() {
        try {
            List<Review> reviews = reviewService.getReviewsOrderedByCreatedDateDesc();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews ordered by creation date ascending
    @GetMapping("/ordered/created-asc")
    public ResponseEntity<?> getReviewsOrderedByCreatedDateAsc() {
        try {
            List<Review> reviews = reviewService.getReviewsOrderedByCreatedDateAsc();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews ordered by title
    @GetMapping("/ordered/title")
    public ResponseEntity<?> getReviewsOrderedByTitle() {
        try {
            List<Review> reviews = reviewService.getReviewsOrderedByTitle();
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Update review
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @Valid @RequestBody UpdateReviewRequestDTO request) {
        try {
            Optional<Review> existingReview = reviewService.getReviewById(id);
            Review review = new Review();
            review.setTitle(request.getTitle());
            review.setContent(request.getContent());
            review.setRating(request.getRating());
            review.setUser(existingReview.get().getUser());

            
            Review updatedReview = reviewService.updateReview(id, review);
            ReviewDTO reviewDTO = new ReviewDTO(updatedReview);
            return ResponseEntity.ok(reviewDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Soft delete review
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(new SuccessResponse("Review with ID " + id + " has been deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Permanent delete review
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<?> permanentDeleteReview(@PathVariable Long id) {
        try {
            reviewService.permanentDeleteReview(id);
            return ResponseEntity.ok(new SuccessResponse("Review with ID " + id + " has been permanently deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Restore deleted review
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreReview(@PathVariable Long id) {
        try {
            Review restoredReview = reviewService.restoreReview(id);
            ReviewDTO reviewDTO = new ReviewDTO(restoredReview);
            return ResponseEntity.ok(reviewDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Search reviews by title
    @GetMapping("/search/title")
    public ResponseEntity<?> searchReviewsByTitle(@RequestParam String title) {
        try {
            List<Review> reviews = reviewService.searchReviewsByTitle(title);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Search reviews by content
    @GetMapping("/search/content")
    public ResponseEntity<?> searchReviewsByContent(@RequestParam String content) {
        try {
            List<Review> reviews = reviewService.searchReviewsByContent(content);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Search reviews by multiple criteria
    @GetMapping("/search")
    public ResponseEntity<?> searchReviewsByMultipleCriteria(@RequestParam String searchTerm) {
        try {
            List<Review> reviews = reviewService.searchReviewsByMultipleCriteria(searchTerm);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get recent reviews
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentReviews(@RequestParam(defaultValue = "30") int days) {
        try {
            List<Review> reviews = reviewService.getRecentReviews(days);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get reviews by user ordered by rating
    @GetMapping("/user/{userId}/ordered/rating")
    public ResponseEntity<?> getReviewsByUserOrderedByRating(@PathVariable Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUserOrderedByRating(userId);
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(ReviewDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reviewDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get review statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getReviewStats() {
        try {
            long activeCount = reviewService.getActiveReviewCount();
            long deletedCount = reviewService.getDeletedReviewCount();
            long highRatedCount = reviewService.getHighRatedReviewCount();
            long lowRatedCount = reviewService.getLowRatedReviewCount();
            Double averageRating = reviewService.getAverageRating();
            
            return ResponseEntity.ok(new ReviewStats(activeCount, deletedCount, highRatedCount, lowRatedCount, averageRating));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get review count by user
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<?> getReviewCountByUser(@PathVariable Long userId) {
        try {
            long count = reviewService.getReviewCountByUser(userId);
            return ResponseEntity.ok(new ReviewCountResponse(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get review count by rating
    @GetMapping("/stats/rating/{rating}")
    public ResponseEntity<?> getReviewCountByRating(@PathVariable Integer rating) {
        try {
            long count = reviewService.getReviewCountByRating(rating);
            return ResponseEntity.ok(new ReviewCountResponse(count));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get average rating by user
    @GetMapping("/stats/user/{userId}/average-rating")
    public ResponseEntity<?> getAverageRatingByUser(@PathVariable Long userId) {
        try {
            Double averageRating = reviewService.getAverageRatingByUser(userId);
            return ResponseEntity.ok(new AverageRatingResponse(averageRating));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get rating distribution
    @GetMapping("/stats/rating-distribution")
    public ResponseEntity<?> getRatingDistribution() {
        try {
            List<Object[]> distribution = reviewService.getRatingDistribution();
            return ResponseEntity.ok(distribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Response classes
    public static class ErrorResponse {
        private String error;
        private String message;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
        
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
    
    public static class SuccessResponse {
        private String message;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
    }
    
    public static class ReviewStats {
        private long activeReviews;
        private long deletedReviews;
        private long highRatedReviews;
        private long lowRatedReviews;
        private Double averageRating;
        
        public ReviewStats(long activeReviews, long deletedReviews, long highRatedReviews, long lowRatedReviews, Double averageRating) {
            this.activeReviews = activeReviews;
            this.deletedReviews = deletedReviews;
            this.highRatedReviews = highRatedReviews;
            this.lowRatedReviews = lowRatedReviews;
            this.averageRating = averageRating;
        }
        
        public long getActiveReviews() { return activeReviews; }
        public long getDeletedReviews() { return deletedReviews; }
        public long getHighRatedReviews() { return highRatedReviews; }
        public long getLowRatedReviews() { return lowRatedReviews; }
        public Double getAverageRating() { return averageRating; }
    }
    
    public static class ReviewCountResponse {
        private long count;
        
        public ReviewCountResponse(long count) {
            this.count = count;
        }
        
        public long getCount() { return count; }
    }
    
    public static class AverageRatingResponse {
        private Double averageRating;
        
        public AverageRatingResponse(Double averageRating) {
            this.averageRating = averageRating;
        }
        
        public Double getAverageRating() { return averageRating; }
    }
}
