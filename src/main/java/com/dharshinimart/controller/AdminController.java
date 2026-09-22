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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final CartService cartService;

    public AdminController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        return user != null && "ADMIN".equals(user.getRole().name());
    }

    @GetMapping("/stock")
    public String stock(@RequestParam(required = false) String category,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String stockFilter,
                        HttpSession session, Model model) {
        if (session.getAttribute(AuthInterceptor.SESSION_USER) == null) return "redirect:/login";
        if (!isAdmin(session)) return "redirect:/dashboard";
        User user = (User) session.getAttribute(AuthInterceptor.SESSION_USER);
        var products = productService.filter(category, search);
        // additional stock filter
        if (stockFilter != null) {
            if (stockFilter.equals("low")) products = products.stream().filter(p -> p.getStock() > 0 && p.getStock() <= p.getMinStock()).toList();
            else if (stockFilter.equals("out")) products = products.stream().filter(p -> p.getStock() <= 0).toList();
            else if (stockFilter.equals("in")) products = products.stream().filter(p -> p.getStock() > p.getMinStock()).toList();
        }
        model.addAttribute("user", user);
        model.addAttribute("products", products);
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category == null ? "ALL" : category);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("stockFilter", stockFilter == null ? "all" : stockFilter);
        model.addAttribute("cartCount", cartService.getCartCount(session));
        model.addAttribute("newProduct", new Product());
        return "admin-stock";
    }

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/dashboard";
        if (product.getImage() == null || product.getImage().isBlank()) product.setImage("🛒");
        productService.save(product);
        ra.addFlashAttribute("success", "Product added: " + product.getName());
        return "redirect:/admin/stock";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam Category category,
                              @RequestParam double price,
                              @RequestParam String unit,
                              @RequestParam double stock,
                              @RequestParam double minStock,
                              @RequestParam int discount,
                              @RequestParam(required = false) String image,
                              @RequestParam(required = false) String description,
                              HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/dashboard";
        var opt = productService.findById(id);
        if (opt.isPresent()) {
            Product p = opt.get();
            p.setName(name);
            p.setCategory(category);
            p.setPrice(price);
            p.setUnit(unit);
            p.setStock(stock);
            p.setMinStock(minStock);
            p.setDiscount(discount);
            if (image != null && !image.isBlank()) p.setImage(image);
            if (description != null) p.setDescription(description);
            productService.save(p);
            ra.addFlashAttribute("success", "Product updated: " + p.getName());
        }
        return "redirect:/admin/stock";
    }

    @PostMapping("/product/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/dashboard";
        productService.delete(id);
        ra.addFlashAttribute("success", "Product deleted");
        return "redirect:/admin/stock";
    }

    @PostMapping("/product/{id}/stock")
    public String updateStock(@PathVariable Long id, @RequestParam double delta, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/dashboard";
        var opt = productService.findById(id);
        if (opt.isPresent()) {
            Product p = opt.get();
            p.setStock(Math.max(0, p.getStock() + delta));
            productService.save(p);
        }
        return "redirect:/admin/stock";
    }
}
