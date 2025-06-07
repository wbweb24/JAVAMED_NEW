package com.mycompany.javamed_new.services.auth;

import persistence.DataFetcher;
import persistence.config.DbManager;

public class AuthService {
    private DataFetcher dataFetcher;
    private PasswordManager passwordManager;

    public AuthService(DbManager dbManager) {
        this.dataFetcher = new DataFetcher(dbManager); // ✅ Ahora recibe `DbManager`
        this.passwordManager = new PasswordManager();
    }

    

    public boolean authenticateUser(String username, String password) {
        String storedHash = dataFetcher.getUserPasswordHash(username);
        return storedHash != null && passwordManager.checkPassword(password, storedHash);
    }
}
