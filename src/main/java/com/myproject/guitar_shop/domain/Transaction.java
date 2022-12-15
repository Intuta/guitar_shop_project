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
    @SequenceGenerator(name = "transaction_hibernate_sequence", sequenceName = "transaction_hibernate_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transaction) {
            Transaction other = (Transaction) obj;
            return other.getId().equals(id) && other.getCreationDate().equals(creationDate) && other.getSum() == sum;
        }
        return false;
    }
}
