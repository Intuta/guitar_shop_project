package com.myproject.guitar_shop.service;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.exception.ProductNotFoundException;
import com.myproject.guitar_shop.repository.ProductRepository;
import com.myproject.guitar_shop.utility.ErrorMessages;
import com.myproject.guitar_shop.utility.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService extends AppService<Product> {
    private final ProductRepository productRepository;
    public static final String SRC = "/images/%s.jpg";
    public static final String FILE_NAME = "/%s.jpg";
    public static final String BRAND = "brand";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String PRICE = "price";
    public static final String INFO = "info";
    public static final String QUANTITY = "quantity";

    @Autowired
    public ProductService(ProductRepository repository) {
        super(repository);
        this.productRepository = repository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public Page<Product> getProductsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAllByTitleContaining(title, pageable);
    }

    public List<Product> getAllProductsByCategory(Product.Category category) {
        List<Product> products = new ArrayList<>();
        productRepository.findAllByCategory(category).forEach(products::add);
        return products;
    }

    @Transactional
    public Product addNewProduct(Map<String, String> attributes, MultipartFile image) {
        String src = String.format(SRC, attributes.get(TITLE));
        Product product = Product.builder()
                .brand(attributes.get(BRAND).toUpperCase())
                .title(attributes.get(TITLE).toUpperCase())
                .category(Product.Category.valueOf(attributes.get(CATEGORY)))
                .price(Double.parseDouble(attributes.get(PRICE)))
                .info(attributes.get(INFO))
                .quantity(Integer.parseInt(attributes.get(QUANTITY)))
                .src(src)
                .build();
        ImageService.saveImageAs(String.format(FILE_NAME, attributes.get(TITLE)), image);
        return save(product);
    }

    @Transactional
    public Product update(Map<String, String> attributes, int productId) {
        Optional<Product> currentProduct = productRepository.findById(productId);
        currentProduct.ifPresent(product -> attributes.keySet().forEach(key -> {
            switch (key) {
                case BRAND:
                    product.setBrand(attributes.get(BRAND));
                    break;
                case TITLE:
                    product.setTitle(attributes.get(TITLE));
                    break;
                case CATEGORY:
                    product.setCategory(Product.Category.valueOf(attributes.get(CATEGORY)));
                    break;
                case PRICE:
                    product.setPrice(Double.parseDouble(attributes.get(PRICE)));
                    break;
                case INFO:
                    product.setInfo(attributes.get(INFO));
                    break;
                case QUANTITY:
                    product.setQuantity(Integer.parseInt(attributes.get(QUANTITY)));
                    break;
                default:
                    break;
            }
        }));
        return save(currentProduct.orElseThrow(() -> new ProductNotFoundException(ErrorMessages.PRODUCT_NOT_FOUND)));
    }

}
