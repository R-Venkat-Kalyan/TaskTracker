package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.MilestoneLogService;
import com.tracker.tasktracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;

    private final MilestoneLogService milestoneLogService;

    @GetMapping("/dashboard")
    public String employeeDashBoard(HttpSession session, Model model){
        // Get id from session
        UserEntity user = (UserEntity) session.getAttribute("user");

        if(user == null){
            return "redirect:/sign-in";
        }

        // Map values to thymeleaf
        model.addAttribute("name", user.getFullName());
        model.addAttribute("mail", user.getEmail());
        model.addAttribute("id", user.getEmpId());
        model.addAttribute("project", user.getProject());
        return "/employee-pages/dashboard";
    }

    @GetMapping("/milestone-tracker")
    public String milestoneTracker(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        List<MilestoneLogEntity> logs = milestoneLogService.findByUserId(user.getId());
        model.addAttribute("logs",logs);
        model.addAttribute("mainContent", "/employee-pages/milestone-tracker");
        return "/employee-pages/layout";
    }

    @PostMapping("/save-milestone")
    public String saveMilestoneLog(@ModelAttribute MilestoneLogEntity logData, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("user");
        if(user == null){
            return "redirect:/sign-in";
        }
        logData.setUserId(user.getId());
        logData.setEmpId(user.getEmpId());
        logData.setEmpName(user.getFullName());
        milestoneLogService.saveMilestoneData(logData);
        return "redirect:/employee/milestone-tracker";
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
