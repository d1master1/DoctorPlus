package org.example.controller.web;

import org.example.dto.DoctorDTO;
import org.example.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public String listDoctors(@RequestParam(name = "sort", required = false) String sort,
                              Model model) {
        model.addAttribute("doctors", doctorService.findAllSorted(sort));
        return "doctors/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("doctor", DoctorDTO.builder().build());
        return "doctors/form";
    }

    @PostMapping
    public String createDoctor(@Valid @ModelAttribute("doctor") DoctorDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "doctors/form";
        }
        doctorService.create(dto);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("doctor", doctorService.findById(id));
        return "doctors/form";
    }

    @PostMapping("/edit")
    public String updateDoctor(@Valid @ModelAttribute("doctor") DoctorDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "doctors/form";
        }
        doctorService.update(dto);
        return "redirect:/doctors";
    }

    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return "redirect:/doctors";
    }
}