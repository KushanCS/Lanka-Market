package com.example.lankamarket.features.category_management;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * CategoryController with comprehensive try-catch error handling
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    // Create a new category
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setIsDeleted(category.getIsDeleted());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }



    // Create category by name
    @PostMapping("/create")
    public ResponseEntity<?> createCategoryByName(@RequestParam String name) {
        try {
            Category createdCategory = categoryService.createCategory(name);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            Optional<Category> category = categoryService.getCategoryById(id);
            if (category.isPresent()) {
                return ResponseEntity.ok(category.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get category by name
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getCategoryByName(@PathVariable String name) {
        try {
            Optional<Category> category = categoryService.getCategoryByName(name);
            if (category.isPresent()) {
                return ResponseEntity.ok(category.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get all active categories
    @GetMapping
    public ResponseEntity<?> getAllActiveCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllActiveCategories()
                    .stream()
                    .map(this::toDTO)
                    .toList());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get all deleted categories
    @GetMapping("/deleted")
    public ResponseEntity<?> getAllDeletedCategories() {
        try {
            List<Category> categories = categoryService.getAllDeletedCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get categories ordered by name
    @GetMapping("/ordered/name")
    public ResponseEntity<?> getCategoriesOrderedByName() {
        try {
            List<Category> categories = categoryService.getCategoriesOrderedByName();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get categories ordered by creation date
    @GetMapping("/ordered/created")
    public ResponseEntity<?> getCategoriesOrderedByCreatedDate() {
        try {
            List<Category> categories = categoryService.getCategoriesOrderedByCreatedDate();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Soft delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new SuccessResponse("Category with ID " + id + " has been deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Permanent delete category
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<?> permanentDeleteCategory(@PathVariable Long id) {
        try {
            categoryService.permanentDeleteCategory(id);
            return ResponseEntity.ok(new SuccessResponse("Category with ID " + id + " has been permanently deleted"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Restore deleted category
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreCategory(@PathVariable Long id) {
        try {
            Category restoredCategory = categoryService.restoreCategory(id);
            return ResponseEntity.ok(restoredCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Search categories by name
    @GetMapping("/search")
    public ResponseEntity<?> searchCategoriesByName(@RequestParam String name) {
        try {
            List<Category> categories = categoryService.searchCategoriesByName(name);
            return ResponseEntity.ok(categories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Get category statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getCategoryStats() {
        try {
            long activeCount = categoryService.getActiveCategoryCount();
            long deletedCount = categoryService.getDeletedCategoryCount();
            
            return ResponseEntity.ok(new CategoryStats(activeCount, deletedCount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Check if category exists by name
    @GetMapping("/exists")
    public ResponseEntity<?> checkCategoryExists(@RequestParam String name) {
        try {
            boolean exists = categoryService.categoryExistsByName(name);
            return ResponseEntity.ok(new CategoryExistsResponse(exists));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }
    
    // Response classes
    public static class ErrorResponse {
        private String error;
        private String message;
        
        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }
        
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
    
    public static class SuccessResponse {
        private String message;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
    }
    
    public static class CategoryStats {
        private long activeCategories;
        private long deletedCategories;
        
        public CategoryStats(long activeCategories, long deletedCategories) {
            this.activeCategories = activeCategories;
            this.deletedCategories = deletedCategories;
        }
        
        public long getActiveCategories() { return activeCategories; }
        public long getDeletedCategories() { return deletedCategories; }
    }
    
    public static class CategoryExistsResponse {
        private boolean exists;
        
        public CategoryExistsResponse(boolean exists) {
            this.exists = exists;
        }
        
        public boolean isExists() { return exists; }
    }
}
