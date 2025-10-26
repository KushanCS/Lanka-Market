package com.example.lankamarket.features.order_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Create order
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequestDTO request) {
        try {
            Order order = orderService.createOrder(
                request.getUserId(),
                request.getDeliveryAddress(),
                request.getContactNumber(),
                request.getProductIds(),
                request.getPaymentMethod(),
                request.getNotes()
            );
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                OrderDTO orderDTO = new OrderDTO(order.get());
                return ResponseEntity.ok(orderDTO);
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

    // Get orders by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get all active orders
    @GetMapping
    public ResponseEntity<?> getAllActiveOrders() {
        try {
            List<Order> orders = orderService.getAllActiveOrders();
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get orders by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Update order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @Valid @RequestBody UpdateOrderRequestDTO request) {
        try {
            Order order = orderService.updateOrder(
                id,
                request.getDeliveryAddress(),
                request.getContactNumber(),
                request.getProductIds(),
                request.getPaymentMethod(),
                request.getNotes()
            );
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequestDTO request) {
        try {
            Order order = orderService.updateOrderStatus(id, request.getStatus());
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Add product to order
    @PostMapping("/{orderId}/products")
    public ResponseEntity<?> addProductToOrder(@PathVariable Long orderId, @RequestParam Long productId) {
        try {
            Order order = orderService.addProductToOrder(orderId, productId);
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Remove product from order
    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<?> removeProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        try {
            Order order = orderService.removeProductFromOrder(orderId, productId);
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Cancel order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Delete order (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(new SuccessResponse("Order with ID " + id + " has been deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Restore order
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreOrder(@PathVariable Long id) {
        try {
            Order order = orderService.restoreOrder(id);
            OrderDTO orderDTO = new OrderDTO(order);
            return ResponseEntity.ok(orderDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get recent orders
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentOrders(@RequestParam(defaultValue = "30") int days) {
        try {
            List<Order> orders = orderService.getRecentOrders(days);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get orders with more than N products
    @GetMapping("/with-more-than")
    public ResponseEntity<?> getOrdersWithMoreThanNProducts(@RequestParam int minCount) {
        try {
            List<Order> orders = orderService.getOrdersWithMoreThanNProducts(minCount);
            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get order statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getOrderStats() {
        try {
            long activeCount = orderService.getActiveOrderCount();
            long pendingCount = orderService.getOrderCountByStatus("PENDING");
            long confirmedCount = orderService.getOrderCountByStatus("CONFIRMED");
            long processingCount = orderService.getOrderCountByStatus("PROCESSING");
            long shippedCount = orderService.getOrderCountByStatus("SHIPPED");
            long deliveredCount = orderService.getOrderCountByStatus("DELIVERED");
            long cancelledCount = orderService.getOrderCountByStatus("CANCELLED");
            long refundedCount = orderService.getOrderCountByStatus("REFUNDED");

            return ResponseEntity.ok(new OrderStats(
                activeCount, pendingCount, confirmedCount, processingCount,
                shippedCount, deliveredCount, cancelledCount, refundedCount
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Internal Server Error", e.getMessage()));
        }
    }

    // Get revenue statistics
    @GetMapping("/stats/revenue")
    public ResponseEntity<?> getRevenueStats() {
        try {
            java.math.BigDecimal totalRevenue = orderService.getTotalRevenue();
            java.math.BigDecimal averageOrderValue = orderService.getAverageOrderValue();

            return ResponseEntity.ok(new RevenueStats(totalRevenue, averageOrderValue));
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

    public static class OrderStats {
        private long activeOrders;
        private long pendingOrders;
        private long confirmedOrders;
        private long processingOrders;
        private long shippedOrders;
        private long deliveredOrders;
        private long cancelledOrders;
        private long refundedOrders;

        public OrderStats(long activeOrders, long pendingOrders, long confirmedOrders, 
                         long processingOrders, long shippedOrders, long deliveredOrders, 
                         long cancelledOrders, long refundedOrders) {
            this.activeOrders = activeOrders;
            this.pendingOrders = pendingOrders;
            this.confirmedOrders = confirmedOrders;
            this.processingOrders = processingOrders;
            this.shippedOrders = shippedOrders;
            this.deliveredOrders = deliveredOrders;
            this.cancelledOrders = cancelledOrders;
            this.refundedOrders = refundedOrders;
        }

        public long getActiveOrders() { return activeOrders; }
        public long getPendingOrders() { return pendingOrders; }
        public long getConfirmedOrders() { return confirmedOrders; }
        public long getProcessingOrders() { return processingOrders; }
        public long getShippedOrders() { return shippedOrders; }
        public long getDeliveredOrders() { return deliveredOrders; }
        public long getCancelledOrders() { return cancelledOrders; }
        public long getRefundedOrders() { return refundedOrders; }
    }

    public static class RevenueStats {
        private java.math.BigDecimal totalRevenue;
        private java.math.BigDecimal averageOrderValue;

        public RevenueStats(java.math.BigDecimal totalRevenue, java.math.BigDecimal averageOrderValue) {
            this.totalRevenue = totalRevenue;
            this.averageOrderValue = averageOrderValue;
        }

        public java.math.BigDecimal getTotalRevenue() { return totalRevenue; }
        public java.math.BigDecimal getAverageOrderValue() { return averageOrderValue; }
    }
}
