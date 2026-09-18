package com.example.club.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccesoDeniedController {

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "401";
    }
}
