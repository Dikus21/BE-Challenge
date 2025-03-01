package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductImpl implements ProductService {
    @Autowired
    public ProductRepository productRepository;

    @Override
    public Map<Object, Object> save(Product product) {
        Map<Object, Object> map = new HashMap<>();
        Product doSave = productRepository.save(product);
        map.put("data", doSave);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    @Override
    public Map<Object, Object> update(Product product) {
        Map<Object, Object> map = new HashMap<>();
        Product checkData = productRepository.getById(product.getId());
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setName(product.getName());
        checkData.setPrice(product.getPrice());
        Product doUpdate = productRepository.save(checkData);
        map.put("data", doUpdate);
        return map;
    }

    @Override
    public Map<Object, Object> delete(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Product checkData = productRepository.getById(uuid);
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setDeletedDate(new Date());
        Product doDelete = productRepository.save(checkData);
        map.put("data", doDelete);
        return map;
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Product findUser = productRepository.getById(uuid);
        if(findUser == null) {
            map.put("message", "data not found");
            return map;
        }
        map.put("data", findUser);
        return map;
    }
}
