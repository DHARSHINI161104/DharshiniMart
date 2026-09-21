package com.dharshinimart.repository;

import com.dharshinimart.model.Category;
import com.dharshinimart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByDiscountGreaterThan(int discount);
    List<Product> findByStockLessThanEqual(double stock);
    long count();
}
