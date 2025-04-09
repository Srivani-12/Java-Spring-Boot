package com.example.demosecurity.controller;

import com.example.demosecurity.entity.Employee;
import com.example.demosecurity.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    // Constructor Injection
    public EmployeeController(EmployeeService theEmployeeService) {
        this.employeeService = theEmployeeService;
    }

    // Show Employee List on Home Page (Dashboard)

    public String showDashboard(Model model) {
        // Get employees from DB
        List<Employee> theEmployees = employeeService.findAll();

        // Debugging: Print employee list to console
        System.out.println("Employees: " + theEmployees);

        // Add to Spring Model
        model.addAttribute("employees", theEmployees);

        return "home"; // Loads home.html (dashboard)
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        Employee theEmployee = new Employee();
        model.addAttribute("employee", theEmployee);

        return "employee-form"; // Employee form will be shown inside home.html
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
        // Save the employee
        employeeService.save(theEmployee);

        // Redirect to home page (where employees list is shown)
        return "redirect:/";
    }


    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int theId, Model model) {
        // Get the employee from the service
        Employee theEmployee = employeeService.findById(theId);

        // Set employee in the model to prepopulate the form
        model.addAttribute("employee", theEmployee);

        // Show form inside home.html
        return "employee-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int theId) {
        employeeService.deleteById(theId);
        return "redirect:/"; // Redirect back to dashboard
    }
}
