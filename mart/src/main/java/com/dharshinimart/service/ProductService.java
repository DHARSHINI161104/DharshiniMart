package com.dharshinimart.service;

import com.dharshinimart.model.Category;
import com.dharshinimart.model.Product;
import com.dharshinimart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> search(String query) {
        if (query == null || query.isBlank()) return findAll();
        return productRepository.findByNameContainingIgnoreCase(query.trim());
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> filter(String categoryStr, String search) {
        boolean hasCategory = categoryStr != null && !categoryStr.isBlank() && !categoryStr.equalsIgnoreCase("ALL");
        boolean hasSearch = search != null && !search.isBlank();
        List<Product> list;
        if (hasCategory) {
            try {
                Category cat = Category.valueOf(categoryStr);
                list = findByCategory(cat);
            } catch (IllegalArgumentException e) {
                list = findAll();
            }
        } else {
            list = findAll();
        }
        if (hasSearch) {
            String q = search.toLowerCase().trim();
            list = list.stream().filter(p -> p.getName().toLowerCase().contains(q) || p.getDescription().toLowerCase().contains(q)).toList();
        }
        return list;
    }

    public List<Product> findOffers() {
        return productRepository.findByDiscountGreaterThan(0);
    }

    public List<Product> findLowStock() {
        return productRepository.findAll().stream().filter(p -> p.getStock() > 0 && p.getStock() <= p.getMinStock()).toList();
    }

    public List<Product> findOutOfStock() {
        return productRepository.findAll().stream().filter(p -> p.getStock() <= 0).toList();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public void decreaseStock(Long productId, double qty) {
        Product p = productRepository.findById(productId).orElseThrow();
        p.setStock(Math.max(0, p.getStock() - qty));
        productRepository.save(p);
    }
}
