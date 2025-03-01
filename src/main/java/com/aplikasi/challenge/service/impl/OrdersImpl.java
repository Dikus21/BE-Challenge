package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Orders;
import com.aplikasi.challenge.repository.OrdersRepository;
import com.aplikasi.challenge.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrdersImpl implements OrdersService {
    @Autowired
    public OrdersRepository ordersRepository;

    @Override
    public Map<Object, Object> save(Orders orders) {
        Map<Object, Object> map = new HashMap<>();
        Orders doSave = ordersRepository.save(orders);
        map.put("data", doSave);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    @Override
    public Map<Object, Object> update(Orders orders) {
        Map<Object, Object> map = new HashMap<>();
        Orders checkData = ordersRepository.getById(orders.getId());
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setCompleted(orders.getCompleted());
        checkData.setDestinationAddress(orders.getDestinationAddress());
        Orders doUpdate = ordersRepository.save(checkData);
        map.put("data", doUpdate);
        return map;
    }

    @Override
    public Map<Object, Object> delete(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Orders checkData = ordersRepository.getById(uuid);
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setDeletedDate(new Date());
        Orders doDelete = ordersRepository.save(checkData);
        map.put("data", doDelete);
        return map;
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Orders findUser = ordersRepository.getById(uuid);
        if(findUser == null) {
            map.put("message", "data not found");
            return map;
        }
        map.put("data", findUser);
        return map;
    }
}
