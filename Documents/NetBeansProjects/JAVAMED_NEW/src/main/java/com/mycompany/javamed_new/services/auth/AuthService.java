package com.mycompany.javamed_new.services.auth;

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
        return storedHash != null && passwordManager.checkPassword(password, storedHash);
    }

    private String getUserPasswordHash(String username) {
        String query = "SELECT password_hash FROM users_credentials WHERE username = ?";
        Map<String, Object> result = dataFetcher.fetchData(List.of("password_hash"), username);
        return result.getOrDefault("password_hash", "").toString();
    }
}
