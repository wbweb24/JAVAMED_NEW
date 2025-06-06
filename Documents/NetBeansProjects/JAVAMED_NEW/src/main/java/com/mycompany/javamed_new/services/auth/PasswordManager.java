package com.mycompany.javamed_new.services.auth;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

    // ✅ Método para generar un hash seguro a partir de una contraseña
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    // ✅ Método para verificar si una contraseña es correcta comparándola con su hash
    public boolean checkPassword(String inputPassword, String storedPassword) {
        return BCrypt.checkpw(inputPassword, storedPassword);
    }
}
