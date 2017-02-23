package com.redhat.thermostat.server.core.internal.security.auth.none;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class NoAuthFilterTest {

    private NoAuthFilter noAuthFilter;
    private ContainerRequestContext crq;
    private ArgumentCaptor<SecurityContext> sc = ArgumentCaptor.forClass(SecurityContext.class);
    @Before
    public void setup() throws IOException {
        noAuthFilter = new NoAuthFilter();
        crq = mock(ContainerRequestContext.class);

        noAuthFilter.filter(crq);
    }

    @Test
    public void testGetUserPrincipalName() {
        verify(crq).setSecurityContext(sc.capture());

        assertEquals("user", sc.getValue().getUserPrincipal().getName());
    }

    @Test
    public void testIsUserInRole() {
        verify(crq).setSecurityContext(sc.capture());

        assertEquals(true, sc.getValue().isUserInRole("any-string-works"));
    }

    @Test
    public void testIsSecure() {
        verify(crq).setSecurityContext(sc.capture());

        assertEquals(false, sc.getValue().isSecure());
    }

    @Test
    public void testGetAuthenticationScheme() {
        verify(crq).setSecurityContext(sc.capture());

        assertEquals(null, sc.getValue().getAuthenticationScheme());
    }
}