package com.redhat.thermostat.server.core.web;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.Response;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;

import com.redhat.thermostat.server.core.web.setup.ProxyCoreServerTestSetup;

public class ProxyCoreServerTest extends ProxyCoreServerTestSetup {
    @Test
    public void testGetNoAuthHeader() throws InterruptedException, ExecutionException, TimeoutException {
        String url = baseUrl + "/namespace/systems/systemId";
        ContentResponse getResponse = client.newRequest(url).method(HttpMethod.GET).send();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), getResponse.getStatus());
    }

    @Test
    public void testGetNoGroups() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        String url = baseUrl + "/namespace/systems/systemId";

        ContentResponse getResponse = client.newRequest(url).method(HttpMethod.GET).header("X-SSSD-REMOTE-USER", "user").send();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), getResponse.getStatus());
    }

    @Test
    public void testGetGroupMismatch() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        String url = baseUrl + "/namespace/systems/systemId";

        ContentResponse getResponse = client.newRequest(url).method(HttpMethod.GET).header("X-SSSD-REMOTE-USER", "user").header("X-SSSD-REMOTE-USER-GROUPS", "thermostat-systems-x").send();

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), getResponse.getStatus());
    }


    @Test
    public void testGetGroupMatch() throws InterruptedException, ExecutionException, TimeoutException, URISyntaxException {
        String url = baseUrl + "/namespace/systems/systemId";

        ContentResponse getResponse = client.newRequest(url).method(HttpMethod.GET).header("X-SSSD-REMOTE-USER", "user").header("X-SSSD-REMOTE-USER-GROUPS", "thermostat-namespaces-all:thermostat-systems-systemId").send();

        assertEquals(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), getResponse.getStatus());
    }

}
