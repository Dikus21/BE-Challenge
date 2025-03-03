package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.entity.OrderDetail;
import com.aplikasi.challenge.entity.Order;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.OrderDetailRepository;
import com.aplikasi.challenge.repository.OrderRepository;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.OrderDetailService;
import com.aplikasi.challenge.utils.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderDetailImpl implements OrderDetailService {
    @Autowired
    public OrderDetailRepository orderDetailRepository;
    @Autowired
    public TemplateResponse templateResponse;
    @Autowired
    public OrderRepository orderRepository;
    @Autowired
    public ProductRepository productRepository;

    @Override
    public Map<Object, Object> save(OrderDetail request) {
        try {
            log.info("Save New Order Detail");
            if (request.getOrder().getId() == null) return templateResponse.error("Order Id is Required");
            Optional<Order> checkDataDBOrder = orderRepository.findById(request.getOrder().getId());
            if (!checkDataDBOrder.isPresent()) return templateResponse.error("Order Not Found");
            request.setOrder(checkDataDBOrder.get());
            Optional<Product> checkDataDBProduct = productRepository.findById(request.getProduct().getId());
            if (!checkDataDBProduct.isPresent()) return templateResponse.error("Product Not Found");
            request.setProduct(checkDataDBProduct.get());
            if (request.getQuantity() == 0) return templateResponse.error("Quantity is required");
            request.setTotalPrice(request.getProduct().getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

            log.info("Order Detail Save Success");
            return templateResponse.success(orderDetailRepository.save(request));
        } catch (Exception e) {
            log.error("Save Order Detail Error: " + e.getMessage());
            return templateResponse.error("Save Order Detail: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> update(OrderDetail request) {
        try {
            log.info("Update Order Detail");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<OrderDetail> checkDataDBOrderDetail = orderDetailRepository.findById(request.getId());
            if (!checkDataDBOrderDetail.isPresent()) return templateResponse.error("Order Detail not Found");
            if (request.getQuantity() == 0) delete(checkDataDBOrderDetail.get());
            checkDataDBOrderDetail.get().setTotalPrice(request.getProduct().getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));


            log.info("Update Order Detail Success");
            return templateResponse.success(orderDetailRepository.save(checkDataDBOrderDetail.get()));
        } catch (Exception e) {
            log.error("Update Order Detail Error: " + e.getMessage());
            return templateResponse.error("Update Order Detail: " + e.getMessage());
        }
    }

    @Override
    public Map<Object, Object> delete(OrderDetail request) {
        try {
            log.info("Delete Order Detail");
            if (request.getId() == null) return templateResponse.error("Id is required");
            Optional<OrderDetail> checkDataDBOrderDetail = orderDetailRepository.findById(request.getId());
            if (!checkDataDBOrderDetail.isPresent()) return templateResponse.error("Order not Found");

            log.info("Order Deleted");
            checkDataDBOrderDetail.get().setDeletedDate(new Date());
            return templateResponse.success(orderDetailRepository.save(checkDataDBOrderDetail.get()));
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
            Optional<OrderDetail> checkDataDBOrderDetail = orderDetailRepository.findById(id);
            if (!checkDataDBOrderDetail.isPresent()) return templateResponse.error("Order not Found");

            log.info("Order Found");
            return templateResponse.success(checkDataDBOrderDetail.get());
        } catch (Exception e) {
            log.error("Get Order Error: " + e.getMessage());
            return templateResponse.error("Get Order: " + e.getMessage());
        }
    }

//    @Override
//    public BigDecimal totalPrice(OrderDetail orderDetail) {
//        return orderDetail.getProduct().getPrice().multiply(orderDetail.getQuantity());
//    }


}
