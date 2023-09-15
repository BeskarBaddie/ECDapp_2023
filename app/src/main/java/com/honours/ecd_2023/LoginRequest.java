package com.honours.ecd_2023;
/**
 * Represents a login request with a username and password.
 */
public class LoginRequest {
    private String username;
    private String password;

    /**
     * Creates a new instance of the LoginRequest class with the specified username and password.
     *
     * @param username The user's username.
     * @param password The user's password.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
