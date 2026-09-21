package com.dharshinimart.service;

import com.dharshinimart.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    public static final String CART_SESSION_KEY = "CART";

    @SuppressWarnings("unchecked")
    public Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Long productId, int qty) {
        Map<Long, Integer> cart = getCart(session);
        cart.merge(productId, qty, Integer::sum);
        if (cart.get(productId) <= 0) cart.remove(productId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, int qty) {
        Map<Long, Integer> cart = getCart(session);
        if (qty <= 0) cart.remove(productId);
        else cart.put(productId, qty);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public int getCartCount(HttpSession session) {
        return getCart(session).values().stream().mapToInt(Integer::intValue).sum();
    }

    public static class CartItem {
        public Product product;
        public int quantity;
        public double lineTotal;
        public double lineSubtotal;
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            double discounted = product.getDiscountedPrice();
            double original = product.getPrice();
            this.lineTotal = Math.round(discounted * quantity * 100.0)/100.0;
            this.lineSubtotal = Math.round(original * quantity * 100.0)/100.0;
        }
    }

    public List<CartItem> getCartItems(HttpSession session, ProductService productService) {
        Map<Long, Integer> cart = getCart(session);
        List<CartItem> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> e : cart.entrySet()) {
            productService.findById(e.getKey()).ifPresent(p -> items.add(new CartItem(p, e.getValue())));
        }
        return items;
    }

    public double getSubtotal(HttpSession session, ProductService productService) {
        return getCartItems(session, productService).stream().mapToDouble(c -> c.lineSubtotal).sum();
    }

    public double getTotal(HttpSession session, ProductService productService) {
        return getCartItems(session, productService).stream().mapToDouble(c -> c.lineTotal).sum();
    }

    public double getDiscount(HttpSession session, ProductService productService) {
        return Math.round((getSubtotal(session, productService) - getTotal(session, productService)) * 100.0)/100.0;
    }
}
