package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.OrderStatus;
import com.dharshinimart.model.User;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/orders")
    public String orders(HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var orders = user.getRole().name().equals("ADMIN") ? orderService.findAll() : orderService.findByUser(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var order = orderService.findById(id);
        if (order == null) return "redirect:/orders";
        if (!order.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) return "redirect:/orders";
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("statuses", OrderStatus.values());
        return "order-detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status, HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        if (!user.getRole().name().equals("ADMIN")) return "redirect:/orders";
        orderService.updateStatus(id, status);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var order = orderService.findById(id);
        if (order != null && (order.getUser().getId().equals(user.getId()) || user.getRole().name().equals("ADMIN"))) {
            if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED) {
                orderService.updateStatus(id, OrderStatus.CANCELLED);
            }
        }
        return "redirect:/orders";
    }
}
