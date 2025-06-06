package com.mycompany.javamed_new.services.auth;

import java.util.List;
import java.util.Map;
import persistence.DataFetcher;

public class AuthService {
    private PasswordManager passwordManager;
    private DataFetcher dataFetcher;

    public AuthService(PasswordManager passwordManager, DataFetcher dataFetcher) {
        this.passwordManager = passwordManager;
        this.dataFetcher = dataFetcher;
    }

    public boolean isAuthenticated(String userId, String password) {
        // Solicita solo el password cifrado
        Map<String, Object> userData = dataFetcher.fetchData(List.of("encrypted_password"), userId);

        String storedPassword = (String) userData.get("encrypted_password");
        return passwordManager.checkPassword(password, storedPassword);
    }

    public boolean isValidUser(String userId) {
        // Solicita solo el estado activo del usuario
        Map<String, Object> userData = dataFetcher.fetchData(List.of("active"), userId);
        
        return (Boolean) userData.get("active");
    }
}




