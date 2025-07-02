package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String listPatients(Model model,
                              @RequestParam(required = false) String sortField,
                              @RequestParam(required = false) String sortDir) {

        List<Patient> patients;
        String direction = (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? "desc" : "asc";

        if (sortField == null || sortField.isBlank()) {
            patients = patientService.findAll();
            sortField = null;
            direction = null;
        } else {
            patients = patientService.findAllSortedByField(sortField.toLowerCase(), direction);
        }

        model.addAttribute("patients", patients);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", direction);

        return "patient/patient_list";
    }

    @GetMapping("/add")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/patient_form";
    }

    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Optional<Patient> patientOpt = patientService.findById(id);
        if (patientOpt.isEmpty()) {
            return "redirect:/patient";
        }
        model.addAttribute("patient", patientOpt.get());
        return "patient/patient_form";
    }

    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patient") Patient patient,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "patient/patient_form";
        }
        patientService.save(patient);
        return "redirect:/patient";
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deleteByIdIfPossible(id);
        return deleted ? "redirect:/patient" : "redirect:/patient?error=true";
    }

    @PostMapping("/deleteAll")
    public String deleteAllPatients() {
        int notDeletedCount = patientService.deleteAllExceptWithAppointments();
        if (notDeletedCount > 0) {
            return "redirect:/patient?partialError=" + notDeletedCount;
        }
        return "redirect:/patient";
    }
}