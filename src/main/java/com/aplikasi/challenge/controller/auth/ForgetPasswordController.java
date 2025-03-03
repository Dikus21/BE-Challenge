package com.aplikasi.challenge.controller.auth;

import com.aplikasi.challenge.dto.user.password.ChangePasswordDTO;
import com.aplikasi.challenge.dto.user.password.EmailValidationDTO;
import com.aplikasi.challenge.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/forget-password/")
@Tag(name = "Reset Password", description = "Reset Password APIs")
public class ForgetPasswordController {

    @Autowired
    public UserService serviceReq;

    // Step 1 : Send OTP
    @PostMapping("")
    @Operation(summary = "Email Validation", description = "Email Validation")
    public ResponseEntity<Map> emailValidation(@Valid @RequestBody EmailValidationDTO request) {
        return new ResponseEntity<>(serviceReq.forgotPasswordRequest(request), HttpStatus.OK);
    }

    // Step 2 : change password
    @Transactional
    @PutMapping("/change-password")
    @Operation(summary = "Change Password", description = "New Password Validation and Confirmation")
    public ResponseEntity<Map> changePassword(@Valid @RequestBody ChangePasswordDTO request) {
        return new ResponseEntity<>(serviceReq.changePassword(request), HttpStatus.OK);
    }

    @PostMapping("/check-token/{request}")
    @Operation(summary = "Token Validation", description = "Forgot Password Token Validation")
    public ResponseEntity<Map> tokenValidation(@PathVariable(value = "request") String request) {
        return new ResponseEntity<>(serviceReq.checkOtpValidity(request), HttpStatus.OK);
    }

}
