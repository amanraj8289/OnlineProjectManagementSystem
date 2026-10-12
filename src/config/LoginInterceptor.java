
        package com.codenexus.projectmanagement.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        // Public pages
        if (path.equals("/")
                || path.equals("/login")
                || path.equals("/logout")) {
            return true;
        }

        // Login check
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {
            response.sendRedirect(contextPath + "/login");
            return false;
        }

        String role = String.valueOf(session.getAttribute("role"));

        // Admin can access all protected pages
        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        // Only Admin can access Users Management
        if (path.equals("/users") || path.startsWith("/users/")) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only Admin can manage users."
            );
            return false;
        }

        // Project Manager can manage projects and tasks
        if ("PROJECT_MANAGER".equalsIgnoreCase(role)) {
            if (path.equals("/projects")
                    || path.startsWith("/projects/")
                    || path.equals("/tasks")
                    || path.startsWith("/tasks/")) {
                return true;
            }
        }

        // Team Member can view project/task list only
        if ("TEAM_MEMBER".equalsIgnoreCase(role)) {
            if (path.equals("/projects") || path.equals("/tasks")) {
                return true;
            }
        }

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "You do not have permission to access this page."
        );
        return false;
    }
}
