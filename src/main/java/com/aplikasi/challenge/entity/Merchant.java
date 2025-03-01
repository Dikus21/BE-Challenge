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
@Table(name = "merchant")
@Where(clause = "deleted_date is null")
public class Merchant extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "merchant_name", length = 150)
    private String name;

    @NotNull(message = "This field cannot be null!")
    @Column(name = "merchant_location")
    private String location;

    private boolean open;

    public boolean getOpen(){
        return this.open;
    }
}
