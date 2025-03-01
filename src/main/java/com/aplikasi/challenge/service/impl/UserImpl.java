package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.repository.UserRepository;
import com.aplikasi.challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserImpl implements UserService {
    @Autowired
    public UserRepository userRepository;

    @Override
    public Map<Object, Object> save(Users user) {
        Map<Object, Object> map = new HashMap<>();
        Users doSave = userRepository.save(user);
        map.put("data", doSave);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    @Override
    public Map<Object, Object> update(Users user) {
        Map<Object, Object> map = new HashMap<>();
        Users checkData = userRepository.getById(user.getId());
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
//        userRepository.updateData(user);
//        Users displayUser = userRepository.getById(user.getId());
        checkData.setPassword(user.getPassword());
        checkData.setEmailAddress(user.getEmailAddress());
        checkData.setUsername(user.getUsername());
        Users doUpdate = userRepository.save(checkData);
        map.put("data", doUpdate);
        return map;
    }

    @Override
    public Map<Object, Object> delete(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Users checkData = userRepository.getById(uuid);
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setDeletedDate(new Date());
        Users doDelete = userRepository.save(checkData);
        map.put("data", doDelete);
        return map;
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Users findUser = userRepository.getById(uuid);
        if(findUser == null) {
            map.put("message", "data not found");
            return map;
        }
        map.put("data", findUser);
        return map;
    }
}
