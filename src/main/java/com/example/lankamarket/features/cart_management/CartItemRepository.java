package com.example.lankamarket.features.cart_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Basic queries
    List<CartItem> findByIsDeletedFalse();
    List<CartItem> findByIsDeletedTrue();
    Optional<CartItem> findByIdAndIsDeletedFalse(Long id);
    
    // Cart-related queries
    List<CartItem> findByCartIdAndIsDeletedFalse(Long cartId);
    List<CartItem> findByCartIdAndIsDeletedFalseOrderByCreatedAtDesc(Long cartId);
    List<CartItem> findByCartIdAndIsDeletedFalseOrderByUpdatedAtDesc(Long cartId);
    
    // Product-related queries
    List<CartItem> findByProductIdAndIsDeletedFalse(Long productId);
    List<CartItem> findByProductIdAndIsDeletedFalseOrderByCreatedAtDesc(Long productId);
    Optional<CartItem> findByCartIdAndProductIdAndIsDeletedFalse(Long cartId, Long productId);
    
    // Quantity-related queries
    List<CartItem> findByQuantityGreaterThanAndIsDeletedFalse(Integer quantity);
    List<CartItem> findByQuantityLessThanAndIsDeletedFalse(Integer quantity);
    List<CartItem> findByQuantityBetweenAndIsDeletedFalse(Integer minQuantity, Integer maxQuantity);
    List<CartItem> findByQuantityAndIsDeletedFalse(Integer quantity);
    
    // Date-related queries
    List<CartItem> findByCreatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    List<CartItem> findByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    List<CartItem> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    List<CartItem> findByUpdatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    List<CartItem> findByUpdatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    List<CartItem> findByUpdatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    
    // Ordering queries
    List<CartItem> findByIsDeletedFalseOrderByCreatedAtDesc();
    List<CartItem> findByIsDeletedFalseOrderByCreatedAtAsc();
    List<CartItem> findByIsDeletedFalseOrderByUpdatedAtDesc();
    List<CartItem> findByIsDeletedFalseOrderByUpdatedAtAsc();
    List<CartItem> findByIsDeletedFalseOrderByQuantityDesc();
    List<CartItem> findByIsDeletedFalseOrderByQuantityAsc();
    
    // Statistics queries
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.isDeleted = false")
    long countActiveCartItems();
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.isDeleted = true")
    long countDeletedCartItems();
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.isDeleted = false")
    long countCartItemsByCart(@Param("cartId") Long cartId);
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.product.id = :productId AND ci.isDeleted = false")
    long countCartItemsByProduct(@Param("productId") Long productId);
    
    // Total quantity queries
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.isDeleted = false")
    Integer getTotalQuantityByCart(@Param("cartId") Long cartId);
    
    @Query("SELECT SUM(ci.quantity) FROM CartItem ci WHERE ci.product.id = :productId AND ci.isDeleted = false")
    Integer getTotalQuantityByProduct(@Param("productId") Long productId);
    
    // Existence checks
    @Query("SELECT CASE WHEN COUNT(ci) > 0 THEN true ELSE false END FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId AND ci.isDeleted = false")
    boolean existsByCartAndProduct(@Param("cartId") Long cartId, @Param("productId") Long productId);
    
    @Query("SELECT CASE WHEN COUNT(ci) > 0 THEN true ELSE false END FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.isDeleted = false")
    boolean existsByCartAndIsDeletedFalse(@Param("cartId") Long cartId);
    
    @Query("SELECT CASE WHEN COUNT(ci) > 0 THEN true ELSE false END FROM CartItem ci WHERE ci.product.id = :productId AND ci.isDeleted = false")
    boolean existsByProductAndIsDeletedFalse(@Param("productId") Long productId);
    
    // Find cart items by user
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.id = :userId AND ci.isDeleted = false ORDER BY ci.createdAt DESC")
    List<CartItem> findCartItemsByUser(@Param("userId") Long userId);
    
    // Find cart items by user and product
    @Query("SELECT ci FROM CartItem ci JOIN ci.cart c WHERE c.user.id = :userId AND ci.product.id = :productId AND ci.isDeleted = false")
    Optional<CartItem> findCartItemByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
    
    // Recent cart items
    @Query("SELECT ci FROM CartItem ci WHERE ci.createdAt >= :sinceDate AND ci.isDeleted = false ORDER BY ci.createdAt DESC")
    List<CartItem> getRecentCartItems(@Param("sinceDate") LocalDateTime sinceDate);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.updatedAt >= :sinceDate AND ci.isDeleted = false ORDER BY ci.updatedAt DESC")
    List<CartItem> getRecentlyUpdatedCartItems(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Find cart items with specific quantity ranges
    @Query("SELECT ci FROM CartItem ci WHERE ci.quantity >= :minQuantity AND ci.quantity <= :maxQuantity AND ci.isDeleted = false")
    List<CartItem> findCartItemsByQuantityRange(@Param("minQuantity") Integer minQuantity, @Param("maxQuantity") Integer maxQuantity);
    
    // Find cart items by cart and quantity
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.quantity = :quantity AND ci.isDeleted = false")
    List<CartItem> findCartItemsByCartAndQuantity(@Param("cartId") Long cartId, @Param("quantity") Integer quantity);
    
    // Find cart items by product and quantity
    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = :productId AND ci.quantity = :quantity AND ci.isDeleted = false")
    List<CartItem> findCartItemsByProductAndQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    
    // Delete cart items by cart
    @Modifying
    @Query("DELETE CartItem ci  WHERE ci.cart.id = :cartId")
    void softDeleteCartItemsByCart(@Param("cartId") Long cartId);
    
    // Delete cart items by product
    @Modifying
    @Query("UPDATE CartItem ci SET ci.isDeleted = true WHERE ci.product.id = :productId")
    void softDeleteCartItemsByProduct(@Param("productId") Long productId);
}
