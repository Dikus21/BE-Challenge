package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.OrderDetail;
import com.aplikasi.challenge.entity.Orders;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.OrderDetailRepository;
import com.aplikasi.challenge.repository.OrdersRepository;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderDetailImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrdersRepository ordersRepository;

    private final ProductRepository productRepository;

    @Autowired
    public OrderDetailImpl(OrderDetailRepository orderDetailRepository, OrdersRepository ordersRepository, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Map<Object, Object> save(OrderDetail orderDetail) {
        Orders order = ordersRepository.findById(orderDetail.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(orderDetail.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        Map<Object, Object> map = new HashMap<>();
        OrderDetail doSave = orderDetailRepository.save(orderDetail);
        map.put("data", doSave);
        map.put("message", "success");
        map.put("code", 200);
        return map;
    }

    @Override
    public Map<Object, Object> update(OrderDetail orderDetail) {
        Map<Object, Object> map = new HashMap<>();
        OrderDetail checkData = orderDetailRepository.getById(orderDetail.getId());
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setProduct(orderDetail.getProduct());
        checkData.setQuantity(orderDetail.getQuantity());
        checkData.setTotalPrice(orderDetail.getTotalPrice());
        OrderDetail doUpdate = orderDetailRepository.save(checkData);
        map.put("data", doUpdate);
        return map;
    }

    @Override
    public Map<Object, Object> delete(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        OrderDetail checkData = orderDetailRepository.getById(uuid);
        if(checkData == null) {
            map.put("message", "data not found");
            return map;
        }
        checkData.setDeletedDate(new Date());
        OrderDetail doDelete = orderDetailRepository.save(checkData);
        map.put("data", doDelete);
        return map;
    }

    @Override
    public Map<Object, Object> getById(UUID uuid) {
        Map<Object, Object> map = new HashMap<>();
        OrderDetail findUser = orderDetailRepository.getById(uuid);
        if(findUser == null) {
            map.put("message", "data not found");
            return map;
        }
        map.put("data", findUser);
        return map;
    }

//    @Override
//    public BigDecimal totalPrice(OrderDetail orderDetail) {
//        return orderDetail.getProduct().getPrice().multiply(orderDetail.getQuantity());
//    }


}
