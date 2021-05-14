package com.pvt73.recycling.controller;

import com.pvt73.recycling.exception.ErrorMessage;
import com.pvt73.recycling.model.dao.User;
import com.pvt73.recycling.model.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Users", description = "Completely unsecure user controller!")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService service;

    @ApiResponse(responseCode = "201", description = "New User Created.")
    @ApiResponse(responseCode = "400", description = "parameter is missing or wrong formatted.", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "409", description = "user already exist.", content = @Content(schema = @Schema(implementation = ErrorMessage.class)))

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    User creat(@RequestBody @Valid User user) {
        return service.creat(user);
    }


    @ApiResponse(responseCode = "200", description = "User found.")
    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    User findById(@PathVariable String id) {
        return service.findByID(id);
    }


    @PutMapping(value = "users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    User update(@RequestBody User user, @PathVariable String id) {
        return service.update(user, id);
    }


    @ApiResponse(responseCode = "204", description = "No content, User deleted.")
    @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(schema = @Schema(implementation = ErrorMessage.class), mediaType = MediaType.APPLICATION_JSON_VALUE))

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable String id) {
        service.delete(id);
    }

}
