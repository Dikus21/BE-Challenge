package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.repository.UserRepository;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.utils.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseTemplate<Users> save(Users user) {
        return new ResponseTemplate<>(200, "success", userRepository.save(user));
    }

    @Override
    public ResponseTemplate<Users> update(Users user) {
        Users checkData = userRepository.findById(user.getId()).orElse(null);
        if(checkData == null) {
            return new ResponseTemplate<>(404, "Data not found", null);
        }
        checkData.setPassword(user.getPassword());
        checkData.setEmailAddress(user.getEmailAddress());
        checkData.setUsername(user.getUsername());
        return new ResponseTemplate<>(200, "success", userRepository.save(checkData));
    }

    @Override
    public ResponseTemplate<Users> delete(UUID uuid) {
        Users checkData = userRepository.findById(uuid).orElse(null);
        if(checkData == null) {
            return new ResponseTemplate<>(404, "Data not found", null);
        }
        checkData.setDeletedDate(new Date());
        return new ResponseTemplate<>(200, "success", userRepository.save(checkData));
    }

    @Override
    public ResponseTemplate<Users> getById(UUID uuid) {
        Users findUser = userRepository.findById(uuid).orElse(null);
        if(findUser == null) {
            return new ResponseTemplate<>(404, "Data not found", null);
        }
        return new ResponseTemplate<>(200, "success", findUser);
    }
}
