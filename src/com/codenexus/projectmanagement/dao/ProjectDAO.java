package com.codenexus.projectmanagement.dao;

import com.codenexus.projectmanagement.model.Project;
import com.codenexus.projectmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // 1. SAVE PROJECT
    public void saveProject(Project project) {
        String sql = "INSERT INTO projects (name, description, status) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setString(3, project.getStatus());

            statement.executeUpdate();
            System.out.println("Project saved successfully!");

        } catch (Exception e) {
            System.out.println("Failed to save project!");
            e.printStackTrace();
        }
    }

    // 2. GET PROJECT BY ID
    public Project getProjectById(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Project(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("description"),
                            result.getString("status")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to get project!");
            e.printStackTrace();
        }

        return null;
    }

    // 3. UPDATE PROJECT
    public void updateProject(Project project) {
        String sql = "UPDATE projects SET name = ?, description = ?, status = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, project.getName());
            statement.setString(2, project.getDescription());
            statement.setString(3, project.getStatus());
            statement.setInt(4, project.getId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Project updated successfully!");
            } else {
                System.out.println("Project not found!");
            }

        } catch (Exception e) {
            System.out.println("Failed to update project!");
            e.printStackTrace();
        }
    }

    // 4. DELETE PROJECT
    public void deleteProject(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Project deleted successfully!");
            } else {
                System.out.println("Project not found!");
            }

        } catch (Exception e) {
            System.out.println("Failed to delete project!");
            e.printStackTrace();
        }
    }

    // 5. GET ALL PROJECTS AS A LIST
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Project project = new Project(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("status")
                );

                projects.add(project);
            }

        } catch (Exception e) {
            System.out.println("Failed to get all projects!");
            e.printStackTrace();
        }

        return projects;
    }
}