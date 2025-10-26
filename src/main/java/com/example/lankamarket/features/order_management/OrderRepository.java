package com.example.lankamarket.features.order_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Basic queries
    List<Order> findByIsDeletedFalse();
    List<Order> findByIsDeletedTrue();
    Optional<Order> findByIdAndIsDeletedFalse(Long id);
    
    // User-related queries
    List<Order> findByUserIdAndIsDeletedFalse(Long userId);
    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    List<Order> findByUserIdAndStatusAndIsDeletedFalse(Long userId, String status);
    
    // Status-related queries
    List<Order> findByStatusAndIsDeletedFalse(String status);
    List<Order> findByStatusAndIsDeletedFalseOrderByCreatedAtDesc(String status);
    List<Order> findByStatusInAndIsDeletedFalse(List<String> statuses);
    
    // Amount-related queries
    List<Order> findByTotalAmountGreaterThanAndIsDeletedFalse(BigDecimal amount);
    List<Order> findByTotalAmountLessThanAndIsDeletedFalse(BigDecimal amount);
    List<Order> findByTotalAmountBetweenAndIsDeletedFalse(BigDecimal minAmount, BigDecimal maxAmount);
    
    // Date-related queries
    List<Order> findByCreatedAtAfterAndIsDeletedFalse(LocalDateTime date);
    List<Order> findByCreatedAtBeforeAndIsDeletedFalse(LocalDateTime date);
    List<Order> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByPaymentDateAfterAndIsDeletedFalse(LocalDateTime date);
    List<Order> findByDeliveryDateAfterAndIsDeletedFalse(LocalDateTime date);
    
    // Contact-related queries
    List<Order> findByContactNumberAndIsDeletedFalse(String contactNumber);
    List<Order> findByDeliveryAddressContainingIgnoreCaseAndIsDeletedFalse(String address);
    
    // Product-related queries
    @Query("SELECT o FROM Order o JOIN o.products p WHERE p.id = :productId AND o.isDeleted = false")
    List<Order> findOrdersContainingProduct(@Param("productId") Long productId);
    
    @Query("SELECT o FROM Order o WHERE SIZE(o.products) = :productCount AND o.isDeleted = false")
    List<Order> findOrdersWithProductCount(@Param("productCount") int productCount);
    
    @Query("SELECT o FROM Order o WHERE SIZE(o.products) > :minCount AND o.isDeleted = false")
    List<Order> findOrdersWithMoreThanNProducts(@Param("minCount") int minCount);
    
    // Ordering queries
    List<Order> findByIsDeletedFalseOrderByCreatedAtDesc();
    List<Order> findByIsDeletedFalseOrderByCreatedAtAsc();
    List<Order> findByIsDeletedFalseOrderByTotalAmountDesc();
    List<Order> findByIsDeletedFalseOrderByTotalAmountAsc();
    List<Order> findByIsDeletedFalseOrderByUpdatedAtDesc();
    
    // Status-specific ordering
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.isDeleted = false ORDER BY o.createdAt DESC")
    List<Order> findOrdersByStatusOrderedByCreatedDateDesc(@Param("status") String status);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.isDeleted = false ORDER BY o.totalAmount DESC")
    List<Order> findOrdersByStatusOrderedByAmountDesc(@Param("status") String status);
    
    // Recent orders
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :sinceDate AND o.isDeleted = false ORDER BY o.createdAt DESC")
    List<Order> getRecentOrders(@Param("sinceDate") LocalDateTime sinceDate);
    
    // Statistics queries
    @Query("SELECT COUNT(o) FROM Order o WHERE o.isDeleted = false")
    long countActiveOrders();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.isDeleted = true")
    long countDeletedOrders();
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.isDeleted = false")
    long countOrdersByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId AND o.isDeleted = false")
    long countOrdersByUser(@Param("userId") Long userId);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.isDeleted = false")
    BigDecimal getTotalRevenue();
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = :status AND o.isDeleted = false")
    BigDecimal getRevenueByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.user.id = :userId AND o.isDeleted = false")
    BigDecimal getTotalSpentByUser(@Param("userId") Long userId);
    
    // Popular products in orders
    @Query("SELECT p, COUNT(o) as orderCount FROM Order o JOIN o.products p WHERE o.isDeleted = false GROUP BY p ORDER BY orderCount DESC")
    List<Object[]> getMostPopularProductsInOrders();
    
    // Order statistics by user
    @Query("SELECT o.user.id, COUNT(o) as orderCount, SUM(o.totalAmount) as totalSpent FROM Order o WHERE o.isDeleted = false GROUP BY o.user.id")
    List<Object[]> getOrderStatisticsByUser();
    
    // Average order value
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.isDeleted = false")
    BigDecimal getAverageOrderValue();
    
    // Orders by payment method
    List<Order> findByPaymentMethodAndIsDeletedFalse(String paymentMethod);
    
    // Orders with notes
    List<Order> findByNotesIsNotNullAndIsDeletedFalse();
    List<Order> findByNotesContainingIgnoreCaseAndIsDeletedFalse(String notes);
    
    // Existence checks
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.user.id = :userId AND o.isDeleted = false")
    boolean existsByUserIdAndIsDeletedFalse(@Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.status = :status AND o.isDeleted = false")
    boolean existsByStatusAndIsDeletedFalse(@Param("status") String status);
}
