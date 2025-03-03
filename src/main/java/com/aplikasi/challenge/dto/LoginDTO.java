package com.aplikasi.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class LoginDTO {
    @NotEmpty(message = "username is required.")
    @Schema(description = "username", example = "user@mail.com")
    private String username;
    @NotEmpty(message = "password is required.")
    @Schema(description = "password", example = "password")
    private String password;
}
