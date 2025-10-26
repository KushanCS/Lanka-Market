package controllers;

import dto.WishlistItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.WishlistService;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // Health check endpoint
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Wishlist API is running! ✅");
    }

    // Add product to wishlist
    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            System.out.println("Adding to wishlist - userId: " + userId + ", productId: " + productId);
            String result = wishlistService.addToWishlist(userId, productId);
            System.out.println("Add result: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error adding to wishlist: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding product to wishlist: " + e.getMessage());
        }
    }

    // Get user's wishlist items
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistItemDTO>> getUserWishlist(@PathVariable Long userId) {
        try {
            System.out.println("Getting wishlist for userId: " + userId);
            List<WishlistItemDTO> wishlistItems = wishlistService.getUserWishlistItems(userId);
            System.out.println("Found " + (wishlistItems != null ? wishlistItems.size() : 0) + " wishlist items");
            return ResponseEntity.ok(wishlistItems);
        } catch (Exception e) {
            System.err.println("Error getting wishlist: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Remove product from wishlist
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            System.out.println("Removing from wishlist - userId: " + userId + ", productId: " + productId);
            String result = wishlistService.removeFromWishlist(userId, productId);
            System.out.println("Remove result: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error removing from wishlist: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing product from wishlist: " + e.getMessage());
        }
    }

    // Check if product is in user's wishlist
    @GetMapping("/check")
    public ResponseEntity<Boolean> isProductInWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            System.out.println("Checking wishlist status - userId: " + userId + ", productId: " + productId);
            boolean isInWishlist = wishlistService.isProductInWishlist(userId, productId);
            System.out.println("Is in wishlist: " + isInWishlist);
            return ResponseEntity.ok(isInWishlist);
        } catch (Exception e) {
            System.err.println("Error checking wishlist status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }
    }

    // Get wishlist count for user
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getWishlistCount(@PathVariable Long userId) {
        try {
            System.out.println("Getting wishlist count for userId: " + userId);
            Long count = wishlistService.getWishlistCount(userId);
            System.out.println("Wishlist count: " + count);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.err.println("Error getting wishlist count: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(0L);
        }
    }

    // Clear user's entire wishlist
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearWishlist(@PathVariable Long userId) {
        try {
            System.out.println("Clearing wishlist for userId: " + userId);
            String result = wishlistService.clearUserWishlist(userId);
            System.out.println("Clear result: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error clearing wishlist: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error clearing wishlist: " + e.getMessage());
        }
    }
}