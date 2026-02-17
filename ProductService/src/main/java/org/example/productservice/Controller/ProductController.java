package org.example.productservice.Controller;

import org.example.productservice.Model.Product;
import org.example.productservice.Repository.ProductRepository;
import org.example.productservice.Service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository _productRepository;
    private final FeatureFlagService _featureFlagService;

    public ProductController(ProductRepository productRepository, FeatureFlagService featureFlagService) {
        _productRepository = productRepository;
        _featureFlagService = featureFlagService;
    }

    @GetMapping()
    public List<Product> GetProducts() {
        return _productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product GetProductById(@PathVariable Long id) {
        return _productRepository.findById(id).orElseThrow();
    }

    @GetMapping("/api/products/premium")
    public List<Product> getPremiumProducts() {

        boolean premiumEnabled = _featureFlagService.isPremiumPricingEnabled();

        return _productRepository.findAll()
                .stream()
                .map(product -> {
                    if (premiumEnabled) {
                        product.setPrice(product.getPrice() * 0.9);
                    }
                    return product;
                })
                .toList();
    }

    @PostMapping()
    public Product AddProduct(@RequestBody Product product) {
        return _productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void DeleteProduct(@PathVariable Long id) {
        _productRepository.deleteById(id);
    }
}