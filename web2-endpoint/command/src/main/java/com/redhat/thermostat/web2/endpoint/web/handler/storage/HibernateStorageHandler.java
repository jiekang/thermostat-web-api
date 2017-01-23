package com.redhat.thermostat.web2.endpoint.web.handler.storage;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.ChunkedOutput;

public class HibernateStorageHandler implements StorageHandler {
    @Override
    public void getAgent(SecurityContext securityContext, AsyncResponse asyncResponse, String agentId, String count, String sort) {
    }

    @Override
    public Response putAgent(String body, SecurityContext context) {
        return null;
    }

    @Override
    public Response getHostCpuInfo(SecurityContext securityContext, String agentId, String count, String sort, String maxTimestamp, String minTimestamp) {
        return null;
    }

    @Override
    public ChunkedOutput<String> streamHostCpuInfo(SecurityContext securityContext, String agentId) {
        return null;
    }
}
