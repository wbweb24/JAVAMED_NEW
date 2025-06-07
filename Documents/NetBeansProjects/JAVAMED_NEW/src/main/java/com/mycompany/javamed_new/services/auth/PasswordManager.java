package com.mycompany.javamed_new.services.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordManager {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean checkPassword(String inputPassword, String storedPassword) {
        return encoder.matches(inputPassword, storedPassword);
    }
}
