package com.aplikasi.challenge.controller.auth;

import com.aplikasi.challenge.entity.User;
import com.aplikasi.challenge.repository.oauth.UserRepository;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.utils.SimpleStringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
@Tag(name = "User", description = "User API")
public class UserController {
    @Autowired
    public UserService userService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SimpleStringUtils simpleStringUtils;

    @PutMapping("/update")
    @Operation(summary = "Update User", description = "Update User")
    public ResponseEntity<Map> update(@RequestBody User request) {
        return new ResponseEntity<Map>(userService.update(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete User", description = "Delete User")
    public ResponseEntity<Map> delete(@RequestBody User request) {
        return new ResponseEntity<>(userService.delete(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Get User by ID")
    public ResponseEntity<Map> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "List User", description = "List User")
    public ResponseEntity<Map> listQuizHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String emailAddress,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable showData = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<User> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (username != null && !username.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
                    }
                    if (emailAddress != null && !emailAddress.isEmpty()) {
                        predicates.add(criteriaBuilder.equal(root.get("email_address"), emailAddress));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });

        Page<User> list = userRepository.findAll(spec, showData);

        Map map = new HashMap();
        map.put("data",list);
        return new ResponseEntity<Map>(map, new HttpHeaders(), HttpStatus.OK);
    }

}
