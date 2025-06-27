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
import java.util.List;

@Controller
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ServingService servingService;

    // --- Список приёмов ---
    @GetMapping
    public String listAppointments(
            Model model,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "sortDir", required = false) String sortDir
    ) {
        Sort sort;
        if (sortField == null || sortField.isEmpty()) {
            sort = Sort.unsorted();
            sortField = "date";
            sortDir = "asc";
        } else {
            sortDir = (sortDir != null && sortDir.equalsIgnoreCase("desc")) ? "desc" : "asc";
            sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        }

        List<Appointment> appointments = appointmentService.getAllAppointments(sort);

        model.addAttribute("appointments", appointments);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "appointment/appointment_list";
    }

    // --- Форма добавления/редактирования ---
    @GetMapping({"/add", "/edit/{id}"})
    public String showAppointmentForm(@PathVariable(required = false) Long id,
                                      Model model) {

        // Получаем приём или создаём новый
        Appointment appointment = (id != null)
                ? appointmentService.findById(id).orElse(new Appointment())
                : new Appointment();

        // Получаем данные для формы
        List<Patient> patients = patientService.findAll();
        List<Doctor> doctors = doctorService.findAll();
        List<Serving> servings = servingService.findAll();

        // Передаём данные в модель
        model.addAttribute("appointment", appointment);
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        model.addAttribute("servings", servings);

        // Подготавливаем JSON для автозаполнения через JS
        model.addAttribute("patientJson", toJson(patients));
        model.addAttribute("doctorJson", toJson(doctors));
        model.addAttribute("servingJson", toJson(servings));

        return "appointment/appointment_form";
    }

    // Вспомогательный метод для конвертации объектов в JSON
    private String toJson(List<?> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return "[]"; // безопасный fallback
        }
    }

    // --- Сохранение приёма ---
    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute Appointment appointment) {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointment";
    }

    // --- Удаление приёма ---
    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            appointmentService.deleteAppointment(id);
            redirectAttrs.addFlashAttribute("successMessage", "Приём успешно удалён");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Ошибка при удалении приёма");
        }
        return "redirect:/appointment";
    }

    // --- Массовое удаление ---
    @PostMapping("/deleteAll")
    public String deleteAllAppointments(RedirectAttributes redirectAttrs) {
        try {
            int deletedCount = appointmentService.deleteAllAppointments();
            if (deletedCount > 0) {
                redirectAttrs.addFlashAttribute("successMessage", deletedCount + " приём(ов) удалено");
            } else {
                redirectAttrs.addFlashAttribute("warningMessage", "Нет записей для удаления");
            }
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage", "Ошибка при массовом удалении");
        }
        return "redirect:/appointment";
    }
}