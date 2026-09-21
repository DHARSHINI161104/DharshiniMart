package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.dto.LoginRequest;
import com.dharshinimart.dto.RegisterRequest;
import com.dharshinimart.exception.AuthException;
import com.dharshinimart.model.Category;
import com.dharshinimart.model.User;
import com.dharshinimart.service.AuthService;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;
    private final ProductService productService;
    private final CartService cartService;

    public AuthController(AuthService authService, ProductService productService, CartService cartService) {
        this.authService = authService;
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest request,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }
        try {
            User user = authService.login(request.getEmail(), request.getPassword());
            session.setAttribute(AuthInterceptor.SESSION_USER, user);
            return "redirect:/dashboard";
        } catch (AuthException ex) {
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        try {
            authService.register(request);
            return "redirect:/login?registered";
        } catch (AuthException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String category,
                            @RequestParam(required = false) String search,
                            HttpSession session, Model model) {
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        model.addAttribute("user", user);
        model.addAttribute("products", productService.filter(category, search));
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category == null ? "ALL" : category);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("lowStockProducts", productService.findLowStock());
        model.addAttribute("offers", productService.findOffers());
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}