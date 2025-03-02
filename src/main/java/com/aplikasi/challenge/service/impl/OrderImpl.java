package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Order;
import com.aplikasi.challenge.entity.User;
import com.aplikasi.challenge.repository.OrderRepository;
import com.aplikasi.challenge.repository.oauth.UserRepository;
import com.aplikasi.challenge.service.OrderService;
import com.aplikasi.challenge.utils.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderImpl implements OrderService {
    @Autowired
    public OrderRepository orderRepository;
    @Autowired
    public TemplateResponse templateResponse;
    @Autowired
    public UserRepository userRepository;

    @Override
    public Map<Object, Object> save(Order request) {
        try {
            log.info("Save New Order");
            if (request.getUser().getId() == null) return templateResponse.error("User Id is Required");
            Optional<User> checkDataDBUser = userRepository.findById(request.getUser().getId());
            if (!checkDataDBUser.isPresent()) return templateResponse.error("User Not Found");
            if (request.getDestinationAddress().isEmpty()) return templateResponse.error("Destination Address is required");

            log.info("Order Save Success");
            request.setUser(checkDataDBUser.get());
            return templateResponse.success(orderRepository.save(request));
        } catch (Exception e) {
            log.error("Save Order Error: " + e.getMessage());
            return templateResponse.error("Save Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Order request) {
        try {
            log.info("Update Order");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return templateResponse.error("Id is not registered");
            if (!request.getDestinationAddress().isEmpty()) checkDataDBOrder.get().setDestinationAddress(request.getDestinationAddress());
            checkDataDBOrder.get().setCompleted(request.isCompleted());

            log.info("Update Order Success");
            return templateResponse.success(orderRepository.save(checkDataDBOrder.get()));
        } catch (Exception e) {
            log.error("Update Order Error: " + e.getMessage());
            return templateResponse.error("Update Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Order request) {
        try {
            log.info("Delete Order");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return templateResponse.error("Order not Found");

            log.info("Order Deleted");
            checkDataDBOrder.get().setDeletedDate(new Date());
            return templateResponse.success(orderRepository.save(checkDataDBOrder.get()));
        } catch (Exception e) {
            log.error("Delete Order Error: " + e.getMessage());
            return templateResponse.error("Delete Order : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(Long id) {
        try {
            log.info("Get Order");
            if (id == null) return templateResponse.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(id);
            if (!checkDataDBOrder.isPresent()) return templateResponse.error("Order not Found");

            log.info("Order Found");
            return templateResponse.success(checkDataDBOrder.get());
        } catch (Exception e) {
            log.error("Get Order Error: " + e.getMessage());
            return templateResponse.error("Get Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getOrderDetailList(Order request) {
        try {
            log.info("Get Order Detail List");
            if (request.getId() ==  null) return templateResponse.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return templateResponse.error("Order not Found");

            log.info("Get Orders Detail List Success");
            return templateResponse.success(checkDataDBOrder.get().getOrderDetails());
        } catch (Exception e) {
            log.error("Get Order Detail List Error: " + e.getMessage());
            return templateResponse.error("Get Order Detail List: " + e.getMessage());
        }
    }
}
