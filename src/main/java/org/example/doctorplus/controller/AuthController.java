package org.example.doctorplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam Map<String, String> params, Model model) {
        if (params.containsKey("error")) {
            model.addAttribute("error", true);
        }
        return "include/login";
    }
}