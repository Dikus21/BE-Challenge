package com.aplikasi.challenge.dto.user.password;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class EmailValidationDTO {
    @NotEmpty(message = "email is required")
    @Schema(description = "email", example = "user@mail.com")
    private String email;
}
