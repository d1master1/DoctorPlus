package org.example.doctorplus.controller;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // --- Список докторов ---
    @GetMapping
    public String listDoctors(@RequestParam(name = "sortBy", required = false) String sortBy,
                              @RequestParam(name = "sortDir", required = false) String sortDir,
                              Model model) {
        List<Doctor> doctors;

        if ("name".equalsIgnoreCase(sortBy)) {
            doctors = "desc".equalsIgnoreCase(sortDir)
                    ? doctorService.findAllOrderByNameDesc()
                    : doctorService.findAllOrderByNameAsc();
        } else if ("speciality".equalsIgnoreCase(sortBy)) {
            doctors = "desc".equalsIgnoreCase(sortDir)
                    ? doctorService.findAllBySpecialityDesc()
                    : doctorService.findAllBySpecialityAsc();
        } else {
            doctors = doctorService.findAll();
            sortBy = null;
            sortDir = null;
        }

        model.addAttribute("doctors", doctors);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "doctor/doctor_list";
    }

    // --- Форма добавления доктора ---
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor/doctor_form";
    }

    // --- Форма редактирования доктора ---
    @GetMapping("/edit/{id}")
    public String editDoctorForm(@PathVariable Long id, Model model) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return "redirect:/doctor";
        }
        model.addAttribute("doctor", doctor);
        return "doctor/doctor_form";
    }

    // --- Сохранение доктора ---
    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute("doctor") Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return "redirect:/doctor";
    }

    // --- Удаление доктора ---
    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctor(id);
        } catch (IllegalStateException e) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/doctor";
        }
        return "redirect:/doctor";
    }

    // --- Массовое удаление докторов ---
    @PostMapping("/deleteAll")
    public String deleteAllDoctors() {
        List<Doctor> undeleted = doctorService.deleteAllExceptWithAppointments();
        if (!undeleted.isEmpty()) {
            return "redirect:/doctor?partialError=" + undeleted.size();
        }
        return "redirect:/doctor";
    }
}