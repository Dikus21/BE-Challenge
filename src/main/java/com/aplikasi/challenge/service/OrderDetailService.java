package com.aplikasi.challenge.service;

import com.aplikasi.challenge.entity.OrderDetail;

import java.util.Map;
import java.util.UUID;

public interface OrderDetailService {
    Map<Object, Object> save(OrderDetail orderDetail);

    Map<Object, Object> update(OrderDetail orderDetail);

    Map<Object, Object> delete(UUID uuid);

    Map<Object, Object> getById(UUID uuid);

//    BigDecimal totalPrice(OrderDetail orderDetail);
}
