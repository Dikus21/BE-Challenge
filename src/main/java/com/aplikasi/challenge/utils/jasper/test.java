//package com.aplikasi.challenge.utils.jasper;
//
//import com.aplikasi.challenge.dto.OrderDetailDTO;
//import com.aplikasi.challenge.entity.Order;
//import com.aplikasi.challenge.entity.Users;
//import com.aplikasi.challenge.repository.UsersRepository;
//import net.sf.jasperreports.engine.JRDataSource;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.transaction.Transactional;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class test {
//    @Autowired
//    public UsersRepository usersRepository;
//    @Autowired
//    public ReportService reportService;
//
//    @Test
//    @Transactional
//    public void test1() {
//        UUID uuid = UUID.fromString("783b6a20-bd57-4937-9c07-0073fc6fc05f");
//        Users users = usersRepository.findByIdWithOrders(uuid);
//        List<OrderDetailDTO> orderDetailDTOList = users.getOrders().stream()
//                .map(Order::getOrderDetails)
//                .flatMap(List::stream)
//                .map(orderDetail -> {
//                    OrderDetailDTO dto = new OrderDetailDTO();
//                    dto.setDetailId(String.valueOf(orderDetail.getId()));
//                    dto.setQuantity(orderDetail.getQuantity());
//                    dto.setTotalPrice(orderDetail.getTotalPrice());
//                    dto.setPrice(orderDetail.getProduct().getPrice());
//                    dto.setProductName(orderDetail.getProduct().getName());
//                    return dto;
//                })
//                .collect(Collectors.toList());
////
//        System.out.println(orderDetailDTOList);
//        JRDataSource dataSource = new JRBeanCollectionDataSource(orderDetailDTOList);
//
//        String path = "D:\\Andika\\BootCamp(SYNERGY)\\Project\\Challenge\\Challenge_3\\report\\Blank_A4.jrxml";
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("USER_ID", users.getId().toString());
//        parameters.put("USERNAME", users.getUsername());
//        parameters.put("USER_EMAIL", users.getEmailAddress());
//        parameters.put("CREATED_DATE_INVOICE", new Date());
//
//        reportService.generate_pdf(parameters, path, dataSource);
//    }
//}
