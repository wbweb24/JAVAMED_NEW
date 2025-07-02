/*package com.mycompany.javamed_new.services.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordManagerTest {

    @Test 
    void testHashYVerificacion() {
        String testPassword = "testPassword123"; // 🔐 valor ficticio de prueba

        String hashed = PasswordManager.hashPassword(testPassword);

        assertNotEquals(testPassword, hashed, "El hash nunca debe ser igual a la contraseña original");

        assertTrue(PasswordManager.checkPassword(testPassword, hashed),
            "La contraseña debe validarse correctamente con el hash generado");

        assertFalse(PasswordManager.checkPassword("claveErronea", hashed),
            "Una contraseña incorrecta no debe pasar la verificación");
    }
}
*/