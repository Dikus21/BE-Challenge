package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.repository.MerchantRepository;
import com.aplikasi.challenge.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MerchantImpl implements MerchantService {

    @Autowired
    public MerchantRepository merchantRepository;

    @Override
    public Map<Object, Object> save(Merchant merchant) {
        Map<Object, Object> map = new HashMap<>();
        Merchant doSave = merchantRepository.save(merchant);
        map.put("data", doSave);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    @Override
    public Map<Object, Object> update(Merchant merchant) {
        Map<Object, Object> map = new HashMap<>();
        Merchant checkData = merchantRepository.getById(merchant.getId());
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setName(merchant.getName());
        checkData.setOpen(merchant.isOpen());
        checkData.setLocation(merchant.getLocation());
        Merchant doUpdate = merchantRepository.save(checkData);
        map.put("data", doUpdate);
        return map;
    }

    @Override
    public Map<Object, Object> delete(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Merchant checkData = merchantRepository.getById(uuid);
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setDeletedDate(new Date());
        Merchant doDelete = merchantRepository.save(checkData);
        map.put("data", doDelete);
        return map;
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        Merchant findUser = merchantRepository.getById(uuid);
        if(findUser == null) {
            map.put("message", "data not found");
            return map;
        }
        map.put("data", findUser);
        return map;
    }
}
