package com.aplikasi.challenge.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@AttributeOverride(name = "createdDate", column = @Column(name = "order_time", updatable = false))
@AttributeOverride(name = "deletedDate", column = @Column(name = "cancel_time"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "update_time"))
@Where(clause = "cancel_time is null")
public class Orders extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "destination_address")
    private String destinationAddress;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_constraint"))
    private Users user;

    @NotNull(message = "This field cannot be null!")
    private boolean completed = false;

    public boolean getCompleted(){
        return this.completed;
    }

}
