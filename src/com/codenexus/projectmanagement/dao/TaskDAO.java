package com.codenexus.projectmanagement.dao;

import com.codenexus.projectmanagement.model.Task;
import com.codenexus.projectmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT id, title, description, status FROM tasks ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Task task = new Task(
                        result.getInt("id"),
                        result.getString("title"),
                        result.getString("description"),
                        result.getString("status")
                );

                tasks.add(task);
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve tasks!");
            e.printStackTrace();
        }

        return tasks;
    }

    public void saveTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, status) VALUES (?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus());

            statement.executeUpdate();
            System.out.println("Task saved successfully!");

        } catch (Exception e) {
            System.out.println("Failed to save task!");
            e.printStackTrace();
        }
    }

    public Task getTaskById(int id) {
        String sql = "SELECT id, title, description, status FROM tasks WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Task(
                            result.getInt("id"),
                            result.getString("title"),
                            result.getString("description"),
                            result.getString("status")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to retrieve task!");
            e.printStackTrace();
        }

        return null;
    }

    public void updateTask(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus());
            statement.setInt(4, task.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Failed to update task!");
            e.printStackTrace();
        }
    }

    public void deleteTask(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Failed to delete task!");
            e.printStackTrace();
        }
    }
}
