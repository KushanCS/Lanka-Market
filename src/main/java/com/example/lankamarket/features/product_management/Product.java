package com.example.lankamarket.features.product_management;

import com.example.lankamarket.features.category_management.Category;
import com.example.lankamarket.features.seller_management.Seller;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$", 
             message = "Image URL must be a valid URL format")
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @NotNull(message = "In stock amount is required")
    @Min(value = 0, message = "In stock amount must be 0 or greater")
    @Column(name = "in_stock_amount", nullable = false)
    private Integer inStockAmount;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Many-to-One relationship with Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private Category category;
    
    // Many-to-One relationship with Seller
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @NotNull(message = "Seller is required")
    private Seller seller;


    // Default constructor
    public Product() {
    }
    
    // Constructor with required fields
    public Product(String name, BigDecimal price, String description, Integer inStockAmount, Category category, Seller seller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.inStockAmount = inStockAmount;
        this.category = category;
        this.seller = seller;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public Product(String name, BigDecimal price, String description, String imageUrl, Integer inStockAmount, Category category, Seller seller) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.inStockAmount = inStockAmount;
        this.category = category;
        this.seller = seller;
        this.isDeleted = false;
    }
    
    // JPA lifecycle callbacks
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
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Seller getSeller() {
        return seller;
    }
    
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    

    

    // Helper method for soft delete
    public void softDelete() {
        this.isDeleted = true;
    }
    
    // Helper method for restore
    public void restore() {
        this.isDeleted = false;
    }
    
    // Helper method to check if product has image
    public boolean hasImage() {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }
    
    // Helper method to check if product is in stock
    public boolean isInStock() {
        return inStockAmount != null && inStockAmount > 0;
    }
    
    // Helper method to get stock status
    public String getStockStatus() {
        if (inStockAmount == null || inStockAmount == 0) {
            return "Out of Stock";
        } else if (inStockAmount <= 5) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
    
    // Helper method to get display name with price
    public String getDisplayNameWithPrice() {
        return name + " - $" + price;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", inStockAmount=" + inStockAmount +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryId=" + (category != null ? category.getId() : null) +
                ", sellerId=" + (seller != null ? seller.getId() : null) +
                '}';
    }
}
