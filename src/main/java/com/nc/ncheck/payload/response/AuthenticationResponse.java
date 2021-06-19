package com.nc.ncheck.payload.response;

public class AuthenticationResponse {
    private String jwt;
    private String username;
    private Long userId;
    private String avatarPath;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String jwt, String username, Long userId) {
        this.jwt = jwt;
        this.username = username;
        this.userId = userId;
    }

    public AuthenticationResponse(String jwt, String username, Long userId, String avatarPath) {
        this.jwt = jwt;
        this.username = username;
        this.userId = userId;
        this.avatarPath = avatarPath;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
