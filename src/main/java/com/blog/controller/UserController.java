package com.blog.controller;

import com.blog.dto.request.RegisterRequest;
import com.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @PostMapping
//    public RegisterRequest createUser(@RequestBody UserCreateDto dto) {
//    }
//
//    @PutMapping("/{id}")
//    public RegisterRequest updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
//    }
//
//    @GetMapping
//    public List<RegisterRequest> getAllUsers() {
//    }
//
//    @GetMapping("/{id}")
//    public RegisterRequest getUserById(@PathVariable Long id) {
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteUserById(@PathVariable Long id) {
//    }
}
