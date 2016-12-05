package com.redhat.thermostat.web2.endpoint.security;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ContainerRequestFilter {

    private final UserStore userStore;

    public BasicAuthFilter() {
        this.userStore = new UserStore();
    }

    public BasicAuthFilter(UserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
            String authentication = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authentication == null) {
                throw new NotAuthorizedException("Authentication credentials are required");
            }

            if (!authentication.startsWith("Basic ")) {
                return;
            }

            authentication = authentication.substring("Basic ".length());
            String[] values = new String(DatatypeConverter.parseBase64Binary(authentication),
                    Charset.forName("ASCII")).split(":");
            if (values.length < 2) {
                throw new WebApplicationException(400);
            }

            String username = values[0];
            String password = values[1];

            BasicUser user = userStore.getUser(username);
            if (user == null) {
                throw new NotAuthorizedException("Authentication credentials are required");
            }

            if (!new String(user.getPassword()).equals(password)) {
                throw new NotAuthorizedException("Authentication credentials are required");
            }

            requestContext.setSecurityContext(new BasicSecurityContext(user));
    }
}
