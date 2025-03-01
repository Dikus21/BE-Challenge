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
@Table(name = "order_detail")
@Where(clause = "deleted_date is null")
public class OrderDetail extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_id_constraint"))
    private Orders order;


    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_id_constraint"))
    private Product product;

    @Column(precision = 3)
    private BigDecimal quantity;

    @Column(name = "total_price", precision = 20, scale = 2)
    private BigDecimal totalPrice;
}
