package org.example.controller.web;

import org.example.dto.PatientDTO;
import org.example.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public String listPatients(Model model) {
        model.addAttribute("patients", patientService.findAll());
        return "patients/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", PatientDTO.builder().build());
        return "patients/form";
    }

    @PostMapping
    public String createPatient(@Valid @ModelAttribute("patient") PatientDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "patients/form";
        }
        patientService.create(dto);
        return "redirect:/patients";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        return "patients/form";
    }

    @PostMapping("/edit")
    public String updatePatient(@Valid @ModelAttribute("patient") PatientDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "patients/form";
        }
        patientService.update(dto);
        return "redirect:/patients";
    }
}