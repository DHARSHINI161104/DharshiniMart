package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.User;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var items = cartService.getCartItems(session, productService);
        model.addAttribute("user", user);
        model.addAttribute("items", items);
        model.addAttribute("subtotal", cartService.getSubtotal(session, productService));
        model.addAttribute("discount", cartService.getDiscount(session, productService));
        model.addAttribute("total", cartService.getTotal(session, productService));
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        cartService.updateQuantity(session, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long productId, HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        cartService.removeFromCart(session, productId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clear(HttpSession session) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        cartService.clearCart(session);
        return "redirect:/cart";
    }
}
