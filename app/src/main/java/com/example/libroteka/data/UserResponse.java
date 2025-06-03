package com.example.libroteka.data;

public class UserResponse {
    private String message;
   private String token;
   private String refresh;// assuming a token is returned for login
    private GetUserResponse user;
    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getToken() {
       return token;
   }
   public String getRefreshToken() {
       return refresh;
   }
   public void setRefreshToken(String refresh) {
       this.refresh = refresh;
   }
}

