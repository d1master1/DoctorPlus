package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.service.DoctorService;
import com.example.DoctorPlus.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final UserService userService;

    public DoctorController(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @GetMapping
    public String listDoctors(@RequestParam(value = "sortBy", required = false) String sortBy,
                                @RequestParam(value = "sortDir", required = false) String sortDir,
                                Model model) {
        List<Doctor> doctors;

        if (sortBy == null || sortBy.isEmpty()) {
            doctors = doctorService.getAllDoctorsWithUsers();
        } else {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            doctors = doctorService.getAllDoctorsSorted(Sort.by(direction, sortBy));
        }

        model.addAttribute("doctors", doctors);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "doctor/doctor_list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("users", userService.findAll());
        return "doctor/doctor_form";
    }

    @PostMapping("/save")
    public String saveDoctor(@Valid @ModelAttribute("doctor") Doctor doctor,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            return "doctor/doctor_form";
        }

        try {
            doctorService.saveDoctor(doctor);
            return "redirect:/doctor";
        } catch (Exception e) {
            model.addAttribute("validationErrors", List.of("Ошибка при сохранении доктора: " + e.getMessage()));
            model.addAttribute("users", userService.findAll());
            return "doctor/doctor_form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return "redirect:/doctor";
        }
        model.addAttribute("doctor", doctor);
        model.addAttribute("users", userService.findAll());
        return "doctor/doctor_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctor(id);
        } catch (IllegalStateException e) {
            redirectAttributes.addAttribute("error", true);
        }
        return "redirect:/doctor";
    }

    @GetMapping("/deleteAll")
    public String deleteAllDoctors(RedirectAttributes redirectAttributes) {
        List<Doctor> notDeleted = doctorService.deleteAllExceptWithAppointments();

        if (!notDeleted.isEmpty()) {
            redirectAttributes.addAttribute("partialError", notDeleted.size());
        }

        return "redirect:/doctor";
    }

    @GetMapping("/patronymic/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getPatronymicByUserId(@PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        if (doctor != null && doctor.getPatronymic() != null) {
            return ResponseEntity.ok(Map.of("patronymic", doctor.getPatronymic()));
        } else {
            return ResponseEntity.ok(Map.of("patronymic", ""));
        }
    }
}