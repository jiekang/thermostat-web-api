package com.redhat.thermostat.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class ProxyFilter implements ContainerRequestFilter{

    private final UserStore userStore = new UserStore();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String username = requestContext.getHeaderString("X-SSSD-REMOTE-USER");
        if (username == null) {
            throw new NotAuthorizedException("Authentication credentials are required");
        }

        System.out.println("X-SSSD-REMOTE-USER: " + username);
        BasicUser user = userStore.getUser(username);
        if (user == null) {
            throw new NotAuthorizedException("Authentication credentials are required");
        }

        requestContext.setSecurityContext(new BasicSecurityContext(user));
    }
}
