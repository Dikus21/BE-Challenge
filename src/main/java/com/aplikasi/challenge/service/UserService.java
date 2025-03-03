package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Users;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    Map<Object, Object> save(Users request);

    Map<Object, Object> update(Users request);

    Map<Object, Object> delete(Users request);

    Map<Object, Object> getById(UUID id);
}
