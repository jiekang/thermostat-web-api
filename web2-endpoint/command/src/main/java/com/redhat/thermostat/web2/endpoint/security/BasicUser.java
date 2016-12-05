package com.redhat.thermostat.web2.endpoint.security;

import java.util.List;

public class BasicUser {
    private final String username;
    private final List<String> roles;
    private final char[] password;

    public BasicUser(String username, char[] password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return this.username;
    }

    public char[] getPassword() {
        return this.password;
    }

    public boolean isUserInRole(String role) {
        return this.roles.contains(role);
    }
}
