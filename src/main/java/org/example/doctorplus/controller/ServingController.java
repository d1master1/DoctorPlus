package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.ServingDTO;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.example.doctorplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/serving")
@RequiredArgsConstructor
public class ServingController {

    private final ServingService servingService;
    private final UserService userService;
    private final PatientService patientService;

    @GetMapping("/list")
    public String listServings(Model model) {
        List<Serving> servings = servingService.findAll(); // вызов метода
        model.addAttribute("servings", servings);
        return "include/serving_list";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        ServingDTO dto = new ServingDTO();

        if (id != null && servingService.findById(id).isPresent()) {
            Serving serving = servingService.findById(id).get();
            dto.setId(serving.getId());
            dto.setName(serving.getName());
            dto.setDescription(serving.getDescription());
            dto.setCost(serving.getCost());
            dto.setUserId(serving.getUser().getId());
            dto.setUsername(serving.getUser().getUsername());

            if (serving.getPatient() != null) {
                dto.setPatientId(serving.getPatient().getId());
            }
        }

        model.addAttribute("servingDTO", dto);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("patients", patientService.findAll());

        return "include/serving_form";
    }

    @PostMapping("/save")
    public String saveServing(@Valid @ModelAttribute("servingDTO") ServingDTO dto,
                              BindingResult result,
                              RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.serving", result);
            redirectAttrs.addFlashAttribute("serving", dto);
            return "redirect:/serving/form";
        }

        Serving serving = new Serving();
        serving.setId(dto.getId());
        serving.setName(dto.getName());
        serving.setDescription(dto.getDescription());
        serving.setCost(dto.getCost());

        // Установка пользователя
        User user = userService.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        serving.setUser(user);

        // Установка пациента (опционально)
        if (dto.getPatientId() != null && !dto.getPatientId().equals(0L)) {
            Patient patient = patientService.findById(dto.getPatientId())
                    .orElseThrow(() -> new IllegalArgumentException("Пациент не найден"));
            serving.setPatient(patient);
        } else {
            serving.setPatient(null); // если пациент не выбран — очистить
        }

        servingService.save(serving);
        return "redirect:/serving/list";
    }

    @GetMapping("/edit/{id}")
    public String editServing(@PathVariable Long id, Model model) {
        return showForm(id, model);
    }

    @GetMapping("/delete/{id}")
    public String deleteServing(@PathVariable Long id) {
        servingService.deleteById(id);
        return "redirect:/serving/list";
    }
}