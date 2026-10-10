package com.codenexus.projectmanagement.controller;

import com.codenexus.projectmanagement.dao.ProjectDAO;
import com.codenexus.projectmanagement.model.Project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProjectController {

    private final ProjectDAO projectDAO = new ProjectDAO();

    @GetMapping("/projects")
    public String showProjects(Model model) {
        List<Project> projects = projectDAO.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping("/projects/new")
    public String showAddProjectForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("pageTitle", "Add Project");
        model.addAttribute("formAction", "/projects/save");
        return "project-form";
    }

    @PostMapping("/projects/save")
    public String saveProject(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (name.isBlank() || description.isBlank()
                || !isValidStatus(status)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please enter valid project details.");
            return "redirect:/projects/new";
        }

        Project project = new Project(0, name.trim(),
                description.trim(), status);

        projectDAO.saveProject(project);
        redirectAttributes.addFlashAttribute(
                "message", "Project added. Please check the projects list.");
        return "redirect:/projects";
    }

    @GetMapping("/projects/edit")
    public String showEditProjectForm(
            @RequestParam int id, Model model) {

        Project project = projectDAO.getProjectById(id);

        if (project == null) {
            return "redirect:/projects";
        }

        model.addAttribute("project", project);
        model.addAttribute("pageTitle", "Edit Project");
        model.addAttribute("formAction", "/projects/update");
        return "project-form";
    }

    @PostMapping("/projects/update")
    public String updateProject(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (id <= 0 || name.isBlank() || description.isBlank()
                || !isValidStatus(status)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please enter valid project details.");
            return "redirect:/projects";
        }

        Project existingProject = projectDAO.getProjectById(id);
        if (existingProject == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Project not found.");
            return "redirect:/projects";
        }

        Project project = new Project(id, name.trim(),
                description.trim(), status);

        projectDAO.updateProject(project);
        redirectAttributes.addFlashAttribute(
                "message", "Project updated.");
        return "redirect:/projects";
    }

    @PostMapping("/projects/delete")
    public String deleteProject(
            @RequestParam int id,
            RedirectAttributes redirectAttributes) {

        if (id <= 0 || projectDAO.getProjectById(id) == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Project not found.");
            return "redirect:/projects";
        }

        projectDAO.deleteProject(id);
        redirectAttributes.addFlashAttribute(
                "message", "Delete request processed.");
        return "redirect:/projects";
    }

    private boolean isValidStatus(String status) {
        return status != null && (
                status.equals("ACTIVE")
                        || status.equals("IN_PROGRESS")
                        || status.equals("COMPLETED")
                        || status.equals("ON_HOLD"));
    }
}