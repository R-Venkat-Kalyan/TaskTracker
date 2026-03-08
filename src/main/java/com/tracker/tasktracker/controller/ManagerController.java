package com.tracker.tasktracker.controller;

import com.tracker.tasktracker.entity.UserEntity;
import com.tracker.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    @GetMapping("/dashboard")
    public String managerDashBoard(){
        return "/manager-pages/dashboard";
    }

    @GetMapping("/milestone-tracker")
    public String milestoneTracker(Model model) {
        model.addAttribute("mainContent", "/manager-pages/milestone-tracker");
        return "/manager-pages/layout";
    }

    @GetMapping("/leave-tracker")
    public String leaveeTracker(Model model) {
        model.addAttribute("mainContent", "/manager-pages/leave-tracker");
        return "/manager-pages/layout";
    }

    @GetMapping("/add-user")
    public String addNewEmployee(Model model) {
        model.addAttribute("mainContent", "/manager-pages/add-user");
        return "/manager-pages/layout";
    }

    private final UserService userService;

    @PostMapping("/save-user")
    public String saveUser(@ModelAttribute UserEntity user){
        userService.saveUser(user);
        return "redirect:/manager/dashboard";
    }

    @GetMapping("/view-users")
    public String viewUsers(Model model) {
        List<UserEntity> users = userService.findAllUsers();
        model.addAttribute("employees", users);
        model.addAttribute("mainContent", "/manager-pages/view-users");
        return "/manager-pages/layout";
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(Model model, @PathVariable String id) {
        UserEntity user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("mainContent", "/manager-pages/edit-user");
        return "/manager-pages/layout";
    }

    @PostMapping("/update-user")
    public String updateUser(UserEntity user) {

        UserEntity existing = userService.findUserById(user.getId());

        if(existing != null) {

            existing.setFullName(user.getFullName());
            existing.setOrganisation(user.getOrganisation()) ;
            existing.setProject(user.getProject());

            if(user.getPassword() != null && !user.getPassword().isEmpty()) {
                existing.setPassword(user.getPassword());
            }

            userService.saveUser(existing);
        }
        return "redirect:/manager/view-users";
    }

    @GetMapping("/save-as/{id}")
    public String cloneUser(@PathVariable String id, Model model) {

        UserEntity user = userService.findUserById(id);

        model.addAttribute("user", user);

        return "manager-pages/clone-user";
    }

}
