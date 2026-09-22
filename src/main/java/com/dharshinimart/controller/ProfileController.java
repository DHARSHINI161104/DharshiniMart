package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.User;
import com.dharshinimart.repository.UserRepository;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final CartService cartService;

    public ProfileController(UserRepository userRepository, OrderService orderService, CartService cartService) {
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        // refresh from DB
        user = userRepository.findById(user.getId()).orElse(user);
        session.setAttribute(AuthInterceptor.SESSION_USER, user);
        model.addAttribute("user", user);
        model.addAttribute("orders", orderService.findByUser(user.getId()));
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam(required = false) String mobile,
                                @RequestParam(required = false) String address,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        user = userRepository.findById(user.getId()).orElse(user);
        user.setName(name);
        user.setMobile(mobile);
        user.setAddress(address);
        userRepository.save(user);
        session.setAttribute(AuthInterceptor.SESSION_USER, user);
        ra.addFlashAttribute("success", "Profile updated successfully");
        return "redirect:/profile";
    }
}
