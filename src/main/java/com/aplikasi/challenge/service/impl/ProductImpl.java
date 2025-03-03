package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.MerchantRepository;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.ProductService;
import com.aplikasi.challenge.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductImpl implements ProductService {
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public Response response;
    @Autowired
    public MerchantRepository merchantRepository;

    @Override
    public Map<Object, Object> save(Product request) {
        try {
            log.info("Save New Product");
            if (request.getMerchant().getId() == null) return response.error("Merchant Id is Required");
            Optional<Merchant> checkDataDBMerchant = merchantRepository.findById(request.getMerchant().getId());
            if (!checkDataDBMerchant.isPresent()) return response.error("Merchant Not Found");
            if (request.getName().isEmpty()) return response.error("Name is required");
            if (request.getPrice() == null) return response.error("Price is required");

            log.info("Product Save Success");
            request.setMerchant(checkDataDBMerchant.get());
            return response.success(productRepository.save(request));
        } catch (Exception e) {
            log.error("Save Product Error: " + e.getMessage());
            return response.error("Save Product: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Product request) {
        try {
            if (request.getId() == null) return response.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(request.getId());
            if (!checkDataDBProduct.isPresent()) return response.error("Id is not registered");
            if (!request.getName().isEmpty()) checkDataDBProduct.get().setName(request.getName());
            if (request.getPrice() != null) checkDataDBProduct.get().setPrice(request.getPrice());

            log.info("Update Product Success");
            return response.success(productRepository.save(checkDataDBProduct.get()));
        } catch (Exception e) {
            log.error("Update Product Error: " + e.getMessage());
            return response.error("Update Product: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Product request) {
        try {
            log.info("Delete Product");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(request.getId());
            if (!checkDataDBProduct.isPresent()) return response.error("Product not Found");

            log.info("Product Deleted");
            checkDataDBProduct.get().setDeletedDate(new Date());
            return response.success(productRepository.save(checkDataDBProduct.get()));
        } catch (Exception e) {
            log.error("Delete Product Error: " + e.getMessage());
            return response.error("Delete Product : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        try {
            log.info("Get Product");
            if (uuid == null) return response.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(uuid);
            if (!checkDataDBProduct.isPresent()) return response.error("Product not Found");

            log.info("Product Found");
            return response.success(checkDataDBProduct.get());
        } catch (Exception e) {
            log.error("Get Product Error: " + e.getMessage());
            return response.error("Get Product: " + e.getMessage());
        }
    }
}
