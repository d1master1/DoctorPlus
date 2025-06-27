package org.example.doctorplus.controller;

import org.example.doctorplus.model.Serving;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @GetMapping("/serving")
    public String listServings(Model model,
                               @RequestParam(name = "sortField", required = false) String sortField,
                               @RequestParam(name = "sortDir", required = false) String sortDir) {

        Sort.Direction direction = (sortDir != null && sortDir.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField != null ? sortField : "name");

        model.addAttribute("servings", servingService.findAllSorted(sort));
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

    @PostMapping("/serving/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        if (servingService.deleteByIdIfPossible(id)) {
            redirectAttrs.addFlashAttribute("successMessage", "Услуга успешно удалена");
        } else {
            redirectAttrs.addFlashAttribute("errorMessage", "Невозможно удалить: услуга используется");
        }
        return "redirect:/serving";
    }

    @PostMapping("/serving/deleteAll")
    public String deleteAll(RedirectAttributes redirectAttrs) {
        List<Serving> undeleted = servingService.deleteAllExceptWithAppointments();

        if (!undeleted.isEmpty()) {
            redirectAttrs.addFlashAttribute("warningMessage", "Не удалось удалить " + undeleted.size() + " услуг(и), так как они связаны с приемами");
        } else {
            redirectAttrs.addFlashAttribute("successMessage", "Все услуги удалены");
        }

        return "redirect:/serving";
    }
}