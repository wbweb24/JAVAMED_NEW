package com.mycompany.javamed_new.services.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordManager {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        PasswordManager manager = new PasswordManager();
        String hash = manager.hashPassword("123"); // 🔹 Prueba con una contraseña simple
        System.out.println("✅ Nuevo Hash BCrypt: " + hash);
    }

    public String hashPassword(String password) {
        return encoder.encode(password); // 🔹 Se cifra con `BCrypt`, sin necesidad de `salt` manual
    }

    public boolean checkPassword(String inputPassword, String storedPassword) {
        return encoder.matches(inputPassword, storedPassword);
    }
}
