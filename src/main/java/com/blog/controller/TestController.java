package com.blog.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test/me")
    public String whoAmI(Authentication authentication) {
        return "Salom, " + authentication.getName() + "! Sening rollaring " + authentication.getAuthorities();
    }
}
