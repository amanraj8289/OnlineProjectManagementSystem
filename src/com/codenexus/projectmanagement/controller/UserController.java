
        package com.codenexus.projectmanagement.controller;

import com.codenexus.projectmanagement.dao.UserDAO;
import com.codenexus.projectmanagement.model.User;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    private final UserDAO userDAO = new UserDAO();

    private static final Set<String> ALLOWED_ROLES =
            Set.of("ADMIN", "PROJECT_MANAGER", "TEAM_MEMBER");

    // Show users: ADMIN only
    @GetMapping("/users")
    public String showUsers(HttpSession session, Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<User> users = userDAO.getAllUsers();
        model.addAttribute("users", users);

        return "users";
    }

    // Update role: ADMIN only
    @PostMapping("/users/update-role")
    public String updateUserRole(
            @RequestParam("userId") int userId,
            @RequestParam("role") String role,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        String normalizedRole = role.trim().toUpperCase();

        if (!ALLOWED_ROLES.contains(normalizedRole)) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid role selected."
            );
            return "redirect:/users";
        }

        // Prevent the current admin from accidentally removing their own admin role.
        Object currentUserId = session.getAttribute("userId");

        if (currentUserId instanceof Number
                && ((Number) currentUserId).intValue() == userId
                && !"ADMIN".equals(normalizedRole)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "You cannot remove your own ADMIN role."
            );
            return "redirect:/users";
        }

        boolean updated = userDAO.updateUserRole(userId, normalizedRole);

        if (updated) {
            redirectAttributes.addFlashAttribute(
                    "successMessage", "User role updated successfully."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Role update failed. Please check the user ID."
            );
        }

        return "redirect:/users";
    }

    private boolean isAdmin(HttpSession session) {
        return session != null
                && session.getAttribute("userId") != null
                && "ADMIN".equalsIgnoreCase(
                String.valueOf(session.getAttribute("role"))
        );
    }
}