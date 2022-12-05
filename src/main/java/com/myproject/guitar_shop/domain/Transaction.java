package com.myproject.guitar_shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @Column(name = "id")
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "Date")
    private Date creationDate;

    @Column(name = "sum")
    private double sum;

    @OneToMany(mappedBy = "transactionId", fetch = FetchType.EAGER)
    private List<Item> items;
}
