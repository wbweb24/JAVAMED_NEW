package com.mycompany.javamed_new.services.auth;

import com.mycompany.javamed_new.errors.ErrorsHandler;
import com.mycompany.javamed_new.persistence.DataFetcher;
import com.mycompany.javamed_new.services.sessioncontext.SessionService;

import java.util.List;
import java.util.Map;

public class AuthService {

    private final DataFetcher dataFetcher;

    public AuthService(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            ErrorsHandler.log("login.empty.fields");
            return false;
        }

        Map<String, Object> userInfo = dataFetcher.fetchData(
            List.of("id", "position"), "users", Map.of("username", username)
        );

        if (userInfo == null || !userInfo.containsKey("id")) {
            ErrorsHandler.log("login.failure.generic");
            return false;
        }

        int userId = Integer.parseInt(userInfo.get("id").toString());

        String storedHash = getUserPasswordHash(userId);
        if (storedHash == null || storedHash.isEmpty()) {
            ErrorsHandler.log("login.failure.generic");
            return false;
        }

        boolean passwordMatch = PasswordManager.checkPassword(password, storedHash);
        if (passwordMatch) {
            String position = userInfo.getOrDefault("position", "").toString();
            SessionService.startSession(username, position);
            ErrorsHandler.log("login.success", username);
            return true;
        }

        ErrorsHandler.log("login.failure.generic");
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

    public int getUserId(String username) {
        Map<String, Object> result = dataFetcher.fetchData(
            List.of("id"), "users", Map.of("username", username)
        );
        return (result != null && result.containsKey("id"))
            ? Integer.parseInt(result.get("id").toString())
            : -1;
    }
}
