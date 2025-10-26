package com.example.lankamarket.features.cart_management;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    
    private Long id;
    private String name;
    private String description;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private Long userId; // Only user ID, not the full user object
    private List<CartItemDTO> cartItems;
    
    // Calculated fields
    private int itemCount;
    private int totalQuantity;
    private BigDecimal totalAmount;
    private boolean isEmpty;
    
    // Default constructor
    public CartDTO() {
    }
    
    // Constructor from Cart entity
    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.name = cart.getName();
        this.description = cart.getDescription();
        this.createdAt = cart.getCreatedAt();
        this.updatedAt = cart.getUpdatedAt();
        this.userId = cart.getUser() != null ? cart.getUser().getId() : null;
        this.cartItems = cart.getCartItems() != null ? 
            cart.getCartItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList()) : null;
        this.itemCount = cart.getItemCount();
        this.totalQuantity = cart.getTotalQuantity();
        this.totalAmount = calculateTotalAmount();
        this.isEmpty = cart.isEmpty();
    }
    
    // Calculate total amount
    private BigDecimal calculateTotalAmount() {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    
    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
    
    public int getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    public int getTotalQuantity() {
        return totalQuantity;
    }
    
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
    
    @Override
    public String toString() {
        return "CartDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                ", itemCount=" + itemCount +
                ", totalQuantity=" + totalQuantity +
                ", totalAmount=" + totalAmount +
                ", isEmpty=" + isEmpty +
                '}';
    }
}

// CartItem DTO
class CartItemDTO {
    
    private Long id;
    private Integer quantity;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private Long cartId; // Only cart ID, not the full cart object
    private ProductDTO product; // Product details
    
    // Calculated fields
    private BigDecimal subtotal;
    
    // Default constructor
    public CartItemDTO() {
    }
    
    // Constructor from CartItem entity
    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.quantity = cartItem.getQuantity();
        this.createdAt = cartItem.getCreatedAt();
        this.updatedAt = cartItem.getUpdatedAt();
        this.cartId = cartItem.getCart() != null ? cartItem.getCart().getId() : null;
        this.product = cartItem.getProduct() != null ? new ProductDTO(cartItem.getProduct()) : null;
        this.subtotal = calculateSubtotal();
    }
    
    // Calculate subtotal
    private BigDecimal calculateSubtotal() {
        if (product != null && product.getPrice() != null && quantity != null) {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
    
    public Long getCartId() {
        return cartId;
    }
    
    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }
    
    public ProductDTO getProduct() {
        return product;
    }
    
    public void setProduct(ProductDTO product) {
        this.product = product;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", cartId=" + cartId +
                ", subtotal=" + subtotal +
                '}';
    }
}

// Product DTO for cart items
class ProductDTO {
    
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Integer inStockAmount;
    private Long categoryId;
    private Long sellerId;
    private String sellerName;
    
    // Default constructor
    public ProductDTO() {
    }
    
    // Constructor from Product entity
    public ProductDTO(com.example.lankamarket.features.product_management.Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.inStockAmount = product.getInStockAmount();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.sellerId = product.getSeller() != null ? product.getSeller().getId() : null;
        this.sellerName = product.getSeller() != null ? product.getSeller().getName() : null;
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
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Integer getInStockAmount() {
        return inStockAmount;
    }
    
    public void setInStockAmount(Integer inStockAmount) {
        this.inStockAmount = inStockAmount;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getSellerId() {
        return sellerId;
    }
    
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    
    public String getSellerName() {
        return sellerName;
    }
    
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    
    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", inStockAmount=" + inStockAmount +
                ", categoryId=" + categoryId +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}

// Create Cart Request DTO
class CreateCartRequestDTO {
    
    @NotBlank(message = "Cart name is required")
    @Size(max = 100, message = "Cart name must not exceed 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    // Default constructor
    public CreateCartRequestDTO() {
    }
    
    // Getters and Setters
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
}

// Update Cart Request DTO
class UpdateCartRequestDTO {
    
    @Size(max = 100, message = "Cart name must not exceed 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    // Default constructor
    public UpdateCartRequestDTO() {
    }
    
    // Getters and Setters
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
}

// Add Item to Cart Request DTO
class AddItemToCartRequestDTO {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    // Default constructor
    public AddItemToCartRequestDTO() {
    }
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

// Update Cart Item Quantity Request DTO
class UpdateCartItemQuantityRequestDTO {
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    // Default constructor
    public UpdateCartItemQuantityRequestDTO() {
    }
    
    // Getters and Setters
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
