package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.LeaveEntity;
import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.LeaveService;
import com.tracker.tasktracker.service.MilestoneLogService;
import com.tracker.tasktracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final UserService userService;
    private final MilestoneLogService milestoneLogService;
    private final LeaveService leaveService;

    @GetMapping("/dashboard")
    public String employeeDashBoard(HttpSession session, Model model) {
        // Get id from session
        UserEntity user = (UserEntity) session.getAttribute("user");

        if (user == null) {
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
    public String milestoneTracker(Model model, HttpSession session, @RequestParam(required = false) String month) {
        UserEntity user = (UserEntity) session.getAttribute("user");
//        List<MilestoneLogEntity> logs = milestoneLogService.findByUserId(user.getId());
//        model.addAttribute("logs", logs);
//        model.addAttribute("mainContent", "/employee-pages/milestone-tracker");
//        return "/employee-pages/layout";


        YearMonth selectedMonth;

        if (month == null || month.isEmpty()) {
            selectedMonth = YearMonth.now();
        } else {
            selectedMonth = YearMonth.parse(month);
        }

        LocalDate start = selectedMonth.atDay(1);
        LocalDate end = selectedMonth.atEndOfMonth();

        List<MilestoneLogEntity> logs = milestoneLogService.getLogsForMonth(user.getId(), start, end);
        model.addAttribute("month", selectedMonth.toString());
        model.addAttribute("logs", logs);
        model.addAttribute("mainContent", "/employee-pages/milestone-tracker");
        return "/employee-pages/layout";
    }

    @GetMapping("/add-milestone-data")
    public String addMileStoneData(Model model) {
        model.addAttribute("mainContent", "/employee-pages/add-milestone-data");
        return "/employee-pages/layout";
    }

    @PostMapping("/save-milestone")
    public String saveMilestoneLog(@ModelAttribute MilestoneLogEntity logData, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/sign-in";
        }
        logData.setUserId(user.getId());
        logData.setEmpId(user.getEmpId());
        logData.setEmpName(user.getFullName());
        milestoneLogService.saveMilestoneData(logData);
        return "redirect:/employee/milestone-tracker";
    }

    @GetMapping("/delete-data/{id}")
    public String deleteLog(@PathVariable String id) {

        milestoneLogService.deleteLog(id);

        return "redirect:/employee/milestone-tracker";
    }

    @GetMapping("/edit-log/{id}")
    public String editLog(@PathVariable String id, Model model) {

        MilestoneLogEntity log = milestoneLogService.getById(id);

        model.addAttribute("log", log);

        model.addAttribute("mainContent", "/employee-pages/add-milestone-data");
        return "/employee-pages/layout";
    }

    @GetMapping("/save-as/{id}")
    public String cloneLog(@PathVariable String id, Model model) {

        MilestoneLogEntity log = milestoneLogService.getById(id);

        log.setId(null);   // important

        model.addAttribute("log", log);

        model.addAttribute("mainContent", "/employee-pages/add-milestone-data");
        return "/employee-pages/layout";
    }

    @GetMapping("/leave-tracker")
    public String leaveeTracker(Model model, HttpSession session, @RequestParam(required = false) String month) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user == null) {
            return "redirect:/sign-in";
        }
//        List<LeaveEntity> leaves = leaveService.getLeavesByUserId(user.getId());
//        model.addAttribute("leaves",leaves);
//        model.addAttribute("mainContent", "/employee-pages/leave-tracker");
//        return "/employee-pages/layout";

//        String userId = (String) session.getAttribute("userId");

        YearMonth selectedMonth;

        if (month == null || month.isEmpty()) {
            selectedMonth = YearMonth.now();
        } else {
            selectedMonth = YearMonth.parse(month);
        }

        LocalDate start = selectedMonth.atDay(1);
        LocalDate end = selectedMonth.atEndOfMonth();

        List<LeaveEntity> leaves =
                leaveService.getLeavesForMonth(user.getId(), start, end);

        model.addAttribute("leaves", leaves);
        model.addAttribute("month", selectedMonth.toString());
        model.addAttribute("mainContent", "/employee-pages/leave-tracker");
        return "/employee-pages/layout";

    }

    @GetMapping("/add-leave")
    public String addLeave(Model model) {
        model.addAttribute("mainContent", "/employee-pages/add-leave");
        return "/employee-pages/layout";
    }

    @PostMapping("/save-leave")
    public String applyLeave(@ModelAttribute LeaveEntity leave, HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");

        leave.setUserId(user.getId());
        leave.setEmpName(user.getFullName());
        leave.setEmpId(user.getEmpId());

        leaveService.saveLeave(leave);

        return "redirect:/employee/dashboard";
    }

    @GetMapping("/delete-leave/{id}")
    public String deleteLeave(
            @PathVariable String id) {

        leaveService.deleteLeave(id);

        return "redirect:/employee/leave-tracker";
    }

}
