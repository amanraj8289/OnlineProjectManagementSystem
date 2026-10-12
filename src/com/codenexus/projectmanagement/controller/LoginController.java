package com.codenexus.projectmanagement.controller;

import com.codenexus.projectmanagement.dao.UserDAO;
import com.codenexus.projectmanagement.model.User;
import com.codenexus.projectmanagement.service.PasswordService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserDAO userDAO = new UserDAO();
    private final PasswordService passwordService = new PasswordService();

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest request) {

        User user = userDAO.getUserByEmail(email);

        if (user != null
                && passwordService.verifyPassword(
                password, user.getPassword())) {

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("name", user.getName());
            session.setAttribute("role", user.getRole());
            model.addAttribute("name", user.getName());
            model.addAttribute("role", user.getRole());

            String role = user.getRole();

            if ("ADMIN".equalsIgnoreCase(role)) {
                return "admin-dashboard";
            } else if ("PROJECT_MANAGER".equalsIgnoreCase(role)) {
                return "manager-dashboard";
            } else {
                return "dashboard";
            }
        }

        model.addAttribute(
                "error",
                "Invalid email or password. Please try again."
        );

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }
}