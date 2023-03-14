package com.app.testproject.controller;

import com.app.testproject.dto.UserDTO;
import com.app.testproject.dto.UserRequest;
import com.app.testproject.model.User;
import com.app.testproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserDTO> userOptional = userService.getUserById(id);
        if (userOptional.isPresent())
            return ResponseEntity.ok(userOptional.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest);
    }
}
