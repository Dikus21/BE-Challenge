package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.OrderDetail;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.OrderDetailRepository;
import com.aplikasi.challenge.service.OrderDetailService;
import com.aplikasi.challenge.utils.SimpleStringUtils;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/v1/order_detail")
@Tag(name = "Order Detail", description = "Order Detail API")
public class OrderDetailController {
    @Autowired
    public OrderDetailService orderDetailService;

    @Autowired
    public OrderDetailRepository orderDetailRepository;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PostMapping("/save")
    @Operation(summary = "Save Order Detail", description = "Save Order Detail")
    public ResponseEntity<Map> save(@RequestBody OrderDetail request) {
        return new ResponseEntity<Map>(orderDetailService.save(request), HttpStatus.OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Update Order Detail", description = "Update Order Detail")
    public ResponseEntity<Map> update(@RequestBody OrderDetail request) {
        return new ResponseEntity<Map>(orderDetailService.update(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete Order Detail", description = "Delete Order Detail")
    public ResponseEntity<Map> delete(@RequestBody OrderDetail request) {
        return new ResponseEntity<>(orderDetailService.delete(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order Detail by ID", description = "Get Order Detail by ID")
    public ResponseEntity<Map> getById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(orderDetailService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "List Order Detail", description = "Pageable List Order Detail")
    public ResponseEntity<Map> listQuizHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) BigDecimal quantity,
            @RequestParam(required = false) BigDecimal totalPrice,
            @RequestParam(required = false) Product product,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<OrderDetail> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (quantity != null) {
                        predicates.add(criteriaBuilder.equal(root.get("quantity"), quantity));
                    }
                    if (totalPrice != null) {
                        predicates.add(criteriaBuilder.equal(root.get("totalPrice"), totalPrice));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<OrderDetail> list = orderDetailRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }

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
