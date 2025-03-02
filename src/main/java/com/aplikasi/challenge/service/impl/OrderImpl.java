package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.Order;
import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.repository.OrderRepository;
import com.aplikasi.challenge.repository.UserRepository;
import com.aplikasi.challenge.service.OrderService;
import com.aplikasi.challenge.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrderImpl implements OrderService {
    @Autowired
    public OrderRepository orderRepository;
    @Autowired
    public Response response;
    @Autowired
    public UserRepository userRepository;

    @Override
    public Map<Object, Object> save(Order request) {
        try {
            log.info("Save New Order");
            if (request.getUser().getId() == null) return response.error("User Id is Required");
            Optional<Users> checkDataDBUser = userRepository.findById(request.getUser().getId());
            if (!checkDataDBUser.isPresent()) return response.error("User Not Found");
            if (request.getDestinationAddress().isEmpty()) return response.error("Destination Address is required");

            log.info("Order Save Success");
            request.setUser(checkDataDBUser.get());
            return response.success(orderRepository.save(request));
        } catch (Exception e) {
            log.error("Save Order Error: " + e.getMessage());
            return response.error("Save Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(Order request) {
        try {
            log.info("Update Order");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return response.error("Id is not registered");
            if (!request.getDestinationAddress().isEmpty()) checkDataDBOrder.get().setDestinationAddress(request.getDestinationAddress());
            checkDataDBOrder.get().setCompleted(request.isCompleted());

            log.info("Update Order Success");
            return response.success(orderRepository.save(checkDataDBOrder.get()));
        } catch (Exception e) {
            log.error("Update Order Error: " + e.getMessage());
            return response.error("Update Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(Order request) {
        try {
            log.info("Delete Order");
            if (request.getId() == null) return response.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return response.error("Order not Found");

            log.info("Order Deleted");
            checkDataDBOrder.get().setDeletedDate(new Date());
            return response.success(orderRepository.save(checkDataDBOrder.get()));
        } catch (Exception e) {
            log.error("Delete Order Error: " + e.getMessage());
            return response.error("Delete Order : " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        try {
            log.info("Get Order");
            if (uuid == null) return response.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(uuid);
            if (!checkDataDBOrder.isPresent()) return response.error("Order not Found");

            log.info("Order Found");
            return response.success(checkDataDBOrder.get());
        } catch (Exception e) {
            log.error("Get Order Error: " + e.getMessage());
            return response.error("Get Order: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> getOrderDetailList(Order request) {
        try {
            log.info("Get Order Detail List");
            if (request.getId() ==  null) return response.error("Id is required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getId());
            if (!checkDataDBOrder.isPresent()) return response.error("Order not Found");

            log.info("Get Orders Detail List Success");
            return response.success(checkDataDBOrder.get().getOrderDetails());
        } catch (Exception e) {
            log.error("Get Order Detail List Error: " + e.getMessage());
            return response.error("Get Order Detail List: " + e.getMessage());
        }
    }
}
