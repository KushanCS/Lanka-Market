package com.example.lankamarket.features.cart_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Basic queries
    List<Cart> findByIsDeletedFalse();
    List<Cart> findByIsDeletedTrue();
    Optional<Cart> findByIdAndIsDeletedFalse(Long id);
    
    // User-related queries
    Optional<Cart> findByUserIdAndIsDeletedFalse(Long userId);
    List<Cart> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndIsDeletedFalse(Long userId);
    
    // Name-related queries
    List<Cart> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);
    List<Cart> findByNameAndIsDeletedFalse(String name);
    
    // Date-related queries
    List<Cart> findByCreatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    List<Cart> findByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    List<Cart> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    List<Cart> findByUpdatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    List<Cart> findByUpdatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    List<Cart> findByUpdatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    
    // Ordering queries
    List<Cart> findByIsDeletedFalseOrderByCreatedAtDesc();
    List<Cart> findByIsDeletedFalseOrderByCreatedAtAsc();
    List<Cart> findByIsDeletedFalseOrderByUpdatedAtDesc();
    List<Cart> findByIsDeletedFalseOrderByUpdatedAtAsc();
    List<Cart> findByIsDeletedFalseOrderByNameAsc();
    List<Cart> findByIsDeletedFalseOrderByNameDesc();
    
    // Cart items related queries
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.product.id = :productId AND c.isDeleted = false")
    List<Cart> findCartsContainingProduct(@Param("productId") Long productId);
    
    @Query("SELECT c FROM Cart c WHERE SIZE(c.cartItems) > :minItems AND c.isDeleted = false")
    List<Cart> findCartsWithMoreThanNItems(@Param("minItems") int minItems);
    
    @Query("SELECT c FROM Cart c WHERE SIZE(c.cartItems) = :exactItems AND c.isDeleted = false")
    List<Cart> findCartsWithExactNItems(@Param("exactItems") int exactItems);
    
    @Query("SELECT c FROM Cart c WHERE SIZE(c.cartItems) = 0 AND c.isDeleted = false")
    List<Cart> findEmptyCarts();
    
    @Query("SELECT c FROM Cart c WHERE SIZE(c.cartItems) > 0 AND c.isDeleted = false")
    List<Cart> findNonEmptyCarts();
    
    // Cart items ordered by count
    @Query("SELECT c FROM Cart c WHERE c.isDeleted = false ORDER BY SIZE(c.cartItems) DESC")
    List<Cart> findCartsOrderedByItemCountDesc();
    
    @Query("SELECT c FROM Cart c WHERE c.isDeleted = false ORDER BY SIZE(c.cartItems) ASC")
    List<Cart> findCartsOrderedByItemCountAsc();
    
    // Statistics queries
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.isDeleted = false")
    long countActiveCarts();
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.isDeleted = true")
    long countDeletedCarts();
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.user.id = :userId AND c.isDeleted = false")
    long countCartsByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE SIZE(c.cartItems) = 0 AND c.isDeleted = false")
    long countEmptyCarts();
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE SIZE(c.cartItems) > 0 AND c.isDeleted = false")
    long countNonEmptyCarts();
    
    // Average items per cart
    @Query("SELECT AVG(SIZE(c.cartItems)) FROM Cart c WHERE c.isDeleted = false")
    Double getAverageItemsPerCart();
    
    // Cart statistics by user
    @Query("SELECT c.user.id, COUNT(c) as cartCount, AVG(SIZE(c.cartItems)) as avgItems FROM Cart c WHERE c.isDeleted = false GROUP BY c.user.id")
    List<Object[]> getCartStatisticsByUser();
    
    // Recent carts
    @Query("SELECT c FROM Cart c WHERE c.createdAt >= :sinceDate AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Cart> getRecentCarts(@Param("sinceDate") LocalDateTime sinceDate);
    
    @Query("SELECT c FROM Cart c WHERE c.updatedAt >= :sinceDate AND c.isDeleted = false ORDER BY c.updatedAt DESC")
    List<Cart> getRecentlyUpdatedCarts(@Param("sinceDate") LocalDateTime sinceDate);
    

    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cart c WHERE c.name = :name AND c.isDeleted = false")
    boolean existsByNameAndIsDeletedFalse(@Param("name") String name);
    
    // Find carts by specific criteria
    @Query("SELECT c FROM Cart c WHERE c.name LIKE %:name% AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Cart> findCartsByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM Cart c WHERE c.description LIKE %:description% AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Cart> findCartsByDescriptionContaining(@Param("description") String description);
    
    // Find carts with specific products
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.product.id IN :productIds AND c.isDeleted = false")
    List<Cart> findCartsContainingAnyOfProducts(@Param("productIds") List<Long> productIds);
    
    // Find carts by user and product
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE c.user.id = :userId AND ci.product.id = :productId AND c.isDeleted = false")
    Optional<Cart> findCartByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
    
    // Find carts with items added after a specific date
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.createdAt >= :sinceDate AND c.isDeleted = false ORDER BY ci.createdAt DESC")
    List<Cart> findCartsWithItemsAddedAfter(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Find carts with items updated after a specific date
    @Query("SELECT c FROM Cart c JOIN c.cartItems ci WHERE ci.updatedAt >= :sinceDate AND c.isDeleted = false ORDER BY ci.updatedAt DESC")
    List<Cart> findCartsWithItemsUpdatedAfter(@Param("sinceDate") LocalDateTime sinceDate);
}
