package org.example.controller.web;

import org.example.dto.AppointmentDTO;
import org.example.repo.PatientRepository;
import org.example.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientService;
    private final PatientRepository doctorService;
    private final PatientRepository serviceService;

    @GetMapping("/appointments")
    public String listAppointments(@RequestParam(name = "sort", required = false) String sort,
                                   Model model) {
        model.addAttribute("appointments", appointmentService.findAllSorted(sort));
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("services", serviceService.findAll());
        return "appointments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("appointment", AppointmentDTO.builder().build());
        model.addAttribute("patients", appointmentService.findAllPatients());
        model.addAttribute("doctors", appointmentService.findAllDoctors());
        model.addAttribute("services", appointmentService.findAllServices());
        return "appointments/form";
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute("appointment") AppointmentDTO dto,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", appointmentService.findAllPatients());
            model.addAttribute("doctors", appointmentService.findAllDoctors());
            model.addAttribute("services", appointmentService.findAllServices());
            return "appointments/form";
        }
        appointmentService.create(dto);
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("appointment", appointmentService.findById(id));
        model.addAttribute("patients", appointmentService.findAllPatients());
        model.addAttribute("doctors", appointmentService.findAllDoctors());
        model.addAttribute("services", appointmentService.findAllServices());
        return "appointments/form";
    }

    @PostMapping("/edit")
    public String updateAppointment(@Valid @ModelAttribute("appointment") AppointmentDTO dto,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patients", appointmentService.findAllPatients());
            model.addAttribute("doctors", appointmentService.findAllDoctors());
            model.addAttribute("services", appointmentService.findAllServices());
            return "appointments/form";
        }
        appointmentService.update(dto);
        return "redirect:/appointments";
    }

    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return "redirect:/appointments";
    }

}