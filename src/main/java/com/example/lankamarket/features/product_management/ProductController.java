package com.example.lankamarket.features.product_management;

import com.example.lankamarket.features.category_management.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Convert Product -> ProductDTO
    private ProductDTO toDTO(Product product) {
        CategoryDTO categoryDTO = null;
        if (product.getCategory() != null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setIsDeleted(product.getCategory().getIsDeleted());
            categoryDTO.setCreatedAt(product.getCategory().getCreatedAt());
            categoryDTO.setUpdatedAt(product.getCategory().getUpdatedAt());
        }

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getImageUrl(),
                product.getInStockAmount(),
                product.getIsDeleted(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                categoryDTO,
                product.getSeller() != null ? product.getSeller().getId() : null,
                product.getSeller() != null ? product.getSeller().getName() : null
        );
    }

    // Convert List<Product> -> List<ProductDTO>
    private List<ProductDTO> toDTOList(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ✅ Create product (Request: Product, Response: ProductDTO)
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
        try {
            Product created = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(created));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProductWithParams(
            @RequestParam String name,
            @RequestParam BigDecimal price,
            @RequestParam String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam Integer inStockAmount,
            @RequestParam Long categoryId,
            @RequestParam Long sellerId) {
        try {
            Product created = productService.createProduct(name, price, description, imageUrl, inStockAmount, categoryId, sellerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(created));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    // ✅ Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            return product.map(value -> ResponseEntity.ok(toDTO(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    // ✅ All active
    @GetMapping
    public ResponseEntity<?> getAllActiveProducts() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getAllActiveProducts()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/deleted")
    public ResponseEntity<?> getAllDeletedProducts() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getAllDeletedProducts()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsByCategory(categoryId)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getProductsBySeller(@PathVariable Long sellerId) {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsBySeller(sellerId)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/ordered/name")
    public ResponseEntity<?> getProductsOrderedByName() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOrderedByName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/ordered/price-asc")
    public ResponseEntity<?> getProductsOrderedByPriceAsc() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOrderedByPriceAsc()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/ordered/price-desc")
    public ResponseEntity<?> getProductsOrderedByPriceDesc() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOrderedByPriceDesc()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/ordered/created")
    public ResponseEntity<?> getProductsOrderedByCreatedDate() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOrderedByCreatedDate()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/ordered/stock")
    public ResponseEntity<?> getProductsOrderedByStockAmount() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOrderedByStockAmount()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    // ✅ Update product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateProductStock(@PathVariable Long id, @RequestParam Integer stockAmount) {
        try {
            Product updated = productService.updateProductStock(id, stockAmount);
            return ResponseEntity.ok(toDTO(updated));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new SuccessResponse("Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreProduct(@PathVariable Long id) {
        try {
            Product restored = productService.restoreProduct(id);
            return ResponseEntity.ok(toDTO(restored));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/search/name")
    public ResponseEntity<?> searchProductsByName(@RequestParam String name) {
        try {
            return ResponseEntity.ok(toDTOList(productService.searchProductsByName(name)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/search/description")
    public ResponseEntity<?> searchProductsByDescription(@RequestParam String description) {
        try {
            return ResponseEntity.ok(toDTOList(productService.searchProductsByDescription(description)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductsByMultipleCriteria(@RequestParam String searchTerm) {
        try {
            return ResponseEntity.ok(toDTOList(productService.searchProductsByMultipleCriteria(searchTerm)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<?> getProductsByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice) {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsByPriceRange(minPrice, maxPrice)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/in-stock")
    public ResponseEntity<?> getProductsInStock() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsInStock()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<?> getProductsOutOfStock() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsOutOfStock()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getProductsWithLowStock(@RequestParam(defaultValue = "5") Integer threshold) {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsWithLowStock(threshold)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/with-images")
    public ResponseEntity<?> getProductsWithImages() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsWithImages()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/without-images")
    public ResponseEntity<?> getProductsWithoutImages() {
        try {
            return ResponseEntity.ok(toDTOList(productService.getProductsWithoutImages()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    // ✅ Stats remain unchanged (no DTO needed)
    @GetMapping("/stats")
    public ResponseEntity<?> getProductStats() {
        try {
            long active = productService.getActiveProductCount();
            long deleted = productService.getDeletedProductCount();
            long inStock = productService.getProductCountInStock();
            long outOfStock = productService.getProductCountOutOfStock();

            return ResponseEntity.ok(new ProductStats(active, deleted, inStock, outOfStock));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/stats/category/{categoryId}")
    public ResponseEntity<?> getProductCountByCategory(@PathVariable Long categoryId) {
        try {
            long count = productService.getProductCountByCategory(categoryId);
            return ResponseEntity.ok(new ProductCountResponse(count));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    @GetMapping("/stats/seller/{sellerId}")
    public ResponseEntity<?> getProductCountBySeller(@PathVariable Long sellerId) {
        try {
            long count = productService.getProductCountBySeller(sellerId);
            return ResponseEntity.ok(new ProductCountResponse(count));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("Internal Error", e.getMessage()));
        }
    }

    // ✅ Response classes
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

    public static class ProductStats {
        private long activeProducts;
        private long deletedProducts;
        private long inStockProducts;
        private long outOfStockProducts;

        public ProductStats(long activeProducts, long deletedProducts, long inStockProducts, long outOfStockProducts) {
            this.activeProducts = activeProducts;
            this.deletedProducts = deletedProducts;
            this.inStockProducts = inStockProducts;
            this.outOfStockProducts = outOfStockProducts;
        }

        public long getActiveProducts() { return activeProducts; }
        public long getDeletedProducts() { return deletedProducts; }
        public long getInStockProducts() { return inStockProducts; }
        public long getOutOfStockProducts() { return outOfStockProducts; }
    }

    public static class ProductCountResponse {
        private long count;

        public ProductCountResponse(long count) {
            this.count = count;
        }

        public long getCount() { return count; }
    }
}
