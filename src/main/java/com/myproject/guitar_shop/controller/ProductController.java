package com.myproject.guitar_shop.controller;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.exception.NonExistentItemException;
import com.myproject.guitar_shop.exception.NotEnoughProductException;
import com.myproject.guitar_shop.exception.ProductNotFoundException;
import com.myproject.guitar_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.getById(id));
        return "home";
    }

    @GetMapping("/{page}/{size}")
    public String getProductsByTitle(@RequestParam("title") String title, Model model, @PathVariable int page, @PathVariable int size) {
        Page<Product> pageOfProducts = productService.getProductsByTitle(title.toUpperCase(), page, size);
        int totalPages = pageOfProducts.getTotalPages();
        long totalItems = pageOfProducts.getTotalElements();
        model.addAttribute("title", title);
        model.addAttribute("products", pageOfProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return "home";
    }

    @GetMapping("/category/{category}")
    public String getProductByCategory(@PathVariable String category, Model model) {
        model.addAttribute("products", productService.getAllProductsByCategory(Product.Category.valueOf(category)));
        return "home";
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/form")
    public String addProductForm(Model model) {
        model.addAttribute("adding_product_form", true);
        return "home";
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/add")
    public String addNewProduct(@RequestParam(value = "image") MultipartFile image, @RequestParam Map<String, String> params, Model model) {
        model.addAttribute("product", productService.addNewProduct(params, image));
        return "home";
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/update/{product_id}")
    public String updateProduct(@PathVariable int product_id, @RequestParam Map<String, String> params, Model model) {
        Product updatedProduct = productService.update(params, product_id);
        model.addAttribute("product", updatedProduct);
        return "home";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NoSuchElementException.class, NonExistentItemException.class, NotEnoughProductException.class, ProductNotFoundException.class})
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

}
