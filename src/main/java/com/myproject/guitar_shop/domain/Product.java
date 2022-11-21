package com.myproject.guitar_shop.domain;

import com.myproject.guitar_shop.repository.utility.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "brand")
    private String brand;
    @Column(name = "title")
    private String title;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "price")
    private double price;
    @Column(name = "info")
    private String info;
    @Column(name = "quantity")
    private Integer quantity;
}
