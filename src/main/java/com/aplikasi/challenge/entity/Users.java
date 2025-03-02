package com.aplikasi.challenge.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@Where(clause = "deleted_date is null")
public class Users extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    @Schema(hidden = true)
    private UUID id;

    @Column(name = "username", length = 100)
    @Schema(description = "Username of the user", example = "user123")
    private String username;

    @Column(name = "email_address", length = 100)
    @Schema(description = "Email address of the user", example = "user@mail.com")
    private String emailAddress;

    @Schema(description = "Password of the user", example = "password123")
    private String password;


}
