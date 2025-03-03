package com.aplikasi.challenge.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "product")
@Where(clause = "deleted_date is null")
public class Product extends AbstractDate implements Serializable {
    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "product_name", length = 100)
    @Schema(description = "Name of the product", example = "product123")
    private String name;

    @Column(precision = 12, scale = 2)
    @Schema(description = "Price of the product", example = "100")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "merchant_id", foreignKey = @ForeignKey(name = "merchant_id_constraint"))
    @Schema(hidden = true)
    private Merchant merchant;

    @Transient
    @Schema(description = "Merchant ID", example = "1")
    private Long merchantId;

    @JsonIgnore
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
