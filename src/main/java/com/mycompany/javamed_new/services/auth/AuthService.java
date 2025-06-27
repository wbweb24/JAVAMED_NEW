package com.mycompany.javamed_new.services.auth;

import com.mycompany.javamed_new.persistence.DataFetcher;
import com.mycompany.javamed_new.services.sessioncontext.SessionService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

public class AuthService {

    private final DataFetcher dataFetcher;
    private final BCryptPasswordEncoder encoder;

    // ✅ Constructor con inyección de dependencia
    public AuthService(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
        this.encoder = new BCryptPasswordEncoder();
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Error: Usuario o contraseña vacíos.");
            return false;
        }

        Map<String, Object> userInfo = dataFetcher.fetchData(
            List.of("id", "position"), "users", Map.of("username", username)
        );

        if (userInfo == null || !userInfo.containsKey("id")) {
            System.out.println("❌ Error: Usuario no encontrado.");
            return false;
        }

        int userId = Integer.parseInt(userInfo.get("id").toString());

        String storedHash = getUserPasswordHash(userId);
        if (storedHash == null || storedHash.isEmpty()) {
            System.out.println("❌ Error: Contraseña no encontrada.");
            return false;
        }

        boolean passwordMatch = checkPassword(password, storedHash);
        if (passwordMatch) {
            String position = userInfo.containsKey("position")
                ? userInfo.get("position").toString()
                : null;
            SessionService.startSession(username, position);
            System.out.println("✅ Usuario autenticado correctamente.");
            return true;
        }

        System.out.println("❌ Error: Contraseña incorrecta.");
        return false;
    }

    private String getUserPasswordHash(int userId) {
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("password_hash"), "passwords", Map.of("id_user", String.valueOf(userId))
        );

        return result != null && result.containsKey("password_hash")
            ? result.get("password_hash").toString()
            : "";
    }

    public static String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public boolean checkPassword(String inputPassword, String storedHash) {
        return encoder.matches(inputPassword, storedHash);
    }

    public int getUserId(String username) {   
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("id"), "users", Map.of("username", username)
        );
        return (result != null && result.containsKey("id"))
            ? Integer.parseInt(result.get("id").toString())
            : -1;
        }
}
