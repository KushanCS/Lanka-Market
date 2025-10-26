package com.example.lankamarket.features.order_management;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.product_management.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order status is required")
    @Pattern(regexp = "^(PENDING|CONFIRMED|PROCESSING|SHIPPED|DELIVERED|CANCELLED|REFUNDED)$", 
             message = "Order status must be one of: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED")
    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @NotBlank(message = "Delivery address is required")
    @Size(max = 500, message = "Delivery address must not exceed 500 characters")
    @Column(name = "delivery_address", nullable = false, length = 500)
    private String deliveryAddress;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,15}$", message = "Contact number must be between 10-15 characters and contain only numbers, +, -, spaces, and parentheses")
    @Column(name = "contact_number", nullable = false)
    private String contactNumber;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Many-to-One relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    // Many-to-Many relationship with Products
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    // Default constructor
    public Order() {
    }

    // Constructor with required fields
    public Order(String status, String deliveryAddress, String contactNumber, 
                BigDecimal totalAmount, User user) {
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.contactNumber = contactNumber;
        this.totalAmount = totalAmount;
        this.user = user;
        this.isDeleted = false;
    }

    // Constructor with all fields
    public Order(String status, String deliveryAddress, String contactNumber, 
                BigDecimal totalAmount, String paymentMethod, String notes, 
                LocalDateTime paymentDate, LocalDateTime deliveryDate, User user) {
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.contactNumber = contactNumber;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.paymentDate = paymentDate;
        this.deliveryDate = deliveryDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Helper methods
    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }

    public void addProduct(Product product) {
        if (product != null && !products.contains(product)) {
            products.add(product);
        }
    }

    public void removeProduct(Product product) {
        if (product != null) {
            products.remove(product);
        }
    }

    public boolean containsProduct(Product product) {
        return product != null && products.contains(product);
    }

    public boolean containsProductById(Long productId) {
        return productId != null && products.stream()
                .anyMatch(product -> product.getId().equals(productId));
    }

    public int getProductCount() {
        return products != null ? products.size() : 0;
    }

    public boolean isEmpty() {
        return products == null || products.isEmpty();
    }

    public void calculateTotalAmount() {
        if (products == null || products.isEmpty()) {
            this.totalAmount = BigDecimal.ZERO;
            return;
        }

        BigDecimal total = products.stream()
                .filter(product -> product.getPrice() != null)
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = total;
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isConfirmed() {
        return "CONFIRMED".equals(status);
    }

    public boolean isProcessing() {
        return "PROCESSING".equals(status);
    }

    public boolean isShipped() {
        return "SHIPPED".equals(status);
    }

    public boolean isDelivered() {
        return "DELIVERED".equals(status);
    }

    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }

    public boolean isRefunded() {
        return "REFUNDED".equals(status);
    }

    public boolean canBeCancelled() {
        return isPending() || isConfirmed();
    }

    public boolean canBeRefunded() {
        return isDelivered() || isCancelled();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", notes='" + notes + '\'' +
                ", paymentDate=" + paymentDate +
                ", deliveryDate=" + deliveryDate +
                ", isDeleted=" + isDeleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userId=" + (user != null ? user.getId() : null) +
                ", productCount=" + getProductCount() +
                '}';
    }
}
