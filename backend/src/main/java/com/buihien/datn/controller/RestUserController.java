package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Validated
public class RestUserController {
    @Autowired
    private UserService userService;

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/save")
    public ResponseEntity<?> saveOrUpdate(@Valid @RequestBody UserDto dto) {
        return new ResponseEntity<>(userService.saveOrUpdate(dto), HttpStatus.CREATED);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable @Min(value = 1, message = "Id must be greater than 0") long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable @Min(1) long id) {
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging")
    public ResponseEntity<?> pagingUser(@Valid @RequestBody SearchDto dto) {
        return new ResponseEntity<>(userService.pagingUser(dto), HttpStatus.OK);

    }
}
