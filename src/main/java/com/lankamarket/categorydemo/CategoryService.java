package com.example.lankamarket.features.category_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * CategoryService with comprehensive try-catch error handling
 */
@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    // Create a new category with try-catch
    public Category createCategory(Category category) {
        try {
            // Validate category name
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be null or empty");
            }
            
            // Check if category name already exists
            if (categoryRepository.existsByNameAndIsDeletedFalse(category.getName())) {
                throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
            }
            
            // Ensure isDeleted is set to false
            category.setIsDeleted(false);
            
            return categoryRepository.save(category);
            
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category: " + e.getMessage(), e);
        }
    }
    
    // Create category by name with try-catch
    public Category createCategory(String name) {
        try {
            Category category = new Category(name);
            return createCategory(category);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create category with name '" + name + "': " + e.getMessage(), e);
        }
    }
    
    // Get category by ID with try-catch
    public Optional<Category> getCategoryById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            return categoryRepository.findById(id)
                    .filter(category -> !category.getIsDeleted());
                    
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get category by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get category by ID (throws exception if not found)
    public Category getCategoryByIdOrThrow(Long id) {
        try {
            Optional<Category> category = getCategoryById(id);
            if (category.isPresent()) {
                return category.get();
            } else {
                throw new RuntimeException("Category not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get category by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get category by name with try-catch
    public Optional<Category> getCategoryByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be null or empty");
            }
            
            return categoryRepository.findByNameAndIsDeletedFalse(name);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get category by name '" + name + "': " + e.getMessage(), e);
        }
    }
    
    // Get all active categories with try-catch
    public List<Category> getAllActiveCategories() {
        try {
            return categoryRepository.findByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all active categories: " + e.getMessage(), e);
        }
    }
    
    // Get all deleted categories with try-catch
    public List<Category> getAllDeletedCategories() {
        try {
            return categoryRepository.findByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all deleted categories: " + e.getMessage(), e);
        }
    }
    
    // Get categories ordered by name with try-catch
    public List<Category> getCategoriesOrderedByName() {
        try {
            return categoryRepository.findByIsDeletedFalseOrderByNameAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get categories ordered by name: " + e.getMessage(), e);
        }
    }
    
    // Get categories ordered by creation date with try-catch
    public List<Category> getCategoriesOrderedByCreatedDate() {
        try {
            return categoryRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get categories ordered by creation date: " + e.getMessage(), e);
        }
    }
    
    // Update category with try-catch
    public Category updateCategory(Long id, Category updatedCategory) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            if (updatedCategory.getName() == null || updatedCategory.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be null or empty");
            }
            
            // Find existing category
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
            
            // Check if category is deleted
            if (existingCategory.getIsDeleted()) {
                throw new RuntimeException("Cannot update deleted category with ID: " + id);
            }
            
            // Check if new name already exists (excluding current category)
            if (!existingCategory.getName().equals(updatedCategory.getName()) && 
                categoryRepository.existsByNameAndIsDeletedFalse(updatedCategory.getName())) {
                throw new IllegalArgumentException("Category with name '" + updatedCategory.getName() + "' already exists");
            }
            
            // Update fields
            existingCategory.setName(updatedCategory.getName());
            
            return categoryRepository.save(existingCategory);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Soft delete category with try-catch
    public void deleteCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
            
            if (category.getIsDeleted()) {
                throw new RuntimeException("Category with ID " + id + " is already deleted");
            }
            
            category.softDelete();
            categoryRepository.save(category);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Hard delete category with try-catch
    public void permanentDeleteCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
            
            categoryRepository.delete(category);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to permanently delete category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Restore deleted category with try-catch
    public Category restoreCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));
            
            if (!category.getIsDeleted()) {
                throw new RuntimeException("Category with ID " + id + " is not deleted");
            }
            
            category.restore();
            return categoryRepository.save(category);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Search categories by name with try-catch
    public List<Category> searchCategoriesByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Search name cannot be null or empty");
            }
            
            return categoryRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search categories by name '" + name + "': " + e.getMessage(), e);
        }
    }
    
    // Get category count statistics with try-catch
    public long getActiveCategoryCount() {
        try {
            return categoryRepository.countByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active category count: " + e.getMessage(), e);
        }
    }
    
    public long getDeletedCategoryCount() {
        try {
            return categoryRepository.countByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get deleted category count: " + e.getMessage(), e);
        }
    }
    
    // Check if category exists by name with try-catch
    public boolean categoryExistsByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be null or empty");
            }
            
            return categoryRepository.existsByNameAndIsDeletedFalse(name);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if category exists by name '" + name + "': " + e.getMessage(), e);
        }
    }
}
