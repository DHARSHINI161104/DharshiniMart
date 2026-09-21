package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.User;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.OrderService;
import com.dharshinimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final ProductService productService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, ProductService productService, OrderService orderService) {
        this.cartService = cartService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var items = cartService.getCartItems(session, productService);
        if (items.isEmpty()) return "redirect:/cart";
        model.addAttribute("user", user);
        model.addAttribute("items", items);
        model.addAttribute("subtotal", cartService.getSubtotal(session, productService));
        model.addAttribute("discount", cartService.getDiscount(session, productService));
        model.addAttribute("total", cartService.getTotal(session, productService));
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(@RequestParam String customerName,
                             @RequestParam String mobile,
                             @RequestParam String address,
                             HttpSession session,
                             Model model,
                             RedirectAttributes ra) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        try {
            var order = orderService.placeOrder(user, customerName, mobile, address, session);
            ra.addFlashAttribute("orderId", order.getId());
            return "redirect:/order-confirmation/" + order.getId();
        } catch (Exception ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/order-confirmation/{id}")
    public String confirmation(@org.springframework.web.bind.annotation.PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var order = orderService.findById(id);
        if (order == null || !order.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "order-confirmation";
    }
}
