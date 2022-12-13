package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.ProductRepository;
import com.myproject.guitar_shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    @Captor
    ArgumentCaptor<Product> captor;
    private Product product;
    private Map<String, String> attributes;

    @BeforeEach
    public void initialization() {
        product = Product.builder()
                .id(1)
                .title("title")
                .build();

        attributes = new HashMap<>();
        attributes.put("brand", "brand");
        attributes.put("title", "title");
        attributes.put("category", "BASS_GUITAR");
        attributes.put("price", "300.0");
        attributes.put("info", "info");
        attributes.put("quantity", "3");
    }

    @Test
    public void getByIdTest_Pass() {
        int productId = product.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product returnedProduct = productService.getById(productId);

        assertThat(returnedProduct).isEqualTo(product).usingRecursiveComparison();
    }

    @Test
    public void getByIdTest_Fail() {
        int productId = product.getId();
        ;

        when(productRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getById(productId)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void getAllProductsTest() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            products.add(Product.builder()
                    .id(i)
                    .title("title" + i)
                    .build());
        }

        when(productRepository.findAll()).thenReturn(products);

        List<Product> returnedProducts = productService.getAllProducts();

        assertThat(returnedProducts).isEqualTo(products).usingRecursiveComparison();
    }

    @Test
    public void getProductsByTitleTest() {
        List<Product> products = new ArrayList<>();
        String title = "title";
        for (int i = 0; i < 3; i++) {
            products.add(Product.builder()
                    .id(i)
                    .title(title + i)
                    .build());
        }

        when(productRepository.findAllByTitleContaining(title)).thenReturn(products);

        List<Product> returnedProducts = productService.getProductsByTitle(title);

        assertThat(returnedProducts).isEqualTo(products).usingRecursiveComparison();
    }

    @Test
    public void getAllProductsByCategoryTest() {
        Product.Category category = Product.Category.valueOf("ACOUSTIC_GUITAR");
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            products.add(Product.builder()
                    .id(i)
                    .title("title" + i)
                    .category(category)
                    .build());
        }

        when(productRepository.findAllByCategory(category)).thenReturn(products);

        List<Product> returnedProducts = productService.getAllProductsByCategory(category);

        assertThat(returnedProducts).isEqualTo(products).usingRecursiveComparison();
    }

    @Test
    public void updateTest() {
        Product expectedProduct = Product.builder()
                .id(product.getId())
                .brand(attributes.get("brand"))
                .title(attributes.get("title"))
                .category(Product.Category.valueOf(attributes.get("category")))
                .price(Double.parseDouble(attributes.get("price")))
                .info(attributes.get("info"))
                .quantity(Integer.parseInt(attributes.get("quantity")))
                .build();

        when(productRepository.findById(expectedProduct.getId())).thenReturn(Optional.of(Product.builder().id(expectedProduct.getId()).build()));
        when(productRepository.save(captor.capture())).thenReturn(expectedProduct);

        Product returnedProduct = productService.update(attributes, expectedProduct.getId());
        Product capturedProduct = captor.getValue();

        assertThat(returnedProduct).isEqualTo(expectedProduct).usingRecursiveComparison();
        assertThat(capturedProduct).isEqualTo(expectedProduct).usingRecursiveComparison();
    }

}
