package com.dharshinimart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, length = 20)
    private String unit; // e.g. kg, litre, packet, piece

    @Column(nullable = false)
    private double stock; // current stock

    @Column(nullable = false)
    private double minStock; // minimum stock level for low stock alert

    @Column(length = 500)
    private String image; // image url or emoji placeholder

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int discount; // percent 0-100

    private LocalDateTime createdAt;

    public Product() {}

    public Product(String name, Category category, double price, String unit, double stock, double minStock, String image, String description, int discount) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.minStock = minStock;
        this.image = image;
        this.description = description;
        this.discount = discount;
        this.createdAt = LocalDateTime.now();
    }

    public String getStockStatus() {
        if (stock <= 0) return "Out of Stock";
        if (stock <= minStock) return "Low Stock";
        return "In Stock";
    }

    public String getStockStatusClass() {
        if (stock <= 0) return "out";
        if (stock <= minStock) return "low";
        return "in";
    }

    public double getDiscountedPrice() {
        if (discount <= 0) return price;
        return Math.round(price * (100 - discount) / 100.0 * 100.0) / 100.0;
    }

    // getters setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }
    public double getMinStock() { return minStock; }
    public void setMinStock(double minStock) { this.minStock = minStock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDiscount() { return discount; }
    public void setDiscount(int discount) { this.discount = discount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
