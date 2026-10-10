package com.codenexus.projectmanagement.controller;

import com.codenexus.projectmanagement.dao.UserDAO;
import com.codenexus.projectmanagement.model.User;
import com.codenexus.projectmanagement.service.PasswordService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserDAO userDAO = new UserDAO();
    private final PasswordService passwordService = new PasswordService();

    // Show login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Process login form securely
    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {

        User user = userDAO.getUserByEmail(email);

        if (user != null
                && passwordService.verifyPassword(
                password,
                user.getPassword())) {

            model.addAttribute("name", user.getName());
            model.addAttribute("role", user.getRole());

            return "dashboard";
        }

        model.addAttribute(
                "error",
                "Invalid email or password. Please try again."
        );

        return "login";
    }
}