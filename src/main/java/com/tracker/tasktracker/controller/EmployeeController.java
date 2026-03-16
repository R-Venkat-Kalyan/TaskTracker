package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.FeedBackEntity;
import com.tracker.tasktracker.entity.LeaveEntity;
import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.FeedBackService;
import com.tracker.tasktracker.service.LeaveService;
import com.tracker.tasktracker.service.MilestoneLogService;
import com.tracker.tasktracker.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final MilestoneLogService milestoneLogService;
    private final LeaveService leaveService;
    private final FeedBackService feedBackService;
    private final UserService userService;


    @GetMapping("/dashboard")
    public String employeeDashBoard(@RequestParam(required = false) String month, HttpSession session, Model model) {

        UserEntity user = (UserEntity) session.getAttribute("user");

        if (month == null) {
            month = java.time.YearMonth.now().toString();
        }

        YearMonth ym = YearMonth.parse(month);

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();


        /* ---- STORY POINTS ---- */

        double storyPoints = 0;

        for (MilestoneLogEntity log :
                milestoneLogService.findStoryPoints(user.getId(), start, end)) {

            storyPoints += log.getStoryPoint();
        }


        /* ---- EFFORT HOURS ---- */

        int effortHours = 0;

        for (MilestoneLogEntity log :
                milestoneLogService.findEffortHours(user.getId(), start, end)) {

            effortHours += log.getEffortHours();
        }


        /* ---- WORKING DAYS ---- */

        Set<LocalDate> dates = new HashSet<>();

        for (MilestoneLogEntity log :
                milestoneLogService.findDates(user.getId(), start, end)) {

            dates.add(log.getDate());
        }

        int workingDays = dates.size();


        /* ---- PLANNED LEAVES ---- */

        int plannedLeaves = 0;

        for (LeaveEntity leave :
                leaveService.getLeavesForMonth(user.getId(), start, end)) {

            plannedLeaves += leave.getTotalDays();
        }


        // Map values to thymeleaf
        model.addAttribute("name", user.getFullName());
        model.addAttribute("mail", user.getEmail());
        model.addAttribute("id", user.getEmpId());
        model.addAttribute("project", user.getProject());
        model.addAttribute("month", month);
        model.addAttribute("storyPoints", storyPoints);
        model.addAttribute("effortHours", effortHours);
        model.addAttribute("workingDays", workingDays);
        model.addAttribute("plannedLeaveDays", plannedLeaves);

        return "employee-pages/dashboard";
    }

    @GetMapping("/milestone-tracker")
    public String milestoneTracker(Model model, HttpSession session, @RequestParam(required = false) String month) {
        UserEntity user = (UserEntity) session.getAttribute("user");

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
        logData.setId(null);
        logData.setUserId(user.getId());
        logData.setEmpId(user.getEmpId());
        logData.setEmpName(user.getFullName());
        logData.setCreatedAt(LocalDate.now());
        logData.setUpdatedAt(LocalDate.now());
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

        model.addAttribute("mainContent", "/employee-pages/edit-milestone-data");
        return "/employee-pages/layout";
    }


    @PostMapping("/update-milestone")
    public String updateLog(@ModelAttribute MilestoneLogEntity log,
                            HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");

        MilestoneLogEntity existing = milestoneLogService.getById(log.getId());

        // Only update user-editable fields
        existing.setSprint(log.getSprint());
        existing.setRelease(log.getRelease());
        existing.setDate(log.getDate());
        existing.setProgram(log.getProgram());
        existing.setJiraType(log.getJiraType());
        existing.setPriority(log.getPriority());
        existing.setUserStoryId(log.getUserStoryId());
        existing.setJiraId(log.getJiraId());
        existing.setProductionDefect(log.isProductionDefect());
        existing.setEffortCategory(log.getEffortCategory());
        existing.setDescription(log.getDescription());
        existing.setStoryPoint(log.getStoryPoint());
        existing.setBugRca(log.getBugRca());
        existing.setEffortHours(log.getEffortHours());
        existing.setStatus(log.getStatus());

        existing.setUpdatedAt(LocalDate.now());

        milestoneLogService.saveMilestoneData(existing);

        return "redirect:/employee/milestone-tracker";
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
    public String applyLeave(@ModelAttribute LeaveEntity leave, HttpSession session, RedirectAttributes redirectAttributes) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        leave.setId(null);
        leave.setUserId(user.getId());
        leave.setEmpName(user.getFullName());
        leave.setEmpId(user.getEmpId());

        // Duplicate/overlap check
        boolean overlaps = leaveService.overlapsExisting(
                user.getId(),
                leave.getStartDate(),
                leave.getEndDate()
        );

        if (overlaps) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "You already have a leave overlapping these dates. Please adjust the range.");
            return "redirect:/employee/leave-tracker";
        }
        leaveService.saveLeave(leave);

        return "redirect:/employee/leave-tracker";
    }

    @GetMapping("/delete-leave/{id}")
    public String deleteLeave(
            @PathVariable String id) {

        leaveService.deleteLeave(id);

        return "redirect:/employee/leave-tracker";
    }

    @GetMapping("/feedback")
    public String feedback(Model model) {
        model.addAttribute("mainContent", "/feedback");
        return "/employee-pages/layout";

    }

    @PostMapping("/submit-feedback")
    public String saveFeedback(@ModelAttribute FeedBackEntity feedBack, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        feedBack.setEmpId(user.getEmpId());
        feedBack.setEmpName(user.getFullName());
        feedBack.setEmpMail(user.getEmail());
        feedBack.setCreatedAt(LocalDate.now());
        feedBackService.saveFeedBack(feedBack);

        redirectAttributes.addFlashAttribute("successMessage", "FeedBack Submitted !!");
        return "redirect:/employee/dashboard";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("mainContent", "/employee-pages/update-password");
        return "/employee-pages/layout";
    }

    @PostMapping("/update-password")
    public String updatePassword(HttpSession session, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        UserEntity existing = userService.findUserById(user.getId());

        existing.setPassword(newPassword);
        existing.setUpdatedAt(LocalDateTime.now());
        userService.saveUser(existing);

        redirectAttributes.addFlashAttribute("successMessage", "Password Updated !!");
        session.invalidate();
        return "redirect:/sign-in";
    }

}
