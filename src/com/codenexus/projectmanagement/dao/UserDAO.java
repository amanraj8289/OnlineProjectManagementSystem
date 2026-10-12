
        package com.codenexus.projectmanagement.dao;

import com.codenexus.projectmanagement.model.User;
import com.codenexus.projectmanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
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
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
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
                    return mapUser(result);
                }
            }

        } catch (SQLException e) {
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
                    return mapUser(result);
                }
            }

        } catch (SQLException e) {
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

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Failed to update user!");
            e.printStackTrace();
            return false;
        }
    }

    // 5. UPDATE USER ROLE
    public boolean updateUserRole(int userId, String role) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, role);
            statement.setInt(2, userId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Failed to update user role!");
            e.printStackTrace();
            return false;
        }
    }

    // 6. UPDATE PASSWORD
    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Failed to update password!");
            e.printStackTrace();
            return false;
        }
    }

    // 7. DELETE USER
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Failed to delete user!");
            e.printStackTrace();
            return false;
        }
    }

    // 8. GET ALL USERS
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                users.add(mapUser(result));
            }

        } catch (SQLException e) {
            System.out.println("Failed to get all users!");
            e.printStackTrace();
        }

        return users;
    }

    // 9. MAP DATABASE ROW TO USER
    private User mapUser(ResultSet result) throws SQLException {
        return new User(
                result.getInt("id"),
                result.getString("name"),
                result.getString("email"),
                result.getString("role"),
                result.getString("password")
        );
    }
}