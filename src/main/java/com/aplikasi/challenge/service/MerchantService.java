package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.Merchant;

import java.util.Map;
import java.util.UUID;

public interface MerchantService {
    Map<Object, Object> save(Merchant merchant);

    Map<Object, Object> update(Merchant merchant);

    Map<Object, Object> delete(UUID uuid);

    Map<Object, Object> getById(UUID uuid);
}
