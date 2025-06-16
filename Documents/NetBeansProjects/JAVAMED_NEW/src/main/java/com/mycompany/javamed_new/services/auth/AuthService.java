package com.mycompany.javamed_new.services.auth;

import com.mycompany.javamed_new.services.sessioncontext.SessionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Map;
import persistence.DataFetcher;

public class AuthService {
    private final DataFetcher dataFetcher;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService() {
        this.dataFetcher = new DataFetcher(); // 🔹 Inicializa correctamente DataFetcher
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Error: Usuario o contraseña vacíos.");
            return false;
        }

        int userId = getUserId(username);
        if (userId == -1) {
            System.out.println("❌ Error: Usuario no encontrado.");
            return false;
        }

        String storedHash = getUserPasswordHash(userId);
        if (storedHash == null || storedHash.isEmpty()) {
            System.out.println("❌ Error: Contraseña no encontrada.");
            return false;
        }

        // 🔹 Comparar contraseña con BCrypt
        boolean passwordMatch = checkPassword(password, storedHash);
        if (passwordMatch) {
            String position = getUserPosition(username);
            SessionService.startSession(username, position);
            System.out.println("✅ Usuario autenticado correctamente.");
            return true;
        }

        System.out.println("❌ Error: Contraseña incorrecta.");
        return false;
    }

    public int getUserId(String username) {
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("id"),
            "users",
            Map.of("username", username)
        );

        if (result == null || !result.containsKey("id")) {
            System.out.println("❌ Error: No se encontró `id` para el usuario " + username);
            return -1;
        }

        return Integer.parseInt(result.get("id").toString());
    }

    private String getUserPasswordHash(int userId) {
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("password_hash"),
            "passwords",
            Map.of("id_user", String.valueOf(userId))
        );
        return result != null && result.containsKey("password_hash") ? result.get("password_hash").toString() : "";
    }

    public static String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password); // 🔹 Se cifra con un salt interno
    }

    public boolean checkPassword(String inputPassword, String storedHash) {
        return encoder.matches(inputPassword, storedHash); // 🔹 Compara la entrada con el hash cifrado
    }

    private String getUserPosition(String username) {
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("position"),
            "users",
            Map.of("username", username)
        );

        if (result == null || !result.containsKey("position")) {
            System.out.println("❌ Error: No se encontró `position` en la base de datos para el usuario " + username);
            return null; // 🔹 No asignamos "reception", dejamos que la DB gestione el valor
        }

        return result.get("position").toString();
    }

}
