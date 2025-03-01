package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Users;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    Map<Object, Object> save(Users user);

    Map<Object, Object> update(Users user);

    Map<Object, Object> delete(UUID uuid);

    Map<Object, Object> getById(UUID uuid);
}
