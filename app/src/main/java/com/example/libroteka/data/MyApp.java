package com.example.libroteka.data;

import android.app.Application;

public class MyApp extends Application {
    private String userEmail;
    private String userId;

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
