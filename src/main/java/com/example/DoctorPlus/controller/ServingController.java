package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.PatientService;
import com.example.DoctorPlus.service.ServingService;
import com.example.DoctorPlus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/serving")
public class ServingController {

    private final ServingService servingService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;

    public ServingController(ServingService servingService, UserService userService, AppointmentService appointmentService, PatientService patientService) {
        this.servingService = servingService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.patientService = patientService;
    }

    @GetMapping
    public String listServing(
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortDir,
            Model model) {

        List<Serving> servings = servingService.findAll();

        if (sortField != null && sortDir != null) {
            Comparator<Serving> comparator = null;

            switch (sortField) {
                case "cost":
                    comparator = Comparator.comparing(Serving::getCost);
                    break;
                case "name":
                    comparator = Comparator.comparing(Serving::getName, String.CASE_INSENSITIVE_ORDER);
                    break;
                case "description":
                    comparator = Comparator.comparing(Serving::getDescription, String.CASE_INSENSITIVE_ORDER);
                    break;
            }

            if (comparator != null) {
                if ("desc".equalsIgnoreCase(sortDir)) {
                    comparator = comparator.reversed();
                }
                servings.sort(comparator);
            }
        }

        model.addAttribute("servings", servings);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "serving/serving_list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("serving", new Serving());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("appointment", appointmentService.findAll());
        model.addAttribute("patient", patientService.findAll());
        return "serving/serving_form";
    }

    @PostMapping("/save")
    public String saveServing(@ModelAttribute Serving serving) {
        servingService.saveServing(serving);
        return "redirect:/serving";
    }

    @PostMapping("/delete/{id}")
    public String deleteServing(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (servingService.existsByServingId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Невозможно удалить: услугу которая участвует в записях на прием.");
        } else {
            servingService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Услуга успешно удалена.");
        }
        return "redirect:/serving";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Serving serving = servingService.getServingById(id);
        if (serving == null) {
            throw new NoSuchElementException("Услуга с id " + id + " не найдена");
        }
        model.addAttribute("serving", serving);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("appointment", appointmentService.findAll());
        model.addAttribute("patient", patientService.findAll());
        return "serving/serving_form";
    }

    @PostMapping("/delete")
    public String deleteAllExceptWithAppointments(RedirectAttributes redirectAttributes) {
        List<Serving> deleted = servingService.deleteAllExceptWithAppointments();
        if (deleted.isEmpty()) {
            redirectAttributes.addFlashAttribute("warningMessage", "Удаление невозможно: все объекты участвуют в сделках.");
        } else {
            int deletedCount = deleted.size();
            redirectAttributes.addFlashAttribute("warningMessage",
                    "Были удалены все услуги, которые не находятся в записях на прием. Количество удалённых: " + deletedCount);
        }
        return "redirect:/serving";
    }
}