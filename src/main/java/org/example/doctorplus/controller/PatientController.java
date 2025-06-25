package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;

    // Отображает список пациентов
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

    // Форма добавления пациента
    @GetMapping("/add")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient/patient_form";
    }

    // Сохранение нового/редактируемого пациента
    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patient") Patient patient,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "patient/patient_form";
        }
        patientService.save(patient);
        return "redirect:/patient";
    }

    // Редактирование пациента по ID
    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Optional<Patient> patientOpt = patientService.findById(id);
        if (patientOpt.isEmpty()) {
            return "redirect:/patient";
        }
        model.addAttribute("patient", patientOpt.get());
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

    // Просмотр профиля текущего пользователя
    @GetMapping("/profile")
    public String viewProfile(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Patient patient = patientService.findByUser(user).orElseGet(() -> {
            Patient newPatient = new Patient();
            newPatient.setUser(user);
            return newPatient;
        });

        model.addAttribute("user", user);
        model.addAttribute("patient", patient);
        return "include/profile";
    }

    // Редактирование профиля текущего пользователя
    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("patient") Patient patient,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "include/profile";
        }

        // Обновляем данные пациента
        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        Patient existingPatient = patientService.findByUser(optionalUser.get())
                .orElseGet(Patient::new);

        // Перезаписываем поля
        existingPatient.setName(patient.getName());
        existingPatient.setSurname(patient.getSurname());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setPassport(patient.getPassport());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setBirthDate(patient.getBirthDate());

        patientService.save(existingPatient);
        return "redirect:/patient/profile";
    }
}