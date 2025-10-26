package com.example.lankamarket.features.order_management;

import com.example.lankamarket.features.product_management.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    
    private Long id;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    private Long userId; // Only user ID, not the full user object
    private List<ProductDTO> products;
    
    // Order details
    private String deliveryAddress;
    private String contactNumber;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryDate;
    
    // Default constructor
    public OrderDTO() {
    }
    
    // Constructor from Order entity
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.userId = order.getUser() != null ? order.getUser().getId() : null;
        this.products = order.getProducts() != null ? 
            order.getProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList()) : null;
        this.deliveryAddress = order.getDeliveryAddress();
        this.contactNumber = order.getContactNumber();
        this.totalAmount = order.getTotalAmount();
        this.paymentMethod = order.getPaymentMethod();
        this.notes = order.getNotes();
        this.paymentDate = order.getPaymentDate();
        this.deliveryDate = order.getDeliveryDate();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public List<ProductDTO> getProducts() {
        return products;
    }
    
    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    // Helper methods
    public int getProductCount() {
        return products != null ? products.size() : 0;
    }
    
    public boolean isEmpty() {
        return products == null || products.isEmpty();
    }
    
    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + userId +
                ", productCount=" + getProductCount() +
                ", totalAmount=" + totalAmount +
                '}';
    }
    
    // Inner ProductDTO class to avoid circular references
    public static class ProductDTO {
        private Long id;
        private String name;
        private BigDecimal price;
        private String description;
        private String imageUrl;
        private Integer inStockAmount;
        private Boolean isDeleted;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;
        
        private Long categoryId; // Only category ID, not the full category object
        private Long sellerId; // Only seller ID, not the full seller object
        private String sellerName; // Seller name for display
        
        // Default constructor
        public ProductDTO() {
        }
        
        // Constructor from Product entity
        public ProductDTO(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.description = product.getDescription();
            this.imageUrl = product.getImageUrl();
            this.inStockAmount = product.getInStockAmount();
            this.isDeleted = product.getIsDeleted();
            this.createdAt = product.getCreatedAt();
            this.updatedAt = product.getUpdatedAt();
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
                    ", isDeleted=" + isDeleted +
                    ", categoryId=" + categoryId +
                    ", sellerId=" + sellerId +
                    ", sellerName='" + sellerName + '\'' +
                    '}';
        }
    }
}

// Create Order Request DTO
class CreateOrderRequestDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotBlank(message = "Delivery address is required")
    @Size(max = 500, message = "Delivery address must not exceed 500 characters")
    private String deliveryAddress;
    
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,15}$", message = "Contact number must be between 10-15 characters")
    private String contactNumber;
    
    @NotNull(message = "Product IDs are required")
    private List<Long> productIds;
    
    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    private String paymentMethod;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    // Default constructor
    public CreateOrderRequestDTO() {
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public List<Long> getProductIds() {
        return productIds;
    }
    
    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}

// Update Order Request DTO
class UpdateOrderRequestDTO {
    
    @Size(max = 500, message = "Delivery address must not exceed 500 characters")
    private String deliveryAddress;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,15}$", message = "Contact number must be between 10-15 characters")
    private String contactNumber;
    
    private List<Long> productIds;
    
    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    private String paymentMethod;
    
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    // Default constructor
    public UpdateOrderRequestDTO() {
    }
    
    // Getters and Setters
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public List<Long> getProductIds() {
        return productIds;
    }
    
    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}

// Update Order Status Request DTO
class UpdateOrderStatusRequestDTO {
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(PENDING|CONFIRMED|PROCESSING|SHIPPED|DELIVERED|CANCELLED|REFUNDED)$", 
             message = "Status must be one of: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED")
    private String status;
    
    // Default constructor
    public UpdateOrderStatusRequestDTO() {
    }
    
    // Constructor
    public UpdateOrderStatusRequestDTO(String status) {
        this.status = status;
    }
    
    // Getters and Setters
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
