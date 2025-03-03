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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Schema(hidden = true)
    private UUID id;

    @Column(name = "destination_address")
    @Schema(description = "Destination address of the order", example = "Jl. Raya Bogor")
    private String destinationAddress;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_constraint"))
    @Schema(hidden = true)
    private Users user;

    @Transient
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @Schema(description = "Status of the order", example = "true")
    private boolean completed = false;
}
