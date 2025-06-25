package org.example.doctorplus.controller;

import org.example.doctorplus.model.Serving;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/serving")
public class ServingController {

    private final ServingService servingService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public ServingController(ServingService servingService,
                             PatientService patientService,
                             AppointmentService appointmentService) {
        this.servingService = servingService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String listServings(
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
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("appointments", appointmentService.findAll());
        return "serving/serving_form";
    }

    @PostMapping("/save")
    public String saveServing(@ModelAttribute Serving serving) {
        servingService.saveServing(serving);
        return "redirect:/serving";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Serving serving = servingService.getServingById(id);
        if (serving == null) {
            throw new IllegalArgumentException("Услуга с ID " + id + " не найдена");
        }
        model.addAttribute("serving", serving);
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("appointments", appointmentService.findAll());
        return "serving/serving_form";
    }

    @PostMapping("/delete/{id}")
    public String deleteServing(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (appointmentService.existsByServingId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Невозможно удалить: услуга используется в приеме.");
        } else {
            servingService.deleteServing(id);
            redirectAttributes.addFlashAttribute("successMessage", "Услуга успешно удалена.");
        }
        return "redirect:/serving";
    }

    @PostMapping("/delete")
    public String deleteAllExceptWithAppointments(RedirectAttributes redirectAttributes) {
        List<Serving> deleted = servingService.deleteAllExceptWithAppointments();
        if (deleted.isEmpty()) {
            redirectAttributes.addFlashAttribute("warningMessage", "Удаление невозможно: все услуги участвуют в приёмах.");
        } else {
            int deletedCount = deleted.size();
            redirectAttributes.addFlashAttribute("warningMessage",
                    "Были удалены все услуги, которые не находятся в приёмах. Количество удалённых: " + deletedCount);
        }
        return "redirect:/serving";
    }
}