
        package com.codenexus.projectmanagement.dao;

import com.codenexus.projectmanagement.model.User;

public class TestConnection {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        // 1. Save a new user
        User user = new User(
                0,
                "Demo User",
                "demo.user2026@gmail.com",
                "TEAM_MEMBER"
        );

        int newUserId = userDAO.saveUser(user);

        // Continue only if the user was saved successfully
        if (newUserId > 0) {

            // 2. Get the saved user
            User savedUser = userDAO.getUserById(newUserId);

            if (savedUser != null) {
                System.out.println("\nUser found successfully!");
                System.out.println("ID: " + savedUser.getId());
                System.out.println("Name: " + savedUser.getName());
                System.out.println("Email: " + savedUser.getEmail());
                System.out.println("Role: " + savedUser.getRole());
            }

            // 3. Update user
            User updatedUser = new User(
                    newUserId,
                    "Demo User Updated",
                    "demo.user2026@gmail.com",
                    "TEAM_MEMBER"
            );

            userDAO.updateUser(updatedUser);

            // Verify updated user
            User updatedCheck = userDAO.getUserById(newUserId);

            if (updatedCheck != null) {
                System.out.println(
                        "Updated name: " + updatedCheck.getName()
                );
            }

            // 4. Delete the test user
            boolean deleted = userDAO.deleteUser(newUserId);

            if (deleted) {
                System.out.println("\nUser deleted successfully!");

                // Verify that the user no longer exists
                User deletedCheck = userDAO.getUserById(newUserId);

                if (deletedCheck == null) {
                    System.out.println("Delete verified: User no longer exists!");
                } else {
                    System.out.println("User still exists in database!");
                }
            } else {
                System.out.println("User could not be deleted!");
            }

        } else {
            System.out.println(
                    "User could not be saved. Other tests skipped."
            );
        }

        // 5. Display all remaining users
        System.out.println("\nAll remaining users in database:");

        for (User u : userDAO.getAllUsers()) {
            System.out.println(
                    u.getId() + " | " +
                            u.getName() + " | " +
                            u.getEmail() + " | " +
                            u.getRole()
            );
        }
    }
}
