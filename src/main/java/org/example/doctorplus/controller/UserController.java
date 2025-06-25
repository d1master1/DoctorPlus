package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.impl.ServingServiceImpl;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.example.doctorplus.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private ServingServiceImpl servingService;
    private final PatientService patientService;

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User userForm,
                             @RequestParam(value = "roles", required = false) List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        if (roleNames != null) {
            for (String roleName : roleNames) {
                try {
                    roles.add(Role.valueOf(roleName));
                } catch (IllegalArgumentException e) {
                    // логирование
                }
            }
        }
        userService.updateUserRoles(userForm.getId(), roles);
        return "redirect:/user";
    }
    
    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(name = "sort", required = false) String sort) {
        List<User> users = switch (sort != null ? sort.toLowerCase() : "") {
            case "username_asc" -> userService.findAllOrderByUsernameAsc();
            case "username_desc" -> userService.findAllOrderByUsernameDesc();
            case "name_asc" -> userService.findAllOrderByNameAsc();
            case "name_desc" -> userService.findAllOrderByNameDesc();
            default -> userService.findAll();
        };
        model.addAttribute("users", users);
        model.addAttribute("sort", sort);
        return "user/user_list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("allRoles", Role.values());
        return "user/user_form";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }

    @GetMapping("/realty")
    public String userRealty(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        List<Serving> realties = ServingService.findAllByOwner(currentUser);
        model.addAttribute("realties", realties);
        return "profile/realty_list";
    }

    @GetMapping("/users")
    public String getAllUsers(@RequestParam(name = "sort", required = false) String sort, Model model) {
        List<User> users;

        switch (sort != null ? sort : "") {
            case "username_asc":
                users = userService.findAllOrderByUsernameAsc();
                break;
            case "username_desc":
                users = userService.findAllOrderByUsernameDesc();
                break;
            case "name_asc":
                users = userService.findAllOrderByNameAsc();
                break;
            case "name_desc":
                users = userService.findAllOrderByNameDesc();
                break;
            default:
                users = userService.findAll();
        }

        model.addAttribute("user", users);
        model.addAttribute("sort", sort);
        return "users/user_list";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        Patient patient = patientService.findByUser(user).orElse(new Patient());

        UserDTO userDTO = UserDTO.fromEntity(user, patient);

        model.addAttribute("user", userDTO);
        model.addAttribute("allRoles", Role.values());

        return "user/user_form";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") UserDTO userDTO,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", Role.values());
            return "user/user_form";
        }

        userService.update(userDTO);
        return "redirect:/user";
    }
}