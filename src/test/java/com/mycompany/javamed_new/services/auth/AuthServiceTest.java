/*package com.mycompany.javamed_new.services.auth;

import com.mycompany.javamed_new.persistence.DataFetcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Test
    void autenticaUsuarioValidoConCredencialesCorrectas() {
        // 🧪 Simular DataFetcher con Mockito
        DataFetcher mockFetcher = mock(DataFetcher.class);

        String testUsername = "testUser";
        String testPassword = "testPass123";
        String hashedPassword = PasswordManager.hashPassword(testPassword);

        // 📋 Simular respuesta para tabla "users"
        when(mockFetcher.fetchData(
                List.of("id", "position"),
                "users",
                Map.of("username", testUsername)
        )).thenReturn(Map.of("id", "101", "position", "admin"));

        // 📋 Simular respuesta para tabla "passwords"
        when(mockFetcher.fetchData(
                List.of("password_hash"),
                "passwords",
                Map.of("id_user", "101")
        )).thenReturn(Map.of("password_hash", hashedPassword));

        // 🚀 Ejecutar autenticación
        AuthService authService = new AuthService(mockFetcher);
        boolean autenticado = authService.authenticateUser(testUsername, testPassword);

        // ✅ Verificación
        assertTrue(autenticado, "El usuario debe autenticarse correctamente con los datos simulados");
    }
}
*/