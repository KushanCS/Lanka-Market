package com.example.lankamarket.features.product_management;

import com.example.lankamarket.features.category_management.Category;
import com.example.lankamarket.features.category_management.CategoryRepository;
import com.example.lankamarket.features.seller_management.Seller;
import com.example.lankamarket.features.seller_management.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProductService with comprehensive business logic and error handling
 */
@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SellerRepository sellerRepository;
    
    // Create a new product
    public Product createProduct(Product product) {
        try {
            // Validate product data
            validateProductForCreation(product);
            
            // Ensure isDeleted is set to false
            product.setIsDeleted(false);
            
            return productRepository.save(product);
            
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw validation exceptions
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product: " + e.getMessage(), e);
        }
    }
    
    // Create product with individual parameters
    public Product createProduct(String name, BigDecimal price, String description, String imageUrl, 
                                Integer inStockAmount, Long categoryId, Long sellerId) {
        try {
            // Find category and seller
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
            
            Seller seller = sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new IllegalArgumentException("Seller not found with ID: " + sellerId));
            
            Product product = new Product(name, price, description, imageUrl, inStockAmount, category, seller);
            return createProduct(product);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product with name '" + name + "': " + e.getMessage(), e);
        }
    }
    
    // Get product by ID (only if not deleted)
    public Optional<Product> getProductById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            return productRepository.findById(id)
                    .filter(product -> !product.getIsDeleted());
                    
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get product by ID (throws exception if not found)
    public Product getProductByIdOrThrow(Long id) {
        try {
            Optional<Product> product = getProductById(id);
            if (product.isPresent()) {
                return product.get();
            } else {
                throw new RuntimeException("Product not found with ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product by ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get all active products
    public List<Product> getAllActiveProducts() {
        try {
            return productRepository.findByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all active products: " + e.getMessage(), e);
        }
    }
    
    // Get all deleted products
    public List<Product> getAllDeletedProducts() {
        try {
            return productRepository.findByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all deleted products: " + e.getMessage(), e);
        }
    }
    
    // Get products by category
    public List<Product> getProductsByCategory(Long categoryId) {
        try {
            if (categoryId == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            return productRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products by category ID " + categoryId + ": " + e.getMessage(), e);
        }
    }
    
    // Get products by seller
    public List<Product> getProductsBySeller(Long sellerId) {
        try {
            if (sellerId == null) {
                throw new IllegalArgumentException("Seller ID cannot be null");
            }
            
            return productRepository.findBySellerIdAndIsDeletedFalse(sellerId);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products by seller ID " + sellerId + ": " + e.getMessage(), e);
        }
    }
    
    // Get products ordered by name
    public List<Product> getProductsOrderedByName() {
        try {
            return productRepository.findByIsDeletedFalseOrderByNameAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products ordered by name: " + e.getMessage(), e);
        }
    }
    
    // Get products ordered by price ascending
    public List<Product> getProductsOrderedByPriceAsc() {
        try {
            return productRepository.findByIsDeletedFalseOrderByPriceAsc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products ordered by price ascending: " + e.getMessage(), e);
        }
    }
    
    // Get products ordered by price descending
    public List<Product> getProductsOrderedByPriceDesc() {
        try {
            return productRepository.findByIsDeletedFalseOrderByPriceDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products ordered by price descending: " + e.getMessage(), e);
        }
    }
    
    // Get products ordered by creation date
    public List<Product> getProductsOrderedByCreatedDate() {
        try {
            return productRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products ordered by creation date: " + e.getMessage(), e);
        }
    }
    
    // Get products ordered by stock amount
    public List<Product> getProductsOrderedByStockAmount() {
        try {
            return productRepository.findByIsDeletedFalseOrderByInStockAmountDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products ordered by stock amount: " + e.getMessage(), e);
        }
    }
    
    // Update product
    public Product updateProduct(Long id, Product updatedProduct) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            // Validate updated product data
            validateProductForUpdate(updatedProduct, id);
            
            // Find existing product
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
            
            // Check if product is deleted
            if (existingProduct.getIsDeleted()) {
                throw new RuntimeException("Cannot update deleted product with ID: " + id);
            }
            
            // Update fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            existingProduct.setInStockAmount(updatedProduct.getInStockAmount());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setSeller(updatedProduct.getSeller());
            
            return productRepository.save(existingProduct);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Soft delete product
    public void deleteProduct(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
            
            if (product.getIsDeleted()) {
                throw new RuntimeException("Product with ID " + id + " is already deleted");
            }
            
            product.softDelete();
            productRepository.save(product);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Hard delete product (permanent deletion)
    public void permanentDeleteProduct(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
            
            productRepository.delete(product);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to permanently delete product with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Restore deleted product
    public Product restoreProduct(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
            
            if (!product.getIsDeleted()) {
                throw new RuntimeException("Product with ID " + id + " is not deleted");
            }
            
            product.restore();
            return productRepository.save(product);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore product with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Search products by name
    public List<Product> searchProductsByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Search name cannot be null or empty");
            }
            
            return productRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search products by name '" + name + "': " + e.getMessage(), e);
        }
    }
    
    // Search products by description
    public List<Product> searchProductsByDescription(String description) {
        try {
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Search description cannot be null or empty");
            }
            
            return productRepository.findByDescriptionContainingIgnoreCaseAndIsDeletedFalse(description);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search products by description '" + description + "': " + e.getMessage(), e);
        }
    }
    
    // Search products by multiple criteria
    public List<Product> searchProductsByMultipleCriteria(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new IllegalArgumentException("Search term cannot be null or empty");
            }
            
            return productRepository.searchProductsByMultipleCriteria(searchTerm);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search products by multiple criteria '" + searchTerm + "': " + e.getMessage(), e);
        }
    }
    
    // Get products by price range
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            if (minPrice == null || maxPrice == null) {
                throw new IllegalArgumentException("Min price and max price cannot be null");
            }
            
            if (minPrice.compareTo(maxPrice) > 0) {
                throw new IllegalArgumentException("Min price cannot be greater than max price");
            }
            
            return productRepository.findByPriceBetweenAndIsDeletedFalse(minPrice, maxPrice);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products by price range: " + e.getMessage(), e);
        }
    }
    
    // Get products in stock
    public List<Product> getProductsInStock() {
        try {
            return productRepository.findByInStockAmountGreaterThanAndIsDeletedFalse(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products in stock: " + e.getMessage(), e);
        }
    }
    
    // Get products out of stock
    public List<Product> getProductsOutOfStock() {
        try {
            return productRepository.findByInStockAmountAndIsDeletedFalse(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products out of stock: " + e.getMessage(), e);
        }
    }
    
    // Get products with low stock
    public List<Product> getProductsWithLowStock(Integer threshold) {
        try {
            if (threshold == null || threshold < 0) {
                throw new IllegalArgumentException("Stock threshold cannot be null or negative");
            }
            
            return productRepository.findByInStockAmountBetweenAndIsDeletedFalse(1, threshold);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products with low stock: " + e.getMessage(), e);
        }
    }
    
    // Get products with images
    public List<Product> getProductsWithImages() {
        try {
            return productRepository.findByImageUrlIsNotNullAndIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products with images: " + e.getMessage(), e);
        }
    }
    
    // Get products without images
    public List<Product> getProductsWithoutImages() {
        try {
            return productRepository.findByImageUrlIsNullAndIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get products without images: " + e.getMessage(), e);
        }
    }
    
    // Update product stock
    public Product updateProductStock(Long id, Integer newStockAmount) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }
            
            if (newStockAmount == null || newStockAmount < 0) {
                throw new IllegalArgumentException("Stock amount cannot be null or negative");
            }
            
            Product product = getProductByIdOrThrow(id);
            product.setInStockAmount(newStockAmount);
            
            return productRepository.save(product);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product stock for ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    // Get product count statistics
    public long getActiveProductCount() {
        try {
            return productRepository.countByIsDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active product count: " + e.getMessage(), e);
        }
    }
    
    public long getDeletedProductCount() {
        try {
            return productRepository.countByIsDeletedTrue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get deleted product count: " + e.getMessage(), e);
        }
    }
    
    public long getProductCountInStock() {
        try {
            return productRepository.countByInStockAmountGreaterThanAndIsDeletedFalse(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product count in stock: " + e.getMessage(), e);
        }
    }
    
    public long getProductCountOutOfStock() {
        try {
            return productRepository.countByInStockAmountAndIsDeletedFalse(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product count out of stock: " + e.getMessage(), e);
        }
    }
    
    public long getProductCountByCategory(Long categoryId) {
        try {
            if (categoryId == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
            
            return productRepository.countByCategoryAndIsDeletedFalse(category);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product count by category ID " + categoryId + ": " + e.getMessage(), e);
        }
    }
    
    public long getProductCountBySeller(Long sellerId) {
        try {
            if (sellerId == null) {
                throw new IllegalArgumentException("Seller ID cannot be null");
            }
            
            Seller seller = sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new IllegalArgumentException("Seller not found with ID: " + sellerId));
            
            return productRepository.countBySellerAndIsDeletedFalse(seller);
            
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product count by seller ID " + sellerId + ": " + e.getMessage(), e);
        }
    }
    
    // Validation methods
    private void validateProductForCreation(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Product description is required");
        }
        
        if (product.getInStockAmount() == null || product.getInStockAmount() < 0) {
            throw new IllegalArgumentException("In stock amount cannot be negative");
        }
        
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Product category is required");
        }
        
        if (product.getSeller() == null) {
            throw new IllegalArgumentException("Product seller is required");
        }
    }
    
    private void validateProductForUpdate(Product product, Long existingProductId) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        
        if (product.getDescription() == null || product.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Product description is required");
        }
        
        if (product.getInStockAmount() == null || product.getInStockAmount() < 0) {
            throw new IllegalArgumentException("In stock amount cannot be negative");
        }
        
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Product category is required");
        }
        
        if (product.getSeller() == null) {
            throw new IllegalArgumentException("Product seller is required");
        }
    }
}
