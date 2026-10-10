package com.codenexus.projectmanagement.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Convert a plain password into a secure hash
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    // Check whether a password matches its stored hash
    public boolean verifyPassword(
            String plainPassword,
            String hashedPassword) {

        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        return passwordEncoder.matches(
                plainPassword,
                hashedPassword
        );
    }
}