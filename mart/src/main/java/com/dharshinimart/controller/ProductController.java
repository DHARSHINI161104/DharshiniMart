package com.dharshinimart.controller;

import com.dharshinimart.config.AuthInterceptor;
import com.dharshinimart.model.User;
import com.dharshinimart.service.CartService;
import com.dharshinimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var productOpt = productService.findById(id);
        if (productOpt.isEmpty()) return "redirect:/dashboard";
        model.addAttribute("user", user);
        model.addAttribute("product", productOpt.get());
        model.addAttribute("cartCount", cartService.getCartCount(session));
        return "product-detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes ra) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        var productOpt = productService.findById(productId);
        if (productOpt.isEmpty()) return "redirect:/dashboard";
        var product = productOpt.get();
        if (product.getStock() <= 0) {
            ra.addFlashAttribute("error", product.getName() + " is out of stock");
            return "redirect:/dashboard";
        }
        if (quantity > product.getStock()) quantity = (int) product.getStock();
        cartService.addToCart(session, productId, quantity);
        ra.addFlashAttribute("success", product.getName() + " added to cart");
        return "redirect:/dashboard";
    }
}
