package com.pvt73.recycling.controller;

import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.model.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Completely unsecure user controller!")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService service;

    @PostMapping("/users")
    User creat(@RequestBody User user) {
        return service.creat(user);
    }

    @GetMapping("/users/{id}")
    User findById(@PathVariable String id) {
        return service.findByID(id);
    }

    @PutMapping("users/{id}")
    User update(@RequestBody User user, @PathVariable String id) {
        return service.update(user, id);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void delete(@PathVariable String id) {
        service.delete(id);
    }

}
