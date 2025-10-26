package dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItemDTO {
    private Long wishlistId;
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Integer stockQuantity;
    private String productImageUrl;
    private LocalDateTime addedDate;

    // Constructors
    public WishlistItemDTO() {}

    public WishlistItemDTO(Long wishlistId, Long productId, String productName,
                           String productDescription, BigDecimal productPrice,
                           Integer stockQuantity, String productImageUrl, LocalDateTime addedDate) {
        this.wishlistId = wishlistId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.stockQuantity = stockQuantity;
        this.productImageUrl = productImageUrl;
        this.addedDate = addedDate;
    }

    // Getters and Setters
    public Long getWishlistId() { return wishlistId; }
    public void setWishlistId(Long wishlistId) { this.wishlistId = wishlistId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
}