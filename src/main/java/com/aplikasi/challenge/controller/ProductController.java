package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.entity.Product;
import com.aplikasi.challenge.repository.ProductRepository;
import com.aplikasi.challenge.service.ProductService;
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
@RequestMapping("/v1/product")
public class ProductController {
    @Autowired
    public ProductService productService;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody Product request) {
        return new ResponseEntity<Map>(productService.save(request), HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody Product request) {
        return new ResponseEntity<Map>(productService.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody Product request) {
        return new ResponseEntity<>(productService.delete(request.getId()), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<Map> getById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(productService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = {"/list", "/list/"})
    public ResponseEntity<Map> listQuizHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) Merchant merchant,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<Product> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (name != null && !name.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
                    }
                    if (price != null && !price.isEmpty()) {
                        predicates.add(criteriaBuilder.equal(root.get("price"), price));
                    }
//                    if (merchant != null) {
//                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("merchant.name")), "%" + merchant.getName().toLowerCase() + "%"));
//                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<Product> list = productRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }
}
