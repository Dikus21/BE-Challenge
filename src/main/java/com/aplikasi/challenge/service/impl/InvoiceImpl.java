package com.aplikasi.challenge.service.impl;

import com.aplikasi.challenge.dto.OrderDetailDTO;
import com.aplikasi.challenge.entity.Order;
import com.aplikasi.challenge.repository.OrderRepository;
import com.aplikasi.challenge.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvoiceImpl implements InvoiceService {
    public final String reportPath = "D:\\Andika\\BootCamp(SYNERGY)\\Project\\Challenge\\Challenge_3\\report\\Invoice.jrxml";
    @Autowired
    public OrderRepository orderRepository;
    @Override
    public byte[] generateInvoice(Order request) throws IOException, JRException {
        log.info("startMakingInvoice");
        Order order = orderRepository.getById(request.getId());
        List<OrderDetailDTO> orderDetailDTOList = order.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetailDTO detailDTO = new OrderDetailDTO();
                    detailDTO.setDetailId(String.valueOf(orderDetail.getId()));
                    detailDTO.setProductName(orderDetail.getProduct().getName());
                    detailDTO.setQuantity(orderDetail.getQuantity());
                    detailDTO.setPrice(orderDetail.getProduct().getPrice());
                    detailDTO.setTotalPrice(orderDetail.getTotalPrice());
                    return detailDTO;
                })
                .collect(Collectors.toList());
        BigDecimal totalBill = orderDetailDTOList.stream()
                .map(OrderDetailDTO::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        JRDataSource dataSource = new JRBeanCollectionDataSource(orderDetailDTOList);

        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("USERNAME", order.getUser().getUsername());
        parameters.put("EMAIL", order.getUser().getEmailAddress());
        parameters.put("ORDER_ID", String.valueOf(order.getId()));
        parameters.put("DESTINATION_ADDRESS", order.getDestinationAddress());
        parameters.put("CREATED_DATE", new Date());
        parameters.put("MERCHANT_NAME", order.getOrderDetails().get(0).getProduct().getMerchant().getName());
        parameters.put("TOTAL_BILL", totalBill);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        log.info("finished making invoice");

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
//    @Override
//    public byte[] generateInvoice(Users request) throws IOException, JRException {
//        log.info("startMakingInvoice");
//        Users users = userRepository.findByIdWithOrders(request.getId());
//        List<OrderDetailDTO> orderDetailDTOList = users.getOrders().stream()
//                .map(Order::getOrderDetails)
//                .flatMap(List::stream)
//                .map(orderDetail -> {
//                    OrderDetailDTO dto = new OrderDetailDTO();
//                    dto.setDetailId(String.valueOf(orderDetail.getId()));
//                    dto.setQuantity(orderDetail.getQuantity());
//                    dto.setTotalPrice(orderDetail.getTotalPrice());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        JRDataSource dataSource = new JRBeanCollectionDataSource(orderDetailDTOList);
//
//        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("USER_ID", users.getId().toString());
//        parameters.put("USERNAME", users.getUsername());
//        parameters.put("USER_EMAIL", users.getEmailAddress());
//        parameters.put("CREATED_DATE_INVOICE", new Date());
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//        log.info("finished making invoice");
//
//        return JasperExportManager.exportReportToPdf(jasperPrint);
//    }
}
