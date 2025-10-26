package com.example.lankamarket.features.cart_management;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.user_management.UserRepository;
import com.example.lankamarket.features.product_management.Product;
import com.example.lankamarket.features.product_management.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Create cart for user
    public Cart createCart(Long userId, String name, String description) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Cart name cannot be null or empty");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            if (user.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot create cart for deleted user");
            }

            // Check if user already has a cart
            if (cartRepository.existsByUserIdAndIsDeletedFalse(userId)) {
                throw new IllegalArgumentException("User already has a cart");
            }

            Cart cart = new Cart(name, description, user);
            return cartRepository.save(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create cart: " + e.getMessage(), e);
        }
    }

    // Get cart by user ID
    public Optional<Cart> getCartByUserId(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            return cartRepository.findByUserIdAndIsDeletedFalse(userId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get cart by user ID: " + e.getMessage(), e);
        }
    }

    // Get cart by ID
    public Optional<Cart> getCartById(Long cartId) {
        try {
            if (cartId == null) {
                throw new IllegalArgumentException("Cart ID cannot be null");
            }
            return cartRepository.findByIdAndIsDeletedFalse(cartId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get cart by ID: " + e.getMessage(), e);
        }
    }

    // Update cart
    public Cart updateCart(Long cartId, String name, String description) {
        try {
            if (cartId == null) {
                throw new IllegalArgumentException("Cart ID cannot be null");
            }

            Cart cart = getCartByIdOrThrow(cartId);

            if (name != null && !name.trim().isEmpty()) {
                cart.setName(name);
            }
            if (description != null) {
                cart.setDescription(description);
            }

            return cartRepository.save(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update cart: " + e.getMessage(), e);
        }
    }

    // Add item to cart
    public Cart addItemToCart(Long userId, Long productId, Integer quantity) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Get or create cart for user
            Cart cart = getOrCreateCartForUser(userId);

            // Get product
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

            if (product.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot add deleted product to cart");
            }

            if (product.getInStockAmount() < quantity) {
                throw new IllegalArgumentException("Insufficient stock. Available: " + product.getInStockAmount() + ", Requested: " + quantity);
            }

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductIdAndIsDeletedFalse(cart.getId(), productId);

            if (existingItem.isPresent()) {
                // Update existing item quantity
                CartItem cartItem = existingItem.get();
                int newQuantity = cartItem.getQuantity() + quantity;
                
                if (product.getInStockAmount() < newQuantity) {
                    throw new IllegalArgumentException("Insufficient stock. Available: " + product.getInStockAmount() + ", Total requested: " + newQuantity);
                }
                
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            } else {
                // Create new cart item
                CartItem cartItem = new CartItem(quantity, cart, product);
                cartItemRepository.save(cartItem);
            }

            // Refresh cart to get updated items
            return cartRepository.findByIdAndIsDeletedFalse(cart.getId()).orElse(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage(), e);
        }
    }

    // Update cart item quantity
    public Cart updateCartItemQuantity(Long userId, Long productId, Integer newQuantity) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            if (newQuantity == null || newQuantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            Cart cart = getCartByUserIdOrThrow(userId);
            CartItem cartItem = cartItemRepository.findByCartIdAndProductIdAndIsDeletedFalse(cart.getId(), productId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

            Product product = cartItem.getProduct();
            if (product.getInStockAmount() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock. Available: " + product.getInStockAmount() + ", Requested: " + newQuantity);
            }

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);

            // Refresh cart to get updated items
            return cartRepository.findByIdAndIsDeletedFalse(cart.getId()).orElse(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }

    // Remove item from cart
    public Cart removeItemFromCart(Long userId, Long productId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }

            Cart cart = getCartByUserIdOrThrow(userId);
            CartItem cartItem = cartItemRepository.findByCartIdAndProductIdAndIsDeletedFalse(cart.getId(), productId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

            cartItem.softDelete();
            cartItemRepository.save(cartItem);

            // Refresh cart to get updated items
            return cartRepository.findByIdAndIsDeletedFalse(cart.getId()).orElse(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove item from cart: " + e.getMessage(), e);
        }
    }

    // Clear cart
    @Transactional
    public Cart clearCart(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            Cart cart = getCartByUserIdOrThrow(userId);
            
            // Soft delete all cart items
            cartItemRepository.softDeleteCartItemsByCart(cart.getId());
            
            // Refresh cart to get updated items
            return cartRepository.findByIdAndIsDeletedFalse(cart.getId()).orElse(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear cart: " + e.getMessage(), e);
        }
    }

    // Get cart items
    public List<CartItem> getCartItems(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            Cart cart = getCartByUserIdOrThrow(userId);
            return cartItemRepository.findByCartIdAndIsDeletedFalseOrderByCreatedAtDesc(cart.getId());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get cart items: " + e.getMessage(), e);
        }
    }

    // Get cart items ordered by created date
    public List<CartItem> getCartItemsOrderedByCreatedDate(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            Cart cart = getCartByUserIdOrThrow(userId);
            return cartItemRepository.findByCartIdAndIsDeletedFalseOrderByCreatedAtDesc(cart.getId());

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get cart items ordered by created date: " + e.getMessage(), e);
        }
    }

    // Soft delete cart
    public void deleteCart(Long cartId) {
        try {
            if (cartId == null) {
                throw new IllegalArgumentException("Cart ID cannot be null");
            }

            Cart cart = getCartByIdOrThrow(cartId);
            cart.softDelete();
            cartRepository.save(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete cart: " + e.getMessage(), e);
        }
    }

    // Restore cart
    public Cart restoreCart(Long cartId) {
        try {
            if (cartId == null) {
                throw new IllegalArgumentException("Cart ID cannot be null");
            }

            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));

            if (!cart.getIsDeleted()) {
                throw new IllegalArgumentException("Cart is not deleted");
            }

            cart.restore();
            return cartRepository.save(cart);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore cart: " + e.getMessage(), e);
        }
    }

    // Get all active carts
    public List<Cart> getAllActiveCarts() {
        try {
            return cartRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all active carts: " + e.getMessage(), e);
        }
    }

    // Get recent carts
    public List<Cart> getRecentCarts(int days) {
        try {
            if (days < 0) {
                throw new IllegalArgumentException("Days cannot be negative");
            }

            LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
            return cartRepository.getRecentCarts(sinceDate);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get recent carts: " + e.getMessage(), e);
        }
    }

    // Get carts with more than N items
    public List<Cart> getCartsWithMoreThanNItems(int minItems) {
        try {
            if (minItems < 0) {
                throw new IllegalArgumentException("Minimum items cannot be negative");
            }
            return cartRepository.findCartsWithMoreThanNItems(minItems);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get carts with more than N items: " + e.getMessage(), e);
        }
    }

    // Get cart statistics
    public long getActiveCartCount() {
        try {
            return cartRepository.countActiveCarts();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active cart count: " + e.getMessage(), e);
        }
    }

    public long getEmptyCartCount() {
        try {
            return cartRepository.countEmptyCarts();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get empty cart count: " + e.getMessage(), e);
        }
    }

    public long getNonEmptyCartCount() {
        try {
            return cartRepository.countNonEmptyCarts();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get non-empty cart count: " + e.getMessage(), e);
        }
    }

    public Double getAverageItemsPerCart() {
        try {
            return cartRepository.getAverageItemsPerCart();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get average items per cart: " + e.getMessage(), e);
        }
    }

    // Helper methods
    private Cart getCartByIdOrThrow(Long cartId) {
        return cartRepository.findByIdAndIsDeletedFalse(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with ID: " + cartId));
    }

    private Cart getCartByUserIdOrThrow(Long userId) {
        return cartRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user ID: " + userId));
    }

    private Cart getOrCreateCartForUser(Long userId) {
        Optional<Cart> existingCart = cartRepository.findByUserIdAndIsDeletedFalse(userId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        // Create new cart for user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Cart cart = new Cart("My Cart", "Shopping cart for " + user.getFirstName() + " " + user.getLastName(), user);
        return cartRepository.save(cart);
    }
}
