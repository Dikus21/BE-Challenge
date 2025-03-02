package com.aplikasi.challenge.entity;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_id_constraint"))
    @Schema(hidden = true)
    private Order order;

    @Transient
    @Schema(description = "Order ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID orderId;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "product_id_constraint"))
    @Schema(hidden = true)
    private Product product;

    @Transient
    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID productId;

    @Schema(description = "Quantity of the product", example = "2")
    private int quantity;

    @Column(name = "total_price", precision = 20, scale = 2)
    @Schema(description = "Total price of the product", example = "200")
    private BigDecimal totalPrice;
}
