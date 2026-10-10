package com.codenexus.projectmanagement.dao;

import com.codenexus.projectmanagement.model.User;
import com.codenexus.projectmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // 1. SAVE USER
    public int saveUser(User user) {
        String sql = "INSERT INTO users (name, email, role, password) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getPassword());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("User saved successfully!");
                    System.out.println("New User ID: " + id);
                    return id;
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to save user!");
            e.printStackTrace();
        }

        return -1;
    }

    // 2. GET USER BY ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("role"),
                            result.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to get user!");
            e.printStackTrace();
        }

        return null;
    }

    // 3. GET USER BY EMAIL
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new User(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getString("email"),
                            result.getString("role"),
                            result.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to find user by email!");
            e.printStackTrace();
        }

        return null;
    }

    // 4. UPDATE USER DETAILS
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ?, role = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setInt(4, user.getId());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("User updated successfully!");
                return true;
            }

            System.out.println("User not found for update!");

        } catch (Exception e) {
            System.out.println("Failed to update user!");
            e.printStackTrace();
        }

        return false;
    }

    // 5. UPDATE PASSWORD WITH HASH
    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Password updated successfully!");
                return true;
            }

            System.out.println("User not found for password update!");

        } catch (Exception e) {
            System.out.println("Failed to update password!");
            e.printStackTrace();
        }

        return false;
    }

    // 6. DELETE USER
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("User deleted successfully!");
                return true;
            }

            System.out.println("User not found for delete!");

        } catch (Exception e) {
            System.out.println("Failed to delete user!");
            e.printStackTrace();
        }

        return false;
    }

    // 7. GET ALL USERS
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                User user = new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("role"),
                        result.getString("password")
                );

                users.add(user);
            }

        } catch (Exception e) {
            System.out.println("Failed to get all users!");
            e.printStackTrace();
        }

        return users;
    }
}