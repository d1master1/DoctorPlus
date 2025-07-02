package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.DoctorService;
import com.example.DoctorPlus.service.PatientService;
import com.example.DoctorPlus.service.ServingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
    private final ServingService servingService;
    private final DoctorService doctorService;

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
                // Защита от null и неправильных значений
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

        List<Serving> servings = servingService.findAll();
        List<Patient> patients = patientService.findAll();
        List<Doctor> doctors = doctorService.findAll();

        model.addAttribute("appointment", appointment);
        model.addAttribute("servings", servings);
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);

        ObjectMapper objectMapper = new ObjectMapper();

        // JSON-данные по недвижимости
        List<Map<String, Object>> servingJsonList = servings.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("cost", r.getCost());
            /*map.put("ownerName", r.getOwner() != null ? r.getOwner().getFullName() : "—");*/
            return map;
        }).collect(Collectors.toList());

        // JSON-данные по клиентам
        List<Map<String, Object>> patientJsonList = patients.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("phone", c.getPhone());
            return map;
        }).collect(Collectors.toList());

        model.addAttribute("servingJson", objectMapper.writeValueAsString(servingJsonList));
        model.addAttribute("patientJson", objectMapper.writeValueAsString(patientJsonList));

        return "appointment/appointment_form";
    }

    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAppointmentById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Записи о приеме успешно удалены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении записей о приеме");
        }
        return "redirect:/appointment";
    }

    @PostMapping("/deleteAll")
    public String deleteAllAppointments(RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAllAppointments();
            redirectAttributes.addFlashAttribute("successMessage", "Все записи о приеме успешно удалены");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении всех записей о приеме");
        }
        return "redirect:/appointment";
    }
}