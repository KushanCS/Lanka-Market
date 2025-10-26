package repositories;

import dto.WishlistItemDTO;
import entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // Find wishlist items by user ID
    List<Wishlist> findByUser_Id(Long userId);

    // Check if product exists in user's wishlist
    Optional<Wishlist> findByUser_IdAndProduct_Id(Long userId, Long productId);

    // Delete wishlist item by user ID and product ID
    @Modifying
    @Transactional
    void deleteByUser_IdAndProduct_Id(Long userId, Long productId);

    // Count wishlist items for a user
    Long countByUser_Id(Long userId);

    // Custom query to get wishlist items with product details as DTOs
    @Query("SELECT new dto.WishlistItemDTO(w.id, w.product.id, w.product.name, " +
           "w.product.description, w.product.price, w.product.stockQuantity, w.product.imageUrl, w.addedDate) " +
           "FROM Wishlist w WHERE w.user.id = :userId ORDER BY w.addedDate DESC")
    List<WishlistItemDTO> findWishlistItemsByUserId(@Param("userId") Long userId);

}