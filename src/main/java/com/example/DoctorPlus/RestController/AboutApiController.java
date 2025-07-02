package com.example.DoctorPlus.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AboutApiController {

    @GetMapping("/api/about")
    public Map<String, Object> aboutApi() {
        return Map.of(
                "application", "DoctorPlus",
                "version", "1.0",
                "description", "Healthcare management system for clinics and doctors",
                "author", "Your Name"
        );
    }
}