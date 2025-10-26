package com.example.lankamarket.features.order_management;

import com.example.lankamarket.features.user_management.User;
import com.example.lankamarket.features.user_management.UserRepository;
import com.example.lankamarket.features.product_management.Product;
import com.example.lankamarket.features.product_management.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // Create order
    public Order createOrder(Long userId, String deliveryAddress, String contactNumber, 
                           List<Long> productIds, String paymentMethod, String notes) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
                throw new IllegalArgumentException("Delivery address cannot be null or empty");
            }
            if (contactNumber == null || contactNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Contact number cannot be null or empty");
            }
            if (productIds == null || productIds.isEmpty()) {
                throw new IllegalArgumentException("Product IDs cannot be null or empty");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            if (user.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot create order for deleted user");
            }

            List<Product> products = productRepository.findAllById(productIds);
            if (products.size() != productIds.size()) {
                throw new IllegalArgumentException("Some products not found");
            }

            // Check if any products are deleted
            boolean hasDeletedProducts = products.stream()
                    .anyMatch(Product::getIsDeleted);
            if (hasDeletedProducts) {
                throw new IllegalArgumentException("Cannot add deleted products to order");
            }

            Order order = new Order();
            order.setStatus("PENDING");
            order.setDeliveryAddress(deliveryAddress);
            order.setContactNumber(contactNumber);
            order.setPaymentMethod(paymentMethod);
            order.setNotes(notes);
            order.setUser(user);
            order.setProducts(products);
            order.calculateTotalAmount();

            return orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    // Get order by ID
    public Optional<Order> getOrderById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }
            return orderRepository.findByIdAndIsDeletedFalse(id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order: " + e.getMessage(), e);
        }
    }

    // Get orders by user
    public List<Order> getOrdersByUser(Long userId) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            return orderRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get orders by user: " + e.getMessage(), e);
        }
    }

    // Get all active orders
    public List<Order> getAllActiveOrders() {
        try {
            return orderRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all active orders: " + e.getMessage(), e);
        }
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty");
            }
            return orderRepository.findByStatusAndIsDeletedFalseOrderByCreatedAtDesc(status);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get orders by status: " + e.getMessage(), e);
        }
    }

    // Update order
    public Order updateOrder(Long orderId, String deliveryAddress, String contactNumber, 
                           List<Long> productIds, String paymentMethod, String notes) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = getOrderByIdOrThrow(orderId);
            
            if (!order.isPending()) {
                throw new IllegalArgumentException("Only pending orders can be updated");
            }

            if (deliveryAddress != null && !deliveryAddress.trim().isEmpty()) {
                order.setDeliveryAddress(deliveryAddress);
            }
            if (contactNumber != null && !contactNumber.trim().isEmpty()) {
                order.setContactNumber(contactNumber);
            }
            if (paymentMethod != null) {
                order.setPaymentMethod(paymentMethod);
            }
            if (notes != null) {
                order.setNotes(notes);
            }

            if (productIds != null && !productIds.isEmpty()) {
                List<Product> products = productRepository.findAllById(productIds);
                if (products.size() != productIds.size()) {
                    throw new IllegalArgumentException("Some products not found");
                }

                boolean hasDeletedProducts = products.stream()
                        .anyMatch(Product::getIsDeleted);
                if (hasDeletedProducts) {
                    throw new IllegalArgumentException("Cannot add deleted products to order");
                }

                order.setProducts(products);
                order.calculateTotalAmount();
            }

            return orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order: " + e.getMessage(), e);
        }
    }

    // Update order status
    public Order updateOrderStatus(Long orderId, String newStatus) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }
            if (newStatus == null || newStatus.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty");
            }

            Order order = getOrderByIdOrThrow(orderId);
            String currentStatus = order.getStatus();

            // Validate status transition
            if (!isValidStatusTransition(currentStatus, newStatus)) {
                throw new IllegalArgumentException("Invalid status transition from " + currentStatus + " to " + newStatus);
            }

            order.setStatus(newStatus);

            // Set payment date when order is confirmed
            if ("CONFIRMED".equals(newStatus) && order.getPaymentDate() == null) {
                order.setPaymentDate(LocalDateTime.now());
            }

            // Set delivery date when order is delivered
            if ("DELIVERED".equals(newStatus) && order.getDeliveryDate() == null) {
                order.setDeliveryDate(LocalDateTime.now());
            }

            return orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update order status: " + e.getMessage(), e);
        }
    }

    // Add product to order
    public Order addProductToOrder(Long orderId, Long productId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }

            Order order = getOrderByIdOrThrow(orderId);
            
            if (!order.isPending()) {
                throw new IllegalArgumentException("Only pending orders can be modified");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

            if (product.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot add deleted product to order");
            }

            if (!order.containsProduct(product)) {
                order.addProduct(product);
                order.calculateTotalAmount();
                return orderRepository.save(order);
            } else {
                throw new IllegalArgumentException("Product is already in the order");
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to order: " + e.getMessage(), e);
        }
    }

    // Remove product from order
    public Order removeProductFromOrder(Long orderId, Long productId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID cannot be null");
            }

            Order order = getOrderByIdOrThrow(orderId);
            
            if (!order.isPending()) {
                throw new IllegalArgumentException("Only pending orders can be modified");
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

            if (order.containsProduct(product)) {
                order.removeProduct(product);
                order.calculateTotalAmount();
                return orderRepository.save(order);
            } else {
                throw new IllegalArgumentException("Product is not in the order");
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove product from order: " + e.getMessage(), e);
        }
    }

    // Cancel order
    public Order cancelOrder(Long orderId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = getOrderByIdOrThrow(orderId);
            
            if (!order.canBeCancelled()) {
                throw new IllegalArgumentException("Order cannot be cancelled in current status: " + order.getStatus());
            }

            order.setStatus("CANCELLED");
            return orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to cancel order: " + e.getMessage(), e);
        }
    }

    // Soft delete order
    public void deleteOrder(Long orderId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = getOrderByIdOrThrow(orderId);
            order.softDelete();
            orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete order: " + e.getMessage(), e);
        }
    }

    // Restore order
    public Order restoreOrder(Long orderId) {
        try {
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

            if (!order.getIsDeleted()) {
                throw new IllegalArgumentException("Order is not deleted");
            }

            order.restore();
            return orderRepository.save(order);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to restore order: " + e.getMessage(), e);
        }
    }

    // Get recent orders
    public List<Order> getRecentOrders(int days) {
        try {
            if (days < 0) {
                throw new IllegalArgumentException("Days cannot be negative");
            }

            LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
            return orderRepository.getRecentOrders(sinceDate);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get recent orders: " + e.getMessage(), e);
        }
    }

    // Get orders with more than N products
    public List<Order> getOrdersWithMoreThanNProducts(int minCount) {
        try {
            if (minCount < 0) {
                throw new IllegalArgumentException("Minimum count cannot be negative");
            }
            return orderRepository.findOrdersWithMoreThanNProducts(minCount);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get orders with more than " + minCount + " products: " + e.getMessage(), e);
        }
    }

    // Get order statistics
    public long getActiveOrderCount() {
        try {
            return orderRepository.countActiveOrders();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active order count: " + e.getMessage(), e);
        }
    }

    public long getOrderCountByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalArgumentException("Status cannot be null or empty");
            }
            return orderRepository.countOrdersByStatus(status);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get order count by status: " + e.getMessage(), e);
        }
    }

    public BigDecimal getTotalRevenue() {
        try {
            BigDecimal revenue = orderRepository.getTotalRevenue();
            return revenue != null ? revenue : BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get total revenue: " + e.getMessage(), e);
        }
    }

    public BigDecimal getAverageOrderValue() {
        try {
            BigDecimal avgValue = orderRepository.getAverageOrderValue();
            return avgValue != null ? avgValue : BigDecimal.ZERO;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get average order value: " + e.getMessage(), e);
        }
    }

    // Helper methods
    private Order getOrderByIdOrThrow(Long orderId) {
        return orderRepository.findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "PENDING":
                return "CONFIRMED".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "CONFIRMED":
                return "PROCESSING".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "PROCESSING":
                return "SHIPPED".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "SHIPPED":
                return "DELIVERED".equals(newStatus);
            case "DELIVERED":
                return "REFUNDED".equals(newStatus);
            case "CANCELLED":
                return false; // Cannot transition from cancelled
            case "REFUNDED":
                return false; // Cannot transition from refunded
            default:
                return false;
        }
    }
}
