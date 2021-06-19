package com.nc.ncheck.payload.response;

public class AuthenticationResponse {
    private final String jwt;
    private final String username;
    private final Long userId;

    public AuthenticationResponse(String jwt, String username, Long userId) {
        this.jwt = jwt;
        this.username = username;
        this.userId = userId;
    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }
}
