package com.redhat.thermostat.web2.endpoint.security.auth.proxy;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.redhat.thermostat.web2.endpoint.security.UserStore;
import com.redhat.thermostat.web2.endpoint.security.WebUser;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ProxyAuthFilter implements ContainerRequestFilter{

    private final UserStore userStore = new UserStore();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String username = requestContext.getHeaderString("X-SSSD-REMOTE-USER");
        if (username == null) {
            throw new NotAuthorizedException("Authentication credentials are required");
        }

        String groups = requestContext.getHeaderString("X-SSSD-REMOTE-GROUPS");
        if (groups != null) {
            System.out.println("GROUPS: " + groups);
        }

        WebUser user = userStore.getUser(username);
        if (user == null) {
            throw new NotAuthorizedException("Authentication credentials are required");
        }

        requestContext.setSecurityContext(new ProxySecurityContext(user));
    }
}
