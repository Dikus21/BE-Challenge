package com.aplikasi.challenge.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
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
    @Column(name = "id")
    @Schema(hidden = true)
    private UUID id;

    @Column(name = "merchant_name", length = 150)
    @Schema(description = "Name of the merchant", example = "merchant123")
    private String name;

    @NotNull(message = "This field cannot be null!")
    @Column(name = "merchant_location")
    @Schema(description = "Location of the merchant", example = "Jl. Raya Bogor")
    private String location;

    @Schema(description = "Open status of the merchant", example = "true")
    private boolean open = true;

    @JsonIgnore
    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;
}
