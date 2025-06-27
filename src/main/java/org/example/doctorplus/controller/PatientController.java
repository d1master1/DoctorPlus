package org.example.doctorplus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.doctorplus.dto.PatientDTO;
import org.example.doctorplus.impl.PatientServiceImpl;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Setter
@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private PatientServiceImpl userService;

    // Список пациентов
    @GetMapping
    public String listPatients(Model model,
                               @RequestParam(required = false) String sortField,
                               @RequestParam(required = false) String sortDir) {
        String direction = (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? "desc" : "asc";

        List<Patient> patients;
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

    // Сохранение пациента
    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "patient/patient_form";
        }

        Patient patient = new Patient();
        patient.setId(patientDTO.getId());
        patient.setName(patientDTO.getName());
        patient.setSurname(patientDTO.getSurname());
        patient.setPatronymic(patientDTO.getPatronymic());
        patient.setPassport(patientDTO.getPassport());
        patient.setPhone(patientDTO.getPhone());
        patient.setEmail(patientDTO.getEmail());
        patient.setAddress(patientDTO.getAddress());
        patient.setBirthDate(patientDTO.getBirthDate());

        patientService.save(patient);

        return "redirect:/patient";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) throws JsonProcessingException {
        model.addAttribute("patient", new Patient());
        model.addAttribute("users", userService.findAll());

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("userJson", mapper.writeValueAsString(userService.findAll()));

        return "patient/patient_form";
    }

    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) throws JsonProcessingException {
        Patient patient = patientService.findById(id).orElse(null);
        if (patient == null) {
            return "redirect:/patient?error=true";
        }

        model.addAttribute("patient", patient);
        model.addAttribute("users", userService.findAll());

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("userJson", mapper.writeValueAsString(userService.findAll()));

        return "patient/patient_form";
    }

    // Удаление пациента по ID
    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deleteByIdIfPossible(id);
        return deleted ? "redirect:/patient" : "redirect:/patient?error=true";
    }

    // Массовое удаление пациентов
    @PostMapping("/deleteAll")
    public String deleteAllPatients() {
        int notDeletedCount = patientService.deleteAllExceptWithAppointments();
        if (notDeletedCount > 0) {
            return "redirect:/patient?partialError=" + notDeletedCount;
        }
        return "redirect:/patient";
    }
}