package org.example.orderservice;

import org.example.orderservice.Client.ProductClient;
import org.example.orderservice.Client.ProductDto;
import org.example.orderservice.Controller.OrderController;
import org.example.orderservice.Model.Order;
import org.example.orderservice.Repository.OrderRepository;
import org.example.orderservice.Service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void createOrderAndGetById_success() {
        // Arrange: mock ProductDto returned by ProductClient
        ProductDto product = new ProductDto(1L, "Item", 1000.0, 10);
        when(productClient.GetProductById(1L)).thenReturn(product);

        // Arrange: mock feature flags
        when(featureFlagService.isBulkOrderDiscountEnabled()).thenReturn(false);
        when(featureFlagService.isOrderNotificationsEnabled()).thenReturn(false);

        // Arrange: mock saving order
        Order orderToSave = new Order();
        orderToSave.setProductId(1L);
        orderToSave.setQuantity(2);

        Order savedOrder = new Order();
        savedOrder.setId(101L);
        savedOrder.setProductId(1L);
        savedOrder.setQuantity(2);
        savedOrder.setTotalPrice(2000.0); // 1000 * 2
        savedOrder.setStatus("ORDER CREATED");

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderRepository.findById(101L)).thenReturn(Optional.of(savedOrder));

        // Act: create order
        Order result = orderController.CreateOrder(orderToSave);

        // Assert: verify order was saved with correct total price and status
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(101L);
        assertThat(result.getTotalPrice()).isEqualTo(2000.0);
        assertThat(result.getStatus()).isEqualTo("ORDER CREATED");

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productClient, times(1)).GetProductById(1L);

        // Act: get order by ID
        Order fetched = orderController.GetOrderById(101L);

        // Assert: fetched order matches saved order
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(101L);
        assertThat(fetched.getTotalPrice()).isEqualTo(2000.0);
    }
}