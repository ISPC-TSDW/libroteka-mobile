package com.example.libroteka.data;

public class TokenRequest {
    private String email;
    private String password;

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
