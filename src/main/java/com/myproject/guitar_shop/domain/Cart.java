package com.myproject.guitar_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @SequenceGenerator(name = "cart_hibernate_sequence", sequenceName = "cart_hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "sum")
    private Double sum;

    @OneToMany(mappedBy = "cartId", fetch = FetchType.EAGER)
    private List<Item> items;
}
