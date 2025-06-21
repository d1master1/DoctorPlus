package org.example.controller.web;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Model model) {
        try {
            User currentUser = userService.getCurrentUser();
            model.addAttribute("user", currentUser);
        } catch (UsernameNotFoundException e) {
            return "redirect:/login";
        }

        return "user/profile";
    }
}