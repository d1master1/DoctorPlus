package org.example.controller.web;

import org.example.dto.ServiceDTO;
import org.example.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public String listServices(@RequestParam(name = "sort", required = false) String sort,
                               Model model) {
        model.addAttribute("services", serviceService.findAllSorted(sort));
        return "services/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("service", ServiceDTO.builder().build());
        return "services/form";
    }

    @PostMapping
    public String createService(@Valid @ModelAttribute("service") ServiceDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "services/form";
        }
        serviceService.create(dto);
        return "redirect:/services";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("service", serviceService.findById(id));
        return "services/form";
    }

    @PostMapping("/edit")
    public String updateService(@Valid @ModelAttribute("service") ServiceDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "services/form";
        }
        serviceService.update(dto);
        return "redirect:/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceService.delete(id);
        return "redirect:/services";
    }
}