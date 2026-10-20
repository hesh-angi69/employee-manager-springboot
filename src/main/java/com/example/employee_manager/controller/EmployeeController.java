package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    @GetMapping("/")
    public String home() { return "redirect:/employees"; }

    // READ
    @GetMapping("/employees")
    public String list(Model model) {
        model.addAttribute("employees", service.findAll());
        model.addAttribute("employee", new Employee());
        return "employees";
    }

    // CREATE
    @PostMapping("/employees")
    public String create(@Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (employee.getEmail() != null && service.emailExists(employee.getEmail())) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            model.addAttribute("employees", service.findAll());
            return "employees";
        }
        service.save(employee);
        ra.addFlashAttribute("message", "Employee saved successfully");
        return "redirect:/employees";
    }

    // Edit form එක පෙන්නන්න
    @GetMapping("/employees/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee", service.findById(id));
        return "editemployee";
    }

    // UPDATE
    @PostMapping("/employees/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("employee") Employee employee,
                         BindingResult result, RedirectAttributes ra) {
        if (employee.getEmail() != null && service.emailExistsForOther(employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }
        if (result.hasErrors()) {
            employee.setId(id);
            return "editemployee";
        }
        employee.setId(id);
        service.save(employee);
        ra.addFlashAttribute("message", "Employee updated successfully");
        return "redirect:/employees";
    }

    // DELETE
    @PostMapping("/employees/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Employee deleted");
        return "redirect:/employees";
    }
}