package com.example.club.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /** Vista publica de login (ver SecurityConfig: /login esta en permitAll()). */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
