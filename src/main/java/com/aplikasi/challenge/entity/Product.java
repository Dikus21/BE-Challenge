package com.aplikasi.challenge.entity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "product")
@Where(clause = "deleted_date is null")
public class Product extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "product_name", length = 100)
    private String name;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "merchant_id", foreignKey = @ForeignKey(name = "merchant_id_constraint"))
    private Merchant merchant;
}
