package services;

import dto.WishlistItemDTO;
import entities.Product;
import entities.User;
import entities.Wishlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.WishlistRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Add product to wishlist
    public String addToWishlist(Long userId, Long productId) {
        try {
            User user = userService.getUserById(userId);
            Product product = productService.getProductById(productId);

            // Check if product already exists in wishlist
            Optional<Wishlist> existingItem = wishlistRepository.findByUser_IdAndProduct_Id(userId, productId);
            if (existingItem.isPresent()) {
                return "Product is already in your wishlist";
            }

            // Create new wishlist item
            Wishlist wishlistItem = new Wishlist(user, product);
            wishlistRepository.save(wishlistItem);

            return "Product added to wishlist successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error adding product to wishlist: " + e.getMessage());
        }
    }

    // Get user's wishlist items
    public List<WishlistItemDTO> getUserWishlistItems(Long userId) {
        try {
            return wishlistRepository.findWishlistItemsByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting user's wishlist items: " + e.getMessage());
        }
    }

    // Remove item from wishlist
    public String removeFromWishlist(Long userId, Long productId) {
        try {
            // Check if item exists
            Optional<Wishlist> wishlistItem = wishlistRepository.findByUser_IdAndProduct_Id(userId, productId);
            if (wishlistItem.isEmpty()) {
                return "Product not found in your wishlist";
            }

            wishlistRepository.deleteByUser_IdAndProduct_Id(userId, productId);
            return "Product removed from wishlist successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error removing product from wishlist: " + e.getMessage());
        }
    }

    // Check if product is in user's wishlist
    public boolean isProductInWishlist(Long userId, Long productId) {
        try {
            return wishlistRepository.findByUser_IdAndProduct_Id(userId, productId).isPresent();
        } catch (Exception e) {
            throw new RuntimeException("Error checking wishlist status: " + e.getMessage());
        }
    }

    // Get wishlist count for user
    public Long getWishlistCount(Long userId) {
        try {
            return wishlistRepository.countByUser_Id(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error getting wishlist count: " + e.getMessage());
        }
    }

    // Clear user's entire wishlist
    public String clearUserWishlist(Long userId) {
        try {
            List<Wishlist> userWishlistItems = wishlistRepository.findByUser_Id(userId);
            if (userWishlistItems.isEmpty()) {
                return "Your wishlist is already empty";
            }
            
            wishlistRepository.deleteAll(userWishlistItems);
            return "Wishlist cleared successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error clearing wishlist: " + e.getMessage());
        }
    }

    public Wishlist getWishlistById(Long id) {
        return wishlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wishlist not found with id: " + id));
    }
}