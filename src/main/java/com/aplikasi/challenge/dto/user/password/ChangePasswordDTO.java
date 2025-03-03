package com.aplikasi.challenge.dto.user.password;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordDTO {
    @NotEmpty(message = "email is required")
    @Schema(description = "email", example = "user@mail.com")
    private String email;
    @NotEmpty(message = "new password required")
    @Schema(description = "new password", example = "Password123")
    private String newPassword;
    @NotEmpty(message = "confirm password required")
    @Schema(description = "confirm new password", example = "Password123")
    private String confirmPassword;
    @NotEmpty(message = "OTP is required")
    @Schema(description = "OTP", example = "123456")
    private String otp;
}
