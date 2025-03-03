package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.Order;
import com.aplikasi.challenge.repository.OrderRepository;
import com.aplikasi.challenge.service.InvoiceService;
import com.aplikasi.challenge.service.OrderService;
import com.aplikasi.challenge.utils.SimpleStringUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.io.ByteArrayInputStream;
import java.util.*;

@RestController
@RequestMapping("/v1/order")
@Tag(name = "Order", description = "Order API")
public class OrderController {
    @Autowired
    public OrderRepository orderRepository;

    @Autowired
    public InvoiceService invoiceService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PostMapping("/generateInvoice")
    @Operation(summary = "Generate Invoice", description = "Generate Invoice")
    public ResponseEntity<?> generateInvoice(@RequestBody Order request) {
        try {
            byte[] pdfData = invoiceService.generateInvoice(request);
            ByteArrayInputStream bis = new ByteArrayInputStream(pdfData);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=invoice.pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/save")
    @Operation(summary = "Save Order", description = "Save Order")
    public ResponseEntity<Map> save(@RequestBody Order request) {
        return new ResponseEntity<Map>(orderService.save(request), HttpStatus.OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Order", description = "Update Order")
    public ResponseEntity<Map> update(@RequestBody Order request) {
        return new ResponseEntity<Map>(orderService.update(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Order", description = "Delete Order")
    public ResponseEntity<Map> delete(@RequestBody Order request) {
        return new ResponseEntity<>(orderService.delete(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order by ID", description = "Get Order by ID")
    public ResponseEntity<Map> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/showOrderDetail")
    @Operation(summary = "Show Order Detail", description = "Show Order Detail")
    public ResponseEntity<Map> getOrderDetailList(@RequestBody Order request) {
        return new ResponseEntity<>(orderService.getOrderDetailList(request), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "List Order", description = "Pageable List Order")
    public ResponseEntity<Map> listOrder(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) UUID user,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<Order> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (address != null && !address.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("destinationAddress")), "%" + address.toLowerCase() + "%"));
                    }
//                    if (username != null && !username.isEmpty()) {
//                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("user.username")), "%" + username.toLowerCase() + "%"));
//                    }
//                    if (user != null) {
//                        predicates.add(criteriaBuilder.equal(root.get("user.id"), user));
//                    }
//                    predicates.add(criteriaBuilder.equal(root.get("completed"), status));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<Order> list = orderRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }
//    @ExceptionHandler(InvalidFormatException.class)
//    public ResponseEntity<String> invalidFormatHandler(InvalidFormatException e) {
//        if (e.getTargetType().equals(UUID.class)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format in the provided JSON.");
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data format.");
//    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map> invalidFormatHandler(InvalidFormatException e) {
        Map<Object, Object> map = new HashMap<>();
        if (e.getTargetType().equals(UUID.class)) {
            map.put("ERROR", "Invalid UUID format provided in JSON");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        map.put("ERROR", "Invalid data format");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
