package com.redhat.thermostat.web2.endpoint.security.auth.proxy;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.redhat.thermostat.web2.endpoint.security.WebUser;

public class ProxySecurityContext implements SecurityContext{
    private final WebUser user;

    public ProxySecurityContext(WebUser user) {
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
        return "Proxy";
    }
}
