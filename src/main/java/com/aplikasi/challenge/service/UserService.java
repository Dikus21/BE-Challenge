package com.aplikasi.challenge.service;

import com.aplikasi.challenge.dto.user.password.ChangePasswordDTO;
import com.aplikasi.challenge.dto.user.password.EmailValidationDTO;
import com.aplikasi.challenge.entity.User;
import com.aplikasi.challenge.dto.LoginDTO;
import com.aplikasi.challenge.dto.RegisterDTO;

import java.util.Map;

public interface UserService {
    Map registerManual(RegisterDTO objModel) ;
    Map registerMerchant (User request);
    Map registerByGoogle(RegisterDTO objModel);
    public Map login(LoginDTO objLogin);
    Map<Object, Object> getById(Long id);
    Map<Object, Object> delete(User request);
    Map<Object, Object> update(User request);

    Map<Object, Object> forgotPasswordRequest(EmailValidationDTO request);
    Map<Object, Object> changePassword(ChangePasswordDTO request);
    Map<Object, Object> checkOtpValidity(String request);
}

