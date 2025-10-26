package com.example.lankamarket.features.cart_management;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.product_management.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cart name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One-to-One relationship with User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @NotNull(message = "User is required")
    private User user;

    // One-to-Many relationship with CartItems
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Default constructor
    public Cart() {
    }

    // Constructor with required fields
    public Cart(String name, User user) {
        this.name = name;
        this.user = user;
        this.isDeleted = false;
    }

    // Constructor with all fields
    public Cart(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.isDeleted = false;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Helper methods
    public void addCartItem(CartItem cartItem) {
        if (cartItem != null) {
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }
    }

    public void removeCartItem(CartItem cartItem) {
        if (cartItem != null) {
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void removeCartItemByProductId(Long productId) {
        cartItems.removeIf(item -> item.getProduct() != null && item.getProduct().getId().equals(productId));
    }

    public CartItem findCartItemByProductId(Long productId) {
        return cartItems.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public int getTotalQuantity() {
        return cartItems != null ? cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum() : 0;
    }

    public boolean isEmpty() {
        return cartItems == null || cartItems.isEmpty();
    }

    public void clearCart() {
        if (cartItems != null) {
            cartItems.clear();
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + (user != null ? user.getId() : null) +
                ", itemCount=" + getItemCount() +
                '}';
    }
}
