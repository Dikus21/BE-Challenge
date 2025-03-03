package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Product;

import java.util.Map;
import java.util.UUID;

public interface ProductService {
    Map<Object, Object> save(Product product);

    Map<Object, Object> update(Product product);

    Map<Object, Object> delete(UUID uuid);

    Map<Object, Object> getById(UUID uuid);
}
