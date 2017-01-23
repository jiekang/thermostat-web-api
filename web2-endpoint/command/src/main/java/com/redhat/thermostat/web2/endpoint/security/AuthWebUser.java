package com.redhat.thermostat.web2.endpoint.security;

import java.util.List;

public class AuthWebUser implements WebUser {
    private final String username;
    private final List<String> roles;
    private final char[] password;

    public AuthWebUser(String username, char[] password, List<String> roles) {
        this.username = username;
        this.roles = roles;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.roles.contains(role);
    }

    public String getPassword() {
        return new String(password);
    }
}
