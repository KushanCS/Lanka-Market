
package com.lankamarket.categorydemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find category by name
    Optional<Category> findByName(String name);

    // Find category by name and not deleted
    Optional<Category> findByNameAndIsDeletedFalse(String name);

    // Find all categories that are not deleted
    List<Category> findByIsDeletedFalse();

    // Find all categories that are deleted
    List<Category> findByIsDeletedTrue();

    // Check if category name exists and category is not deleted
    boolean existsByNameAndIsDeletedFalse(String name);

    // Find categories by name containing (not deleted)
    List<Category> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);

    // Count active categories
    long countByIsDeletedFalse();

    // Count deleted categories
    long countByIsDeletedTrue();

    // Find categories ordered by name (not deleted)
    List<Category> findByIsDeletedFalseOrderByNameAsc();

    // Find categories ordered by creation date (not deleted)
    List<Category> findByIsDeletedFalseOrderByCreatedAtDesc();
}
