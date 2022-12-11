package com.myproject.guitar_shop.unit;

import com.myproject.guitar_shop.domain.Product;
import com.myproject.guitar_shop.repository.ProductRepository;
import com.myproject.guitar_shop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void getByIdTest_Pass() throws Exception {
        int productId = 1;
        Product product = Product.builder()
                .id(productId)
                .title("title")
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product returnedProduct = productService.getById(productId);

        assertThat(returnedProduct).isEqualTo(product).usingRecursiveComparison();
    }

    @Test
    public void getByIdTest_Fail() {
        int productId = 1;

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

//    @Test
//    public void getProductByTitleTest_Pass() {
//        String title = "name";
//        Product product = Product.builder()
//                .id(1)
//                .title(title)
//                .build();
//
//        when(productRepository.findByTitle(title)).thenReturn(Optional.of(product));
//
//        Product returnedProduct = productService.getProductByTitle(title);
//
//        assertThat(returnedProduct).isEqualTo(product).usingRecursiveComparison();
//    }
//
//    @Test
//    public void getProductByTitleTest_Fail() {
//        String title = "name";
//
//        when(productRepository.findByTitle(any())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> productService.getProductByTitle(title)).isInstanceOf(NoSuchElementException.class);
//    }

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
    public void createProductTest() throws Exception {
        Product product = Product.builder()
                .id(1)
                .title("title")
                .build();

        when(productRepository.save(product)).thenReturn(product);

        Product returnedProduct = productService.save(product);

        assertThat(returnedProduct).isEqualTo(product).usingRecursiveComparison();
    }

}
