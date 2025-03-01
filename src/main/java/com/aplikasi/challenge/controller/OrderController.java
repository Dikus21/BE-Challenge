package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.Orders;
import com.aplikasi.challenge.repository.OrdersRepository;
import com.aplikasi.challenge.service.OrdersService;
import com.aplikasi.challenge.utils.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.*;

@RestController
@RequestMapping("/v1/order")
public class OrderController {
    @Autowired
    public OrdersRepository ordersRepository;

    @Autowired
    public OrdersService ordersService;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody Orders request) {
        return new ResponseEntity<Map>(ordersService.save(request), HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody Orders request) {
        return new ResponseEntity<Map>(ordersService.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody Orders request) {
        return new ResponseEntity<>(ordersService.delete(request.getId()), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<Map> getById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(ordersService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = {"/list", "/list/"})
    public ResponseEntity<Map> listOrder(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) UUID user,
            @RequestParam(required = false) boolean status,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<Orders> spec =
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
                    predicates.add(criteriaBuilder.equal(root.get("completed"), status));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<Orders> list = ordersRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }
}
