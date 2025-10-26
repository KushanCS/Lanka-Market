package config;

import entities.Product;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import repositories.ProductRepository;
import repositories.UserRepository;

import java.math.BigDecimal;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed test user if not exists (using your original data)
        if (!userRepository.existsById(1L)) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123");
            testUser.setRole(User.UserRole.BUYER);
            userRepository.save(testUser);
            System.out.println("✅ Created test user with ID: 1 (testuser)");
        }

        if (productRepository.count() == 0) {
            User seller = userRepository.findById(1L).orElse(null);
            
            Product[] products = {
                createProduct("Premium Ceylon Tea", "High-quality Ceylon tea from the hills of Sri Lanka", new BigDecimal("20.99"), 50, "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", seller),
                createProduct("Handwoven Sarong", "Traditional Sri Lankan handwoven sarong in vibrant colors", new BigDecimal("25.50"), 20, "https://th.bing.com/th/id/OIP.N2-iOlNQsSfDE3Ic2AzYXQHaLH?w=135&h=202&c=7&r=0&o=5&dpr=1.6&pid=1.7", seller),
                createProduct("Spice Collection", "Authentic Sri Lankan spice collection including cinnamon, cardamom, and cloves", new BigDecimal("18.75"), 30, "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400", seller),
                createProduct("Wooden Elephant Carving", "Hand-carved wooden elephant figurine made from local timber", new BigDecimal("35.00"), 15, "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400", seller),
                createProduct("Coconut Oil", "Pure virgin coconut oil extracted from fresh coconuts", new BigDecimal("12.99"), 40, "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400", seller),
                createProduct("Traditional Mask", "Colorful traditional Sri Lankan mask for decoration", new BigDecimal("22.00"), 25, "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400", seller),
                createProduct("Ceylon Cinnamon Sticks", "Authentic Ceylon cinnamon sticks - the world's finest cinnamon", new BigDecimal("8.99"), 60, "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400", seller),
                createProduct("Batik Wall Hanging", "Hand-painted batik wall hanging with traditional Sri Lankan patterns", new BigDecimal("42.00"), 12, "https://images.unsplash.com/photo-1594736797933-d0401ba2fe65?w=400", seller),
                createProduct("Ceylon Black Tea", "Premium Ceylon black tea from Nuwara Eliya region", new BigDecimal("16.50"), 45, "https://images.unsplash.com/photo-1597318281675-d6b7b6c28c1b?w=400", seller)
            };
            for (Product product : products) {
                productRepository.save(product);
            }
            
            System.out.println("✅ Created " + products.length + " test products with seller ID: 1");
        }

        System.out.println("🚀 Data seeding completed! Backend ready for frontend.");
    }

    private Product createProduct(String name, String description, BigDecimal price, Integer stock, String imageUrl, User seller) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setImageUrl(imageUrl);
        product.setSeller(seller);
        return product;
    }
}
