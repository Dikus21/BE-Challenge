package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.Merchant;
import com.aplikasi.challenge.repository.MerchantRepository;
import com.aplikasi.challenge.service.MerchantService;
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
@RequestMapping("/v1/merchant")
public class MerchantController {
    @Autowired
    public MerchantService merchantService;

    @Autowired
    public MerchantRepository merchantRepository;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody Merchant request) {
        return new ResponseEntity<Map>(merchantService.save(request), HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody Merchant request) {
        return new ResponseEntity<Map>(merchantService.update(request), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody Merchant request) {
        return new ResponseEntity<>(merchantService.delete(request.getId()), HttpStatus.OK);
    }

    @GetMapping(value = {"/{id}", "/{id}/"})
    public ResponseEntity<Map> getById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(merchantService.getById(id), HttpStatus.OK);
    }

    @GetMapping(value = {"/list", "/list/"})
    public ResponseEntity<Map> listQuizHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) boolean open,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<Merchant> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (name != null && !name.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
                    }
                    if (location != null && !location.isEmpty()) {
                        predicates.add(criteriaBuilder.equal(root.get("location"), location));
                    }
                    predicates.add(criteriaBuilder.equal(root.get("open"), open));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<Merchant> list = merchantRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }
}
