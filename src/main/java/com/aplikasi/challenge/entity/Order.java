package com.aplikasi.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@AttributeOverride(name = "createdDate", column = @Column(name = "order_time", updatable = false))
@AttributeOverride(name = "deletedDate", column = @Column(name = "cancel_time"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "update_time"))
@Where(clause = "cancel_time is null")
public class Order extends AbstractDate implements Serializable {
    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private Long id;

    @Column(name = "destination_address")
    @Schema(description = "Destination address of the order", example = "Jl. Raya Bogor")
    private String destinationAddress;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_constraint"))
    private User user;

    @Transient
    @Schema(description = "User ID", example = "3")
    private Long userId;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Schema(hidden = true)
    private List<OrderDetail> orderDetails;

    @Schema(hidden = true)
    private boolean completed = false;
}
