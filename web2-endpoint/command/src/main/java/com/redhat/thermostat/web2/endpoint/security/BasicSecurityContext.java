package com.redhat.thermostat.web2.endpoint.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class BasicSecurityContext implements SecurityContext {
    private BasicUser user;
    public BasicSecurityContext(BasicUser user) {
        this.user = user;
    }

    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return user.getUsername();
            }
        };
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.isUserInRole(role);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Basic";
    }
}
