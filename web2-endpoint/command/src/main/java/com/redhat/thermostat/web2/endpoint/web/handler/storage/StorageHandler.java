package com.redhat.thermostat.web2.endpoint.web.handler.storage;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.ChunkedOutput;

public interface StorageHandler {
    void getAgent(SecurityContext securityContext,
                  AsyncResponse asyncResponse,
                  String agentId,
                  String count,
                  String sort,
                  String cursor);

    Response putAgent(String body,
                      SecurityContext context);

    Response getHostCpuInfo(SecurityContext securityContext,
                            String agentId,
                            String count,
                            String sort,
                            String maxTimestamp,
                            String minTimestamp);

    ChunkedOutput<String> streamHostCpuInfo(SecurityContext securityContext,
                                            String agentId);
}
