package com.myproject.guitar_shop.integration;

import com.myproject.guitar_shop.GuitarShopApplication;
import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.exception.ProductNotFoundException;
import com.myproject.guitar_shop.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuitarShopApplication.class)
@Sql(scripts = "/test-schema.sql")
public class ProductServiceTests {
    @Autowired
    private ProductService productService;
    private Map<String, String> attributes;
    private MultipartFile image;

    @Before
    public void initialization() {
        attributes = new HashMap<>();
        attributes.put("brand", "brand");
        attributes.put("title", "title");
        attributes.put("category", "BASS_GUITAR");
        attributes.put("price", "300.0");
        attributes.put("info", "info");
        attributes.put("quantity", "3");

        image = new MockMultipartFile("image.jpg", "Some image".getBytes());
    }

    @Test
    public void addNewProductTest() {
        Product expectedProduct = Product.builder()
                .id(1)
                .brand("BRAND")
                .title("TITLE")
                .category(Product.Category.BASS_GUITAR)
                .price(300.0)
                .info("info")
                .quantity(3)
                .src(String.format("/images/%s.jpg", attributes.get("title")))
                .build();

        Product returnedProduct = productService.addNewProduct(attributes, image);

        assertThat(returnedProduct).isEqualTo(expectedProduct).usingRecursiveComparison();
    }

    @Test
    public void updateTest_Pass() {
        Product newProduct = Product.builder()
                .title("title")
                .category(Product.Category.ELECTRIC_GUITAR)
                .price(50.0)
                .quantity(2).build();
        newProduct = productService.save(newProduct);

        Product expectedProduct = Product.builder()
                .id(1)
                .brand("brand")
                .title("title")
                .category(Product.Category.BASS_GUITAR)
                .price(300.0)
                .info("info")
                .quantity(3)
                .build();

        Product returnedProduct = productService.update(attributes, newProduct.getId());

        assertThat(returnedProduct).isEqualTo(expectedProduct).usingRecursiveComparison();
    }

    @Test
    public void updateTest_Fall() {
        assertThatThrownBy(() -> productService.update(attributes, anyInt())).isInstanceOf(ProductNotFoundException.class);
    }

}
