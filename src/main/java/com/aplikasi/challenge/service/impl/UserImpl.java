package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.repository.UserRepository;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.utils.PasswordValidatorUtil;
import com.aplikasi.challenge.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserImpl implements UserService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public Response response;
    public PasswordValidatorUtil passwordValidatorUtil = new PasswordValidatorUtil();

    @Override
    public Map<Object, Object> save(Users request) {
        try {
            log.info("Save New User");
            if (request.getUsername().isEmpty()) return response.error("Username is required");
            if (userRepository.getByUsername(request.getUsername()) > 0) return response.error("Username Used");

            if (request.getEmailAddress().isEmpty()) return response.error("Email Address is required");
            if (userRepository.getByEmailAddress(request.getEmailAddress()) > 0) return response.error("Email Address is already Registered");

            if (request.getPassword().isEmpty()) return response.error("Password is required");
            if (!passwordValidatorUtil.validatePassword(request.getPassword())) {
                return response.error(passwordValidatorUtil.getMessage());
            }

            log.info("User Save Success");
            return response.success(userRepository.save(request));
        } catch (Exception e) {
            log.error("Save User Error: " + e.getMessage());
            return response.error("Save User: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Users request) {
        try {
            log.info("Update User");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Users> checkDataDBUser = userRepository.findById(request.getId());
            if (!checkDataDBUser.isPresent()) return response.error("Id is not Registered");
            if (!request.getUsername().isEmpty()) {
                if (userRepository.getByUsername(request.getUsername()) > 0) return response.error("Username Exist, try other username");
                checkDataDBUser.get().setUsername(request.getUsername());
            }
            if (!request.getEmailAddress().isEmpty()) {
                if (userRepository.getByEmailAddress(request.getEmailAddress()) > 0) return response.error("Email Address is already Registered");
                checkDataDBUser.get().setEmailAddress(request.getEmailAddress());
            }

            log.info("Update User Success");
            return response.success(userRepository.save(checkDataDBUser.get()));
        } catch (Exception e) {
            log.error("Update User Error: " + e.getMessage());
            return response.error("Update User: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Users request) {
        try {
            log.info("Delete User");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Users> checkDataDBUser = userRepository.findById(request.getId());
            if (!checkDataDBUser.isPresent()) return response.error("User not Found");

            log.info("User Deleted");
            checkDataDBUser.get().setDeletedDate(new Date());
            return response.success(userRepository.save(checkDataDBUser.get()));
        } catch (Exception e) {
            log.error("Delete User Error: " + e.getMessage());
            return response.error("Delete User : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(UUID id) {
        try {
            log.info("Get User");
            if (id == null) return response.error("Id is required");
            Optional<Users> checkDataDBUser = userRepository.findById(id);
            if (!checkDataDBUser.isPresent()) return response.error("User not Found");

            log.info("User Found");
            return response.success(checkDataDBUser.get());
        } catch (Exception e) {
            log.error("Get User Error: " + e.getMessage());
            return response.error("Get User: " + e.getMessage());
        }
    }
}
