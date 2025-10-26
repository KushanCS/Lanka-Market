package com.example.lankamarket.features.product_management;

import com.example.lankamarket.features.category_management.Category;
import com.example.lankamarket.features.seller_management.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find all products that are not deleted
    List<Product> findByIsDeletedFalse();
    
    // Find all products that are deleted
    List<Product> findByIsDeletedTrue();
    
    // Find products by category (not deleted)
    List<Product> findByCategoryAndIsDeletedFalse(Category category);
    
    // Find products by seller (not deleted)
    List<Product> findBySellerAndIsDeletedFalse(Seller seller);
    
    // Find products by category ID (not deleted)
    List<Product> findByCategoryIdAndIsDeletedFalse(Long categoryId);
    
    // Find products by seller ID (not deleted)
    List<Product> findBySellerIdAndIsDeletedFalse(Long sellerId);
    
    // Find products by name containing (not deleted)
    List<Product> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);
    
    // Find products by description containing (not deleted)
    List<Product> findByDescriptionContainingIgnoreCaseAndIsDeletedFalse(String description);
    
    // Find products by price range (not deleted)
    List<Product> findByPriceBetweenAndIsDeletedFalse(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find products with price greater than (not deleted)
    List<Product> findByPriceGreaterThanAndIsDeletedFalse(BigDecimal price);
    
    // Find products with price less than (not deleted)
    List<Product> findByPriceLessThanAndIsDeletedFalse(BigDecimal price);
    
    // Find products in stock (not deleted)
    List<Product> findByInStockAmountGreaterThanAndIsDeletedFalse(Integer amount);
    
    // Find products out of stock (not deleted)
    List<Product> findByInStockAmountAndIsDeletedFalse(Integer amount);
    
    // Find products with low stock (not deleted)
    List<Product> findByInStockAmountBetweenAndIsDeletedFalse(Integer minAmount, Integer maxAmount);
    
    // Find products that have image URL (not deleted)
    List<Product> findByImageUrlIsNotNullAndIsDeletedFalse();
    
    // Find products that don't have image URL (not deleted)
    List<Product> findByImageUrlIsNullAndIsDeletedFalse();
    
    // Count active products
    long countByIsDeletedFalse();
    
    // Count deleted products
    long countByIsDeletedTrue();
    
    // Count products by category (not deleted)
    long countByCategoryAndIsDeletedFalse(Category category);
    
    // Count products by seller (not deleted)
    long countBySellerAndIsDeletedFalse(Seller seller);
    
    // Count products in stock (not deleted)
    long countByInStockAmountGreaterThanAndIsDeletedFalse(Integer amount);
    
    // Count products out of stock (not deleted)
    long countByInStockAmountAndIsDeletedFalse(Integer amount);
    
    // Find products ordered by name (not deleted)
    List<Product> findByIsDeletedFalseOrderByNameAsc();
    
    // Find products ordered by price ascending (not deleted)
    List<Product> findByIsDeletedFalseOrderByPriceAsc();
    
    // Find products ordered by price descending (not deleted)
    List<Product> findByIsDeletedFalseOrderByPriceDesc();
    
    // Find products ordered by creation date (not deleted)
    List<Product> findByIsDeletedFalseOrderByCreatedAtDesc();
    
    // Find products ordered by update date (not deleted)
    List<Product> findByIsDeletedFalseOrderByUpdatedAtDesc();
    
    // Find products ordered by stock amount (not deleted)
    List<Product> findByIsDeletedFalseOrderByInStockAmountDesc();
    
    // Search products by multiple criteria
    @Query("SELECT p FROM Product p WHERE " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.isDeleted = false")
    List<Product> searchProductsByMultipleCriteria(@Param("searchTerm") String searchTerm);
    
    // Search products by category and name
    @Query("SELECT p FROM Product p WHERE " +
           "p.category = :category AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.isDeleted = false")
    List<Product> searchProductsByCategoryAndName(@Param("category") Category category, @Param("searchTerm") String searchTerm);
    
    // Search products by seller and name
    @Query("SELECT p FROM Product p WHERE " +
           "p.seller = :seller AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND p.isDeleted = false")
    List<Product> searchProductsBySellerAndName(@Param("seller") Seller seller, @Param("searchTerm") String searchTerm);
    
    // Find products by category and price range
    @Query("SELECT p FROM Product p WHERE " +
           "p.category = :category AND " +
           "p.price BETWEEN :minPrice AND :maxPrice " +
           "AND p.isDeleted = false")
    List<Product> findProductsByCategoryAndPriceRange(@Param("category") Category category, 
                                                   @Param("minPrice") BigDecimal minPrice, 
                                                   @Param("maxPrice") BigDecimal maxPrice);
    
    // Find products by seller and price range
    @Query("SELECT p FROM Product p WHERE " +
           "p.seller = :seller AND " +
           "p.price BETWEEN :minPrice AND :maxPrice " +
           "AND p.isDeleted = false")
    List<Product> findProductsBySellerAndPriceRange(@Param("seller") Seller seller, 
                                                  @Param("minPrice") BigDecimal minPrice, 
                                                  @Param("maxPrice") BigDecimal maxPrice);
}
