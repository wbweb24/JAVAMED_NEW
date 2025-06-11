package com.mycompany.javamed_new.services.auth;

import com.mycompany.javamed_new.services.sessioncontext.SessionService;
import java.util.List;
import java.util.Map;
import persistence.DataFetcher;

public class AuthService {
    private DataFetcher dataFetcher;
    private PasswordManager passwordManager;

    public AuthService(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
        this.passwordManager = new PasswordManager();
    }

    public boolean authenticateUser(String username, String password) {
        String storedHash = getUserPasswordHash(username);
        if (storedHash != null && passwordManager.checkPassword(password, storedHash)) {
            String position = getUserPosition(username);  // 🔹 Obtiene el puesto
            SessionService.startSession(username, position);  // 🔹 Guarda en sesión
            return true;
        }
        return false;
    }


    private String getUserPasswordHash(String username) {
        String query = "SELECT password_hash FROM users_credentials WHERE username = ?";
        Map<String, Object> result = dataFetcher.fetchData(List.of("password_hash"), username);
        return result.getOrDefault("password_hash", "").toString();
    }
    
     private String getUserPosition(String username) {
        String query = "SELECT position FROM users_credentials WHERE username = ?";
        Map<String, Object> result = dataFetcher.fetchData(List.of("position"), username);
        return result.getOrDefault("position", "reception").toString();  // 🔹 Default: "reception"
    }
}
