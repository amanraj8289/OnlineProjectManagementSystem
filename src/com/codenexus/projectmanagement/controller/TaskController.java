package com.codenexus.projectmanagement.controller;

import com.codenexus.projectmanagement.dao.TaskDAO;
import com.codenexus.projectmanagement.model.Task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TaskController {

    private final TaskDAO taskDAO = new TaskDAO();

    // Show all tasks
    @GetMapping("/tasks")
    public String showTasks(Model model) {
        List<Task> tasks = taskDAO.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks";
    }

    // Show add task form
    @GetMapping("/tasks/new")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("pageTitle", "Add Task");
        model.addAttribute("formAction", "/tasks/save");
        return "task-form";
    }

    // Save new task
    @PostMapping("/tasks/save")
    public String saveTask(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (title.isBlank() || description.isBlank()
                || !isValidStatus(status)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please enter valid task details.");
            return "redirect:/tasks/new";
        }

        Task task = new Task(0, title.trim(), description.trim(), status);
        taskDAO.saveTask(task);

        redirectAttributes.addFlashAttribute(
                "message", "Task creation request processed.");
        return "redirect:/tasks";
    }

    // Show edit task form
    @GetMapping("/tasks/edit")
    public String showEditTaskForm(
            @RequestParam int id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Task task = taskDAO.getTaskById(id);

        if (task == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Task not found.");
            return "redirect:/tasks";
        }

        model.addAttribute("task", task);
        model.addAttribute("pageTitle", "Edit Task");
        model.addAttribute("formAction", "/tasks/update");
        return "task-form";
    }

    // Update existing task
    @PostMapping("/tasks/update")
    public String updateTask(
            @RequestParam int id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (id <= 0 || title.isBlank() || description.isBlank()
                || !isValidStatus(status)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Please enter valid task details.");
            return "redirect:/tasks";
        }

        Task existingTask = taskDAO.getTaskById(id);

        if (existingTask == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Task not found.");
            return "redirect:/tasks";
        }

        Task task = new Task(id, title.trim(), description.trim(), status);
        taskDAO.updateTask(task);

        redirectAttributes.addFlashAttribute(
                "message", "Task update request processed.");
        return "redirect:/tasks";
    }

    // Delete task
    @PostMapping("/tasks/delete")
    public String deleteTask(
            @RequestParam int id,
            RedirectAttributes redirectAttributes) {

        if (id <= 0 || taskDAO.getTaskById(id) == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "Task not found.");
            return "redirect:/tasks";
        }

        taskDAO.deleteTask(id);

        redirectAttributes.addFlashAttribute(
                "message", "Task deletion request processed.");
        return "redirect:/tasks";
    }

    private boolean isValidStatus(String status) {
        return status != null && (
                status.equals("PENDING")
                        || status.equals("IN_PROGRESS")
                        || status.equals("COMPLETED"));
    }
}