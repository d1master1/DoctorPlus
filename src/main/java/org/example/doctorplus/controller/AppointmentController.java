package org.example.doctorplus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.DoctorService;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ServingService servingService;

    @GetMapping
    public String listAppointments(
            Model model,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortDir", required = false) String sortDir
    ) {
        Sort sort;
        if (sortField == null || sortField.isEmpty()) {
            sort = Sort.unsorted();
        } else {
            try {
                Sort.Direction direction = (sortDir == null || (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")))
                        ? Sort.Direction.ASC
                        : Sort.Direction.fromString(sortDir);
                sort = Sort.by(direction, sortField);
            } catch (IllegalArgumentException ex) {
                sort = Sort.by(Sort.Direction.ASC, "date");
                sortField = "date";
                sortDir = "asc";
            }
        }
        List<Appointment> appointments = appointmentService.getAllAppointments(sort);
        model.addAttribute("appointments", appointments);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", (sortDir != null && sortDir.equalsIgnoreCase("asc")) ? "desc" : "asc");
        return "appointment/appointment_list";
    }

    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute Appointment appointment) {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointment";
    }

    @GetMapping({"/add", "/edit/{id}"})
    public String showAppointmentForm(@PathVariable(required = false) Long id, Model model) throws JsonProcessingException {
        Appointment appointment = (id != null) ? appointmentService.findById(id).orElse(new Appointment()) : new Appointment();
        List<Patient> patients = patientService.findAll();
        List<Doctor> doctors = doctorService.findAll();
        List<Serving> servings = servingService.findAll();

        model.addAttribute("appointment", appointment);
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        model.addAttribute("servings", servings);

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, Object>> patientJsonList = patients.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("phone", p.getPhone());
            return map;
        }).collect(Collectors.toList());

        List<Map<String, Object>> doctorJsonList = doctors.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", d.getId());
            map.put("fullName", d.getFullName());
            return map;
        }).collect(Collectors.toList());

        List<Map<String, Object>> servingJsonList = servings.stream().map(s -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", s.getId());
            map.put("name", s.getName());
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("patientJson", objectMapper.writeValueAsString(patientJsonList));
        model.addAttribute("doctorJson", objectMapper.writeValueAsString(doctorJsonList));
        model.addAttribute("servingJson", objectMapper.writeValueAsString(servingJsonList));

        return "appointment/appointment_form";
    }

    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Прием успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении приема");
        }
        return "redirect:/appointment";
    }

    @PostMapping("/deleteAll")
    public String deleteAllAppointments(RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAllAppointments();
            redirectAttributes.addFlashAttribute("successMessage", "Все приемы успешно удалены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении всех приемов");
        }
        return "redirect:/appointment";
    }
}