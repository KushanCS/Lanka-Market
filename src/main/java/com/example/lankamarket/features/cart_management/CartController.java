package com.example.lankamarket.features.cart_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    // Create cart
    @PostMapping
    public ResponseEntity<?> createCart(@Valid @RequestBody CreateCartRequestDTO request, @RequestParam Long userId) {
        try {
            Cart cart = cartService.createCart(userId, request.getName(), request.getDescription());
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get cart by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
        try {
            Optional<Cart> cart = cartService.getCartByUserId(userId);
            if (cart.isPresent()) {
                CartDTO cartDTO = new CartDTO(cart.get());
                return ResponseEntity.ok(cartDTO);
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

    // Get cart by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        try {
            Optional<Cart> cart = cartService.getCartById(id);
            if (cart.isPresent()) {
                CartDTO cartDTO = new CartDTO(cart.get());
                return ResponseEntity.ok(cartDTO);
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

    // Update cart
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @Valid @RequestBody UpdateCartRequestDTO request) {
        try {
            Cart cart = cartService.updateCart(id, request.getName(), request.getDescription());
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Add item to cart
    @PostMapping("/user/{userId}/items")
    public ResponseEntity<?> addItemToCart(@PathVariable Long userId, @Valid @RequestBody AddItemToCartRequestDTO request) {
        try {
            Cart cart = cartService.addItemToCart(userId, request.getProductId(), request.getQuantity());
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Update cart item quantity
    @PutMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable Long userId, @PathVariable Long productId, 
                                                   @Valid @RequestBody UpdateCartItemQuantityRequestDTO request) {
        try {
            Cart cart = cartService.updateCartItemQuantity(userId, productId, request.getQuantity());
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Remove item from cart
    @DeleteMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            Cart cart = cartService.removeItemFromCart(userId, productId);
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Clear cart
    @DeleteMapping("/user/{userId}/items")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            Cart cart = cartService.clearCart(userId);
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get cart items
    @GetMapping("/user/{userId}/items")
    public ResponseEntity<?> getCartItems(@PathVariable Long userId) {
        try {
            List<CartItem> cartItems = cartService.getCartItems(userId);
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(CartItemDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cartItemDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get cart items ordered by created date
    @GetMapping("/user/{userId}/items/ordered")
    public ResponseEntity<?> getCartItemsOrderedByCreatedDate(@PathVariable Long userId) {
        try {
            List<CartItem> cartItems = cartService.getCartItemsOrderedByCreatedDate(userId);
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(CartItemDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cartItemDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Delete cart (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok(new SuccessResponse("Cart with ID " + id + " has been deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Restore cart
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreCart(@PathVariable Long id) {
        try {
            Cart cart = cartService.restoreCart(id);
            CartDTO cartDTO = new CartDTO(cart);
            return ResponseEntity.ok(cartDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get all active carts
    @GetMapping
    public ResponseEntity<?> getAllActiveCarts() {
        try {
            List<Cart> carts = cartService.getAllActiveCarts();
            List<CartDTO> cartDTOs = carts.stream()
                    .map(CartDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cartDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get recent carts
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentCarts(@RequestParam(defaultValue = "30") int days) {
        try {
            List<Cart> carts = cartService.getRecentCarts(days);
            List<CartDTO> cartDTOs = carts.stream()
                    .map(CartDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cartDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get carts with more than N items
    @GetMapping("/with-more-than")
    public ResponseEntity<?> getCartsWithMoreThanNItems(@RequestParam int minItems) {
        try {
            List<Cart> carts = cartService.getCartsWithMoreThanNItems(minItems);
            List<CartDTO> cartDTOs = carts.stream()
                    .map(CartDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(cartDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get cart statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getCartStats() {
        try {
            long activeCount = cartService.getActiveCartCount();
            long emptyCount = cartService.getEmptyCartCount();
            long nonEmptyCount = cartService.getNonEmptyCartCount();
            Double averageItems = cartService.getAverageItemsPerCart();

            return ResponseEntity.ok(new CartStats(activeCount, emptyCount, nonEmptyCount, averageItems));
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

    public static class CartStats {
        private long activeCarts;
        private long emptyCarts;
        private long nonEmptyCarts;
        private Double averageItemsPerCart;

        public CartStats(long activeCarts, long emptyCarts, long nonEmptyCarts, Double averageItemsPerCart) {
            this.activeCarts = activeCarts;
            this.emptyCarts = emptyCarts;
            this.nonEmptyCarts = nonEmptyCarts;
            this.averageItemsPerCart = averageItemsPerCart;
        }

        public long getActiveCarts() { return activeCarts; }
        public long getEmptyCarts() { return emptyCarts; }
        public long getNonEmptyCarts() { return nonEmptyCarts; }
        public Double getAverageItemsPerCart() { return averageItemsPerCart; }
    }
}
