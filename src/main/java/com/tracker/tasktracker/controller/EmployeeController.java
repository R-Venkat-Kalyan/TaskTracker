package com.tracker.tasktracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @GetMapping("/dashboard")
    public String employeeDashBoard(){
        return "/employee-pages/dashboard";
    }

    @GetMapping("/milestone-tracker")
    public String milestoneTracker(Model model) {
        model.addAttribute("mainContent", "/employee-pages/milestone-tracker");
        return "/employee-pages/layout";
    }

    @GetMapping("/leave-tracker")
    public String leaveeTracker(Model model) {
        model.addAttribute("mainContent", "/employee-pages/leave-tracker");
        return "/employee-pages/layout";
    }

    @GetMapping("/add-milestone-data")
    public String addMileStoneData(Model model) {
        model.addAttribute("mainContent", "/employee-pages/add-milestone-data");
        return "/employee-pages/layout";
    }

    @GetMapping("/add-leave")
    public String addLeave(Model model) {
        model.addAttribute("mainContent", "/employee-pages/add-leave");
        return "/employee-pages/layout";
    }
}
