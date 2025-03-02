package com.aplikasi.challenge.controller;

import com.aplikasi.challenge.entity.Users;
import com.aplikasi.challenge.repository.UserRepository;
import com.aplikasi.challenge.service.UserService;
import com.aplikasi.challenge.utils.ResponseTemplate;
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
import java.util.*;

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

    @PostMapping("/save")
    @Operation(summary = "Save User", description = "Save User")
    public ResponseEntity<ResponseTemplate<Users>> save(@RequestBody Users request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.OK);
    }

    @PutMapping("/update")
    @Operation(summary = "Update User", description = "Update User")
    public ResponseEntity<ResponseTemplate<Users>> update(@RequestBody Users request) {
        return new ResponseEntity<>(userService.update(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete User", description = "Delete User")
    public ResponseEntity<ResponseTemplate<Users>> delete(@RequestBody Users request) {
        return new ResponseEntity<>(userService.delete(request.getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get User by ID", description = "Get User by ID")
    public ResponseEntity<ResponseTemplate<Users>> getById(@PathVariable("id")UUID id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "List User", description = "Pageable List User")
    public ResponseEntity<ResponseTemplate<Page<Users>>> listQuizHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String emailAddress,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable pageable = simpleStringUtils.getShort(orderby, ordertype, page, size);

        Specification<Users> spec =
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

        // Fetch paginated users from the database
        Page<Users> usersPage = userRepository.findAll(spec, pageable);


        return ResponseEntity.ok(new ResponseTemplate<>(200, "Users retrieved successfully", usersPage));
    }
}
