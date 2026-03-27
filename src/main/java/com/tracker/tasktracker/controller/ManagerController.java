package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.FeedBackEntity;
import com.tracker.tasktracker.entity.LeaveEntity;
import com.tracker.tasktracker.entity.MilestoneLogEntity;
import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final UserService userService;
    private final MilestoneLogService milestoneLogService;
    private final LeaveService leaveService;
    private final FeedBackService feedBackService;

    @GetMapping("/dashboard")
    public String managerDashBoard(Model model, HttpSession session, @RequestParam(required = false) String month) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            if (month == null) {
                month = java.time.YearMonth.now().toString();
            }

            YearMonth ym = YearMonth.parse(month);

            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();

            model.addAttribute("name", user.getFullName());
            model.addAttribute("mail", user.getEmail());
            model.addAttribute("id", user.getEmpId());
            model.addAttribute("project", user.getProject());
            model.addAttribute("month", month);

            model.addAttribute("totalStoryPoints", milestoneLogService.getTotalStoryPoints(month));

            model.addAttribute("totalEffortHours", milestoneLogService.getTotalEffortHours(month));

            model.addAttribute("totalWorkingDays", milestoneLogService.getTotalWorkingDays(month));

            model.addAttribute("totalPlannedLeaves", leaveService.getTotalPlannedLeaves(month));

            model.addAttribute("employeeCount", userService.countByRoleIgnoreCase("employee"));
            model.addAttribute("mainContent", "manager-pages/dashboard");
            return "manager-pages/layout";
        }

        return "redirect:/sign-out";
    }

    @GetMapping("/milestone-tracker")
    public String milestoneTracker(Model model, HttpSession session, @RequestParam(required = false) String month) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {

            if (month == null) {
                month = java.time.YearMonth.now().toString();
            }

            YearMonth ym = YearMonth.parse(month);

            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();

            List<MilestoneLogEntity> logs = milestoneLogService.getAllLogsForMonth(start, end);
            model.addAttribute("month", month);
            model.addAttribute("logs", logs);

            model.addAttribute("mainContent", "manager-pages/milestone-tracker");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }

    @GetMapping("/leave-tracker")
    public String leaveeTracker(Model model, @RequestParam(required = false) String month, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {

            // 1. Initialize selectedMonth and handle null/empty month immediately
            YearMonth selectedMonth;
            if (month == null || month.isEmpty()) {
                selectedMonth = YearMonth.now();
            } else {
                selectedMonth = YearMonth.parse(month);
            }

            // 2. Create a 'safe' string version (e.g., "2026-03")
            String safeMonth = selectedMonth.toString();

            // 3. Define the start and end dates for the database query
            LocalDate start = selectedMonth.atDay(1);
            LocalDate end = selectedMonth.atEndOfMonth();

            // 4. Fetch the leaves using the start/end dates
            List<LeaveEntity> leaves = leaveService.getAllLeavesForMonth(start, end);

            // 5. Add attributes to the model
            model.addAttribute("leaves", leaves);
            model.addAttribute("month", safeMonth); // Use safeMonth to update the <input type="month">

            // FIX: Pass 'safeMonth' instead of 'month' to prevent the NullPointerException
            model.addAttribute("totalPlannedLeaves", leaveService.getTotalPlannedLeaves(safeMonth));

            model.addAttribute("mainContent", "manager-pages/leave-tracker");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }

    @GetMapping("/add-user")
    public String addNewEmployee(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("mainContent", "manager-pages/add-user");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute UserEntity user, RedirectAttributes redirectAttributes, HttpSession session) {
        UserEntity sessionUser = (UserEntity) session.getAttribute("user");
        if (sessionUser != null) {
            boolean exists = userService.userExists(user.getEmail(), user.getEmpId());

            if (exists) {
                redirectAttributes.addFlashAttribute("successMessage",
                        "User with same Email or Employee ID already exists!");

                return "redirect:/manager/add-user";
            }

            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("successMessage",
                    "User created successfully!");

            return "redirect:/manager/view-users";
        }
        return "redirect:/sign-out";
    }

    @GetMapping("/view-users")
    public String viewUsers(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            List<UserEntity> users = userService.findAllUsers();
            model.addAttribute("employees", users);
            model.addAttribute("mainContent", "manager-pages/view-users");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }


    @GetMapping("/edit-user/{id}")
    public String editUser(Model model, @PathVariable String id, HttpSession session) {
        UserEntity sessionUser = (UserEntity) session.getAttribute("user");
        if (sessionUser != null) {
            UserEntity user = userService.findUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("mainContent", "manager-pages/edit-user");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }

    @PostMapping("/update-user")
    public String updateUser(UserEntity user, HttpSession session) {

        UserEntity sessionUser = (UserEntity) session.getAttribute("user");
        if (sessionUser != null) {
            UserEntity existing = userService.findUserById(user.getId());

            if (existing != null) {

                existing.setFullName(user.getFullName());
                existing.setOrganisation(user.getOrganisation());
                existing.setProject(user.getProject());

                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    existing.setPassword(user.getPassword());
                }

                userService.saveUser(existing);
            }
            return "redirect:/manager/view-users";
        }
        return "redirect:/sign-out";
    }

    @GetMapping("/save-as/{id}")
    public String cloneUser(@PathVariable String id, Model model, HttpSession session) {
        UserEntity sessionUser = (UserEntity) session.getAttribute("user");
        if (sessionUser != null) {
            UserEntity user = userService.findUserById(id);

            model.addAttribute("user", user);

            return "manager-pages/clone-user";
        }
        return "redirect:/sign-out";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            userService.deleteUserById(id);
            return "redirect:/manager/view-users";
        }
        return "redirect:/sign-out";
    }


    @GetMapping("/users-summary")
    public String teamSummary(@RequestParam(required = false) String month, Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            if (month == null) {
                month = java.time.YearMonth.now().toString();
            }

            YearMonth ym = YearMonth.parse(month);

            LocalDate start = ym.atDay(1);
            LocalDate end = ym.atEndOfMonth();
            // 1) Get all employees
            List<UserEntity> employees = userService.findEmployees();

            // 2) Build summary per user (reusing your services)
            List<SummaryDTO> summaries = new ArrayList<>();

            int workingDays = 0;
            for (UserEntity u : employees) {

                // Logs & Leaves for this month
                List<MilestoneLogEntity> logs = milestoneLogService.getLogsForMonth(u.getId(), start, end);
                List<LeaveEntity> leaves = leaveService.getLeavesForMonth(u.getId(), start, end);

                double totalSP = logs.stream()
                        .filter(l -> l.getStoryPoint() > 0)
                        .mapToDouble(MilestoneLogEntity::getStoryPoint)
                        .sum();

                int totalHours = logs.stream()
                        .mapToInt(MilestoneLogEntity::getEffortHours)
                        .sum();

                workingDays = (int) logs.stream()
                        .map(MilestoneLogEntity::getDate)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()) // unique LocalDate in this month
                        .size();

                int leaveDays = leaves.stream()
                        .mapToInt(LeaveEntity::getTotalDays)
                        .sum();

                summaries.add(SummaryDTO.builder()
                        .userId(u.getId())
                        .empId(u.getEmpId())
                        .fullName(u.getFullName())
                        .totalStoryPoints(totalSP)
                        .totalEffortHours(totalHours)
                        .totalWorkingDays(workingDays)
                        .totalLeaveDays(leaveDays)
                        .build());
            }

            // Optional: order the table (by name, then effort desc, etc.)
            summaries.sort(Comparator.comparing(SummaryDTO::getFullName, String.CASE_INSENSITIVE_ORDER));

            // Optionally compute totals across the team
            double teamSP = summaries.stream().mapToDouble(SummaryDTO::getTotalStoryPoints).sum();
            int teamHours = summaries.stream().mapToInt(SummaryDTO::getTotalEffortHours).sum();
            int teamLeave = summaries.stream().mapToInt(SummaryDTO::getTotalLeaveDays).sum();

            model.addAttribute("teamTotalWorkingDays", milestoneLogService.getTotalWorkingDays(month));
            // "Total working days" across team isn’t a single number with clear meaning, so we omit.
            model.addAttribute("summaries", summaries);
            model.addAttribute("month", ym.toString()); // YYYY-MM for <input type="month">
            model.addAttribute("teamTotalSP", teamSP);
            model.addAttribute("teamTotalHours", teamHours);
            model.addAttribute("teamTotalLeaveDays", teamLeave);
            model.addAttribute("mainContent", "manager-pages/team-summary");
            return "manager-pages/layout";
        }
        return "redirect:/sign-out";
    }


    @GetMapping("/feedback")
    public String feedback(Model model, HttpSession session) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("mainContent", "manager-pages/feedback");
            return "manager-pages/layout";

        }
        return "redirect:/sign-out";
    }

    @PostMapping("/submit-feedback")
    public String saveFeedback(@ModelAttribute FeedBackEntity feedBack, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        if (user != null) {
            feedBack.setEmpId(user.getEmpId());
            feedBack.setEmpName(user.getFullName());
            feedBack.setEmpMail(user.getEmail());
            feedBack.setCreatedAt(LocalDate.now());
            feedBackService.saveFeedBack(feedBack);

            redirectAttributes.addFlashAttribute("successMessage", "FeedBack Submitted !!");
            return "redirect:/manager/dashboard";
        }
        return "redirect:/sign-out";
    }


}
