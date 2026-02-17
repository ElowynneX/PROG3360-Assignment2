package org.example.orderservice.Controller;

import org.example.orderservice.Client.ProductClient;
import org.example.orderservice.Client.ProductDto;
import org.example.orderservice.Model.Order;
import org.example.orderservice.Repository.OrderRepository;
import org.example.orderservice.Service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository _orderRepository;
    private final ProductClient _productClient;
    private final FeatureFlagService _featureFlagService;

    public OrderController(OrderRepository orderRepository, ProductClient productClient, FeatureFlagService featureFlagService) {
        _orderRepository = orderRepository;
        _productClient = productClient;
        _featureFlagService = featureFlagService;
    }

    @GetMapping()
    public List<Order> GetOrders() {
        return _orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order GetOrderById(@PathVariable Long id) {
        return _orderRepository.findById(id).orElseThrow();
    }

    @PostMapping()
    public Order CreateOrder(@RequestBody Order order) {
        ProductDto product = _productClient.GetProductById(order.getProductId());

        if (product.quantity() < order.getQuantity()) {
            throw new RuntimeException("Insufficient product quantity");
        }

        double totalPrice = product.price() * order.getQuantity();

        // Bulk Order Discount (15% if quantity > 5)
        if (_featureFlagService.isBulkOrderDiscountEnabled() && order.getQuantity() > 5) {
            totalPrice = totalPrice * 0.85;
        }

        order.setTotalPrice(totalPrice);
        order.setStatus("ORDER CREATED");

        Order savedOrder = _orderRepository.save(order);

        // Order Notifications
        if (_featureFlagService.isOrderNotificationsEnabled()) {
            System.out.println("Order Notification: Order ID "
                    + savedOrder.getId()
                    + " | Product ID "
                    + savedOrder.getProductId()
                    + " | Total Price "
                    + savedOrder.getTotalPrice());
        }

        return savedOrder;
    }
}