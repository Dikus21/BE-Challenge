package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.MerchantRepository;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.ProductService;
import com.aplikasi.challenge.utils.TemplateResponse;
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
    public TemplateResponse templateResponse;
    @Autowired
    public MerchantRepository merchantRepository;

    @Override
    public Map<Object, Object> save(Product request) {
        try {
            log.info("Save New Product");
            if (request.getMerchant().getId() == null) return templateResponse.error("Merchant Id is Required");
            Optional<Merchant> checkDataDBMerchant = merchantRepository.findById(request.getMerchant().getId());
            if (!checkDataDBMerchant.isPresent()) return templateResponse.error("Merchant Not Found");
            if (request.getName().isEmpty()) return templateResponse.error("Name is required");
            if (request.getPrice() == null) return templateResponse.error("Price is required");

            log.info("Product Save Success");
            request.setMerchant(checkDataDBMerchant.get());
            return templateResponse.success(productRepository.save(request));
        } catch (Exception e) {
            log.error("Save Product Error: " + e.getMessage());
            return templateResponse.error("Save Product: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Product request) {
        try {
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(request.getId());
            if (!checkDataDBProduct.isPresent()) return templateResponse.error("Id is not registered");
            if (!request.getName().isEmpty()) checkDataDBProduct.get().setName(request.getName());
            if (request.getPrice() != null) checkDataDBProduct.get().setPrice(request.getPrice());

            log.info("Update Product Success");
            return templateResponse.success(productRepository.save(checkDataDBProduct.get()));
        } catch (Exception e) {
            log.error("Update Product Error: " + e.getMessage());
            return templateResponse.error("Update Product: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Product request) {
        try {
            log.info("Delete Product");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(request.getId());
            if (!checkDataDBProduct.isPresent()) return templateResponse.error("Product not Found");

            log.info("Product Deleted");
            checkDataDBProduct.get().setDeletedDate(new Date());
            return templateResponse.success(productRepository.save(checkDataDBProduct.get()));
        } catch (Exception e) {
            log.error("Delete Product Error: " + e.getMessage());
            return templateResponse.error("Delete Product : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(Long id) {
        try {
            log.info("Get Product");
            if (id == null) return templateResponse.error("Id is required");
            Optional<Product> checkDataDBProduct = productRepository.findById(id);
            if (!checkDataDBProduct.isPresent()) return templateResponse.error("Product not Found");

            log.info("Product Found");
            return templateResponse.success(checkDataDBProduct.get());
        } catch (Exception e) {
            log.error("Get Product Error: " + e.getMessage());
            return templateResponse.error("Get Product: " + e.getMessage());
        }
    }
}
