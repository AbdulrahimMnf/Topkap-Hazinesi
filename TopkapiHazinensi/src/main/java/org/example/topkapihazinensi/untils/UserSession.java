package org.example.topkapihazinensi.untils;

public class UserSession {

    private static UserSession instance;

    private final int userId;
    private final String username;
    private final String userEmail;

    public UserSession(int userId, String username, String userEmail) {
        this.userId = userId;
        this.username = username;
        this.userEmail = userEmail;

    }

    public  void login() {
            instance = new UserSession(userId, username, userEmail);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
