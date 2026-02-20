package org.example.productservice;

import org.example.productservice.Controller.ProductController;
import org.example.productservice.Model.Product;
import org.example.productservice.Repository.ProductRepository;
import org.example.productservice.Service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private ProductController productController;

    @Test
    void GetProducts_ReturnsAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Item_1");
        product1.setPrice(1200.00);
        product1.setQuantity(5);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Item_2");
        product2.setPrice(25.00);
        product2.setQuantity(50);

        List<Product> mockProducts = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(mockProducts);

        // Act
        List<Product> result = productController.GetProducts();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Item_1");
        assertThat(result.get(1).getName()).isEqualTo("Item_2");
    }
}