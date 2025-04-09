package com.example.demosecurity.controller;

import com.example.demosecurity.entity.Employee;
import com.example.demosecurity.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DemoController {

    private final EmployeeService employeeService;

    // Constructor Injection for EmployeeService
    public DemoController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String showHome(Model model) {
        // Retrieve the list of employees
        List<Employee> employees = employeeService.findAll();

        // Debugging: Print employees list to console
        System.out.println("Fetched Employees: " + employees);

        // Add employees to the model
        model.addAttribute("employees", employees);

        return "home";  // Maps to home.html
    }

    @GetMapping("/leaders")
    public String showLeaders() {
        return "leaders";  // Maps to leaders.html
    }

    @GetMapping("/systems")
    public String showSystems() {
        return "systems";  // Maps to systems.html
    }
}
