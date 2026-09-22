package com.dharshinimart.service;

import com.dharshinimart.model.Order;
import com.dharshinimart.model.OrderItem;
import com.dharshinimart.model.OrderStatus;
import com.dharshinimart.model.Product;
import com.dharshinimart.model.User;
import com.dharshinimart.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Transactional
    public Order placeOrder(User user, String customerName, String mobile, String address, HttpSession session) {
        List<CartService.CartItem> cartItems = cartService.getCartItems(session, productService);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        // Validate stock
        for (CartService.CartItem ci : cartItems) {
            if (ci.product.getStock() < ci.quantity) {
                throw new IllegalStateException(ci.product.getName() + " has only " + ci.product.getStock() + " " + ci.product.getUnit() + " left");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setMobile(mobile);
        order.setDeliveryAddress(address);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setSubtotal(cartService.getSubtotal(session, productService));
        order.setTotalAmount(cartService.getTotal(session, productService));
        order.setDiscountTotal(cartService.getDiscount(session, productService));

        for (CartService.CartItem ci : cartItems) {
            Product p = ci.product;
            double unitPrice = p.getDiscountedPrice();
            OrderItem oi = new OrderItem(p, ci.quantity, unitPrice, p.getUnit());
            oi.setOrder(order);
            order.getItems().add(oi);
            // decrease stock
            p.setStock(p.getStock() - ci.quantity);
            productService.save(p);
        }
        Order saved = orderRepository.save(order);
        cartService.clearCart(session);
        return saved;
    }

    public List<Order> findByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> findAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Order updateStatus(Long id, OrderStatus status) {
        Order o = findById(id);
        if (o != null) {
            o.setStatus(status);
            return orderRepository.save(o);
        }
        return null;
    }
}
