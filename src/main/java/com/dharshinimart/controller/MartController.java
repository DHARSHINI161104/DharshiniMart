package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.Category;
import com.dharshinimart.model.Product;
import com.dharshinimart.model.User;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MartController {

    private final ProductService productService;
    private final CartService cartService;

    public MartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    private boolean requireLogin(HttpSession session) {
        return session.getAttribute(AuthInterceptor.SESSION_USER) == null;
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category,
                       @RequestParam(required = false) String search,
                       HttpSession session, Model model) {
        if (requireLogin(session)) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        List<Product> products = productService.filter(category, search);
        model.addAttribute("user", user);
        model.addAttribute("products", products);
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category == null ? "ALL" : category);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("lowStockProducts", productService.findLowStock());
        model.addAttribute("offersCount", productService.findOffers().size());
        return "shop";
    }

    @GetMapping("/offers")
    public String offers(HttpSession session, Model model) {
        if (requireLogin(session)) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        List<Product> offers = productService.findOffers();
        model.addAttribute("user", user);
        model.addAttribute("products", offers);
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", "ALL");
        model.addAttribute("search", "");
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("isOffersPage", true);
        return "offers";
    }

    // Override dashboard to show modern mart dashboard
    // AuthController already has /dashboard, but we enhance via model attributes with a ControllerAdvice? 
    // Instead we intercept via separate mapping that adds mart data. Since AuthController's dashboard will be overridden if we map same path, we create a MartDashboardController that handles /dashboard via same URL with higher priority?
    // To avoid conflict, we will keep AuthController's /dashboard but add an interceptor that enriches model via MartDashboardConfig. Simplest: create a new mapping /mart-dashboard and redirect dashboard to it? But spec says after login show dashboard.
    // Solution: Modify AuthController dashboard handling via a new controller with @GetMapping("/dashboard") that takes precedence? Spring will complain ambiguous.
    // Instead we will create a MartDashboardEnhancer that is not a controller but we will modify AuthController via Edit to add product data. Easier: we will edit AuthController to delegate to mart data. But we are told not to modify login logic, but dashboard logic can be enhanced.
    // For now, provide a replacement controller for /dashboard2 and make AuthController redirect? Let's make AuthController modification minimal: add product data injection.
}
