package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.utils.ResponseTemplate;

import java.util.Map;
import java.util.UUID;

public interface UserService {
    ResponseTemplate<Users> save(Users user);

    ResponseTemplate<Users> update(Users user);

    ResponseTemplate<Users> delete(UUID uuid);

    ResponseTemplate<Users> getById(UUID uuid);
}
